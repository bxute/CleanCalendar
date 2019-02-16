package com.bxute.cleancalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cleancalendar.CalendarDayModel;
import com.cleancalendar.MonthView;

public class MainActivity extends AppCompatActivity {
  MonthView monthView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    monthView = findViewById(R.id.view);
    CalendarDayModel dayModel = new CalendarDayModel(2019,2,12);
    int mn = dayModel.monthNumber();
    monthView.setMonthNumber(mn);
  }
}
