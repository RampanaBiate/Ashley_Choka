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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText repassword;
    Button reg_btn;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    String warning_text;
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email= findViewById(R.id.reg_email_id);
        password = findViewById(R.id.reg_password_id);
        repassword = findViewById(R.id.reg_repassword_id);
        reg_btn= findViewById(R.id.regpage_id);
        firestore= FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();

        lottie= findViewById(R.id.animationView_reg);
        lottie.playAnimation();

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")){
                    email.setError("I email enter rawh");
                } else if (password.getText().toString().equals("")) {
                    password.setError("i password enter rawh");
                } else if(repassword.getText().toString().equals("")){
                    repassword.setError("I password enter leh rawh");
                } else if (!password.getText().toString().equals(repassword.getText().toString())) {
                    Toast.makeText(RegisterActivity.this,"Password a in milh lo", Toast.LENGTH_SHORT).show();

                } else {
                    getUserData(email.getText().toString(),password.getText().toString());
                }
            }
        });
    }

    private void getUserData(String email, String password) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Nghak lawk rawh");// email a check vek ta a loading screen
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.hide();
                        if (task.isSuccessful()){
                            HashMap<String, Object> hashMap= new HashMap<>();
                            hashMap.put("Name", "");
                            hashMap.put("email", email);
                            hashMap.put("Phone_no","");
                            hashMap.put("uid",auth.getCurrentUser().getUid());

                            firestore.collection("user_profile").document(auth.getCurrentUser().getUid())
                                    .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.hide();
                                            Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class)
                                                    .putExtra("m", email);
                                            startActivity(intent);
                                            finishAffinity();
                                            Log.d(TAG, "createUserWithEmail: Success");
                                        }

                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.hide();
                                            Toast.makeText(RegisterActivity.this, "Ti nawn leh rawh", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                progressDialog.dismiss();
                                warning_text = "Account a awm tawh, Login rawh";
                                showWarningDialog(warning_text);
                            } else {
                                progressDialog.dismiss();
                                warning_text = "Resgitration a tih theih loh";
                                showWarningDialog(warning_text);
                            }
                        }
                    }
                });
    }

    private void showWarningDialog(String warningText) {
        Toast.makeText(this, warningText, Toast.LENGTH_SHORT).show();
    }
}