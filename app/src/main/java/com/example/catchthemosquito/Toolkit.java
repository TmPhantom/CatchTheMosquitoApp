package com.example.catchthemosquito;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Date;
import java.util.Random;

public class Toolkit {

    private ViewGroup gameView;
    private float scale;
    private static int MOSQUITO_PIXELS_WIDTH = 50;
    private static int MOSQUITO_PIXELS_HEIGHT = 50;
    private static int APPEAR_DURATION_MS = 2000;

    Random random = new Random();

    public Toolkit(ViewGroup gameView, float scale) {
        this.gameView = gameView;

        this.scale = scale;
    }

    public FrameLayout.LayoutParams getParams() {

        int width = gameView.getWidth();
        int height = gameView.getHeight();

        int widthMosquito = Math.round(scale * MOSQUITO_PIXELS_WIDTH);
        int heightMosquito = Math.round(scale * MOSQUITO_PIXELS_HEIGHT);

        //where can mosquito be placed
        int left = random.nextInt(width - widthMosquito);
        int top = random.nextInt(height - heightMosquito);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthMosquito, heightMosquito);
        params.leftMargin = left;
        params.topMargin = top;
        params.gravity = Gravity.TOP + Gravity.LEFT;

        return params;
    }

    public void setGameView(ViewGroup gameView) {
        this.gameView = gameView;
    }

    public void mosquitosDisappears() {

        int n = gameView.getChildCount();

        if (n > 0) {
            int i = 0;
            while (i < n) {

                ImageView mosquito = (ImageView) gameView.getChildAt(i);
                Date birthDate = (Date) mosquito.getTag(R.id.birthdate);
                long age = (new Date()).getTime() - birthDate.getTime();

                if (age >= APPEAR_DURATION_MS) {
                    mosquito.setVisibility(View.GONE);
                    mosquito.setOnClickListener(null);
                }
                ++i;
            }
        }
    }
}
