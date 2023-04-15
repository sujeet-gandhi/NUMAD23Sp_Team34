package com.neu.numad23sp_team_34.project;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConnectingLineItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;

    public ConnectingLineItemDecoration(int color, float strokeWidth) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View child1 = parent.getChildAt(i);
            View child2 = parent.getChildAt(i + 1);

            int x1 = child1.getWidth() / 2;
            int y1 = child1.getBottom();

            int x2 = child2.getWidth() / 2;
            int y2 = child2.getTop();

            Path path = new Path();
            path.moveTo(x1, y1);
            path.lineTo(x2, y2);
            c.drawPath(path, paint);
        }
    }
}
