package ro.usv.alex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Fereastra de setari a aplicatiei, care permite utilizatorului sa aleaga limba aplicatiei
 * si sa se delogheze din contul Firebase.
 */
public class SettingsActivity extends AppCompatActivity {

    /** Obiectul FirebaseAuth folosit pentru autentificarea utilizatorului */
    private FirebaseAuth mAuth;

    /** RadioGroup pentru selectia limbii */
    private RadioGroup radioGroupLanguage;

    /** RadioButton pentru limba engleza */
    private RadioButton radioButtonEnglish;

    /** RadioButton pentru limba romana */
    private RadioButton radioButtonRomanian;

    /** SharedPreferences pentru salvarea setarilor aplicatiei */
    private SharedPreferences sharedPreferences;

    /**
     * Metoda care este apelata atunci cand activitatea este creata.
     * Aici se initializeaza elementele UI si se configureaza comportamentele butoanelor.
     *
     * @param savedInstanceState starea salvata a activitatii
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Seteaza layout-ul corespunzator pentru activitate
        setContentView(R.layout.activity_settings);

        // Initializeaza FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initializeaza SharedPreferences pentru a salva limba selectata
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Initializeaza RadioGroup si RadioButton
        radioGroupLanguage = findViewById(R.id.radioGroup);
        radioButtonEnglish = findViewById(R.id.radioButtonEnglish);
        radioButtonRomanian = findViewById(R.id.radioButtonRomanian);

        // Incarca limba selectata din SharedPreferences
        loadSelectedLanguage();

        // Seteaza actiunea pentru butonul de Back
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> navigateBackToMainActivity());

        // Seteaza actiunea pentru butonul de Logout
        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> logoutAndNavigateToLogin());
    }

    /**
     * Salveaza limba selectata in SharedPreferences.
     *
     * @param language limba selectata de utilizator
     */
    private void saveSelectedLanguage(String language) {
        // Salveaza limba selectata in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selectedLanguage", language);
        editor.apply();
    }

    /**
     * Incarca limba selectata din SharedPreferences si actualizeaza RadioButton-urile.
     */
    private void loadSelectedLanguage() {
        // Preia limba salvata anterior din SharedPreferences
        String savedLanguage = sharedPreferences.getString("selectedLanguage", "en");

        // Marcheaza RadioButton-ul corespunzator limbei salvate
        if (savedLanguage.equals("ro")) {
            radioButtonRomanian.setChecked(true);
        } else {
            radioButtonEnglish.setChecked(true);
        }
    }

    /**
     * Navigheaza inapoi la MainActivity cu limba selectata de utilizator.
     * Salveaza limba selectata in SharedPreferences si o trimite ca parametru in Intent.
     */
    private void navigateBackToMainActivity() {
        // Verifica care RadioButton este selectat si salveaza limba selectata
        String selectedLanguage = radioButtonEnglish.isChecked() ? "en" : "ro";

        // Salveaza limba selectata in SharedPreferences
        saveSelectedLanguage(selectedLanguage);

        // Creeaza un Intent pentru a trimite limba selectata la MainActivity
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.putExtra("selectedLanguage", selectedLanguage);  // Adauga limba selectata in Intent
        startActivity(intent);
        finish(); // Inchide SettingsActivity pentru a nu putea reveni pe ea cu Back
    }

    /**
     * Delogheaza utilizatorul din Firebase si navigheaza la LoginActivity.
     */
    private void logoutAndNavigateToLogin() {
        // Delogheaza utilizatorul din Firebase
        mAuth.signOut();

        // Creeaza un Intent pentru a naviga la LoginActivity
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Inchide SettingsActivity pentru a preveni revenirea la aceasta
    }
}
