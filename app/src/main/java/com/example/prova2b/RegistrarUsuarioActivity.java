package com.example.prova2b;

import static com.example.prova2b.R.id.edt_email_conf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth userAuth;
    private EditText edt_email;
    private EditText edt_email_conf;
    private EditText edt_senha;
    private EditText edt_senha_conf;

    private CheckBox ckb_mostrar_senha;
    private Button btn_registrar;
    private Button btn_voltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        userAuth = FirebaseAuth.getInstance();
        edt_email = findViewById(R.id.edt_email);
        edt_email_conf = findViewById(R.id.edt_email_conf);
        edt_senha = findViewById(R.id.edt_senha);
        edt_senha_conf = findViewById(R.id.edt_senha_conf);
        ckb_mostrar_senha = findViewById(R.id.ckb_mostrar_senha);
        btn_registrar = findViewById(R.id.btn_registrar);
        btn_voltar = findViewById(R.id.btn_voltar);

        ckb_mostrar_senha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    edt_senha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edt_senha_conf.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edt_senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_senha_conf.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edt_email.getText().toString();
                String email_conf = edt_email_conf.getText().toString();
                String senha = edt_senha.getText().toString();
                String senha_conf = edt_senha_conf.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(email_conf) && !TextUtils.isEmpty(senha) && !TextUtils.isEmpty(senha_conf)) {
                    if (email.equals(email_conf)) {
                        if (senha.equals(senha_conf)) {
                            userAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegistrarUsuarioActivity.this, "Usuário registrado!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegistrarUsuarioActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegistrarUsuarioActivity.this, "Usuário não registrado. Verifique todos os dados!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegistrarUsuarioActivity.this, "Usuário não registrado. Senhas estão diferindo!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegistrarUsuarioActivity.this, "Usuário não registrado. E-mails estão diferindo!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegistrarUsuarioActivity.this, "Usuário não registrado. Verifique todos os dados!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrarUsuarioActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}