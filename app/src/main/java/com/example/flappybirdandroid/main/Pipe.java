package com.example.flappybirdandroid.main;

import static com.example.flappybirdandroid.main.Utils.getRandomNumber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
    private static Bitmap bitmap;

    public Pipe(Game game) {
        this.game = game;
        Pipe.vel = game.scaledY(-13);

        double gapH = game.scaledY(500);
        double randomY = (double) getRandomNumber((int) Ground.height, (int) (game.getHeight() - (Ground.height*1.5+gapH)));

        gapRect = new Rect(game.getWidth() + game.scaledX(100), randomY, game.scaledY(190), gapH);
        topRect = new Rect(gapRect.x, 0, gapRect.w, 0+ gapRect.top);
        botRect = new Rect(gapRect.x, gapRect.bot, gapRect.w, game.getHeight() - gapRect.bot);

        if (Pipe.bitmap == null) {
            Pipe.bitmap = BitmapFactory.decodeResource(game.getResources(), R.drawable.pipe);
            Pipe.bitmap = Bitmap.createScaledBitmap(Pipe.bitmap, (int)gapRect.w, (int)(gapRect.w*Pipe.bitmap.getHeight() / Pipe.bitmap.getWidth()), true);
        }
    }

    public Pipe(Game game, double x) {
        this(game);
        this.gapRect.setX(x);
        this.topRect.setX(x);
        this.botRect.setX(x);
    }


    public void draw(Canvas canvas) {
        // sprite
            // bot pipe
        canvas.drawBitmap(bitmap, (float) botRect.left, (float) botRect.top, null);
            // top pipe
        canvas.rotate(180F, (float) topRect.getCenterX(), (float) (topRect.bot - Pipe.bitmap.getHeight()/2));
        canvas.drawBitmap(Pipe.bitmap, (float) topRect.left, (float) (topRect.bot - Pipe.bitmap.getHeight()), null);
        canvas.rotate(-180F, (float) topRect.getCenterX(), (float) (topRect.bot - Pipe.bitmap.getHeight()/2));

        if (game.showHitbox) {
            Paint paint = new Paint();
            // hitboxes
            paint.setColor(ContextCompat.getColor(game.getContext(), R.color.red));
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            // top pipe
            canvas.drawRect((float) topRect.left, (float) topRect.top, (float) topRect.right, (float) topRect.bot, paint);
            // bot pipe
            canvas.drawRect((float) botRect.left, (float) botRect.top, (float) botRect.right, (float) botRect.bot, paint);
        }
    }


    public void update() {
        gapRect.moveX(vel *game.dt);
        topRect.moveX(vel *game.dt);
        botRect.moveX(vel *game.dt);
    }

    public void update(Bird bird) {
        gapRect.moveX(vel *game.dt);
        topRect.moveX(vel *game.dt);
        botRect.moveX(vel *game.dt);
        if (bird.alive) {
            if (bird.rect.collides(botRect) || bird.rect.collides(topRect)) {
                gapRect.setX(bird.rect.right);
                topRect.setX(bird.rect.right);
                botRect.setX(bird.rect.right);

                bird.alive = false;
            }
        }
    }
}
