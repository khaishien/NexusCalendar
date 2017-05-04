package com.kslau.nexus.samplenexuscalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kslau.nexus.nexuscalendar.DayModel;
import com.kslau.nexus.nexuscalendar.NexusCalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NexusCalendarView nexusCalendarView = (NexusCalendarView) findViewById(R.id.calendar);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        for (int i = 0; i < 15; i++) {
            Random random = new Random();
            int rad = random.nextInt(50);
            float radFloat = random.nextFloat();
            nexusCalendarView.addDotValueToDate(rad + radFloat, calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        nexusCalendarView.updateUI();

        nexusCalendarView.setOnDateClickListener(new NexusCalendarView.OnDateClickListener() {
            @Override
            public void onDateClickListener(AdapterView<?> parent, View view, DayModel dayModel) {
                Toast.makeText(MainActivity.this, "on click - Date: " + dayModel.getDate(), Toast.LENGTH_SHORT).show();
            }
        });


        nexusCalendarView.setOnDateLongClickListener(new NexusCalendarView.OnDateLongClickListener() {
            @Override
            public boolean onDateLongClickListener(AdapterView<?> parent, View view, DayModel dayModel) {
                Toast.makeText(MainActivity.this, "on long click - Date: " + dayModel.getDate(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}
