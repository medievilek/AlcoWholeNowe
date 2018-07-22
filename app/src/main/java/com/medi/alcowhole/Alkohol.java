package com.medi.alcowhole;

public class Alkohol {

    String alkoholId;
    String alkoholName;
    String alkoholGenre;

    public Alkohol() { // pusty konstruktor

    }

    public Alkohol(String alkoholId, String alkoholName, String alkoholGenre) {
        this.alkoholId = alkoholId;
        this.alkoholName = alkoholName;
        this.alkoholGenre = alkoholGenre;
    }

    public String getAlkoholId() {
        return alkoholId;
    }

    public String getAlkoholName() {
        return alkoholName;
    }

    public String getAlkoholGenre() {
        return alkoholGenre;
    }
}
