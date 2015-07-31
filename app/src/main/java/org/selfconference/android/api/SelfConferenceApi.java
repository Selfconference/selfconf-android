package org.selfconference.android.api;

import java.util.List;
import javax.inject.Inject;
import org.joda.time.Interval;
import org.selfconference.android.App;
import org.selfconference.android.feedback.Feedback;
import org.selfconference.android.feedback.FeedbackRequest;
import org.selfconference.android.session.Day;
import org.selfconference.android.session.Session;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.sponsors.Sponsor;
import retrofit.client.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

import static org.selfconference.android.utils.DateTimeHelper.intervalForDay;

public final class SelfConferenceApi implements Api {

  @Inject SelfConferenceClient client;

  public SelfConferenceApi() {
    App.getInstance().inject(this);
  }

  @Override public Observable<List<Session>> getSessions() {
    return client.getSessions();
  }

  @Override public Observable<List<Speaker>> getSpeakers() {
    return client.getSpeakers();
  }

  @Override public Observable<List<Sponsor>> getSponsors() {
    return client.getSponsors();
  }

  @Override
  public Observable<Response> submitFeedback(final Session session, final Feedback feedback) {
    return client.submitFeedback(session.getId(), new FeedbackRequest(feedback));
  }

  @Override public Observable<List<Session>> getSessionsByDay(final Day day) {
    return getSessions() //
        .flatMap(new Func1<List<Session>, Observable<Session>>() {
          @Override public Observable<Session> call(List<Session> list) {
            return Observable.from(list);
          }
        }) //
        .filter(new Func1<Session, Boolean>() {
          @Override public Boolean call(Session session) {
            final Interval interval = intervalForDay(day);
            return interval.contains(session.getBeginning());
          }
        }) //
        .toSortedList(sortByDate());
  }

  @Override public Observable<Session> getSessionById(final int id) {
    return client.getSessionById(id);
  }

  private static Func2<Session, Session, Integer> sortByDate() {
    return new Func2<Session, Session, Integer>() {
      @Override public Integer call(Session session, Session session2) {
        return session.getBeginning().compareTo(session2.getBeginning());
      }
    };
  }
}
