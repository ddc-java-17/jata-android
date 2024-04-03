package edu.cnm.deepdive.jata.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import edu.cnm.deepdive.jata.R;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.model.Shot;
import edu.cnm.deepdive.jata.model.entity.Board;

public class BoardView extends View {

  private Board board;
  private int size;
  private Paint gridPaint;
  private MotionEvent downEvent;
  private OnClickListener listener;
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
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onDraw(@NonNull Canvas canvas) {
    super.onDraw(canvas);
    Context context = getContext();
    loadResources(context);
    drawGrid(canvas);
    drawShips(canvas);
    drawShots(canvas);
  }

  public void setBoard(Board board) {
    this.board = board;
    postInvalidate();
  }

  public void setSize(int size) {
    this.size = size;
    float width = getWidth();
    float height = getHeight();
    cellWidth = width / size;
    cellHeight = height / size;
    if (size > 0) {
      setOnTouchListener(new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
          if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downEvent = event;
          } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (
                Math.hypot(event.getX() - downEvent.getX(), event.getY() - downEvent.getY()) < 5
                    && listener != null) {
              listener.onClick((int)(downEvent.getY() / cellHeight), (int)(downEvent.getX() / cellWidth));
            }
          }
          return true;
        }
      });
    }
  }

  public void setListener(OnClickListener listener) {
    this.listener = listener;
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
      // TODO: 4/3/2024 use the ships x and y coords and orientation to select drawable to set bounds
    largeShip.setBounds(0, 0, (int) (4 * cellWidth), (int) cellHeight);
    largeShip.draw(canvas);
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

  public interface OnClickListener {

    void onClick(int row, int column);
  }
}
