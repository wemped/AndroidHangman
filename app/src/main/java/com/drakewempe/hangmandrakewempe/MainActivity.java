package com.drakewempe.hangmandrakewempe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Random;

/*
    Drake Wempe
    CS412 Spring 2015
    Week 3 Assignment
    Hangman App

    Guess characters in a randomly selected word.
    Win by guessing all characters contained in the word.
    You have 6 incorrect guesses before you lose.
    You can check the answer by clicking answer in the top right menu.
    Input guesses by clicking Enter on the keyboard or the guess button.
    Start a new game by clicking the new game button.

 */
public class MainActivity extends ActionBarActivity {

    String[] dictionary;
    Resources res;
    Random rand;
    String currentAnswer;
    EditText guessField;
    TextView answer,guessesUsed,usedChars;
    char[] answerStatus,guessesUsedString;
    ImageView hangmanImageView;
    int guesses = 6,hangmanImageIndex = 0, dictLength;
    Button guessButton,resetButton;
    int[] hangmanImages = {R.drawable.hangman6,R.drawable.hangman5,R.drawable.hangman4,R.drawable.hangman3,R.drawable.hangman2,R.drawable.hangman1,R.drawable.hangman0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Connect all Views from xml to java code
        setContentView(R.layout.activity_main);
        answer = (TextView)findViewById(R.id.answerText);
        guessesUsed = (TextView)findViewById(R.id.guessesUsed);
        guessButton = (Button)findViewById(R.id.guessButton);
        resetButton = (Button)findViewById(R.id.newGameButton);
        guessField = (EditText)findViewById(R.id.guessField);
        usedChars = (TextView)findViewById(R.id.used);
        hangmanImageView = (ImageView)findViewById(R.id.hangmanImageView);

        //Load the dictionary and add first answer
        rand = new Random();
        res = getResources();
        dictionary = res.getStringArray(R.array.dictionary);
        dictLength = dictionary.length;
        currentAnswer = dictionary[rand.nextInt(dictLength-0+1)+0];
        final String questionMarks = new String(new char[currentAnswer.length()]).replace('\0','?');
        answerStatus = questionMarks.toCharArray();
        answer.setText(questionMarks);

        //Do guessButton.onClick() when the user hits the enter button
        guessField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    guessButton.callOnClick();
                }
                return true;
            }
        });

        //Setup for changing the value of # of guesses used
        guessesUsedString = guessesUsed.getText().toString().toCharArray();
        guessesUsed.setText(String.valueOf(guessesUsedString) + " " + Integer.toString(guesses));

        //Prepare an Alert for the losing scenario
        final AlertDialog loseAlert = new AlertDialog.Builder(this).create();
        loseAlert.setTitle(R.string.lose_alert_title);
        loseAlert.setMessage(this.getText(R.string.lose_alert_message).toString() + " " + currentAnswer);
        loseAlert.setButton("OK",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                resetButton.callOnClick();
            }
        });

        //Prepare an Alert for the winning scenario
        final AlertDialog winAlert = new AlertDialog.Builder(this).create();
        winAlert.setTitle(R.string.win_alert_title);
        winAlert.setMessage(this.getText(R.string.alert_message).toString());
        winAlert.setButton("OK",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                resetButton.callOnClick();
            }
        });

        //Game Logic occurs when user makes a guess
        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentGuess = guessField.getText().toString();
                //Check that the guess is only one character
                if (currentGuess.length() != 1){
                    Toast.makeText(getApplicationContext(), "Enter a single letter!",
                        Toast.LENGTH_LONG).show();
                    return;
                }
                //Check that the guess is not a repeat
                if (String.valueOf(usedChars.getText()).contains(currentGuess)){
                    Toast.makeText(getApplicationContext(), "You've already used that letter!",
                            Toast.LENGTH_LONG).show();
                    guessField.setText("");
                    return;
                }
                //Check if the guess is a correct guess
                if (currentAnswer.contains(currentGuess)){
                    //Fill in all question marks of the correct guess
                    int j = 0;
                    j = currentAnswer.indexOf(currentGuess,j);
                    while(j!=-1){
                        answerStatus[j] = currentGuess.charAt(0);
                        Log.v("answerStatus", answerStatus.toString());
                        j = currentAnswer.indexOf(currentGuess,j+1);
                    }
                    answer.setText(String.valueOf(answerStatus));
                    usedChars.append(currentGuess);
                    //If this created a winning state, show winning alert
                    if (!String.valueOf(answerStatus).contains("?")){
                        winAlert.show();
                    }
                }else{
                    //Decrement guess counters and add character to used characters
                    usedChars.append(currentGuess);
                    guesses--;
                    guessesUsed.setText(String.valueOf(guessesUsedString) + " " + Integer.toString(guesses));
                    Log.v("hangmanImageIndex", Integer.toString(hangmanImageIndex));
                    //If this created a losing state, show losing alert
                    if (hangmanImageIndex == 5){
                        loseAlert.show();
                    }
                    hangmanImageIndex++;
                    hangmanImageView.setImageResource(hangmanImages[hangmanImageIndex]);
                }
                guessField.setText("");
            }
        });

        //Reset everything when new game button is clicked
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New answer
                currentAnswer = dictionary[rand.nextInt(dictLength-0+1)+0];
                final String questionMarks = new String(new char[currentAnswer.length()]).replace('\0','?');
                answerStatus = questionMarks.toCharArray();
                loseAlert.setMessage(loseAlert.getContext().getText(R.string.lose_alert_message) + " " + currentAnswer);
                //Set guesses back
                guesses = 6;
                guessesUsed.setText(String.valueOf(guessesUsedString) + " " + Integer.toString(guesses));
                answer.setText(questionMarks);
                hangmanImageIndex = 0;
                hangmanImageView.setImageResource(hangmanImages[hangmanImageIndex]);
                usedChars.setText("");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //Show the answer in an alert
        int id = item.getItemId();
        if (id == R.id.reveal_answer){
            final AlertDialog answerAlert = new AlertDialog.Builder(this).create();
            answerAlert.setTitle(R.string.reveal_answer_title);
            answerAlert.setMessage(currentAnswer);
            answerAlert.setButton("OK",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                }
            });
            answerAlert.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
