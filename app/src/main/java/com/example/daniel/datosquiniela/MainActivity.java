package com.example.daniel.datosquiniela;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public static final String RESULT_FILE_XML = "http://alumno.club/superior/daniel/resultado.xml";
    public static final String RESULT_FILE_JSON = "http://alumno.club/superior/daniel/resultado.json";
    public static final String UPLOAD_PHP = "http://alumno.club/superior/daniel/upload.php";

    private LinearLayout root;
    private Button btn_calculate;
    private EditText edt_betFile;
    private EditText edt_output;
    private RadioGroup rg_fileType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = (LinearLayout)findViewById(R.id.activity_main);
        edt_betFile = (EditText)findViewById(R.id.edt_betsFile);
        edt_output = (EditText)findViewById(R.id.edt_prizesFile);
        rg_fileType = (RadioGroup)findViewById(R.id.rg_FileType);

        btn_calculate = (Button)findViewById(R.id.btn_calculate);
        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String betFile = edt_betFile.getText().toString();
                int radioChecked = rg_fileType.getCheckedRadioButtonId();

                if(radioChecked == R.id.rb_JSON){
                    downloadJSON(betFile);
                }
                else if (radioChecked == R.id.rb_XML){
                    downloadXML();
                }
            }
        });

    }

    private Winners showWinners(Result result, List<Bet> myBets){
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

        return new Winners(winners10, winners11, winners12, winners13, winners14, winners15, prizes15,prizes14, prizes13, prizes12, prizes11, prizes10, Math.round((prizes10+prizes11+prizes12+prizes13+prizes14+prizes15) * 100.0)/100.0);
    }

    private void downloadJSON(final String file){
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Descargando...");
        dialog.show();

        //Download result file
        RestClient.get(RESULT_FILE_JSON, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    final Result result = BetUtils.JSONToResult(response);

                    //Download bets file
                    RestClient.get(file, new FileAsyncHttpResponseHandler(MainActivity.this) {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File response) {
                            Toast.makeText(MainActivity.this, "Error al conectar", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File response) {
                            try {
                                List<Bet> bets = BetUtils.readBetsFile(new FileInputStream(response));
                                uploadWinnersFileJSON(BetUtils.winnersToJson(showWinners(result, bets)));
                                dialog.dismiss();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Archivo de apuestas no encontrado", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dialog.dismiss();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error de formato fichero JSON", Toast.LENGTH_SHORT).show();
                } catch (BetParameterLengthNotValidException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error de formato fichero apuestas", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (BetFormatNotValidException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error de formato fichero apuestas", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MainActivity.this, "Error al conectar", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void uploadWinnersFileJSON(JSONObject json) throws IOException, JSONException {
        RequestParams params = new RequestParams();

        final File jsonFile = new File(getFilesDir(), edt_output.getText().toString());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsonFile)));
        writer.write(json.toString(3));
        writer.close();
        params.add("secretKey", "123");
        params.put("fileToUpload", jsonFile);

        RestClient.post(UPLOAD_PHP, params , new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                Toast.makeText(MainActivity.this, responseBody , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable throwable) {
                Toast.makeText(MainActivity.this, "Problema al subir  "+jsonFile.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadWinnersFileXML(String xml) throws IOException, JSONException {
        RequestParams params = new RequestParams();

        final File xmlFile = new File(getFilesDir(), edt_output.getText().toString());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlFile)));
        writer.write(xml);
        writer.close();
        params.add("secretKey", "123");
        params.put("fileToUpload", xmlFile);

        RestClient.post(UPLOAD_PHP, params , new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                Toast.makeText(MainActivity.this, responseBody , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable throwable) {
                Toast.makeText(MainActivity.this, "Problema al subir  "+xmlFile.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void downloadXML(){
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Descargando...");
        dialog.show();

        //Download result file
        RestClient.get(RESULT_FILE_XML, new FileAsyncHttpResponseHandler(MainActivity.this){
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                try {
                    final Result result = BetUtils.XmlToResult(response);

                    //Download bets file
                    RestClient.get(edt_betFile.getText().toString(), new FileAsyncHttpResponseHandler(MainActivity.this) {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File response) {
                            Toast.makeText(MainActivity.this, "Error al conectar", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File response) {
                            try {
                                List<Bet> bets = BetUtils.readBetsFile(new FileInputStream(response));
                                uploadWinnersFileXML(BetUtils.winnersToXml(showWinners(result, bets)));
                                dialog.dismiss();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Archivo de apuestas no encontrado", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dialog.dismiss();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error de formato fichero JSON", Toast.LENGTH_SHORT).show();
                } catch (BetParameterLengthNotValidException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error de formato fichero apuestas", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (BetFormatNotValidException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error de formato fichero apuestas", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File errorResponse) {
                Toast.makeText(MainActivity.this, "Error al conectar", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
