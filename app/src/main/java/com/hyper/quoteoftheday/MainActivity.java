package com.hyper.quoteoftheday;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvQ;
    private ImageButton ibFav,ibShare;
    private Button btnFavActivity;
    private DatabaseReference ref;
    private List<String> qList;
    private String currentQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvQ = findViewById(R.id.tvQuote);
        ibFav = findViewById(R.id.ibFav);
        ibShare = findViewById(R.id.ibShare);
        btnFavActivity = findViewById(R.id.btnFavQuote);

        ref = FirebaseDatabase.getInstance().getReference("quotes");
        qList = new ArrayList<>();
        loadQuotes();

        ibFav.setOnClickListener(v -> FavQ(currentQ));
        ibShare.setOnClickListener(v -> share(currentQ));

        btnFavActivity.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FavQuote.class));
        });
    }

    private void loadQuotes() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qList.clear();
                for(DataSnapshot qSnapShot : snapshot.getChildren()){
                    String q = qSnapShot.getValue(String.class);
                    qList.add(q);
                }
                showRandomQ();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showRandomQ(){
        if(!qList.isEmpty()){
            Random random = new Random();
            currentQ = qList.get(random.nextInt(qList.size()));
            tvQ.setText(currentQ);
        }
    }

    private void FavQ(String q){
        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favQuotes");
        favRef.push().setValue(q);
        Toast.makeText(this, "Quote added to favourites", Toast.LENGTH_SHORT).show();
    }

    private void share(String q){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, q);
        startActivity(Intent.createChooser(intent, "Share via"));
    }
}