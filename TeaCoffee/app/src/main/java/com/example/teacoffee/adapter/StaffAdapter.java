package com.example.teacoffee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.teacoffee.R;
import com.example.teacoffee.myDB.Account;
import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.VH> {

    public interface OnItemClick {
        void onClick(Account a, int position);
    }

    private final List<Account> data = new ArrayList<>();
    private int selected = RecyclerView.NO_POSITION;
    private final OnItemClick listener;

    public StaffAdapter(OnItemClick l) {
        this.listener = l;
    }

    // Nạp dữ liệu mới
    public void submit(List<Account> list) {
        data.clear();
        if (list != null) data.addAll(list);
        selected = RecyclerView.NO_POSITION; // reset selection khi submit
        notifyDataSetChanged();
    }

    // Lấy row đang chọn
    public Account getSelected() {
        if (selected >= 0 && selected < data.size()) return data.get(selected);
        return null;
    }

    // Clear selection (dùng khi đổi tab / mode / filter)
    public void clearSelection() {
        int old = selected;
        selected = RecyclerView.NO_POSITION;
        if (old != RecyclerView.NO_POSITION) notifyItemChanged(old);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staff, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Account a = data.get(pos);

        // Dòng 1: nvID | Tên hiển thị
        h.tvId.setText("nv" + a.User_Id);
        h.tvDisplayName.setText(a.Display_Name != null ? a.Display_Name : "");

        // Dòng 2: Tên tài khoản | Chức vụ
        h.tvUsername.setText(a.User_Name != null ? a.User_Name : "");
        h.tvRole.setText(a.Type != null ? a.Type.toUpperCase() : "STAFF");

        // Highlight row khi row được chọn
        h.itemView.setActivated(selected == pos);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView tvId, tvDisplayName, tvUsername, tvRole;

        VH(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvRowId);
            tvDisplayName = itemView.findViewById(R.id.tvRowDisplayName);
            tvUsername = itemView.findViewById(R.id.tvRowUsername);
            tvRole = itemView.findViewById(R.id.tvRowRole);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                int old = selected;
                selected = pos;
                if (old != RecyclerView.NO_POSITION) notifyItemChanged(old);
                notifyItemChanged(selected);

                if (listener != null) listener.onClick(data.get(selected), selected);
            });
        }
    }
}
