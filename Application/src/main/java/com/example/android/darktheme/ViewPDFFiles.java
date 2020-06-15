package com.example.android.darktheme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;
import java.util.List;


public class ViewPDFFiles extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageScrollListener {

    PDFView pdfView;
    int position = -1;
    int savedPage;
    int pageNumber;
    int readPages, all, timeSpent, startP;
    private static final String TAG = ViewPDFFiles.class.getSimpleName();
    boolean stop, pause;

    private Thread t;
    String title;
    private float e = 0.025f, n;
    double check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        t = new Thread() {
            @Override
            public void run() {
                while (!stop) {
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeSpent++;
                                // Log.e(TAG, Integer.toString(timeSpent));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

        pdfView = (PDFView) findViewById(R.id.pdfview);
        position = getIntent().getIntExtra("position", -1);
        pdfView.getPageCount();
        //To retrieve the  number of the last page from your shared preferences file
        SharedPreferences mySharedPreferences = getPreferences(Context.MODE_PRIVATE);
        savedPage = mySharedPreferences.getInt("retrievedPage", 0);
        n = mySharedPreferences.getFloat("Pos", 0);
        Log.e(TAG, "nnnnnnnnnnnnn" + n);
        startP = savedPage;
        pageNumber = savedPage;
        displayPDF();
    }

    public void changePos(int s) {
        final float y;
        y = (float) 1 / (float) s * e;
        //Log.e(TAG, "77777777777 " + y);
        t = new Thread() {
            @Override
            public void run() {
                while (!pause) {
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                n = n + y;
                                if (!pause) {
                                    if (pdfView.getPositionOffset() != 0) {
                                        pdfView.setPositionOffset(n);
                                        Log.e(TAG, Float.toString(n));
                                    }
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // This demonstrates how to programmatically tint a drawable
//        MenuItem item = menu.findItem(R.id.action_more);
//        Drawable drawableWrap = DrawableCompat.wrap(item.getIcon()).mutate();
//        DrawableCompat.setTint(drawableWrap, ColorUtils.getThemeColor(this, R.attr.colorOnPrimary));
//        item.setIcon(drawableWrap);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.dark_mode:
                Toast.makeText(this, "Темная Тема", Toast.LENGTH_SHORT).show();
                pdfView.setNightMode(true);
                return true;
            case R.id.light_mode:
                Toast.makeText(this, "Светлая Тема", Toast.LENGTH_SHORT).show();
                pdfView.setNightMode(false);
                return true;
                //скорости
            case R.id.speed_1:
                Toast.makeText(this, "Первая скорость", Toast.LENGTH_SHORT).show();
                e = 0.025f;
                check = 1;
                return true;
            case R.id.speed_2:
                Toast.makeText(this, "Вторая скорость", Toast.LENGTH_SHORT).show();
                e = 0.015f;
                check = 2;
                return true;
            case R.id.speed_3:
                Toast.makeText(this, "Третья скорость", Toast.LENGTH_SHORT).show();
                e = 0.01f;
                check = 3;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        int id = item.getItemId();
//        if (id == R.id.dark_mode) {
//            // TODO
//            return true;
//        }
    }

    public void displayPDF() {
        pdfView.fromFile(Library_VIew.fileList.get(position))
                .defaultPage(pageNumber)
                //.enableSwipe(true)
                //.swipeHorizontal(false)
                .enableDoubletap(true)
                .onPageChange(this)
                .onPageScroll(this)
                .onLoad(this)
                .enableAnnotationRendering(true)
                //.scrollHandle(new DefaultScrollHandle(this))//номер страницы
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .fitEachPage(true)
                .pageFitPolicy(FitPolicy.WIDTH)
                // .pageSnap(true)//перелистывание одной страницы
                //.pageFling(true)
                .nightMode(false)
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        setTitle(String.format("%s %s / %s", pdfView, page + 1, pageCount));
        readPages = page + 1;
    }


    @Override
    public void onPageScrolled(int page, float positionOffset) {
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-/");
        Log.e(TAG, "title = " + meta.getTitle());
        all = nbPages;
        title = meta.getTitle();
        //Log.e(TAG, "ALL = " + pdfView.getPageCount());
        //pdfView.setPositionOffset(n);
        changePos(all);
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        pause = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        pause = true;
        stop = true;
        savedPage = pdfView.getCurrentPage();
        all = pdfView.getPageCount();
        n = pdfView.getPositionOffset();
        //сreate a shared preference file
        SharedPreferences mySharedPreferences = getPreferences(Context.MODE_PRIVATE);
        //to write to this shared preference file
        SharedPreferences.Editor myEditor = mySharedPreferences.edit();
        //store the page number you left it
        myEditor.putInt("retrievedPage", savedPage);
        myEditor.putFloat("Pos", n);
        //to save changes
        myEditor.apply();
        //отправка данных
        //Log.e(TAG, String.format("Pages ", readPages));
        Intent intent = new Intent(getBaseContext(), Progress_Activity.class);
        intent.putExtra("SavedPage", startP + 1);//сохраненная страница
        intent.putExtra("AllPage", all);//Все страницы
        intent.putExtra("LastPage", savedPage + 1);//На какой остановились
        intent.putExtra("PagesRead", readPages);//страницы
        intent.putExtra("TimeSpent", timeSpent);//таймер
        intent.putExtra("Title", title);//имя
        intent.putExtra("CheckSp", check);//режим
        startActivity(intent);
    }

}
