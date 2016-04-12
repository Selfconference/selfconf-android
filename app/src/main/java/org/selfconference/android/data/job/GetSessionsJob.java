package org.selfconference.android.data.job;

import android.support.annotation.NonNull;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.event.GetSessionsAddEvent;
import org.selfconference.android.data.event.GetSessionsSuccessEvent;
import org.selfconference.android.data.api.model.Session;
import retrofit2.Call;
import retrofit2.Response;

public final class GetSessionsJob extends ApiJob<List<Session>> {
  @NonNull @Override protected Object createAddEvent() {
    return new GetSessionsAddEvent();
  }

  @Override protected Call<List<Session>> apiCall() {
    return api.sessions();
  }

  @Override protected void onApiSuccess(Response<List<Session>> response) {
    ImmutableList<Session> sessions = ImmutableList.copyOf(response.body());
    eventBus.post(new GetSessionsSuccessEvent(sessions));
  }

  @Override protected void onApiFailure(Response<List<Session>> response) {

  }
}