package com.example.english_in_it;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BrowseVocabulary extends AppCompatActivity {
    private ConnectionHandler connection_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_vocabulary);

        connection_handler = new ConnectionHandler(BrowseVocabulary.this);

        ArrayList<String> glossary = connection_handler.getGlossary();

        ListView menuList = findViewById(R.id.menuElementsList);
        ArrayAdapter<String> menu_adapter = new ArrayAdapter<>(
                BrowseVocabulary.this, android.R.layout.simple_list_item_1, glossary);

        menuList.setAdapter(menu_adapter);
    }
}