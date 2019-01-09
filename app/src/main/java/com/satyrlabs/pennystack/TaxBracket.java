package com.satyrlabs.pennystack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaxBracket {

    @Expose
    @SerializedName("bracket")
    public int bracket;

    @Expose
    @SerializedName("marginal_rate")
    public float marginal_rate;

    public int getBracket() {
        return bracket;
    }

    public float getMarginal_rate() {
        return marginal_rate;
    }
}
