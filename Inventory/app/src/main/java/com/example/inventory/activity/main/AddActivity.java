package com.example.inventory.activity.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.inventory.R;
import com.example.inventory.activity.editor.EditorActivity;
import com.example.inventory.model.Barang;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddActivity extends AppCompatActivity implements AddView{

    private static final int INTENT_EDIT = 200 ;
    private static final int INTENT_ADD = 100;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    Menu actionMenu;

    AddPresenter presenter;
    AddAdapter adapter;
    AddAdapter.ItemClickListener itemClickListener;

    List<Barang> barang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        swipeRefresh = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.add);
        fab.setOnClickListener(view ->
                startActivityForResult(
                        new Intent(this, EditorActivity.class),
                        INTENT_ADD)
                );

        presenter = new AddPresenter(this);
        presenter.getData();

        swipeRefresh.setOnRefreshListener(
                ()->presenter.getData()
        );

        itemClickListener = ((view, position) ->{

            int id = barang.get(position).getId();
            String nama = barang.get(position).getNama();
            String jumlah = barang.get(position).getJumlah();
            int color = barang.get(position).getColor();

            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("nama", nama);
            intent.putExtra("jumlah", jumlah);
            intent.putExtra("color", color);
            startActivityForResult(intent, INTENT_EDIT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_ADD && requestCode == RESULT_OK){
            presenter.getData(); //reload data
        }
        else if (requestCode == INTENT_EDIT && requestCode == RESULT_OK){
            presenter.getData();
        }
    }

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetResult(List<Barang> barangs) {
        adapter = new AddAdapter(this, barangs, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        barang = barangs;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home , menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MainActivity main = new MainActivity();
        if (item.getItemId() == R.id.logout){
            main.Logout();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
