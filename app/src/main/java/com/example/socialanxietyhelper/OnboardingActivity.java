package com.example.socialanxietyhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class OnboardingActivity extends AppCompatActivity {

    private static final String PREFS_NAME          = "CesaretPrefs";
    private static final String KEY_ONBOARDING_DONE = "onboardingDone";

    ViewPager2 viewPager;
    private TextView btnNext;
    private View     progressBar;
    private TextView txtStep;

    static int    selectedChar = 0;
    static String playerName   = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.getBoolean(KEY_ONBOARDING_DONE, false)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_onboarding);

        viewPager   = findViewById(R.id.viewPager);
        btnNext     = findViewById(R.id.btnNext);
        progressBar = findViewById(R.id.progressFill);
        txtStep     = findViewById(R.id.txtStep);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override public int getItemCount() { return 4; }
            @Override public Fragment createFragment(int pos) {
                switch (pos) {
                    case 0:  return new OnboardingWelcomeFragment();
                    case 1:  return new OnboardingNameFragment();
                    case 2:  return new OnboardingCharFragment();
                    case 3:  return new OnboardingReadyFragment();
                    default: return new OnboardingWelcomeFragment();
                }
            }
        });

        viewPager.setUserInputEnabled(false);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int pos) { updateProgress(pos); }
        });

        btnNext.setOnClickListener(v -> nextPage());
        updateProgress(0);
    }

    void nextPage() {
        int cur = viewPager.getCurrentItem();

        if (cur == 1 && playerName.trim().isEmpty()) {
            Fragment f = getSupportFragmentManager()
                    .findFragmentByTag("f" + viewPager.getCurrentItem());
            if (f instanceof OnboardingNameFragment)
                ((OnboardingNameFragment) f).showError();
            return;
        }

        if (cur < 3) {
            viewPager.setCurrentItem(cur + 1, true);
        } else {
            saveAndFinish();
        }
    }

    private void updateProgress(int pos) {
        if (pos == 0) {
            if (txtStep != null) txtStep.setVisibility(View.INVISIBLE);
            btnNext.setText("Hadi Başlayalım →");
        } else if (pos == 3) {
            if (txtStep != null) {
                txtStep.setVisibility(View.VISIBLE);
                txtStep.setText("ADIM 3 / 3");
            }
            btnNext.setText("Başlayalım! 🚀");
        } else {
            if (txtStep != null) {
                txtStep.setVisibility(View.VISIBLE);
                txtStep.setText("ADIM " + pos + " / 3");
            }
            btnNext.setText("Devam Et →");
        }

        if (progressBar != null) {
            progressBar.post(() -> {
                float pct = pos == 0 ? 0.001f : (float) pos / 3f;
                progressBar.setPivotX(0f);
                progressBar.animate()
                        .scaleX(pct)
                        .setDuration(350)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            });
        }
    }

    private void saveAndFinish() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(KEY_ONBOARDING_DONE, true);
        editor.putString("playerName",     playerName.trim());
        editor.putInt("selectedCharacter", selectedChar);
        editor.apply();

        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}