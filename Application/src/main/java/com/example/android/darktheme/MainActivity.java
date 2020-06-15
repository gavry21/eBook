/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.darktheme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "Main_TAg";
    int pages;
    private Context context;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(WelcomeFragment.TAG);
                    return true;
                case R.id.navigation_progress:
                    intent = new Intent(MainActivity.this, Progress_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MainActivity.this.startActivity(intent);
                    return true;
                case R.id.all_history:
                    intent = new Intent(MainActivity.this, AllHistory.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MainActivity.this.startActivity(intent);
                    return true;
                case R.id.planner:
                    intent = new Intent(MainActivity.this, CalenderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MainActivity.this.startActivity(intent);
                    return true;
                case R.id.navigation_settings:
                    showFragment(SettingsFragment.TAG);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    
        context = getApplicationContext();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationListener);

        if (savedInstanceState == null) {
            showFragment(Library_VIew.TAG);
        }

        //получаем данные о страницах
        pages = getIntent().getIntExtra("PagesRead", 0);
        //Log.e(TAG, String.format("%s", pages));
        //Toast.makeText(this, String.valueOf(pages), Toast.LENGTH_LONG).show();
    }



    //поиск и фильтр
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_serch_filter, menu);
//
//        // This demonstrates how to programmatically tint a drawable
//        MenuItem item = menu.findItem(R.id.search_tool);
//
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setQueryHint("Нажмите для поиска");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        Drawable drawableWrap = DrawableCompat.wrap(item.getIcon()).mutate();
//        DrawableCompat.setTint(drawableWrap, ColorUtils.getThemeColor(this, R.attr.colorOnPrimary));
//        item.setIcon(drawableWrap);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_more) {
//            // TODO
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void showFragment(@NonNull String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            switch (tag) {
                case Library_VIew.TAG: {
                    fragment = new Library_VIew();
                    break;
                }
//                case Progress_Activity.TAG:{
//                    fragment = new Progress_Activity();
//                    break;
//                }

//                case PreferencesFragment.TAG: {
//                    fragment = new PreferencesFragment();
//                    break;
//                }
                case SettingsFragment.TAG: {
                    fragment = new SettingsFragment();
                    break;
                }
                default: {
                    fragment = new Library_VIew();
                    break;
                }
            }
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_layout, fragment, tag)
                .commit();
    }



}
