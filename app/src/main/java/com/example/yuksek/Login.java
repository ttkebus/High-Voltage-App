package com.example.yuksek;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText personelNum,passwd;
        TextView notaccount;
        Button login;
        final FirebaseAuth firebaseAuth;

        personelNum=(EditText) findViewById(R.id.email);
        passwd=(EditText) findViewById(R.id.password);
        notaccount=(TextView) findViewById(R.id.textView);
        login=(Button)findViewById(R.id.login);
        firebaseAuth=FirebaseAuth.getInstance();

        notaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login=new Intent(Login.this, Register.class);
                startActivity(login);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr=personelNum.getText().toString().trim();
                String passwordStr=passwd.getText().toString().trim();

                if (TextUtils.isEmpty(emailStr)){
                    personelNum.setError("Bu alanı doldurmanız gerekmektedir.");
                    return;
                }

                if (TextUtils.isEmpty(passwordStr)){
                    passwd.setError("Bu alanı doldurmanız gerekmektedir.");
                    return;
                }

                if (passwordStr.length() < 6){
                    passwd.setError("Parola 6 karakterden fazla olmalıdır.");
                }

                firebaseAuth.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Giriş başarılı ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Home.class));
                        }
                        else {
                            Toast.makeText(Login.this, "Giriş başarısız !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
