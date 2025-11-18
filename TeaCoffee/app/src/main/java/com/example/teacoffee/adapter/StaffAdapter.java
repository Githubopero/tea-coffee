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

    public void submit(List<Account> list) {
        data.clear();
        if (list != null) data.addAll(list);
        selected = RecyclerView.NO_POSITION;
        notifyDataSetChanged();
    }

    public Account getSelected() {
        if (selected >= 0 && selected < data.size()) return data.get(selected);
        return null;
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
        h.tvId.setText("nv" + a.User_Id);
        h.tvUser.setText(a.User_Name != null ? a.User_Name : "");
        h.tvDisplay.setText(a.Display_Name != null ? a.Display_Name : "");
        h.tvNote.setText(a.Type != null ? a.Type : "");

        // ✅ KÍCH HOẠT NỀN (state_activated)
        h.itemView.setActivated(selected == pos);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView tvId, tvUser, tvDisplay, tvNote;

        VH(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvRowId);
            tvUser = itemView.findViewById(R.id.tvRowUser);
            tvDisplay = itemView.findViewById(R.id.tvRowDisplay);
            tvNote = itemView.findViewById(R.id.tvRowNote);

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
