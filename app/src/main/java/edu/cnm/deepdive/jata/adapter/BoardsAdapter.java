package edu.cnm.deepdive.jata.adapter;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import edu.cnm.deepdive.jata.controller.BoardFragment;
import edu.cnm.deepdive.jata.model.Game;

public class BoardsAdapter extends FragmentStateAdapter {

  private final int size;

  public BoardsAdapter(@NonNull Fragment fragment, Game game) {
    super(fragment);
    size = game.getBoards().size();
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    Fragment fragment = new BoardFragment();
    Bundle bundle = new Bundle();
    bundle.putInt(BoardFragment.BOARD_INDEX_KEY, position);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public int getItemCount() {
    return size;
  }
}
