[![](https://jitpack.io/v/bxute/CleanCalendar.svg)](https://jitpack.io/#bxute/CleanCalendar)
# CleanCalendar

## A lightweight Android library for calendar.

#### Features:
 - Event indicator
 - Selectable dates
 - Formatting of day names as "Monday" | "Mon" | "M"
 - Customizable color for each items
 - Callbacks for date/month change
 - Adapter for setting events for a particular day.

<img src="https://user-images.githubusercontent.com/10809719/53012687-bab2c800-3469-11e9-9e01-0d13d404d854.png" width="320px" height="640px"/>

### Demo gif
![clean_calendar](https://user-images.githubusercontent.com/10809719/53012683-b8506e00-3469-11e9-8207-cf4a3c9d2f8a.gif)


### How to use ?

**1. Add this to your `build.gradle`**

```
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
	}
}
```

**2. Add the dependency**

```
dependencies {
  implementation 'com.github.bxute:CleanCalendar:{latest_version}'
}
```

**3. Add XML to your layout**

```
<?xml version="1.0" encoding="utf-8"?>
<com.cleancalendar.CleanCalendarView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/view"
    app:showMonthBar="true"
    app:cellSize="44dp"
    app:eventIndicatorColor="#03A9F4"
    app:dateTextColor="#1A1A1A"
    app:dayNameFormat="MEDIUM"
    app:dateTextSize="16sp"
    app:dateSelectionOutlineColor="#A5A5A5"
    android:layout_height="wrap_content"
    tools:context=".MainActivity" />
```

That`s it 👍🏻

### Supported Attributes

```
<resources>
    <declare-styleable name="CleanCalendarView">
        <attr name="cellSize" format="dimension" />
        <attr name="dateTextSize" format="dimension" />
        <attr name="dayTextSize" format="dimension" />
        <attr name="dayTextFontStyle" format="enum">
            <enum name="BOLD" value="0" />
            <enum name="NORMAL" value="1" />
        </attr>
        <attr name="dateTextColor" format="color" />
        <attr name="eventIndicatorColor" format="color" />
        <attr name="dateSelectionOutlineColor" format="color" />
        <attr name="dayNameFormat" format="enum">
            <enum name="FULL" value="0" />
            <enum name="MEDIUM" value="1" />
            <enum name="SINGLE_CHAR" value="2" />
        </attr>
        <attr name="showMonthBar" format="boolean" />
    </declare-styleable>
</resources>

```

### Callbacks 

```
calendarView.setCalendarListener(new CalendarListener() {
      @Override
      public void onMonthChanged(CalendarDayModel dayModel) {
        
      }

      @Override
      public void onDateSelected(CalendarDayModel day) {

      }
    });
```

### Adapter for event information

```
calendarView.setEventAdapter(new CalendarEventAdapter() {
      @Override
      public int getEventCountOn(CalendarDayModel day) {
      //return the number of even on this particular day
        return day.getDay() % 3 == 0 ? 1 : 0;
      }

      @Override
      public void preFetchEventFor(CalendarDayModel day) {
		// prepare your events for the given month.
      }
    });
    
```

### Jump to cetain date

```
calendarView.jumpToDate(CalendarDayModel.today());
```

### Contributions

Any contributions are welcome. You can send PR or open issues.

### License
MIT License

Copyright (c) 2018 bxute

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.





