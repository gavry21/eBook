package com.example.android.darktheme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.example.android.darktheme.DataBase.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalenderActivity extends AppCompatActivity {
    private DateRangeCalendarView calendar;
    TextView start, end, plane;
    DBHelper db;
    Double speed;
    String ds,de;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_layput);
        calendar = findViewById(R.id.calendar);

        Calendar current = Calendar.getInstance();
        calendar.setCurrentMonth(current);
        start = findViewById(R.id.start_date);
        end = findViewById(R.id.end_date);
        plane = findViewById(R.id.your_prediction);

        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
                String pattern = "dd/MM/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = startDate.getTime();
                ds = df.format(today);
                start.setText("C: " + ds);
               // Toast.makeText(CalenderActivity.this, "Начальная дата: " + startDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                String pattern = "dd/MM/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = endDate.getTime();
                String de = df.format(today);
                end.setText("По: " + de);
               // Toast.makeText(CalenderActivity.this, "Начальная дата: " + startDate.getTime().toString() + " Дата окончания: " + endDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        db = new DBHelper(this);
        speed = db.getLast();
        plane.setText("С вашей текущей скоростью чтения - " + speed + ", \n"+
                " Вы успеете закончиться книгу в выбранный период");

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        ds = sharedPreferences.getString("Start", ds);
        de = sharedPreferences.getString("End", de);

    }
    public interface CalendarListener {
        void onFirstDateSelected(Calendar startDate);
        void onDateRangeSelected(Calendar startDate, Calendar endDate);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Start", ds);//сохраненная страница
        editor.putString("End", de);//Все страницы
    }
}
