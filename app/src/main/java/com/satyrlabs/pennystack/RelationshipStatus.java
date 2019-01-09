package com.satyrlabs.pennystack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelationshipStatus {

    @Expose
    @SerializedName("specialtaxes")
    public Object specialtaxes;

    @Expose
    @SerializedName("deductions")
    public Object deductions;

    @Expose
    @SerializedName("credits")
    public Object credits;

    @Expose
    @SerializedName("annotations")
    public Object annotations;

    @Expose
    @SerializedName("income_tax_brackets")
    public TaxBracket[] income_tax_brackets;

    @Expose
    @SerializedName("type")
    public String type;

    public Object getSpecialtaxes() {
        return specialtaxes;
    }

    public Object getDeductions() {
        return deductions;
    }

    public Object getCredits() {
        return credits;
    }

    public Object getAnnotations() {
        return annotations;
    }

    public TaxBracket[] getIncome_tax_brackets() {
        return income_tax_brackets;
    }

    public String getType() {
        return type;
    }
}
