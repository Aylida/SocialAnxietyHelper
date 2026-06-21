package com.example.socialanxietyhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "CesaretPrefs";

    private static final int[] WORLD_DIAMONDS = {5, 8, 8, 10, 12, 12, 15, 30};

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

    private boolean isDark;
    private int currentUnlockedWorld = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        isDark = ThemeManager.isDark(this);


        loadAndDisplayData();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndDisplayData();
    }

    private void loadAndDisplayData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        int    level         = prefs.getInt("playerLevel", 1);
        int    xp            = prefs.getInt("playerXP", 0);
        int    xpMax         = prefs.getInt("playerXPMax", 100);
        int    gorevCount    = prefs.getInt("completedGorevs", 0);
        int    charIndex     = prefs.getInt("selectedCharacter", 0);
        currentUnlockedWorld = prefs.getInt("unlockedWorld", 1);

        int anchors     = AnchorManager.getAnchors(this);
        int habitStreak = getHabitStreak(prefs);

        // XP bar
        ProgressBar progressXP = findViewById(R.id.progressXP);
        if (progressXP != null) {
            progressXP.setMax(xpMax);
            progressXP.setProgress(xp);
        }

        // Stat satırı
        setText(R.id.txtStatSeri,    String.valueOf(habitStreak));
        setText(R.id.txtStatKristal, String.valueOf(anchors));
        setText(R.id.txtLevelCurrent, String.valueOf(level));
        setText(R.id.txtXPInfo,      String.valueOf(xp));
        setText(R.id.txtStatGorev,   String.valueOf(gorevCount));

        // XP bar alt yazı
        setText(R.id.txtLevelNext, xp + " / " + xpMax + " XP");

        // Avatar
        ImageView imgChar = findViewById(R.id.imgCharacter);
        if (imgChar != null)
            imgChar.setImageResource(CHARACTER_DRAWABLES[Math.min(charIndex, CHARACTER_DRAWABLES.length - 1)]);


        buildTaskList(prefs);
    }
    private int calcStreak(JSONArray dates) {
        if (dates == null || dates.length() == 0) return 0;
        Set<String> set = new HashSet<>();
        try { for (int i = 0; i < dates.length(); i++) set.add(dates.getString(i)); }
        catch (Exception e) { return 0; }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        int streak = 0;
        for (int i = 0; i < 365; i++) {
            String k = sdf.format(cal.getTime());
            if (set.contains(k)) { streak++; cal.add(Calendar.DAY_OF_YEAR, -1); }
            else { if (i == 0) { cal.add(Calendar.DAY_OF_YEAR, -1); continue; } break; }
        }
        return streak;
    }
    private int getHabitStreak(SharedPreferences prefs) {
        String json = prefs.getString("trackingHabits", "");
        if (json.isEmpty()) return 0;
        int max = 0;
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                int s = calcStreak(arr.getJSONObject(i).optJSONArray("dates"));
                if (s > max) max = s;
            }
        } catch (Exception ignored) {}
        return max;
    }



    private String getTaskName(int worldNum) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int catIdx = prefs.getInt("selectedCategory", 0);
        java.util.List<Gorev> gorevler = GorevDeposu.getGorevler(catIdx, worldNum);
        if (gorevler == null || gorevler.isEmpty()) return "Göreve başla";
        Set<String> doneSet = prefs.getStringSet(
                "doneGorevs_" + catIdx + "_" + worldNum, new HashSet<>());
        for (int i = 0; i < gorevler.size(); i++) {
            if (!doneSet.contains(String.valueOf(i))) {
                return gorevler.get(i).getBaslik();
            }
        }
        return gorevler.get(0).getBaslik();
    }

    // ── Görevler listesi (GÖREVLER başlığı altında) ───────────────────────
    private void buildTaskList(SharedPreferences prefs) {
        LinearLayout container = findViewById(R.id.taskListContainer);
        if (container == null) return;
        container.removeAllViews();

        int catIdx = prefs.getInt("selectedCategory", 0);
        int worldNum = prefs.getInt("unlockedWorld", 1);

        String[][] kategoriAdlari = {
                {"Genel","İş","Okul","Mahalle","Romantik","Aile"}
        };

        int[] dotColors = {
                Color.parseColor("#4CAF50"),
                Color.parseColor("#E91E63"),
                Color.parseColor("#FF9800")
        };

        java.util.List<Gorev> gorevler = GorevDeposu.getGorevler(catIdx, worldNum);
        if (gorevler == null || gorevler.isEmpty()) return;

        Set<String> doneSet = prefs.getStringSet(
                "doneGorevs_" + catIdx + "_" + worldNum, new HashSet<>());

        int shown = 0;
        for (int i = 0; i < gorevler.size() && shown < 3; i++) {
            if (doneSet.contains(String.valueOf(i))) continue;
            Gorev g = gorevler.get(i);
            addTaskRow(container, g.getBaslik(),
                    "Bölüm " + worldNum,
                    dotColors[shown % dotColors.length],
                    g.getZorluk());
            shown++;
        }
    }

    private void addTaskRow(LinearLayout container, String title, String sub, int dotColor, int zorluk) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackgroundResource(R.color.cardBg);
        row.setPadding(dp(20), dp(14), dp(20), dp(14));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(dp(16), 0, dp(16), dp(4));
        row.setLayoutParams(lp);
        row.setClickable(true);
        row.setFocusable(true);

        // Renkli nokta
        View dot = new View(this);
        GradientDrawable dotBg = new GradientDrawable();
        dotBg.setShape(GradientDrawable.OVAL);
        dotBg.setColor(dotColor);
        dot.setBackground(dotBg);
        LinearLayout.LayoutParams dotLp = new LinearLayout.LayoutParams(dp(10), dp(10));
        dotLp.setMarginEnd(dp(14));
        dot.setLayoutParams(dotLp);

        // Başlık + alt başlık
        LinearLayout textCol = new LinearLayout(this);
        textCol.setOrientation(LinearLayout.VERTICAL);
        textCol.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView titleTv = new TextView(this);
        titleTv.setText(title);
        titleTv.setTextSize(15);
        titleTv.setTypeface(null, Typeface.BOLD);
        titleTv.setTextColor(ContextCompat.getColor(this, R.color.textPrimary));

        TextView subTv = new TextView(this);
        subTv.setText(sub);
        subTv.setTextSize(11);
        subTv.setTextColor(ContextCompat.getColor(this, R.color.textSecondary));
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subLp.topMargin = dp(2);
        subTv.setLayoutParams(subLp);

        textCol.addView(titleTv);
        textCol.addView(subTv);

        // Yıldızlar
        LinearLayout stars = new LinearLayout(this);
        stars.setOrientation(LinearLayout.HORIZONTAL);
        stars.setGravity(Gravity.CENTER_VERTICAL);
        for (int i = 0; i < 3; i++) {
            TextView star = new TextView(this);
            star.setText(i < zorluk ? "★" : "☆");
            star.setTextSize(14);
            star.setTextColor(i < zorluk ? dotColor : Color.parseColor("#CCCCCC"));
            LinearLayout.LayoutParams starLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            starLp.leftMargin = dp(2);
            star.setLayoutParams(starLp);
            stars.addView(star);
        }

        row.addView(dot);
        row.addView(textCol);
        row.addView(stars);
        container.addView(row);
    }

    private void setupClickListeners() {
        View.OnClickListener goLevelMap = v -> goToLevelMap();
        safeClick(R.id.activeTaskCard, goLevelMap);

        safeClick(R.id.cardWorlds,   v -> startAct(WorldsActivity.class));
        safeClick(R.id.cardChat,     v -> startAct(ChatActivity.class));
        safeClick(R.id.cardtracking, v -> startAct(TrackingActivity.class));
        safeClick(R.id.cardMood,     v -> startAct(ChatActivity.class));


        safeClick(R.id.btnNavHome,     v -> {});
        safeClick(R.id.btnNavTracking, v -> startAct(TrackingActivity.class));
        safeClick(R.id.btnNavWorlds,   v -> startAct(WorldsActivity.class));
        safeClick(R.id.btnNavChat,     v -> startAct(ChatActivity.class));
        safeClick(R.id.btnNavSettings, v -> startAct(SettingsActivity.class));
    }

    private void goToLevelMap() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int worldNum  = prefs.getInt("unlockedWorld", 1);
        int catIdx    = prefs.getInt("selectedCategory", 0);
        int anchorReward = WORLD_DIAMONDS[Math.min(worldNum - 1, WORLD_DIAMONDS.length - 1)];

        String[][] tumTopicler = {
                {"🌱 Zemin","🌿 Varlık","💬 İlk Adım","🎵 Ses","👋 Temas","❤️ Bağ","📡 Hat","🌍 Alan"},
                {"🪑 Masa","🎤 Söz","🤝 Ağ","🎭 Sahne","💡 Teklif","🚫 Sınır","⚡ Çatışma","🧭 Yön"},
                {"✋ El","🚪 Koridor","👨‍🏫 Hoca","🎙️ Kürsü","🍽️ Yemekhane","👥 Takım","🎪 Sahne","🏛️ Kulüp"},
                {"🛒 Bakkal","🚪 Kapı","🌳 Park","🚌 Durak","☕ Çay","🏛️ Meydan","🙏 İstek","🏘️ Semt"},
                {"👁️ Farkında","👀 Göz Teması","💬 İlk Söz","📱 İletişim","☕ Buluşma","💭 Duygular","💔 Reddedilme","🤝 Sınırlar"},
                {"🗺️ Dinamikler","💬 Günlük","👴 Büyükler","❤️ Duygular","🚫 Hayır","⚖️ Anlaşmazlık","💝 Sevgi","👑 Liderlik"}
        };

        int safeCat = Math.min(catIdx, tumTopicler.length - 1);
        int safeWorld = Math.min(worldNum - 1, tumTopicler[safeCat].length - 1);
        String topic = tumTopicler[safeCat][safeWorld];

        Intent intent = new Intent(this, LevelMapActivity.class);
        intent.putExtra("worldNumber",  worldNum);
        intent.putExtra("worldTopic",   topic);
        intent.putExtra("categoryIdx",  catIdx);
        intent.putExtra("worldAnchors", anchorReward);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void startAct(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void saveProgress(android.content.Context ctx, int xpGained, boolean gorevCompleted) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int xp     = prefs.getInt("playerXP", 0);
        int level  = prefs.getInt("playerLevel", 1);
        int xpMax  = prefs.getInt("playerXPMax", 100);
        int gorevs = prefs.getInt("completedGorevs", 0);
        int newXP  = xp + xpGained;
        if (newXP >= xpMax) {
            newXP -= xpMax; level++;
            xpMax = (int)(xpMax * 1.3);
            editor.putInt("playerLevel", level);
            editor.putInt("playerXPMax", xpMax);
        }
        editor.putInt("playerXP", newXP);
        if (gorevCompleted) editor.putInt("completedGorevs", gorevs + 1);
        editor.apply();
    }

    private void setText(int id, String text) {
        TextView tv = findViewById(id);
        if (tv != null) tv.setText(text);
    }

    private void safeClick(int id, View.OnClickListener l) {
        View v = findViewById(id);
        if (v != null) v.setOnClickListener(l);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }
}