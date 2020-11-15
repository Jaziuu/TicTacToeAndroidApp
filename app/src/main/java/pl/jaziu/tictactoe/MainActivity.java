package pl.jaziu.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //    tworzenie zmiennych ktore beda  odnosiły sie do tekstu na ekranie
    private TextView playerOneScore, playerTwoScore, playerStatus;
    //   tworzenie zmiennych ktore będa odnosiły się do przycisków na ekranie
    private Button[] buttons = new Button[9];
    private Button resetGame;

    //    zmienne do trzymania wartości aktualnego wyniku, i liczby rund rozegranych
    private int playerOneScoreCount, playerTwoScoreCount, roundCount;

    //    zmienna bedzie pomagać zadecydować, czyj ruch jest teraz
    boolean activePlayer;

    //    gracz1 => 0
    //    gracz2 => 1
    //    empty => 2
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    //    Tablica przypadków w ktorym któryś z graczy wygrywa
    int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},  // wiersze
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},  // kolumny
            {0, 4, 8}, {2, 4, 6} //przekątna
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        "Spinanie" naszych zmiennych ze zmiennymi na ekranie
        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        resetGame = (Button) findViewById(R.id.resetGame);

        for (int i = 0; i < buttons.length; i++) {
            String buttonId = "btn_" + i;
            int resourceId = getResources().getIdentifier(buttonId, "id", getPackageName());
            buttons[i] = (Button) findViewById(resourceId);
            buttons[i].setOnClickListener(this);
        }

//        inicjalizacja poczatkowego stanu gry
        roundCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;

    }

    //    ustawienie akcji przy kliknieciu, logika gry
    @Override
    public void onClick(View view) {
//        jezeli button nie jest pusty, wyjdzie z pętli if
        if (!((Button) view).getText().toString().equals("")) {
            return;
        }

        String buttonId = view.getResources().getResourceEntryName(view.getId());
//        pobieranie samego id buttona -> ostatnia litera w nazwie  btn_2 -> 2
        int gameStatePointer = Integer.parseInt(buttonId.substring(buttonId.length() - 1, buttonId.length())); //2
//         logika zmiany gracza, i tekstu  O X
        if (activePlayer) {
            ((Button) view).setText("X");
            ((Button) view).setTextColor(Color.parseColor("#FFC34A"));
            gameState[gameStatePointer] = 0;
        } else {
            ((Button) view).setText("O");
            ((Button) view).setTextColor(Color.parseColor("#70FFEA"));
            gameState[gameStatePointer] = 1;
        }
        roundCount++;
        if (checkWinner()) {
            if (activePlayer) {
                playerOneScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Wygrał gracz 1 !", Toast.LENGTH_SHORT).show();
                playAgain();
            } else {
                playerTwoScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Wygrał gracz 2 !", Toast.LENGTH_SHORT).show();
                playAgain();

            }
        } else if (roundCount == 9) {
            playAgain();
            Toast.makeText(this, "Remis", Toast.LENGTH_SHORT).show();

        } else {
            activePlayer = !activePlayer;
        }
        if (playerOneScoreCount > playerTwoScoreCount) {
            playerStatus.setText("Wygrywa gracz 1!");
        } else if (playerTwoScoreCount > playerOneScoreCount) {
            playerStatus.setText("Wygrywa gracz 2!");
        } else {
            playerStatus.setText("");
        }

//        spięcie buttona zagraj jeszcze raz z logiką
        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
            }
        });
    }

    //    przy kazdym kliknieciu przycisku sprawdzamy czy mamy zwyciezce
    public boolean checkWinner() {
        boolean winnerResult = false;
        for (int[] winningPosition : winningPositions) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]]
                    && gameState[winningPosition[1]] == gameState[winningPosition[2]]
                    && gameState[winningPosition[0]] != 2
            ) {
                winnerResult = true;
            }
        }

        return winnerResult;
    }

    public void updatePlayerScore() {
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    //    resetowanie gry
    public void playAgain() {
        roundCount = 0;
        activePlayer = true;
        for (int i = 0; i < buttons.length; i++) {
            gameState[i] = 2;
            buttons[i].setText("");
        }
    }

}