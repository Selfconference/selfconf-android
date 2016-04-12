package org.selfconference.android.ui.session;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import java.util.Locale;
import org.selfconference.android.data.pref.SessionPreferences;
import org.selfconference.android.ui.misc.ButterKnifeViewHolder;
import org.selfconference.android.ui.misc.FilterableAdapter;
import org.selfconference.android.ui.misc.FilteredDataSubscriber;
import org.selfconference.android.R;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.ui.decorator.DateTimeDecorator;
import rx.functions.Func1;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SessionAdapter extends FilterableAdapter<Session, SessionAdapter.SessionViewHolder> {
  public interface OnSessionClickListener {
    void onSessionClick(Session event);
  }

  private final SessionPreferences preferences;

  private OnSessionClickListener onSessionClickListener;

  public SessionAdapter(SessionPreferences preferences) {
    super();
    this.preferences = preferences;
  }

  public void setOnSessionClickListener(OnSessionClickListener onSessionClickListener) {
    this.onSessionClickListener = onSessionClickListener;
  }

  public void refresh() {
    notifyDataSetChanged();
  }

  @Override public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.include_session_row, parent, false);
    return new SessionViewHolder(view);
  }

  @Override public void onBindViewHolder(SessionViewHolder holder, int position) {
    Session session = getFilteredData().get(position);
    DateTimeDecorator dateTimeDecorator = DateTimeDecorator.fromDateTime(session.beginning());

    holder.itemView.setOnClickListener(v -> {
      if (onSessionClickListener != null) {
        onSessionClickListener.onSessionClick(session);
      }
    });

    holder.favoriteSessionIndicator.setVisibility(preferences.isFavorite(session) ? VISIBLE : GONE);

    holder.sessionTitle.setText(session.title());
    holder.sessionSubtitle.setText(session.room().name());
    holder.startTime.setText(dateTimeDecorator.shortTimeString());
  }

  public void filterFavorites(boolean show) {
    getFilteredData().clear();
    dataObservable() //
        .filter(session -> !show || preferences.isFavorite(session)) //
        .subscribe(new FilteredDataSubscriber<>(this));
  }

  @Override protected Func1<Session, Boolean> filterPredicate(String query) {
    return session -> session.title() //
        .toLowerCase(Locale.US) //
        .contains(query.toLowerCase(Locale.US));
  }

  static final class SessionViewHolder extends ButterKnifeViewHolder {

    @Bind(R.id.start_time) TextView startTime;
    @Bind(R.id.slot_title) TextView sessionTitle;
    @Bind(R.id.slot_subtitle) TextView sessionSubtitle;
    @Bind(R.id.favorite_session_indicator) View favoriteSessionIndicator;

    SessionViewHolder(View itemView) {
      super(itemView);
    }
  }
}