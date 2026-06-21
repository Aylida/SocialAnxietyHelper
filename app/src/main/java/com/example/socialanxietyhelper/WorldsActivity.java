package com.example.socialanxietyhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WorldsActivity extends AppCompatActivity {

    private static final String PREFS_NAME   = "CesaretPrefs";
    private static final String KEY_UNLOCKED = "unlockedWorld";
    private static final String KEY_DIAMONDS = "anchors";

    private static final String[] CAT_KEYS   = {"genel","is","okul","mahalle","romantik","aile"};
    private static final String[] CAT_LABELS = {"🌍 Genel","💼 İş","🎓 Okul","🏘️ Mahalle","❤️ Romantik","👨‍👩‍👧 Aile"};
    private static final int[]    CAT_COSTS  = {0, 120, 80, 60, 150, 90};

    private static final String[][] WORLD_NAMES = {
            {"Göz Teması","Merhaba Demek","Kalabalıkta Yürümek","Soru Sormak",
                    "Tanışmak","Konuşma Başlatmak","Grup İçinde Konuşmak","Telefon Açmak"},
            {"Toplantıda Söz Almak","Sunum Yapmak","Networking Etkinliği","İş Görüşmesi",
                    "Fikir Öne Sürmek","Hayır Demek","Zor Konuşmalar","Liderlik Anı"},
            {"Sınıfta El Kaldırmak","Yeni Arkadaş Edinmek","Hocayla Konuşmak","Sınıfta Sunum",
                    "Kafeteryada Yanına Oturmak","Grup Çalışması","Okul Etkinliğine Katılmak","Mezuniyet Konuşması"},
            {"Markette Soru Sormak","Komşuyla Tanışmak","Parkta Sohbet",
                    "Toplu Taşımada Konuşmak","Kafeye Girmek","Etkinliğe Gitmek",
                    "Yardım İstemek","Toplulukta Öne Çıkmak"},
            {"Göz Teması & Gülümseme","İlk Sözü Söylemek","Numara İstemek","Kahve Teklif Etmek",
                    "Duygularını Söylemek","Reddedilmeyle Yüzleşmek","İlişki Sınırları","Gerçek Bağlantı"},
            {"Aile Sofrasında Söz Almak","Büyüklerle Konuşmak","Aileye Hayır Demek",
                    "Duygularını Paylaşmak","Aile Etkinliğinde Aktif Olmak",
                    "Anlaşmazlıkta Konuşmak","Sevgi İfade Etmek","Aile Liderliği"}
    };

    private static final String[][] WORLD_TOPICS = {
            {"👀 Göz Teması","👋 Merhaba Demek","🚶 Kalabalıkta Yürümek","❓ Soru Sormak",
                    "🤝 Tanışmak","💬 Konuşma Başlatmak","👥 Grup İçinde Konuşmak","📞 Telefon Açmak"},
            {"🗣️ Toplantıda Söz Almak","📊 Sunum Yapmak","🤝 Networking","👔 İş Görüşmesi",
                    "💡 Fikir Öne Sürmek","🚫 Hayır Demek","📧 Zor Konuşmalar","🏆 Liderlik Anı"},
            {"✋ El Kaldırmak","👫 Yeni Arkadaş","👨‍🏫 Hocayla Konuşmak","🎤 Sunum",
                    "🍽️ Kafeterya","📚 Grup Çalışması","🎉 Okul Etkinliği","🎓 Mezuniyet"},
            {"🏪 Markette Soru","🏠 Komşuyla Tanışmak","🌳 Parkta Sohbet","🚌 Toplu Taşıma",
                    "☕ Kafeye Girmek","🎭 Etkinliğe Gitmek","🤲 Yardım İstemek","🌟 Öne Çıkmak"},
            {"😊 Göz Teması","💬 İlk Söz","📱 Numara İstemek","☕ Kahve Teklifi",
                    "🌹 Duygularını Söylemek","💔 Reddedilme","🤝 İlişki Sınırları","❤️ Gerçek Bağlantı"},
            {"🍽️ Aile Sofrası","👴 Büyüklerle Konuşmak","🚫 Aileye Hayır","💭 Duygularını Paylaşmak",
                    "🎉 Aile Etkinliği","🗣️ Anlaşmazlık","🤗 Sevgi İfadesi","🌟 Aile Liderliği"}
    };

    private static final String[] WORLD_COLORS = {
            "#C8420A","#8B1A4A","#1B4332","#1A3A5C",
            "#4A1A6B","#2C4A1A","#5A0A2A","#1A1A1A"
    };

    private static final int[] WORLD_DIAMONDS = {5, 8, 8, 10, 12, 12, 15, 30};

    private static final float[] WORLD_OPACITIES = {1f, 1f, 0.55f, 0.42f, 0.32f, 0.24f, 0.17f, 0.14f};

    private static final int[] CHARACTER_DRAWABLES = {
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

    private int currentCatIdx = 0;
    private TextView[] catTabViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worlds);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentCatIdx = prefs.getInt("selectedCategory", 0);

        buildHeader();
        buildCategoryTabs();
        buildCards();
        setupNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildHeader();
        buildCards();
    }

    // ── Header ────────────────────────────────────────────────────────────
    private void buildHeader() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int anchors = AnchorManager.getAnchors(this);
        int streak  = AnchorManager.getStreak(this);
        int charIdx = prefs.getInt("selectedCharacter", 0);
        int safeIdx = Math.min(charIdx, CHARACTER_DRAWABLES.length - 1);

        // imgCharAvatar artık XML'den geliyor — direkt set et
        ImageView imgChar = findViewById(R.id.imgCharAvatar);
        if (imgChar != null) imgChar.setImageResource(CHARACTER_DRAWABLES[safeIdx]);

        TextView txtAnchors = findViewById(R.id.txtAnchors);
        TextView txtStreak  = findViewById(R.id.txtStreak);
        if (txtAnchors != null) txtAnchors.setText("⚓ " + anchors);
        if (txtStreak  != null) txtStreak.setText("🔥 " + streak);
    }

    // ── Kategori tabları ──────────────────────────────────────────────────
    private void buildCategoryTabs() {
        HorizontalScrollView scrollView = findViewById(R.id.tabsScroll);
        if (scrollView == null) return;
        scrollView.removeAllViews();

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(dp(16), dp(6), dp(16), dp(10));

        catTabViews = new TextView[CAT_LABELS.length];

        for (int i = 0; i < CAT_LABELS.length; i++) {
            final int idx = i;
            TextView tab = new TextView(this);
            tab.setText(CAT_LABELS[i]);
            tab.setTextSize(12);
            tab.setTypeface(null, Typeface.BOLD);
            tab.setPadding(dp(16), dp(8), dp(16), dp(8));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(dp(8));
            tab.setLayoutParams(lp);
            tab.setOnClickListener(v -> {
                currentCatIdx = idx;
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit().putInt("selectedCategory", idx).apply();
                updateTabStyles();
                buildCards();
            });
            catTabViews[i] = tab;
            row.addView(tab);
        }

        scrollView.addView(row);
        updateTabStyles();
    }

    private void updateTabStyles() {
        if (catTabViews == null) return;
        for (int i = 0; i < catTabViews.length; i++) {
            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.RECTANGLE);
            bg.setCornerRadius(dp(50));
            boolean active = (i == currentCatIdx);
            bg.setColor(Color.parseColor(active ? "#1A1A1A" : "#FFFFFF"));
            if (!active) bg.setStroke(dp(1), Color.parseColor("#EAE0CE"));
            catTabViews[i].setBackground(bg);
            catTabViews[i].setTextColor(Color.parseColor(active ? "#F5F0E8" : "#1A1A1A"));
        }
    }

    // ── Kartları oluştur ──────────────────────────────────────────────────
    private void buildCards() {
        LinearLayout container = findViewById(R.id.cardsContainer);
        if (container == null) return;
        container.removeAllViews();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int unlockedWorld   = prefs.getInt(KEY_UNLOCKED, 1);
        int userAnchors     = AnchorManager.getAnchors(this);
        boolean catUnlocked = isCatUnlocked(currentCatIdx, prefs);

        if (!catUnlocked) container.addView(buildLockBanner(userAnchors));

        for (int i = 0; i < 8; i++) {
            final int worldNum = i + 1;
            int completed  = prefs.getInt("completedGorevs_" + currentCatIdx + "_" + worldNum, 0);
            boolean isDone = completed >= 15;
            boolean isActive = catUnlocked && !isDone &&
                    (currentCatIdx == 0 ? worldNum == unlockedWorld : worldNum == 1);
            boolean worldUnlocked = catUnlocked &&
                    (currentCatIdx == 0 ? worldNum <= unlockedWorld : worldNum == 1);

            View card = buildCard(i, worldNum, worldUnlocked, isDone, isActive, completed);

            if (worldUnlocked) {
                card.setOnClickListener(v -> {
                    v.animate().scaleX(0.97f).scaleY(0.97f).setDuration(80)
                            .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(80)
                                    .withEndAction(() -> goToLevelMap(worldNum)).start()).start();
                });
            } else if (catUnlocked) {
                card.setOnClickListener(v -> {
                    v.animate().translationX(8f).setDuration(50)
                            .withEndAction(() -> v.animate().translationX(-8f).setDuration(50)
                                    .withEndAction(() -> v.animate().translationX(0).setDuration(50).start()).start()).start();
                    Toast.makeText(this, "🔒 Önce Bölüm " + (worldNum - 1) + "'i tamamla!", Toast.LENGTH_SHORT).show();
                });
            }

            container.addView(card);
        }
    }

    private boolean isCatUnlocked(int catIdx, SharedPreferences prefs) {
        if (catIdx == 0) return true;
        return prefs.getBoolean("cat_unlocked_" + CAT_KEYS[catIdx], false);
    }

    // ── Kilit banner ──────────────────────────────────────────────────────
    private View buildLockBanner(int userAnchors) {
        int cost = CAT_COSTS[currentCatIdx];
        boolean canAfford = userAnchors >= cost;

        LinearLayout banner = new LinearLayout(this);
        banner.setOrientation(LinearLayout.HORIZONTAL);
        banner.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, dp(12));
        banner.setLayoutParams(lp);
        banner.setPadding(dp(16), dp(12), dp(16), dp(12));

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(dp(14));
        bg.setColor(Color.parseColor(canAfford ? "#E1F5EE" : "#FFF5E6"));
        banner.setBackground(bg);

        LinearLayout textCol = new LinearLayout(this);
        textCol.setOrientation(LinearLayout.VERTICAL);
        textCol.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView title = new TextView(this);
        title.setText(canAfford ? "⚓ Elmasınla açabilirsin!" : "🔒 Bu kategori kilitli");
        title.setTextSize(12);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.parseColor(canAfford ? "#0F6E56" : "#854F0B"));

        TextView sub = new TextView(this);
        sub.setText("Elmasın: " + userAnchors + " ⚓  ·  Gerekli: " + cost + " ⚓");
        sub.setTextSize(10);
        sub.setTextColor(Color.parseColor(canAfford ? "#1D9E75" : "#A0522D"));
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subLp.setMargins(0, dp(3), 0, 0);
        sub.setLayoutParams(subLp);

        textCol.addView(title);
        textCol.addView(sub);

        TextView btnUnlock = new TextView(this);
        btnUnlock.setText(canAfford ? "Aç →" : "Elmas Al");
        btnUnlock.setTextSize(11);
        btnUnlock.setTypeface(null, Typeface.BOLD);
        btnUnlock.setTextColor(Color.WHITE);
        btnUnlock.setPadding(dp(14), dp(8), dp(14), dp(8));
        GradientDrawable btnBg = new GradientDrawable();
        btnBg.setShape(GradientDrawable.RECTANGLE);
        btnBg.setCornerRadius(dp(50));
        btnBg.setColor(Color.parseColor(canAfford ? "#0F6E56" : "#C8420A"));
        btnUnlock.setBackground(btnBg);
        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btnLp.setMarginStart(dp(10));
        btnUnlock.setLayoutParams(btnLp);

        if (canAfford) {
            btnUnlock.setOnClickListener(v -> unlockCategory());
        } else {
            btnUnlock.setOnClickListener(v ->
                    Toast.makeText(this, "⚓ Elmas mağazası yakında!", Toast.LENGTH_SHORT).show());
        }

        banner.addView(textCol);
        banner.addView(btnUnlock);
        return banner;
    }

    private void unlockCategory() {
        int cost = CAT_COSTS[currentCatIdx];
        if (AnchorManager.spendAnchors(this, cost)) {
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putBoolean("cat_unlocked_" + CAT_KEYS[currentCatIdx], true)
                    .apply();
            buildHeader();
            buildCards();
            Toast.makeText(this, "🎉 " + CAT_LABELS[currentCatIdx] + " açıldı!", Toast.LENGTH_SHORT).show();
        }
    }

    // ── Tek kart ──────────────────────────────────────────────────────────
    private View buildCard(int idx, int worldNum, boolean unlocked, boolean isDone, boolean isActive, int completed) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardLp.setMargins(0, 0, 0, dp(10));
        card.setLayoutParams(cardLp);
        card.setPadding(dp(18), dp(18), dp(18), dp(14));
        card.setClickable(true);
        card.setFocusable(true);

        if (isDone)         card.setAlpha(0.72f);
        else if (!unlocked) card.setAlpha(WORLD_OPACITIES[idx]);
        else                card.setAlpha(1f);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadii(new float[]{dp(4),dp(4), dp(20),dp(20), dp(20),dp(20), dp(20),dp(20)});
        bg.setColor(Color.parseColor(WORLD_COLORS[idx]));
        if (isActive) bg.setStroke(dp(2), Color.argb(50, 255, 255, 255));
        card.setBackground(bg);

        android.widget.FrameLayout frame = new android.widget.FrameLayout(this);
        frame.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setLayoutParams(new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout badgeRow = new LinearLayout(this);
        badgeRow.setOrientation(LinearLayout.HORIZONTAL);
        badgeRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams brLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        brLp.setMargins(0, 0, 0, dp(8));
        badgeRow.setLayoutParams(brLp);

        if (isDone) {
            TextView doneBadge = new TextView(this);
            doneBadge.setText("✓  TAMAMLANDI");
            doneBadge.setTextSize(9);
            doneBadge.setTypeface(null, Typeface.BOLD);
            doneBadge.setTextColor(Color.parseColor(WORLD_COLORS[idx]));
            doneBadge.setPadding(dp(10), dp(3), dp(10), dp(3));
            GradientDrawable dBg = new GradientDrawable();
            dBg.setShape(GradientDrawable.RECTANGLE);
            dBg.setCornerRadius(dp(50));
            dBg.setColor(Color.argb(230, 255, 255, 255));
            doneBadge.setBackground(dBg);
            badgeRow.addView(doneBadge);
        } else if (isActive) {
            TextView activeBadge = new TextView(this);
            activeBadge.setText("AKTİF");
            activeBadge.setTextSize(9);
            activeBadge.setTypeface(null, Typeface.BOLD);
            activeBadge.setTextColor(Color.parseColor(WORLD_COLORS[idx]));
            activeBadge.setPadding(dp(10), dp(3), dp(10), dp(3));
            GradientDrawable aBg = new GradientDrawable();
            aBg.setShape(GradientDrawable.RECTANGLE);
            aBg.setCornerRadius(dp(50));
            aBg.setColor(Color.argb(230, 255, 255, 255));
            activeBadge.setBackground(aBg);
            badgeRow.addView(activeBadge);
        }

        if (badgeRow.getChildCount() > 0) content.addView(badgeRow);

        TextView txtNo = new TextView(this);
        txtNo.setText("BÖLÜM " + worldNum + (worldNum == 8 ? " · FİNAL" : ""));
        txtNo.setTextSize(9);
        txtNo.setTypeface(null, Typeface.BOLD);
        txtNo.setLetterSpacing(0.15f);
        txtNo.setTextColor(worldNum == 8
                ? Color.argb(102, 249, 224, 75)
                : Color.argb(128, 255, 255, 255));
        LinearLayout.LayoutParams noLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        noLp.setMargins(0, 0, 0, dp(4));
        txtNo.setLayoutParams(noLp);
        content.addView(txtNo);

        TextView txtName = new TextView(this);
        txtName.setText(WORLD_NAMES[currentCatIdx][idx]);
        txtName.setTextSize(18);
        txtName.setTypeface(null, Typeface.BOLD);
        txtName.setTextColor(worldNum == 8 ? Color.parseColor("#F9E04B") : Color.WHITE);
        LinearLayout.LayoutParams nameLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        nameLp.setMargins(0, 0, 0, dp(10));
        txtName.setLayoutParams(nameLp);
        content.addView(txtName);

        if (isActive || isDone) {
            int progress = Math.min(completed, 15) * 100 / 15;
            android.widget.ProgressBar pb = new android.widget.ProgressBar(this,
                    null, android.R.attr.progressBarStyleHorizontal);
            pb.setMax(100);
            pb.setProgress(progress);
            pb.setProgressTintList(android.content.res.ColorStateList.valueOf(
                    isDone ? Color.argb(180, 255, 255, 255) : Color.WHITE));
            pb.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    Color.argb(40, 255, 255, 255)));
            LinearLayout.LayoutParams pbLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dp(5));
            pbLp.setMargins(0, 0, 0, dp(6));
            pb.setLayoutParams(pbLp);
            content.addView(pb);
        }

        LinearLayout bottomRow = new LinearLayout(this);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setGravity(Gravity.CENTER_VERTICAL);
        bottomRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView txtStatus = new TextView(this);
        if (isDone)          txtStatus.setText("15 / 15 görev tamamlandı");
        else if (isActive)   txtStatus.setText(completed + " / 15 görev");
        else if (unlocked)   txtStatus.setText(completed + " / 15 görev");
        else                 txtStatus.setText("15 görev · Kilitli");
        txtStatus.setTextSize(10);
        txtStatus.setTextColor(Color.argb(160, 255, 255, 255));
        txtStatus.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        bottomRow.addView(txtStatus);

        if (isActive || isDone) {
            TextView btnDevam = new TextView(this);
            btnDevam.setText(isDone ? "Tekrar Gir →" : "Devam →");
            btnDevam.setTextSize(11);
            btnDevam.setTypeface(null, Typeface.BOLD);
            btnDevam.setTextColor(Color.parseColor(WORLD_COLORS[idx]));
            btnDevam.setPadding(dp(14), dp(6), dp(14), dp(6));
            GradientDrawable dBg = new GradientDrawable();
            dBg.setShape(GradientDrawable.RECTANGLE);
            dBg.setCornerRadius(dp(50));
            dBg.setColor(Color.WHITE);
            btnDevam.setBackground(dBg);
            LinearLayout.LayoutParams devamLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            devamLp.setMarginEnd(dp(8));
            btnDevam.setLayoutParams(devamLp);
            bottomRow.addView(btnDevam);
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean anchorClaimed = prefs.getBoolean(
                "anchor_claimed_" + currentCatIdx + "_" + worldNum, false);

        TextView anchorBadge = new TextView(this);
        if (isDone && anchorClaimed) {
            anchorBadge.setText("⚓ Alındı ✓");
            anchorBadge.setTextColor(Color.argb(180, 255, 255, 255));
        } else {
            anchorBadge.setText("⚓ +" + WORLD_DIAMONDS[idx]);
            anchorBadge.setTextColor(Color.WHITE);
        }
        anchorBadge.setTextSize(10);
        anchorBadge.setTypeface(null, Typeface.BOLD);
        anchorBadge.setPadding(dp(8), dp(3), dp(8), dp(3));
        GradientDrawable dBg2 = new GradientDrawable();
        dBg2.setShape(GradientDrawable.RECTANGLE);
        dBg2.setCornerRadius(dp(50));
        dBg2.setColor(Color.argb(isDone && anchorClaimed ? 30 : 46, 255, 255, 255));
        anchorBadge.setBackground(dBg2);
        bottomRow.addView(anchorBadge);

        content.addView(bottomRow);
        frame.addView(content);

        TextView watermark = new TextView(this);
        watermark.setText(String.format("%02d", worldNum));
        watermark.setTextSize(56);
        watermark.setTypeface(null, Typeface.BOLD);
        watermark.setTextColor(worldNum == 8
                ? Color.argb(15, 249, 224, 75)
                : Color.argb(20, 255, 255, 255));
        watermark.setLetterSpacing(-0.05f);
        android.widget.FrameLayout.LayoutParams wLp = new android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                android.widget.FrameLayout.LayoutParams.WRAP_CONTENT);
        wLp.gravity = Gravity.END | Gravity.TOP;
        wLp.setMargins(0, dp(-8), dp(8), 0);
        watermark.setLayoutParams(wLp);
        frame.addView(watermark);

        card.addView(frame);
        return card;
    }

    // ── Navigasyon ────────────────────────────────────────────────────────
    private void goToLevelMap(int worldNum) {
        Intent intent = new Intent(this, LevelMapActivity.class);
        intent.putExtra("worldNumber",  worldNum);
        intent.putExtra("worldTopic",   WORLD_TOPICS[currentCatIdx][worldNum - 1]);
        intent.putExtra("categoryIdx",  currentCatIdx);
        intent.putExtra("worldAnchors", WORLD_DIAMONDS[worldNum - 1]);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void setupNav() {
        findViewById(R.id.btnNavHome).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
        findViewById(R.id.btnNavWorlds).setOnClickListener(v -> {});
        findViewById(R.id.btnNavChat).setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        findViewById(R.id.btnNavSettings).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
        findViewById(R.id.btnNavTracking).setOnClickListener(v -> {
            startActivity(new Intent(this, TrackingActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    // ── Statik yardımcılar ────────────────────────────────────────────────
    public static void unlockNextWorld(android.content.Context ctx, int completedWorld) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
        int current = prefs.getInt(KEY_UNLOCKED, 1);
        if (completedWorld >= current && completedWorld < 8) {
            prefs.edit().putInt(KEY_UNLOCKED, completedWorld + 1).apply();
        }
    }

    public static void addAnchors(android.content.Context ctx, int amount) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
        int current = prefs.getInt(KEY_DIAMONDS, 0);
        prefs.edit().putInt(KEY_DIAMONDS, current + amount).apply();
    }

    private int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }
}