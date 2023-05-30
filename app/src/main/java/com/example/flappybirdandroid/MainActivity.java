package com.example.flappybirdandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Main asdjasjdia
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set window to full screen
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        try {
            FileWriter fw = new FileWriter("/storage/sdcard0/scores.txt");
            fw.write("0");
            fw.close();
            System.out.println("WRITE SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // set content view to new game
        setContentView(new Game(this));
    }
}
