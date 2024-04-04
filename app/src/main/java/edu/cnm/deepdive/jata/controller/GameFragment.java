package edu.cnm.deepdive.jata.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.jata.databinding.FragmentGameBinding;

@AndroidEntryPoint
public class GameFragment extends Fragment {

  private FragmentGameBinding binding;
  // TODO: 4/4/2024 declare fields for viewmodel
  private int boardSize;
  private int playerCount;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // where usually we read arguments that have been passed in.
    GameFragmentArgs gameFragmentArgs = GameFragmentArgs.fromBundle(getArguments());
    boardSize = gameFragmentArgs.getBoardSize();
    playerCount = gameFragmentArgs.getPlayerCount();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentGameBinding.inflate(inflater, container, false);
    // where we add listeners and configure view widgets.
    binding.boardSize.setText(String.valueOf(boardSize));
    binding.playerCount.setText(String.valueOf(playerCount));
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // where we connect to viewmodels, create observers, trigger actions
  }
}
