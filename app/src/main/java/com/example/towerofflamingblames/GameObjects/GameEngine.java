package com.example.towerofflamingblames.GameObjects;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;

import com.example.towerofflamingblames.GameActivity;
import com.example.towerofflamingblames.GameState;
import com.example.towerofflamingblames.GameSurface;
import com.example.towerofflamingblames.R;

import static android.content.Context.SENSOR_SERVICE;

public class GameEngine implements SensorEventListener {
    private final Background background;
    private final Player player;
    private final Generator generator;
    private final GameActivity activity;
    private float[] accelOutput;
    private float[] magOutput;
    private float[] orientation = new float[3];
    private float[] startOrientation = null;

    public GameEngine(GameSurface context, GameActivity activity) {
        this.activity = activity;
        GameState.restart(Resources.getSystem().getDisplayMetrics().widthPixels,
                Resources.getSystem().getDisplayMetrics().heightPixels);

        background = new Background(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.background_flames));
        this.generator = new Generator(context);
        this.player = new Player(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.player_temp), GameState.SCREEN_HEIGHT * GameState.PLAYER_HEIGHT_PERCENTAGE / 100);
        generator.createObjects();
        // tworzenie reagowania na przechylenia telefonu
        SensorManager sensorManager = (SensorManager) context.getContext().getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
        background.update(deltaTime);
        generator.update(deltaTime);
        player.updateFallingPosition(deltaTime);
        this.moveScene(deltaTime);
        if (player.getRect().bottom >= GameState.SCREEN_HEIGHT) {
            int coins = this.player.getCoins();
            this.activity.endGame(coins);
        }
    }

    /**
     * Poruszenie "oknem" - jeśli gracz jest powyżej połowy ekranu - by "przyspieszyć" rozgrywkę
     * @param deltaTime
     */
    private void moveScene(float deltaTime){
        int deltaY = player.getRect().top + player.getRect().height()/2 - GameState.SCREEN_HEIGHT/2;
        if(deltaY < 0) {
            deltaY = (int) (-deltaY * GameState.MOVE_SCENE_SLOW_RATIO * 3.3f / deltaTime);

            player.moveScene(deltaY);
            background.moveScene(deltaY);
            generator.moveScene(deltaY);
        }
    }

    public void draw(Canvas canvas) {
        background.draw(canvas);
        generator.draw(canvas);
        player.draw(canvas);
    }

    public boolean handleEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                if (event.getY() > (float) GameState.SCREEN_HEIGHT / 2) {
                    player.jump();
                } else if (event.getX() < (float) GameState.SCREEN_WIDTH / 2) {
                    player.setHorizontalVelocity(-2);
                } else {
                    player.setHorizontalVelocity(2);
                }
            }
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // dla wartości [-2,2] nie reagujemy, by telefon nie był taki czuły na przechylenia
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelOutput = event.values;
        }
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magOutput = event.values;
        }
        if(accelOutput != null && magOutput != null){
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, accelOutput, magOutput);
            if(success){
                SensorManager.getOrientation(R, orientation);
                if(startOrientation == null){
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation, 0, startOrientation, 0, orientation.length);
                }
                player.setHorizontalVelocity((float) (orientation[2] - startOrientation[2])*10);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
