package com.example.flappybirdandroid;

import static com.example.flappybirdandroid.main.Utils.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.flappybirdandroid.main.*;


/*
    Game manages all objects in the game and is responsible for updating all tha game states and
    rendering the objects in the game
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    GameLoop gameLoop;
    private Bird bird;
    private Pipe[] pipes = new Pipe[0];
    private Ground ground;
    private int score = 0, timer = 0;
    public boolean showHitbox = false;
    Bitmap bgImg;

    public double dt, prevTime;

    private static MediaPlayer pointSound, swooshSound;

    public Game(Context context) {
        super(context);
        // get surface holder
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        if (pointSound == null) {
            pointSound = MediaPlayer.create(getContext(), R.raw.point);
            swooshSound = MediaPlayer.create(getContext(), R.raw.swoosh);
        }

        setFocusable(true);
    }

    public void initGame() {
        bird = new Bird(this);
        pipes = new Pipe[0];
        ground = new Ground(this);
        score = 0;
        timer = 0;
        Game.swooshSound.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!bird.flapping) {
                    pipes = append(pipes, new Pipe(this, this.getWidth()*1.5));
                    bird.flapping = true;
                }
                if (bird.rect.top > 0)
                    bird.jump();
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        initGame();
        bgImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
        bgImg = Bitmap.createScaledBitmap(bgImg, getWidth(), getHeight(), true);

        prevTime = System.currentTimeMillis();
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(bgImg, 0, 0, null);

        for (Pipe pipe : pipes) {
            pipe.draw(canvas);
        }

        ground.draw(canvas);
        bird.draw(canvas);

        drawUPS(canvas);
        drawFPS(canvas);
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString((int)gameLoop.getAverageUPS());
        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.magenta));
        canvas.drawText("UPS: " + averageUPS, 20, 70, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString((int)gameLoop.getAverageFPS());
        Paint paint = new Paint();

        paint.setColor(ContextCompat.getColor(getContext(), R.color.magenta));
        paint.setTextSize(40);
        canvas.drawText("FPS: " + averageFPS, 20, 120, paint);
        canvas.drawText("Score: " + score, 20, 170, paint);
//        canvas.drawText("Vel: " + Pipe.vel, 20, 220, paint);
//        canvas.drawText("BWidth: " + bird.rect.w, 20, 270, paint);
    }

    public void update() {

        dt = (System.currentTimeMillis() - prevTime)/1000.0;
        dt *= GameLoop.MAX_UPS;
//        System.out.println("DT: " + dt);
        prevTime = System.currentTimeMillis();

        // update game state
        bird.update(pipes);

        if (!bird.alive && bird.onGround) {
            timer += 1;
            if (timer > 45) {
               initGame();
            }
        }

        // ground collision
        if (bird.rect.bot > ground.rect.top && !bird.onGround) {
            bird.rect.setY(ground.rect.top - bird.rect.h);
            bird.onGround = true;
            if (bird.alive) {
                Bird.hitSound.start();
                bird.alive = false;
            }
            Game.swooshSound.start();
        }

        if (bird.alive) ground.update();

        for (Pipe pipe : pipes) {
            if (bird.alive) {
                pipe.update();
            }

            // collision
            if (bird.alive && (bird.rect.collides(pipe.topRect) || bird.rect.collides(pipe.botRect)) ) {
                bird.alive = false;
                break;
            }

            // score
            if (!pipe.passed && bird.rect.centerX >= pipe.gapRect.centerX) {
                score ++;
                pipes = append(pipes, new Pipe(this));
                pipe.passed = true;
                Game.pointSound.start();
            }

            // remove once the pipe is offscreen
            if (pipe.gapRect.right <= 0) {
                pipes = remove(pipe, pipes);
            }
        }
    }

    public double scaledX(double xVal) {
        // this is the width of the screen when I was building the app.
        // Therefore, I used this as reference
        double referenceWidth = 1080;
        return this.getWidth() * (xVal/referenceWidth);
    }

    public double scaledY(double yVal) {
        // this is the height of the screen when I was building the app.
        // Therefore, I used this as reference
        double referenceHeight = 1647;
        return this.getHeight() * (yVal/referenceHeight);
    }
}
