package com.example.daniel.datosquiniela;

import android.content.res.AssetManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = (LinearLayout)findViewById(R.id.activity_main);

        try {
            Result result = BetUtils.XmlToResult(getResources().getXml(R.xml.resultado));
            List<Bet> myBets = BetUtils.readBetsFile(getAssets().open("apuestas1.txt"));
            showWinners(result, myBets);

        } catch (Exception e) {
            Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void showWinners(Result result, List<Bet> myBets){
        int winners10 = 0;
        int winners11 = 0;
        int winners12 = 0;
        int winners13 = 0;
        int winners14 = 0;
        int winners15 = 0;

        for (int i = 0; i < myBets.size(); i++) {
            int resemblance = myBets.get(i).compareResemblance(result.getWinningBet());

            if (resemblance >= 10) {
                switch (resemblance) {
                    case 10:
                        winners10++;
                        break;
                    case 11:
                        winners11++;
                        break;
                    case 12:
                        winners12++;
                        break;
                    case 13:
                        winners13++;
                        break;
                    case 14:
                        winners14++;
                        break;
                    case 15:
                        //If you have 15 matches correct, it still counts as 14
                        winners15++;
                        winners14++;
                        break;
                }
            }
        }

        double prizes15 = winners15 * result.getPrize15();
        double prizes14 = winners14 * result.getPrize14();
        double prizes13 = winners13 * result.getPrize13();
        double prizes12 = winners12 * result.getPrize12();
        double prizes11 = winners11 * result.getPrize11();
        double prizes10 = winners10 * result.getPrize10();

        StringBuffer buffer = new StringBuffer();
        buffer.append("15: "+winners15+"--> "+prizes15+"€\n");
        buffer.append("14: "+winners14+"--> "+prizes14+"€\n");
        buffer.append("13: "+winners13+"--> "+prizes13+"€\n");
        buffer.append("12: "+winners12+"--> "+prizes12+"€\n");
        buffer.append("11: "+winners11+"--> "+prizes11+"€\n");
        buffer.append("10: "+winners10+"--> "+prizes10+"€\n");
        buffer.append("\nTotal --> "+ String.format(Locale.getDefault(), "%.2f",prizes15+prizes14+prizes13+prizes12+prizes11+prizes10)+"€");

        new AlertDialog.Builder(MainActivity.this).setMessage(buffer.toString()).show();
    }
}
