package edu.cnm.deepdive.jata.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.jata.R;
import edu.cnm.deepdive.jata.databinding.FragmentBoardBinding;
import edu.cnm.deepdive.jata.model.Board;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.viewmodel.GameViewModel;
import java.util.LinkedList;
import java.util.List;

public class BoardFragment extends Fragment {

  private static final String TAG = BoardFragment.class.getSimpleName();
  public static final String BOARD_INDEX_KEY = "board_index";

  private FragmentBoardBinding binding;
  private GameViewModel viewModel;
  private int boardIndex;
  private Board board;


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
    binding.gameBoard.setClickListener((gridX, gridY, ship) -> Log.d(TAG, String.format("clicked: %1$d, %2$d, %3$s", gridX, gridY, ship)));
    binding.gameBoard.setLongClickListener(
        this::handleLongClick);
    // TODO: 4/3/2024 attach any listeners as needed.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    viewModel.getGame()
        .observe(getViewLifecycleOwner(), (game) -> {
          binding.gameBoard.setSize(game.getBoardSize());
          board = game.getBoards().get(boardIndex);
          binding.gameBoard.setBoard(board);
        });
  }

  private void handleLongClick(int gridX, int gridY, float viewX, float viewY, Ship ship) {
    Log.d(TAG,
        String.format("longClicked: %1$d, %2$d, %4$f, %5$f, %3$s", gridX, gridY, ship, viewX,
            viewY));
    float widthFraction = (viewX + binding.gameBoard.getX())/ binding.getRoot().getWidth();
    float heightFraction = (viewY + binding.gameBoard.getY())/ binding.getRoot().getHeight();
    binding.horizontalGuideline.setGuidelinePercent(heightFraction);
    binding.verticalGuideline.setGuidelinePercent(widthFraction);
    PopupMenu popupMenu = new PopupMenu(requireContext(), binding.popUpMenuFocus);
    Menu menu = popupMenu.getMenu();
    popupMenu.getMenuInflater().inflate(R.menu.ship_actions, menu);
    menu.findItem(R.id.rotate).setOnMenuItemClickListener(new OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(@NonNull MenuItem item) {
        List<Ship> placement = new LinkedList<>(board.getShips());
        Ship rotatedShip = new Ship(ship.getX(), ship.getY(), ship.getLength(), !ship.isVertical());
        placement.remove(ship);
        placement.add(rotatedShip);
        viewModel.changePlacement(placement, boardIndex);
        return true;
      }
    });
    popupMenu.show();
  }

}
