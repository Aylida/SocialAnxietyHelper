package com.example.socialanxietyhelper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * AnchorManager — Çapa ve seri yönetimi
 *
 * Kazanma kuralları:
 *   • Her 5 günlük seri tamamlanınca      → +1 ⚓
 *   • Bölüm tamamlanınca                  → +bölümün elmas değeri
 *
 * XP arka planda çalışmaya devam eder (seviye için), UI'da gösterilmez.
 */
public class AnchorManager {

    // ── SharedPreferences anahtarları ─────────────────────────────────────
    private static final String PREFS_NAME          = "CesaretPrefs";

    // Elmas
    public  static final String KEY_DIAMONDS        = "anchors";

    // Seri
    public  static final String KEY_STREAK          = "currentSeri";
    public  static final String KEY_STREAK_LAST_DAY = "streakLastDay";   // epoch gün sayısı
    public  static final String KEY_STREAK_DIAMONDS = "streakAnchorCount"; // kaç kez 5'in katına ulaşıldı

    // XP (görünmez ama seviye için tutuluyor)
    public  static final String KEY_XP              = "playerXP";
    public  static final String KEY_XP_MAX          = "playerXPMax";
    public  static final String KEY_LEVEL           = "playerLevel";

    // Görev sayacı
    public  static final String KEY_TASKS_DONE      = "completedGorevs";

    private static final int    XP_MAX_DEFAULT      = 1000;
    private static final int    MAX_LEVEL           = 10;
    private static final int    STREAK_DIAMOND_STEP = 5;   // her 5 seride 1 elmas
    private static final int    AVATAR_PRICE        = 5;
    private static final int    CATEGORY_PRICE      = 3;

    // ── Elmas işlemleri ───────────────────────────────────────────────────

    /** Mevcut elmas bakiyesini döner */
    public static int getAnchors(Context ctx) {
        return prefs(ctx).getInt(KEY_DIAMONDS, 0);
    }

    /** Elmas ekle ve kaydet */
    public static void addAnchors(Context ctx, int amount) {
        SharedPreferences.Editor ed = prefs(ctx).edit();
        ed.putInt(KEY_DIAMONDS, getAnchors(ctx) + amount);
        ed.apply();
    }

    /**
     * Elmas harca. Yeterli bakiye varsa harcar ve true döner.
     * Yeterli bakiye yoksa false döner, harcama yapılmaz.
     */
    public static boolean spendAnchors(Context ctx, int amount) {
        int current = getAnchors(ctx);
        if (current < amount) return false;
        prefs(ctx).edit().putInt(KEY_DIAMONDS, current - amount).apply();
        return true;
    }

    /** Avatar satın alma — 5 ⚓ */
    public static boolean buyAvatar(Context ctx) {
        return spendAnchors(ctx, AVATAR_PRICE);
    }

    /** Kategori açma — 3 ⚓ */
    public static boolean buyCategory(Context ctx) {
        return spendAnchors(ctx, CATEGORY_PRICE);
    }

    // ── Bölüm tamamlama ───────────────────────────────────────────────────

    /**
     * Bir bölüm tamamlandığında çağrılır.
     * @param anchorReward bölümün tanımlı elmas ödülü
     * @return kazanılan elmas sayısı
     */
    public static int onWorldCompleted(Context ctx, int anchorReward) {
        addAnchors(ctx, anchorReward);
        return anchorReward;
    }

    // ── Seri (streak) yönetimi ────────────────────────────────────────────

    /**
     * Günlük görev tamamlandığında çağrılır.
     * Seriyi günceller, 5'in katına ulaşıldıysa elmas verir.
     *
     * @return AnchorResult — kaç elmas kazanıldı + yeni seri değeri
     */
    public static AnchorResult onTaskCompleted(Context ctx) {
        SharedPreferences sp = prefs(ctx);
        SharedPreferences.Editor ed = sp.edit();

        long todayEpoch = todayEpochDay();
        long lastDay    = sp.getLong(KEY_STREAK_LAST_DAY, -1);
        int  streak     = sp.getInt(KEY_STREAK, 0);
        int  streakAnchorCount = sp.getInt(KEY_STREAK_DIAMONDS, 0);

        // Bugün zaten sayıldıysa tekrar sayma
        if (lastDay == todayEpoch) {
            return new AnchorResult(0, streak, false);
        }

        // Dün yapıldıysa seriyi devam ettir, yoksa sıfırla
        if (lastDay == todayEpoch - 1) {
            streak++;
        } else if (lastDay != todayEpoch) {
            streak = 1; // sıfırla ve bugünü say
        }

        ed.putInt(KEY_STREAK, streak);
        ed.putLong(KEY_STREAK_LAST_DAY, todayEpoch);

        // Her 5 seride 1 elmas
        int newStreakAnchorCount = streak / STREAK_DIAMOND_STEP;
        int anchorsEarned = 0;
        boolean milestoneReached = false;

        if (newStreakAnchorCount > streakAnchorCount) {
            anchorsEarned = newStreakAnchorCount - streakAnchorCount;
            ed.putInt(KEY_STREAK_DIAMONDS, newStreakAnchorCount);
            addAnchors(ctx, anchorsEarned);
            milestoneReached = true;
        }

        // Görev sayacını artır
        int tasksDone = sp.getInt(KEY_TASKS_DONE, 0);
        ed.putInt(KEY_TASKS_DONE, tasksDone + 1);

        ed.apply();
        return new AnchorResult(anchorsEarned, streak, milestoneReached);
    }

    // ── XP / Seviye (arka planda) ─────────────────────────────────────────

    /**
     * XP ekle ve gerekirse seviye atla.
     * UI'da gösterilmez, sadece arka planda çalışır.
     */
    public static void addXP(Context ctx, int xpGained) {
        SharedPreferences sp = prefs(ctx);
        SharedPreferences.Editor ed = sp.edit();

        int xp      = sp.getInt(KEY_XP, 0);
        int level   = sp.getInt(KEY_LEVEL, 1);
        int xpMax   = sp.getInt(KEY_XP_MAX, XP_MAX_DEFAULT);

        int newXP = xp + xpGained;
        if (newXP >= xpMax && level < MAX_LEVEL) {
            newXP -= xpMax;
            level++;
            xpMax = (int)(xpMax * 1.3f);
            ed.putInt(KEY_LEVEL,   level);
            ed.putInt(KEY_XP_MAX,  xpMax);
        }
        ed.putInt(KEY_XP, Math.min(newXP, xpMax));
        ed.apply();
    }

    /** Mevcut seviyeyi döner */
    public static int getLevel(Context ctx) {
        return prefs(ctx).getInt(KEY_LEVEL, 1);
    }

    /** Mevcut seriyi döner */
    public static int getStreak(Context ctx) {
        return prefs(ctx).getInt(KEY_STREAK, 0);
    }

    // ── Yardımcılar ───────────────────────────────────────────────────────

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /** Bugünün epoch gün sayısını döner (gün bazlı karşılaştırma için) */
    private static long todayEpochDay() {
        return System.currentTimeMillis() / (1000L * 60 * 60 * 24);
    }

    // ── Sonuç modeli ──────────────────────────────────────────────────────

    /**
     * onTaskCompleted'ın döndürdüğü sonuç.
     * anchorsEarned > 0 ise kullanıcıya bildirim göster.
     */
    public static class AnchorResult {
        public final int     anchorsEarned;   // bu görevden kazanılan elmas (0 veya 1)
        public final int     newStreak;        // güncel seri
        public final boolean milestoneReached; // 5'in katına ulaşıldı mı

        public AnchorResult(int anchorsEarned, int newStreak, boolean milestoneReached) {
            this.anchorsEarned   = anchorsEarned;
            this.newStreak        = newStreak;
            this.milestoneReached = milestoneReached;
        }
    }
}