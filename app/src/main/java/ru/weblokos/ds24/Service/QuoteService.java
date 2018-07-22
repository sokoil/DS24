package ru.weblokos.ds24.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuoteService {
    public final static int DEFAULT_LIMIT = 10;
    private Retrofit retrofit = null;

    public QuoteAPI getAPI() {
        String BASE_URL = "http://ds24.ru/test/";

        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(QuoteAPI.class);
    }
}
