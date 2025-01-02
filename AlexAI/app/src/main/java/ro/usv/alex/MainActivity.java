package ro.usv.alex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fereastra principala a aplicatiei, care permite utilizatorului sa trimita intrebari si sa primeasca raspunsuri de la server.
 */
public class MainActivity extends AppCompatActivity {

    /** URL-ul serverului la care se va trimite cererea */
    private static final String BASE_URL = "http://10.0.2.2:8000";

    /** TextView-ul unde va fi afisat chat-ul */
    private TextView textViewChat;

    /** Limba selectata de utilizator */
    private String selectedLanguage = "en";

    /**
     * Metoda care este apelata cand activitatea este creata.
     * Aici se seteaza layout-ul, se initializeaza elementele UI si se configureaza comportamentele butoanelor.
     *
     * @param savedInstanceState starea salvata a activitatii
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activeaza modul Edge-to-Edge pentru a permite o interfata de utilizator mai moderna
        EdgeToEdge.enable(this);

        // Seteaza layout-ul activitatii
        setContentView(R.layout.activity_main);

        // Preia limba selectata din Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedLanguage")) {
            selectedLanguage = intent.getStringExtra("selectedLanguage");  // Salveaza limba aleasa
        }

        // Initializeaza elementele de UI
        EditText editTextMessage = findViewById(R.id.editTextMessage);
        textViewChat = findViewById(R.id.textViewChat);
        ImageButton imageButtonSend = findViewById(R.id.imageButtonSend);

        ImageButton imageButtonSettings = findViewById(R.id.imageButtonSettings);

        // Seteaza actiunea pentru butonul de settings care va naviga la SettingsActivity
        imageButtonSettings.setOnClickListener(v -> navigateToSettingsActivity());

        // Actiunea pentru butonul de send
        imageButtonSend.setOnClickListener(v -> {
            // Preia intrebarea introdusa de utilizator
            String question = editTextMessage.getText().toString().trim();

            // Verifica daca utilizatorul a introdus un mesaj gol
            if (question.isEmpty()) {
                Toast.makeText(MainActivity.this, "Mesajul nu poate fi gol!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Afiseaza intrebarea utilizatorului in chat
            textViewChat.append("Tu: " + question + "\n");

            // Trimite intrebarea la server
            sendQuestionToServer(question);

            // Goleste campul EditText dupa trimiterea mesajului
            editTextMessage.setText("");
        });

        // Aplica padding-ul corect pentru a face loc pentru bara de status
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Trimite intrebarea la server si proceseaza raspunsul.
     *
     * @param question intrebarea care va fi trimisa catre server
     */
    private void sendQuestionToServer(String question) {
        // Creeaza un client HTTP
        OkHttpClient client = new OkHttpClient();

        // Construieste URL-ul pentru cererea GET, incluzand intrebarea si limba selectata
        String url = BASE_URL + "/ask?question=" + question + "&language=" + selectedLanguage;

        // Creeaza cererea HTTP GET
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // Trimite cererea asincron
        client.newCall(request).enqueue(new Callback() {
            /**
             * Apelat atunci cand cererea nu reuseste.
             *
             * @param call obiectul care reprezinta cererea
             * @param e exceptia care a aparut
             */
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Daca exista o eroare de conexiune cu serverul
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Eroare la conectarea cu serverul!", Toast.LENGTH_SHORT).show();
                });
            }

            /**
             * Apelat atunci cand cererea a avut succes si serverul a trimis un raspuns.
             *
             * @param call obiectul care reprezinta cererea
             * @param response raspunsul primit de la server
             * @throws IOException daca apare o eroare la citirea raspunsului
             */
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // Daca cererea a fost procesata cu succes de server
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Converteste raspunsul in string
                        String serverResponse = response.body().string();

                        // Parseaza raspunsul JSON si extrage mesajul
                        JSONObject jsonResponse = new JSONObject(serverResponse);
                        String message = jsonResponse.getString("response");

                        // Afiseaza raspunsul primit de la server in UI
                        runOnUiThread(() -> textViewChat.append("AlexAI: " + message + "\n"));
                    } catch (Exception e) {
                        // Daca exista o eroare in procesarea raspunsului JSON
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Eroare la procesarea raspunsului!", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    // Daca serverul returneaza un raspuns invalid
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Raspuns invalid de la server!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    /**
     * Navigheaza catre pagina de setari.
     */
    private void navigateToSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);  // Lanseaza activitatea de settings
    }
}
