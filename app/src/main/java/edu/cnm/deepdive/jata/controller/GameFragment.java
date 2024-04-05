package edu.cnm.deepdive.jata.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.tabs.TabLayout.Tab;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.jata.adapter.BoardsAdapter;
import edu.cnm.deepdive.jata.databinding.FragmentGameBinding;
import edu.cnm.deepdive.jata.viewmodel.GameViewModel;

@AndroidEntryPoint
public class GameFragment extends Fragment {

  private FragmentGameBinding binding;
  private GameViewModel viewModel;
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

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    viewModel.getGame()
        .observe(getViewLifecycleOwner(), (game) -> {
          BoardsAdapter adapter = new BoardsAdapter(this, game);
          binding.playerBoardsHost.setAdapter(adapter);
          new TabLayoutMediator(binding.playerBoards, binding.playerBoardsHost,
              (tab, position) -> tab.setText(
                  game.getBoards().get(position).getPlayer().getDisplayName()))
              .attach();
        });
    viewModel.pollGame();
  }
}
