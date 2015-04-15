package com.drakewempe.hangmandrakewempe;

import android.content.res.Resources;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    String[] dictionary;
    Resources res;
    Random rand;
    String currentAnswer;
    EditText guessField;
    TextView answer,guessesUsed,usedChars;
    char[] answerStatus;
    ImageView hangmanImageView;
    int guesses = 6,hangmanImageIndex = 0, dictLength;
    Button guessButton,resetButton;
    int[] hangmanImages = {R.drawable.hangman6,R.drawable.hangman5,R.drawable.hangman4,R.drawable.hangman3,R.drawable.hangman2,R.drawable.hangman1,R.drawable.hangman0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        //DEBUG
        guessesUsed.setText(currentAnswer);
        //DEBUG

        answer.setText(questionMarks);
        //Load hangman images into an array
        //hangmanImages = getResources().getIntArray(R.array.hangmanImages);


        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentGuess = guessField.getText().toString();
                if (currentGuess.length() != 1){
                    Toast.makeText(getApplicationContext(), "Enter a single letter!",
                        Toast.LENGTH_LONG).show();
                    return;
                }
                if (currentAnswer.contains(currentGuess)){
                    int j = 0;
                    j = currentAnswer.indexOf(currentGuess,j);
                    Log.d("j", Integer.toString(j));
                    while(j!=-1){
                        //Log.v("debug","yo");
                        answerStatus[j] = currentGuess.charAt(0);
                        Log.v("answerStatus", answerStatus.toString());
                        j = currentAnswer.indexOf(currentGuess,j+1);
                    }
                    answer.setText(String.valueOf(answerStatus));
                    usedChars.append(currentGuess);

                }else{
                    usedChars.append(currentGuess);
                    if (hangmanImageIndex == 6){
                        //youlose
                    }
                    hangmanImageIndex++;
                    hangmanImageView.setImageResource(hangmanImages[hangmanImageIndex]);


                }
                guessField.setText("");
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load the dictionary and add first answer
                //rand = new Random();
                //res = getResources();
                //dictionary = res.getStringArray(R.array.dictionary);
                //int dictLength = dictionary.length;
                currentAnswer = dictionary[rand.nextInt(dictLength-0+1)+0];
                final String questionMarks = new String(new char[currentAnswer.length()]).replace('\0','?');
                answerStatus = questionMarks.toCharArray();


                //DEBUG
                guessesUsed.setText(currentAnswer);
                //DEBUG

                answer.setText(questionMarks);

                hangmanImageIndex = 0;
                hangmanImageView.setImageResource(hangmanImages[hangmanImageIndex]);

                usedChars.setText("");
                guessesUsed.setText(R.string.guesses_used);

                //Load hangman images into an array
                //hangmanImages = getResources().getIntArray(R.array.hangmanImages);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
