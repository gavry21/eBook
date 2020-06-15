package com.example.android.darktheme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android.darktheme.DataBase.DBHelper;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AllHistory extends AppCompatActivity {

    static final String TAG = "AllHistoryTAG";
    int[] colorArray = new int[]{R.color.piechar1, R.color.piechar2};
    private int all = 0, pages = 0, check = 0;
    private String date, dateCur;
    private int finalValue = 0;
    Double speed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_history);

        //SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        Calendar calendar = Calendar.getInstance();
        dateCur = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date = getIntent().getStringExtra("Date");
        pages += getIntent().getIntExtra("RP", 0);
        speed = getIntent().getDoubleExtra("SPE",0);

//        if ((pages == 0)) {
//            date = sharedPreferences.getString("Dat", date);
//            pages += sharedPreferences.getInt("PL", pages);
//        }
        fillListview();

    }

    public void fillListview() {
        ListView myListview = findViewById(R.id.history_list);
        DBHelper dbhelper = new DBHelper(this);
        ArrayList<InfoObject> infoObjects = dbhelper.getAllData();
        PagesAdapter myAdapter = new PagesAdapter(infoObjects, this);
        myListview.setAdapter(myAdapter);
    }

//    private void viewData() {
//        Cursor cursor = db.viewDat();
//        if (cursor.getCount() == 0) {
//            Toast.makeText(this, "Нет данных", Toast.LENGTH_LONG).show();
//        } else {
//            while (cursor.moveToNext()) {
//                listItem.add(cursor.getString(1));
//            }
//            adapter1 = new ArrayAdapter<>(this,R.layout.all_history_el, R.id.read_pages, listItem);
//            listView.setAdapter(adapter1);
//
//        }
//    }

//    private void openDialog() {
//        AddGoals addGoals = new AddGoals();
//        addGoals.show(getSupportFragmentManager(), "dialog");
//
//    }
//
//    @Override
//    public void applyTexts(String page) {
//
//        finalValue = Integer.parseInt(page);
//        goal_pages.setText(finalValue);
//
//       // AddData(finalValue);
//    }
//
//    public void AddData(double newEntry) {
//        boolean insertData = dbHelper.addData(newEntry);
//
//        if (insertData) {
//            toastMessage("Data Successfully Inserted!");
//        } else {
//            toastMessage("Something went wrong");
//        }
//    }
//
//    /**
//     * customizable toast
//     * @param message
//     */
//    private void toastMessage(String message){
//        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("Dat", date);//дата
//        editor.putInt("PL", pages);// страницы
//
//        Log.e(TAG, Double.toString(finalValue));
//
//        editor.putInt("Want", finalValue);
//        editor.apply();
//
//    }
}
