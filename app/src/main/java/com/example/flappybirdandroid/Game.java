package com.example.flappybirdandroid;

import static com.example.flappybirdandroid.main.Utils.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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

    private GameLoop gameLoop;
    private Bird bird;
    private Pipe[] pipes = new Pipe[0];
    private int score = 0, timer = 0;

    public Game(Context context) {
        super(context);
        // get surface holder
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder);
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (bird.rect.top > 0)
                    bird.jump();
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        bird = new Bird(this);
        pipes = append(pipes, new Pipe(this));
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

        for (Pipe pipe : pipes) {
            pipe.draw(canvas);
        }

        bird.draw(canvas);

        drawUPS(canvas);
        drawFPS(canvas);
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.magenta));
        canvas.drawText("UPS: " + averageUPS, 20, 70, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();

        paint.setColor(ContextCompat.getColor(getContext(), R.color.magenta));
        paint.setTextSize(40);
        canvas.drawText("FPS: " + averageFPS, 20, 120, paint);
        canvas.drawText("Score: " + score, 20, 170, paint);
    }

    public void update() {
        // update game state
        bird.update();

        if (!bird.alive) {
            timer += 1;
            if (timer > 120) {
                gameLoop.stop();
            }
        }

        // ground
        if (bird.rect.bot >= this.getHeight()) {
            bird.rect.setY(this.getHeight() - bird.rect.h);
            bird.alive = false;
        }

        for (Pipe pipe : pipes) {
            if (bird.alive) {
                pipe.update();
            }

            // collision
            if (bird.rect.collides(pipe.topRect) || bird.rect.collides(pipe.botRect)) {
                bird.alive = false;
            }

            // score
            if (!pipe.passed && bird.rect.centerX >= pipe.gapRect.centerX) {
                score ++;
                pipes = append(pipes, new Pipe(this));
                pipe.passed = true;
                System.out.println("score: " + score);
            }

            // remove once the pipe is offscreen
            if (pipe.gapRect.right <= 0) {
                pipes = remove(pipe, pipes);
            }
        }
    }
}
