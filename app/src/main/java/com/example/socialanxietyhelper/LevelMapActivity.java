package com.example.socialanxietyhelper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LevelMapActivity extends AppCompatActivity {

    private static final String PREFS_NAME  = "CesaretPrefs";
    private static final String KEY_NAME    = "playerName";
    private static final String KEY_LEVEL   = "playerLevel";
    private static final String KEY_XP      = "playerXP";
    private static final String KEY_XP_MAX  = "playerXPMax";

    private static final String[] WORLD_COLORS = {
            "#C8420A","#8B1A4A","#1B4332","#1A3A5C",
            "#4A1A6B","#2C4A1A","#5A0A2A","#1A1A1A"
    };

    private static final float SWIPE_THRESHOLD = 120f;
    private static final float SWIPE_VELOCITY  = 400f;
    private static final float TILT_MAX_DEG    = 15f;
    private static final float DISMISS_X       = 1400f;
    private static final float DISMISS_Y       = -100f;

    static class Task {
        String title, desc, color;
        int zorluk;
        boolean done;
        Task(String title, String desc, int zorluk, String color) {
            this.title = title; this.desc = desc;
            this.zorluk = zorluk; this.color = color;
        }
    }

    // Views
    private CardView     cardTop, cardBg1, cardBg2;
    private TextView     txtTaskTitle, txtTaskDesc, txtRepeatBadge, txtTaskNo;
    private TextView     txtCounter, txtWorldTitle;
    private TextView     txtPlayerName, txtLevelBadge, txtSeri, txtXPInfo;
    private TextView     btnDone, btnSkip, btnBack;
    private LinearLayout pipRow, statusRow;
    private ProgressBar  progressXP;
    private TextView     txtSwipeHintRight, txtSwipeHintLeft;

    // Floating hint button
    private View    hintBtnOuter;
    private boolean hintBubbleVisible = false;
    private View    hintBubbleView    = null;

    private final List<Task>   tasks      = new ArrayList<>();
    private int                curIndex   = 0;
    private int                worldNumber, categoryIdx, worldAnchors;
    private String             worldTopic;

    private final Set<Integer> doneSet    = new HashSet<>();
    private final Set<Integer> skippedSet = new HashSet<>();

    private boolean browseMode = false;

    // Swipe state
    private float   touchStartX, touchStartY, touchStartTime;
    private boolean isSwiping = false;
    private boolean animating = false;

    // ── Oluşturma ─────────────────────────────────────────────────────────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_map);

        worldNumber  = getIntent().getIntExtra("worldNumber",  1);
        categoryIdx  = getIntent().getIntExtra("categoryIdx",  0);
        worldAnchors = getIntent().getIntExtra("worldAnchors", 5);
        worldTopic   = getIntent().getStringExtra("worldTopic");
        if (worldTopic == null) worldTopic = "";

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        bindViews();
        loadPlayerInfo(prefs);
        loadDoneSet(prefs);
        buildTasks();

        browseMode = (doneSet.size() >= tasks.size() && !tasks.isEmpty());

        setupButtonListeners();
        setupSwipe();
        setupNav();
        setupFloatingHintButton();

        curIndex = browseMode ? 0 : firstUndoneIndex();
        renderCard(false);

        if (txtWorldTitle != null)
            txtWorldTitle.setText("BÖLÜM " + worldNumber
                    + (worldTopic.isEmpty() ? "" : " — " + worldTopic.toUpperCase()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlayerInfo(getSharedPreferences(PREFS_NAME, MODE_PRIVATE));
    }

    // ══════════════════════════════════════════════════════════════════════
    //  FLOATING HINT BUTTON
    // ══════════════════════════════════════════════════════════════════════

    private void setupFloatingHintButton() {
        FrameLayout rootFrame = findViewById(android.R.id.content);

        // ── Dış container (pulse halkalar + buton) ────────────────────
        FrameLayout outerWrap = new FrameLayout(this);
        int outerSize = dp(72);
        FrameLayout.LayoutParams outerLp = new FrameLayout.LayoutParams(outerSize, outerSize);
        outerLp.gravity    = android.view.Gravity.BOTTOM | android.view.Gravity.END;
        outerLp.bottomMargin = dp(104);
        outerLp.rightMargin  = dp(20);
        outerWrap.setLayoutParams(outerLp);
        hintBtnOuter = outerWrap;

        // ── Pulse halkası 1 ────────────────────────────────────────────
        View ring1 = new View(this);
        FrameLayout.LayoutParams ringLp1 = new FrameLayout.LayoutParams(outerSize, outerSize);
        ringLp1.gravity = android.view.Gravity.CENTER;
        ring1.setLayoutParams(ringLp1);
        GradientDrawable ringBg1 = new GradientDrawable();
        ringBg1.setShape(GradientDrawable.OVAL);
        ringBg1.setColor(Color.argb(55, 245, 200, 66));
        ring1.setBackground(ringBg1);
        outerWrap.addView(ring1);

        // ── Pulse halkası 2 ────────────────────────────────────────────
        View ring2 = new View(this);
        FrameLayout.LayoutParams ringLp2 = new FrameLayout.LayoutParams(outerSize, outerSize);
        ringLp2.gravity = android.view.Gravity.CENTER;
        ring2.setLayoutParams(ringLp2);
        GradientDrawable ringBg2 = new GradientDrawable();
        ringBg2.setShape(GradientDrawable.OVAL);
        ringBg2.setColor(Color.argb(28, 245, 200, 66));
        ring2.setBackground(ringBg2);
        outerWrap.addView(ring2);

        // ── Ana buton dairesi ──────────────────────────────────────────
        TextView hintBtn = new TextView(this);
        int btnSize = dp(56);
        FrameLayout.LayoutParams btnLp = new FrameLayout.LayoutParams(btnSize, btnSize);
        btnLp.gravity = android.view.Gravity.CENTER;
        hintBtn.setLayoutParams(btnLp);
        hintBtn.setText("🍩");
        hintBtn.setTextSize(24);
        hintBtn.setGravity(android.view.Gravity.CENTER);
        GradientDrawable btnBg = new GradientDrawable();
        btnBg.setShape(GradientDrawable.OVAL);
        btnBg.setColor(Color.parseColor("#2D2A1E"));
        btnBg.setStroke(dp(2), Color.parseColor("#F5C842"));
        hintBtn.setBackground(btnBg);
        hintBtn.setElevation(dp(8));
        outerWrap.addView(hintBtn);

        // ── Canlı nokta (badge) ────────────────────────────────────────
        View dot = new View(this);
        int dotSize = dp(13);
        FrameLayout.LayoutParams dotLp = new FrameLayout.LayoutParams(dotSize, dotSize);
        dotLp.gravity     = android.view.Gravity.TOP | android.view.Gravity.END;
        dotLp.topMargin   = dp(6);
        dotLp.rightMargin = dp(6);
        dot.setLayoutParams(dotLp);
        GradientDrawable dotBg = new GradientDrawable();
        dotBg.setShape(GradientDrawable.OVAL);
        dotBg.setColor(Color.parseColor("#F5C842"));
        dotBg.setStroke(dp(2), Color.parseColor("#1A1A1A"));
        dot.setBackground(dotBg);
        outerWrap.addView(dot);

        rootFrame.addView(outerWrap);

        // ── Animasyonlar ──────────────────────────────────────────────
        startPulseLoop(ring1, 0);
        startPulseLoop(ring2, 900);
        startDotBlink(dot);

        // ── Tıklama ────────────────────────────────────────────────────
        hintBtn.setOnClickListener(v -> {
            if (hintBubbleVisible) {
                dismissHintBubble();
            } else {
                fetchHintForCurrentTask();
            }
        });

        // Basılı tutunca küçük scale efekti
        hintBtn.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.animate().scaleX(0.90f).scaleY(0.90f).setDuration(100).start();
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.animate().scaleX(1f).scaleY(1f).setDuration(150)
                        .setInterpolator(new OvershootInterpolator(2f)).start();
                v.performClick();
            }
            return true;
        });
    }

    private void startPulseLoop(View ring, long delayMs) {
        Runnable loop = new Runnable() {
            @Override public void run() {
                ring.setScaleX(1f);
                ring.setScaleY(1f);
                ring.setAlpha(0.85f);
                ring.animate()
                        .scaleX(1.80f).scaleY(1.80f).alpha(0f)
                        .setDuration(1900)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .withEndAction(() -> ring.postDelayed(this, 500))
                        .start();
            }
        };
        ring.postDelayed(loop, delayMs);
    }

    private void startDotBlink(View dot) {
        Runnable blink = new Runnable() {
            @Override public void run() {
                dot.animate().alpha(0.35f).setDuration(900).withEndAction(() ->
                        dot.animate().alpha(1f).setDuration(900)
                                .withEndAction(() -> dot.postDelayed(this, 200))
                                .start()
                ).start();
            }
        };
        dot.post(blink);
    }

    // ── Inline baloncuk göster ────────────────────────────────────────────
    private void showInlineBubble(String taskTitle, String hint) {
        dismissHintBubble();

        FrameLayout rootFrame = findViewById(android.R.id.content);

        // ── Kart ──────────────────────────────────────────────────────
        LinearLayout bubble = new LinearLayout(this);
        bubble.setOrientation(LinearLayout.VERTICAL);
        bubble.setPadding(dp(16), dp(14), dp(16), dp(14));

        GradientDrawable bubbleBg = new GradientDrawable();
        bubbleBg.setShape(GradientDrawable.RECTANGLE);
        bubbleBg.setColor(Color.parseColor("#252218"));
        bubbleBg.setCornerRadii(new float[]{
                dp(16), dp(16),
                dp(16), dp(16),
                dp(16), dp(16),
                dp(4),  dp(4)
        });
        bubbleBg.setStroke(dp(1), Color.parseColor("#4D3F10"));
        bubble.setBackground(bubbleBg);
        bubble.setElevation(dp(14));

        FrameLayout.LayoutParams blp = new FrameLayout.LayoutParams(
                dp(284), FrameLayout.LayoutParams.WRAP_CONTENT);
        blp.gravity      = android.view.Gravity.BOTTOM | android.view.Gravity.END;
        blp.bottomMargin = dp(190);
        blp.rightMargin  = dp(16);
        bubble.setLayoutParams(blp);

        // ── Header ────────────────────────────────────────────────────
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams hLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        hLp.setMargins(0, 0, 0, dp(10));
        header.setLayoutParams(hLp);

        // Avatar
        TextView avatarTv = new TextView(this);
        avatarTv.setText("🍩");
        avatarTv.setTextSize(18);
        avatarTv.setGravity(android.view.Gravity.CENTER);
        LinearLayout.LayoutParams avLp = new LinearLayout.LayoutParams(dp(40), dp(40));
        avLp.setMarginEnd(dp(10));
        avatarTv.setLayoutParams(avLp);
        GradientDrawable avBg = new GradientDrawable();
        avBg.setShape(GradientDrawable.OVAL);
        avBg.setColor(Color.parseColor("#F5C842"));
        avBg.setStroke(dp(2), Color.parseColor("#3A2E00"));
        avatarTv.setBackground(avBg);

        // İsim kolonu
        LinearLayout nameCol = new LinearLayout(this);
        nameCol.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams nameColLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        nameCol.setLayoutParams(nameColLp);

        TextView nameTv = new TextView(this);
        nameTv.setText("Rehber Simit");
        nameTv.setTextSize(13);
        nameTv.setTypeface(null, android.graphics.Typeface.BOLD);
        nameTv.setTextColor(Color.parseColor("#F5C842"));

        TextView subTv = new TextView(this);
        subTv.setText("Göreve özel ipucu 💡");
        subTv.setTextSize(10);
        subTv.setTextColor(Color.parseColor("#887755"));

        nameCol.addView(nameTv);
        nameCol.addView(subTv);

        // Kapat butonu
        TextView closeBtn = new TextView(this);
        closeBtn.setText("✕");
        closeBtn.setTextSize(14);
        closeBtn.setTextColor(Color.parseColor("#665544"));
        closeBtn.setTypeface(null, android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams closeLp = new LinearLayout.LayoutParams(dp(28), dp(28));
        closeLp.gravity = android.view.Gravity.CENTER_VERTICAL;
        closeBtn.setLayoutParams(closeLp);
        closeBtn.setGravity(android.view.Gravity.CENTER);
        closeBtn.setOnClickListener(x -> dismissHintBubble());

        header.addView(avatarTv);
        header.addView(nameCol);
        header.addView(closeBtn);

        // ── Görev chip ────────────────────────────────────────────────
        TextView chipTv = new TextView(this);
        chipTv.setText(taskTitle);
        chipTv.setTextSize(10);
        chipTv.setTypeface(null, android.graphics.Typeface.BOLD);
        chipTv.setTextColor(Color.parseColor("#F5C842"));
        chipTv.setPadding(dp(8), dp(4), dp(8), dp(4));
        GradientDrawable chipBg = new GradientDrawable();
        chipBg.setShape(GradientDrawable.RECTANGLE);
        chipBg.setCornerRadius(dp(6));
        chipBg.setColor(Color.parseColor("#1A1714"));
        chipBg.setStroke(dp(1), Color.parseColor("#3A2E00"));
        chipTv.setBackground(chipBg);
        LinearLayout.LayoutParams chipLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chipLp.setMargins(0, 0, 0, dp(10));
        chipTv.setLayoutParams(chipLp);

        // ── İpucu metni ───────────────────────────────────────────────
        TextView hintTv = new TextView(this);
        hintTv.setText(hint);
        hintTv.setTextSize(12);
        hintTv.setTextColor(Color.parseColor("#D4C89A"));
        hintTv.setLineSpacing(0, 1.6f);
        LinearLayout.LayoutParams htLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        htLp.setMargins(0, 0, 0, dp(14));
        hintTv.setLayoutParams(htLp);

        // ── Ayırıcı çizgi ─────────────────────────────────────────────
        View divider = new View(this);
        LinearLayout.LayoutParams divLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(1));
        divLp.setMargins(0, 0, 0, dp(12));
        divider.setLayoutParams(divLp);
        divider.setBackgroundColor(Color.parseColor("#2E2820"));

        // ── Aksiyon butonları ─────────────────────────────────────────
        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setWeightSum(2f);
        actions.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // Simit ile Konuş
        TextView btnChat = new TextView(this);
        btnChat.setText("Simit ile Konuş →");
        btnChat.setTextSize(11);
        btnChat.setTypeface(null, android.graphics.Typeface.BOLD);
        btnChat.setTextColor(Color.parseColor("#F5C842"));
        btnChat.setGravity(android.view.Gravity.CENTER);
        btnChat.setPadding(dp(8), dp(9), dp(8), dp(9));
        GradientDrawable chatBg = new GradientDrawable();
        chatBg.setShape(GradientDrawable.RECTANGLE);
        chatBg.setCornerRadius(dp(9));
        chatBg.setColor(Color.parseColor("#2A2000"));
        chatBg.setStroke(dp(1), Color.parseColor("#F5C842"));
        btnChat.setBackground(chatBg);
        LinearLayout.LayoutParams chatLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        chatLp.setMarginEnd(dp(7));
        btnChat.setLayoutParams(chatLp);
        btnChat.setOnClickListener(x -> {
            dismissHintBubble();
            startActivity(new Intent(this, ChatActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Anladım
        TextView btnOk = new TextView(this);
        btnOk.setText("Anladım 👍");
        btnOk.setTextSize(11);
        btnOk.setTextColor(Color.parseColor("#887755"));
        btnOk.setGravity(android.view.Gravity.CENTER);
        btnOk.setPadding(dp(8), dp(9), dp(8), dp(9));
        GradientDrawable okBg = new GradientDrawable();
        okBg.setShape(GradientDrawable.RECTANGLE);
        okBg.setCornerRadius(dp(9));
        okBg.setColor(Color.parseColor("#1E1C14"));
        okBg.setStroke(dp(1), Color.parseColor("#3A3020"));
        btnOk.setBackground(okBg);
        btnOk.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        btnOk.setOnClickListener(x -> dismissHintBubble());

        actions.addView(btnChat);
        actions.addView(btnOk);

        bubble.addView(header);
        bubble.addView(chipTv);
        bubble.addView(hintTv);
        bubble.addView(divider);
        bubble.addView(actions);

        // ── Animasyonlu giriş ─────────────────────────────────────────
        bubble.setAlpha(0f);
        bubble.setTranslationY(dp(32));
        bubble.setScaleX(0.86f);
        bubble.setScaleY(0.86f);
        rootFrame.addView(bubble);

        bubble.animate()
                .alpha(1f)
                .translationY(0f)
                .scaleX(1f).scaleY(1f)
                .setDuration(340)
                .setInterpolator(new OvershootInterpolator(1.15f))
                .start();

        hintBubbleView    = bubble;
        hintBubbleVisible = true;
    }

    private void dismissHintBubble() {
        if (hintBubbleView == null) return;
        View b = hintBubbleView;
        hintBubbleView    = null;
        hintBubbleVisible = false;
        b.animate()
                .alpha(0f)
                .translationY(dp(22))
                .scaleX(0.88f).scaleY(0.88f)
                .setDuration(210)
                .setInterpolator(new AccelerateInterpolator(1.4f))
                .withEndAction(() -> {
                    FrameLayout root = findViewById(android.R.id.content);
                    if (root != null) root.removeView(b);
                })
                .start();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HINT API
    // ══════════════════════════════════════════════════════════════════════

    private void fetchHintForCurrentTask() {
        if (tasks.isEmpty() || curIndex >= tasks.size()) return;
        Task t = tasks.get(curIndex);

        AlertDialog loadingDialog = new AlertDialog.Builder(this)
                .setTitle("💡 İpucu Hazırlanıyor...")
                .setMessage("Rehber Simit düşünüyor...")
                .setCancelable(false)
                .create();
        loadingDialog.show();

        android.net.ConnectivityManager cm =
                (android.net.ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected()) {
            loadingDialog.dismiss();
            new AlertDialog.Builder(this)
                    .setTitle("📡 Bağlantı Yok")
                    .setMessage("İpucu almak için internet bağlantısı gerekiyor.")
                    .setPositiveButton("Tamam", null)
                    .show();
            return;
        }

        java.util.concurrent.ExecutorService exec =
                java.util.concurrent.Executors.newSingleThreadExecutor();
        android.os.Handler handler =
                new android.os.Handler(android.os.Looper.getMainLooper());

        exec.execute(() -> {
            try {
                String prompt = "Sosyal anksiyete için şu göreve özel, kısa ve pratik bir ipucu ver:\n"
                        + "Görev: " + t.title + "\n"
                        + "Açıklama: " + t.desc + "\n"
                        + "2-3 cümle, Türkçe, cesaretlendirici ve somut ol. Emoji kullanabilirsin.";

                org.json.JSONArray messages = new org.json.JSONArray();
                org.json.JSONObject systemMsg = new org.json.JSONObject();
                systemMsg.put("role", "system");
                systemMsg.put("content",
                        "Sen 'Rehber Simit' adında bir sosyal anksiyete uzmanısın. "
                                + "Kısa, pratik ve cesaretlendirici ipuçları veriyorsun.");
                messages.put(systemMsg);

                org.json.JSONObject userMsg = new org.json.JSONObject();
                userMsg.put("role", "user");
                userMsg.put("content", prompt);
                messages.put(userMsg);

                org.json.JSONObject body = new org.json.JSONObject();
                body.put("model", "llama-3.3-70b-versatile");
                body.put("messages", messages);
                body.put("max_tokens", 200);
                body.put("temperature", 0.7);

                byte[] bodyBytes = body.toString()
                        .getBytes(java.nio.charset.StandardCharsets.UTF_8);

                java.net.URL url = new java.net.URL(
                        "https://api.groq.com/openai/v1/chat/completions");
                java.net.HttpURLConnection conn =
                        (java.net.HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(30000);
                conn.setRequestProperty("Content-Type",
                        "application/json; charset=utf-8");
                conn.setRequestProperty("Authorization",
                        "");
                conn.setRequestProperty("Content-Length",
                        String.valueOf(bodyBytes.length));
                conn.connect();

                java.io.OutputStream os = conn.getOutputStream();
                os.write(bodyBytes); os.flush(); os.close();

                int code = conn.getResponseCode();
                java.io.BufferedReader br = code == 200
                        ? new java.io.BufferedReader(new java.io.InputStreamReader(
                        conn.getInputStream(),
                        java.nio.charset.StandardCharsets.UTF_8))
                        : new java.io.BufferedReader(new java.io.InputStreamReader(
                        conn.getErrorStream(),
                        java.nio.charset.StandardCharsets.UTF_8));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                String hint;
                if (code == 200) {
                    org.json.JSONObject response =
                            new org.json.JSONObject(sb.toString());
                    hint = response.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");
                } else {
                    hint = "Şu an ipucu alınamadı. Lütfen tekrar dene.";
                }

                final String finalHint = hint;
                handler.post(() -> {
                    loadingDialog.dismiss();
                    showInlineBubble(t.title, finalHint);
                });

            } catch (Exception e) {
                handler.post(() -> {
                    loadingDialog.dismiss();
                    showInlineBubble("Hata",
                            "İpucu alınamadı: " + e.getMessage());
                });
            }
        });
    }

    // ══════════════════════════════════════════════════════════════════════
    //  VIEW BAĞLAMA
    // ══════════════════════════════════════════════════════════════════════

    private void bindViews() {
        cardTop           = findViewById(R.id.cardTop);
        cardBg1           = findViewById(R.id.cardBg1);
        cardBg2           = findViewById(R.id.cardBg2);
        txtTaskTitle      = findViewById(R.id.txtTaskTitle);
        txtTaskDesc       = findViewById(R.id.txtTaskDesc);
        txtRepeatBadge    = findViewById(R.id.txtRepeatBadge);
        txtTaskNo         = findViewById(R.id.txtTaskNo);
        txtCounter        = findViewById(R.id.txtCounter);
        txtWorldTitle     = findViewById(R.id.txtWorldTitle);
        txtPlayerName     = findViewById(R.id.txtPlayerName);
        txtLevelBadge     = findViewById(R.id.txtLevelBadge);
        txtSeri           = findViewById(R.id.txtSeri);
        txtXPInfo         = findViewById(R.id.txtXPInfo);
        progressXP        = findViewById(R.id.progressXP);
        btnDone           = findViewById(R.id.btnDone);
        btnSkip           = findViewById(R.id.btnSkip);
        btnBack           = findViewById(R.id.btnBack);
        pipRow            = findViewById(R.id.pipRow);
        statusRow         = findViewById(R.id.statusRow);
        txtSwipeHintRight = findViewById(R.id.txtSwipeHintRight);
        txtSwipeHintLeft  = findViewById(R.id.txtSwipeHintLeft);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  OYUNCU BİLGİSİ
    // ══════════════════════════════════════════════════════════════════════

    private void loadPlayerInfo(SharedPreferences prefs) {
        int level = prefs.getInt(KEY_LEVEL,  1);
        int xp    = prefs.getInt(KEY_XP,     0);
        int xpMax = prefs.getInt(KEY_XP_MAX, 100);
        int seri  = AnchorManager.getStreak(this);
        if (txtPlayerName != null) txtPlayerName.setText(prefs.getString(KEY_NAME, "Kahraman"));
        if (txtLevelBadge != null) txtLevelBadge.setText("Seviye " + level);
        if (txtSeri       != null) txtSeri.setText("🔥 " + seri);
        if (txtXPInfo     != null) txtXPInfo.setText(xp + " / " + xpMax + " XP");
        if (progressXP    != null) { progressXP.setMax(xpMax); progressXP.setProgress(xp); }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  KAYIT
    // ══════════════════════════════════════════════════════════════════════

    private void loadDoneSet(SharedPreferences prefs) {
        doneSet.clear();
        for (String s : prefs.getStringSet(doneSetKey(), new HashSet<>())) {
            try { doneSet.add(Integer.parseInt(s)); }
            catch (NumberFormatException ignored) {}
        }
    }

    private void saveDoneSet() {
        Set<String> out = new HashSet<>();
        for (int i : doneSet) out.add(String.valueOf(i));
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                .putStringSet(doneSetKey(), out)
                .putInt(countKey(), doneSet.size())
                .apply();
    }

    private String doneSetKey() { return "doneGorevs_"      + categoryIdx + "_" + worldNumber; }
    private String countKey()   { return "completedGorevs_" + categoryIdx + "_" + worldNumber; }

    // ══════════════════════════════════════════════════════════════════════
    //  GÖREV LİSTESİ
    // ══════════════════════════════════════════════════════════════════════

    private void buildTasks() {
        tasks.clear();
        String color = WORLD_COLORS[Math.min(worldNumber - 1, WORLD_COLORS.length - 1)];
        for (Gorev g : GorevDeposu.getGorevler(categoryIdx, worldNumber))
            tasks.add(new Task(g.getBaslik(), g.getAciklama(), g.getZorluk(), color));
    }

    // ══════════════════════════════════════════════════════════════════════
    //  BUTON DİNLEYİCİLER
    // ══════════════════════════════════════════════════════════════════════

    private void setupButtonListeners() {
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        if (btnDone != null) btnDone.setOnClickListener(v -> {
            if (!animating && !tasks.isEmpty()) swipeDismiss(true);
        });

        if (btnSkip != null) btnSkip.setOnClickListener(v -> {
            if (!animating && !tasks.isEmpty()) swipeDismiss(false);
        });
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SWIPE
    // ══════════════════════════════════════════════════════════════════════

    private void setupSwipe() {
        if (cardTop == null) return;
        cardTop.setOnTouchListener((v, event) -> {
            if (animating) return true;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStartX    = event.getRawX();
                    touchStartY    = event.getRawY();
                    touchStartTime = System.currentTimeMillis();
                    isSwiping      = false;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    float dx = event.getRawX() - touchStartX;
                    float dy = event.getRawY() - touchStartY;
                    if (!isSwiping && Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > 10)
                        isSwiping = true;
                    if (!isSwiping) return true;

                    cardTop.setTranslationX(dx);
                    cardTop.setTranslationY(dy * 0.25f);
                    cardTop.setRotation((dx / getScreenWidth()) * TILT_MAX_DEG);

                    float progress = Math.min(Math.abs(dx) / SWIPE_THRESHOLD, 1f);
                    if (txtSwipeHintRight != null)
                        txtSwipeHintRight.setAlpha(dx > 0 ? progress : 0f);
                    if (txtSwipeHintLeft != null)
                        txtSwipeHintLeft.setAlpha(dx < 0 ? progress : 0f);

                    float bgScale = 0.92f + 0.08f * progress;
                    if (cardBg1 != null) { cardBg1.setScaleX(bgScale); cardBg1.setScaleY(bgScale); }
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (!isSwiping) return true;
                    float finalDx  = event.getRawX() - touchStartX;
                    float elapsed  = System.currentTimeMillis() - touchStartTime;
                    float velocity = Math.abs(finalDx) / Math.max(elapsed / 1000f, 0.01f);

                    boolean goRight = finalDx >  SWIPE_THRESHOLD || (finalDx >  50 && velocity > SWIPE_VELOCITY);
                    boolean goLeft  = finalDx < -SWIPE_THRESHOLD || (finalDx < -50 && velocity > SWIPE_VELOCITY);

                    if      (goRight) swipeDismiss(true);
                    else if (goLeft)  swipeDismiss(false);
                    else              snapBack();
                    return true;
            }
            return false;
        });
    }

    private void swipeDismiss(boolean complete) {
        if (animating || cardTop == null) return;
        animating = true;

        float targetX   = complete ? DISMISS_X : -DISMISS_X;
        float targetRot = complete ? TILT_MAX_DEG * 2 : -TILT_MAX_DEG * 2;

        if (txtSwipeHintRight != null) txtSwipeHintRight.setAlpha(complete ? 1f : 0f);
        if (txtSwipeHintLeft  != null) txtSwipeHintLeft.setAlpha(complete  ? 0f : 1f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(cardTop, "translationX", cardTop.getTranslationX(), targetX),
                ObjectAnimator.ofFloat(cardTop, "translationY", cardTop.getTranslationY(), DISMISS_Y),
                ObjectAnimator.ofFloat(cardTop, "rotation",     cardTop.getRotation(),     targetRot),
                ObjectAnimator.ofFloat(cardTop, "alpha",        1f, 0f)
        );
        set.setDuration(420);
        set.setInterpolator(new DecelerateInterpolator(1.2f));
        set.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator a) {
                hideHintsNow();
                if (complete) onSwipeRight();
                else          onSwipeLeft();
            }
        });
        set.start();
    }

    private void snapBack() {
        if (cardTop == null) return;
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(cardTop, "translationX", cardTop.getTranslationX(), 0f),
                ObjectAnimator.ofFloat(cardTop, "translationY", cardTop.getTranslationY(), 0f),
                ObjectAnimator.ofFloat(cardTop, "rotation",     cardTop.getRotation(),     0f),
                ObjectAnimator.ofFloat(cardTop, "alpha",        cardTop.getAlpha(),         1f)
        );
        if (cardBg1 != null) set.playTogether(
                ObjectAnimator.ofFloat(cardBg1, "scaleX", cardBg1.getScaleX(), 0.92f),
                ObjectAnimator.ofFloat(cardBg1, "scaleY", cardBg1.getScaleY(), 0.92f)
        );
        set.setDuration(280);
        set.setInterpolator(new OvershootInterpolator(1.0f));
        set.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator a) {
                if (txtSwipeHintLeft != null) txtSwipeHintLeft.setAlpha(0f);
            }
        });
        set.start();
    }

    private void hideHintsNow() {
        if (txtSwipeHintRight != null) txtSwipeHintRight.setAlpha(0f);
        if (txtSwipeHintLeft  != null) txtSwipeHintLeft.setAlpha(0f);
    }

    private void animateNextCardIn() {
        if (cardTop == null) { animating = false; return; }
        hideHintsNow();

        cardTop.setTranslationX(500f);
        cardTop.setTranslationY(40f);
        cardTop.setRotation(6f);
        cardTop.setAlpha(0f);
        cardTop.setScaleX(0.92f);
        cardTop.setScaleY(0.92f);
        if (cardBg1 != null) { cardBg1.setScaleX(0.88f); cardBg1.setScaleY(0.88f); }
        if (cardBg2 != null) { cardBg2.setScaleX(0.84f); cardBg2.setScaleY(0.84f); }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(cardTop, "translationX", 500f,  0f),
                ObjectAnimator.ofFloat(cardTop, "translationY", 40f,   0f),
                ObjectAnimator.ofFloat(cardTop, "rotation",     6f,    0f),
                ObjectAnimator.ofFloat(cardTop, "alpha",        0f,    1f),
                ObjectAnimator.ofFloat(cardTop, "scaleX",       0.92f, 1f),
                ObjectAnimator.ofFloat(cardTop, "scaleY",       0.92f, 1f)
        );
        if (cardBg1 != null) set.playTogether(
                ObjectAnimator.ofFloat(cardBg1, "scaleX", 0.88f, 0.92f),
                ObjectAnimator.ofFloat(cardBg1, "scaleY", 0.88f, 0.92f)
        );
        if (cardBg2 != null) set.playTogether(
                ObjectAnimator.ofFloat(cardBg2, "scaleX", 0.84f, 0.88f),
                ObjectAnimator.ofFloat(cardBg2, "scaleY", 0.84f, 0.88f)
        );
        set.setDuration(520);
        set.setInterpolator(new OvershootInterpolator(0.8f));
        set.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator a) { animating = false; }
        });
        set.start();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SWIPE MANTIĞI
    // ══════════════════════════════════════════════════════════════════════

    private void onSwipeRight() {
        if (browseMode) {
            curIndex = (curIndex + 1) % tasks.size();
            renderCard(true);
        } else {
            onTaskCompleted(curIndex);
        }
    }

    private void onSwipeLeft() {
        if (browseMode) {
            curIndex = (curIndex - 1 + tasks.size()) % tasks.size();
            renderCard(true);
        } else {
            onTaskSkipped();
        }
    }

    private void onTaskCompleted(int index) {
        boolean wasAlreadyDone = doneSet.contains(index);

        if (!wasAlreadyDone) {
            tasks.get(index).done = true;
            doneSet.add(index);
            skippedSet.remove(index);
            saveDoneSet();

            AnchorManager.AnchorResult result = AnchorManager.onTaskCompleted(this);
            AnchorManager.addXP(this, tasks.get(index).zorluk * 10);
            HomeActivity.saveProgress(this, 0, true);

            if (result.milestoneReached) {
                Toast.makeText(this,
                        "🎉 +" + result.anchorsEarned + " ⚓  |  "
                                + result.newStreak + " günlük seri!",
                        Toast.LENGTH_LONG).show();
            }
            loadPlayerInfo(getSharedPreferences(PREFS_NAME, MODE_PRIVATE));

            if (doneSet.size() >= tasks.size()) {
                animating = false;
                onAllDone();
                return;
            }
        }

        int next = nextUndoneIndex(curIndex + 1);
        if (next == -1) next = firstUndoneIndex();
        if (next != -1) curIndex = next;
        renderCard(true);
    }

    private void onTaskSkipped() {
        if (!doneSet.contains(curIndex)) skippedSet.add(curIndex);
        int next = nextUndoneIndex(curIndex + 1);
        if (next == -1) next = firstUndoneIndex();
        if (next != -1) curIndex = next;
        renderCard(true);
    }

    private void onAllDone() {
        SharedPreferences prefs  = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String  claimKey = "anchor_claimed_" + categoryIdx + "_" + worldNumber;
        boolean claimed  = prefs.getBoolean(claimKey, false);

        if (claimed) {
            browseMode = true;
            curIndex   = 0;
            renderCard(true);
            return;
        }

        AnchorManager.onWorldCompleted(this, worldAnchors);
        prefs.edit().putBoolean(claimKey, true).apply();
        if (categoryIdx == 0) WorldsActivity.unlockNextWorld(this, worldNumber);
        loadPlayerInfo(prefs);

        new AlertDialog.Builder(this)
                .setTitle("TEBRİKLER! 🎉")
                .setMessage("Tüm görevleri tamamladın!\n⚓ +" + worldAnchors + " çapa kazandın!")
                .setPositiveButton("HARİTAYA DÖN", (d, w) -> finish())
                .setNegativeButton("Görevlere Bak", (d, w) -> {
                    browseMode = true;
                    curIndex   = 0;
                    renderCard(false);
                })
                .setCancelable(false)
                .show();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  KART RENDER
    // ══════════════════════════════════════════════════════════════════════

    private void renderCard(boolean animate) {
        if (tasks.isEmpty()) {
            if (txtTaskTitle != null) txtTaskTitle.setText("Görev bulunamadı");
            if (txtTaskDesc  != null) txtTaskDesc.setText("Bu bölüm henüz hazır değil.");
            return;
        }
        if (curIndex < 0 || curIndex >= tasks.size()) curIndex = 0;

        Task t = tasks.get(curIndex);
        if (txtTaskTitle   != null) txtTaskTitle.setText(t.title);
        if (txtTaskDesc    != null) txtTaskDesc.setText(t.desc);
        if (txtTaskNo      != null) txtTaskNo.setText((curIndex + 1) + " / " + tasks.size());
        if (txtCounter     != null) txtCounter.setText(doneSet.size() + " / " + tasks.size() + " tamamlandı");
        if (txtRepeatBadge != null)
            txtRepeatBadge.setText(t.zorluk == 1 ? "★☆☆" : t.zorluk == 2 ? "★★☆" : "★★★");

        if (cardTop != null) {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.parseColor(t.color));
            gd.setCornerRadius(dp(24));
            cardTop.setBackground(gd);
            cardTop.setAlpha(browseMode && doneSet.contains(curIndex) ? 0.82f : 1f);
        }
        applyBgCard(cardBg1, t.color, 0.6f);
        applyBgCard(cardBg2, t.color, 0.35f);
        renderPips();

        if (animate) {
            animateNextCardIn();
        } else {
            if (cardTop != null) {
                cardTop.setTranslationY(-40f);
                cardTop.setAlpha(0f);
                cardTop.animate()
                        .translationY(0f).alpha(1f)
                        .setDuration(350)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }
        }
    }

    private void applyBgCard(CardView card, String hexColor, float ratio) {
        if (card == null) return;
        try {
            int c = Color.parseColor(hexColor);
            int r = (int)(Color.red(c)   * ratio + 255 * (1 - ratio));
            int g = (int)(Color.green(c) * ratio + 255 * (1 - ratio));
            int b = (int)(Color.blue(c)  * ratio + 255 * (1 - ratio));
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.rgb(r, g, b));
            gd.setCornerRadius(dp(24));
            card.setBackground(gd);
        } catch (Exception ignored) {}
    }

    private void renderPips() {
        if (pipRow == null) return;
        pipRow.removeAllViews();
        for (int i = 0; i < tasks.size(); i++) {
            View pip = new View(this);
            int size = (i == curIndex) ? dp(10) : dp(7);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
            lp.setMargins(dp(3), 0, dp(3), 0);
            pip.setLayoutParams(lp);
            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.OVAL);
            if (i == curIndex) {
                bg.setColor(Color.parseColor("#FFFFFF"));
            } else if (doneSet.contains(i)) {
                bg.setColor(Color.argb(200, 255, 255, 255));
            } else if (skippedSet.contains(i)) {
                bg.setColor(Color.parseColor("#FF6B35"));
            } else {
                bg.setColor(Color.argb(80, 255, 255, 255));
            }
            pip.setBackground(bg);
            pipRow.addView(pip);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  YARDIMCILAR
    // ══════════════════════════════════════════════════════════════════════

    private int firstUndoneIndex() {
        for (int i = 0; i < tasks.size(); i++) if (!doneSet.contains(i)) return i;
        return 0;
    }

    private int nextUndoneIndex(int from) {
        for (int i = from; i < tasks.size(); i++) if (!doneSet.contains(i)) return i;
        return -1;
    }

    private float getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  NAVİGASYON
    // ══════════════════════════════════════════════════════════════════════

    private void setupNav() {
        safeNav(R.id.btnNavHome,     HomeActivity.class,     true);
        safeNav(R.id.btnNavWorlds,   WorldsActivity.class,   true);
        safeNav(R.id.btnNavChat,     ChatActivity.class,     false);
        safeNav(R.id.btnNavTracking, TrackingActivity.class, false);
        safeNav(R.id.btnNavSettings, SettingsActivity.class, true);
    }

    private void safeNav(int viewId, Class<?> target, boolean finishSelf) {
        View v = findViewById(viewId);
        if (v == null) return;
        v.setOnClickListener(x -> {
            startActivity(new Intent(this, target));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            if (finishSelf) finish();
        });
    }
}