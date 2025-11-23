package com.example.teacoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teacoffee.myDB.Bill;
import com.example.teacoffee.myDB.BillInfor;
import com.example.teacoffee.myDB.Food;
import com.example.teacoffee.myDB.MyDatabase;
import com.example.teacoffee.myDB.TableEntity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BillListActivity extends AppCompatActivity {

    private Button btnTabUnpaid, btnTabPaid;
    private LinearLayout billContainer;
    private LayoutInflater inflater;

    private List<Bill> unpaidBills;
    private List<Bill> paidBills;

    private final SimpleDateFormat sdf =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private final NumberFormat VND =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);

        btnTabUnpaid = findViewById(R.id.btnTabUnpaid);
        btnTabPaid   = findViewById(R.id.btnTabPaid);
        billContainer = findViewById(R.id.billContainer);
        inflater = LayoutInflater.from(this);


        loadData();
        showUnpaidTab(); // mặc định

        btnTabUnpaid.setOnClickListener(v -> showUnpaidTab());
        btnTabPaid.setOnClickListener(v -> showPaidTab());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // trở lại từ Order / Payment thì reload list
        loadData();
        if (!btnTabUnpaid.isEnabled()) {
            showUnpaidTab();
        } else if (!btnTabPaid.isEnabled()) {
            showPaidTab();
        }
    }

    private void loadData() {
        MyDatabase db = MyDatabase.getINSTANCE(this);
        unpaidBills = db.getBillDAO().getOpenBills();
        paidBills   = db.getBillDAO().getClosedBills();
    }

    private void showUnpaidTab() {
        styleTabs(true);
        renderBills(unpaidBills, true);
    }

    private void showPaidTab() {
        styleTabs(false);
        renderBills(paidBills, false);
    }

    private void styleTabs(boolean unpaidActive) {
        if (unpaidActive) {
            btnTabUnpaid.setEnabled(false);
            btnTabUnpaid.setBackgroundResource(R.drawable.bg_chip_active);
            btnTabUnpaid.setTextColor(getResources().getColor(android.R.color.white));

            btnTabPaid.setEnabled(true);
            btnTabPaid.setBackgroundResource(R.drawable.bg_chip_inactive);
            btnTabPaid.setTextColor(0xFF4E4E4E);
        } else {
            btnTabUnpaid.setEnabled(true);
            btnTabUnpaid.setBackgroundResource(R.drawable.bg_chip_inactive);
            btnTabUnpaid.setTextColor(0xFF4E4E4E);

            btnTabPaid.setEnabled(false);
            btnTabPaid.setBackgroundResource(R.drawable.bg_chip_active);
            btnTabPaid.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    private void renderBills(List<Bill> bills, boolean isUnpaidTab) {
        billContainer.removeAllViews();
        MyDatabase db = MyDatabase.getINSTANCE(this);

        if (bills == null || bills.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("Không có hóa đơn");
            tv.setTextSize(14f);
            tv.setPadding(8, 8, 8, 8);
            billContainer.addView(tv);
            return;
        }

        for (Bill b : bills) {
            View card = inflater.inflate(R.layout.item_bill_user, billContainer, false);

            TextView tvTableName   = card.findViewById(R.id.tvTableName);
            TextView tvBillCode    = card.findViewById(R.id.tvBillCode);
            TextView tvCheckin     = card.findViewById(R.id.tvDateCheckin);
            TextView tvCheckout    = card.findViewById(R.id.tvDateCheckout);
            TextView tvToggle      = card.findViewById(R.id.tvToggleDetail);
            LinearLayout layoutDetails = card.findViewById(R.id.layoutDetails);
            LinearLayout layoutButtons = card.findViewById(R.id.layoutButtons);

            TableEntity t = db.getTableDAO().getById(b.Table_Id);
            String tableName = (t != null && t.Table_Name != null && !t.Table_Name.isEmpty())
                    ? t.Table_Name : ("Bàn " + b.Table_Id);

            tvTableName.setText(tableName);
            tvBillCode.setText("Mã hóa đơn #" + b.Bill_Id);
            tvCheckin.setText("Ngày đặt bàn: " + formatDate(b.Date_Checkin));
            tvCheckout.setText("Ngày thanh toán: " + formatDate(b.Date_Checkout));

            // Tab ĐÃ THANH TOÁN: ẩn 2 nút
            if (!isUnpaidTab) {
                layoutButtons.setVisibility(View.GONE);
            } else {
                layoutButtons.setVisibility(View.VISIBLE);
                Button btnEdit = card.findViewById(R.id.btnEditBill);
                Button btnPay  = card.findViewById(R.id.btnPayBill);

                // CHỈNH SỬA -> OrderActivity
                btnEdit.setOnClickListener(v -> {
                    Intent it = new Intent(BillListActivity.this, OrderActivity.class);
                    it.putExtra("tableId", b.Table_Id);
                    startActivity(it);
                });

                // THANH TOÁN -> PaymentActivity
                btnPay.setOnClickListener(v -> {
                    Intent it = new Intent(BillListActivity.this, PaymentActivity.class);
                    it.putExtra("tableId", b.Table_Id);
                    startActivity(it);
                });
            }

            // XỔ / THU CHI TIẾT
            tvToggle.setOnClickListener(v -> {
                if (layoutDetails.getVisibility() == View.VISIBLE) {
                    layoutDetails.setVisibility(View.GONE);
                } else {
                    layoutDetails.setVisibility(View.VISIBLE);
                    if (layoutDetails.getTag() == null) {
                        bindBillDetails(card, b);
                        layoutDetails.setTag(Boolean.TRUE); // đánh dấu đã bind để không add trùng
                    }
                }
            });

            billContainer.addView(card);
        }
    }

    /** Gán list món + chiết khấu + thành tiền vào vùng chi tiết */
    private void bindBillDetails(View card, Bill bill) {
        LinearLayout layoutItems = card.findViewById(R.id.layoutItems);
        TextView tvDiscount = card.findViewById(R.id.tvDiscount);
        TextView tvTotal = card.findViewById(R.id.tvTotal);

        layoutItems.removeAllViews();

        MyDatabase db = MyDatabase.getINSTANCE(this);
        List<BillInfor> lines = db.getBillInforDAO().getByBillId(bill.Bill_Id);

        if (lines == null || lines.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("Hóa đơn không có món.");
            layoutItems.addView(tv);
            tvDiscount.setText("0 đ");
            tvTotal.setText("0 đ");
            return;
        }

        long subtotal = 0;

        for (BillInfor li : lines) {
            Food f = db.getFoodDAO().getById(li.Food_Id);
            long price = (f == null ? 0 : f.Price);
            subtotal += price * li.Count;

            String name = (f == null ? ("Món #" + li.Food_Id) : f.Food_Name);
            String priceStr = formatMoney(price);
            String qtyStr = String.valueOf(li.Count);

            View row = inflater.inflate(R.layout.item_payment_row, layoutItems, false);
            ((TextView) row.findViewById(R.id.tvItemName)).setText("• " + name);
            ((TextView) row.findViewById(R.id.tvItemPrice)).setText(priceStr);
            ((TextView) row.findViewById(R.id.tvItemQty)).setText(qtyStr);

            layoutItems.addView(row);
        }

        int percent = bill.Discount == null ? 0 : bill.Discount;
        long grand;

        if (bill.Total_Price == null || bill.Total_Price == 0) {
            // Chưa chốt: tính tạm theo % chiết khấu
            if (percent > 0) {
                grand = subtotal - (subtotal * percent / 100);
            } else {
                grand = subtotal;
            }
        } else {
            grand = bill.Total_Price;
        }

        long discountMoney = Math.max(0, subtotal - grand);

        if (percent > 0) {
            tvDiscount.setText(percent + "% (" + formatMoney(discountMoney) + ")");
        } else {
            tvDiscount.setText(formatMoney(discountMoney));
        }
        tvTotal.setText(formatMoney(grand));
    }

    private String formatDate(Long epochMillis) {
        if (epochMillis == null || epochMillis == 0) return "—";
        try {
            return sdf.format(new Date(epochMillis));
        } catch (Exception e) {
            return "—";
        }
    }

    private String formatMoney(long value) {
        try {
            return VND.format(value);
        } catch (Exception e) {
            return value + " đ";
        }
    }
}
