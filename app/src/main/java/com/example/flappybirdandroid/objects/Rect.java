package com.example.flappybirdandroid.objects;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.flappybirdandroid.R;

public class Rect {

    public double x, y, w, h;
    public double top, left, bot, right, centerX, centerY;

    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.top = y;
        this.left = x;
        this.bot = top + h;
        this.right = left + w;
        centerX = left + w/2;
    }


    public Rect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        updateRect();
    }


    private void updateRect() {
        top = y;
        left = x;
        bot = y + h;
        right = x + w;

        centerX = x + w/2;
        centerY = y + h/2;
    }


    public boolean collides(int x, int y) {

        if (x >= this.x && x <= this.x + this.w) {
            if (y >= this.y && y <= this.y + this.h) {
                return true;
            }
        }

        return false;
    }


    public boolean collides(Rect rect) {

        if (right > rect.left && left < rect.right) {
            if (bot > rect.top && top < rect.bot) {
                return true;
            }
        }

        return false;
    }

    public void setY(double y) {
        this.y = y;
        updateRect();
    }


    public void setX(double x) {
        this.x = x;
        updateRect();
    }


    public void moveY(double val) {
        this.y += val;
        updateRect();
    }

    public void moveX(double val) {
        this.x += val;
        updateRect();
    }

    public double getCenterX() {
        return left + w/2;
    }

    public double getCenterY() {
        return top + h/2;
    }

}
