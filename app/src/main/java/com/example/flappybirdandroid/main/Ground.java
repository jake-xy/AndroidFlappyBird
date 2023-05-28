package com.example.flappybirdandroid.main;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.flappybirdandroid.Game;
import com.example.flappybirdandroid.R;
import com.example.flappybirdandroid.objects.Rect;

public class Ground {

    public static double height = 110;
    private Game game;
    public Rect rect;
    private Rect[] displayRects = new Rect[2];

    public Ground(Game game) {
        this.game = game;
        rect = new Rect(game.getWidth(), game.getHeight()-height, game.getWidth(), height);
        for (int i = 0; i < 2; i++) {
            displayRects[i] = new Rect(rect.x*i, rect.y, rect.w, rect.h);
        }
    }


    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(game.getContext(), R.color.green));

        for (Rect tile : displayRects) {
            // update its pos
            tile.moveX(Pipe.vel);
            // then render
            canvas.drawRect((float) tile.left, (float) tile.top, (float) tile.right, (float) tile.bot, paint);

            if (tile.right <= 0) {
                tile.setX(game.getWidth());
            }
        }
    }
}
