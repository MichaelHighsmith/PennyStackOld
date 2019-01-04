package com.satyrlabs.pennystack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TickerView tickerView;
    TickerView taxTickerView;
    TextView startButton;
    EditText hourlyWageEditText;

    Spinner stateSpinner;

    boolean counting = false;
    Disposable disposable;
    Disposable taxDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.start_button);
        tickerView = findViewById(R.id.tickerView);
        taxTickerView = findViewById(R.id.taxTickerView);
        hourlyWageEditText = findViewById(R.id.wage_edit_text);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());
        taxTickerView.setCharacterLists(TickerUtils.provideNumberList());
        stateSpinner = findViewById(R.id.stateSpinner);

        stateSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TaxCalculator.State.values()));

        startButton.setOnClickListener(v -> startCounting());
    }

    public void startCounting() {

        String hourlyWageString = hourlyWageEditText.getText().toString();
        float hourlyWage = Float.valueOf(hourlyWageString);
        float penniesPerHour = hourlyWage * 100;
        float penniesPerMinute = penniesPerHour / 60;
        float penniesPerSecond = penniesPerMinute / 60;
        float secondsPerPenny = 60 / penniesPerMinute;
        long millisecondsPerPenny = (long) (secondsPerPenny * 1000);


        //Tax version
        TaxCalculator taxCalculator = new TaxCalculator(stateSpinner.getSelectedItem().toString(), hourlyWage);
        float tax = taxCalculator.calculateTaxRate();
        float actualEarning = 1 - tax;

        millisecondsPerPenny = (long) (millisecondsPerPenny / actualEarning);
        float taxPenniesPerMinute = penniesPerMinute * tax;
        float taxSecondsPerPenny = 60 / taxPenniesPerMinute;
        long taxMillisecondsPerPenny = (long) (taxSecondsPerPenny * 1000);



        taxTickerView.setAnimationDuration(taxMillisecondsPerPenny);

        tickerView.setAnimationDuration(millisecondsPerPenny);

        if (!counting) {
            disposable = Observable.interval(1000, millisecondsPerPenny, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addPenny);

            taxDisposable = Observable.interval(1000, taxMillisecondsPerPenny, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addTaxPenny);

            startButton.setText("Stop Counting");

            counting = true;
        } else {
            startButton.setText("Start Counting");
            disposable.dispose();
            taxDisposable.dispose();
            counting = false;
        }
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
