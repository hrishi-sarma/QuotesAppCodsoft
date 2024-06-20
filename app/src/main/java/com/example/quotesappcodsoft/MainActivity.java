package com.example.quotesappcodsoft;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button showSavedQuotesButton;
    private Button newQuotesButton;
    private Button saveButton;
    private Button shareButton;
    private TextView editTextQuote;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "QuotesAppPrefs";
    private static final String SAVED_QUOTES_KEY = "SavedQuotes";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        showSavedQuotesButton = findViewById(R.id.savedQuotesButton);
        newQuotesButton = findViewById(R.id.newQuotesButton);
        saveButton = findViewById(R.id.saveButton);
        shareButton = findViewById(R.id.shareButton);
        editTextQuote = findViewById(R.id.editTextQuote);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        showSavedQuotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SavedQuotesActivity.class);
                startActivity(intent);
            }
        });


        newQuotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new FetchQuoteTask().execute();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveCurrentQuote();
            }
        });


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareCurrentQuote();
            }
        });


        new FetchQuoteTask().execute();
    }

    private void saveCurrentQuote() {
        String currentQuote = editTextQuote.getText().toString();
        Set<String> savedQuotes = sharedPreferences.getStringSet(SAVED_QUOTES_KEY, new HashSet<>());
        savedQuotes.add(currentQuote);
        sharedPreferences.edit().putStringSet(SAVED_QUOTES_KEY, savedQuotes).apply();
    }

    private void shareCurrentQuote() {
        String currentQuote = editTextQuote.getText().toString();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, currentQuote);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Quote via"));
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchQuoteTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://zenquotes.io/api/random");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONArray jsonArray = new JSONArray(response.toString());
                JSONObject quoteObject = jsonArray.getJSONObject(0);
                return quoteObject.getString("q") + " - " + quoteObject.getString("a");
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed to fetch quote.";
            }
        }

        @Override
        protected void onPostExecute(String quote) {
            editTextQuote.setText(quote);
        }
    }
}
