package com.satyrlabs.pennystack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class BasicWageFragment extends Fragment {

    TickerView basicTickerView;
    EditText basicHourlyWageEditText;
    TextView startButton;

    Disposable disposable;
    boolean counting = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.basic_wage_fragment, parentViewGroup, false);

        basicTickerView = view.findViewById(R.id.basicTickerView);
        basicTickerView.setCharacterLists(TickerUtils.provideNumberList());
        basicHourlyWageEditText = view.findViewById(R.id.basic_wage_edit_text);
        startButton = view.findViewById(R.id.basic_start_button);

        startButton.setOnClickListener(v -> startCounting());

        return view;
    }

    public void startCounting() {
        if (basicHourlyWageEditText.getText().toString().isEmpty()) {
            return;
        }

        String hourlyWageString = basicHourlyWageEditText.getText().toString();
        float hourlyWage = Float.valueOf(hourlyWageString);
        float penniesPerHour = hourlyWage * 100;
        float penniesPerMinute = penniesPerHour / 60;
        float penniesPerSecond = penniesPerMinute / 60;
        float secondsPerPenny = 60 / penniesPerMinute;
        long millisecondsPerPenny = (long) (secondsPerPenny * 1000);

        basicTickerView.setAnimationDuration(millisecondsPerPenny);

        if (!counting) {
            disposable = Observable.interval(0, millisecondsPerPenny, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::addPenny);

            counting = true;
        } else {
            disposable.dispose();
            counting = false;
        }
    }

    public void addPenny(Long newNumber) {
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        String s = n.format(newNumber / 100.0);

        basicTickerView.setText(s);
    }

}
