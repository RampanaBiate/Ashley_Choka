package com.kailawn.recipe;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button login;
    TextView reg;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    LottieAnimationView lottie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password_id);
        login = findViewById(R.id.login_id);
        reg = findViewById(R.id.reg_id);
        auth = FirebaseAuth.getInstance();

        lottie= findViewById(R.id.animationView);
        lottie.playAnimation();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    email.setError("I email enter rawh");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("I password enter rawh");

                } else {
                    getUserData(email.getText().toString(),password.getText().toString());
                }
            }

        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }


    private void getUserData(String email, String password) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Nghak lawk rawh");// email a check vek ta a loading screen
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {///
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.hide();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finishAffinity();

                    } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Password a dik lo.",
                                    Toast.LENGTH_SHORT).show();
                            }
                        }
                });



        }
    }
