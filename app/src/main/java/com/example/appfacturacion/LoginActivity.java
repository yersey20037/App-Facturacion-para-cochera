package com.example.appfacturacion;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_login =findViewById(R.id.btn_Login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            EditText edtemail= findViewById(R.id.editTextTextEmailAddress);
            EditText edtpassword = findViewById(R.id.editTextTextPassword);
            HttpLoggingInterceptor loggin =new HttpLoggingInterceptor();

            @Override
            public void onClick(View v) {
                String email=edtemail.getText().toString().trim();
                String password = edtpassword.getText().toString().trim();
                loggin.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder httpClient= new OkHttpClient.Builder();
                httpClient.addInterceptor(loggin);

                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl("https://geasacperu.com/apifac/public/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();
                ApiUser login =retrofit.create(ApiUser.class);
                Call<LoginResponse> call =login.LOGIN_CALL(email,password);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() !=null){
                            edtemail.getText().clear();
                            edtpassword.getText().clear();

                            String tokenInter= response.body().getMessage().getToken();
                            String nropos= response.body().getMessage().getNropos();
                            String id_estacion=response.body().getMessage().getId_estacion();
                            String turno= response.body().getMessage().getTurno();
                            String fechaproceso=response.body().getMessage().getFechaproceso();
                            Intent intent =new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("token",tokenInter);
                            intent.putExtra("nropos",nropos);
                            intent.putExtra("id_estacion",id_estacion);
                            intent.putExtra("turno",turno);
                            intent.putExtra("fechaproceso",fechaproceso);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this,"Bienvenido "+ nropos,Toast.LENGTH_SHORT).show();
                        } else if (response.code()==401) {
                            Toast.makeText(LoginActivity.this, "Debes validar tu correo", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "Error en las credenciales", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "LO SENTIMOS HUBO UN PROBLEMA INTERNO", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
    }
    //public void InitActivity(View view){
    //    Intent inicio = new Intent(this, MainActivity.class);
    //    startActivity(inicio);
    //};
}