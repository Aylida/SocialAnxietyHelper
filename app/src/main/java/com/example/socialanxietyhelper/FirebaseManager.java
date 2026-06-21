package com.example.socialanxietyhelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * FirebaseManager — sadece Authentication
 * Veri SharedPreferences'ta local kalır, bulut sync yok.
 */
public class FirebaseManager {

    public static boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static FirebaseUser currentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void register(String email, String password, AuthCallback cb) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(r -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(friendly(e.getMessage())));
    }

    public static void login(String email, String password, AuthCallback cb) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(r -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(friendly(e.getMessage())));
    }

    public static void resetPassword(String email, AuthCallback cb) {
        FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnSuccessListener(v -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(friendly(e.getMessage())));
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    private static String friendly(String msg) {
        if (msg == null) return "Bir hata oluştu.";
        if (msg.contains("email address is already in use")) return "Bu e-posta zaten kayıtlı.";
        if (msg.contains("badly formatted"))                return "Geçersiz e-posta adresi.";
        if (msg.contains("INVALID_LOGIN_CREDENTIALS"))      return "E-posta veya şifre hatalı.";
        if (msg.contains("no user record"))                 return "Bu e-posta ile kayıtlı hesap bulunamadı.";
        if (msg.contains("weak-password"))                  return "Şifre en az 6 karakter olmalı.";
        if (msg.contains("network"))                        return "İnternet bağlantısı yok.";
        return "Bir hata oluştu. Tekrar dene.";
    }

    public interface AuthCallback {
        void onSuccess();
        void onError(String message);
    }
}