package com.example.dominik.ukf_app.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.example.dominik.ukf_app.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.List;

/**
 * Created by Dominik on 09.03.2018.
 */

public class EventDecorator implements DayViewDecorator {

    private Drawable highlightDrawable;
    private Context context;
    private List<String> events;

    public EventDecorator(Context context,List<String> events) {
        this.context = context;
        this.events = events;
        highlightDrawable = this.context.getResources().getDrawable(R.drawable.event_background);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        java.util.Date utilDate = day.getDate();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return events.contains(sqlDate.toString());
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(highlightDrawable);
        view.addSpan(new ForegroundColorSpan(Color.BLACK));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.3f));

    }
}
