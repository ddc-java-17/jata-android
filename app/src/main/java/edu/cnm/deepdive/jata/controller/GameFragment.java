package edu.cnm.deepdive.jata.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;
import com.google.android.material.tabs.TabLayoutMediator;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.jata.adapter.BoardsAdapter;
import edu.cnm.deepdive.jata.databinding.FragmentGameBinding;
import edu.cnm.deepdive.jata.viewmodel.GameViewModel;
import edu.cnm.deepdive.jata.viewmodel.LoginViewModel;
import edu.cnm.deepdive.jata.viewmodel.PermissionsViewModel;
import edu.cnm.deepdive.jata.viewmodel.PreferencesViewModel;
import edu.cnm.deepdive.jata.viewmodel.UserViewModel;

/**
 * Demonstrates access to and observation of {@link androidx.lifecycle.LiveData} elements in
 * {@link GameViewModel},as well as acting as a navigation placeholder. This fragment is used to implement
 * the viewpager 2 and tabLayoutMediator which allow you to scroll between boards during a game, as well
 * as handling the action of firing shots using the fire button.
 */
@AndroidEntryPoint
public class GameFragment extends Fragment {

  private FragmentGameBinding binding;
  private GameViewModel viewModel;
  private int boardSize;
  private int playerCount;
  private int currentBoardIndex;

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
    binding.playerBoardsHost.registerOnPageChangeCallback(new BoardChangeListener());
    binding.fireButton.setOnClickListener((v) -> viewModel.submitShots());
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
          binding.playerBoardsHost.setCurrentItem(currentBoardIndex);
          new TabLayoutMediator(binding.playerBoards, binding.playerBoardsHost,
              (tab, position) -> tab.setText(
                  game.getBoards().get(position).getPlayer().getDisplayName()))
              .attach();
          if (game.isYourTurn()) {
            binding.fireButton.setVisibility(View.VISIBLE);
            binding.shotsRemaining.setVisibility(View.VISIBLE);
            binding.shotsRemainingLabel.setVisibility(View.VISIBLE);
          } else {
            binding.fireButton.setVisibility(View.INVISIBLE);
            binding.shotsRemaining.setVisibility(View.INVISIBLE);
            binding.shotsRemainingLabel.setVisibility(View.INVISIBLE);
          }
        });
    viewModel.getShotsRemaining()
        .observe(getViewLifecycleOwner(),
            (remaining) -> binding.shotsRemaining.setText(String.valueOf(remaining)));
    }

  private class BoardChangeListener extends OnPageChangeCallback {

    @Override
    public void onPageSelected(int position) {
      super.onPageSelected(position);
      currentBoardIndex = position;
    }

  }

}
