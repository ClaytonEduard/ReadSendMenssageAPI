package com.claytoneduard.readsendmenssageapi.service;

import com.claytoneduard.readsendmenssageapi.model.Sms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceSms {
    @POST("/v1/send")
    Call<Sms> enviarSMS(@Body Sms sms);


}
