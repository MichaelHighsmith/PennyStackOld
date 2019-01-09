package com.satyrlabs.pennystack;

import android.content.Context;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class TaxCalculator {

    private String state;
    private float hourlyWage;
    private Context context;

    private float ssTax = 0.062f;
    private float medicareTax = 0.0145f;

    BehaviorSubject<Float> taxSubject = BehaviorSubject.create();

    TaxCalculator(Context context, String state, float hourlyWage) {
        this.context = context;
        this.state = state;
        this.hourlyWage = hourlyWage;
    }


    public float calculateTaxRate(float stateTax) {
        float federalTaxRate = getFederalTaxRate(hourlyWage);
        return stateTax + federalTaxRate + ssTax + medicareTax;
    }

    private float getFederalTaxRate(float hourlyWage) {
        //TODO add federal income tax info
        return 0.22f;
    }

    public BehaviorSubject<Float> getStateTaxRate(String state) {

        TaxService service = ApiClient.getClient().create(TaxService.class);
        Disposable taxInfoDisposable = service.getTaxInfo(state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stateTaxResponse -> {

                    float taxRate = 0.0f;

                    if (stateTaxResponse.single.type != null && stateTaxResponse.single.type.equals("none")) {
                        taxSubject.onNext(taxRate);
                        return;
                    }

                    TaxBracket[] taxBrackets = stateTaxResponse.single.income_tax_brackets;

                    for (int i = taxBrackets.length - 1; i < taxBrackets.length; i--) {
                        if (hourlyWage * 2000 > taxBrackets[i].bracket) {
                            taxRate = taxBrackets[i].marginal_rate;
                            break;
                        }
                    }

                    taxSubject.onNext(taxRate);
                });

        return taxSubject;
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


