package com.example.daniel.datosquiniela;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

/**
 * Created by Daniel on 12/01/2017.
 */

public class Bet {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({})
    public @interface  matchesValues {
        String x = "X";
        String one = "1";
        String two = "2";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({})
    public @interface goalNumberValues{
        String zero = "0";
        String one = "1";
        String two = "2";
        String more = "M";
    }

    private static final String[] allowedMatchesResults = {"1", "X", "2"};
    private static final String[] allowedGoalNumber = {"0", "1", "2", "M"};

   private @matchesValues String[] matchesResults;
    private @goalNumberValues String[] goalNumbers;


    /**
     * Creates a Bet given matches results and last match goal numbers
     * @param matchesResults Matches results. Must be of size 14
     * @param goalNumbers Goal Numbers, must be of size 2
     */
    public Bet( @matchesValues String[] matchesResults, @goalNumberValues String[] goalNumbers) throws BetFormatNotValidException, BetParameterLengthNotValidException {
        if(matchesResults.length != 14 || goalNumbers.length != 2){
            throw new BetParameterLengthNotValidException("matchesResult or goalNumbers length number incorrect");
        }

        if(!checkGoalNumber(goalNumbers) || !checkMatchesResult(matchesResults)){
            throw new BetFormatNotValidException("Bet format non compliant");
        }

        this.matchesResults = matchesResults;
        this.goalNumbers = goalNumbers;
    }

    public boolean checkMatchesResult(String[] matchesResults){
        boolean result = false;

        for(int i = 0; i < matchesResults.length; i++){
            for(int j = 0; j < allowedMatchesResults.length; j++){
                if(matchesResults[i].equals(allowedMatchesResults[j])){
                    result = true;
                    return result;
                }
            }
        }

        return result;
    }

    public boolean checkGoalNumber(String[] goalNumber){
        boolean result = false;

        for(int i = 0; i < goalNumber.length; i++){
            for(int j = 0; j < allowedGoalNumber.length; j++){
                if(goalNumber[i].equals(allowedGoalNumber[j])){
                    result = true;
                    return result;
                }
            }
        }

        return result;
    }

    public String[] getMatchesResults() {
        return matchesResults;
    }

    public void setMatchesResults(@matchesValues String[] matchesResults) {
        this.matchesResults = matchesResults;
    }

    public String[] getGoalNumbers() {
        return goalNumbers;
    }

    public void setGoalNumbers(@goalNumberValues String[] goalNumbers) {
        this.goalNumbers = goalNumbers;
    }

    public int compareResemblance(Bet b){
        int count = 0;

        for(int i = 0; i < b.getMatchesResults().length; i++){
            if(getMatchesResults()[i].equals(b.getMatchesResults()[i])){
                count++;
            }
        }

        if(count == 14){
            if(getGoalNumbers()[0].equals(b.getGoalNumbers()[0])
                    && getGoalNumbers()[1].equals(b.getGoalNumbers()[1])){
                count++;
            }
        }

        return count;
    }
}
