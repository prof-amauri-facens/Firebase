package br.facens.aula.aulafinalautenticacaofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button createUserButton;
    private Button loginButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        createUserButton = findViewById(R.id.createUserButton);
        resultTextView = findViewById(R.id.resultTextView);

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });

        // Adicionar um botão para abrir a nova atividade
        Button newUserInfoButton = findViewById(R.id.newUserInfoButton);
        newUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewUserInfoActivity();
            }
        });
    }

    private void createUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        resultTextView.setText("Espere");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            resultTextView.setText("Sucesso");
                        } else {
                            resultTextView.setText("Falha: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void openNewUserInfoActivity() {
        Intent intent = new Intent(this, NewUserInfoActivity.class);
        startActivity(intent);
    }
}
