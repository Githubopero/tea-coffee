package com.example.teacoffee.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacoffee.R;
import com.example.teacoffee.myDB.FoodCategory; // Sửa
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> { // Sửa

    public interface OnItemClickListener {
        void onItemClick(FoodCategory category, int position); // Sửa
    }

    private List<FoodCategory> list = new ArrayList<>(); // Sửa
    private final OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public CategoryAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submit(List<FoodCategory> newList) { // Sửa
        list.clear();
        list.addAll(newList);
        selectedPosition = RecyclerView.NO_POSITION;
        notifyDataSetChanged();
    }

    public FoodCategory getSelected() { // Sửa
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return list.get(selectedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sửa layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        FoodCategory category = list.get(position); // Sửa
        holder.tvId.setText("ID: " + category.Food_Category_Id); // Sửa
        holder.tvName.setText(category.Food_Category_Name); // Sửa

        if (position == selectedPosition) {
            holder.container.setBackgroundColor(Color.parseColor("#E0E0E0"));
        } else {
            holder.container.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(v -> {
            int oldSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(oldSelected);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onItemClick(category, selectedPosition); // Sửa
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView tvId, tvName; // Sửa
        LinearLayout container;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvCategoryId); // Sửa
            tvName = itemView.findViewById(R.id.tvCategoryName); // Sửa
            container = itemView.findViewById(R.id.rowContainer);
        }
    }
}