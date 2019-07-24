package com.kuntaru.book;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText editTitle = findViewById(R.id.editTitle);
        final EditText editAuthor = findViewById(R.id.editAuthor);

        final EditText editPublisher = findViewById(R.id.editPublisher);

        final EditText editIsbn = findViewById(R.id.ediIsbn);
        Button button = findViewById(R.id.btnSearch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString().trim();
                String author = editAuthor.getText().toString().trim();
                String publisher = editPublisher.getText().toString().trim();
                String isbn = editIsbn.getText().toString().trim();

                if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
                    String message = getString(R.string.searchkey);
                    Toast.makeText(SearchActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    URL queryURL = ApiUtil.buildUrl(title, author, publisher, isbn);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("Query",queryURL);
                    startActivity(intent);
                    Context context = getApplicationContext();
                    int position = SpUtil.getPreferenceInt(context, SpUtil.POSITION);
                    if (position == 0 || position == 5){
                        position = 1;
                    } else {
                        position++;
                    }

                    String key = SpUtil.QUERY +String.valueOf(position);
                    String value =  title + ", " +author +" ," + publisher + ", " + isbn;
                    SpUtil.setPreferenceString(context, key, value);
                    SpUtil.setPreferenceIntent(context, SpUtil.POSITION, position);

                }
            }
        });
    }
}
