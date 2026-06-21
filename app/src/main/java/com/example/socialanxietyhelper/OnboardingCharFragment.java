package com.example.socialanxietyhelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class OnboardingCharFragment extends Fragment {

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

    private CardView[] cards = new CardView[18];

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup c, Bundle b) {
        View root = inf.inflate(R.layout.fragment_onboarding_char, c, false);
        LinearLayout container = root.findViewById(R.id.charContainer);

        for (int row = 0; row < 6; row++) {
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rowLp.setMargins(0, 0, 0, dp(10));
            rowLayout.setLayoutParams(rowLp);

            for (int col = 0; col < 3; col++) {
                int idx = row * 3 + col;

                LinearLayout cell = new LinearLayout(getContext());
                cell.setOrientation(LinearLayout.VERTICAL);
                cell.setGravity(android.view.Gravity.CENTER);
                LinearLayout.LayoutParams cellLp = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                cellLp.setMargins(col > 0 ? dp(10) : 0, 0, 0, 0);
                cell.setLayoutParams(cellLp);

                CardView card = new CardView(requireContext());
                int size = dp(80);
                LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(size, size);
                card.setLayoutParams(cardLp);
                card.setRadius(dp(40));
                card.setCardElevation(0f);
                card.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.cardBg));

                ImageView img = new ImageView(getContext());
                img.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                img.setImageResource(AVATARS[idx]);
                card.addView(img);

                cards[idx] = card;
                final int finalIdx = idx;
                cell.addView(card);
                cell.setOnClickListener(v -> selectChar(finalIdx));
                rowLayout.addView(cell);
            }
            container.addView(rowLayout);
        }

        selectChar(OnboardingActivity.selectedChar);
        return root;
    }

    private void selectChar(int idx) {
        OnboardingActivity.selectedChar = idx;
        for (int i = 0; i < 18; i++) {
            if (cards[i] == null) continue;
            if (i == idx) {
                cards[i].setCardElevation(dp(6));
                cards[i].setScaleX(1.12f);
                cards[i].setScaleY(1.12f);
                // Sarı border için outline drawable
                android.graphics.drawable.GradientDrawable border =
                        new android.graphics.drawable.GradientDrawable();
                border.setShape(android.graphics.drawable.GradientDrawable.OVAL);
                border.setStroke(dp(3), android.graphics.Color.parseColor("#F5C842"));
                border.setColor(android.graphics.Color.TRANSPARENT);
                cards[i].setForeground(border);
            } else {
                cards[i].setCardElevation(0f);
                cards[i].setScaleX(1f);
                cards[i].setScaleY(1f);
                cards[i].setForeground(null);
            }
        }
    }

    private int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }
}