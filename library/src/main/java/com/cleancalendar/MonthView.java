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
  private final int DEFAULT_WEEKS_COUNT = 6;
  private final int DEFAULT_WEEK_LABEL_HEIGHT_DP = 48;
  private final Context mContext;
  private int mMonthIndex;
  private CalendarDayModel firstDayOfMonth;
  private int totalWeeks;
  private int totalHeight;
  private int dayColumnWidth;
  private int month;
  private int year;
  private int totalDays;
  private int totalWeeksHeight;
  private int weekLabelHeightPx;
  private int singleWeekHeight;
  private int firstWeekDay;
  private CalendarDayModel today;

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
    this.mMonthIndex = monthNumber;
    firstDayOfMonth = CalendarDayModel.fromMonthNumber(monthNumber);
    today = CalendarDayModel.today();
    firstWeekDay = firstDayOfMonth.dayOfWeek().getValue();
    totalWeeks = firstDayOfMonth.weeksCount();
    totalDays = firstDayOfMonth.daysInMonth();
    weekLabelHeightPx = inPx(DEFAULT_WEEK_LABEL_HEIGHT_DP);
    totalWeeksHeight = (6 * inPx(CleanCalendarView.TILE_HEIGHT_DP));
    singleWeekHeight = totalWeeksHeight / totalWeeks;
    totalHeight = totalWeeksHeight + weekLabelHeightPx;
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
      view.setText("Pos " + i);
      addView(view);
    }
  }

  private void addDayViews() {
    for (int i = 1; i <= totalDays; i++) {
      TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.day_view,
       null, false);
      view.setText("" + i);
      if (i == today.getDate().getDayOfMonth()) {
        view.setTextColor(Color.WHITE);
      } else {
        view.setTextColor(Color.BLACK);
      }
      addView(view);
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int childCount = getChildCount();
    int parentWidth = getMeasuredWidth();
    int top = t;
    int left = l;
    int currentDayCol = 1;
    boolean startDayLayout = false;
    for (int i = 1; i <= childCount; ) {
      View child = getChildAt(i - 1);
      if (child.getVisibility() == View.GONE)
        continue;
      float alpha = i;
      child.setAlpha(alpha / (i + 5));

      //layout if week labels
      if (i < 8) {
        i++;
        child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
        currentDayCol++;
      } else {
        //layout days
        if (startDayLayout) {
          i++;
          child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
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
