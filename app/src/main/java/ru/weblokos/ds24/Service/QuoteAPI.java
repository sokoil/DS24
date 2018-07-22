package ru.weblokos.ds24.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface QuoteAPI {

    @GET("list.php")
    Call<QuotesListResponse> getQuotes(@Query("offset") int offset, @Query("limit") int limit);

    @GET("detail.php")
    Call<QuotesListResponse> getQuote(@Query("id") int quote);

}
