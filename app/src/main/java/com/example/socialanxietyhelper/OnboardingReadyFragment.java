package com.example.socialanxietyhelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class OnboardingReadyFragment extends Fragment {

    private static final int[] AVATARS = {
            R.drawable.avatarmaker_1,  R.drawable.avatarmaker_2,
            R.drawable.avatarmaker_3,  R.drawable.avatarmaker_4,
            R.drawable.avatarmaker_5,  R.drawable.avatarmaker_6,
            R.drawable.avatarmaker_7,  R.drawable.avatarmaker_8,
            R.drawable.avatarmaker_9,  R.drawable.avatarmaker_10,
            R.drawable.avatarmaker_11, R.drawable.avatarmaker_12,
            R.drawable.avatarmaker_13, R.drawable.avatarmaker_14,
            R.drawable.avatarmaker_15, R.drawable.avatarmaker_16,
            R.drawable.avatarmaker_17, R.drawable.avatarmaker_18
    };

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View v = i.inflate(R.layout.fragment_onboarding_ready, c, false);

        ImageView imgChar = v.findViewById(R.id.imgReadyChar);
        int charIdx = Math.min(OnboardingActivity.selectedChar, AVATARS.length - 1);
        if (imgChar != null) imgChar.setImageResource(AVATARS[charIdx]);

        TextView txtGreet = v.findViewById(R.id.txtReadyGreet);
        String name = OnboardingActivity.playerName.trim();
        if (txtGreet != null)
            txtGreet.setText("Merhaba, " + (name.isEmpty() ? "Kahraman" : name) + "! 👋");

        return v;
    }
}