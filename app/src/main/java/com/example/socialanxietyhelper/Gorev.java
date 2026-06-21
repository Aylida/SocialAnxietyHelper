package com.example.socialanxietyhelper;

public class Gorev {
    private String baslik;
    private String aciklama;
    private boolean tamamlandi;
    private int zorluk;
    private int xp; // ileride kullanılacak
    private boolean kilitli;

    // Constructor
    public Gorev(String baslik, String aciklama, int zorluk, boolean kilitli) {
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.zorluk = zorluk;
        this.kilitli = kilitli;
        this.tamamlandi = false;
        this.xp = 0;
    }

    // Getter & Setter
    public String getBaslik() { return baslik; }
    public String getAciklama() { return aciklama; }
    public boolean isTamamlandi() { return tamamlandi; }
    public int getZorluk() { return zorluk; }

    public void setTamamlandi(boolean tamamlandi) { this.tamamlandi = tamamlandi; }

    public boolean isKilitli() { return kilitli; }  // adapterde kullanacağımız doğru isim
    public void setKilitli(boolean kilitli) { this.kilitli = kilitli; }
}