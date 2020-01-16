package com.example.inventory.activity.main;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventory.R;
import com.example.inventory.model.Barang;

import java.util.List;

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.RecyclerViewAdapter> {

    private Context context;
    private List<Barang> barang;
    private ItemClickListener  itemClickListener;

    public AddAdapter(Context context, List<Barang> barang, ItemClickListener itemClickListener) {
        this.context = context;
        this.barang = barang;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inventory,
                parent, false);

        return new RecyclerViewAdapter(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
        Barang bar = barang.get(position);
        holder.tv_nama.setText(bar.getNama());
        holder.tv_jumlah.setText(bar.getJumlah());
        holder.tv_date.setText(bar.getTanggal());
        holder.card_item.setBackgroundColor(bar.getColor());
    }

    @Override
    public int getItemCount() {
        return barang.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView tv_nama, tv_jumlah, tv_date;
        CardView card_item;
        ItemClickListener itemClickListener;


         RecyclerViewAdapter(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            this.itemClickListener = itemClickListener;

            tv_nama = itemView.findViewById(R.id.nama);
            tv_jumlah = itemView.findViewById(R.id.jumlah);
            tv_date = itemView.findViewById(R.id.date);
            card_item = itemView.findViewById(R.id.card_item);

            this.itemClickListener = itemClickListener;
            card_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

             itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
