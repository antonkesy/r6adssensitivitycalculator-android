package com.poorskill.r6adssensitivitycalculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import static com.poorskill.r6adssensitivitycalculator.SensitivityCalculator.calculateAspectRatio;

public class FirstFragment extends Fragment implements TextWatcher {
    private EditText oldADSEdit, fovEdit, aspectRatioWidthEdit, aspectRatioHeightEdit;
    private TextView aspectRatioTextView;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_old_values, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.oldADSEdit = view.findViewById(R.id.edit_ads);
        this.fovEdit = view.findViewById(R.id.edit_fov);
        this.aspectRatioWidthEdit = view.findViewById(R.id.edit_aspect_width);
        this.aspectRatioHeightEdit = view.findViewById(R.id.edit_aspect_height);
        this.aspectRatioTextView = view.findViewById(R.id.textview_aspect_ratio);

        int[] values = LastCalculationValues.getOldValues(getContext());
        oldADSEdit.setText(String.valueOf(values[0]));
        fovEdit.setText(String.valueOf(values[1]));
        aspectRatioWidthEdit.setText(String.valueOf(values[2]));
        aspectRatioHeightEdit.setText(String.valueOf(values[3]));
        setAspectRatio();
        aspectRatioHeightEdit.addTextChangedListener(this);
        aspectRatioWidthEdit.addTextChangedListener(this);

        view.findViewById(R.id.btn_calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checks if all EditText are entered
                if (oldADSEdit.getText().length() > 0 && fovEdit.getText().length() > 0 && aspectRatioHeightEdit.getText().length() > 0 && aspectRatioWidthEdit.getText().length() > 0 && aspectRatioTextView.getText().length() > 0
                        //Check if nothing zero
                        && Double.parseDouble(oldADSEdit.getText().toString()) > 0 && Double.parseDouble(fovEdit.getText().toString()) > 0 && Double.parseDouble(aspectRatioHeightEdit.getText().toString()) > 0 && Double.parseDouble(aspectRatioWidthEdit.getText().toString()) > 0 && Double.parseDouble(aspectRatioTextView.getText().toString().replace(",", ".")) > 0) {
                    MainActivity.INSTANCE.setInputValues(Integer.parseInt(oldADSEdit.getText().toString()), Integer.parseInt(fovEdit.getText().toString()), Integer.parseInt(aspectRatioWidthEdit.getText().toString()), Integer.parseInt(aspectRatioHeightEdit.getText().toString()));
                    MainActivity.INSTANCE.calculateNewAds();
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                } else {
                    Toast.makeText(getContext(), requireContext().getString(R.string.missingEditText), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //noting
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        setAspectRatio();
    }

    @SuppressLint("DefaultLocale")
    private void setAspectRatio() {
        if (aspectRatioWidthEdit.getText().length() > 0 && aspectRatioHeightEdit.getText().length() > 0) {
            this.aspectRatioTextView.setText(String.format("%.2f", (calculateAspectRatio(Double.parseDouble(aspectRatioWidthEdit.getText().toString()), Double.parseDouble(aspectRatioHeightEdit.getText().toString())))));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //nothing
    }
}