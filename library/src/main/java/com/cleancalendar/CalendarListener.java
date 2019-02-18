/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

public interface CalendarListener {
  void onMonthChanged(CalendarDayModel dayModel);

  void onDateSelected(CalendarDayModel day);
}
