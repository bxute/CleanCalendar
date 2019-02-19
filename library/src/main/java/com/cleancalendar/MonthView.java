/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.cleancalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.format.TextStyle;

import java.util.Locale;

public class MonthView extends ViewGroup {
  public static final int DEFAULT_WEEK_LABEL_HEIGHT_DP = 36;
  private static final int NO_DATE_SELECTION = -11;
  private static final int DEFAULT_MAX_WEEKS_IN_A_MONTH = 6;
  private static String[] weeks = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
  private static String[] full_weeks = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
  private static String[] char_weeks = {"M", "T", "W", "T", "F", "S", "S"};
  private final Context mContext;
  private CalendarDayModel firstDayOfMonth;
  private int weeksInThisMonth;
  private int totalHeight;
  private int dayColumnWidth;
  private int totalDays;
  private int dayNameRowHeight;
  private int firstWeekDay;
  private CalendarDayModel today;
  private int mSelectedDate = NO_DATE_SELECTION;
  private int fullWeeksHeight;
  private CalendarEventAdapter mEventAdapter;
  private View eventIndicator;
  private CalendarListener mCalendarListener;
  private float calendarWidth;
  private int extraVerticalSpaceOffset;
  private CalendarConfig mCalendarConfig;

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
    firstDayOfMonth = CalendarDayModel.fromMonthNumber(monthNumber);
    today = CalendarDayModel.today();
    firstWeekDay = firstDayOfMonth.dayOfWeek().getValue();
    weeksInThisMonth = firstDayOfMonth.weeksCount();
    totalDays = firstDayOfMonth.daysInMonth();

    calcualateSizes();
    addWeekDays();
    addDayViews();
    requestLayout();
  }

  private void calcualateSizes() {
    dayNameRowHeight = inPx(DEFAULT_WEEK_LABEL_HEIGHT_DP);
    fullWeeksHeight = (DEFAULT_MAX_WEEKS_IN_A_MONTH * mCalendarConfig.getCellSize());
    calendarWidth = 7 * mCalendarConfig.getCellSize();
    extraVerticalSpaceOffset = ((DEFAULT_MAX_WEEKS_IN_A_MONTH - weeksInThisMonth) * mCalendarConfig.getCellSize()) / weeksInThisMonth;
    totalHeight = fullWeeksHeight + dayNameRowHeight;
  }

  private void addWeekDays() {
    String[] weekNames;
    switch (mCalendarConfig.getDayLabelFormat()) {
      case 0:
        weekNames = full_weeks;
        break;
      case 1:
        weekNames = weeks;
        break;
      case 2:
        weekNames = char_weeks;
        break;
      default:
        weekNames = weeks;
    }
    for (int i = 0; i < 7; i++) {
      TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.week_name_label_view,
       null, false);
      view.setText(weekNames[i]);
      if (mCalendarConfig.getDayNameTextStyle() == 0) {
        //BOLD
        view.setTypeface(null, Typeface.BOLD);
      } else {
        view.setTypeface(null, Typeface.NORMAL);
      }
      view.setTextSize(mCalendarConfig.getDayNameTextSize());
      addView(view);
    }
  }

  private void addDayViews() {
    for (int i = 1; i <= totalDays; i++) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.day_view,
       null, false);
      TextView date = view.findViewById(R.id.date);
      date.setTextColor(mCalendarConfig.getDateTextColor());
      date.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCalendarConfig.getDateTextSize());
      eventIndicator = view.findViewById(R.id.event_indicator);
      date.setText(String.valueOf(i));
      view.setTag(i);
      //set event indicator
      showEventsIndicator(i);
      view.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          int date = (int) v.getTag();
          selectNewDate(date);
        }
      });
      addView(view);
    }
    selectNewDate(today.getDay());
  }

  private int inPx(int dp) {
    return (int) TypedValue
     .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources()
      .getDisplayMetrics());
  }

  private void showEventsIndicator(int date) {
    if (mEventAdapter != null) {
      final CalendarDayModel calendarDay = CalendarDayModel.from(LocalDate.of(firstDayOfMonth.getYear(),
       firstDayOfMonth.getMonth(),
       date));
      boolean hasEvent = mEventAdapter.getEventCountOn(calendarDay) > 0;
      if (hasEvent) {
        eventIndicator.setVisibility(VISIBLE);
        Drawable dotDrawable = mContext.getResources().getDrawable(R.drawable.one_dot);
        dotDrawable.setColorFilter(mCalendarConfig.getEventIndicatorColor(), PorterDuff.Mode.SRC_ATOP);
        eventIndicator.setBackground(dotDrawable);
      } else {
        eventIndicator.setVisibility(GONE);
      }
    }
  }

  private void selectNewDate(int date) {
    if (mSelectedDate == date)
      return;
    //remove background if its not today
    if (!isToday(mSelectedDate) && mSelectedDate != NO_DATE_SELECTION) {
      getChildAtDate(mSelectedDate)
       .findViewById(R.id.background_view)
       .setBackgroundResource(0);
    }

    View newSelectedView = getChildAtDate(date);
    int textColor = isToday(date) ? Color.WHITE : mCalendarConfig.getDateTextColor();

    Drawable drawable;
    if (isToday(date)) {
      drawable = mContext
       .getResources()
       .getDrawable(R.drawable.today_indicator);
      drawable.setColorFilter(mCalendarConfig.getEventIndicatorColor(), PorterDuff.Mode.SRC_ATOP);
    } else {
      drawable = mContext
       .getResources()
       .getDrawable(R.drawable.other_day_selection_indicator);
      drawable.setColorFilter(mCalendarConfig.getOtherDateSelectionIndicatorColor(), PorterDuff.Mode.MULTIPLY);
      newSelectedView
       .findViewById(R.id.background_view)
       .setBackground(drawable);
    }

    newSelectedView
     .findViewById(R.id.background_view)
     .setBackground(drawable);

    ((TextView) newSelectedView
     .findViewById(R.id.date)).setTextColor(textColor);

    mSelectedDate = date;
    dispatchDateSelectionEvent();
  }

  private boolean isToday(int date) {
    return (firstDayOfMonth.getYear() == today.getYear()
     && firstDayOfMonth.getMonth() == today.getMonth()
     && date == today.getDay());
  }

  private View getChildAtDate(int date) {
    int index = date + 6;
    return getChildAt(index);
  }

  private void dispatchDateSelectionEvent() {
    if (mCalendarListener != null) {
      mCalendarListener.onDateSelected(CalendarDayModel.from(LocalDate.of(
       firstDayOfMonth.getYear(),
       firstDayOfMonth.getMonth(),
       mSelectedDate
      )));
    }
  }

  public void setDefaultDate() {
    //get last day of this month
    LocalDate nextMonth = firstDayOfMonth.getDate().plusMonths(1);
    LocalDate lastDayOfThisMonth = nextMonth.minusDays(1);

    if (today.getDay() > lastDayOfThisMonth.getDayOfMonth()) {
      //select last day of this month
      selectNewDate(lastDayOfThisMonth.getDayOfMonth());
    } else {
      //select same days as today
      selectNewDate(today.getDay());
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int childCount = getChildCount();
    int parentWidth = getMeasuredWidth();
    int spaceOffset = (int) ((parentWidth - calendarWidth) / 2);
    int top = 0;
    int left = spaceOffset;
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
          //layout day child which exists such as 1,2,3
          child.layout(left,
           top,
           left + child.getMeasuredWidth(),
           (top + child.getMeasuredHeight()));
          currentDayCol++;
        } else {
          //skip for non-existent days, advance the left and column. Such as 31 of prev Month or 1 of next month
          left += child.getMeasuredWidth();
          currentDayCol++;
          if (currentDayCol >= firstWeekDay) {
            startDayLayout = true;
          }
          continue;
        }
      }

      left += child.getMeasuredWidth();
      //wrap to the next row
      if (currentDayCol > 7) {
        top += child.getMeasuredHeight() + extraVerticalSpaceOffset;
        left = spaceOffset;
        currentDayCol = 1;
      }
    }
  }

  @Override
  public String toString() {
    return String.valueOf(Month.of(firstDayOfMonth.getMonth()).getDisplayName(TextStyle.SHORT, Locale.getDefault()));
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measuredHeight = totalHeight;
    int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
    setMeasuredDimension(parentWidth, measuredHeight);
    //measure childs
    dayColumnWidth = mCalendarConfig.getCellSize();
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View view = getChildAt(i);
      int childWidth = dayColumnWidth;
      int childHeight;
      if (i < 7) {
        childHeight = dayNameRowHeight;
      } else {
        childHeight = mCalendarConfig.getCellSize();
      }
      int childHeighSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
      int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
      view.measure(childWidthSpec, childHeighSpec);
    }
  }

  public void setEventAdapter(CalendarEventAdapter eventAdapter) {
    this.mEventAdapter = eventAdapter;
  }

  public void setCalendarListener(CalendarListener mCalendarListener) {
    this.mCalendarListener = mCalendarListener;
  }

  public void setCalendarConfig(CalendarConfig calendarConfig) {
    this.mCalendarConfig = calendarConfig;
  }
}
