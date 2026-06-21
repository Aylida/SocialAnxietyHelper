package com.example.socialanxietyhelper;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "CesaretPrefs";

    private static final int[] AVATAR_DRAWABLES = {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        loadData();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    // ── Veri yükle ────────────────────────────────────────────────────────
    private void loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String name    = prefs.getString("playerName", "Kahraman");
        int    level   = prefs.getInt("playerLevel", 1);
        int    xp      = prefs.getInt("playerXP", 0);
        int    charIdx = prefs.getInt("selectedCharacter", 0);
        String lang    = prefs.getString("language", "TR");
        int    safeChar = Math.min(charIdx, AVATAR_DRAWABLES.length - 1);

        // Avatar
        ImageView img = findViewById(R.id.txtProfileEmoji);
        if (img != null) img.setImageResource(AVATAR_DRAWABLES[safeChar]);

        // Metinler
        setText(R.id.txtProfileName,  name);
        setText(R.id.txtProfileLevel, "SEVİYE " + level + " · " + xp + " XP");
        setText(R.id.txtNameSub,      name);
        setText(R.id.txtCharSub,      "Avatar " + (safeChar + 1));
        setText(R.id.txtLangSub,      lang);

        // E-posta — Firebase'den
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setText(R.id.txtEmailSub,
                (user != null && user.getEmail() != null) ? user.getEmail() : "—");

        // Dil butonları
        TextView btnTR = findViewById(R.id.btnLangTR);
        TextView btnEN = findViewById(R.id.btnLangEN);
        if (btnTR == null || btnEN == null) return;
        if ("TR".equals(lang)) {
            btnTR.setBackgroundResource(R.drawable.pill_dark);
            btnTR.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            btnEN.setBackgroundResource(R.drawable.pill_gray);
            btnEN.setTextColor(ContextCompat.getColor(this, R.color.textSecondary));
        } else {
            btnEN.setBackgroundResource(R.drawable.pill_dark);
            btnTR.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            btnTR.setBackgroundResource(R.drawable.pill_gray);
            btnEN.setTextColor(ContextCompat.getColor(this, R.color.textSecondary));
        }
    }

    // ── Listener'lar ─────────────────────────────────────────────────────
    private void setupListeners() {

        // Şifre — sıfırlama maili
        safeClick(R.id.itemPassword, () -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null || user.getEmail() == null) return;
            new AlertDialog.Builder(this)
                    .setTitle("Şifre Değiştir")
                    .setMessage(user.getEmail() + " adresine şifre sıfırlama maili gönderilsin mi?")
                    .setPositiveButton("Gönder", (d, w) ->
                            FirebaseAuth.getInstance()
                                    .sendPasswordResetEmail(user.getEmail())
                                    .addOnSuccessListener(v ->
                                            Toast.makeText(this, "Mail gönderildi ✓", Toast.LENGTH_LONG).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show()))
                    .setNegativeButton("İptal", null)
                    .show();
        });

        // Görünen isim
        safeClick(R.id.itemName, () -> {
            EditText et = makeEditText("Görünen ismin...");
            et.setText(getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString("playerName", ""));
            new AlertDialog.Builder(this)
                    .setTitle("Görünen İsim")
                    .setView(et)
                    .setPositiveButton("Kaydet", (d, w) -> {
                        String name = et.getText().toString().trim();
                        if (!name.isEmpty()) {
                            getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                                    .putString("playerName", name).apply();
                            loadData();
                            Toast.makeText(this, "İsim güncellendi!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("İptal", null)
                    .show();
        });

        // Karakter
        safeClick(R.id.itemChar, () ->
                startActivity(new Intent(this, CharacterSelectActivity.class)));

        // Dil
        safeClick(R.id.btnLangTR, () -> setLanguage("TR"));
        safeClick(R.id.btnLangEN, () -> setLanguage("EN"));

        // Tema seçimi — dialog
        safeClick(R.id.itemTheme, () -> {
            boolean currentlyDark = ThemeManager.isDark(this);
            String[] options = {"\uD83C\uDF19  Karanlık mod", "\u2600\uFE0F  Açık mod"};
            int checked = currentlyDark ? 0 : 1;
            androidx.appcompat.app.AlertDialog.Builder builder =
                    new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("Tema Seç");
            builder.setSingleChoiceItems(options, checked, null);
            builder.setPositiveButton("Uygula", (d, w) -> {
                android.widget.ListView lv = ((androidx.appcompat.app.AlertDialog) d).getListView();
                int sel = lv.getCheckedItemPosition();
                boolean wantDark = (sel == 0);
                if (wantDark != currentlyDark) {
                    ThemeManager.setDark(this, wantDark);
                    Intent i = new Intent(this, HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finishAffinity();
                }
            });
            builder.setNegativeButton("İptal", null);
            builder.show();
        });

        // Hakkında
        safeClick(R.id.itemAbout, () ->
                new AlertDialog.Builder(this)
                        .setTitle("Cesaret Koçu")
                        .setMessage("Versiyon 1.0.0\n\nSosyal cesaretini geliştirmek için tasarlandı.\n\n© 2026 Cesaret Koçu")
                        .setPositiveButton("Tamam", null)
                        .show());

        // Veriyi sıfırla
        safeClick(R.id.itemReset, () ->
                new AlertDialog.Builder(this)
                        .setTitle("⚠️ Emin misin?")
                        .setMessage("Tüm ilerleme, XP ve seri kaydı silinecek. Bu işlem geri alınamaz.")
                        .setPositiveButton("Sıfırla", (d, w) -> resetData())
                        .setNegativeButton("İptal", null)
                        .show());

        // Çıkış yap
        safeClick(R.id.itemLogout, () ->
                new AlertDialog.Builder(this)
                        .setTitle("Çıkış Yap")
                        .setMessage("Hesabından çıkış yapmak istediğine emin misin?")
                        .setPositiveButton("Çıkış Yap", (d, w) -> logout())
                        .setNegativeButton("İptal", null)
                        .show());

        // Nav
        safeClick(R.id.btnNavHome, () -> {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
        safeClick(R.id.btnNavWorlds, () -> {
            startActivity(new Intent(this, WorldsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
        safeClick(R.id.btnNavTracking, () -> {
            startActivity(new Intent(this, TrackingActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        safeClick(R.id.btnNavChat, () -> {
            startActivity(new Intent(this, ChatActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        safeClick(R.id.btnNavSettings, () -> {});
    }

    // ── Dil ───────────────────────────────────────────────────────────────
    private void setLanguage(String lang) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                .putString("language", lang).apply();
        loadData();
        Toast.makeText(this,
                "TR".equals(lang) ? "Türkçe seçildi" : "English selected",
                Toast.LENGTH_SHORT).show();
    }

    // ── Veri sıfırla ─────────────────────────────────────────────────────
    private void resetData() {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
        Toast.makeText(this, "Tüm veriler sıfırlandı.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, OnboardingActivity.class));
        finishAffinity();
    }

    // ── Çıkış ────────────────────────────────────────────────────────────
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, AuthActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finishAffinity();
    }

    // ── Yardımcılar ───────────────────────────────────────────────────────
    private void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (tv != null) tv.setText(text);
    }

    private void safeClick(int viewId, Runnable action) {
        android.view.View v = findViewById(viewId);
        if (v != null) v.setOnClickListener(x -> action.run());
    }

    private EditText makeEditText(String hint) {
        EditText et = new EditText(this);
        et.setHint(hint);
        et.setMaxLines(1);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        et.setPadding(dp(16), dp(12), dp(16), dp(12));
        return et;
    }

    private int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }

}