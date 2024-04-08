package edu.cnm.deepdive.jata.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import edu.cnm.deepdive.jata.R;
import edu.cnm.deepdive.jata.model.Board;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.model.Shot;

/**
 * This view class extends the View class and implements OnTouchListener. This class handles
 * everything that happens on the board. It holds all the drawables for the board itself, the ships,
 * and the shots, as well as the methods to draw them on the board.
 */
public class BoardView extends View implements OnTouchListener {

  private static final long LONG_CLICK_DURATION = ViewConfiguration.getLongPressTimeout();
  private static final int MAX_CLICK_RADIUS = 15;
  public static final int DRAWING_INSET = 20;

  private Board board;
  private int size;
  private boolean[][] shots;
  private Paint gridPaint;
  private Paint shipPaint;
  private Paint selectedShipPaint;
  private Paint pendingShotPaint;
  private float downX;
  private float downY;
  private long downTime;
  private OnClickListener clickListener;
  private OnLongClickListener longClickListener;
  private float cellWidth;
  private float cellHeight;
  private Drawable smallShip;
  private Drawable mediumShip;
  private Drawable largeShip;
  private Drawable hit;
  private Drawable miss;

  {
    gridPaint = new Paint();
    gridPaint.setColor(Color.GRAY);
    gridPaint.setStrokeWidth(2);
    shipPaint = new Paint();
    shipPaint.setColor(Color.MAGENTA);
    shipPaint.setStyle(Style.FILL);
    selectedShipPaint = new Paint();
    selectedShipPaint.setColor(Color.BLACK);
    selectedShipPaint.setStyle(Style.STROKE);
    selectedShipPaint.setStrokeWidth(4);
    pendingShotPaint = new Paint();
    pendingShotPaint.setColor(Color.RED);
    pendingShotPaint.setStyle(Style.FILL);
  }

  /**
   * A constructor for the BoardView.
   *
   * @param context Context
   */
  public BoardView(Context context) {
    super(context);
    loadResources(context);
  }

  /**
   * Another constructor, with an extra parameter.
   *
   * @param context Context.
   * @param attrs   AttributeSet.
   */
  public BoardView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    loadResources(context);
  }

  /**
   * Another constructor, this one implementing more parameters.
   *
   * @param context      Context.
   * @param attrs        AttributeSet.
   * @param defStyleAttr int.
   */
  public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    loadResources(context);
  }

  /**
   * A fourth constructor, this one with one additional parameter.
   *
   * @param context      Context.
   * @param attrs        AttributeSet.
   * @param defStyleAttr int.
   * @param defStyleRes  int.
   */
  public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    loadResources(context);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = getSuggestedMinimumWidth();
    int height = getSuggestedMinimumHeight();
    width =
        resolveSizeAndState(getPaddingLeft() + getPaddingRight() + width, widthMeasureSpec, 0);
    height =
        resolveSizeAndState(getPaddingTop() + getPaddingBottom() + height, heightMeasureSpec, 0);
    int size = Math.min(width, height);
    setMeasuredDimension(size, size);
    cellWidth = (float) width / this.size;
    cellHeight = (float) height / this.size;
  }

  @Override
  protected void onDraw(@NonNull Canvas canvas) {
    super.onDraw(canvas);
    update();
    if (board != null && size > 0) {
      Context context = getContext();
      loadResources(context);
      drawGrid(canvas);
      if (board.isMine()) {
        drawShips(canvas);
      }
      drawShots(canvas);
      drawPendingShots(canvas);
    }
  }

  /**
   * Sets the board for a game.
   *
   * @param board Board.
   */
  public void setBoard(Board board) {
    this.board = board;
    update();
  }

  /**
   * Sets the boardSize.
   *
   * @param size int.
   */
  public void setSize(int size) {
    this.size = size;
    update();
  }

  /**
   * Sets the shots on a board.
   *
   * @param shots boolean[][]
   */
  public void setShots(boolean[][] shots) {
    this.shots = shots;
  }

  private void update() {
    if (size > 0) {
      float width = getWidth();
      float height = getHeight();
      cellWidth = width / size;
      cellHeight = height / size;
      postInvalidate();
      setOnTouchListener(this);
    }
  }

  /**
   * Sets the long click listener used to get the pop-up menu for moving ships.
   *
   * @param longClickListener onLongClickListener.
   */
  public void setLongClickListener(
      OnLongClickListener longClickListener) {
    this.longClickListener = longClickListener;
  }

  /**
   * Sets the click listener used to place and toggle shots.
   *
   * @param clickListener OnClickListener.
   */
  public void setClickListener(OnClickListener clickListener) {
    this.clickListener = clickListener;
  }

  private boolean isCloseEnough(MotionEvent event) {
    return Math.hypot(event.getX() - downX, event.getY() - downY) < MAX_CLICK_RADIUS;
  }

  private void drawGrid(Canvas canvas) {
    float width = getWidth();
    float height = getHeight();
    for (int lineIndex = 0; lineIndex <= size; lineIndex++) {
      canvas.drawLine(0, lineIndex * cellHeight, width, lineIndex * cellHeight, gridPaint);
      canvas.drawLine(lineIndex * cellWidth, 0, lineIndex * cellWidth, height, gridPaint);
    }
  }

  private void drawShips(Canvas canvas) {
    // TODO: 4/3/2024 invoke drawable.draw (canvas)  to draw ships on canvas.
    for (Ship ship : board.getShips()) {
      int x = ship.getX();
      float left = (x - 1) * cellWidth;
      int y = ship.getY();
      float top = (y - 1) * cellHeight;
      float right;
      float bottom;
      boolean vertical = ship.isVertical();
      int length = ship.getLength();
      if (vertical) {
        right = x * cellWidth;
        bottom = (y - 1 + length) * cellHeight;
      } else {
        right = (x - 1 + length) * cellWidth;
        bottom = y * cellHeight;
      }
//      right -= 20;
//      bottom -= 20;
//      canvas.drawRoundRect(left + DRAWING_INSET, top + DRAWING_INSET, right - DRAWING_INSET, bottom - DRAWING_INSET, DRAWING_INSET, DRAWING_INSET, shipPaint);
//      if (ship.isSelected()) {
//        canvas.drawRoundRect(left + DRAWING_INSET, top + DRAWING_INSET, right - DRAWING_INSET, bottom - DRAWING_INSET, DRAWING_INSET, DRAWING_INSET, selectedShipPaint);
//      }
      // TODO: 4/3/2024 use the ships x and y coords and orientation to select drawable to set bounds
      Drawable shipDrawable = switch (ship.getLength()) {
        case 2 -> smallShip;
        case 3 -> mediumShip;
        case 4 -> largeShip;
        default -> null;
      };
      //noinspection DataFlowIssue
      shipDrawable.setBounds((int) left + DRAWING_INSET, (int) top + DRAWING_INSET, (int) right - DRAWING_INSET, (int) bottom - DRAWING_INSET);
      shipDrawable.draw(canvas);
    }

  }

  private void drawShots(Canvas canvas) {
    for (Shot shot : board.getShots()) {
      int x = shot.getX();
      float left = (x - 1) * cellWidth;
      int y = shot.getY();
      float top = (y - 1) * cellHeight;
      float right = left + cellWidth;
      float bottom = top + cellHeight;
      Drawable shotDrawable = shot.isHit() ? hit : miss;
      shotDrawable.setBounds((int) left + DRAWING_INSET,(int) top + DRAWING_INSET,(int) right - DRAWING_INSET,(int) bottom - DRAWING_INSET);
     shotDrawable.draw(canvas);
    }
  }

  private void drawPendingShots(Canvas canvas) {
    if (shots != null) {
      for (int rowIndex = 0; rowIndex < shots.length; rowIndex++) {
        for (int columnIndex = 0; columnIndex < shots[rowIndex].length; columnIndex++) {
          float centerY = (rowIndex + 0.5F) * cellHeight;
          if (shots[rowIndex][columnIndex]) {
            float centerX = (columnIndex + 0.5F) * cellWidth;
            canvas.drawCircle(centerX, centerY, cellWidth * 0.4F, pendingShotPaint);
          }
        }
      }
    }
  }

  private void loadResources(Context context) {
    smallShip = AppCompatResources.getDrawable(context, R.drawable.canoe);
    mediumShip = AppCompatResources.getDrawable(context, R.drawable.galley);
    largeShip = AppCompatResources.getDrawable(context, R.drawable.trireme);
    hit = AppCompatResources.getDrawable(context, R.drawable.hit);
    miss = AppCompatResources.getDrawable(context, R.drawable.miss);
  }

  private Ship shipAt(int gridX, int gridY) {
    if (board.isMine()) {
      return board.getShips()
          .stream()
          .filter((ship) -> ship.includesPoint(gridX, gridY))
          .findFirst()
          .orElse(null);
    } else {
      return null;
    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    boolean handled = false;
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      downX = event.getX();
      downY = event.getY();
      downTime = event.getEventTime();
      handled = true;
    } else if (event.getAction() == MotionEvent.ACTION_UP && isCloseEnough(event)) {
      long duration = event.getEventTime() - downTime;
      int gridX = (int) (downX / cellWidth) + 1;
      int gridY = (int) (downY / cellHeight) + 1;
      Ship ship = shipAt(gridX, gridY);
      if (duration < LONG_CLICK_DURATION) {
        if (clickListener != null) {
          clickListener.onClick(gridX, gridY, ship);
        }
      } else if (longClickListener != null && ship != null) {
        longClickListener.onLongClick(gridX, gridY, event.getX(), event.getY(), ship);
      }

      handled = true;
    }
    return handled;
  }

  /**
   * A nested Interface used to set the click listener for toggling shot placement.
   */
  public interface OnClickListener {

    void onClick(int gridX, int gridY, Ship ship);
  }

  /**
   * A nested interface used to set the long click listener used to invoke pop-up menu used to move
   * ships during ship placement.
   */
  public interface OnLongClickListener {

    void onLongClick(int gridX, int gridY, float viewX, float viewY, Ship ship);
  }
}
