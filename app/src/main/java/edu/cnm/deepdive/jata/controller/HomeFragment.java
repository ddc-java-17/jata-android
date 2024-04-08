package edu.cnm.deepdive.jata.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.jata.R;
import edu.cnm.deepdive.jata.databinding.FragmentHomeBinding;
import edu.cnm.deepdive.jata.viewmodel.GameViewModel;
import edu.cnm.deepdive.jata.viewmodel.LoginViewModel;
import edu.cnm.deepdive.jata.viewmodel.PermissionsViewModel;
import edu.cnm.deepdive.jata.viewmodel.PreferencesViewModel;
import edu.cnm.deepdive.jata.viewmodel.UserViewModel;

/**
 * Demonstrates access to and observation of {@link androidx.lifecycle.LiveData} elements in
 * {@link GameViewModel}, as well as acting as a navigation placeholder. This fragment inflates the home
 * screen for the app, and holds the settings for boardSize and player count for the user to set. It also
 * has a start game button which navigates you to the GameFragment.
 */
@AndroidEntryPoint
public class HomeFragment extends Fragment {

  private FragmentHomeBinding binding;
  private NavController navController;
  private GameViewModel viewModel;
  private int boardSizeValue;
  private int playerCountValue;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentHomeBinding.inflate(inflater, container, false);
    String[] boardSizeNames = getResources().getStringArray(R.array.board_size_names);
    int[] boardSizeValues = getResources().getIntArray(R.array.board_size_values);
    binding.boardSize.setAdapter(new ArrayAdapter<>(
        requireContext(), android.R.layout.simple_dropdown_item_1line, boardSizeNames));
    String[] playerCount = getResources().getStringArray(R.array.player_count);
    binding.playerCount.setAdapter(new ArrayAdapter<>(
        requireContext(), android.R.layout.simple_dropdown_item_1line, playerCount));
    // TODO: 4/4/2024 attach click listener to button to start game.
    binding.startGame.setOnClickListener((v) -> {
      boardSizeValue = boardSizeValues[binding.boardSize.getSelectedItemPosition()];
      playerCountValue = Integer.parseInt((String) binding.playerCount.getSelectedItem());
      viewModel.startGame(boardSizeValue, playerCountValue);
    });
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(view);
    viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    viewModel.getGame()
        .observe(getViewLifecycleOwner(), (game) -> {
          if (game != null) {
            navController.navigate(HomeFragmentDirections.navigateToGame(boardSizeValue, playerCountValue));
          }
        });
  }

}
