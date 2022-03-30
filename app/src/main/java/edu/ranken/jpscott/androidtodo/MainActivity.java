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

public class MainActivity extends AppCompatActivity {
    TextView tvGoToRegisterPage;
    EditText etEmailLogin;
    EditText etPasswordLogin;
    Button   btnLogin;
    private FirebaseAuth   auth;
    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvGoToRegisterPage = findViewById(R.id.tvGoToRegisterPage);
        etEmailLogin       = findViewById(R.id.etEmailLogin);
        etPasswordLogin    = findViewById(R.id.etPasswordLogin);
        btnLogin           = findViewById(R.id.btnLogin);

        auth   = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        tvGoToRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                                           RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email    = etEmailLogin.getText().toString().trim();
                String password = etPasswordLogin.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    etEmailLogin.setError("Email Required!");
                    etEmailLogin.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password))
                {
                    etPasswordLogin.setError("Password Required!");
                    etPasswordLogin.requestFocus();
                    return;
                }

                loader.setMessage("Login in progress");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    Intent intent = new Intent(MainActivity.this,
                                            SuccessActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }
                                else
                                {
                                    String error = task.getException().toString();
                                    Toast.makeText(MainActivity.this,
                                            "Login Failed\n" +
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