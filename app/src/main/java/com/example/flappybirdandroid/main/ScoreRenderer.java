package com.example.flappybirdandroid.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.flappybirdandroid.Game;
import com.example.flappybirdandroid.objects.Rect;

public class ScoreRenderer {
    Bitmap[] bitmaps = new Bitmap[10];
    String scoreStr;
    double x, y, digitH, digitW, width;
    Game game;

    public ScoreRenderer(Game game) {
        this.game = game;
        digitH = game.scaledY(160);

        for (int i = 0; i <= 9; i++) {
            int id = game.getResources().getIdentifier("score" + (i), "drawable", game.getContext().getPackageName());
            bitmaps[i] = BitmapFactory.decodeResource(game.getResources(), id);
            bitmaps[i] = Bitmap.createScaledBitmap(
                    bitmaps[i],
                    (int) (bitmaps[i].getWidth()*digitH/bitmaps[i].getHeight()),
                    (int) digitH,
                    true);
        }

        digitW = bitmaps[0].getWidth();

        System.out.println("Scorer: " + "w: " + digitW + ", h: " + digitH);
    }


    public void setDigitHeight(double height) {
        digitH = height;

        for (int i = 0; i <= 9; i++) {
            int id = game.getResources().getIdentifier("score" + (i), "drawable", game.getContext().getPackageName());
            bitmaps[i] = BitmapFactory.decodeResource(game.getResources(), id);
            bitmaps[i] = Bitmap.createScaledBitmap(
                    bitmaps[i],
                    (int) (bitmaps[i].getWidth()*digitH/bitmaps[i].getHeight()),
                    (int) digitH,
                    true);
        }

        digitW = bitmaps[0].getWidth();
    }


    public void render(int score, Canvas canvas) {
        // convert score to string
        scoreStr = Integer.toString(score);
        // get the overall width
        width = digitW*scoreStr.length();

        // look for the middle of the screen based on that width
        x = game.getWidth()/2 - width/2;
        y = game.getHeight() * 0.15;

        // draw the digits
        for (int i = 0; i < scoreStr.length(); i++) {
            canvas.drawBitmap(bitmaps[Integer.parseInt(scoreStr.charAt(i) + "")], (float) (x + digitW*i), (float) y, null);
        }
    }

    public void renderAlignedLeft(int score, int x, int y, Canvas canvas) {
        // convert score to string
        scoreStr = Integer.toString(score);
        // get the overall width
        width = digitW*scoreStr.length();

        // draw the digits
        for (int i = 0; i < scoreStr.length(); i++) {
            canvas.drawBitmap(bitmaps[Integer.parseInt(scoreStr.charAt(i) + "")], (float) (x + digitW*i), (float) y, null);
        }
    }

    public void renderAlignedRight(int score, int x, int y, Canvas canvas) {
        // convert score to string
        scoreStr = Integer.toString(score);
        // get the overall width
        width = digitW*scoreStr.length();

        // draw the digits
        x -= width;
        for (int i = 0; i < scoreStr.length(); i++) {
            canvas.drawBitmap(bitmaps[Integer.parseInt(scoreStr.charAt(i) + "")], (float) (x + digitW*i), (float) y, null);
        }
    }
}
