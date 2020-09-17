package com.example.yuksek;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static android.graphics.Color.parseColor;

public class Home extends AppCompatActivity {



    Button button,cikis;
    TextView akim,gerilim,durum;
    RelativeLayout relativeLayout;
    private static final int READ_REQUEST_CODE = 42;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);

        }
        button = (Button) findViewById(R.id.button);
        cikis=(Button) findViewById(R.id.button3);
        durum=(TextView)findViewById(R.id.durum);
        akim=(TextView) findViewById(R.id.akim);
        gerilim=(TextView)findViewById(R.id.gerilim);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/digit.TTF");
        gerilim.setTypeface(typeface);
        akim.setTypeface(typeface);
        relativeLayout=(RelativeLayout)findViewById(R.id.rela);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(isConnected()){

                button.setText("DEĞERLERİ AL");
                Toast.makeText(getApplicationContext(), "USB bağlantısı başarılı", Toast.LENGTH_SHORT).show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isConnected()) {
                        button.setText("DEĞERLERİ AL");
                        Toast.makeText(getApplicationContext(), "USB bağlantısı başarılı", Toast.LENGTH_SHORT).show();

                            if (readAkim().toString().matches("0") && readVolt().toString().contains("100")) {
                                akim.setText(readAkim());
                                gerilim.setText(readVolt());
                                durum.setText("Hücrede güvenli bir şekilde İŞLEM YAPABİLİRSİNİZ ");
                                startCountAkimAnimation(0);
                                startCountVoltAnimation(36000);
                                startColorAnimation(relativeLayout, 2);
                            } else if (readAkim().toString().matches("5") && readVolt().toString().contains("100")) {
                                akim.setText(readAkim());
                                gerilim.setText(readVolt());
                                durum.setText("Bu hücrede İŞLEM YAPILAMAZ ! Yanlış hücre !");
                                startCountAkimAnimation(16000);
                                startCountVoltAnimation(36000);
                                startColorAnimation(relativeLayout, 1);
                            }   else if (readVolt().toString().contains("")){
                                Toast.makeText(Home.this, "Gerilim değerlerinin okunacağı dosya bulunamadı !", Toast.LENGTH_SHORT).show();
                                durum.setText("Dosya bulunamadı ! İŞLEM YAPILAMAZ !");
                                akim.setText("");
                                gerilim.setText("");
                                startColorAnimation(relativeLayout, 3);
                            }
                        } else if (!isConnected()){
                            button.setText("BAĞLAN");
                            Toast.makeText(Home.this, "USB bağlantısına ulaşılamıyor!", Toast.LENGTH_SHORT).show();
                            durum.setText("Bağlantı sağlanamadı ! İŞLEM YAPILAMAZ !");
                            akim.setText("");
                            gerilim.setText("");
                            startColorAnimation(relativeLayout, 3);
                            }
                    }
                });
            } else if (!isConnected()){


                Toast.makeText(Home.this, "USB bağlantısına ulaşılamıyor!", Toast.LENGTH_SHORT).show();
                button.setText("BAĞLAN");
                durum.setText("Bağlantı sağlanamadı ! İŞLEM YAPILAMAZ !");
                akim.setText("");
                gerilim.setText("");
                startColorAnimation(relativeLayout, 3);
            }

}
        });

    cikis.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Home.this,MainActivity.class));
        }
    });


    }



    private StringBuilder readAkim() {

        InputStream input=getResources().openRawResource(R.raw.akim);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(input));
        String line="";
        StringBuilder stringBuilder=new StringBuilder();

        try{
            while ((line=bufferedReader.readLine())!= null){
                stringBuilder.append(line);
            }
        }catch (IOException e){
            Toast.makeText(this, "Akım değerlerinin okunacağı dosya bulunamadı !", Toast.LENGTH_SHORT).show();
        }
        return stringBuilder;
    }

    private StringBuilder readVolt() {

        InputStream input=getResources().openRawResource(R.raw.gerilim);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(input));
        String line="";
        StringBuilder stringBuilder=new StringBuilder();

        try{
            if ((line=bufferedReader.readLine())!= null){
                stringBuilder.append(line);
            } else {
                Toast.makeText(this, "Gerilim değerlerinin okunacağı dosya bulunamadı !", Toast.LENGTH_SHORT).show();
            }
        }catch (IOException e){
            Toast.makeText(this, "Gerilim değerlerinin okunacağı dosya bulunamadı !", Toast.LENGTH_SHORT).show();
        }
        return stringBuilder;
    }

    private String readAkimFromFile() {

        String ret = "";

        try {

            FileInputStream in = openFileInput("akim.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            inputStreamReader.close();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, "Akım Dosyası bulunamadı", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private String readVoltFromFile() {

        String ret = "";

        try {

            FileInputStream in = openFileInput("/storage/sdcard/gerilim.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            inputStreamReader.close();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, "Gerilim Dosyası bulunamadı", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void startCountAkimAnimation(int limit) {

        ValueAnimator animator = ValueAnimator.ofInt(0, limit);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                akim.setText(animation.getAnimatedValue().toString());

            }

        });
        animator.start();

    }
    private void startCountVoltAnimation(int limit) {

        ValueAnimator animator = ValueAnimator.ofInt(0, limit);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                gerilim.setText(animation.getAnimatedValue().toString());

            }

        });
        animator.start();

    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startColorAnimation(View view,int color){

        int colorStart=view.getSolidColor();

        if (color==1){
        int colorEnd=parseColor("#ff0000");

        ValueAnimator valueAnimator= ObjectAnimator.ofInt(view,"backgroundColor",colorStart,colorEnd);
        valueAnimator.setDuration(2000);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setRepeatCount(4);
        valueAnimator.setRepeatMode(valueAnimator.REVERSE);
        valueAnimator.start();}

        else if(color==2){
            int colorEnd=parseColor("#00ff00");
            ValueAnimator valueAnimator= ObjectAnimator.ofInt(view,"backgroundColor",colorStart,colorEnd);
            valueAnimator.setDuration(2000);
            valueAnimator.setEvaluator(new ArgbEvaluator());
            valueAnimator.setRepeatCount(4);
            valueAnimator.setRepeatMode(valueAnimator.REVERSE);
            valueAnimator.start();
        }else if(color==3){
            int colorEnd=parseColor("#b3b2af");
            ValueAnimator valueAnimator= ObjectAnimator.ofInt(view,"backgroundColor",colorStart,colorEnd);
            valueAnimator.setDuration(2000);
            valueAnimator.setEvaluator(new ArgbEvaluator());
            valueAnimator.setRepeatCount(4);
            valueAnimator.setRepeatMode(valueAnimator.REVERSE);
            valueAnimator.start();
        }

    }

    private void performFileSearh() {

      /*  Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);*/
        //Toast.makeText(this, "USB başarılı bir şekilde bağlandı", Toast.LENGTH_SHORT).show();

        ValueAnimator valueAnimator=ObjectAnimator.ofInt();
        valueAnimator.setDuration(2000);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Dosya okuma izni verildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Dosya okuma izni verilmedi", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public boolean isConnected() {
        while(true){
        Intent intent = registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        return intent.getExtras().getBoolean("connected");
    }}

}