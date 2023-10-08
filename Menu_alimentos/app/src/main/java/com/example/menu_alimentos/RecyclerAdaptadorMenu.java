package com.example.menu_alimentos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;





public class RecyclerAdaptadorMenu extends RecyclerView.Adapter<RecyclerAdaptadorMenu.ViewHolder> {
    private List<ModeloMenu> userList;

    private OnButtonClickListener onButtonClickListener;

    public void setUserList(List<ModeloMenu> userList) {
        this.userList = userList;
    }
    public RecyclerAdaptadorMenu(List<ModeloMenu> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerviewmenu, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModeloMenu menu = userList.get(position);
        holder.textviewtipo.setText(menu.getTipo());
        holder.textviewnombre.setText(menu.getNombre());
        holder.textviewprecio.setText("$"+menu.getPrecio());


        holder.btnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onButtonClick(menu);
                }
            }
        });
        // holder.imageViewQRCode.setImageBitmap(user.getCodigoQR());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewtipo;
        public TextView textviewnombre;
        public TextView textviewprecio;
        public Button btnActivar;

        //public ImageView imageViewQRCode;
        public ViewHolder(View view) {
            super(view);
            textviewtipo = view.findViewById(R.id.textviewtipo);
            textviewnombre = view.findViewById(R.id.textviewnombre);
            textviewprecio = view.findViewById(R.id.textviewprecio);
            btnActivar = view.findViewById(R.id.btnAgregarorden);

        }
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClick(ModeloMenu menu);
    }

}

