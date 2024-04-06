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

public class BoardView extends View implements OnTouchListener {

  private static final long LONG_CLICK_DURATION = ViewConfiguration.getLongPressTimeout();
  private static final int MAX_CLICK_RADIUS = 15;

  private Board board;
  private int size;
  private Paint gridPaint;
  private Paint shipPaint;
  private Paint selectedShipPaint;
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
  }

  public BoardView(Context context) {
    super(context);
    loadResources(context);
  }

  public BoardView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    loadResources(context);
  }

  public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    loadResources(context);
  }

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
      drawShips(canvas);
      drawShots(canvas);
    }
  }

  public void setBoard(Board board) {
    this.board = board;
    update();
  }

  public void setSize(int size) {
    this.size = size;
    update();
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

  private boolean isCloseEnough(MotionEvent event) {
    return Math.hypot(event.getX() - downX, event.getY() - downY) < MAX_CLICK_RADIUS;
  }

  public void setClickListener(OnClickListener clickListener) {
    this.clickListener = clickListener;
  }

  public void setLongClickListener(
      OnLongClickListener longClickListener) {
    this.longClickListener = longClickListener;
  }

  private void drawGrid(Canvas canvas) {
    float width = getWidth();
    float height = getHeight();
    for (int lineIndex = 0; lineIndex <= size; lineIndex++) {
      canvas.drawLine(0, lineIndex * cellHeight, width, lineIndex * cellHeight, gridPaint);
      canvas.drawLine(lineIndex * cellWidth, 0, lineIndex * cellWidth, height, gridPaint);
    }
    // TODO: 4/3/2024 draw using canvas.drawlines
  }

  private void drawShips(Canvas canvas) {
    // TODO: 4/3/2024 invoke drawable.draw (canvas)  to draw ships on canvas.
    for (Ship ship : board.getShips()) {
      int x = ship.getX();
      float left = (x - 1) * cellWidth + 20;
      int y = ship.getY();
      float top = (y - 1) * cellHeight + 20;
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
      right -= 20;
      bottom -= 20;
      canvas.drawRoundRect(left, top, right, bottom, 20, 20, shipPaint);
      if (ship.isSelected()) {
        canvas.drawRoundRect(left, top, right, bottom, 20, 20, selectedShipPaint);
      }
      // TODO: 4/3/2024 use the ships x and y coords and orientation to select drawable to set bounds
//    largeShip.setBounds((int) ((x - 1) * cellWidth), (int) ((y -1) * cellHeight),
//        (int) ((x - 1 + length) * cellWidth), (int) cellHeight);
//    largeShip.draw(canvas);
    }

  }

  private void drawShots(Canvas canvas) {
    // TODO: 4/3/2024 use drawable.draw (canvas) to draw each shot on the canvas.
    for (Shot shot : board.getShots()) {
      // TODO: 4/3/2024 use shot x and y coords and isHit method to pick a drawable and set its bounds.
      // TODO: 4/3/2024 invoke draw method on drawable
    }
  }

  private void loadResources(Context context) {
    smallShip = AppCompatResources.getDrawable(context, R.drawable.small);
    mediumShip = AppCompatResources.getDrawable(context, R.drawable.medium);
    largeShip = AppCompatResources.getDrawable(context, R.drawable.large);
    hit = AppCompatResources.getDrawable(context, R.drawable.hit);
    miss = AppCompatResources.getDrawable(context, R.drawable.miss);
  }

  private Ship shipAt(int gridX, int gridY) {
    return board.getShips()
        .stream()
        .filter((ship) -> ship.includesPoint(gridX, gridY))
        .findFirst()
        .orElse(null);
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

  public interface OnClickListener {
    void onClick(int gridX, int gridY, Ship ship);
  }

  public interface OnLongClickListener {
    void onLongClick(int gridX, int gridY, float viewX, float viewY, Ship ship);
  }
}
