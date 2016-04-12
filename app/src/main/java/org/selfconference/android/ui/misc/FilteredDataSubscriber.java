package org.selfconference.android.ui.misc;

import android.support.v7.widget.RecyclerView;
import rx.Subscriber;

public final class FilteredDataSubscriber<T, VH extends RecyclerView.ViewHolder>
    extends Subscriber<T> {

  private final FilterableAdapter<T, VH> filterableAdapter;

  public FilteredDataSubscriber(FilterableAdapter<T, VH> filterableAdapter) {
    this.filterableAdapter = filterableAdapter;
  }

  @Override public void onCompleted() {
    filterableAdapter.notifyDataSetChanged();
  }

  @Override public void onError(Throwable e) {

  }

  @Override public void onNext(T item) {
    filterableAdapter.getFilteredData().add(item);
  }
}