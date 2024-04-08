package edu.cnm.deepdive.jata.controller;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.jata.R;
import edu.cnm.deepdive.jata.viewmodel.GameViewModel;

/**
 * This LoseDialogFragment extends DialogFragment and makes a pop-up occur whenever you lose a game,
 * with an option to navigate back to the home screen to start a new game.
 */
public class LoseDialogFragment extends DialogFragment {

  private GameViewModel viewModel;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    return new AlertDialog.Builder(requireContext())

        .setTitle("YOU LOSE!")
        .setMessage("Fleet has been sunk! You have failed your crew")
        .setNeutralButton("Home", (dlg, which) -> {
          NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
          navController.navigate(LoseDialogFragmentDirections.navigateToHome());
        })
        .create();

  }

}
