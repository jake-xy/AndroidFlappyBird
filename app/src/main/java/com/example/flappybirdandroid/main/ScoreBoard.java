package com.example.flappybirdandroid.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.flappybirdandroid.Game;
import com.example.flappybirdandroid.GameLoop;
import com.example.flappybirdandroid.R;
import com.example.flappybirdandroid.objects.Rect;

public class ScoreBoard {

    Game game;
    ScoreRenderer scorer;
    public Rect rect, okRect;
    Bitmap bitmap, medalBitmap, okBitmap;
    Bitmap[] medalBitmaps = new Bitmap[4];

    double targetY, medalX, medalY, scoreX, scoreY, acc = 0, vel = 34;
    int score, waitTick = 0;
    public double displayScore, scoreAcc, lastTime;
    public boolean reachedTarget, countingUp, counted, drawingMedal, visible, okPressed;

    public ScoreBoard(Game game) {
        // intialize variables
        resetVariables();
        this.game = game;
        this.scorer = new ScoreRenderer(game);
        double width = game.getWidth() - 100;

        this.bitmap = BitmapFactory.decodeResource(game.getResources(), R.drawable.scoreboard);
        this.rect = new Rect(0, 0, width, bitmap.getHeight()*width / bitmap.getWidth());
        this.bitmap = Bitmap.createScaledBitmap(bitmap, (int) rect.w, (int) rect.h, true);

        scorer.setDigitHeight(rect.h * 0.20);

        rect.setX(game.getWidth()/2 - rect.w/2);
        rect.setY(game.getHeight());
        targetY = game.getHeight() * 0.30;

        // medal
        this.medalBitmaps[3] = BitmapFactory.decodeResource(game.getResources(), R.drawable.platinum);
        this.medalBitmaps[2] = BitmapFactory.decodeResource(game.getResources(), R.drawable.gold);
        this.medalBitmaps[1] = BitmapFactory.decodeResource(game.getResources(), R.drawable.silver);
        this.medalBitmaps[0] = BitmapFactory.decodeResource(game.getResources(), R.drawable.bronze);
        for (int i = 0; i < medalBitmaps.length; i++) {
            // 0.1952 --> 19.52% of the scoreboard's width is the width of the medal
            medalBitmaps[i] = Bitmap.createScaledBitmap(medalBitmaps[i], (int) (rect.w*0.1952), (int) (rect.w*0.1952), true);
        }

        // ok button
        double okH = game.scaledY(100);
            // generate bitmap
        okBitmap = BitmapFactory.decodeResource(game.getResources(), R.drawable.ok);
            // generate scaled
        okRect = new Rect(0, 0, okBitmap.getWidth()*okH / okBitmap.getHeight(), okH);
        okRect.setY(game.getHeight()*0.75);
        okRect.setX(game.getWidth()/2 - okRect.w/2);
            // resize bitmap
        okBitmap = Bitmap.createScaledBitmap(okBitmap, (int) (okRect.w), (int) (okRect.h), true);

        System.out.println("OK: " + okRect.toString());
    }

    public void update(int score) {
        this.score = score;
        if (score >= 50) {
            medalBitmap = medalBitmaps[3];
        }
        else if (score >= 10) {
            medalBitmap = medalBitmaps[score/10 - 1];
        }

        if (visible) {
            if (rect.top > targetY) {
                rect.moveY(-(vel+acc) *game.dt);

                acc += game.scaledY(15) *game.dt;
            }
            else {
                rect.setY(targetY);
                acc = 0;
            }
        }

        else {
            if (rect.top < game.getHeight()) {
                rect.moveY((vel+acc) *game.dt);

                acc += game.scaledY(15) *game.dt;
            }
            else if (rect.top > game.getHeight()){
                // set the y
                rect.setY(game.getHeight());

                // reset all the necessary variables
                resetVariables();
            }
        }

        // pos where the medal can be drawn on
        medalX = rect.x + rect.w * 0.115; // 11.5% of the scoreboard's width is the x pos of the medal relative to the scoreboard
        medalY = rect.y + rect.h * 0.366;

        // pos where the socre can be drawn on
        scoreX = rect.x + rect.w * 0.903; // 90.25% of the scoreboard's width is the x (right) pos of the score area relative to the scoreboard
        scoreY = rect.y + rect.h * 0.280;
    }


    public void resetVariables() {
        scoreAcc = 0;
        displayScore = 0;
        waitTick = 0;
        acc = 0;
        reachedTarget = false;
        countingUp = false;
        counted = false;
        drawingMedal = false;
        if (okRect != null) {
            okRect.setY(game.getHeight()*0.75);
        }
        visible = false;
        okPressed = false;
    }


    public void draw(Canvas canvas) {

        // drawing the scoreboard
        if (rect.top < game.getHeight()) {
            canvas.drawBitmap(bitmap, (float) rect.left, (float) rect.top, null);

            // display score
            scorer.renderAlignedRight((int) displayScore, (int) scoreX, (int) scoreY, canvas);

            if (!reachedTarget && rect.top == targetY) {
                lastTime = System.currentTimeMillis();
                reachedTarget = true;
            }

            if (reachedTarget) {
                // waits 0.5 secs after reaching target y
                if (System.currentTimeMillis() - lastTime >= 500 && !countingUp) {
                    countingUp = true;
                    reachedTarget = false;
                }
            }

            if (countingUp) {
                // animates the counting up score
                if (displayScore < score) {
                    displayScore += 0.1 + scoreAcc;
                    scoreAcc += 0.005;
                }
                if (displayScore >= score && !counted) {
                    lastTime = System.currentTimeMillis();
                    counted = true;
                    countingUp = false;
                }
            }

            if (counted) {
                displayScore = score;
                // waits 0.5 secs after counting up
                if (System.currentTimeMillis() - lastTime >= 500 && !drawingMedal) {
                    drawingMedal = true;
                    counted = false;
                }
            }

            if (drawingMedal && rect.top == targetY) {
                // 0.5 secs after being counted.., display the appropriate medal if there is any
                if (this.score >= 10) {
                    canvas.drawBitmap(medalBitmap, (float) medalX, (float) medalY, null);
                }

                // display the high score

                // then display the ok button
                canvas.drawBitmap(okBitmap, (float) okRect.left, (float) okRect.top, null);
            }
        }
    }
}
