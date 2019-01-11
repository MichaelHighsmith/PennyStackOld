package com.satyrlabs.pennystack;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class TaxCalculator {

    private String state;
    private float hourlyWage;
    private Context context;

    private float ssTax = 0.062f;
    private float medicareTax = 0.0145f;

    TaxService service;

    TaxCalculator(Context context, String state, float hourlyWage) {
        this.context = context;
        this.state = state;
        this.hourlyWage = hourlyWage;

        service = ApiClient.getClient().create(TaxService.class);
    }

    public Observable<Float> getTaxRates(String state) {

        Observable<Float> zipObservable = Observable.zip(service.getFederalTaxInfo(), service.getTaxInfo(state), new BiFunction<StateTaxResponse, StateTaxResponse, Float>() {

            @Override
            public Float apply(StateTaxResponse federal, StateTaxResponse state) {
                float totalTaxRate = 0.0f;
                float stateTaxRate = 0.0f;
                float federalTaxRate = 0.0f;

                if (federal.single.type != null && federal.single.type.equals("none")) {
                    federalTaxRate = 0.0f;
                } else {
                    TaxBracket[] federalTaxBrackets = federal.single.income_tax_brackets;
                    for (int i = federalTaxBrackets.length - 1; i < federalTaxBrackets.length; i--) {
                        if (hourlyWage * 2000 > federalTaxBrackets[i].bracket) {
                            federalTaxRate = federalTaxBrackets[i].marginal_rate;
                            break;
                        }
                    }
                }

                if (state.single.type != null && state.single.type.equals("none")) {
                    stateTaxRate = 0.0f;
                } else {
                    TaxBracket[] taxBrackets = state.single.income_tax_brackets;
                    for (int i = taxBrackets.length - 1; i < taxBrackets.length; i--) {
                        if (hourlyWage * 2000 > taxBrackets[i].bracket) {
                            stateTaxRate = taxBrackets[i].marginal_rate;
                            break;
                        }
                    }
                }

                totalTaxRate = (federalTaxRate / 100) + (stateTaxRate / 100) + ssTax + medicareTax;

                return totalTaxRate;
            }
        });

        return zipObservable;
    }

    public String getStateAbbreviation(String state) {
        String abbreviation = "NY";
        switch (State.valueOf(state)) {
            case Alabama:
                abbreviation = "AL";
                break;
            case Alaska:
                abbreviation = "AK";
                break;
            case Arizona:
                abbreviation = "AZ";
                break;
            case Arkansas:
                abbreviation = "AR";
                break;
            case California:
                abbreviation = "CA";
                break;
            case Colorado:
                abbreviation = "CO";
                break;
            case Connecticut:
                abbreviation = "CT";
                break;
            case Deleware:
                abbreviation = "DE";
                break;
            case Florida:
                abbreviation = "FL";
                break;
            case Georgia:
                abbreviation = "GA";
                break;
            case Hawaii:
                abbreviation = "HI";
                break;
            case Idaho:
                abbreviation = "ID";
                break;
            case Illinois:
                abbreviation = "IL";
                break;
            case Indiana:
                abbreviation = "IN";
                break;
            case Iowa:
                abbreviation = "IA";
                break;
            case Kansas:
                abbreviation = "KS";
                break;
            case Kentucky:
                abbreviation = "KY";
                break;
            case Louisiana:
                abbreviation = "LA";
                break;
            case Maine:
                abbreviation = "ME";
                break;
            case Maryland:
                abbreviation = "MD";
                break;
            case Massachusetts:
                abbreviation = "MA";
                break;
            case Michigan:
                abbreviation = "MI";
                break;
            case Minnesota:
                abbreviation = "MS";
                break;
            case Missouri:
                abbreviation = "MO";
                break;
            case Montana:
                abbreviation = "MT";
                break;
            case Nebraska:
                abbreviation = "NE";
                break;
            case Nevada:
                abbreviation = "NV";
                break;
            case NewHampshire:
                abbreviation = "NH";
                break;
            case NewJersey:
                abbreviation = "NJ";
                break;
            case NewMexico:
                abbreviation = "NM";
                break;
            case NewYork:
                abbreviation = "NY";
                break;
            case NorthCarolina:
                abbreviation = "NC";
                break;
            case NorthDakota:
                abbreviation = "ND";
                break;
            case Ohio:
                abbreviation = "OH";
                break;
            case Oklahoma:
                abbreviation = "OK";
                break;
            case Oregon:
                abbreviation = "OR";
                break;
            case Pennsylvania:
                abbreviation = "PA";
                break;
            case RhodeIsland:
                abbreviation = "RI";
                break;
            case SouthCarolina:
                abbreviation = "SC";
                break;
            case SouthDakota:
                abbreviation = "SD";
                break;
            case Tennessee:
                abbreviation = "TN";
                break;
            case Texas:
                abbreviation = "TX";
                break;
            case Utah:
                abbreviation = "UT";
                break;
            case Vermont:
                abbreviation = "VT";
                break;
            case Virginia:
                abbreviation = "VA";
                break;
            case Washington:
                abbreviation = "WA";
                break;
            case WestVirginia:
                abbreviation = "WV";
                break;
            case Wisconsin:
                abbreviation = "WI";
                break;
            case Wyoming:
                abbreviation = "WY";
                break;
        }

        return abbreviation;
    }

    enum State {
        Alabama, Alaska, Arizona, Arkansas, California, Colorado,
        Connecticut, Deleware, Florida, Georgia, Hawaii, Idaho, Illinois,
        Indiana, Iowa, Kansas, Kentucky, Louisiana, Maine, Maryland, Massachusetts,
        Michigan, Minnesota, Mississippi, Missouri, Montana, Nebraska, Nevada,
        NewHampshire, NewJersey, NewMexico, NewYork, NorthCarolina, NorthDakota,
        Ohio, Oklahoma, Oregon, Pennsylvania, RhodeIsland, SouthCarolina,
        SouthDakota, Tennessee, Texas, Utah, Vermont, Virginia, Washington, WestVirginia,
        Wisconsin, Wyoming
    }
}


