package edu.cnm.deepdive.jata.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.cnm.deepdive.jata.R;
import edu.cnm.deepdive.jata.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

  private FragmentHomeBinding binding;

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
    binding.startGame.setOnClickListener((game) -> onStart());
    // TODO: 4/4/2024 attach click listener to button to start game.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

}
