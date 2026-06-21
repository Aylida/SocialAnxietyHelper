package com.example.socialanxietyhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {

    private static final String PREFS_NAME   = "CesaretPrefs";
    private static final String API_KEY      = "YOUR_API_KEY";
    private static final String API_URL      = "https://api.groq.com/openai/v1/chat/completions";
    private static final String ACCENT_COLOR = "#A0522D";

    private LinearLayout msgContainer;
    private ScrollView   scrollView;
    private EditText     etInput;
    private ImageButton  btnSend;
    private LinearLayout quickBtnsRow;

    private final List<JSONObject> chatHistory = new ArrayList<>();
    private final ExecutorService  executor    = Executors.newSingleThreadExecutor();
    private final Handler          mainHandler = new Handler(Looper.getMainLooper());

    private String playerName = "Kahraman";
    private String charEmoji  = "🐱";

    private static final String[] CHAR_EMOJIS = {"🐱","🐧","🐓","🐢","🦕"};
    private static final String[] QUICK_QUESTIONS = {
            "Nasıl başlamalıyım?",
            "Panik hissediyorum 😰",
            "Görev için ipucu ver lütfen",
            "Neden böyle hissediyorum?",
            "Motivasyon lazım 💪",
            "Bugün çok zorlandım"
    };

    private static final String SYSTEM_PROMPT =
            "Sen 'Rehber Simit' adında bir sosyal anksiyete uzmanısın. " +
                    "Sosyal anksiyete konusunda bilimsel ama sıcak, destekleyici ve anlayışlı bir yaklaşım benimsiyorsun. " +
                    "Cevapların kısa ve öz olsun (2-4 cümle). Türkçe konuş. " +
                    "Kullanıcıyı cesaretlendir, yargılama. Pratik ipuçları ver. " +
                    "Emoji kullanabilirsin ama abartma.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        playerName = prefs.getString("playerName", "Kahraman");
        int charIdx = prefs.getInt("selectedCharacter", 0);
        charEmoji = CHAR_EMOJIS[charIdx < CHAR_EMOJIS.length ? charIdx : 0];

        bindViews();
        setupQuickButtons();
        setupListeners();
        setupNav();
        showWelcomeMessage();
    }

    private void bindViews() {
        msgContainer = findViewById(R.id.msgContainer);
        scrollView   = findViewById(R.id.scrollView);
        etInput      = findViewById(R.id.etInput);
        btnSend      = findViewById(R.id.btnSend);
        quickBtnsRow = findViewById(R.id.quickBtnsRow);

        TextView txtChar = findViewById(R.id.txtCharEmoji);
        if (txtChar != null) txtChar.setText(charEmoji);

        etInput.setHintTextColor(ContextCompat.getColor(this, R.color.textSecondary));
        etInput.setTextColor(ContextCompat.getColor(this, R.color.textPrimary));
    }

    private boolean welcomeShown = false;

    private void showWelcomeMessage() {
        if (welcomeShown) return;
        welcomeShown = true;
        addAIMessage("Merhaba " + playerName + "! 👋 Ben Rehber Simit — sosyal anksiyete alanında uzmanlaşmış yol arkadaşınım. Görevlerinde zorlandığın anlarda veya sadece konuşmak istediğinde buradayım. 🌿");
    }

    private void setupQuickButtons() {
        if (quickBtnsRow == null) return;
        quickBtnsRow.removeAllViews();
        for (String q : QUICK_QUESTIONS) {
            TextView btn = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(dp(8));
            btn.setLayoutParams(lp);
            btn.setText(q);
            btn.setTextSize(12);
            btn.setTypeface(null, Typeface.NORMAL);
            btn.setTextColor(ContextCompat.getColor(this, R.color.textPrimary));
            btn.setPadding(dp(14), dp(8), dp(14), dp(8));

            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.RECTANGLE);
            bg.setCornerRadius(dp(20));
            bg.setColor(ContextCompat.getColor(this, R.color.cardBg));
            bg.setStroke(dp(1), ContextCompat.getColor(this, R.color.cardBorder));
            btn.setBackground(bg);

            btn.setOnClickListener(v -> sendMessage(q));
            quickBtnsRow.addView(btn);
        }
    }

    private void setupListeners() {
        btnSend.setOnClickListener(v -> {
            String text = etInput.getText().toString().trim();
            if (!text.isEmpty()) {
                etInput.setText("");
                sendMessage(text);
            }
        });

        etInput.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                btnSend.performClick();
                return true;
            }
            return false;
        });
    }

    private void sendMessage(String text) {
        addUserMessage(text);
        showTypingIndicator();
        try {
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", text);
            chatHistory.add(userMsg);
        } catch (Exception e) { e.printStackTrace(); }
        callGroqAPI();
    }

    private void callGroqAPI() {
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager)
                getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected()) {
            mainHandler.post(() -> {
                hideTypingIndicator();
                addAIMessage("📡 Şu an çevrimdışısın. İnternet bağlantını kontrol edip tekrar dene.");
            });
            return;
        }


        executor.execute(() -> {
            try {
                JSONArray messages = new JSONArray();
                JSONObject systemMsg = new JSONObject();
                systemMsg.put("role", "system");
                systemMsg.put("content", SYSTEM_PROMPT + " Kullanıcının adı " + playerName + ".");
                messages.put(systemMsg);
                for (JSONObject msg : chatHistory) messages.put(msg);

                JSONObject body = new JSONObject();
                body.put("model", "llama-3.3-70b-versatile");
                body.put("messages", messages);
                body.put("max_tokens", 300);
                body.put("temperature", 0.7);

                byte[] bodyBytes = body.toString().getBytes(StandardCharsets.UTF_8);

                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(30000);
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
                conn.setRequestProperty("Content-Length", String.valueOf(bodyBytes.length));
                conn.connect();

                OutputStream os = conn.getOutputStream();
                os.write(bodyBytes); os.flush(); os.close();

                int responseCode = conn.getResponseCode();
                BufferedReader br = responseCode == 200
                        ? new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))
                        : new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                String responseText;
                if (responseCode == 200) {
                    JSONObject response = new JSONObject(sb.toString());
                    responseText = response.getJSONArray("choices")
                            .getJSONObject(0).getJSONObject("message").getString("content");
                    JSONObject assistantMsg = new JSONObject();
                    assistantMsg.put("role", "assistant");
                    assistantMsg.put("content", responseText);
                    chatHistory.add(assistantMsg);
                } else {
                    responseText = "Bağlantı sorunu (" + responseCode + "). Lütfen tekrar dene.";
                }

                final String finalText = responseText;
                mainHandler.post(() -> { hideTypingIndicator(); addAIMessage(finalText); });

            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    hideTypingIndicator();
                    addAIMessage("Bağlantı hatası: " + e.getMessage());
                });
            }
        });
    }

    private void addUserMessage(String text) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.END);
        LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowLp.setMargins(0, 0, 0, dp(12));
        row.setLayoutParams(rowLp);

        TextView bubble = new TextView(this);
        bubble.setText(text);
        bubble.setTextSize(13);
        bubble.setTextColor(Color.WHITE);
        bubble.setLineSpacing(dp(3), 1f);
        bubble.setPadding(dp(14), dp(10), dp(14), dp(10));
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadii(new float[]{dp(18),dp(18),dp(18),dp(18),dp(4),dp(4),dp(18),dp(18)});
        bg.setColor(Color.parseColor(ACCENT_COLOR));
        bubble.setBackground(bg);
        bubble.setMaxWidth((int)(getResources().getDisplayMetrics().widthPixels * 0.75f));
        row.addView(bubble);

        msgContainer.addView(row);
        scrollToBottom();
    }

    private void addAIMessage(String text) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.START);
        LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowLp.setMargins(0, 0, 0, dp(12));
        row.setLayoutParams(rowLp);

        TextView avatar = new TextView(this);
        avatar.setText(charEmoji);
        avatar.setTextSize(14);
        avatar.setGravity(android.view.Gravity.CENTER);
        LinearLayout.LayoutParams avatarLp = new LinearLayout.LayoutParams(dp(32), dp(32));
        avatarLp.setMarginEnd(dp(8));
        avatarLp.gravity = android.view.Gravity.TOP;
        avatar.setLayoutParams(avatarLp);
        GradientDrawable avatarBg = new GradientDrawable();
        avatarBg.setShape(GradientDrawable.OVAL);
        avatarBg.setColor(Color.parseColor("#F5C842"));
        avatarBg.setStroke(dp(2), Color.parseColor("#3A2E00"));
        avatar.setBackground(avatarBg);

        TextView bubble = new TextView(this);
        bubble.setText(text);
        bubble.setTextSize(13);
        bubble.setTextColor(ContextCompat.getColor(this, R.color.textPrimary));
        bubble.setLineSpacing(dp(3), 1f);
        bubble.setPadding(dp(14), dp(12), dp(14), dp(12));
        GradientDrawable bubbleBg = new GradientDrawable();
        bubbleBg.setShape(GradientDrawable.RECTANGLE);
        bubbleBg.setCornerRadii(new float[]{dp(4),dp(4),dp(18),dp(18),dp(18),dp(18),dp(18),dp(18)});
        bubbleBg.setColor(ContextCompat.getColor(this, R.color.cardBg));
        bubbleBg.setStroke(dp(1), ContextCompat.getColor(this, R.color.cardBorder));
        bubble.setBackground(bubbleBg);
        bubble.setMaxWidth((int)(getResources().getDisplayMetrics().widthPixels * 0.75f));

        row.addView(avatar);
        row.addView(bubble);
        msgContainer.addView(row);
        scrollToBottom();
    }

    private View typingView;

    private void showTypingIndicator() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(android.view.Gravity.START);
        LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowLp.setMargins(0, 0, 0, dp(12));
        row.setLayoutParams(rowLp);

        TextView avatar = new TextView(this);
        avatar.setText(charEmoji);
        avatar.setTextSize(14);
        avatar.setGravity(android.view.Gravity.CENTER);
        LinearLayout.LayoutParams avatarLp = new LinearLayout.LayoutParams(dp(32), dp(32));
        avatarLp.setMarginEnd(dp(8));
        avatarLp.gravity = android.view.Gravity.TOP;
        avatar.setLayoutParams(avatarLp);
        GradientDrawable avatarBg = new GradientDrawable();
        avatarBg.setShape(GradientDrawable.OVAL);
        avatarBg.setColor(Color.parseColor("#F5C842"));
        avatarBg.setStroke(dp(2), Color.parseColor("#3A2E00"));
        avatar.setBackground(avatarBg);

        TextView dots = new TextView(this);
        dots.setText("• • •");
        dots.setTextSize(16);
        dots.setTextColor(ContextCompat.getColor(this, R.color.textSecondary));
        dots.setPadding(dp(14), dp(10), dp(14), dp(10));
        GradientDrawable dotsBg = new GradientDrawable();
        dotsBg.setShape(GradientDrawable.RECTANGLE);
        dotsBg.setCornerRadius(dp(14));
        dotsBg.setColor(ContextCompat.getColor(this, R.color.cardBg));
        dotsBg.setStroke(dp(1), ContextCompat.getColor(this, R.color.cardBorder));
        dots.setBackground(dotsBg);

        row.addView(avatar);
        row.addView(dots);
        typingView = row;
        msgContainer.addView(row);
        scrollToBottom();
    }

    private void hideTypingIndicator() {
        if (typingView != null) {
            msgContainer.removeView(typingView);
            typingView = null;
        }
    }

    private void scrollToBottom() {
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }

    private void setupNav() {
        findViewById(R.id.btnNavHome).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
        findViewById(R.id.btnNavWorlds).setOnClickListener(v -> {
            startActivity(new Intent(this, WorldsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
        findViewById(R.id.btnNavSettings).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
        findViewById(R.id.btnNavTracking).setOnClickListener(v -> {
            startActivity(new Intent(this, TrackingActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
        findViewById(R.id.btnNavChat).setOnClickListener(v -> {});
    }

    private int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}