package com.example.android.darktheme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;

public class Library_VIew extends Fragment {

    static final String TAG = "VectorDrawableFragmentTag";

    ListView lv_pdf;
    public static ArrayList<File> fileList = new ArrayList<>();//pdf
    public static ArrayList<File> fileList1 = new ArrayList<>();//epub
    PDFAdapter obj_adapter;
    public static int REQUSET_PERMISSION = 1;
    boolean boolean_permision;
    File dir;



//    public static final String LIGHT_MODE = "light";
//    public static final String DARK_MODE = "dark";
//
//    private SearchView searchView = null;
//    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_viewer, container, false);
        //pdf
        lv_pdf = view.findViewById(R.id.listview_pdf);
        dir = new File(Environment.getExternalStorageDirectory().toString());

        permission_fn();
        lv_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ViewPDFFiles.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        return view;
    }


    private void permission_fn() {
        if ((ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUSET_PERMISSION);
            }

        } else {

            boolean_permision = true;
            getfile(dir);
            obj_adapter = new PDFAdapter(getActivity().getApplicationContext(), fileList);
            lv_pdf.setAdapter(obj_adapter);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUSET_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boolean_permision = true;
                getfile(dir);
                obj_adapter = new PDFAdapter(getActivity().getApplicationContext(), fileList);
                lv_pdf.setAdapter(obj_adapter);
            } else {
                Toast.makeText(getActivity(), "Пожалуйста, дайте разрешение", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    getfile(listFile[i]);
                } else {
                    boolean booleanpdf = false;
                    if (listFile[i].getName().endsWith(".pdf")) {
                        for (int j = 0; j < fileList.size(); j++) {
                            if (fileList.get(j).getName().equals(listFile[i].getAbsolutePath())) {
                                booleanpdf = true;

                            } else {

                            }
                        }
                        if (booleanpdf) {
                            booleanpdf = false;
                        } else {
                            fileList.add(listFile[i]);
                        }
                    }
                }

            }
        }
        return fileList;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.toolbar_serch_filter, menu);
//        MenuItem searchItem = menu.findItem(R.id.search_tool);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        if (searchView != null) {
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    return false;
//                }
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    obj_adapter.getFilter().filter(newText);
//                    return true;
//                }
//            });
//        }
//    }
    //поиск
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.toolbar_serch_filter, menu);
//        //SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        MenuItem item = menu.findItem(R.id.search_tool);
//        //SearchView searchView = (SearchView) item.getActionView();
//        SearchManager searchManager = (SearchManager)  getActivity().getSystemService(Context.SEARCH_SERVICE);
////        searchView = (SearchView) menu.findItem(R.id.search_tool)
////                .getActionView();
////        searchView.setSearchableInfo(searchManager
////                .getSearchableInfo(getActivity().getComponentName()));
////        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        if (item != null) {
//            searchView = (SearchView) item.getActionView();
//        }
//        if (searchView != null) {
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String query) {
////                obj_adapter.getFilter().filter(query);
////                return false;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String newText) {
////                obj_adapter.getFilter().filter(newText);
////                return false;
////            }
////        });
////            searchView.setOnQueryTextListener(queryTextListener);
////        }
////        super.onCreateOptionsMenu(menu, inflater);
////    }
//
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//            queryTextListener = new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextChange(String newText) {
//
//                    ((PDFAdapter)lv_pdf.getAdapter()).getFilter().filter(newText);
//                    return false;
//                }
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    ((PDFAdapter)lv_pdf.getAdapter()).getFilter().filter(query);
//                    return false;
//                }
//            };
//            searchView.setOnQueryTextListener(queryTextListener);
//        }
//        super.onCreateOptionsMenu(menu, inflater);
//    }

}
