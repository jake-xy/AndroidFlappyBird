package com.example.flappybirdandroid.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;

import androidx.core.content.ContextCompat;

import com.example.flappybirdandroid.Game;
import com.example.flappybirdandroid.GameLoop;
import com.example.flappybirdandroid.R;
import com.example.flappybirdandroid.objects.*;

import java.io.IOException;

public class Bird {

    private Game game;
    public Rect rect;
    public boolean alive = true, onGround = false, flapping = false;
    private boolean initDeathAnime = false;
    private int JUMP_VEL;
    private double jumpVel = 0, acc = 0, rotation = 0;
    private double swayDir = -1, swayVel = -8;
    private Bitmap[] bitmaps = new Bitmap[3];
    private int drawTicks = 0;
    private final int maxDrawTicks = 30;
    public static MediaPlayer jumpSound, hitSound, dieSound;

    public Bird(Game game) {
        this.game = game;
        JUMP_VEL = (int) game.scaledY(42);
        rect = new Rect(game.getWidth() * 0.30, game.getHeight() / 2, game.scaledY(120), game.scaledY(100));

        for (int i = 0; i < 3; i++) {
            int id = game.getResources().getIdentifier("b" + (i+1), "drawable", game.getContext().getPackageName());
            bitmaps[i] = BitmapFactory.decodeResource(game.getResources(), id);
            bitmaps[i] = Bitmap.createScaledBitmap(bitmaps[i], (int) rect.w, (int) rect.h, true);
        }

        if (Bird.jumpSound == null) {
            Bird.jumpSound = MediaPlayer.create(game.getContext(), R.raw.wing);
            Bird.hitSound = MediaPlayer.create(game.getContext(), R.raw.hit);
            Bird.dieSound = MediaPlayer.create(game.getContext(), R.raw.die);
        }
    }

    public void draw(Canvas canvas) {
        Paint paint = null;

        if (game.showHitbox) {
                // draws the hitbox
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(ContextCompat.getColor(game.getContext(), R.color.red));
            paint.setStrokeWidth(5);
            canvas.drawRect((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bot, paint);
        }

            // renders the sprite
        Bitmap bitmap = bitmaps[(int)(drawTicks/(maxDrawTicks/3))];

        canvas.rotate((float) rotation, (float) rect.getCenterX(), (float) rect.getCenterY());
        canvas.drawBitmap(bitmap, (float) (rect.left), (float) (rect.top), paint);
        canvas.rotate((float) -rotation, (float) rect.getCenterX(), (float) rect.getCenterY());

        if (rotation < 45 && flapping && !onGround) {
            rotation += 2 + acc;
        }

        if (drawTicks < maxDrawTicks-1) {
            if (alive) {
                drawTicks += 1;
            }
        }
        else {
            drawTicks = 0;
        }
    }

    public void update() {
        if (flapping) {
            if (!onGround) {
                rect.moveY(jumpVel *game.dt);

                if (jumpVel > 0) {
                    acc += game.scaledY(0.3);
                    jumpVel += game.scaledY(3) + acc;
                }
                else {
                    jumpVel += game.scaledY(3);
                }
            }
            // when the bird dies (collides with pipe)
            if(!alive && !initDeathAnime && !onGround) {
                if (!onGround) {
                    // jump
                    jumpVel = -JUMP_VEL;
                    rotation = -30;
                    acc = 0;
                    initDeathAnime = true;
                    Bird.hitSound.start();
                    Bird.dieSound.start();
                }
            }
        }
        else {
            if (swayDir == -1 && rect.y < game.getHeight()/2 - game.scaledY(80)) {
                swayDir = 1;
            }
            else if (swayDir == 1 && rect.y > game.getHeight()/2 + game.scaledY(80)) {
                swayVel = game.scaledY(8);
                swayDir = -1;
            }
            swayVel += 0.5*swayDir;
            rect.moveY(swayVel *game.dt);
        }
    }


    public void update(Pipe[] pipes) {
        if (flapping) {
            if (!onGround) {
                rect.moveY(jumpVel *game.dt);
                // collision
                if (alive) {
                    for (Pipe pipe : pipes) {
                        if (rect.collides(pipe.botRect)) {
                            rect.setY(pipe.botRect.top - rect.h);
                            alive = false;
                            break;
                        }
                        else if (rect.collides(pipe.topRect)) {
                            rect.setY(pipe.topRect.bot);
                            alive = false;
                            break;
                        }
                    }
                }

                if (jumpVel > 0) {
                    acc += game.scaledY(0.3);
                    jumpVel += game.scaledY(3) + acc;
                }
                else {
                    jumpVel += game.scaledY(3);
                }
            }
            // when the bird dies (collides with pipe)
            if(!alive && !initDeathAnime) {
                if (!onGround) {
                    // jump
                    jumpVel = -JUMP_VEL;
                    rotation = -30;
                    acc = 0;
                    initDeathAnime = true;
                    Bird.hitSound.start();
                    Bird.dieSound.start();
                }
            }
        }
        else {
            if (swayDir == -1 && rect.y < game.getHeight()/2 - game.scaledY(80)) {
                swayDir = 1;
            }
            else if (swayDir == 1 && rect.y > game.getHeight()/2 + game.scaledY(80)) {
                swayVel = game.scaledY(8);
                swayDir = -1;
            }
            swayVel += 0.5*swayDir;
            rect.moveY(swayVel *game.dt);
        }
    }

    public void jump() {
        if (alive) {
            if(Bird.jumpSound.isPlaying()) {
                Bird.jumpSound.seekTo(0);
            }
            Bird.jumpSound.start();
            jumpVel = -JUMP_VEL;
            rotation = -30;
            acc = 0;
        }
    }
}
