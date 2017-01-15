package com.example.daniel.datosquiniela;

import android.util.Xml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daniel on 12/01/2017.
 */

public class BetUtils {

    private static double XmlMoneyBetValueToDouble(String value){
        StringBuffer buffer = new StringBuffer(value);

        if(buffer.length() >= 4)
            buffer.insert(buffer.length()-4, ".");
        else{
            buffer.insert(0, "0");
            buffer.insert(1, ".");

            for(int i = 0; i < 4-value.length();i++){
                buffer.insert(2+i, "0");
            }
        }

        return Double.parseDouble(buffer.toString());
    }

    private static double XmlValueToDouble(String value){
        StringBuffer buffer = new StringBuffer(value);

        if(buffer.length() >= 2)
            buffer.insert(buffer.length()-2, ".");
        else{
            buffer.insert(0, "0");
            buffer.insert(1, ".");

            for(int i = 0; i < 2-value.length();i++){
                buffer.insert(2+i, "0");
            }
        }

        return Double.parseDouble(buffer.toString());
    }

    public static Result XmlToResult(File file) throws Exception {
        Bet tmpBet = null;

        String[] matchResults = new String[14];
        String[] goalNumber = new String[2];

        double moneyBet = 0.0;
        double prize15 = 0.0;
        double prize14 = 0.0;
        double prize13 = 0.0;
        double prize12 = 0.0;
        double prize11 = 0.0;
        double prize10 = 0.0;

        boolean stopReading = false;
        boolean inPool = false;
        boolean foundPool = false;
        String tag;

        int matchCounter = 0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuffer buffer = new StringBuffer();
        String line;
        while((line = reader.readLine())!=null){
            buffer.append(line.replaceAll("^.*<", "<"));
        }

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(new StringReader(buffer.toString()));
        int event = parser.next();

        while(event != XmlPullParser.END_DOCUMENT && !stopReading){
            switch (event){
                case XmlPullParser.START_TAG:
                    tag = parser.getName();

                    if(tag.equals("quiniela")){

                        if(!parser.getAttributeValue(3).equals("0") || !parser.getAttributeValue(4).equals("0") || !parser.getAttributeValue(5).equals("0")
                                || !parser.getAttributeValue(6).equals("0") || !parser.getAttributeValue(7).equals("0") || !parser.getAttributeValue(8).equals("0")){
                            inPool = true;
                            int jornada = Integer.parseInt(parser.getAttributeValue(0));
                            prize15 = XmlValueToDouble(parser.getAttributeValue(3));
                            prize14 = XmlValueToDouble(parser.getAttributeValue(4));
                            prize13 = XmlValueToDouble(parser.getAttributeValue(5));
                            prize12 = XmlValueToDouble(parser.getAttributeValue(6));
                            prize11 = XmlValueToDouble(parser.getAttributeValue(7));
                            prize10 = XmlValueToDouble(parser.getAttributeValue(8));
                            moneyBet = XmlMoneyBetValueToDouble(parser.getAttributeValue(9));

                        }else if(parser.getAttributeValue(3).equals("0") && parser.getAttributeValue(4).equals("0") && parser.getAttributeValue(5).equals("0")
                                && parser.getAttributeValue(6).equals("0") && parser.getAttributeValue(7).equals("0") && parser.getAttributeValue(8).equals("0")){
                            inPool = true;
                            foundPool = true;
                        }

                        if(foundPool){
                            stopReading = true;
                        }

                    }
                    else if(inPool && tag.equals("partit")){
                        if(matchCounter < 14){
                            matchResults[matchCounter] = parser.getAttributeValue(6);
                        }else{
                            String goalNumberCombined = parser.getAttributeValue(6);
                            goalNumber[0] = String.valueOf(goalNumberCombined.charAt(0));
                            goalNumber[1] = String.valueOf(goalNumberCombined.charAt(1));

                            tmpBet = new Bet(matchResults, goalNumber);
                        }

                        matchCounter ++;
                    }

                    break;

                case XmlPullParser.END_TAG:
                    tag = parser.getName();

                    if(tag.equals("quiniela")){
                        inPool = false;
                        matchCounter = 0;
                    }

                    break;
            }

            event = parser.next();
        }


        return new Result(tmpBet, moneyBet, prize15, prize14, prize13, prize12, prize11, prize10);
    }

    public static Result JSONToResult(JSONObject json) throws JSONException, BetParameterLengthNotValidException, BetFormatNotValidException {
        double moneyBet = 0.0;
        double prize15 = 0.0;
        double prize14 = 0.0;
        double prize13 = 0.0;
        double prize12 = 0.0;
        double prize11 = 0.0;
        double prize10 = 0.0;

        String[] matchResults = new String[14];
        String[] goalNumber = new String[2];

        JSONArray quinielas = json.getJSONObject("quinielista").getJSONArray("quiniela");

        JSONObject quiniela = null;

        //Find last valid object
        for (int i = 0; i < quinielas.length(); i++) {
            JSONObject quinielaTmp = quinielas.getJSONObject(i);

            if(quinielaTmp.getString("_el15").equals("0")
                    && quinielaTmp.getString("_el14").equals("0")
                    && quinielaTmp.getString("_el13").equals("0")
                    && quinielaTmp.getString("_el12").equals("0")
                    && quinielaTmp.getString("_el11").equals("0")
                    && quinielaTmp.getString("_el10").equals("0")){

                break;
            }

            quiniela = quinielaTmp;
        }

        if(quiniela!=null){
            moneyBet = XmlMoneyBetValueToDouble(quiniela.getString("_apuesta"));
            prize15 = XmlValueToDouble(quiniela.getString("_el15"));
            prize14 = XmlValueToDouble(quiniela.getString("_el14"));
            prize13 = XmlValueToDouble(quiniela.getString("_el13"));
            prize12 = XmlValueToDouble(quiniela.getString("_el12"));
            prize11 = XmlValueToDouble(quiniela.getString("_el11"));
            prize10 = XmlValueToDouble(quiniela.getString("_el10"));

            JSONArray matches = quiniela.getJSONArray("partit");

            for(int i = 0; i < matches.length(); i++){
                if(i < 14){
                    matchResults[i] = matches.getJSONObject(i).getString("_sig");
                }else{
                    goalNumber[0] = String.valueOf(matches.getJSONObject(i).getString("_sig").charAt(0));
                    goalNumber[1] = String.valueOf(matches.getJSONObject(i).getString("_sig").charAt(1));
                }
            }

            return new Result(new Bet(matchResults, goalNumber), moneyBet, prize15, prize14, prize13, prize12, prize11, prize10);
        }

        return null;
    }


    public static List<Bet> readBetsFile(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        List<Bet> bets = new ArrayList<>();
        Bet currentBet = null;
        String[] matchResults = new String[14];
        String[] goalNumber = new String[2];

        String line;

        while((line = reader.readLine()) != null){
            if(line.length()!=16){ //If line is not length, it's not a valid line
                continue;
            }

            matchResults =  new String[14];
            goalNumber = new String[2];

            for(int i = 0; i < line.length(); i++){
                if(i < 14){
                    matchResults[i] = String.valueOf(line.charAt(i));
                }else{
                    goalNumber[i-14] = String.valueOf(line.charAt(i));
                }
            }

            try{
                currentBet = new Bet(matchResults, goalNumber);
                bets.add(currentBet);
            }catch (BetFormatNotValidException e){
                currentBet = null;
                matchResults = null;
                goalNumber = null;
                continue;
            }catch (BetParameterLengthNotValidException e){
                e.printStackTrace();
            }
        }

        return bets;
    }

    public static JSONObject winnersToJson(Winners winners) throws JSONException {
        return new JSONObject(new GsonBuilder().create().toJson(winners));
    }

    public static String winnersToXml(Winners winners){
        StringBuilder result = new StringBuilder();

        result.append("<prizes>\n");
        result.append("\t<prizes10>"+String.valueOf(winners.getPrizes10())+"</prizes10>\n");
        result.append("\t<prizes11>"+String.valueOf(winners.getPrizes11())+"</prizes11>\n");
        result.append("\t<prizes12>"+String.valueOf(winners.getPrizes12())+"</prizes12>\n");
        result.append("\t<prizes13>"+String.valueOf(winners.getPrizes13())+"</prizes13>\n");
        result.append("\t<prizes14>"+String.valueOf(winners.getPrizes14())+"</prizes14>\n");
        result.append("\t<prizes15>"+String.valueOf(winners.getPrizes15())+"</prizes15>\n");
        result.append("\t<winners10>"+String.valueOf(winners.getWinners10())+"</winners10>\n");
        result.append("\t<winners11>"+String.valueOf(winners.getWinners11())+"</winners11>\n");
        result.append("\t<winners12>"+String.valueOf(winners.getWinners12())+"</winners12>\n");
        result.append("\t<winners13>"+String.valueOf(winners.getWinners13())+"</winners13>\n");
        result.append("\t<winners14>"+String.valueOf(winners.getWinners14())+"</winners14>\n");
        result.append("\t<winners15>"+String.valueOf(winners.getWinners15())+"</winners15>\n");
        result.append("\t<total>"+String.valueOf(winners.getTotal())+"</total>\n");
        result.append("</prizes>");

        return result.toString();
    }



}
