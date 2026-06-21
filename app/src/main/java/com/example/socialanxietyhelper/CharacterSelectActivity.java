package com.example.socialanxietyhelper;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CharacterSelectActivity extends AppCompatActivity {

    private static final String PREFS_NAME    = "CesaretPrefs";
    private static final String KEY_CHARACTER = "selectedCharacter";

    // Seçili kart renkleri
    private static final int COLOR_SELECTED_BG     = 0xFFF0EBE3;
    private static final int COLOR_NORMAL_BG        = 0xFFFFFFFF;
    private static final int COLOR_SELECTED_STROKE  = 0xFF1A1714;
    private static final int COLOR_NORMAL_STROKE    = 0xFFEAE5DC;

    private final int[] drawables = {
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

    private int        selectedIndex = 0;
    private ImageView  imgPreview;
    private GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);

        selectedIndex = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getInt(KEY_CHARACTER, 0);

        imgPreview = findViewById(R.id.imgPreview);
        grid       = findViewById(R.id.characterGrid);

        // Önizleme — FIT_CENTER yerine daha yakın görünüm
        imgPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgPreview.setImageResource(drawables[selectedIndex]);

        TextView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        TextView btnConfirm = findViewById(R.id.btnConfirm);
        if (btnConfirm != null) btnConfirm.setOnClickListener(v -> { save(); finish(); });

        buildGrid();
    }

    // ── Grid ─────────────────────────────────────────────────────────────
    private void buildGrid() {
        grid.removeAllViews();

        for (int i = 0; i < drawables.length; i++) {
            final int idx = i;

            // ── Wrapper — grid hücresi ──
            FrameLayout wrapper = new FrameLayout(this);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            lp.width  = 0;
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
            lp.setMargins(dp(10), dp(10), dp(10), dp(10));
            wrapper.setLayoutParams(lp);

            // ── CardView — sabit kare + yarı radius = tam daire clip ──
            CardView card = new CardView(this);
            int cellSize = dp(92);
            FrameLayout.LayoutParams cardLp =
                    new FrameLayout.LayoutParams(cellSize, cellSize);
            cardLp.gravity = Gravity.CENTER;
            card.setLayoutParams(cardLp);
            card.setRadius(cellSize / 2f);  // yarısı = tam daire
            card.setCardElevation(i == selectedIndex ? dp(3) : 0);
            card.setUseCompatPadding(false);
            card.setPreventCornerOverlap(true);
            card.setCardBackgroundColor(
                    i == selectedIndex ? COLOR_SELECTED_BG : COLOR_NORMAL_BG);

            // Seçili border — OVAL GradientDrawable card'a arka plan olarak ver
            android.graphics.drawable.GradientDrawable strokeBg =
                    new android.graphics.drawable.GradientDrawable();
            strokeBg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            strokeBg.setColor(i == selectedIndex ? COLOR_SELECTED_BG : COLOR_NORMAL_BG);
            strokeBg.setStroke(dp(i == selectedIndex ? 3 : 1),
                    i == selectedIndex ? COLOR_SELECTED_STROKE : COLOR_NORMAL_STROKE);
            card.setBackground(strokeBg);

            // ── Avatar — tam doldur ──
            ImageView img = new ImageView(this);
            img.setImageResource(drawables[i]);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
            card.addView(img);
            wrapper.addView(card);

            // Seçili olmayan soluk
            wrapper.setAlpha(i == selectedIndex ? 1f : 0.58f);

            // Tıklama
            wrapper.setClickable(true);
            wrapper.setFocusable(true);
            wrapper.setOnClickListener(v -> {
                selectedIndex = idx;

                // Önizleme pop
                imgPreview.animate().scaleX(0.72f).scaleY(0.72f).setDuration(90)
                        .withEndAction(() -> {
                            imgPreview.setImageResource(drawables[idx]);
                            imgPreview.animate()
                                    .scaleX(1f).scaleY(1f)
                                    .setDuration(230)
                                    .setInterpolator(new OvershootInterpolator(1.4f))
                                    .start();
                        }).start();

                refreshGrid();
                save();
            });

            grid.addView(wrapper);
        }
    }

    // ── Tüm hücreleri güncelle ────────────────────────────────────────────
    private void refreshGrid() {
        for (int i = 0; i < grid.getChildCount(); i++) {
            FrameLayout wrapper = (FrameLayout) grid.getChildAt(i);
            CardView     card   = (CardView) wrapper.getChildAt(0);
            boolean      sel    = (i == selectedIndex);

            // Card stroke güncelle
            android.graphics.drawable.GradientDrawable strokeBg =
                    new android.graphics.drawable.GradientDrawable();
            strokeBg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            strokeBg.setColor(sel ? COLOR_SELECTED_BG : COLOR_NORMAL_BG);
            strokeBg.setStroke(dp(sel ? 3 : 1),
                    sel ? COLOR_SELECTED_STROKE : COLOR_NORMAL_STROKE);
            card.setBackground(strokeBg);
            card.setCardElevation(sel ? dp(3) : 0);
            card.setCardBackgroundColor(sel ? COLOR_SELECTED_BG : COLOR_NORMAL_BG);

            // Alpha + scale animasyonu
            wrapper.animate()
                    .alpha(sel ? 1f : 0.58f)
                    .scaleX(sel ? 1.05f : 1f)
                    .scaleY(sel ? 1.05f : 1f)
                    .setDuration(170)
                    .setInterpolator(new OvershootInterpolator(1.1f))
                    .start();
        }
    }

    // ── Kaydet ───────────────────────────────────────────────────────────
    private void save() {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                .putInt(KEY_CHARACTER, selectedIndex)
                .apply();
    }

    private int dp(int val) {
        return Math.round(val * getResources().getDisplayMetrics().density);
    }
}