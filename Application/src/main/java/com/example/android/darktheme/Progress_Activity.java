package com.example.android.darktheme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.android.darktheme.DataBase.DBHelper;
import com.example.android.darktheme.Dialog.AddGoals;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.DatabaseMetaData;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.lang.Math.round;

public class Progress_Activity extends AppCompatActivity {

    int[] colorArray = new int[]{Color.rgb(133, 142, 231), Color.rgb(138, 196, 82)};

    static final String TAG = "ProgressFragmentTag";
    int pages = 10;
    int spentTime, savedp, all, last, left, defp, defst, defsp, defa, defl;
    double count = 1, dif = 1;
    int avTime = 120000;//2 минуты на страницу
    double t = 0, check;
    String s, tb, date, deftb, ch = "режиме скорости";
    TextView speed, prediction, title, date_book;
    ArrayList<Double> all_speed = new ArrayList<>();
    ArrayList<Double> all_time = new ArrayList<>();
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_progress);

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        pages = getIntent().getIntExtra("PagesRead", defp);
        defp = defp + pages;

        spentTime = getIntent().getIntExtra("TimeSpent", defst);
        defst = defst + spentTime;

        savedp = getIntent().getIntExtra("SavedPage", defsp);
        defsp = savedp;

        all = getIntent().getIntExtra("AllPage", defa);
        defa = all;

        last = getIntent().getIntExtra("LastPage", defl);
        defl = last;

        tb = getIntent().getStringExtra("Title");

        check = getIntent().getDoubleExtra("CheckSp", check);

        if ((defsp == 0) || (defl == 0) || (defst == 0) || (defa == 0) || (defp == 0)) {
            savedp = sharedPreferences.getInt("SavedPag", savedp);//сохраненная страница
            all = sharedPreferences.getInt("AllPag", all);//Все страницы
            last = sharedPreferences.getInt("LastPag", last);//На какой остановились
            pages = sharedPreferences.getInt("PagesRea", pages);//страницы
            spentTime = sharedPreferences.getInt("TimeSpen", spentTime);//таймер
            tb = sharedPreferences.getString("Titl", tb);
            //check = sharedPreferences.get("CheckS", check);//режим скорости
            defst = defst + spentTime;
            defp = defp + pages;
            defl = last;
            defa = all;
        }

        // if ((defsp != 0) || (defl != 0) || (defa != 0) || (defp != 0) || (defst != 0)) {
        if (defsp != 1) {
            if (defsp < defp) {
                defp = defp - defsp;
                //Log.e(TAG, Integer.toString(pages));
            } else {
                defp = defsp - defp;
                //Log.e(TAG, Integer.toString(pages));
            }
        }

        if (check == 1) ch = "первом режиме скорости";
        if (check == 2) ch = "втором режиме скорости";
        if (check == 3) ch = "третьем режиме скорости";
        speed = findViewById(R.id.speed);
        speed.setText("При выбраном вами " + ch);

        count = defst / defp * 1000;
        //Log.e(TAG, Double.toString(count));

        if (count < avTime) {
            dif = avTime / count;
            //Toast.makeText(this, String.format("Вы читаете в столько раз быстрее",dif), Toast.LENGTH_LONG).show();
            //Log.e(TAG, Double.toString(dif));
        }
        if (count > avTime) {
            dif = count / avTime;
            //Toast.makeText(this, String.format("Вы читаете в столько раз медленее",dif), Toast.LENGTH_LONG).show();
          //  speed = findViewById(R.id.speed);
          //  speed.setText("Ваша скорость чтения в " + Math.round(dif) + " раз ниже чем средняя");
        }

        t = all / dif;
        if (t < 60) {
            s = "сек.";
        }

        if (t > 60 && t < 3600) {
            s = "мин.";
        }

        if (t > 3600) {
            s = "ч.";
        }

        Log.e(TAG, Double.toString(t));
        prediction = findViewById(R.id.leftPages);
        prediction.setText("Вы закончите книгу с такой же скоростью за " + Math.round(t) + " " + s);

        title = findViewById(R.id.title_book);
        title.setText(tb);
        //Log.e(TAG, "title ==== " + tb);


        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_book = findViewById(R.id.date_book);
        date_book.setText(date);

        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String dst = df.format(today);

        //для прогноза
        all_speed.add(dif);
        all_time.add(t);

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("d MM yyyy", Locale.getDefault());
        String strDate = dateFormat.format(date);

        //База данных
        db = new DBHelper(this);
        if (last != 0 && db.addData(last,check, dst)) {
            Log.e(TAG, dst);
            Toast.makeText(this, "Данные добавлены", Toast.LENGTH_LONG).show();
        }

        left = all - last;
        Log.e(TAG, all_speed.toString());

        //график
        PieChart pieChart = findViewById(R.id.barchart);
        PieDataSet pieDataSet = new PieDataSet(getData(), "");
        pieDataSet.setColors(colorArray);

        PieData pieData = new PieData(pieDataSet);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateXY(5000, 5000);//анимация
        pieChart.setData(pieData);
        pieChart.getLegend().setEnabled(false);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(20);

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int) value) + " ";
            }
        };
        pieDataSet.setValueFormatter(formatter);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    //    //данные для графа
    private ArrayList getData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        //у - значение
        entries.add(new PieEntry(all, "Всего страниц"));
        entries.add(new PieEntry(last, "Прочитано"));
        return entries;
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        setContentView(R.layout.fragment_progress);
        pages = getIntent().getIntExtra("PagesRead", defp);
        defp = pages;
        //Log.e(TAG, Integer.toString(pages));
        spentTime = getIntent().getIntExtra("TimeSpent", defst);
        defst = spentTime;
        //Log.e(TAG, Integer.toString(spentTime));
        savedp = getIntent().getIntExtra("SavedPage", defsp);
        defsp = savedp;
        //Log.e(TAG, Integer.toString(savedp));
        all = getIntent().getIntExtra("AllPage", defa);
        defa = all;
        //Log.e(TAG, Integer.toString(all));
        last = getIntent().getIntExtra("LastPage", defl);
        defl = last;
        //Log.e(TAG, Integer.toString(last));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("SavedPag", savedp);//сохраненная страница
        editor.putInt("AllPag", all);//Все страницы
        editor.putInt("LastPag", last);//На какой остановились
        editor.putInt("PagesRea", pages);//страницы
        editor.putInt("TimeSpen", spentTime);//таймер
        editor.putString("Titl", tb);//имя
        //editor.putInt("CheckS", check);//режим скорости
        editor.apply();

        Intent intent = new Intent(getBaseContext(), AllHistory.class);
        intent.putExtra("Date", date);//дата
        intent.putExtra("RP", pages);//прочитанные страницы
        intent.putExtra("SPE", dif);
       // startActivity(intent);
    }
}
