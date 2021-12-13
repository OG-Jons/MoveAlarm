package pro.marschall.uek.movealarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class DismissedAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismissed_alarm);

        // wait 5 seconds and then close the app
        System.out.println("yes");
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            System.out.println("waited 5 secs");
            closeApp();
        }, 5000);
    }

    private void closeApp() {
        System.out.println("Closing app...");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(startMain);
    }
}