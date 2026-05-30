package com.mindspace.app.network;

import com.mindspace.app.data.model.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuoteApiService {
    
    @GET("quotes/random")
    Call<List<Quote>> getRandomQuote(@Query("limit") int limit);
    
    @GET("quotes")
    Call<List<Quote>> getQuotesByCategory(@Query("category") String category, @Query("limit") int limit);
}
