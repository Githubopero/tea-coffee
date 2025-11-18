package com.example.teacoffee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.teacoffee.R;
import com.example.teacoffee.myDB.Bill;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private List<Bill> billList;
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public BillAdapter(List<Bill> billList) {
        this.billList = billList;
    }

    public void setBillList(List<Bill> newBillList) {
        this.billList = newBillList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);

        holder.tvBillId.setText(String.valueOf(bill.Bill_Id));

        if (bill.Date_Checkin != null) {
            holder.tvDateCheckin.setText(dateFormat.format(bill.Date_Checkin));
        } else {
            holder.tvDateCheckin.setText("--/--/----");
        }

        if (bill.Date_Checkout != null) {
            holder.tvDateCheckout.setText(dateFormat.format(bill.Date_Checkout));
        } else {
            holder.tvDateCheckout.setText("...");
        }

        // Đã bỏ NumberFormat vì không có import, sử dụng String.format để đơn giản hóa
        holder.tvTotalPrice.setText(String.format("%,d VNĐ", bill.Total_Price != null ? bill.Total_Price : 0));

        holder.tvStatus.setText(bill.Status);
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    // ✅ SỬA LỖI: THÊM public vào đây
    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvBillId, tvDateCheckin, tvDateCheckout, tvTotalPrice, tvStatus;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillId = itemView.findViewById(R.id.tvBillId);
            tvDateCheckin = itemView.findViewById(R.id.tvDateCheckin);
            tvDateCheckout = itemView.findViewById(R.id.tvDateCheckout);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}