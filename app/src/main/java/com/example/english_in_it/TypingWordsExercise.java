package com.example.english_in_it;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import activities_menu.StartListActivity;


public class TypingWordsExercise extends AppCompatActivity {

    ArrayList<Word> words = new ArrayList<Word>();
    //ArrayList<Word> words = (ArrayList<Word>) getIntent().getSerializableExtra("words");

    private ConnectionHandler connection_handler;

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    public TextView meaning;
    public Button check_button;
    public EditText word;
    public TextView good_bad;
    public TextView correct_answer;
    //private Iterator<Word> iter = words.iterator();
    private Word current_word;
    private int counter_for_clicks;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Word word1 = new Word("abstract method","Method with only a signature and no implementation body.",0,0,new Date());
        Word word2 = new Word("algorithm","An unambiguous specification of how to solve a class of problems.",0,0,new Date());
        words.add(word1);
        words.add(word2);*/
        //Bundle b = getIntent().getExtras();
        //ArrayList<Word> words = (ArrayList<Word>) getIntent().getParcelableExtra("words");
        //Intent intent = getIntent();
        Bundle repetitions_bundle = getIntent().getExtras();
        Boolean repetitions = repetitions_bundle.getBoolean("repetitions");
        connection_handler = new ConnectionHandler(TypingWordsExercise.this);

        if(repetitions) {
            ArrayList<String> all_sets = connection_handler.getAllLearningSets();
            for(int i = 0; i < all_sets.size(); i++) {
                Date today = new Date();
                //connection_handler.setWordRepetitionDate("bit", today);
                try {
                    ArrayList<Word> current_list = connection_handler.getLearningSetList(all_sets.get(i));
                    for(int j = 0; j < current_list.size(); j++) {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                        String formated_today = formatter.format(today);
                        if(current_list.get(j).when_to_remind != null && formated_today.equals(formatter.format(current_list.get(j).when_to_remind))) {
                            words.add(current_list.get(j));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        else {

        }

        Iterator<Word> iter = words.iterator();

        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setTheme(Utils.getTheme(pref.getString("theme", null)));
        setContentView(R.layout.activity_typing_words);



        meaning = findViewById(R.id.meaning);
        check_button = findViewById(R.id.check_button);
        word = findViewById(R.id.word);
        good_bad = findViewById(R.id.good_bad);
        correct_answer = findViewById(R.id.correct_answer);

        current_word = iter.next();
        meaning.setText(current_word.meaning);
        counter_for_clicks = 0;
        correct_answer.setText("");
        good_bad.setText("");
        check_button.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                if(counter_for_clicks%2 == 0) {//wpisywanie tłumaczenia, po którym pojawia się feedback
                    correct_answer.setText("");
                    good_bad.setText("");
                    String entered_word = word.getText().toString();
                    if (entered_word.equals(current_word.word)) {
                        good_bad.setText("CORRECT!");
                        //good_bad.setTextColor(Color.parseColor("0xff00ff00"));
                    } else {
                        good_bad.setText("INCORRECT");
                        correct_answer.setText("correct answer: " + current_word.word);
                        //good_bad.setTextColor(Color.parseColor("0xffff0000"));
                    }
                    check_button.setText("CONTINUE");
                    counter_for_clicks++;
                }
                else {//pojawia się nowe słówko
                    //good_bad.setTextColor(Color.parseColor("0xff000000"));
                    correct_answer.setText("");
                    good_bad.setText("");
                    check_button.setText("CHECK");
                    if (!iter.hasNext()) {
                        Intent intent = new Intent(TypingWordsExercise.this, StartListActivity.class);
                        startActivity(intent);
                        return;
                    }
                    current_word = iter.next();
                    meaning.setText(current_word.meaning);
                    word.setText("");
                    counter_for_clicks++;
                }
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu:
                Intent settings_intent = new Intent(TypingWordsExercise.this, Settings.class);
                startActivity(settings_intent);
                return true;
            case R.id.home_menu:
                Intent home_intent = new Intent(TypingWordsExercise.this, StartListActivity.class);
                startActivity(home_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
