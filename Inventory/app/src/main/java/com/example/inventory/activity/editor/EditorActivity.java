package com.example.inventory.activity.editor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventory.R;
import com.example.inventory.api.ApiClient;
import com.example.inventory.api.ApiInterface;
import com.example.inventory.model.Barang;
import com.thebluealliance.spectrum.SpectrumPalette;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity implements  EditorView  {

    EditText edtNama, edtJumlah;
    ProgressDialog progressDialog;
    SpectrumPalette palette;

    EditorPresenter presenter;

    ApiInterface apiInterface;

    int color, id;
    String nama, jumlah;

    Menu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        edtNama = findViewById(R.id.nama);
        edtJumlah = findViewById(R.id.jumlah);
        palette = findViewById(R.id.palette);

        palette.setOnColorSelectedListener(
                clr -> color = clr
        );


//      Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon tunggu...");

        presenter = new EditorPresenter(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        nama = intent.getStringExtra("nama");
        jumlah = intent.getStringExtra("jumlah");
        color = intent.getIntExtra("color", 0);

        setDataFromIntentExtra();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor , menu);
        actionMenu = menu;

        if (id != 0){
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
        }else{
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nama  = edtNama.getText().toString().trim();
        String jumlah  = edtJumlah.getText().toString().trim();
        int color  = this.color;

        switch (item.getItemId()){
            case R.id.save:
                //Save
                if (nama.isEmpty()){
                    edtNama.setError("Tolong isikan nama");
                }else if (jumlah.isEmpty())
                    edtJumlah.setError("Tolong isikan jumlah");
                else {
                    presenter.saveInventory(nama, jumlah, color);
                }
                return true;

            case R.id.edit:

                editMode();
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);

                return  true;
            case R.id.update:
                //Update
                if (nama.isEmpty()){
                    edtNama.setError("Tolong isikan nama");
                }else if (jumlah.isEmpty())
                    edtJumlah.setError("Tolong isikan jumlah");
                else {
                    presenter.updateBarang(id, nama, jumlah, color);
                }
                return true;

            case R.id.delete:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Confirm!");
                alertDialog.setMessage("Yakin ?");
                alertDialog.setNegativeButton("YES", (dialog, which) -> {
                    dialog.dismiss();
                    presenter.deleteBarang(id);
                });
                alertDialog.setPositiveButton("Cancel",(
                        dialog, which)-> dialog.dismiss());
                alertDialog.show();
                return  true;
                default:
                    return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onRequestSuccess(String message) {
        Toast.makeText(EditorActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
        finish(); //kembali ke mainactivity
    }

    @Override
    public void onRequestError(String message) {
        Toast.makeText(EditorActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
        finish(); //kembali ke mainactivity
    }

    private void setDataFromIntentExtra() {
        if (id != 0){
            edtNama.setText(nama);
            edtJumlah.setText(jumlah);
            palette.setSelectedColor(color);

            getSupportActionBar().setTitle("Update Inventory");
            readMode();
        } else{
            // Default Color Setup
            palette.setSelectedColor(getResources().getColor(R.color.white));
            color = getResources().getColor(R.color.white);
            editMode();
        }
    }

    private void editMode() {
        edtNama.setFocusableInTouchMode(true);
        edtJumlah.setFocusableInTouchMode(true);
        palette.setEnabled(true);
    }

    private void readMode() {
        edtNama.setFocusableInTouchMode(false);
        edtJumlah.setFocusableInTouchMode(false);
        edtNama.setFocusable(false);
        edtJumlah.setFocusable(false);

        palette.setEnabled(false);
    }
}
