package com.example.catchthemosquito;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GameViewer {

    private int width;
    private int neededMosquitos;
    private int neededMosquitosConst;
    private int allMosquitos;
    private int remainingMosquitos;

    private TextView tvNeeded;
    private TextView tvRemaining;

    private FrameLayout flNeeded;
    private FrameLayout flRemaining;

    private ViewGroup.LayoutParams lpNeeded;
    private ViewGroup.LayoutParams lpRemaining;

    public GameViewer(int neededMosquitos, int allMosquitos, int width) {
        this.neededMosquitos = neededMosquitos;
        this.allMosquitos = allMosquitos;
        this.remainingMosquitos = allMosquitos;
        this.width = width;
    }

    /**
     * expect this function is  called every time a mosquito is catched
     * @param
     */
    public void updateNeededMosquitos() {
        neededMosquitos--;
        String sNeeded = Integer.toString(neededMosquitos);
        tvNeeded.setText(sNeeded);

        lpNeeded.width = Math.round(width * neededMosquitos / neededMosquitosConst);
    }

    //shsh
    public void updateRemainingMosquitos() {
        remainingMosquitos--;
        String sRemaining = Integer.toString(remainingMosquitos);
        tvRemaining.setText(sRemaining);

        lpRemaining.width = Math.round(width * remainingMosquitos / allMosquitos);
    }

    public void setUddateableBunch(TextView tvNeeded,  FrameLayout flNeeded) {
        this.tvNeeded = tvNeeded;
        this.flNeeded = flNeeded;

        lpNeeded = flNeeded.getLayoutParams();
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
