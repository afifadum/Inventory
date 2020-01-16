package com.example.inventory.activity.editor;

import androidx.annotation.NonNull;

import com.example.inventory.api.ApiClient;
import com.example.inventory.api.ApiInterface;
import com.example.inventory.model.Barang;
import com.google.android.gms.common.api.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorPresenter {

    private EditorView view;

    public EditorPresenter(EditorView view) {
        this.view = view;
    }

     void saveInventory(final String nama, final String jumlah, final int color) {
        view.showProgress();

        ApiInterface apiInterface = ApiClient.getApiClient()
                .create(ApiInterface.class);
        Call<Barang> call = apiInterface.saveBarang(nama, jumlah, color);

        call.enqueue(new Callback<Barang>() {
            @Override
            public void onResponse(@NonNull Call<Barang> call, @NonNull Response<Barang> response) {
                view.hideProgress();

                if (response.isSuccessful()&& response.body() != null){

                    Boolean success = response.body().getSuccess();
                    if (success){
                        view.onRequestSuccess(response.body().getMessage());

                    }
                    else{
                        view.onRequestError(response.body().getMessage());
//                        Toast.makeText(EditorActivity.this,
//                                response.body().getMessage(),
//                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Barang> call, @NonNull Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }

    void updateBarang(int id, String nama, String jumlah, int color){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Barang> call = apiInterface.updateBarang(id, nama, jumlah, color);
        call.enqueue(new Callback<Barang>() {
            @Override
            public void onResponse(@NonNull Call<Barang> call, @NonNull Response<Barang> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null){
                    Boolean success = response.body().getSuccess();
                    if (success){
                        view.onRequestSuccess(response.body().getMessage());
                    } else{
                        view.onRequestError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Barang> call,@NonNull Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }

    void deleteBarang(int id){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Barang> call = apiInterface.deleteBarang(id);
        call.enqueue(new Callback<Barang>() {
            @Override
            public void onResponse(@NonNull Call<Barang> call, @NonNull Response<Barang> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body()!= null){
                    Boolean success = response.body().getSuccess();
                    if (success){
                        view.onRequestSuccess(response.body().getMessage());
                    } else{
                        view.onRequestError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Barang> call,@NonNull Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }

}
