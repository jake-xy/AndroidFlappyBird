package com.example.flappybirdandroid.objects;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.flappybirdandroid.R;

public class Rect {

    public double x, y, w, h;
    public double top, left, bot, right;

    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.top = y;
        this.left = x;
        this.bot = top + h;
        this.right = left + w;
    }


    public Rect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.top = y;
        this.left = x;
        this.bot = top + h;
        this.right = left + w;
    }

    public boolean collides(int x, int y) {

        if (x >= this.x && x <= this.x + this.w) {
            if (y >= this.y && y <= this.y + this.h) {
                return true;
            }
        }

        return false;
    }

    public void moveY(double val) {
        this.y += val;
        this.top = y;
        this.bot = top + h;
    }

    public void moveX(double val) {
        this.x += val;
        this.left = x;
        this.right = left + w;
    }
}
