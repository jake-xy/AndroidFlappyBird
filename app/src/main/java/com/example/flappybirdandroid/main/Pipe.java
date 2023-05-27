package com.example.flappybirdandroid.main;

import static com.example.flappybirdandroid.main.Utils.getRandomNumber;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.flappybirdandroid.Game;
import com.example.flappybirdandroid.R;
import com.example.flappybirdandroid.objects.Rect;

public class Pipe {

    public Rect gapRect, topRect, botRect;
    public boolean passed = false;
    Game game;

    public Pipe(Game game) {
        this.game = game;
        double h = 540;
        double randomY = (double) getRandomNumber(200, (int) (game.getHeight() - (200+h)));

        gapRect = new Rect(game.getWidth() + 100, randomY, 150, h);
        topRect = new Rect(gapRect.x, 0, gapRect.w, 0+ gapRect.top);
        botRect = new Rect(gapRect.x, gapRect.bot, gapRect.w, game.getHeight() - gapRect.bot);
    }


    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(game.getContext(), R.color.white));
        paint.setStrokeWidth(10);
        // left top right bot
        canvas.drawRect((float) topRect.left, (float) topRect.top, (float) topRect.right, (float) topRect.bot, paint);
        canvas.drawRect((float) botRect.left, (float) botRect.top, (float) botRect.right, (float) botRect.bot, paint);

//        canvas.drawRect((float) gapRect.x, (float) 0, (float) gapRect.right, (float) gapRect.y, paint);
//        canvas.drawRect((float) gapRect.left, (float) gapRect.bot, (float) gapRect.right, game.getHeight(), paint);
    }


    public void update() {
        gapRect.moveX(-20);
        topRect.moveX(-20);
        botRect.moveX(-20);
    }
}
