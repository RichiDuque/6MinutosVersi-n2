package com.venta.a6minutosapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class minutoAdapter extends RecyclerView.Adapter<minutoAdapter.MyViewHolder> {

    Context mContext;
    static List<dataMinuto> mData;

    public minutoAdapter(Context mContext,List<dataMinuto> mData){
        this.mContext=mContext;
        this.mData=mData;
    }

    @NonNull
    @Override
    public minutoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater =LayoutInflater.from(mContext);
        v= inflater.inflate(R.layout.minuto_model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull minutoAdapter.MyViewHolder holder, int position) {
        holder.txhminuto.post(()->{holder.txhminuto.setText(mData.get(position).getMinuto()+"");});
        holder.txhFC.post(()->{holder.txhFC.setText(mData.get(position).getFC()+"");});
        holder.txhSat.post(()->{holder.txhSat.setText(mData.get(position).getSaturacion()+"");});
        holder.txhTas.post(()->{holder.txhTas.setText(mData.get(position).getTAS()+"");});
        holder.txhTad.post(()->{holder.txhTad.setText(mData.get(position).getTAD()+"");});
        holder.txhMMII.post(()->{holder.txhMMII.setText(mData.get(position).getMMII()+"");});
        holder.txhDisnea.post(()->{holder.txhDisnea.setText(mData.get(position).getDisnea());});
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txhminuto, txhFC,txhSat,txhTas,txhTad,txhMMII,txhDisnea;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txhminuto=itemView.findViewById(R.id.textMinutoModel);
            txhFC=itemView.findViewById(R.id.textFCModel);
            txhSat=itemView.findViewById(R.id.textSatModel);
            txhTas=itemView.findViewById(R.id.textTasModel);
            txhTad=itemView.findViewById(R.id.textTadModel);
            txhMMII=itemView.findViewById(R.id.textMMIIModel);
            txhDisnea=itemView.findViewById(R.id.textDisneaModel);

        }
    }
}
