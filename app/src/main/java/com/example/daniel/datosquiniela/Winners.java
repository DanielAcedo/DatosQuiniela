package com.example.daniel.datosquiniela;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel on 15/01/2017.
 */

public class Winners {

    @SerializedName("winners10")
    private int winners10 = 0;
    @SerializedName("winners11")
    private int winners11 = 0;
    @SerializedName("winners12")
    private int winners12 = 0;
    @SerializedName("winners13")
    private int winners13 = 0;
    @SerializedName("winners14")
    private int winners14 = 0;
    @SerializedName("winners15")
    private int winners15 = 0;

    @SerializedName("prizes15")
    private double prizes15;
    @SerializedName("prizes14")
    private double prizes14;
    @SerializedName("prizes13")
    private double prizes13;
    @SerializedName("prizes12")
    private double prizes12;
    @SerializedName("prizes11")
    private double prizes11;
    @SerializedName("prizes10")
    private double prizes10;
    @SerializedName("total")
    private double total;

    public Winners(int winners10, int winners11, int winners12, int winners13, int winners14, int winners15, double prizes15, double prizes14, double prizes13, double prizes12, double prizes11, double prizes10, double total) {
        this.winners10 = winners10;
        this.winners11 = winners11;
        this.winners12 = winners12;
        this.winners13 = winners13;
        this.winners14 = winners14;
        this.winners15 = winners15;
        this.prizes15 = prizes15;
        this.prizes14 = prizes14;
        this.prizes13 = prizes13;
        this.prizes12 = prizes12;
        this.prizes11 = prizes11;
        this.prizes10 = prizes10;
        this.total = total;
    }

    public int getWinners10() {
        return winners10;
    }

    public void setWinners10(int winners10) {
        this.winners10 = winners10;
    }

    public int getWinners11() {
        return winners11;
    }

    public void setWinners11(int winners11) {
        this.winners11 = winners11;
    }

    public int getWinners12() {
        return winners12;
    }

    public void setWinners12(int winners12) {
        this.winners12 = winners12;
    }

    public int getWinners13() {
        return winners13;
    }

    public void setWinners13(int winners13) {
        this.winners13 = winners13;
    }

    public int getWinners14() {
        return winners14;
    }

    public void setWinners14(int winners14) {
        this.winners14 = winners14;
    }

    public int getWinners15() {
        return winners15;
    }

    public void setWinners15(int winners15) {
        this.winners15 = winners15;
    }

    public double getPrizes15() {
        return prizes15;
    }

    public void setPrizes15(double prizes15) {
        this.prizes15 = prizes15;
    }

    public double getPrizes14() {
        return prizes14;
    }

    public void setPrizes14(double prizes14) {
        this.prizes14 = prizes14;
    }

    public double getPrizes13() {
        return prizes13;
    }

    public void setPrizes13(double prizes13) {
        this.prizes13 = prizes13;
    }

    public double getPrizes12() {
        return prizes12;
    }

    public void setPrizes12(double prizes12) {
        this.prizes12 = prizes12;
    }

    public double getPrizes11() {
        return prizes11;
    }

    public void setPrizes11(double prizes11) {
        this.prizes11 = prizes11;
    }

    public double getPrizes10() {
        return prizes10;
    }

    public void setPrizes10(double prizes10) {
        this.prizes10 = prizes10;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
