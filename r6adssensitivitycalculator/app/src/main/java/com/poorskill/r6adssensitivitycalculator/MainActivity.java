package com.poorskill.r6adssensitivitycalculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static MainActivity INSTANCE;
    private double oldSensValue;
    private double fov;
    private double aspectRatioWidth;
    private double aspectRatioHeight;

    private double[] adsValues = new double[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String urlUbisoftGuide = "https://www.ubisoft.com/en-gb/game/rainbow-six/siege/news-updates/3IMlDGlaRFgdvQNq3BOSFv/guide-to-ads-sensitivity-in-y5s3";
        switch (item.getItemId()) {
            case R.id.action_about:
                this.startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.action_help:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlUbisoftGuide)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void calculateNewAds() {
        this.adsValues = SensitivityCalculator.calculateNewAdsSensitivity(oldSensValue, fov, aspectRatioWidth / aspectRatioHeight);
    }

    public void setInputValues(int oldSensValue, int fov, int aspectRatioWidth, int aspectRatioHeight) {
        this.oldSensValue = oldSensValue;
        this.fov = fov;
        this.aspectRatioWidth = aspectRatioWidth;
        this.aspectRatioHeight = aspectRatioHeight;
        LastCalculationValues.saveValues(getApplicationContext(), oldSensValue, fov, aspectRatioWidth, aspectRatioHeight);
    }

    public double[] getAdsValues() {
        return this.adsValues;
    }
}