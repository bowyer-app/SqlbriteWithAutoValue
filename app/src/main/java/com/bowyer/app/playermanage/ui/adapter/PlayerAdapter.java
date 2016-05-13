package com.bowyer.app.playermanage.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.database.dto.Player;
import com.bowyer.app.playermanage.database.dto.Sex;
import com.bowyer.app.playermanage.ui.activity.PlayerManageActivity;
import java.util.Collections;
import java.util.List;
import rx.functions.Action1;

public class PlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements Action1<List<Player>> {

  private final LayoutInflater inflater;

  private List<Player> items = Collections.emptyList();

  @Override public void call(List<Player> items) {
    this.items = items;
    notifyDataSetChanged();
  }

  public Player getItem(int positon) {
    return items.get(positon);
  }

  public PlayerAdapter(Context context) {
    this.inflater = LayoutInflater.from(context);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = inflater.inflate(R.layout.player_list_item, parent, false);
    return new ViewHolder(v);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    final ViewHolder holder = (ViewHolder) viewHolder;
    Player player = getItem(position);
    String lastNameText = player.lastName();
    TextView initial = holder.mInitial;

    if (!TextUtils.isEmpty(lastNameText)) {
      initial.setText(lastNameText.substring(0, 1));
    }
    holder.mFirstName.setText(player.firstName());
    holder.mLastName.setText(lastNameText);
    holder.mRank.setText(player.rank());

    if (player.sex() == Sex.FEMALE.getSex()) {
      initial.setBackground(
          ContextCompat.getDrawable(initial.getContext(), R.drawable.female_initial));
    } else {
      initial.setBackground(
          ContextCompat.getDrawable(initial.getContext(), R.drawable.male_initial));
    }
  }

  @Override public int getItemCount() {
    return items.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.initial) TextView mInitial;
    @Bind(R.id.first_name) TextView mFirstName;
    @Bind(R.id.last_name) TextView mLastName;
    @Bind(R.id.rank_text) TextView mRank;

    @OnClick(R.id.root_view) void onItemClick(View view) {
      int position = getLayoutPosition();
      Player player = getItem(position);
      PlayerManageActivity.startActivity(view.getContext(), player);
    }

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
