package edu.cnm.deepdive.jata.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.jata.databinding.FragmentBoardBinding;
import edu.cnm.deepdive.jata.viewmodel.GameViewModel;
import java.util.List;

public class BoardFragment extends Fragment {

  public static final String BOARD_INDEX_KEY = "board_index";

  private FragmentBoardBinding binding;
  private GameViewModel viewModel;
  private int boardIndex;


  @Override
  public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    boardIndex = getArguments().getInt(BOARD_INDEX_KEY);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = FragmentBoardBinding.inflate(inflater, container, false);
    // TODO: 4/3/2024 attach any listeners as needed.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // TODO: 4/3/2024 connect to viewmodels as needed and observe live data.
    //  - when observer is executed when game is updated, invoke the following
    //    - binding.gameBoard.setSize(game.getSize())
    //    - binding.gameBoard.setBoard(game.getBoards().get(boardIndex));
    viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    viewModel.getGame()
        .observe(getViewLifecycleOwner(), (game) -> {
          binding.gameBoard.setSize(game.getBoardSize());
          binding.gameBoard.setBoard(game.getBoards().get(boardIndex));
        });
  }
}
