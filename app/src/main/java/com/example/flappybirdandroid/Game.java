package com.example.flappybirdandroid;

import static com.example.flappybirdandroid.main.Utils.*;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.loader.ResourcesProvider;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/*
    Game manages all objects in the game and is responsible for updating all tha game states and
    rendering the objects in the game
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    GameLoop gameLoop;
    private Bird bird;
    private Pipe[] pipes = new Pipe[0];
    private Ground ground;
    public int score = 0, timer = 0;
    public boolean showHitbox = false;
    Bitmap bgImg;
    public double dt, prevTime;
    private static MediaPlayer pointSound, swooshSound;
    ScoreRenderer scorer;
    ScoreBoard scoreBoard;
    InputStream inputStream;

    public Game(Context context) {
        super(context);
        // get surface holder
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        System.out.println("-------------------");
        try {
            inputStream = getContext().getAssets().open("/storage/sdcard0/scores.txt");
            System.out.println("HIGH: " + inputStream.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("-------------------");

        if (pointSound == null) {
            pointSound = MediaPlayer.create(getContext(), R.raw.point);
            swooshSound = MediaPlayer.create(getContext(), R.raw.swoosh);
        }
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // to start the actual game (where the bird is flapping)
                if (!bird.flapping) {
                    pipes = append(pipes, new Pipe(this, this.getWidth()*1.5));
                    bird.flapping = true;
                }

                if (bird.rect.top > 0) {
                    bird.jump();
                }

                if (scoreBoard.visible) {
                    if (scoreBoard.countingUp) {
                        scoreBoard.countingUp = false;
                        scoreBoard.counted = true;
                    }
                }

                if (scoreBoard.drawingMedal) {
                    if (scoreBoard.okRect.collides((int) event.getX(), (int) event.getY())) {
                        scoreBoard.lastTime = System.currentTimeMillis();
                        scoreBoard.okPressed = true;
                        scoreBoard.visible = false;
                        initGame();
                    }
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    public void initGame() {
        bird = new Bird(this);
        pipes = new Pipe[0];
        ground = new Ground(this);
        score = 0;
        Game.swooshSound.start();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        initGame();
        scorer = new ScoreRenderer(this);
        scoreBoard = new ScoreBoard(this);

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

        if (!bird.onGround) {
            scorer.render(score, canvas);
        }

        scoreBoard.draw(canvas);

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
    }

    public void update() {

        dt = (System.currentTimeMillis() - prevTime)/1000.0;
        dt *= 32;

        prevTime = System.currentTimeMillis();

        // update game state
        bird.update(pipes);

        // handles death
        if (!bird.alive && bird.onGround && !scoreBoard.okPressed) {
            scoreBoard.visible = true;
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
            if (!pipe.passedBird && bird.rect.centerX >= pipe.gapRect.centerX) {
                score ++;
                // generating new pipes
                pipes = append(pipes, new Pipe(this));
                pipe.passedBird = true;
                Game.pointSound.start();
            }

            // remove once the pipe is offscreen
            if (pipe.gapRect.right <= 0) {
                pipes = remove(pipe, pipes);
            }
        }

        // scoreboard
        scoreBoard.update(score);
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
