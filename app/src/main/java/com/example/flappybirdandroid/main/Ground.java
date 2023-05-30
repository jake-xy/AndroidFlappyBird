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

    public static double height = 180;
    private Game game;
    public Rect rect;
    private Rect[] displayRects = new Rect[2];
    Bitmap bitmap;

    public Ground(Game game) {
        this.game = game;
        Ground.height = game.scaledY(130);

        rect = new Rect(0, game.getHeight()-height, game.getWidth(), height);
        for (int i = 0; i < 2; i++) {
            displayRects[i] = new Rect(rect.w*i, rect.y, rect.w, rect.h);
        }
        bitmap = BitmapFactory.decodeResource(game.getResources(), R.drawable.ground);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) rect.w, (int) rect.h, true);
    }

    public void update() {
//        for (int i = 0; i < displayRects.length; i++) {
//            displayRects[i].moveX(Pipe.vel *game.dt);
//            if (displayRects[i].right < 0) {
//                displayRects[i == 1 ? 0 : 1].setX(0);
//                displayRects[i].setX(game.getWidth());
//            }
//        }
        for (Rect ground : displayRects) {
            ground.moveX(Pipe.vel *game.dt);
        }

        if (displayRects[0].right <= 0) {
            displayRects[0].setX(0);
            displayRects[1].setX(game.getWidth());
        }
    }

    public void draw(Canvas canvas) {

        for (Rect tile : displayRects) {
            // then render
                // sprite
            canvas.drawBitmap(bitmap, (float) tile.left, (float) tile.top, null);
        }
        // hitbox
        if (game.showHitbox) {
            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(game.getContext(), R.color.red));
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bot, paint);
        }
    }
}
