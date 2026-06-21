package com.example.socialanxietyhelper;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class OnboardingNameFragment extends Fragment {

    private EditText etName;
    private TextView txtCount;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View v = i.inflate(R.layout.fragment_onboarding_name, c, false);
        etName   = v.findViewById(R.id.etName);
        txtCount = v.findViewById(R.id.txtNameCount);

        etName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s,int a,int b,int c){}
            public void onTextChanged(CharSequence s,int a,int b,int c){
                OnboardingActivity.playerName = s.toString();
                txtCount.setText(s.length() + " / 20");
                etName.setBackgroundResource(R.drawable.input_normal);
            }
            public void afterTextChanged(Editable s){}
        });

        return v;
    }

    public void showError() {
        if (etName != null)
            etName.setBackgroundResource(R.drawable.input_error);
    }
}