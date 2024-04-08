package edu.cnm.deepdive.jata.controller;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.jata.R;

/**
 * Informs the winning user that they have won.
 */
@AndroidEntryPoint
public class WinDialogueFragment extends DialogFragment {

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    return new AlertDialog.Builder(requireContext())
        .setTitle("You WIN!")
        .setMessage("The Golden Fleece is yours!")
        .setNeutralButton("Home", (dlg, which) -> {
          NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
          navController.navigate(WinDialogueFragmentDirections.navigateToHome());
        })
        .create();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }
}