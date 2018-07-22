package com.medi.alcowhole;

public class Kryterium {
    private String kryteriumId;
    private String kryteriumName;
    private int kryteriumRating;

    public Kryterium(){

    }

    public Kryterium(String kryteriumId, String kryteriumName, int kryteriumRating) {
        this.kryteriumId = kryteriumId;
        this.kryteriumName = kryteriumName;
        this.kryteriumRating = kryteriumRating;
    }

    public String getKryteriumId() {
        return kryteriumId;
    }

    public String getKryteriumName() {
        return kryteriumName;
    }

    public int getKryteriumRating() {
        return kryteriumRating;
    }
}
