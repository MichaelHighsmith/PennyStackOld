package com.satyrlabs.pennystack;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaxService {

    @GET("/api/v2/state/2019/{State}")
    public Observable<StateTaxResponse> getTaxInfo(@Path("State") String state);

    @GET("/api/v2/federal/2019")
    public Observable<StateTaxResponse> getFederalTaxInfo();

}
