package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();
    private EditText editText;
    private String search_url;
    private ListView listView;
    private BookAdapter adapter;
    private Button search_button;
    private ProgressBar loading;
    private TextView situation;
    TextInputLayout main_search;
    ConnectivityManager connect;
    NetworkInfo network;
    String search_query;
    private static final String REQUEST_URL ="https://www.googleapis.com/books/v1/volumes?q=flowers+inauthor:keyes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loading = (ProgressBar) findViewById(R.id.loading);
        main_search = (TextInputLayout) findViewById(R.id.main_search);
        situation = (TextView) findViewById(R.id.situation);

        connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        network = connect.getActiveNetworkInfo();
        if (network != null && network.isConnected()) {
            situation.setText(R.string.connected);

            situation.setVisibility(View.GONE);
        } else {
            situation.setText(R.string.not_connected);
            situation.setVisibility(View.VISIBLE);
        }
        loading.setVisibility(View.INVISIBLE);
        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.search_result);
        search_button = (Button) findViewById(R.id.search_button);
        adapter = new BookAdapter(MainActivity.this, new ArrayList<googleBook>());
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
                    InputMethodManager ii = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    ii.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                editText = (EditText) findViewById(R.id.search_result);
                search_query = editText.getText().toString();
                if ( search_query != null && search_query.length() == 0) {
                    main_search.setErrorEnabled(true);
                    main_search.setError("Field can't be empty!");

                } else {
                    main_search.setError("");
                    main_search.setErrorEnabled(false);

                   // search_query = search_query.replaceAll(" ", "+");
                    search_url = REQUEST_URL ;
                    listView.setAdapter(adapter);

                    connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    network = connect.getActiveNetworkInfo();
                    if (network != null && network.isConnected()) {
                        situation.setText(R.string.connected);
                        situation.setVisibility(View.GONE);
                        loading.setVisibility(View.VISIBLE);
                        adapter.clear();
                        Log.d(LOG_TAG, "Internet Connected");
                        Log.d(LOG_TAG, search_query);
                        BookAsyncTask task = new BookAsyncTask();
                        task.execute(search_url);
                    } else {
                        situation.setText(R.string.not_connected);
                        situation.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                googleBook current_book = adapter.getItem(position);
                Uri bookUri = Uri.parse(current_book.getUrl());

                Intent book = new Intent(Intent.ACTION_VIEW, bookUri);

                startActivity(book);
            }
        });
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<googleBook>> {


        @Override
        protected List<googleBook> doInBackground(String... urls) {
            Log.d(LOG_TAG, "doInBackground process started.....");
            if (urls.length < 1 || urls[0] == null)
                return null;
            List<googleBook> list_of_books = Utils.fetchGoogleBookData(urls[0]);
            return list_of_books;
        }

        @Override
        protected void onPostExecute(List<googleBook> books) {
            Log.d(LOG_TAG, "onPostExecute process started.....");
            adapter.clear();
            if (books != null && !books.isEmpty()) {
                loading.setVisibility(View.INVISIBLE);
                adapter.addAll(books);
                Log.d(LOG_TAG, "Books added.....");
            }
        }
    }

}

