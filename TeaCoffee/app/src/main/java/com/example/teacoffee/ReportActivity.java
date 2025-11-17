package com.example.teacoffee;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity; // ✅ SỬA LỖI: Kế thừa AppCompatActivity
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Import các thư viện cần thiết
import com.example.teacoffee.adapter.BillAdapter;
import com.example.teacoffee.myDB.MyDatabase;
import com.example.teacoffee.myDB.Bill;
import com.example.teacoffee.myDB.BillDAO;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;


// ✅ SỬA LỖI: ReportActivity phải kế thừa AppCompatActivity
public class ReportActivity extends AppCompatActivity {

    // Các thành phần UI của tab Thống kê
    private TextView tvStartDate, tvEndDate, tvTotalRevenue;
    private Button btnStartDate, btnEndDate;
    private RecyclerView rvBills;

    // ✅ NEW: TextView thông báo khi không có dữ liệu
    private TextView tvNoDataMessage;

    private BillDAO billDAO;
    private BillAdapter billAdapter;

    private Calendar startCalendar, endCalendar;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Khởi tạo DAO
        billDAO = MyDatabase.getINSTANCE(this).getBillDAO();

        // Ánh xạ View của tab THỐNG KÊ
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        rvBills = findViewById(R.id.rvBills);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);

        // ✅ Ánh xạ TextView thông báo
        tvNoDataMessage = findViewById(R.id.tvNoDataMessage);

        // Thiết lập RecyclerView
        billAdapter = new BillAdapter(List.of());
        rvBills.setLayoutManager(new LinearLayoutManager(this));
        rvBills.setAdapter(billAdapter);

        // Khởi tạo Calendar
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        // Khởi tạo ngày ban đầu
        updateDisplayDates();

        // Listener cho Button chọn ngày
        btnStartDate.setOnClickListener(v -> showDatePicker(startCalendar, tvStartDate, true));
        btnEndDate.setOnClickListener(v -> showDatePicker(endCalendar, tvEndDate, false));

        // ✅ GỌI NavigationHelper để quản lý việc chuyển đổi Activity
        NavigationHelper.setupAdminTabs(this, R.id.btnTabStats);

        // Tải dữ liệu lần đầu (vì không dùng onTabSelected nữa)
        loadReportData();
    }

    // ==============================================================
    // ✅ ĐÃ XÓA: CÁC PHƯƠNG THỨC BẮT BUỘC CỦA BASE CLASS (getTabContainers, onTabSelected)
    // ==============================================================

    // Hàm showDatePicker
    private void showDatePicker(Calendar calendar, TextView textView, boolean isStart) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    textView.setText(dateFormat.format(calendar.getTime()));
                    loadReportData(); // Tải lại dữ liệu khi đổi ngày
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Cập nhật hiển thị ngày trên TextView
    private void updateDisplayDates() {
        tvStartDate.setText(dateFormat.format(startCalendar.getTime()));
        tvEndDate.setText(dateFormat.format(endCalendar.getTime()));
    }

    // Tải dữ liệu báo cáo từ Room DB
    private void loadReportData() {
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        long startTime = startCalendar.getTimeInMillis();

        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        endCalendar.set(Calendar.MILLISECOND, 999);
        long endTime = endCalendar.getTimeInMillis();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Bill> bills = billDAO.getClosedBillsInPeriod(startTime, endTime);
            runOnUiThread(() -> {
                // Cập nhật Adapter
                billAdapter.setBillList(bills);

                // Tính và hiển thị tổng doanh thu
                calculateTotalRevenue(bills);
            });
        });
    }

    // Hàm tính tổng doanh thu
    private void calculateTotalRevenue(List<Bill> bills) {
        long totalRevenue = bills.stream()
                .mapToLong(bill -> bill.Total_Price != null ? bill.Total_Price : 0)
                .sum();

        if (tvTotalRevenue != null) {
            tvTotalRevenue.setText("Tổng Doanh Thu: " + currencyFormat.format(totalRevenue) + " VNĐ");
        }
    }
}