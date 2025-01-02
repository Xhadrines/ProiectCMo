package ro.usv.alex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.material.button.MaterialButton;

/**
 * Fereastra de login a aplicatiei, care permite utilizatorilor sa se autentifice folosind
 * email si parola sau prin Google Sign-In.
 */
public class LoginActivity extends AppCompatActivity {

    /** Campul EditText pentru adresa de email */
    private EditText editEmailAddress, editPassword;

    /** Butonul pentru autentificare cu email si parola */
    private Button buttonLogin;

    /** Butonul pentru autentificare cu Google */
    private MaterialButton buttonLoginGoogle;

    /** Obiectul FirebaseAuth pentru gestionarea autentificarii utilizatorului */
    private FirebaseAuth mAuth;

    /** Obiectul GoogleSignInClient pentru a realiza autentificarea prin Google */
    private GoogleSignInClient mGoogleSignInClient;

    /** Codul pentru activitatea de login Google */
    private static final int RC_SIGN_IN = 9001;

    /**
     * Metoda care este apelata atunci cand activitatea este creata.
     * Aici se initializeaza componentele UI si se configureaza comportamentele butoanelor de login.
     *
     * @param savedInstanceState starea salvata a activitatii
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Seteaza layout-ul corespunzator pentru activitate
        setContentView(R.layout.activity_login);

        // Initializeaza componentele UI
        editEmailAddress = findViewById(R.id.editEmailAddress);
        editPassword = findViewById(R.id.editPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLoginGoogle = findViewById(R.id.buttonLoginGoogle);

        // Initializeaza FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Configureaza Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // Client ID-ul obtinut din Firebase Console
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Verifica daca utilizatorul este deja autentificat si trimite-l pe MainActivity
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            navigateToMainActivity();
        }

        // Seteaza listener pentru autentificarea cu email si parola
        buttonLogin.setOnClickListener(v -> loginUser());

        // Seteaza listener pentru autentificarea cu Google
        buttonLoginGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    /**
     * Autentifica utilizatorul folosind email si parola.
     * Daca autentificarea reuseste, se redirectioneaza utilizatorul la MainActivity.
     * Daca autentificarea esueaza, se afiseaza un mesaj de eroare.
     */
    private void loginUser() {
        String email = editEmailAddress.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Verifica daca campurile sunt completate
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Va rugam sa completati toate campurile!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Incearca autentificarea cu Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        navigateToMainActivity();  // Daca autentificarea reuseste
                    } else {
                        Toast.makeText(LoginActivity.this, "Login esuat. Verificati email-ul si parola.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Initiaza autentificarea prin Google.
     * Se deschide o activitate externa pentru login Google.
     */
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Preia rezultatul activitatii de login Google si incearca sa autentifice utilizatorul
     * cu Firebase folosind token-ul Google.
     *
     * @param requestCode codul cererii pentru autentificare
     * @param resultCode codul rezultatului cererii
     * @param data datele de intoarcere de la activitatea Google Sign-In
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Daca activitatea de login Google s-a finalizat
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);  // Autentificare cu Firebase folosind contul Google
            } catch (ApiException e) {
                Toast.makeText(this, "Autentificare Google esuata", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Autentifica utilizatorul cu Firebase folosind informatiile obtinute din Google Sign-In.
     * Daca autentificarea reuseste, se redirectioneaza utilizatorul la MainActivity.
     * Daca autentificarea esueaza, se afiseaza un mesaj de eroare.
     *
     * @param account contul Google semnat
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        // Creeaza credentialele Firebase din token-ul Google
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        navigateToMainActivity();  // Daca autentificarea reuseste
                    } else {
                        Toast.makeText(LoginActivity.this, "Autentificare Google esuata", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Redirectioneaza utilizatorul la MainActivity dupa autentificare.
     * Inchide activitatea curenta pentru a preveni revenirea la LoginActivity prin apasarea butonului Back.
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();  // Inchide LoginActivity
    }
}
