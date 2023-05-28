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
    public boolean alive = true, onGround = false, flapping = false;
    private boolean initDeathAnime = false;
    private final static int JUMP_VEL = 45;
    private double jumpVel = 0, acc = 0;
    private double swayDir = -1, swayVel = -8;


    public Bird(Game game) {
        this.game = game;
        rect = new Rect(game.getWidth() * 0.30, game.getHeight() / 2, 110, 100);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(game.getContext(), R.color.red));
        paint.setStrokeWidth(5);

        canvas.drawRect((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bot, paint);
    }

    public void update() {
        if (flapping) {
            if (!onGround) {
                rect.moveY(jumpVel);

                if (jumpVel > 0) {
                    acc += 0.3;
                    jumpVel += 3 + acc;
                }
                else {
                    jumpVel += 3;
                }
            }

            if(!alive && !initDeathAnime) {
                // jump
                jumpVel = -JUMP_VEL;
                acc = 0;
                initDeathAnime = true;
            }
        }
        else {
            if (swayDir == -1 && rect.y < game.getHeight()/2 - 80) {
                swayDir = 1;
            }
            else if (swayDir == 1 && rect.y > game.getHeight()/2 + 80) {
                swayVel = 8;
                swayDir = -1;
            }
            swayVel += 0.5*swayDir;
            rect.moveY(swayVel);
        }
    }

    public void jump() {
        if (alive) {
            jumpVel = -JUMP_VEL;
            acc = 0;
        }
    }
}
