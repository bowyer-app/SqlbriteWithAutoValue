package com.bowyer.app.playermanage.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import com.bowyer.app.playermanage.R;
import com.bowyer.app.playermanage.database.dto.Rank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankSelectDialogFragment extends DialogFragment {

  public interface OnRankSelectListener {
    void onRankSelect(String rank);
  }

  public static RankSelectDialogFragment newInstance() {
    return new RankSelectDialogFragment();
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    super.onCreateDialog(savedInstanceState);
    Activity activity = getActivity();

    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

    dialog.setTitle(activity.getString(R.string.title_player_rank_select));
    dialog.setPositiveButton(activity.getString(R.string.player_rank_select_cancel),
        (dialogInterface, i) -> {
          dismiss();
        });

    List<Rank> rankList = Arrays.asList(Rank.values());
    ArrayList<String> list = new ArrayList<>(rankList.size());
    //ランク無しの選択肢を入れておく
    list.add(getString(R.string.player_no_rank));
    for (int i = 1; i < rankList.size(); i++) {
      list.add(rankList.get(i).getRank());
    }

    CharSequence[] cs = list.toArray(new CharSequence[list.size()]);
    dialog.setSingleChoiceItems(cs, 0, (dialogInterface, i) -> {
      if (activity instanceof OnRankSelectListener) {
        ((OnRankSelectListener) activity).onRankSelect(list.get(i));
        dismiss();
      }
    });

    return dialog.create();
  }
}
