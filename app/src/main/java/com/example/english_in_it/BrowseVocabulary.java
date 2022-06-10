package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import activities_menu.StartListActivity;
import learning_sets.SetListRecViewAdapter;

public class BrowseVocabulary extends AppCompatActivity {
    private ConnectionHandler connection_handler;
    private RecyclerView vocabulary_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_browse_vocabulary);

        connection_handler = new ConnectionHandler(BrowseVocabulary.this);

        HashMap<String, String> glossary = ConnectionHandlerUtils.getGlossaryMapTermToDef(connection_handler, 1);
        ArrayList<String> glossary_terms = ConnectionHandlerUtils.getGlossaryJustTerms(connection_handler);
        ArrayList<TermAndDef> vocab = new ArrayList<>();

        for (String term : glossary_terms) {
            vocab.add(new TermAndDef(term, glossary.get(term)));
        }

        vocabulary_view = findViewById(R.id.VocabularyRecRecView);
        VocabularyRecViewAdapter adapter = new VocabularyRecViewAdapter(this);
        adapter.setVocabulary(vocab);
        vocabulary_view.setAdapter(adapter);
        vocabulary_view.setLayoutManager(new LinearLayoutManager(this));
        vocabulary_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu:
                Intent settings_intent = new Intent(BrowseVocabulary.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(BrowseVocabulary.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}