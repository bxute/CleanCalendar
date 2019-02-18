/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayDeque;
import java.util.Iterator;

public class MonthViewAdapter extends PagerAdapter {
  private static final int MIN_YEAR = 1800;
  private static final int MAX_YEAR = 2200;
  private ArrayDeque<MonthView> monthViewsDequeable;
  private CalendarEventAdapter mEventAdapter;
  private CalendarListener mCalendarListener;

  public MonthViewAdapter() {
    monthViewsDequeable = new ArrayDeque<>();
  }

  @Override
  public int getCount() {
    return CalendarDayModel.totalMonthsInRange(MIN_YEAR, MAX_YEAR);
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    MonthView monthView = new MonthView(container.getContext());
    monthView.setEventAdapter(mEventAdapter);
    monthView.setCalendarListener(mCalendarListener);
    monthView.setMonthNumber(position);
    container.addView(monthView);
    monthViewsDequeable.add(monthView);
    return monthView;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    View view = (View) object;
    container.removeView(view);
    monthViewsDequeable.remove(object);
    invalidateChilds();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
    return view == o;
  }

  private void invalidateChilds() {
    Iterator<MonthView> iterators = monthViewsDequeable.iterator();
    while (iterators.hasNext()) {
      iterators.next().resetSelections();
    }
  }

  public void setEventAdapter(CalendarEventAdapter eventAdapter) {
    this.mEventAdapter = eventAdapter;
  }

  public void setCalendarListener(CalendarListener calendarListener) {
    this.mCalendarListener = calendarListener;
  }
}
