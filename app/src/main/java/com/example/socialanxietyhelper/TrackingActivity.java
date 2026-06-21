package com.example.socialanxietyhelper;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TrackingActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "CesaretPrefs";
    private static final String KEY_HABITS = "trackingHabits";

    private static final String[] MONTH_NAMES = {
            "Ocak","Şubat","Mart","Nisan","Mayıs","Haziran",
            "Temmuz","Ağustos","Eylül","Ekim","Kasım","Aralık"
    };
    private static final String[] DAY_NAMES    = {"Pt","Sa","Ça","Pe","Cu","Ct","Pz"};
    private static final String[] DAY_INITIALS = {"P","S","Ç","P","C","C","P"};
    private static final int[]    MONTH_DAYS   = {31,28,31,30,31,30,31,31,30,31,30,31};

    private static final int[] HABIT_COLORS = {
            0xFFE8633A, 0xFF3BAD7A, 0xFF8B6FD4,
            0xFF5B9DBF, 0xFFD4A843, 0xFFD45B8A
    };
    private static final int[] HABIT_BG_COLORS = {
            0xFFFDF0EB, 0xFFEBF8F2, 0xFFF0ECFB,
            0xFFE8F2F8, 0xFFFBF5E6, 0xFFFBECF4
    };
    private static final int[] HABIT_BG_COLORS_DARK = {
            0xFF2A1810, 0xFF0E2018, 0xFF1E1530,
            0xFF0E1E2A, 0xFF2A2210, 0xFF2A1020
    };

    private static class Habit {
        String name;
        int colorIdx;
        List<String> doneDates = new ArrayList<>();
        Habit(String name, int colorIdx) {
            this.name = name;
            this.colorIdx = colorIdx;
        }
    }

    private final List<Habit> habits = new ArrayList<>();
    private int currentWeekOffset = 0;
    private int currentMonth, currentYear;
    private int currentMonthHabitIdx = 0;
    private int currentViewYear;

    private LinearLayout viewWeekly, viewMonthly, viewYearly;
    private TextView tabWeekly, tabMonthly, tabYearly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.apply(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Calendar cal = Calendar.getInstance();
        currentMonth = cal.get(Calendar.MONTH);
        currentYear  = cal.get(Calendar.YEAR);
        currentViewYear = currentYear;

        bindViews();
        loadHabits();
        setupListeners();
        setupNav();
        renderWeekly();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int streak = habits.stream()
                .mapToInt(h -> calcStreak(h))
                .max()
                .orElse(0);
        ((TextView) findViewById(R.id.txtStreakBadge)).setText(streak + " gün seri");
    }

    private void bindViews() {
        viewWeekly  = findViewById(R.id.viewWeekly);
        viewMonthly = findViewById(R.id.viewMonthly);
        viewYearly  = findViewById(R.id.viewYearly);
        tabWeekly   = findViewById(R.id.tabWeekly);
        tabMonthly  = findViewById(R.id.tabMonthly);
        tabYearly   = findViewById(R.id.tabYearly);
    }

    // ── Tema yardımcıları ─────────────────────────────────────────────────
    private boolean isDark() { return ThemeManager.isDark(this); }

    private int cardColor() {
        return isDark() ? Color.parseColor("#242018") : Color.WHITE;
    }

    private int textPrimary() {
        return isDark() ? Color.WHITE : Color.parseColor("#1A1A1A");
    }

    private int textSecondary() {
        return isDark() ? Color.parseColor("#80FFFFFF") : Color.parseColor("#B0A898");
    }

    private int bgEmpty() {
        return isDark() ? Color.parseColor("#2C2820") : Color.parseColor("#E8E3DC");
    }

    private int habitBgColor(int colorIdx) {
        return isDark()
                ? HABIT_BG_COLORS_DARK[colorIdx % HABIT_BG_COLORS_DARK.length]
                : HABIT_BG_COLORS[colorIdx % HABIT_BG_COLORS.length];
    }

    // ── Veri ──────────────────────────────────────────────────────────────
    private void loadHabits() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_HABITS, "");
        habits.clear();

        if (json.isEmpty()) {
            habits.add(new Habit("Günlük yazma", 0));
            habits.add(new Habit("Spor", 1));
            habits.add(new Habit("Sesli kitap okuma", 2));
        } else {
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    org.json.JSONObject obj = arr.getJSONObject(i);
                    Habit h = new Habit(obj.getString("name"), obj.getInt("colorIdx"));
                    JSONArray dates = obj.optJSONArray("dates");
                    if (dates != null)
                        for (int j = 0; j < dates.length(); j++)
                            h.doneDates.add(dates.getString(j));
                    habits.add(h);
                }
            } catch (JSONException e) { e.printStackTrace(); }
        }
    }

    private void saveHabits() {
        try {
            JSONArray arr = new JSONArray();
            for (Habit h : habits) {
                org.json.JSONObject obj = new org.json.JSONObject();
                obj.put("name", h.name);
                obj.put("colorIdx", h.colorIdx);
                JSONArray dates = new JSONArray();
                for (String d : h.doneDates) dates.put(d);
                obj.put("dates", dates);
                arr.put(obj);
            }
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit().putString(KEY_HABITS, arr.toString()).apply();
        } catch (JSONException e) { e.printStackTrace(); }
    }

    private int calcStreak(Habit h) {
        if (h.doneDates.isEmpty()) return 0;
        Set<String> dateSet = new HashSet<>(h.doneDates);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        int streak = 0;
        for (int i = 0; i < 365; i++) {
            String key = sdf.format(cal.getTime());
            if (dateSet.contains(key)) {
                streak++;
                cal.add(Calendar.DAY_OF_YEAR, -1);
            } else {
                if (i == 0) { cal.add(Calendar.DAY_OF_YEAR, -1); continue; }
                break;
            }
        }
        return streak;
    }

    // ── Tab yönetimi ──────────────────────────────────────────────────────
    private void setupListeners() {
        tabWeekly.setOnClickListener(v  -> showTab(0));
        tabMonthly.setOnClickListener(v -> showTab(1));
        tabYearly.setOnClickListener(v  -> showTab(2));

        findViewById(R.id.btnPrevWeek).setOnClickListener(v -> { currentWeekOffset--; renderWeekly(); });
        findViewById(R.id.btnNextWeek).setOnClickListener(v -> { currentWeekOffset++; renderWeekly(); });
        findViewById(R.id.btnPrevMonth).setOnClickListener(v -> {
            currentMonth--; if (currentMonth < 0) { currentMonth = 11; currentYear--; } renderMonthly();
        });
        findViewById(R.id.btnNextMonth).setOnClickListener(v -> {
            currentMonth++; if (currentMonth > 11) { currentMonth = 0; currentYear++; } renderMonthly();
        });
        findViewById(R.id.btnPrevYear).setOnClickListener(v -> { currentViewYear--; renderYearly(); });
        findViewById(R.id.btnNextYear).setOnClickListener(v -> { currentViewYear++; renderYearly(); });
        findViewById(R.id.btnAddHabit).setOnClickListener(v -> showAddHabitDialog());
    }

    private void showTab(int tab) {
        tabWeekly.setBackground(tab == 0 ? makeTabPill() : null);
        tabWeekly.setTextColor(tab == 0 ? textPrimary() : textSecondary());
        tabMonthly.setBackground(tab == 1 ? makeTabPill() : null);
        tabMonthly.setTextColor(tab == 1 ? textPrimary() : textSecondary());
        tabYearly.setBackground(tab == 2 ? makeTabPill() : null);
        tabYearly.setTextColor(tab == 2 ? textPrimary() : textSecondary());

        viewWeekly.setVisibility(tab == 0 ? View.VISIBLE : View.GONE);
        viewMonthly.setVisibility(tab == 1 ? View.VISIBLE : View.GONE);
        viewYearly.setVisibility(tab == 2 ? View.VISIBLE : View.GONE);

        if (tab == 1) renderMonthly();
        if (tab == 2) renderYearly();
    }

    private GradientDrawable makeTabPill() {
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.RECTANGLE);
        d.setCornerRadius(dp(10));
        d.setColor(cardColor());
        return d;
    }

    // ── HAFTALIK ──────────────────────────────────────────────────────────
    private void renderWeekly() {
        Calendar weekStart = getWeekStart(currentWeekOffset);
        Calendar weekEnd   = (Calendar) weekStart.clone();
        weekEnd.add(Calendar.DAY_OF_YEAR, 6);

        String label = MONTH_NAMES[weekStart.get(Calendar.MONTH)] + " "
                + weekStart.get(Calendar.YEAR) + " · "
                + weekStart.get(Calendar.DAY_OF_MONTH) + " – "
                + weekEnd.get(Calendar.DAY_OF_MONTH);
        ((TextView) findViewById(R.id.txtWeekLabel)).setText(label);
        ((TextView) findViewById(R.id.txtWeekLabel)).setTextColor(textPrimary());

        buildWeekDayRow(weekStart);
        buildBarChart(weekStart);
        buildWeekStats(weekStart);
        buildHabitsRows(weekStart);
        buildRing(weekStart);
    }

    private Calendar getWeekStart(int offset) {
        Calendar cal = Calendar.getInstance();
        int dow  = cal.get(Calendar.DAY_OF_WEEK);
        int diff = (dow == Calendar.SUNDAY) ? -6 : 2 - dow;
        cal.add(Calendar.DAY_OF_YEAR, diff + offset * 7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    private void buildWeekDayRow(Calendar weekStart) {
        LinearLayout row = findViewById(R.id.weekDayRow);
        row.removeAllViews();
        Calendar today = Calendar.getInstance();
        Calendar day   = (Calendar) weekStart.clone();

        for (int i = 0; i < 7; i++) {
            boolean isToday  = isSameDay(day, today);
            boolean isFuture = day.after(today);

            LinearLayout col = new LinearLayout(this);
            col.setOrientation(LinearLayout.VERTICAL);
            col.setGravity(Gravity.CENTER);
            col.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView dayName = new TextView(this);
            dayName.setText(DAY_NAMES[i]);
            dayName.setTextSize(10);
            dayName.setTypeface(null, isToday ? Typeface.BOLD : Typeface.NORMAL);
            dayName.setTextColor(isToday ? Color.parseColor("#C8420A")
                    : isFuture ? (isDark() ? Color.parseColor("#40FFFFFF") : Color.parseColor("#D8D0C8"))
                    : textSecondary());
            dayName.setGravity(Gravity.CENTER);
            col.addView(dayName);

            TextView dayNum = new TextView(this);
            dayNum.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));
            dayNum.setTextSize(12);
            dayNum.setTypeface(null, isToday ? Typeface.BOLD : Typeface.NORMAL);
            dayNum.setTextColor(isToday ? Color.parseColor("#C8420A")
                    : isFuture ? (isDark() ? Color.parseColor("#40FFFFFF") : Color.parseColor("#D8D0C8"))
                    : textSecondary());
            dayNum.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams numLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            numLp.setMargins(0, dp(3), 0, dp(3));
            dayNum.setLayoutParams(numLp);
            col.addView(dayNum);

            View dot = new View(this);
            int dotSize = isToday ? dp(7) : dp(5);
            LinearLayout.LayoutParams dotLp = new LinearLayout.LayoutParams(dotSize, dotSize);
            dot.setLayoutParams(dotLp);
            GradientDrawable dotBg = new GradientDrawable();
            dotBg.setShape(GradientDrawable.OVAL);
            dotBg.setColor(isToday ? Color.parseColor("#C8420A") : bgEmpty());
            dot.setBackground(dotBg);
            col.addView(dot);

            row.addView(col);
            day.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void buildBarChart(Calendar weekStart) {
        LinearLayout container = findViewById(R.id.barChartContainer);
        Calendar today = Calendar.getInstance();
        Calendar day   = (Calendar) weekStart.clone();
        int maxDone    = Math.max(1, habits.size());

        String weekKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(weekStart.getTime());
        boolean isSameWeek = weekKey.equals(container.getTag());
        boolean isFirstBuild = container.getChildCount() == 0 || !isSameWeek;

        if (!isSameWeek) {
            container.removeAllViews();
            container.setTag(weekKey);
        }

        for (int i = 0; i < 7; i++) {
            boolean isToday  = isSameDay(day, today);
            boolean isFuture = day.after(today);

            int done = 0;
            if (!isFuture) for (Habit h : habits) if (isDone(h, day)) done++;

            final int targetH = isFuture ? dp(6)
                    : Math.max(dp(6), (int)(dp(50) * (float) done / maxDone));

            int barColor = isToday  ? Color.parseColor("#C8420A")
                    : isFuture ? bgEmpty()
                    : (isDark() ? Color.parseColor("#F5C842") : Color.parseColor("#1A1A1A"));

            if (isFirstBuild) {
                LinearLayout col = new LinearLayout(this);
                col.setOrientation(LinearLayout.VERTICAL);
                col.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                col.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));

                View bar = new View(this);
                LinearLayout.LayoutParams barLp = new LinearLayout.LayoutParams(dp(10), dp(4));
                barLp.setMargins(0, 0, 0, dp(5));
                bar.setLayoutParams(barLp);
                GradientDrawable barBg = new GradientDrawable();
                barBg.setShape(GradientDrawable.RECTANGLE);
                barBg.setCornerRadius(dp(3));
                barBg.setColor(barColor);
                bar.setBackground(barBg);
                col.addView(bar);

                TextView letter = new TextView(this);
                letter.setText(DAY_INITIALS[i]);
                letter.setTextSize(9);
                letter.setTypeface(null, isToday ? Typeface.BOLD : Typeface.NORMAL);
                letter.setTextColor(isToday ? Color.parseColor("#C8420A") : textSecondary());
                letter.setGravity(Gravity.CENTER);
                col.addView(letter);
                container.addView(col);

                final int delay = i * 55;
                bar.post(() -> {
                    android.animation.ValueAnimator anim =
                            android.animation.ValueAnimator.ofInt(dp(4), targetH);
                    anim.setDuration(450);
                    anim.setStartDelay(delay);
                    anim.setInterpolator(new android.view.animation.DecelerateInterpolator(1.5f));
                    anim.addUpdateListener(a -> {
                        LinearLayout.LayoutParams lp =
                                (LinearLayout.LayoutParams) bar.getLayoutParams();
                        lp.height = (int) a.getAnimatedValue();
                        bar.setLayoutParams(lp);
                    });
                    anim.start();
                });
            } else {
                LinearLayout col = (LinearLayout) container.getChildAt(i);
                if (col == null) continue;
                View bar = col.getChildAt(0);
                if (bar == null) continue;

                GradientDrawable barBg = new GradientDrawable();
                barBg.setShape(GradientDrawable.RECTANGLE);
                barBg.setCornerRadius(dp(3));
                barBg.setColor(barColor);
                bar.setBackground(barBg);

                int currentH = bar.getLayoutParams().height;
                if (currentH == targetH) { day.add(Calendar.DAY_OF_YEAR, 1); continue; }

                android.animation.ValueAnimator anim =
                        android.animation.ValueAnimator.ofInt(currentH, targetH);
                anim.setDuration(300);
                anim.setInterpolator(new android.view.animation.DecelerateInterpolator());
                anim.addUpdateListener(a -> {
                    LinearLayout.LayoutParams lp =
                            (LinearLayout.LayoutParams) bar.getLayoutParams();
                    lp.height = (int) a.getAnimatedValue();
                    bar.setLayoutParams(lp);
                });
                anim.start();
            }
            day.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void buildRing(Calendar weekStart) {
        FrameLayout ringContainer = findViewById(R.id.ringContainer);

        Calendar today = Calendar.getInstance();
        int done = 0, possible = 0;
        Calendar day = (Calendar) weekStart.clone();
        for (int di = 0; di < 7; di++) {
            if (!day.after(today)) {
                for (Habit h : habits) { possible++; if (isDone(h, day)) done++; }
            }
            day.add(Calendar.DAY_OF_YEAR, 1);
        }
        final int rate = possible == 0 ? 0 : done * 100 / possible;
        final float targetSweep = 360f * rate / 100f;

        TextView rateTv = findViewById(R.id.txtWeekRate);
        rateTv.setTextColor(textPrimary());
        String current = rateTv.getText().toString().replace("%", "").trim();
        int currentRate = 0;
        try { currentRate = Integer.parseInt(current); } catch (Exception ignored) {}

        if (currentRate != rate) {
            final int fromRate = currentRate;
            android.animation.ValueAnimator countAnim =
                    android.animation.ValueAnimator.ofInt(fromRate, rate);
            countAnim.setDuration(500);
            countAnim.setInterpolator(new android.view.animation.DecelerateInterpolator());
            countAnim.addUpdateListener(a -> rateTv.setText(a.getAnimatedValue() + "%"));
            countAnim.start();
        }

        final float[] currentSweep = {0f};
        if (ringContainer.getChildCount() > 0) {
            View existing = ringContainer.getChildAt(0);
            Object tag = existing.getTag();
            if (tag instanceof Float) currentSweep[0] = (Float) tag;
            ringContainer.removeAllViews();
        }

        int ringFgColor = isDark() ? Color.parseColor("#F5C842") : Color.parseColor("#1A1A1A");
        int ringBgColor = isDark() ? Color.parseColor("#2C2820") : Color.parseColor("#E8E3DC");

        View ringView = new View(this) {
            @Override
            protected void onDraw(Canvas canvas) {
                float w = getWidth(), h = getHeight();
                float cx = w / 2f, cy = h / 2f;
                float radius = Math.min(w, h) / 2f - dp(4);
                Paint bgP = new Paint(Paint.ANTI_ALIAS_FLAG);
                bgP.setStyle(Paint.Style.STROKE);
                bgP.setStrokeWidth(dp(6));
                bgP.setColor(ringBgColor);
                canvas.drawCircle(cx, cy, radius, bgP);
                Paint fgP = new Paint(Paint.ANTI_ALIAS_FLAG);
                fgP.setStyle(Paint.Style.STROKE);
                fgP.setStrokeWidth(dp(6));
                fgP.setStrokeCap(Paint.Cap.ROUND);
                fgP.setColor(ringFgColor);
                android.graphics.RectF oval = new android.graphics.RectF(
                        cx - radius, cy - radius, cx + radius, cy + radius);
                canvas.drawArc(oval, -90, currentSweep[0], false, fgP);
            }
        };
        ringView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        ringContainer.addView(ringView);

        android.animation.ValueAnimator ringAnim =
                android.animation.ValueAnimator.ofFloat(currentSweep[0], targetSweep);
        ringAnim.setDuration(500);
        ringAnim.setInterpolator(new android.view.animation.DecelerateInterpolator());
        ringAnim.addUpdateListener(a -> {
            currentSweep[0] = (float) a.getAnimatedValue();
            ringView.setTag(currentSweep[0]);
            ringView.invalidate();
        });
        ringAnim.start();
    }

    private void buildWeekStats(Calendar weekStart) {
        LinearLayout statsRow = findViewById(R.id.statsRow);
        statsRow.removeAllViews();

        int streak = habits.stream().mapToInt(h -> calcStreak(h)).max().orElse(0);
        TextView badge = findViewById(R.id.txtStreakBadge);
        if (badge != null) badge.setText(streak + " gün seri");

        int anchors = AnchorManager.getAnchors(this);

        Calendar today = Calendar.getInstance();
        int done = 0;
        Calendar day = (Calendar) weekStart.clone();
        for (int di = 0; di < 7; di++) {
            if (!day.after(today)) for (Habit h : habits) if (isDone(h, day)) done++;
            day.add(Calendar.DAY_OF_YEAR, 1);
        }

        String[] icons  = {"🔥", "⚓", "⭐"};
        String[] vals   = {String.valueOf(streak), String.valueOf(anchors), String.valueOf(done)};
        String[] labels = {"SERİ", "ÇAPA", "GÖREV"};

        for (int i = 0; i < 3; i++) {
            LinearLayout col = new LinearLayout(this);
            col.setOrientation(LinearLayout.VERTICAL);
            col.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams colLp = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            col.setLayoutParams(colLp);
            col.setPadding(dp(6), dp(14), dp(6), dp(14));
            GradientDrawable colBg = new GradientDrawable();
            colBg.setShape(GradientDrawable.RECTANGLE);
            colBg.setCornerRadius(dp(14));
            colBg.setColor(cardColor());
            col.setBackground(colBg);

            TextView iconTv = new TextView(this);
            iconTv.setText(icons[i]); iconTv.setTextSize(22);
            iconTv.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams iLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            iLp.setMargins(0, 0, 0, dp(5)); iconTv.setLayoutParams(iLp);
            col.addView(iconTv);

            TextView valTv = new TextView(this);
            valTv.setText(vals[i]); valTv.setTextSize(18);
            valTv.setTypeface(null, Typeface.BOLD);
            valTv.setTextColor(textPrimary()); valTv.setGravity(Gravity.CENTER);
            col.addView(valTv);

            TextView lblTv = new TextView(this);
            lblTv.setText(labels[i]); lblTv.setTextSize(9);
            lblTv.setTypeface(null, Typeface.BOLD);
            lblTv.setTextColor(textSecondary()); lblTv.setGravity(Gravity.CENTER);
            lblTv.setLetterSpacing(0.05f);
            LinearLayout.LayoutParams lblLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lblLp.setMargins(0, dp(2), 0, 0); lblTv.setLayoutParams(lblLp);
            col.addView(lblTv);

            statsRow.addView(col);
            if (i < 2) {
                View gap = new View(this);
                gap.setLayoutParams(new LinearLayout.LayoutParams(dp(8), 0));
                statsRow.addView(gap);
            }
        }
    }

    private void buildHabitsRows(Calendar weekStart) {
        LinearLayout container = findViewById(R.id.habitsContainer);
        container.removeAllViews();
        Calendar today = Calendar.getInstance();

        for (Habit h : habits) {
            int color   = HABIT_COLORS[h.colorIdx % HABIT_COLORS.length];
            int bgColor = habitBgColor(h.colorIdx);
            int streak  = calcStreak(h);

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            cardLp.setMargins(0, 0, 0, dp(8));
            card.setLayoutParams(cardLp);
            card.setPadding(dp(14), dp(11), dp(14), dp(11));
            GradientDrawable cardBg = new GradientDrawable();
            cardBg.setShape(GradientDrawable.RECTANGLE);
            cardBg.setCornerRadius(dp(14));
            cardBg.setColor(cardColor());
            card.setBackground(cardBg);

            final Habit currentHabit = h;
            card.setOnLongClickListener(v -> { showDeleteHabitDialog(currentHabit); return true; });

            TextView nameTv = new TextView(this);
            nameTv.setText(h.name); nameTv.setTextSize(12);
            nameTv.setTypeface(null, Typeface.BOLD);
            nameTv.setTextColor(textPrimary());
            nameTv.setLayoutParams(new LinearLayout.LayoutParams(dp(88), LinearLayout.LayoutParams.WRAP_CONTENT));
            nameTv.setMaxLines(2);
            card.addView(nameTv);

            LinearLayout dotsRow = new LinearLayout(this);
            dotsRow.setOrientation(LinearLayout.HORIZONTAL);
            dotsRow.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            dotsRow.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            Calendar day = (Calendar) weekStart.clone();
            for (int di = 0; di < 7; di++) {
                boolean isFuture = day.after(today);
                boolean isDone   = !isFuture && isDone(h, day);
                boolean isToday  = isSameDay(day, today);

                View box = new View(this);
                LinearLayout.LayoutParams boxLp = new LinearLayout.LayoutParams(dp(18), dp(18));
                boxLp.setMarginEnd(di < 6 ? dp(3) : 0);
                box.setLayoutParams(boxLp);
                GradientDrawable boxBg = new GradientDrawable();
                boxBg.setShape(GradientDrawable.RECTANGLE);
                boxBg.setCornerRadius(dp(4));
                if (isToday && !isDone) {
                    boxBg.setColor(isDark() ? Color.parseColor("#2C2820") : Color.parseColor("#F2EFE9"));
                    boxBg.setStroke(dp(2), Color.parseColor("#C8420A"));
                } else if (isDone) {
                    boxBg.setColor(color);
                } else {
                    boxBg.setColor(bgEmpty());
                }
                box.setBackground(boxBg);

                final Calendar daySnap = (Calendar) day.clone();
                final boolean fut = isFuture;
                box.setOnClickListener(v -> {
                    if (!fut) { toggleDone(h, daySnap); renderWeekly(); }
                });
                dotsRow.addView(box);
                day.add(Calendar.DAY_OF_YEAR, 1);
            }
            card.addView(dotsRow);

            TextView streakTv = new TextView(this);
            streakTv.setText(streak > 0 ? "🔥" + streak : "—");
            streakTv.setTextSize(10);
            streakTv.setTypeface(null, Typeface.BOLD);
            streakTv.setTextColor(streak > 0 ? color : textSecondary());
            streakTv.setPadding(dp(8), dp(2), dp(8), dp(2));
            GradientDrawable sBg = new GradientDrawable();
            sBg.setShape(GradientDrawable.RECTANGLE);
            sBg.setCornerRadius(dp(50));
            sBg.setColor(streak > 0 ? bgColor : bgEmpty());
            streakTv.setBackground(sBg);
            LinearLayout.LayoutParams sLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            sLp.setMarginStart(dp(8));
            streakTv.setLayoutParams(sLp);
            card.addView(streakTv);

            TextView editBtn = new TextView(this);
            editBtn.setText("⋮"); editBtn.setTextSize(18);
            editBtn.setPadding(dp(12), dp(4), dp(8), dp(4));
            editBtn.setTextColor(textSecondary());
            editBtn.setGravity(Gravity.CENTER);
            card.addView(editBtn);

            final Habit hToEdit = h;
            editBtn.setOnClickListener(v -> showEditHabitDialog(hToEdit));
            container.addView(card);
        }
    }

    private void showDeleteHabitDialog(Habit h) {
        new AlertDialog.Builder(this)
                .setTitle("Alışkanlığı Sil")
                .setMessage("'" + h.name + "' alışkanlığını silmek istediğine emin misin?")
                .setPositiveButton("Sil", (dialog, which) -> {
                    habits.remove(h); saveHabits(); renderWeekly();
                    android.widget.Toast.makeText(this, "Silindi", android.widget.Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Vazgeç", null).show();
    }

    private void showEditHabitDialog(Habit habit) {
        int[] dialogColors = HABIT_COLORS;
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(20), dp(15), dp(20), dp(15));

        EditText etName = new EditText(this);
        etName.setText(habit.name);
        etName.setHint("Alışkanlık adı...");
        layout.addView(etName);

        TextView colorLbl = new TextView(this);
        colorLbl.setText("Rengi Güncelle:");
        colorLbl.setPadding(0, dp(15), 0, dp(8));
        layout.addView(colorLbl);

        LinearLayout colorRow = new LinearLayout(this);
        colorRow.setOrientation(LinearLayout.HORIZONTAL);
        final int[] selectedColorIdx = {habit.colorIdx};

        for (int ci = 0; ci < dialogColors.length; ci++) {
            final int idx = ci;
            View btn = new View(this);
            LinearLayout.LayoutParams bLp = new LinearLayout.LayoutParams(dp(30), dp(30));
            bLp.setMarginEnd(dp(10)); btn.setLayoutParams(bLp);
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.OVAL);
            gd.setColor(dialogColors[ci]);
            if (ci == habit.colorIdx) gd.setStroke(dp(3), Color.BLACK);
            btn.setBackground(gd);
            btn.setOnClickListener(v -> {
                selectedColorIdx[0] = idx;
                for (int j = 0; j < colorRow.getChildCount(); j++) {
                    GradientDrawable b = new GradientDrawable();
                    b.setShape(GradientDrawable.OVAL); b.setColor(dialogColors[j]);
                    if (j == idx) b.setStroke(dp(3), Color.BLACK);
                    colorRow.getChildAt(j).setBackground(b);
                }
            });
            colorRow.addView(btn);
        }
        layout.addView(colorRow);

        TextView btnDelete = new TextView(this);
        btnDelete.setText("Alışkanlığı sil");
        btnDelete.setTextColor(Color.RED);
        btnDelete.setGravity(Gravity.RIGHT);
        btnDelete.setPadding(0, dp(25), 0, 0);
        btnDelete.setTypeface(null, Typeface.BOLD);
        layout.addView(btnDelete);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Düzenle").setView(layout)
                .setPositiveButton("Güncelle", (d, w) -> {
                    String newName = etName.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        habit.name = newName;
                        habit.colorIdx = selectedColorIdx[0];
                        saveHabits(); renderWeekly();
                    }
                })
                .setNegativeButton("Vazgeç", null).show();

        btnDelete.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Emin misin?")
                        .setMessage("Bu alışkanlık kalıcı olarak silinecek.")
                        .setPositiveButton("Evet, sil", (d, w) -> {
                            habits.remove(habit); saveHabits(); renderWeekly(); dialog.dismiss();
                        })
                        .setNegativeButton("Hayır", null).show());
    }

    private boolean isDone(Habit h, Calendar day) {
        return h.doneDates.contains(dateKey(day));
    }

    private void toggleDone(Habit h, Calendar day) {
        String key = dateKey(day);
        if (h.doneDates.contains(key)) {
            h.doneDates.remove(key);
        } else {
            h.doneDates.add(key);
            AnchorManager.AnchorResult result = AnchorManager.onTaskCompleted(this);
            if (result.milestoneReached) {
                android.widget.Toast.makeText(this,
                        "🎉 +" + result.anchorsEarned + " ⚓  |  " + result.newStreak + " günlük seri!",
                        android.widget.Toast.LENGTH_LONG).show();
            }
        }
        saveHabits();
    }

    private String dateKey(Calendar cal) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
    }

    private boolean isSameDay(Calendar a, Calendar b) {
        return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
                a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR);
    }

    // ── AYLIK ─────────────────────────────────────────────────────────────
    private void renderMonthly() {
        TextView monthLabel = (TextView) findViewById(R.id.txtMonthLabel);
        monthLabel.setText(MONTH_NAMES[currentMonth] + " " + currentYear);
        monthLabel.setTextColor(textPrimary());

        LinearLayout tabsRow = findViewById(R.id.monthHabitTabs);
        tabsRow.removeAllViews();
        for (int i = 0; i < habits.size(); i++) {
            final int idx = i;
            Habit h = habits.get(i);
            int color = HABIT_COLORS[h.colorIdx % HABIT_COLORS.length];
            TextView tab = new TextView(this);
            tab.setText(h.name.split(" ")[0]);
            tab.setTextSize(11); tab.setTypeface(null, Typeface.BOLD);
            tab.setGravity(Gravity.CENTER);
            tab.setPadding(dp(8), dp(5), dp(8), dp(5));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            lp.setMarginEnd(dp(4)); tab.setLayoutParams(lp);
            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.RECTANGLE);
            bg.setCornerRadius(dp(50));
            bg.setColor(i == currentMonthHabitIdx ? color : bgEmpty());
            tab.setBackground(bg);
            tab.setTextColor(i == currentMonthHabitIdx ? Color.WHITE : textSecondary());
            tab.setOnClickListener(v -> { currentMonthHabitIdx = idx; renderMonthly(); });
            tabsRow.addView(tab);
        }

        LinearLayout dayHdr = findViewById(R.id.monthDayHeaders);
        dayHdr.removeAllViews();
        for (String dn : DAY_NAMES) {
            TextView tv = new TextView(this);
            tv.setText(dn); tv.setTextSize(9); tv.setTypeface(null, Typeface.BOLD);
            tv.setTextColor(textSecondary()); tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            dayHdr.addView(tv);
        }

        GridLayout grid = findViewById(R.id.monthGrid);
        grid.removeAllViews();
        int days = MONTH_DAYS[currentMonth];
        Calendar cal = Calendar.getInstance();
        cal.set(currentYear, currentMonth, 1);
        int firstDay = cal.get(Calendar.DAY_OF_WEEK);
        int offset   = (firstDay == Calendar.SUNDAY) ? 6 : firstDay - 2;
        Habit h      = habits.size() > currentMonthHabitIdx ? habits.get(currentMonthHabitIdx) : habits.get(0);
        int color    = HABIT_COLORS[h.colorIdx % HABIT_COLORS.length];
        Calendar today = Calendar.getInstance();

        for (int i = 0; i < offset; i++) {
            View e = new View(this);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0; lp.height = dp(28);
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            lp.setMargins(dp(2), dp(2), dp(2), dp(2));
            grid.addView(e, lp);
        }

        for (int d = 1; d <= days; d++) {
            cal.set(currentYear, currentMonth, d);
            boolean isFut   = cal.after(today);
            boolean isDone2 = !isFut && h.doneDates.contains(dateKey(cal));
            boolean isTod   = isSameDay(cal, today);
            TextView cell   = new TextView(this);
            cell.setTextSize(9); cell.setTypeface(null, Typeface.BOLD);
            cell.setGravity(Gravity.CENTER);
            cell.setText(isDone2 ? "✓" : String.valueOf(d));
            cell.setTextColor(isDone2 ? Color.WHITE : textSecondary());
            GradientDrawable cBg = new GradientDrawable();
            cBg.setShape(GradientDrawable.RECTANGLE);
            cBg.setCornerRadius(dp(6));
            cBg.setColor(isDone2 ? color : bgEmpty());
            if (isTod) cBg.setStroke(dp(2), isDark() ? Color.parseColor("#F5C842") : Color.parseColor("#1A1A1A"));
            cell.setBackground(cBg);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0; lp.height = dp(28);
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            lp.setMargins(dp(2), dp(2), dp(2), dp(2));
            grid.addView(cell, lp);
        }

        LinearLayout stats = findViewById(R.id.monthStats);
        String monthPrefix = currentYear + "-" + String.format("%02d", currentMonth + 1);
        int doneCnt = (int) h.doneDates.stream().filter(k -> k.startsWith(monthPrefix)).count();
        int rate2   = doneCnt * 100 / Math.max(1, days);
        int streak  = calcStreak(h);
        buildStatCols(stats,
                new String[]{String.valueOf(doneCnt), rate2 + "%", String.valueOf(streak)},
                new String[]{"Tamamlandı", "Başarı", "Güncel seri"});
    }

    // ── YILLIK ────────────────────────────────────────────────────────────
    private void renderYearly() {
        ((TextView) findViewById(R.id.txtYearLabel)).setText(String.valueOf(currentViewYear));
        ((TextView) findViewById(R.id.txtYearLabel)).setTextColor(textPrimary());

        GridLayout grid = findViewById(R.id.yearGrid);
        grid.removeAllViews();

        int[] levels = isDark() ? new int[]{
                Color.parseColor("#2C2820"),
                Color.parseColor("#3A2810"),
                Color.parseColor("#6B3820"),
                Color.parseColor("#A0522D")
        } : new int[]{
                Color.parseColor("#E2DDD6"),
                Color.parseColor("#E8C8A8"),
                Color.parseColor("#C8956C"),
                Color.parseColor("#A0522D")
        };

        Calendar today = Calendar.getInstance();

        for (int m = 0; m < 12; m++) {
            LinearLayout block = new LinearLayout(this);
            block.setOrientation(LinearLayout.VERTICAL);
            GridLayout.LayoutParams bLp = new GridLayout.LayoutParams();
            bLp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            bLp.width = 0; bLp.setMargins(dp(3), dp(3), dp(3), dp(3));
            block.setLayoutParams(bLp);

            boolean isCur  = m == today.get(Calendar.MONTH) && currentViewYear == today.get(Calendar.YEAR);
            TextView mLbl  = new TextView(this);
            mLbl.setText(MONTH_NAMES[m].substring(0, 3)); mLbl.setTextSize(9);
            mLbl.setTypeface(null, Typeface.BOLD);
            mLbl.setTextColor(isCur ? Color.parseColor("#C8956C") : textSecondary());
            mLbl.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams mlLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mlLp.setMargins(0, 0, 0, dp(3)); mLbl.setLayoutParams(mlLp);
            block.addView(mLbl);

            GridLayout mini = new GridLayout(this);
            mini.setColumnCount(7);
            mini.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            int days2 = MONTH_DAYS[m];
            Calendar cal = Calendar.getInstance();
            cal.set(currentViewYear, m, 1);
            int first = cal.get(Calendar.DAY_OF_WEEK);
            int off   = (first == Calendar.SUNDAY) ? 6 : first - 2;

            for (int i = 0; i < off; i++) {
                View e = new View(this);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.width = 0; lp.height = dp(8);
                lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                lp.setMargins(dp(1), dp(1), dp(1), dp(1));
                mini.addView(e, lp);
            }

            for (int d = 1; d <= days2; d++) {
                cal.set(currentViewYear, m, d);
                boolean isFut  = cal.after(today);
                boolean isTod  = isSameDay(cal, today);
                int totalDone = 0;
                for (Habit hh : habits) if (hh.doneDates.contains(dateKey(cal))) totalDone++;
                int level = isFut ? 0 : Math.min(3, totalDone);
                View cell = new View(this);
                GradientDrawable cBg = new GradientDrawable();
                cBg.setShape(GradientDrawable.RECTANGLE);
                cBg.setCornerRadius(dp(2));
                cBg.setColor(levels[level]);
                if (isTod) cBg.setStroke(dp(1), Color.parseColor("#C8956C"));
                cell.setBackground(cBg);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.width = 0; lp.height = dp(8);
                lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                lp.setMargins(dp(1), dp(1), dp(1), dp(1));
                mini.addView(cell, lp);
            }
            block.addView(mini);
            grid.addView(block);
        }

        buildYearStats();
        buildYearScale();
    }

    private void buildYearStats() {
        LinearLayout stats = findViewById(R.id.yearStats);
        stats.removeAllViews();
        int totalDone = habits.stream().mapToInt(h -> h.doneDates.size()).sum();
        int maxStreak = habits.stream().mapToInt(this::calcStreak).max().orElse(0);
        buildStatCols(stats,
                new String[]{String.valueOf(totalDone), String.valueOf(maxStreak), String.valueOf(habits.size())},
                new String[]{"Toplam gün", "En uzun seri", "Alışkanlık"});
    }

    private void buildYearScale() {
        LinearLayout scale = findViewById(R.id.yearScale);
        scale.removeAllViews();
        int[] scaleColors = isDark() ? new int[]{
                Color.parseColor("#2C2820"), Color.parseColor("#3A2810"),
                Color.parseColor("#6B3820"), Color.parseColor("#A0522D")
        } : new int[]{
                Color.parseColor("#E2DDD6"), Color.parseColor("#E8C8A8"),
                Color.parseColor("#C8956C"), Color.parseColor("#A0522D")
        };

        TextView az = new TextView(this); az.setText("Az"); az.setTextSize(10);
        az.setTextColor(textSecondary());
        LinearLayout.LayoutParams azLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        azLp.setMarginEnd(dp(6)); az.setLayoutParams(azLp);
        scale.addView(az);

        for (int sc : scaleColors) {
            View dot = new View(this);
            GradientDrawable sBg = new GradientDrawable();
            sBg.setShape(GradientDrawable.RECTANGLE); sBg.setCornerRadius(dp(2)); sBg.setColor(sc);
            dot.setBackground(sBg);
            LinearLayout.LayoutParams sLp = new LinearLayout.LayoutParams(dp(12), dp(12));
            sLp.setMarginEnd(dp(4)); dot.setLayoutParams(sLp);
            scale.addView(dot);
        }

        TextView cok = new TextView(this); cok.setText("Çok"); cok.setTextSize(10);
        cok.setTextColor(textSecondary());
        LinearLayout.LayoutParams cLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cLp.setMarginStart(dp(2)); cok.setLayoutParams(cLp);
        scale.addView(cok);
    }

    private void buildStatCols(LinearLayout container, String[] vals, String[] lbls) {
        container.removeAllViews();
        for (int i = 0; i < vals.length; i++) {
            LinearLayout col = new LinearLayout(this);
            col.setOrientation(LinearLayout.VERTICAL); col.setGravity(Gravity.CENTER);
            col.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            TextView vTv = new TextView(this);
            vTv.setText(vals[i]); vTv.setTextSize(17);
            vTv.setTypeface(null, Typeface.BOLD);
            vTv.setTextColor(textPrimary()); vTv.setGravity(Gravity.CENTER);
            col.addView(vTv);
            TextView lTv = new TextView(this);
            lTv.setText(lbls[i]); lTv.setTextSize(10);
            lTv.setTextColor(textSecondary()); lTv.setGravity(Gravity.CENTER);
            col.addView(lTv);
            container.addView(col);
            if (i < vals.length - 1) {
                View div = new View(this);
                div.setBackgroundColor(isDark() ? Color.parseColor("#30FFFFFF") : Color.parseColor("#EAE0CE"));
                container.addView(div, new LinearLayout.LayoutParams(dp(1),
                        LinearLayout.LayoutParams.MATCH_PARENT));
            }
        }
    }

    private void showAddHabitDialog() {
        int[] dialogColors = HABIT_COLORS;
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(16), dp(8), dp(16), dp(8));

        EditText etName = new EditText(this);
        etName.setHint("Alışkanlık adı..."); etName.setMaxLines(1);
        etName.setPadding(dp(8), dp(8), dp(8), dp(8));
        layout.addView(etName);

        TextView colorLbl = new TextView(this);
        colorLbl.setText("Renk seç:"); colorLbl.setTextSize(12);
        colorLbl.setTextColor(textSecondary());
        LinearLayout.LayoutParams clLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        clLp.setMargins(0, dp(12), 0, dp(6)); colorLbl.setLayoutParams(clLp);
        layout.addView(colorLbl);

        LinearLayout colorRow = new LinearLayout(this);
        colorRow.setOrientation(LinearLayout.HORIZONTAL);
        final int[] selectedColorIdx = {0};

        for (int ci = 0; ci < dialogColors.length; ci++) {
            final int idx = ci;
            View btn = new View(this);
            LinearLayout.LayoutParams bLp = new LinearLayout.LayoutParams(dp(28), dp(28));
            bLp.setMarginEnd(dp(10)); btn.setLayoutParams(bLp);
            GradientDrawable cbBg = new GradientDrawable();
            cbBg.setShape(GradientDrawable.OVAL); cbBg.setColor(dialogColors[ci]);
            btn.setBackground(cbBg);
            btn.setOnClickListener(v -> {
                selectedColorIdx[0] = idx;
                for (int j = 0; j < colorRow.getChildCount(); j++) {
                    GradientDrawable b = new GradientDrawable();
                    b.setShape(GradientDrawable.OVAL); b.setColor(dialogColors[j]);
                    colorRow.getChildAt(j).setBackground(b);
                }
                GradientDrawable selBg = new GradientDrawable();
                selBg.setShape(GradientDrawable.OVAL); selBg.setColor(dialogColors[idx]);
                selBg.setStroke(dp(3), Color.parseColor("#1A1A1A"));
                btn.setBackground(selBg);
            });
            colorRow.addView(btn);
        }
        layout.addView(colorRow);

        new AlertDialog.Builder(this)
                .setTitle("Alışkanlık Ekle").setView(layout)
                .setPositiveButton("Ekle", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    if (!name.isEmpty()) {
                        habits.add(new Habit(name, selectedColorIdx[0]));
                        saveHabits(); renderWeekly();
                    }
                })
                .setNegativeButton("İptal", null).show();
    }

    private void setupNav() {
        findViewById(R.id.btnNavHome).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); finish();
        });
        findViewById(R.id.btnNavTracking).setOnClickListener(v -> {});
        findViewById(R.id.btnNavWorlds).setOnClickListener(v -> {
            startActivity(new Intent(this, WorldsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); finish();
        });
        findViewById(R.id.btnNavChat).setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        findViewById(R.id.btnNavSettings).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); finish();
        });
    }

    private int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }
}