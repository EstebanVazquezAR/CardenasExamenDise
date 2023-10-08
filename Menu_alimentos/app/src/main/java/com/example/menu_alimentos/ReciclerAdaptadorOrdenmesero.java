package com.example.menu_alimentos;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReciclerAdaptadorOrdenmesero extends RecyclerView.Adapter<ReciclerAdaptadorOrdenmesero.ViewHolder> {
    private List<ModeloOrdenMesero> userList;

    private OnButtonClickListener2 onButtonClickListener2;

    public void setUserList(List<ModeloOrdenMesero> userList) {
        this.userList = userList;
    }
    public ReciclerAdaptadorOrdenmesero(List<ModeloOrdenMesero> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_mesero_ordenes, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModeloOrdenMesero menu = userList.get(position);
        holder.textviewMESA.setText("Mesa: "+menu.getMesa());
        holder.textviewPEDIDO.setText(menu.getPedido());
        holder.textviewTOTAL.setText("$"+menu.getTotal());


        holder.btnaUTORIZARORDEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener2 != null) {
                    onButtonClickListener2.onButtonClick(menu);
                }
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearList() {
        userList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewMESA;
        public TextView textviewPEDIDO;
        public TextView textviewTOTAL;
        public Button btnaUTORIZARORDEN;

        //public ImageView imageViewQRCode;
        public ViewHolder(View view) {
            super(view);
            textviewMESA = view.findViewById(R.id.textviewMESA);
            textviewPEDIDO = view.findViewById(R.id.textviewPEDIDO);
            textviewTOTAL = view.findViewById(R.id.textviewTOTAL);
            btnaUTORIZARORDEN = view.findViewById(R.id.btnaUTORIZARORDEN);

        }
    }

    public void setOnButtonClickListener(OnButtonClickListener2 listener) {
        this.onButtonClickListener2 = listener;
    }

    public interface OnButtonClickListener2 {
        void onButtonClick(ModeloOrdenMesero menu);
    }

}

