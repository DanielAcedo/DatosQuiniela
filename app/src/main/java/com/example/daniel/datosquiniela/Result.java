package com.example.daniel.datosquiniela;

/**
 * Created by Daniel on 12/01/2017.
 */

public class Result {
    Bet winningBet;

    double moneyBet;

    double prize15;
    double prize14;
    double prize13;
    double prize12;
    double prize11;
    double prize10;

    public Result(Bet winningBet, double moneyBet, double prize15, double prize14, double prize13, double prize12, double prize11, double prize10) {
        this.winningBet = winningBet;
        this.moneyBet = moneyBet;
        this.prize15 = prize15;
        this.prize14 = prize14;
        this.prize13 = prize13;
        this.prize12 = prize12;
        this.prize11 = prize11;
        this.prize10 = prize10;
    }

    public Bet getWinningBet() {
        return winningBet;
    }

    public void setWinningBet(Bet winningBet) {
        this.winningBet = winningBet;
    }

    public double getMoneyBet() {
        return moneyBet;
    }

    public void setMoneyBet(double moneyBet) {
        this.moneyBet = moneyBet;
    }

    public double getPrize15() {
        return prize15;
    }

    public void setPrize15(double prize15) {
        this.prize15 = prize15;
    }

    public double getPrize14() {
        return prize14;
    }

    public void setPrize14(double prize14) {
        this.prize14 = prize14;
    }

    public double getPrize13() {
        return prize13;
    }

    public void setPrize13(double prize13) {
        this.prize13 = prize13;
    }

    public double getPrize12() {
        return prize12;
    }

    public void setPrize12(double prize12) {
        this.prize12 = prize12;
    }

    public double getPrize11() {
        return prize11;
    }

    public void setPrize11(double prize11) {
        this.prize11 = prize11;
    }

    public double getPrize10() {
        return prize10;
    }

    public void setPrize10(double prize10) {
        this.prize10 = prize10;
    }
}
