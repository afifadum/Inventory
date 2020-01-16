package com.example.inventory.activity.main;

import androidx.annotation.NonNull;

import com.example.inventory.api.ApiClient;
import com.example.inventory.api.ApiInterface;
import com.example.inventory.model.Barang;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPresenter {

    private AddView view;

    public AddPresenter(AddView view) {
        this.view = view;
    }

    void getData(){
        view.showLoading();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Barang>> call = apiInterface.getBarang();
        call.enqueue(new Callback<List<Barang>>() {
            @Override
            public void onResponse(@NonNull Call<List<Barang>> call, @NonNull Response<List<Barang>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null){
                    view.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Barang>> call, @NonNull Throwable t) {
                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });


    }
}
