package com.theradikalsoftware.alertacovid_19nl.main.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theradikalsoftware.alertacovid_19nl.R;
import com.theradikalsoftware.alertacovid_19nl.retrofit.models.InsideJSONModel;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<InsideJSONModel> mDataSet;

    public MyAdapter(List<InsideJSONModel> data) {
        mDataSet = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView municipioTXV, casosTXV;

        public MyViewHolder(View v) {
            super(v);
            casosTXV = v.findViewById(R.id.recyclerviewitem_textview_casos);
            municipioTXV = v.findViewById(R.id.recyclerviewitem_textview_municipio);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_map_datadetail_recyclerview_item, parent, false);

        return new MyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.municipioTXV.setText(mDataSet.get(position).getMunicipio());
        holder.casosTXV.setText(String.valueOf(mDataSet.get(position).getCasosconfirmados()));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}