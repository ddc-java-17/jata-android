package edu.cnm.deepdive.jata.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.jata.model.entity.Board;

public class BoardView extends View {

 private Board board;
 private Paint gridPaint;

  {
    gridPaint = new Paint();
    gridPaint.setColor(Color.GRAY);
    gridPaint.setStrokeWidth(2);
  }

  public BoardView(Context context) {
    super(context);
  }

  public BoardView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onDraw(@NonNull Canvas canvas) {
    super.onDraw(canvas);
    drawGrid(canvas);
    drawShips(canvas);
    drawShots(canvas);
  }

  public void setBoard(Board board) {
    this.board = board;
    postInvalidate();
  }

  private void drawGrid(Canvas canvas) {
    int boardSize = 10; // FIXME: 4/3/2024
    float width = getWidth();
    float cellWidth =  width / boardSize;
    float height = getHeight();
    float cellHeight = height / boardSize;
    for (int lineIndex = 0; lineIndex <= boardSize; lineIndex++) {
      canvas.drawLine(0, lineIndex * cellHeight, width, lineIndex * cellHeight, gridPaint);
      canvas.drawLine(lineIndex * cellWidth, 0, lineIndex * cellWidth, height, gridPaint);
    }
    // TODO: 4/3/2024 draw using canvas.drawlines
  }

  private void drawShips(Canvas canvas) {
    // TODO: 4/3/2024 invoke drawable.draw (canvas)  to draw ships on canvas.
  }

  private void drawShots(Canvas canvas) {
    // TODO: 4/3/2024 use drawable.draw (canvas) to draw each shot on the canvas.
  }
}
