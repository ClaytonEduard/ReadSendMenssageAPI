package com.claytoneduard.readsendmenssageapi.service;

import com.claytoneduard.readsendmenssageapi.model.Todos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceTodos {

    @GET("/todos")
    Call<List<Todos>> recuperarDados();

}
