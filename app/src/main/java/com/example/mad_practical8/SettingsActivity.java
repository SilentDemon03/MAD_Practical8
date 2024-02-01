package com.example.mad_practical8;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText ETHexCode = findViewById(R.id.ETHexCode);
        EditText ETNumberOfImages = findViewById(R.id.ETNumberOfImages);
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        String SPBgColorCode = preferences.getString("BgColor", "");
        int SPNumberOfImages = preferences.getInt("NumberOfImages", 3);
        ETHexCode.setText(SPBgColorCode);
        ETNumberOfImages.setText(String.valueOf(SPNumberOfImages));

        // btnSubmitChangeSettings
        Button BtnChangeSettings = findViewById(R.id.BtnChangeSettings);
        BtnChangeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor SPEditor = sharedPreferences.edit();

                String BgColorCode = ETHexCode.getText().toString();
                Integer NumberOfImages = Integer.parseInt(ETNumberOfImages.getText().toString());
                SPEditor.putString("BgColor", BgColorCode);
                SPEditor.putInt("NumberOfImages", NumberOfImages);

                SPEditor.commit();
            }
        });
    }

}