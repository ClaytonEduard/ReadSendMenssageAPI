package com.claytoneduard.readsendmenssageapi;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.claytoneduard.readsendmenssageapi.model.Sms;
import com.claytoneduard.readsendmenssageapi.model.Todos;
import com.claytoneduard.readsendmenssageapi.service.ServiceSms;
import com.claytoneduard.readsendmenssageapi.service.ServiceTodos;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView txtResultado;
    private Retrofit retrofit;
    private Retrofit retrofitSMS;
    private ServiceTodos serviceTodos;

    private ServiceSms serviceSms;
    private List<Todos> listTodos = new ArrayList<>();
    String url = "https://jsonplaceholder.typicode.com";
    String urlSms = "https://api.smsempresa.com.br";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtResultado = findViewById(R.id.textResultado);
        //btnRecuperar = findViewById(R.id.buttonCapturarDados);

        //passando a URl para o retrofit
        retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        serviceTodos = retrofit.create(ServiceTodos.class);
        try {
            sleep(3000);
            recuperarDados();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        retrofitSMS = new Retrofit.Builder().baseUrl(urlSms).addConverterFactory(GsonConverterFactory.create()).build();
        serviceSms = retrofitSMS.create(ServiceSms.class);

    }

    private void recuperarDados() {

        Call<List<Todos>> call = serviceTodos.recuperarDados();

        call.enqueue(new Callback<List<Todos>>() {
            @Override
            public void onResponse(Call<List<Todos>> call, Response<List<Todos>> response) {
                Todos todos = new Todos();
                if (response.isSuccessful()) {
                    listTodos = response.body();
                    for (int i = 0; i < listTodos.size(); i++) {
                        todos = listTodos.get(i);
                        Log.d("Resultado", "Resultado" + todos.getId() + " / " + todos.getTitle());
                        txtResultado.setText("Dados Capturados da API com sucesso!");
                    }
                }
                if(enviarSMS(todos.getTitle())) {
                   Toast.makeText(MainActivity.this, "Envio de SMS realizado! \n " + todos.getTitle(), Toast.LENGTH_LONG).show();
                }else if(!enviarSMS(todos.getTitle())){
                    Toast.makeText(MainActivity.this, "Erro ao enviar SMS", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Todos>> call, Throwable t) {
                Log.e("API Service  ", "Erro ao consultar API  " + t.getMessage());
            }
        });

    }

    // Enviar mensagem

    private boolean enviarSMS(String msg) {
        // key disponibilizada pelo site, essa chave posso usar somente 10 envios de testes.
        String key = "BKAVQ8WFL58HNJJR0287K2JN20WVUEDVAIQPQW4UEWDV7HJIOZ9EMFVDK6QLNAAWOZ7EGNLFZM2SK26QZ59GT99VPHBD7EQ2VL9O54XNLC76I14KC02GKJRDGISO2BHU";
        Sms sms = new Sms(key, 9, 64992118865L, "Consegui fazer um esboso do projeto!");

        Call<Sms> call = serviceSms.enviarSMS(sms);
        call.enqueue(new Callback<Sms>() {
            @Override
            public void onResponse(Call<Sms> call, Response<Sms> response) {
                if (response.isSuccessful()) {
                    Sms smsEnvio = response.body();
                    txtResultado.setText("Código: " + response.code() +
                            "Numero: " + smsEnvio.getNumber() + " Mensagem: " + smsEnvio.getMsg());
                    return;
                }
            }

            @Override
            public void onFailure(Call<Sms> call, Throwable t) {
                Log.e("SMS Service  ", "Erro ao enviar sms" + t.getMessage());
            }
        });
        return true;
    }


}