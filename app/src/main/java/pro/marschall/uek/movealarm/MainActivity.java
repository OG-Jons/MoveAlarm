package pro.marschall.uek.movealarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.marschall.uek.movealarm.models.AlarmModel;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    // Components from layout
    TextView alarmTimeText;
    Button newAlarmButton;
    Button testShakeAlarmButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setBindings();

        newAlarmButton.setOnClickListener(v -> {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");

        });

        cancelButton.setOnClickListener(v -> {
            cancelAlarm();
        });


        testShakeAlarmButton.setOnClickListener(v -> {
            // Intent go to ShakeAlarmActivity
            Intent intent = new Intent(this, RingingAlarmActivity.class);
            intent.putExtra("ALARM_TYPE", "SHAKE");
            startActivity(intent);
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);
    }

    private void setBindings() {
        newAlarmButton = findViewById(R.id.newAlarmButton);
        testShakeAlarmButton = findViewById(R.id.testAlarmButton);
        cancelButton = findViewById(R.id.cancelButton);
        alarmTimeText = findViewById(R.id.alarmTime);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        alarmTimeText.setText(timeText);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Intent for notification
        Intent intent = new Intent(this, AlertReceiver.class);
        // intent.putExtra("ALARM_TYPE", "SHAKE");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        alarmTimeText.setText("Alarm canceled");
    }
}