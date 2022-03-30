package edu.ranken.jpscott.androidtodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private TextView tvEmailRegister;
    private EditText etEmailRegister;
    private EditText etPasswordRegister;
    private EditText etPasswordConfirm;
    private Button   btnRegister;
    private FirebaseAuth auth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //  Get references to widgets
        tvEmailRegister    = findViewById(R.id.tvRegister);
        etEmailRegister    = findViewById(R.id.etEmailRegister);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
        etPasswordConfirm  = findViewById(R.id.etPasswordConfirm);
        btnRegister        = findViewById(R.id.btnLogin);

        auth   = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        tvEmailRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,
                                           SuccessActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email            = etEmailRegister.getText().toString().trim();
                String password         = etPasswordRegister.getText().toString().trim();
                String passwordConfirm  = etPasswordConfirm.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    etEmailRegister.setError("Email Required!");
                    etEmailRegister.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password))
                {
                    etPasswordRegister.setError("Password Required!");
                    etPasswordRegister.requestFocus();
                    return;
                }

                if (!passwordConfirm.equals(password))
                {
                    etPasswordConfirm.setError("Confirm Password Must Equal Password!");
                    etPasswordConfirm.setText("");
                    etPasswordConfirm.requestFocus();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    Intent intent = new Intent(RegisterActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }
                                else
                                {
                                    String error = task.getException().toString();
                                    Toast.makeText(RegisterActivity.this,
                                                  "Registration Failed\n" +
                                                  error, Toast.LENGTH_LONG).show();
                                    loader.dismiss();
                                }
                            }
                        }
                );
            }
        });
    }
}