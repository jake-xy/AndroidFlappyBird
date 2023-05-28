package com.example.flappybirdandroid.main;

import static com.example.flappybirdandroid.main.Utils.getRandomNumber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.flappybirdandroid.Game;
import com.example.flappybirdandroid.R;
import com.example.flappybirdandroid.objects.Rect;

public class Pipe {

    public Rect gapRect, topRect, botRect;
    public boolean passed = false;
    public static double vel = -13;
    Game game;
    Bitmap bitmap;

    public Pipe(Game game) {
        this.game = game;
        Pipe.vel = game.scaledX(-13);

        double gapH = game.scaledY(540);
        double randomY = (double) getRandomNumber((int) Ground.height, (int) (game.getHeight() - (Ground.height*1.5+gapH)));

        gapRect = new Rect(game.getWidth() + game.scaledX(100), randomY, game.scaledX(190), gapH);
        topRect = new Rect(gapRect.x, 0, gapRect.w, 0+ gapRect.top);
        botRect = new Rect(gapRect.x, gapRect.bot, gapRect.w, game.getHeight() - gapRect.bot);

        bitmap = BitmapFactory.decodeResource(game.getResources(), R.drawable.pipe);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)gapRect.w, (int)(gapRect.w*bitmap.getHeight() / bitmap.getWidth()), true );
    }

    public Pipe(Game game, double x) {
        this(game);
        this.gapRect.setX(x);
        this.topRect.setX(x);
        this.botRect.setX(x);
    }


    public void draw(Canvas canvas) {
        Paint paint = new Paint();
//        // hitboxes
//        paint.setColor(ContextCompat.getColor(game.getContext(), R.color.white));
//        paint.setStrokeWidth(10);
//            // top pipe
//        canvas.drawRect((float) topRect.left, (float) topRect.top, (float) topRect.right, (float) topRect.bot, paint);
//            // bot pipe
//        canvas.drawRect((float) botRect.left, (float) botRect.top, (float) botRect.right, (float) botRect.bot, paint);

        // sprite
            // bot pipe
        canvas.drawBitmap(bitmap, (float) botRect.left, (float) botRect.top, paint);
            // top pipe
        canvas.rotate(180F, (float) topRect.getCenterX(), (float) (topRect.bot - bitmap.getHeight()/2));
        canvas.drawBitmap(bitmap, (float) topRect.left, (float) (topRect.bot - bitmap.getHeight()), paint);
        canvas.rotate(-180F, (float) topRect.getCenterX(), (float) (topRect.bot - bitmap.getHeight()/2));

    }


    public void update() {
        gapRect.moveX(vel);
        topRect.moveX(vel);
        botRect.moveX(vel);
    }
}
