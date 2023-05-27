package com.example.flappybirdandroid.main;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.flappybirdandroid.Game;
import com.example.flappybirdandroid.R;
import com.example.flappybirdandroid.objects.*;

public class Bird {

    private Game game;
    public Rect rect;
    double acc = 0;
    private final int JUMP_VEL = 50;
    private boolean jumping = false;
    private double jumpVel = 0;

    public Bird(Game game) {
        this.game = game;
        rect = new Rect(game.getWidth() * 0.30, game.getHeight()/2, 100, 100);
        jump();
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(game.getContext(), R.color.red));
        paint.setStrokeWidth(5);

        canvas.drawRect((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bot, paint);
    }

    public void update() {
        if (jumping) {
            rect.moveY(-jumpVel);

            if (jumpVel > -JUMP_VEL) {
                jumpVel -= 4;
            }
            else {
                jumping = false;
            }
        }
        else {
            acc += 10;
            this.rect.moveY(-jumpVel + acc);
        }
    }

    public void jump() {
        jumpVel = JUMP_VEL;
        jumping = true;
        acc = 0;
        System.out.println("JUMPS!!!");
    }
}
