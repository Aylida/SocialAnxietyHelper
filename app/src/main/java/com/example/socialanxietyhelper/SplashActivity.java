package com.example.socialanxietyhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity — uygulamanın ilk açıldığı Activity (AndroidManifest'te launcher olmalı)
 *
 * Akış:
 *   İlk açılış (onboarding görülmedi) → OnboardingActivity
 *   Oturum açık                        → HomeActivity (+ buluttan veri çek)
 *   Oturum yok                         → AuthActivity
 */
public class SplashActivity extends AppCompatActivity {

    private static final String PREFS_NAME         = "CesaretPrefs";
    private static final String KEY_ONBOARDING_DONE = "onboardingDone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean onboardingDone = prefs.getBoolean(KEY_ONBOARDING_DONE, false);

        if (!onboardingDone) {
            // İlk açılış — onboarding'e git
            go(OnboardingActivity.class);
            return;
        }

        if (FirebaseManager.isLoggedIn()) {
            // Oturum var — direkt HomeActivity
            go(HomeActivity.class);
        } else {
            // Oturum yok — giriş sayfasına
            go(AuthActivity.class);
        }
    }

    private void go(Class<?> target) {
        startActivity(new Intent(this, target));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
