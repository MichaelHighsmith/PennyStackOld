package com.satyrlabs.pennystack;

import static com.satyrlabs.pennystack.TaxCalculator.State.Alabama;
import static java.lang.Enum.valueOf;

public class TaxCalculator {

    //this class takes in a federal and state tax rate, Social security (6.2%), Medicare (1.45%) and an hourly rate
    //using these it calculates the total tax rate to be taken out of the pennies per millisecond.  Then this class returns
    //a new "real" earnings per millisecond and tax amounts per millisecond for each value

    private String state;
    private float hourlyWage;

    TaxCalculator(String state, float hourlyWage) {
        this.state = state;
        this.hourlyWage = hourlyWage;
    }


    public float calculateTaxRate() {
        float penniesPerHour = hourlyWage * 100;
        float penniesPerMinute = penniesPerHour / 60;
        float penniesPerSecond = penniesPerMinute / 60;
        float secondsPerPenny = 60 / penniesPerMinute;
        long millisecondsPerPenny = (long) (secondsPerPenny * 1000);

        float stateTaxRate = getStateTaxRate(state);
        float federalTaxRate = getFederalTaxRate(hourlyWage);
        float totalTaxRate = stateTaxRate + federalTaxRate + 0.062f + 0.0145f;
        return totalTaxRate;
    }

    private float getFederalTaxRate(float hourlyWage) {
        return 0.22f;
    }

    private float getStateTaxRate(String state) {

        float taxRate = 0.0f;

        switch (State.valueOf(state)) {
            case Alabama:
                taxRate = 0.05f;
                break;
            case Alaska:
                taxRate = 0.0f;
                break;
            case Arizona:
                taxRate = 0.0454f;
                break;
            case Arkansas:
                taxRate = 0.069f;
                break;
            case California:
                taxRate = 0.133f;
                break;
            case Colorado:
                taxRate = 0.0463f;
                break;
            case Connecticut:
                taxRate = 0.4f;
                break;
            case Deleware:
                taxRate = 0.4f;
                break;
            case Florida:
                taxRate = 0.4f;
                break;
            case Georgia:
                taxRate = 0.4f;
                break;
            case Hawaii:
                taxRate = 0.4f;
                break;
            case Idaho:
                taxRate = 0.4f;
                break;
            case Illinois:
                taxRate = 0.4f;
                break;
            case Indiana:
                taxRate = 0.4f;
                break;
            case Iowa:
                taxRate = 0.4f;
                break;
            case Kansas:
                taxRate = 0.4f;
                break;
            case Kentucky:
                taxRate = 0.4f;
                break;
            case Louisiana:
                taxRate = 0.4f;
                break;
            case Maine:
                taxRate = 0.4f;
                break;
            case Maryland:
                taxRate = 0.4f;
                break;
            case Massachusetts:
                taxRate = 0.4f;
                break;
            case Michigan:
                taxRate = 0.4f;
                break;
            case Minnesota:
                taxRate = 0.4f;
                break;
            case Missouri:
                taxRate = 0.4f;
                break;
            case Montana:
                taxRate = 0.4f;
                break;
            case Nebraska:
                taxRate = 0.4f;
                break;
            case Nevada:
                taxRate = 0.4f;
                break;
            case NewHampshire:
                taxRate = 0.4f;
                break;
            case NewJersey:
                taxRate = 0.4f;
                break;
            case NewMexico:
                taxRate = 0.4f;
                break;
            case NewYork:
                taxRate = 0.4f;
                break;
            case NorthCarolina:
                taxRate = 0.4f;
                break;
            case NorthDakota:
                taxRate = 0.4f;
                break;
            case Ohio:
                taxRate = 0.4f;
                break;
            case Oklahoma:
                taxRate = 0.4f;
                break;
            case Oregon:
                taxRate = 0.4f;
                break;
            case Pennsylvania:
                taxRate = 0.4f;
                break;
            case RhodeIsland:
                taxRate = 0.4f;
                break;
            case SouthCarolina:
                taxRate = 0.4f;
                break;
            case SouthDakota:
                taxRate = 0.4f;
                break;
            case Tennessee:
                taxRate = 0.4f;
                break;
            case Texas:
                taxRate = 0.4f;
                break;
            case Utah:
                taxRate = 0.4f;
                break;
            case Vermont:
                taxRate = 0.4f;
                break;
            case Virginia:
                taxRate = 0.4f;
                break;
            case Washington:
                taxRate = 0.4f;
                break;
            case WestVirginia:
                taxRate = 0.4f;
                break;
            case Wisconsin:
                taxRate = 0.4f;
                break;
            case Wyoming:
                taxRate = 0.4f;
                break;
        }

        return taxRate;
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


