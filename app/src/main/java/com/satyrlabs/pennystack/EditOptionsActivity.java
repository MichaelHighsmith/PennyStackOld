package com.satyrlabs.pennystack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EditOptionsActivity extends AppCompatActivity {

    Button setBasicButton, includeTaxButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_options_activity);

        setBasicButton = findViewById(R.id.set_basic_button);
        setBasicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedLayout", "basic");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        includeTaxButton = findViewById(R.id.set_tax_included_button);
        includeTaxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedLayout", "taxIncluded");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
