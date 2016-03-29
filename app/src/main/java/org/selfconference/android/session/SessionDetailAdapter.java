package org.selfconference.android.session;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.util.List;
import org.selfconference.android.ButterKnifeViewHolder;
import org.selfconference.android.R;

public final class SessionDetailAdapter
    extends RecyclerView.Adapter<SessionDetailAdapter.ViewHolder> {

  private final List<SessionDetail> sessionDetails;

  public SessionDetailAdapter(List<SessionDetail> sessionDetails) {
    // TODO the constructor parameter is not null, so no need to wrap it in an Optional
    this.sessionDetails =
        Optional.fromNullable(sessionDetails).or(Lists.<SessionDetail>newArrayList());
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.include_session_detail_item, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    final SessionDetail sessionDetail = sessionDetails.get(position);

    holder.icon.setImageDrawable(sessionDetail.drawable());
    holder.title.setText(sessionDetail.info());
  }

  @Override public int getItemCount() {
    return sessionDetails.size();
  }

  static final class ViewHolder extends ButterKnifeViewHolder {

    @Bind(R.id.row_icon) ImageView icon;
    @Bind(R.id.row_title) TextView title;

    ViewHolder(View itemView) {
      super(itemView);
    }
  }
}
