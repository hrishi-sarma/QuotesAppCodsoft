package com.example.quotesappcodsoft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class SavedQuotesActivity extends AppCompatActivity {

    private LinearLayout quoteContainer;
    private Button backToHomeButton;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "QuotesAppPrefs";
    private static final String SAVED_QUOTES_KEY = "SavedQuotes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedlayout);


        quoteContainer = findViewById(R.id.quoteContainer);


        backToHomeButton = findViewById(R.id.backToHomeButton);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        showSavedQuotes();


        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SavedQuotesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showSavedQuotes() {
        Set<String> savedQuotes = sharedPreferences.getStringSet(SAVED_QUOTES_KEY, new HashSet<>());


        quoteContainer.removeAllViews();


        for (String quote : savedQuotes) {
            View quoteItem = LayoutInflater.from(this).inflate(R.layout.activity_saved, quoteContainer, false);
            TextView quoteText = quoteItem.findViewById(R.id.quoteText);
            quoteText.setText(quote);

            quoteContainer.addView(quoteItem);
        }
    }
}
