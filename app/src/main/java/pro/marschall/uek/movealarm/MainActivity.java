package pro.marschall.uek.movealarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.marschall.uek.movealarm.models.AlarmModel;

public class MainActivity extends AppCompatActivity {

    // Components from layout
    ListView alarmListView;
    FloatingActionButton fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBindings();
        loadData();

        fabButton.setOnClickListener(v -> {
            // Intent to go to NewAlarmActivity
            Intent intent = new Intent(this, RingingAlarmActivity.class);
            intent.putExtra("ALARM_TYPE", "SHAKE");
            startActivity(intent);
        });


    }

    private void setBindings() {
        alarmListView = findViewById(R.id.listView);
        fabButton = findViewById(R.id.fabButton);
    }

    private void loadData() {
        HashMap<String, AlarmModel> alarmList = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            AlarmModel alarm = new AlarmModel(LocalTime.of(i+1, i+5), true, "Shake");
            alarmList.put("Alarm " + i, alarm);
        }

        List<HashMap<String, String>> listItems = new ArrayList<>();

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                listItems,
                R.layout.alarm_list_item,
                new String[]{"Alarm", "Sub"},
                new int[]{R.id.firstLine, R.id.secondLine});

        for (Map.Entry<String, AlarmModel> stringStringEntry : alarmList.entrySet()) {
            HashMap<String, String> resultsMap = new HashMap<>();
            resultsMap.put("Alarm", stringStringEntry.getValue().getTime().toString());
            resultsMap.put("Sub", stringStringEntry.getValue().getDismissType());
            listItems.add(resultsMap);
        }

        alarmListView.setAdapter(simpleAdapter);
    }
}