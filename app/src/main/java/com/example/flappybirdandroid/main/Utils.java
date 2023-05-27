package com.example.flappybirdandroid.main;

public class Utils {

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static Pipe[] append(Pipe[] array, Pipe item) {
        Pipe[] out = new Pipe[array.length+1];

        for (int i = 0; i < array.length; i++) {
            out[i] = array[i];
        }

        out[array.length] = item;
        return out;
    }

    public static Pipe[] pop(Pipe[] array) {
        Pipe[] out = new Pipe[array.length-1];

        for (int i = 0; i < array.length-1; i++) {
            out[i] = array[i];
        }

        return out;
    }

    public static Pipe[] remove(Pipe item, Pipe[] array) {
        int itemsToRemove = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(item)) {
                itemsToRemove += 1;
            }
        }

        Pipe[] out = new Pipe[array.length-itemsToRemove];
        int offset = 0;

        for (int i = 0; i < out.length; i++) {
            if (array[i+offset].equals(item)) {
                offset += 1;
            }
            out[i] = array[i+offset];
        }

        return out;
    }


    public static Pipe[] remove(int index, Pipe[] array) {
        if (index < 0) index = array.length - index;
        Pipe[] out = new Pipe[array.length-1];

        int offset = 0;
        for (int i = 0; i < out.length; i++) {

            if (i == index) {
                offset += 1;
            }

            out[i] = array[i + offset];
        }

        return out;
    }
}
