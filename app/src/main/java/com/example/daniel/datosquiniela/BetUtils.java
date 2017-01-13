package com.example.daniel.datosquiniela;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static Result XmlToResult(XmlPullParser parser) throws Exception {
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
}
