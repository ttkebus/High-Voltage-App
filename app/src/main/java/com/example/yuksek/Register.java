package com.example.yuksek;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText personelNum,email,passwd;
        TextView already;
        Button register;
        ProgressBar progressBar;

        final FirebaseAuth firebaseAuth;

        personelNum=(EditText) findViewById(R.id.personelnum);
        email=(EditText) findViewById(R.id.email);
        passwd=(EditText) findViewById(R.id.password);
        already=(TextView) findViewById(R.id.textView);
        register=(Button)findViewById(R.id.register);

        firebaseAuth=FirebaseAuth.getInstance();

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login=new Intent(Register.this, Login.class);
                startActivity(login);
            }
        });

       /* if(firebaseAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        } */

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr=email.getText().toString().trim();
                String passwordStr=passwd.getText().toString().trim();

                if (TextUtils.isEmpty(emailStr)){
                    email.setError("Bu alanı doldurmanız gerekmektedir.");
                    return;
                }

                if (TextUtils.isEmpty(passwordStr)){
                    passwd.setError("Bu alanı doldurmanız gerekmektedir.");
                    return;
                }

                if (passwordStr.length() < 6){
                    passwd.setError("Parola 6 karakterden fazla olmalıdır.");
                }

                firebaseAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "Kullanıcı başarılı bir şekilde oluşturuldu.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Home.class));
                        }
                        else{
                            Toast.makeText(Register.this, "Kullanıcı oluşturulamadı !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
