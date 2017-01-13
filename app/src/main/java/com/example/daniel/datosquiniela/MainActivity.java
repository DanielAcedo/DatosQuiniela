package com.example.daniel.datosquiniela;

import android.content.res.AssetManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = (LinearLayout)findViewById(R.id.activity_main);

        try {
            Result result = BetUtils.XmlToResult(getResources().getXml(R.xml.resultado));
            Snackbar.make(root, String.valueOf(result.getWinningBet().getGoalNumbers()[1]), Snackbar.LENGTH_INDEFINITE).show();
        } catch (Exception e) {
            Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
        }
    }
}
