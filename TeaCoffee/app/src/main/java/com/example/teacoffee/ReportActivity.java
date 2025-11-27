package com.example.teacoffee;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacoffee.adapter.BillAdapter;
import com.example.teacoffee.myDB.Bill;
import com.example.teacoffee.myDB.BillDAO;
import com.example.teacoffee.myDB.FoodSalesStats;
import com.example.teacoffee.myDB.MyDatabase;
import com.example.teacoffee.myDB.SimpleBarChart;

import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    // ✅ NEW: ImageView cho xuất PDF
    private ImageView ivExportPdf;


    // Danh sách hóa đơn hiện tại và request code cho SAF
    private List<Bill> currentBills = List.of();
    private static final int CREATE_FILE_REQUEST_CODE = 200;


    private SimpleBarChart simpleBarChart;
    private Button btnShowChart;
    private boolean isChartVisible = false;

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

        ivExportPdf = findViewById(R.id.ivExportPdf); // ✅ ÁNH XẠ MỚI


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

        // ✅ NEW: Listener cho nút Xuất PDF
        ivExportPdf.setOnClickListener(v -> exportPdfReport());


        simpleBarChart = findViewById(R.id.simpleBarChart);
        btnShowChart = findViewById(R.id.btnShowChart);

        btnShowChart.setOnClickListener(v -> {
            isChartVisible = !isChartVisible;
            if (isChartVisible) {
                loadSimpleChart();
                simpleBarChart.setVisibility(View.VISIBLE);
                rvBills.setVisibility(View.GONE);
                btnShowChart.setText("Ẩn biểu đồ");
            } else {
                simpleBarChart.setVisibility(View.GONE);
                rvBills.setVisibility(View.VISIBLE);
                btnShowChart.setText("Biểu đồ số lượng món");
            }
        });

//        SimpleBarChart barChart = findViewById(R.id.simpleBarChart);
//        List<SimpleBarChart.BarData> list = new ArrayList<>();
//        list.add(new SimpleBarChart.BarData("Trà sữa trân châu", 89));
//        list.add(new SimpleBarChart.BarData("Cà phê đen", 72));
//        list.add(new SimpleBarChart.BarData("Sinh tố dâu", 65));
//// ... thêm bao nhiêu cũng được
//
//        barChart.setData(list);

        // ✅ GỌI NavigationHelper để quản lý việc chuyển đổi Activity
        NavigationHelper.setupAdminTabs(this, R.id.btnTabStats);

        // Tải dữ liệu lần đầu (vì không dùng onTabSelected nữa)
        loadReportData();
    }

    private void loadSimpleChart() {
        long startTime = startCalendar.getTimeInMillis();
        long endTime = endCalendar.getTimeInMillis();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<FoodSalesStats> list = billDAO.getSoldFoodsInPeriod(startTime, endTime);

            runOnUiThread(() -> {
                if (list.isEmpty()) {
                    Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<SimpleBarChart.BarData> chartData = new ArrayList<>();
                for (FoodSalesStats item : list) {
                    if (item.quantity > 0) {
                        chartData.add(new SimpleBarChart.BarData(item.foodName, (int) item.quantity));
                    }
                }

                simpleBarChart.setData(chartData);
            });
        });
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
                    loadSimpleChart(); // TỰ ĐỘNG VẼ LẠI BIỂU ĐỒ MỚI - SIÊU CHUYÊN NGHIỆP
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

                currentBills = bills; // ✅ CẬP NHẬT DANH SÁCH HIỆN TẠI

                // ✅ BƯỚC 1: TÍNH TỔNG DOANH THU trong luồng nền
                long totalRevenue = calculateTotalRevenue(bills);
                // ✅ BƯỚC 2: CẬP NHẬT UI HIỂN THỊ TỔNG TIỀN TRONG LUỒNG CHÍNH
                if (tvTotalRevenue != null) {
                    tvTotalRevenue.setText("Tổng Doanh Thu: " + currencyFormat.format(totalRevenue) + " VNĐ");
                }
                // ✅ HIỂN THỊ/ẨN THÔNG BÁO KHÔNG CÓ DỮ LIỆU
                if (bills.isEmpty()) {
                    tvNoDataMessage.setVisibility(View.VISIBLE);
                    rvBills.setVisibility(View.GONE);
                } else {
                    tvNoDataMessage.setVisibility(View.GONE);
                    rvBills.setVisibility(View.VISIBLE);
                }
            });
        });
    }



    // Hàm tính tổng doanh thu
    private long calculateTotalRevenue(List<Bill> bills) { // ✅ THAY ĐỔI: Trả về long
        long totalRevenue = bills.stream()
                .mapToLong(bill -> bill.Total_Price != null ? bill.Total_Price : 0)
                .sum();

//        if (tvTotalRevenue != null) {
//            tvTotalRevenue.setText("Tổng Doanh Thu: " + currencyFormat.format(totalRevenue) + " VNĐ");
//        }
        return totalRevenue; // ✅ TRẢ VỀ TỔNG DOANH THU
    }


    // ✅ NEW: Hàm xuất báo cáo PDF
    private void exportPdfReport() {
        if (currentBills.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu để xuất PDF", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo Intent ACTION_CREATE_DOCUMENT
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        // Đề xuất tên file mặc định
        String startDate = dateFormat.format(startCalendar.getTime());
        String endDate = dateFormat.format(endCalendar.getTime());
        String fileName = "Report_" + startDate.replace("/", "") + "_to_" + endDate.replace("/", "") + ".pdf";
        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        try {
            startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở trình quản lý file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                // Tiến hành ghi PDF vào Uri đã chọn trong một luồng riêng
                Executors.newSingleThreadExecutor().execute(() -> writePdfToUri(uri));
            } else {
                Toast.makeText(this, "Hủy thao tác lưu file.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Tạo tài liệu PdfDocument với nội dung báo cáo.
     */
    private PdfDocument createPdfDocument() {
        // Khổ A5 (148 × 210 mm) → 420 × 595 pt (1mm = 2.834pt)
        int pageWidth = 420;
        int pageHeight = 595;

        // === CÁC HẰNG SỐ THIẾT KẾ ===
        int margin = 35;
        int lineHeight = 22;
        int titleSize = 24;
        int headerSize = 12;
        int normalSize = 11;
        int smallSize = 10;

        // Màu sắc chuyên nghiệp
        int colorPrimary = 0xFF1565C0;   // Xanh dương đậm (primary)
        int colorAccent  = 0xFF42A5F5;   // Xanh dương nhạt
        int colorHeaderBg = 0xFFF5F7FA;
        int colorTableLine = 0xFFE0E0E0;
        int colorText = 0xFF212121;
        int colorLightText = 0xFF666666;

        PdfDocument document = new PdfDocument();
        int pageNumber = 1;
        int y = margin + 10;

        long totalRevenue = calculateTotalRevenue(currentBills);

        PdfDocument.Page page = startNewPage(document, pageWidth, pageHeight, pageNumber++);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int x = margin;

        // ==================== 1. TIÊU ĐỀ ====================
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(titleSize);
        paint.setColor(colorPrimary);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("BÁO CÁO DOANH THU TEACOFFEE", pageWidth / 2f, y, paint);

        y += 30;

        // Đường kẻ phân cách đẹp
        paint.setStrokeWidth(3f);
        paint.setColor(colorAccent);
        canvas.drawLine(margin, y, pageWidth - margin, y, paint);

        y += 25;

        // ==================== 2. THÔNG TIN CHUNG ====================
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(normalSize);
        paint.setColor(colorText);
        paint.setTextAlign(Paint.Align.LEFT);

        String startDate = dateFormat.format(startCalendar.getTime());
        String endDate = dateFormat.format(endCalendar.getTime());
        String period = "Từ ngày: " + startDate + "  →  Đến ngày: " + endDate;

        canvas.drawText("Khoảng thời gian:", x, y, paint);
        paint.setFakeBoldText(true);
        canvas.drawText(period, x + 100, y, paint);
        paint.setFakeBoldText(false);
        y += lineHeight + 5;

        canvas.drawText("Ngày in báo cáo:", x, y, paint);
        paint.setFakeBoldText(true);
        canvas.drawText(dateFormat.format(new Date()), x + 100, y, paint);
        paint.setFakeBoldText(false);
        y += lineHeight + 15;

        // ==================== 3. TỔNG DOANH THU (NỔI BẬT) ====================
        paint.setTextSize(16);
        paint.setFakeBoldText(true);
        paint.setColor(colorPrimary);
        canvas.drawText("TỔNG DOANH THU", x, y, paint);

        paint.setTextSize(22);
        paint.setColor(0xFFD32F2F); // Đỏ nổi bật cho số tiền lớn
        canvas.drawText(currencyFormat.format(totalRevenue) + " VNĐ", x, y + 35, paint);

        paint.setColor(colorLightText);
        paint.setTextSize(smallSize);
        canvas.drawText("Đã bao gồm tất cả hóa đơn trong khoảng thời gian", x, y + 50, paint);

        y += 85;

        // ==================== 4. BẢNG CHI TIẾT ====================
        int tableTop = y;
        int tableBottom = 0;
        int col1 = 50, col2 = 130, col3 = 230, col4 = 330; // Cột cố định, đẹp hơn

        // Header bảng
        paint.setColor(colorHeaderBg);
        canvas.drawRect(margin, y, pageWidth - margin, y + 40, paint);

        paint.setColor(colorPrimary);
        paint.setStrokeWidth(2.5f);
        canvas.drawLine(margin, y, pageWidth - margin, y, paint); // Đường trên dày

        paint.setColor(colorText);
        paint.setFakeBoldText(true);
        paint.setTextSize(headerSize);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("ID", (margin + col1) / 2f, y + 25, paint);
        canvas.drawText("Ngày vào", (col1 + col2) / 2f, y + 25, paint);
        canvas.drawText("Ngày ra", (col2 + col3) / 2f, y + 25, paint);
        canvas.drawText("Tổng tiền", (col3 + (pageWidth - margin)) / 2f, y + 25, paint);

        y += 40;

        // Nội dung bảng
        paint.setFakeBoldText(false);
        paint.setTextSize(smallSize);

        for (int i = 0; i < currentBills.size(); i++) {
            Bill bill = currentBills.get(i);

            // Kiểm tra tràn trang
            if (y + 35 > pageHeight - 60) {
                drawTableBottomBorder(canvas, paint, margin, pageWidth, y, colorPrimary);
                document.finishPage(page);

                page = startNewPage(document, pageWidth, pageHeight, pageNumber++);
                canvas = page.getCanvas();
                y = margin + 60;

                // Vẽ lại header trên trang mới
                drawTableHeader(canvas, paint, margin, pageWidth, y, colorHeaderBg, colorPrimary, headerSize);
                y += 40;
            }

            String checkin = bill.Date_Checkin != null ? dateFormat.format(bill.Date_Checkin) : "-";
            String checkout = bill.Date_Checkout != null ? dateFormat.format(bill.Date_Checkout) : "-";
            String total = currencyFormat.format(bill.Total_Price != null ? bill.Total_Price : 0) + " ₫";

            // Dòng chẵn/lẻ có màu nền nhạt để dễ đọc
            if (i % 2 == 1) {
                paint.setColor(0xFFFAFAFA);
                canvas.drawRect(margin, y - 8, pageWidth - margin, y + 27, paint);
            }

            paint.setColor(colorText);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(bill.Bill_Id), (margin + col1) / 2f, y + 15, paint);
            canvas.drawText(checkin, (col1 + col2) / 2f, y + 15, paint);
            canvas.drawText(checkout, (col2 + col3) / 2f, y + 15, paint);

            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(total, pageWidth - margin - 15, y + 15, paint);

            // Đường kẻ ngang nhẹ
            paint.setColor(colorTableLine);
            paint.setStrokeWidth(1f);
            canvas.drawLine(margin, y + 27, pageWidth - margin, y + 27, paint);

            y += 35;
        }

        // Viền dưới bảng
        drawTableBottomBorder(canvas, paint, margin, pageWidth, y - 8, colorPrimary);

        // ==================== FOOTER (SỐ TRANG) ====================
        paint.setTextSize(9);
        paint.setColor(colorLightText);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Trang " + (pageNumber - 1), pageWidth / 2f, pageHeight - 30, paint);

        document.finishPage(page);
        return document;
    }

    // Hàm hỗ trợ vẽ header bảng (dùng lại khi xuống trang)
    private void drawTableHeader(Canvas canvas, Paint paint, int margin, int pageWidth, int y,
                                 int bgColor, int lineColor, int textSize) {
        paint.setColor(bgColor);
        canvas.drawRect(margin, y, pageWidth - margin, y + 40, paint);

        paint.setColor(lineColor);
        paint.setStrokeWidth(2.5f);
        canvas.drawLine(margin, y, pageWidth - margin, y, paint);

        paint.setColor(0xFF212121);
        paint.setFakeBoldText(true);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        int col1 = 50, col2 = 130, col3 = 230;
        canvas.drawText("ID", (margin + col1) / 2f, y + 25, paint);
        canvas.drawText("Ngày vào", (col1 + col2) / 2f, y + 25, paint);
        canvas.drawText("Ngày ra", (col2 + col3) / 2f, y + 25, paint);
        canvas.drawText("Tổng tiền", (col3 + (pageWidth - margin)) / 2f, y + 25, paint);
    }

    // Viền dưới bảng
    private void drawTableBottomBorder(Canvas canvas, Paint paint, int margin, int pageWidth, int y, int color) {
        paint.setColor(color);
        paint.setStrokeWidth(2.5f);
        canvas.drawLine(margin, y, pageWidth - margin, y, paint);
    }

    // Tạo trang mới
    private PdfDocument.Page startNewPage(PdfDocument document, int w, int h, int num) {
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(w, h, num).create();
        return document.startPage(pageInfo);
    }

    /**
     * Ghi nội dung PdfDocument vào Uri (vị trí người dùng đã chọn)
     */
    private void writePdfToUri(Uri uri) {
        // Gọi hàm tạo PdfDocument
        PdfDocument document = createPdfDocument();

        if (document == null) {
            runOnUiThread(() -> {
                Toast.makeText(this, "Lỗi tạo tài liệu PDF.", Toast.LENGTH_LONG).show();
            });
            return;
        }

        try (OutputStream os = getContentResolver().openOutputStream(uri)) {
            if (os != null) {
                document.writeTo(os);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Xuất PDF thành công.", Toast.LENGTH_LONG).show();
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Không thể mở luồng ghi file.", Toast.LENGTH_LONG).show();
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                Toast.makeText(this, "Lỗi khi xuất PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        } finally {
            if (document != null) {
                document.close(); // ✅ QUAN TRỌNG: Đảm bảo document được đóng
            }
        }
    }
}