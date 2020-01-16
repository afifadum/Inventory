package com.example.inventory.activity.main;

import com.example.inventory.model.Barang;

import java.util.List;

public interface AddView {

    void showLoading();
    void hideLoading();
    void onGetResult(List<Barang> barang);
    void onErrorLoading (String message);
}
