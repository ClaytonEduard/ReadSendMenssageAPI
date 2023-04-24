package com.claytoneduard.readsendmenssageapi;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.claytoneduard.readsendmenssageapi.model.Todos;
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
   // private Button btnRecuperar;
    private Retrofit retrofit;
    private ServiceTodos serviceTodos;
    private List<Todos> listTodos = new ArrayList<>();
    String url = "https://jsonplaceholder.typicode.com";

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

    }

    private void recuperarDados() {

        Call<List<Todos>> call = serviceTodos.recuperarDados();

        call.enqueue(new Callback<List<Todos>>() {
            @Override
            public void onResponse(Call<List<Todos>> call, Response<List<Todos>> response) {
                if (response.isSuccessful()) {
                    listTodos = response.body();
                    for (int i = 0; i < listTodos.size(); i++) {
                        Todos todos = listTodos.get(i);
                        Log.d("Resultado", "Resultado" + todos.getId() + " / " + todos.getTitle());
                        txtResultado.setText("Dados Capturados com sucesso \n ");
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Todos>> call, Throwable t) {

            }
        });

    }
}