package pro.marschall.uek.movealarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class DismissAlarmShakeActivity extends AppCompatActivity {

    private static final float SHAKE_THRESHOLD_GRAVITY = 3F;
    private static final int SHAKE_SLOP_TIME_MS = 250;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 60000;
    private static final int SHAKE_AMOUNT = 30;

    private long mShakeTimestamp;
    private int mShakeCount;

    CountDownTimer countdown;

    TextView countText;
    LinearProgressIndicator progressBar;

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener accelerometerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismiss_alarm_shake);

        progressBar = findViewById(R.id.progress_indicator);

        countText = findViewById(R.id.count_of_shakes_left);
        countText.setText(String.valueOf(SHAKE_AMOUNT));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        inactivityChecker();
        setSensorListener();
    }

    private void startAlarm() {
        Intent intent = new Intent(this, RingingAlarmActivity.class);
        intent.putExtra("ALARM_TYPE", "SHAKE");
        startActivity(intent);
    }

    protected void inactivityChecker() {
        countdown = new CountDownTimer(30000, 100) {

            public void onTick(long millisUntilFinished) {
                System.out.println(millisUntilFinished);
                progressBar.setProgress((int) millisUntilFinished / 100);
            }

            public void onFinish() {
                progressBar.setProgress(0);
                startAlarm();
            }
        }.start();
    }

    private void redirectIntent() {
        Intent intent = new Intent(this, DismissedAlarmActivity.class);
        startActivity(intent);
    }

    private void setSensorListener() {
        accelerometerListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // ignore
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (sensor != null) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    float gX = x / SensorManager.GRAVITY_EARTH;
                    float gY = y / SensorManager.GRAVITY_EARTH;
                    float gZ = z / SensorManager.GRAVITY_EARTH;

                    // gForce will be close to 1 when there is no movement.
                    float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

                    if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                        final long now = System.currentTimeMillis();

                        // ignore shake events too close to each other (500ms)
                        if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                            return;
                        }

                        if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                            mShakeCount = 0;
                        }

                        mShakeTimestamp = now;
                        mShakeCount++;

                        if (mShakeCount < SHAKE_AMOUNT) {
                            countText.setText(String.valueOf(SHAKE_AMOUNT - mShakeCount));
                        } else {
                            countText.setText("0");
                            countdown.cancel();
                            redirectIntent();
                        }
                    }
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensor != null) {
            sensorManager.registerListener(accelerometerListener, sensor,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensor != null) {
            sensorManager.unregisterListener(accelerometerListener);
        }
    }

}