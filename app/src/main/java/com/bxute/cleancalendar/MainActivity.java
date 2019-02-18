package com.bxute.cleancalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cleancalendar.CalendarDayModel;
import com.cleancalendar.CalendarEventAdapter;
import com.cleancalendar.CleanCalendarView;

public class MainActivity extends AppCompatActivity {
  CleanCalendarView calendarView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    calendarView = findViewById(R.id.view);
    calendarView.setEventAdapter(new CalendarEventAdapter() {
      @Override
      public int getEventCountOn(CalendarDayModel day) {
        return day.getDay() % 2 == 0 ? 1 : 0;
      }

      @Override
      public void preFetchEventFor(CalendarDayModel day) {
        Log.d("MainPage", "prefetch for " + day.getMonth());
      }
    });
  }
}
