package com.example.socialanxietyhelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int totalStars = 20;
    private int secilenSeviye;
    private List<Gorev> gorevListesi;

    private TextView txtStars, txtLevelTitle;
    private ImageView characterImage;
    private ImageButton btnHome, btnStar, btnProfile;
    private RecyclerView recyclerTasks;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        secilenSeviye = getIntent().getIntExtra("SELECTED_LEVEL", 1);

        txtStars = findViewById(R.id.txtStars);
        txtLevelTitle = findViewById(R.id.txtLevelTitle);
        characterImage = findViewById(R.id.characterImage);
        btnHome = findViewById(R.id.btnHome);
        btnStar = findViewById(R.id.btnStar);
        btnProfile = findViewById(R.id.btnProfile);
        recyclerTasks = findViewById(R.id.recyclerTasks);

        loadLevelContent();
        setupBottomMenu();
    }

    private void loadLevelContent() {
        gorevListesi = GorevDeposu.getSeviyeGorevleri(secilenSeviye);
        txtLevelTitle.setText("LEVEL " + secilenSeviye);

        adapter = new TaskAdapter(gorevListesi, this::handleTaskClick);
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(adapter);

        updateScoreText();
    }

    private void handleTaskClick(boolean isChecked, int index) {
        if (isChecked) {
            totalStars += 5;
            Toast.makeText(this, "Harika! +5 Yıldız! ✨", Toast.LENGTH_SHORT).show();
        } else {
            totalStars -= 5;
        }
        updateScoreText();

        boolean tamamlandi = true;
        for (Gorev g : gorevListesi) {
            if (!g.isTamamlandi()) {
                tamamlandi = false;
                break;
            }
        }

        if (tamamlandi) showSuccessDialog();
    }

    private void showSuccessDialog() {
        seviyeyiBitir(secilenSeviye);
        new AlertDialog.Builder(this)
                .setTitle("TEBRİKLER! 🎉")
                .setMessage("Tüm görevleri başarıyla tamamladın. Bir sonraki seviye artık açık!")
                .setPositiveButton("HARİTAYA DÖN", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void seviyeyiBitir(int bitenSeviye) {
        SharedPreferences preferences = getSharedPreferences("OYUN_VERISI", MODE_PRIVATE);
        int suAnkiUlasilan = preferences.getInt("ulasilanSeviye", 1);
        if (bitenSeviye >= suAnkiUlasilan) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("ulasilanSeviye", bitenSeviye + 1);
            editor.apply();
        }
    }

    private void updateScoreText() {
        txtStars.setText("Camille   " + totalStars + " ★");
    }

    private void setupBottomMenu() {
        btnHome.setOnClickListener(v -> Toast.makeText(this, "Ana Sayfa", Toast.LENGTH_SHORT).show());
        btnStar.setOnClickListener(v -> Toast.makeText(this, "Yıldızlar", Toast.LENGTH_SHORT).show());
        btnProfile.setOnClickListener(v -> Toast.makeText(this, "Profil", Toast.LENGTH_SHORT).show());
    }
}