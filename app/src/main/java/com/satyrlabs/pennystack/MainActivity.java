package com.satyrlabs.pennystack;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    public static final int NEW_EDIT_VALUES = 111;

    TaxIncludedWageFragment taxIncludedWageFragment;
    BasicWageFragment basicWageFragment;
    Fragment currentActiveFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        basicWageFragment = new BasicWageFragment();
        taxIncludedWageFragment = new TaxIncludedWageFragment();
        currentActiveFragment = basicWageFragment;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.currentWageLayout, basicWageFragment)
                .add(R.id.currentWageLayout, taxIncludedWageFragment)
                .hide(taxIncludedWageFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_layout:
                startActivityForResult(new Intent(this, EditOptionsActivity.class), NEW_EDIT_VALUES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_EDIT_VALUES) {
            if (resultCode == RESULT_OK) {

                String selectedLayout = data.getStringExtra("selectedLayout");

                switch (selectedLayout) {
                    case "basic":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .show(basicWageFragment)
                                .hide(taxIncludedWageFragment)
                                .commit();
                        currentActiveFragment = basicWageFragment;
                        break;
                    case "taxIncluded":
                        getSupportFragmentManager()
                                .beginTransaction()
                                .show(taxIncludedWageFragment)
                                .hide(basicWageFragment)
                                .commit();
                        currentActiveFragment = taxIncludedWageFragment;
                        break;
                }
            }
        }
    }
}
