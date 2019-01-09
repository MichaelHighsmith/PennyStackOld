package com.satyrlabs.pennystack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateTaxResponse {

    @Expose
    @SerializedName("single")
    public RelationshipStatus single;

    @Expose
    @SerializedName("married")
    public Object married;

    @Expose
    @SerializedName("married_separately")
    public Object married_separately;

    @Expose
    @SerializedName("head_of_household")
    public Object head_of_household;

    public Object getSingle() {
        return single;
    }

    public Object getMarried() {
        return married;
    }

    public Object getMarried_separately() {
        return married_separately;
    }

    public Object getHead_of_household() {
        return head_of_household;
    }
}
