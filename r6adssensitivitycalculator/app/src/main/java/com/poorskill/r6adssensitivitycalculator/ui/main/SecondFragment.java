package com.poorskill.r6adssensitivitycalculator.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.poorskill.r6adssensitivitycalculator.R;

import java.text.DecimalFormat;

public class SecondFragment extends Fragment {

    private TextView ads_0, ads_1, ads_2, ads_3, ads_4, ads_5, ads_6, ads_7;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_new_values_retrofit, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.ads_0 = view.findViewById(R.id.output_ads_0);
        this.ads_1 = view.findViewById(R.id.output_ads_1);
        this.ads_2 = view.findViewById(R.id.output_ads_2);
        this.ads_3 = view.findViewById(R.id.output_ads_3);
        this.ads_4 = view.findViewById(R.id.output_ads_4);
        this.ads_5 = view.findViewById(R.id.output_ads_5);
        this.ads_6 = view.findViewById(R.id.output_ads_6);
        this.ads_7 = view.findViewById(R.id.output_ads_7);

        view.findViewById(R.id.ads0_row).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copyValueToClipboard(ads_0.getText().toString(), "ADS 1x");
            }
        });
        view.findViewById(R.id.ads1_row).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copyValueToClipboard(ads_1.getText().toString(), "ADS 1.5x");
            }
        });
        view.findViewById(R.id.ads2_row).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copyValueToClipboard(ads_2.getText().toString(), "ADS 2x");
            }
        });
        view.findViewById(R.id.ads3_row).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copyValueToClipboard(ads_3.getText().toString(), "ADS 2.5x");
            }
        });
        view.findViewById(R.id.ads4_row).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copyValueToClipboard(ads_4.getText().toString(), "ADS 3x");
            }
        });
        view.findViewById(R.id.ads5_row).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copyValueToClipboard(ads_5.getText().toString(), "ADS 4x");
            }
        });
        view.findViewById(R.id.ads6_row).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copyValueToClipboard(ads_6.getText().toString(), "ADS 5x");
            }
        });
        view.findViewById(R.id.ads7_row).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copyValueToClipboard(ads_7.getText().toString(), "ADS 12x");
            }
        });
        view.findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                copyValueToClipboard(convertAllValuesToString(), "everything");
            }
        });

        view.findViewById(R.id.btn_edit_values).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        inputCalculatedValues();


    }

    private void inputCalculatedValues() {
        double[] adsValues = MainActivity.Companion.getAdsValues();
        DecimalFormat df = new DecimalFormat("#");
        this.ads_0.setText(df.format(adsValues[0]));
        this.ads_1.setText(df.format(adsValues[1]));
        this.ads_2.setText(df.format(adsValues[2]));
        this.ads_3.setText(df.format(adsValues[3]));
        this.ads_4.setText(df.format(adsValues[4]));
        this.ads_5.setText(df.format(adsValues[5]));
        this.ads_6.setText(df.format(adsValues[6]));
        this.ads_7.setText(df.format(adsValues[7]));
    }

    private String convertAllValuesToString() {
        return getResources().getString(R.string.copy_start)
                + "\n" + "ADS 1x = " + ads_0.getText().toString()
                + "\n" + "ADS 1.5x = " + ads_1.getText().toString()
                + "\n" + "ADS 2x = " + ads_2.getText().toString()
                + "\n" + "ADS 2.5x = " + ads_3.getText().toString()
                + "\n" + "ADS 3x = " + ads_4.getText().toString()
                + "\n" + "ADS 4x = " + ads_5.getText().toString()
                + "\n" + "ADS 5x = " + ads_6.getText().toString()
                + "\n" + "ADS 12x = " + ads_7.getText().toString();
    }

    private void copyValueToClipboard(String value, String name) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", value);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "copied " + name + " to clipboard", Toast.LENGTH_SHORT).show();
    }
}