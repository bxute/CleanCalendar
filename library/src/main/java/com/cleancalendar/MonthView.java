/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MonthView extends ViewGroup {
  public static final int DEFAULT_WEEK_LABEL_HEIGHT_DP = 48;
  private static String[] weeks = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
  private final Context mContext;
  private CalendarDayModel firstDayOfMonth;
  private int totalWeeks;
  private int totalHeight;
  private int dayColumnWidth;
  private int totalDays;
  private int weekLabelHeightPx;
  private int singleWeekHeight;
  private int firstWeekDay;
  private CalendarDayModel today;
  private int mMonthNum;
  private int fullWeeksHeight;

  public MonthView(Context context) {
    this(context, null);
  }

  public MonthView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.mContext = context;
  }

  /**
   * Setter for month index.
   *
   * @param monthNumber index of month
   */
  public void setMonthNumber(int monthNumber) {
    this.mMonthNum = monthNumber;
    firstDayOfMonth = CalendarDayModel.fromMonthNumber(monthNumber);
    today = CalendarDayModel.today();
    firstWeekDay = firstDayOfMonth.dayOfWeek().getValue();
    totalWeeks = firstDayOfMonth.weeksCount();
    totalDays = firstDayOfMonth.daysInMonth();
    weekLabelHeightPx = inPx(DEFAULT_WEEK_LABEL_HEIGHT_DP);
    fullWeeksHeight = 6 * inPx(CleanCalendarView.TILE_HEIGHT_DP);
    singleWeekHeight = fullWeeksHeight / totalWeeks;
    totalHeight = fullWeeksHeight + weekLabelHeightPx;
    addWeekDays();
    addDayViews();
    requestLayout();
  }

  private int inPx(int dp) {
    return (int) TypedValue
     .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources()
      .getDisplayMetrics());
  }

  private void addWeekDays() {
    for (int i = 0; i < 7; i++) {
      TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.week_name_label_view,
       null, false);
      view.setText(weeks[i]);
      addView(view);
    }
  }

  private void addDayViews() {
    for (int i = 1; i <= totalDays; i++) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.day_view,
       null, false);
      TextView date = view.findViewById(R.id.date);
      View background = view.findViewById(R.id.background_view);
      date.setText(String.valueOf(i));
      if (isToday(i)) {
        background.setBackgroundResource(R.drawable.today_indicator);
        date.setTextColor(Color.WHITE);
      } else {
        background.setBackgroundResource(0);
        date.setTextColor(Color.BLACK);
      }
      addView(view);
    }
  }

  private boolean isToday(int date) {
    return (firstDayOfMonth.getYear() == today.getYear()
     && firstDayOfMonth.getMonth() == today.getMonth()
     && date == today.getDay());
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int childCount = getChildCount();
    int top = 0;
    int left = 0;
    int currentDayCol = 1;
    boolean startDayLayout = false;
    for (int i = 1; i <= childCount; ) {
      View child = getChildAt(i - 1);
      if (child.getVisibility() == View.GONE)
        continue;
      //layout if week labels
      if (i < 8) {
        i++;
        child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
        currentDayCol++;
      } else {
        //layout days
        if (startDayLayout) {
          i++;
          child.layout(left,
           top,
           left + child.getMeasuredWidth(),
           top + child.getMeasuredHeight());

          currentDayCol++;
        } else {
          left += child.getMeasuredWidth();
          currentDayCol++;
          if (currentDayCol >= firstWeekDay) {
            startDayLayout = true;
          }
          continue;
        }
      }

      left += child.getMeasuredWidth();
      if (currentDayCol > 7) {
        top += child.getMeasuredHeight();
        left = 0;
        currentDayCol = 1;
      }
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measuredHeight = totalHeight;
    int width = MeasureSpec.getSize(widthMeasureSpec);
    setMeasuredDimension(width, measuredHeight);
    //measure childs
    dayColumnWidth = width / 7;
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View view = getChildAt(i);
      int childWidth = dayColumnWidth;
      int childHeight;
      if (i < 7) {
        childHeight = weekLabelHeightPx;
      } else {
        childHeight = singleWeekHeight;
      }
      int childHeighSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
      int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
      view.measure(childWidthSpec, childHeighSpec);
    }
  }
}
