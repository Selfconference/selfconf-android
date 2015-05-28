package org.selfconference.android.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;

import org.selfconference.android.BaseListFragment;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.api.Api;
import org.selfconference.android.api.ApiRequestSubscriber;
import org.selfconference.android.session.SessionAdapter.OnSessionClickListener;
import org.selfconference.android.utils.rx.Transformers;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.Observable;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;
import static rx.android.app.AppObservable.bindFragment;

public class SessionListFragment extends BaseListFragment implements OnSessionClickListener, OnRefreshListener {
    private static final String EXTRA_DAY =
            "org.selfconference.android.session.SessionListFragment.EXTRA_DAY";

    @InjectView(R.id.schedule_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.schedule_item_recycler_view) RecyclerView scheduleItemRecyclerView;

    @Inject Api api;
    @Inject SessionPreferences sessionPreferences;

    private final SessionAdapter sessionAdapter = new SessionAdapter();
    private Day day;
    private MenuItem favoritesItem;

    public static SessionListFragment newInstance(Day day) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DAY, day);
        final SessionListFragment fragment = new SessionListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public SessionListFragment() {
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(this);

        sessionAdapter.setOnSessionClickListener(this);

        scheduleItemRecyclerView.setAdapter(sessionAdapter);
        scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        day = checkNotNull((Day) getArguments().getSerializable(EXTRA_DAY));

        fetchData();
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (sessionPreferences.hasFavorites()) {
            inflater.inflate(R.menu.favorites, menu);
            favoritesItem = menu.findItem(R.id.action_favorites);
            favoritesItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                    item.setChecked(!item.isChecked());
                    sessionAdapter.filterFavorites(item.isChecked());
                    return true;
                }
            });
        }
    }

    @Override public void onResume() {
        super.onResume();
        sessionAdapter.refresh();
        ((AppCompatActivity) getActivity()).getSupportActionBar().invalidateOptionsMenu();
    }

    @Override protected int layoutResId() {
        return R.layout.fragment_schedule_item;
    }

    @Override protected FilterableAdapter getFilterableAdapter() {
        return sessionAdapter;
    }

    @Override public void onSessionClick(Session session) {
        final Intent intent = SessionDetailsActivity.newIntent(getActivity(), session);
        getActivity().startActivity(intent);
    }

    @Override public void onRefresh() {
        fetchData();
    }

    private void fetchData() {
        final Observable<List<Session>> getSessionsByDay = bindFragment(this, api.getSessionsByDay(day))
                .compose(Transformers.<List<Session>>setRefreshing(swipeRefreshLayout));
        addSubscription(getSessionsByDay.subscribe(new SessionListSubscriber(sessionAdapter)));
    }

    private static final class SessionListSubscriber extends ApiRequestSubscriber<List<Session>> {

        private final WeakReference<SessionAdapter> sessionAdapter;

        public SessionListSubscriber(SessionAdapter sessionAdapter) {
            super();
            this.sessionAdapter = new WeakReference<>(sessionAdapter);
        }

        @Override public void onError(Throwable e) {
            super.onError(e);
            Timber.e(e, "Schedule failed to load");
        }

        @Override public void onNext(List<Session> sessions) {
            final SessionAdapter sessionAdapter = this.sessionAdapter.get();
            if (sessionAdapter != null) {
                sessionAdapter.setData(sessions);
            }
        }
    }
}
