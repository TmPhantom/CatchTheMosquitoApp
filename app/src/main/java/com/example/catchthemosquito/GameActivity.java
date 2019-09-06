package com.example.catchthemosquito;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements Runnable {

    private float scale;
    private int width;
    private float toleranceFactor = 1.5f;

    //gamevalues
    private int round;
    private int points;
    private int time;
    private int mosquitosNeededConst;
    private int mosquitosNeeded;
    private int countAllMosquitos;
    private int countCatchedMosquitos;
    private int remainingMosquitos;
    private int increasePerRound = 10;
    private int increasePoints = 10;

    private Toolkit toolkit;
    private Random random;
    private Handler handler;
    private ViewGroup gameView;
    private GameViewer gameViewer;

    private FrameLayout flNeeded;
    private FrameLayout flTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scale = getResources().getDisplayMetrics().density;
        gameView = findViewById(R.id.viewGame);
        handler = new Handler();
        toolkit = new Toolkit(gameView, scale);
        random = new Random();
        flNeeded = findViewById(R.id.barNeeded);
        flTime = findViewById(R.id.barTime);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startGame();
            }
        }, 200);
    }

    @Override
    public void run() {
        decreaseTimePerSecond();
    }

    /*
    This function reduce time by 1 per second
     */
    private void decreaseTimePerSecond() {
        time--;
        float zufallszahl = random.nextFloat();
        float probability = Float.intBitsToFloat(remainingMosquitos) / Float.intBitsToFloat(time);

        if (probability > zufallszahl) {
            showMosquito();
        }
        updateScreen();
        toolkit.mosquitosDisappears();
        if (!gameIsOver()) {
            if(!roundIsOver()) {
                handler.removeCallbacks(this);
            }
        }

        handler.postDelayed(this, 1000);
    }

    private void showMosquito() {

        ImageView mosquito = createMosquito();
        FrameLayout.LayoutParams params = toolkit.getParams();
        gameView.addView(mosquito, params);
        remainingMosquitos--;
        //gameViewer.updateRemainingMosquitos();
    }

    private ImageView createMosquito(){

        final ImageView mosquito = new ImageView(this);

        mosquito.setImageResource(R.drawable.mosquito);

        View.OnClickListener onClickFunction;
        onClickFunction = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.removeView(mosquito);
                points += increasePoints;
                mosquitosNeeded--;
                updateScreen();
                //gameViewer.updateNeededMosquitos();
            }
        };

        mosquito.setOnClickListener(onClickFunction);

        //to identify a mosquito
        mosquito.setTag(R.id.birthdate, new Date());

        return mosquito;
    }

    private void updateScreen() {
        //update Points
        TextView tvPoints = findViewById(R.id.viewPoints);
        String sPoints = Integer.toString(points);
        tvPoints.setText(sPoints);

        //update count of Rounds
        TextView tvRound = findViewById(R.id.viewRound);
        String sRound = Integer.toString(round);
        tvRound.setText(sRound);

        //update count catched Mosquitos
        TextView tvNeeded = findViewById(R.id.viewNeeded);
        String sHits = Integer.toString(mosquitosNeeded);
        tvNeeded.setText(sHits);

        //update time
        TextView tvTime = findViewById(R.id.viewTime);
        String sTime = Integer.toString(time);
        tvTime.setText(sTime);

        ViewGroup.LayoutParams lpTime = flTime.getLayoutParams();
        ViewGroup.LayoutParams lpNeeded = flNeeded.getLayoutParams();

        lpNeeded.width = Math.round(width * mosquitosNeeded / mosquitosNeededConst);
        lpTime.width = Math.round(width * time / 60);

    }

    private boolean gameIsOver() {
        if (time == 0 && countCatchedMosquitos < mosquitosNeeded || (remainingMosquitos + 1) < mosquitosNeeded) {
            gameOver();
            return true;
        }
        return false;
    }

    private void gameOver() {
        finish();
        //Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        //startActivity(intent);
        handler.removeCallbacks(this);
    }

    private boolean roundIsOver() {
        if (countCatchedMosquitos >= mosquitosNeeded) {
            startRound();
            return true;
        }
        return false;
    }

    private void startGame() {
        round = 0;
        points = 0;
        toolkit.setGameView(gameView);
        startRound();
    }

    private void startRound() {
        round++;
        mosquitosNeeded = round * increasePerRound;
        mosquitosNeededConst = mosquitosNeeded;
        countCatchedMosquitos = 0;
        countAllMosquitos = Math.round(mosquitosNeeded * toleranceFactor);
        remainingMosquitos = countAllMosquitos;
        time = 60;
        width = gameView.getWidth();
        gameViewer = new GameViewer(mosquitosNeeded, countAllMosquitos, width);
        TextView tvNeeded = findViewById(R.id.viewNeeded);
        FrameLayout flNeeded = findViewById(R.id.barNeeded);

        gameViewer.setUddateableBunch(tvNeeded, flNeeded);

        decreaseTimePerSecond();
        updateScreen();
    }
}
