package com.example.socialanxietyhelper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * AuthActivity — Kayıt / Giriş / Şifre Sıfırlama
 *
 * Akış:
 *   OnboardingActivity (ilk açılış) → AuthActivity → HomeActivity
 *   SplashActivity (sonraki açılışlar) → AuthActivity (oturum yoksa) → HomeActivity
 */
public class AuthActivity extends AppCompatActivity {

    private static final String MODE_LOGIN    = "login";
    private static final String MODE_REGISTER = "register";
    private static final String MODE_RESET    = "reset";

    private String currentMode = MODE_LOGIN;

    // Views
    private TextView  txtTitle, txtSubtitle, txtSwitch, txtForgot;
    private EditText  etEmail, etPassword, etPasswordConfirm;
    private TextView  btnAction;
    private ProgressBar loading;
    private LinearLayout passwordConfirmWrap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Zaten giriş yapılmışsa direkt HomeActivity'e git
        if (FirebaseManager.isLoggedIn()) {
            goHome();
            return;
        }

        buildUI();
    }

    // ── UI ───────────────────────────────────────────────────────────────
    private void buildUI() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.parseColor("#F5F0E8"));
        root.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(28), dp(0), dp(28), dp(0));

        // Logo / emoji
        TextView logo = new TextView(this);
        logo.setText("⚓");
        logo.setTextSize(56);
        logo.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams logoLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        logoLp.setMargins(0, 0, 0, dp(12));
        logo.setLayoutParams(logoLp);
        root.addView(logo);

        // Başlık
        txtTitle = new TextView(this);
        txtTitle.setTextSize(26);
        txtTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        txtTitle.setTextColor(Color.parseColor("#1A1A1A"));
        txtTitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        titleLp.setMargins(0, 0, 0, dp(6));
        txtTitle.setLayoutParams(titleLp);
        root.addView(txtTitle);

        // Alt başlık
        txtSubtitle = new TextView(this);
        txtSubtitle.setTextSize(13);
        txtSubtitle.setTextColor(Color.parseColor("#888888"));
        txtSubtitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        subLp.setMargins(0, 0, 0, dp(28));
        txtSubtitle.setLayoutParams(subLp);
        root.addView(txtSubtitle);

        // E-posta
        etEmail = makeInput("E-posta adresi", false);
        root.addView(etEmail);

        // Şifre
        etPassword = makeInput("Şifre", true);
        LinearLayout.LayoutParams passLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        passLp.setMargins(0, dp(10), 0, 0);
        etPassword.setLayoutParams(passLp);
        root.addView(etPassword);

        // Şifre tekrar (sadece kayıt modunda görünür)
        passwordConfirmWrap = new LinearLayout(this);
        passwordConfirmWrap.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams confirmWrapLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        confirmWrapLp.setMargins(0, dp(10), 0, 0);
        passwordConfirmWrap.setLayoutParams(confirmWrapLp);

        etPasswordConfirm = makeInput("Şifreyi tekrar gir", true);
        passwordConfirmWrap.addView(etPasswordConfirm);
        passwordConfirmWrap.setVisibility(View.GONE);
        root.addView(passwordConfirmWrap);

        // Şifremi unuttum
        txtForgot = new TextView(this);
        txtForgot.setText("Şifremi unuttum");
        txtForgot.setTextSize(12);
        txtForgot.setTextColor(Color.parseColor("#A0522D"));
        txtForgot.setGravity(Gravity.END);
        LinearLayout.LayoutParams forgotLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        forgotLp.setMargins(0, dp(8), 0, dp(20));
        txtForgot.setLayoutParams(forgotLp);
        txtForgot.setOnClickListener(v -> setMode(MODE_RESET));
        root.addView(txtForgot);

        // Loading
        loading = new ProgressBar(this);
        loading.setVisibility(View.GONE);
        LinearLayout.LayoutParams loadLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        loadLp.gravity = Gravity.CENTER;
        loadLp.setMargins(0, 0, 0, dp(8));
        loading.setLayoutParams(loadLp);
        root.addView(loading);

        // Ana buton
        btnAction = new TextView(this);
        btnAction.setTextSize(15);
        btnAction.setTypeface(null, android.graphics.Typeface.BOLD);
        btnAction.setTextColor(Color.parseColor("#F9E04B"));
        btnAction.setGravity(Gravity.CENTER);
        GradientDrawable btnBg = new GradientDrawable();
        btnBg.setShape(GradientDrawable.RECTANGLE);
        btnBg.setCornerRadius(dp(16));
        btnBg.setColor(Color.parseColor("#1A1714"));
        btnAction.setBackground(btnBg);
        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(54));
        btnLp.setMargins(0, 0, 0, dp(16));
        btnAction.setLayoutParams(btnLp);
        btnAction.setOnClickListener(v -> onActionClick());
        root.addView(btnAction);

        // Geçiş metni (giriş ↔ kayıt)
        txtSwitch = new TextView(this);
        txtSwitch.setTextSize(13);
        txtSwitch.setTextColor(Color.parseColor("#888888"));
        txtSwitch.setGravity(Gravity.CENTER);
        txtSwitch.setOnClickListener(v -> {
            if (currentMode.equals(MODE_LOGIN))        setMode(MODE_REGISTER);
            else if (currentMode.equals(MODE_REGISTER)) setMode(MODE_LOGIN);
            else                                        setMode(MODE_LOGIN);
        });
        root.addView(txtSwitch);

        setContentView(root);
        setMode(MODE_LOGIN);
    }

    // ── Mod geçişi ────────────────────────────────────────────────────────
    private void setMode(String mode) {
        currentMode = mode;
        switch (mode) {
            case MODE_LOGIN:
                txtTitle.setText("Tekrar hoş geldin 👋");
                txtSubtitle.setText("Hesabına giriş yap, kaldığın yerden devam et.");
                btnAction.setText("Giriş Yap");
                txtSwitch.setText("Hesabın yok mu? Kayıt ol →");
                txtForgot.setVisibility(View.VISIBLE);
                passwordConfirmWrap.setVisibility(View.GONE);
                etPassword.setVisibility(View.VISIBLE);
                break;
            case MODE_REGISTER:
                txtTitle.setText("Cesaret yolculuğuna başla ⚓");
                txtSubtitle.setText("Ücretsiz hesap oluştur, ilerlemeni kaydet.");
                btnAction.setText("Kayıt Ol");
                txtSwitch.setText("Zaten hesabın var mı? Giriş yap →");
                txtForgot.setVisibility(View.GONE);
                passwordConfirmWrap.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);
                break;
            case MODE_RESET:
                txtTitle.setText("Şifreni sıfırla 🔑");
                txtSubtitle.setText("E-posta adresine sıfırlama bağlantısı göndereceğiz.");
                btnAction.setText("Sıfırlama Maili Gönder");
                txtSwitch.setText("← Giriş sayfasına dön");
                txtForgot.setVisibility(View.GONE);
                passwordConfirmWrap.setVisibility(View.GONE);
                etPassword.setVisibility(View.GONE);
                break;
        }
    }

    // ── Buton aksiyonu ────────────────────────────────────────────────────
    private void onActionClick() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("E-posta gerekli");
            return;
        }

        switch (currentMode) {
            case MODE_LOGIN:    doLogin(email);    break;
            case MODE_REGISTER: doRegister(email); break;
            case MODE_RESET:    doReset(email);    break;
        }
    }

    private void doLogin(String email) {
        String pass = etPassword.getText().toString();
        if (pass.isEmpty()) { etPassword.setError("Şifre gerekli"); return; }

        setLoading(true);
        FirebaseManager.login(email, pass, new FirebaseManager.AuthCallback() {
            @Override public void onSuccess() {
                setLoading(false);
                goHome();
            }
            @Override public void onError(String message) {
                setLoading(false);
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doRegister(String email) {
        String pass    = etPassword.getText().toString();
        String confirm = etPasswordConfirm.getText().toString();

        if (pass.length() < 6) { etPassword.setError("En az 6 karakter"); return; }
        if (!pass.equals(confirm)) { etPasswordConfirm.setError("Şifreler eşleşmiyor"); return; }

        setLoading(true);
        FirebaseManager.register(email, pass, new FirebaseManager.AuthCallback() {
            @Override public void onSuccess() {
                setLoading(false);
                // Yeni kullanıcı — direkt HomeActivity, veri yoktur
                goHome();
            }
            @Override public void onError(String message) {
                setLoading(false);
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doReset(String email) {
        setLoading(true);
        FirebaseManager.resetPassword(email, new FirebaseManager.AuthCallback() {
            @Override public void onSuccess() {
                setLoading(false);
                Toast.makeText(AuthActivity.this,
                        "Sıfırlama maili gönderildi ✓", Toast.LENGTH_LONG).show();
                setMode(MODE_LOGIN);
            }
            @Override public void onError(String message) {
                setLoading(false);
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    // ── Yardımcılar ───────────────────────────────────────────────────────
    private void setLoading(boolean show) {
        loading.setVisibility(show ? View.VISIBLE : View.GONE);
        btnAction.setAlpha(show ? 0.5f : 1f);
        btnAction.setEnabled(!show);
    }

    private void goHome() {
        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private EditText makeInput(String hint, boolean isPassword) {
        EditText et = new EditText(this);
        et.setHint(hint);
        et.setTextSize(14);
        et.setTextColor(Color.parseColor("#1A1A1A"));
        et.setHintTextColor(Color.parseColor("#B0A898"));
        et.setPadding(dp(16), dp(14), dp(16), dp(14));
        if (isPassword) {
            et.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            et.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(dp(14));
        bg.setColor(Color.WHITE);
        bg.setStroke(dp(1), Color.parseColor("#EAE5DC"));
        et.setBackground(bg);
        et.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return et;
    }

    private int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }
}