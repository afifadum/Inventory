package com.example.inventory.api;

import com.example.inventory.model.Barang;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("save.php")
    Call<Barang> saveBarang(
        @Field("nama") String nama,
        @Field("jumlah") String jumlah,
        @Field("color") int color
    );

    @GET("barang.php")
    Call<List<Barang>> getBarang();

    @FormUrlEncoded
    @POST("update.php")
    Call<Barang> updateBarang(
            @Field("id") int id,
            @Field("nama") String nama,
            @Field("jumlah") String jumlah,
            @Field("color") int color
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<Barang> deleteBarang( @Field("id") int id);
}
