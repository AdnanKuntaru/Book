package com.kuntaru.book;

import android.content.Intent;
import android.os.AsyncTask;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ProgressBar mLoadingProgress;
    private RecyclerView rbBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rbBooks = findViewById(R.id.rv_books);

        LinearLayoutManager bookLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rbBooks.setLayoutManager(bookLayoutManager);
        mLoadingProgress = findViewById(R.id.pb_loading);

        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");
        URL bookUrl;
        try {
            if (query == null || query.isEmpty()){
                 bookUrl = ApiUtil.buildUrl("cooking");
            } else {
                bookUrl = new URL(query);
            }

            String jsonResult = ApiUtil.getJson(bookUrl);
            new BookQueryTask().execute(bookUrl);
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searItem = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searItem);
        ArrayList<String>  recentList = SpUtil.getQueryList(getApplicationContext());
        int itemNumber = recentList.size();
        MenuItem recentMenu;
        for (int i = 0; i < itemNumber; i++){
            recentMenu = menu.add(Menu.NONE, i, Menu.NONE,recentList.get(i));
        }
//        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return  true;
                default:
                    int position =item.getItemId() + 1 ;
                    String preferenceName = SpUtil.QUERY + String.valueOf(position);
                    String  query = SpUtil.getPreferenceString(getApplicationContext(),preferenceName);
                    String[] prefParams = query.split("\\,");
                    String[] queryParam = new String[4];
                    for (int i = 0; i<prefParams.length; i++){
                        queryParam[i] = prefParams[i];
                    }
                    URL bookUrl = ApiUtil.buildUrl(
                            (queryParam[0] == null) ? "":queryParam[0],
                            (queryParam[1] == null) ? "":queryParam[1],
                            (queryParam[2] == null) ? "":queryParam[2],
                            (queryParam[3] == null) ? "":queryParam[3]

                            );
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {

            URL bookUrl = ApiUtil.buildUrl(query);
            new BookQueryTask().execute(bookUrl);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class BookQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;

            try {
                result = ApiUtil.getJson(searchUrl);

            } catch (IOException e) {
                Log.d("Error", e.getMessage());

            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            mLoadingProgress.setVisibility(View.INVISIBLE);
            TextView tvError = findViewById(R.id.tv_error);
            if (result == null) {
                rbBooks.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            } else {
                rbBooks.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);

            }
            ArrayList<Book> books = ApiUtil.getBooksFromJson(result);
            String resultString = "";

            BooksAdapter adapter = new BooksAdapter(books);
            rbBooks.setAdapter(adapter);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
