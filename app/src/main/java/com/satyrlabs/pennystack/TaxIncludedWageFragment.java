package com.satyrlabs.pennystack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TaxIncludedWageFragment extends Fragment {

    TickerView tickerView;
    TickerView taxTickerView;
    TextView startButton;
    EditText hourlyWageEditText;
    Spinner stateSpinner;

    Disposable disposable;
    Disposable taxDisposable;
    Disposable stateTaxDisposable;

    TaxCalculator taxCalculator;

    boolean counting = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tax_included_wage_fragment, parentViewGroup, false);

        startButton = view.findViewById(R.id.start_button);
        tickerView = view.findViewById(R.id.tickerView);
        taxTickerView = view.findViewById(R.id.taxTickerView);
        hourlyWageEditText = view.findViewById(R.id.wage_edit_text);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());
        taxTickerView.setCharacterLists(TickerUtils.provideNumberList());
        stateSpinner = view.findViewById(R.id.stateSpinner);

        stateSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, TaxCalculator.State.values()));

        startButton.setOnClickListener(v -> setUpInfo());

        return view;
    }

    public void startCounting(Float totalTax) {
        String hourlyWageString = hourlyWageEditText.getText().toString();
        float hourlyWage = Float.valueOf(hourlyWageString);
        float penniesPerHour = hourlyWage * 100;
        float penniesPerMinute = penniesPerHour / 60;
        float penniesPerSecond = penniesPerMinute / 60;
        float secondsPerPenny = 60 / penniesPerMinute;
        long millisecondsPerPenny = (long) (secondsPerPenny * 1000);

        float actualEarning = 1 - totalTax;

        millisecondsPerPenny = (long) (millisecondsPerPenny / actualEarning);
        float taxPenniesPerMinute = penniesPerMinute * totalTax;
        float taxSecondsPerPenny = 60 / taxPenniesPerMinute;
        long taxMillisecondsPerPenny = (long) (taxSecondsPerPenny * 1000);

        taxTickerView.setAnimationDuration(taxMillisecondsPerPenny);
        tickerView.setAnimationDuration(millisecondsPerPenny);

        disposable = Observable.interval(1000, millisecondsPerPenny, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::addPenny);

        taxDisposable = Observable.interval(1000, taxMillisecondsPerPenny, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::addTaxPenny);
    }

    public void setUpInfo() {

        if (hourlyWageEditText.getText().toString().isEmpty()) {
            return;
        }

        String hourlyWageString = hourlyWageEditText.getText().toString();
        float hourlyWage = Float.valueOf(hourlyWageString);

        taxCalculator = new TaxCalculator(getContext(), stateSpinner.getSelectedItem().toString(), hourlyWage);
        String stateAbbreviation = taxCalculator.getStateAbbreviation(stateSpinner.getSelectedItem().toString());

        stateTaxDisposable = taxCalculator.getTaxRates(stateAbbreviation).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(taxRate -> {
            if (!counting) {
                startCounting(taxRate);
                startButton.setText("Stop Counting");
                counting = true;
            } else {
                startButton.setText("Start Counting");
                disposable.dispose();
                taxDisposable.dispose();
                counting = false;
            }
        });
    }

    public void addPenny(Long newNumber) {

        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        String s = n.format(newNumber / 100.0);

        tickerView.setText(s);
    }

    public void addTaxPenny(Long newNumber) {

        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        String s = n.format(newNumber / 100.0);

        taxTickerView.setText(s);
    }

}
