package com.example.flappybirdandroid.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.flappybirdandroid.Game;
import com.example.flappybirdandroid.R;
import com.example.flappybirdandroid.objects.Rect;

public class Ground {

    public static double height = 130;
    private Game game;
    public Rect rect;
    private Rect[] displayRects = new Rect[2];
    Bitmap bitmap;

    public Ground(Game game) {
        this.game = game;
        Ground.height = game.scaledY(130);

        rect = new Rect(game.getWidth(), game.getHeight()-height, game.getWidth() + game.scaledX(20), height);
        for (int i = 0; i < 2; i++) {
            displayRects[i] = new Rect(rect.x*i, rect.y, rect.w, rect.h);
        }
        bitmap = BitmapFactory.decodeResource(game.getResources(), R.drawable.ground);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) rect.w, (int) rect.h, true);
    }

    public void update() {
        for (int i = 0; i < displayRects.length; i++) {
            displayRects[i].moveX(Pipe.vel);
            if (displayRects[i].right < 0) {
                displayRects[i == 1 ? 0 : 1].setX(0);
                displayRects[i].setX(game.getWidth());
            }
        }
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(game.getContext(), R.color.green));

        for (Rect tile : displayRects) {
            // then render
                // hitbox
            canvas.drawRect((float) tile.left, (float) tile.top, (float) tile.right, (float) tile.bot, paint);
                // sprite
            canvas.drawBitmap(bitmap, (float) tile.left, (float) tile.top, null);
        }
    }
}
