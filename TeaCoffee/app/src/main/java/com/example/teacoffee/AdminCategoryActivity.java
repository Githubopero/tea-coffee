// File: AdminCategoryActivity.java
package com.example.teacoffee;

import android.content.Intent; // Thêm import này
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// SỬA CÁC IMPORT
import com.example.teacoffee.adapter.CategoryAdapter;
import com.example.teacoffee.myDB.FoodCategory;
import com.example.teacoffee.myDB.FoodCategoryDAO;
import com.example.teacoffee.myDB.FoodDAO;
import com.example.teacoffee.myDB.MyDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup; // Thêm import
import com.google.android.material.textfield.TextInputEditText;

public class AdminCategoryActivity extends AppCompatActivity { // Sửa tên class

    enum Mode { ADD, EDIT, DELETE }
    private Mode mode = null;

    private View formContainer;
    private CategoryAdapter adapter; // SỬA

    // form (SỬA)
    private TextView tvId;
    private TextInputEditText etCategoryName;

    private MaterialButton btnConfirm, btnAddMode, btnEditMode, btnDeleteMode;

    private FoodCategoryDAO foodCategoryDAO; // SỬA
    private FoodDAO foodDAO; // <-- THÊM DÒNG NÀY

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // SỬA LAYOUT
        setContentView(R.layout.activity_admin_category);

        // SỬA DAO
        foodCategoryDAO = MyDatabase.getINSTANCE(getApplicationContext()).getFoodCategoryDAO();
        foodDAO = MyDatabase.getINSTANCE(getApplicationContext()).getFoodDAO(); // <-- THÊM DÒNG NÀY

        // Tabs (Thêm logic điều hướng)
        MaterialButtonToggleGroup tabs = findViewById(R.id.tabsGroup);
        MaterialButton tabCategory = findViewById(R.id.btnTabCategory);
        // Chọn sẵn tab "Danh mục"
        if (tabCategory != null) tabs.check(tabCategory.getId());

        // Thêm điều hướng cho các tab khác
        findViewById(R.id.btnTabStaff).setOnClickListener(v -> {
            // Quay về trang Nhân viên
            finish(); // Chỉ cần đóng trang này là quay về trang cũ
        });


        // ==== find views
        formContainer   = findViewById(R.id.formContainer);
        tvId            = findViewById(R.id.tvId);
        etCategoryName  = findViewById(R.id.etCategoryName); // SỬA
        btnConfirm      = findViewById(R.id.btnConfirm);
        btnAddMode      = findViewById(R.id.btnAddMode);
        btnEditMode     = findViewById(R.id.btnEditMode);
        btnDeleteMode   = findViewById(R.id.btnDeleteMode);


        RecyclerView rv = findViewById(R.id.rvCategory); // SỬA ID
        adapter = new CategoryAdapter((category, pos) -> {
            if (mode != null && mode == Mode.EDIT) {
                showForm(true);
                fillForm(category); // SỬA
                btnConfirm.setText("Xác nhận sửa");
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        reloadList();

        // ==== Mode buttons
        btnAddMode.setOnClickListener(v -> setMode(Mode.ADD));
        btnEditMode.setOnClickListener(v -> setMode(Mode.EDIT));
        btnDeleteMode.setOnClickListener(v -> setMode(Mode.DELETE));

        showForm(false); // mặc định xem danh sách
        btnConfirm.setOnClickListener(v -> onConfirm());

        // ✅ GỌI NavigationHelper để quản lý việc chuyển đổi Activity
        NavigationHelper.setupAdminTabs(this, R.id.btnTabCategory);
    }

    private void showForm(boolean show) {
        formContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void setMode(Mode m) {
        mode = m;
        switch (m) {
            case ADD:
                btnConfirm.setText("Xác nhận thêm");
                tvId.setText("ID: (auto)");
                clearInputs();
                showForm(true);
                break;
            case EDIT:
                btnConfirm.setText("Xác nhận sửa");
                showForm(false); // Ẩn form, đợi user click
                adapter.notifyDataSetChanged(); // Reset lại click
                break;
            case DELETE:
                btnConfirm.setText("Xác nhận xóa");
                showForm(false);
                FoodCategory target = adapter.getSelected(); // SỬA
                if (target == null) {
                    toast("Hãy chọn 1 danh mục để xóa");
                } else {
                    showDeleteDialog(target); // SỬA
                }
                break;
        }
    }

    private void clearInputs() {
        etCategoryName.setText(null); // SỬA
    }

    /** Nạp lại danh sách */
    private void reloadList() {
        adapter.submit(foodCategoryDAO.getAll());
    }

    private void fillForm(FoodCategory c) { // SỬA
        if (c == null) return;
        tvId.setText("ID: " + c.Food_Category_Id);
        etCategoryName.setText(c.Food_Category_Name);
    }

    // --- THAY THẾ TOÀN BỘ HÀM NÀY ---
    private void onConfirm() {
        final String categoryName = safe(etCategoryName).trim(); // Dùng .trim() để xóa khoảng trắng thừa

        // --- BẮT ĐẦU VALIDATION ---

        // 1. Kiểm tra rỗng (Code cũ)
        if (categoryName.isEmpty()) {
            toast("Vui lòng nhập tên danh mục");
            return;
        }

        // 2. TC-DM-03: Kiểm tra ký tự đặc biệt
        // Regex này cho phép chữ cái (tiếng Việt), số, và khoảng trắng
        // Nó sẽ báo lỗi nếu có dấu chấm, phẩy, @, #, $, %...
        String regex = "^[\\p{L}\\p{N} ]+$";
        if (!categoryName.matches(regex)) {
            toast("Vui lòng nhập lại tên danh mục (không chứa ký tự đặc biệt)");
            return;
        }

        // 3. TC-DM-13: Kiểm tra độ dài (Đã làm)
        if (categoryName.length() > 50) {
            toast("Tên danh mục quá dài (tối đa 50 ký tự)");
            return;
        }

        // --- KẾT THÚC VALIDATION ---


        // --- XỬ LÝ THÊM MỚI (ADD) ---
        if (mode == Mode.ADD) {
            // 4. TC-DM-04: Kiểm tra trùng tên khi Thêm
            if (foodCategoryDAO.findByName(categoryName) != null) {
                toast("Danh mục đã tồn tại");
                return;
            }

            // Nếu qua hết kiểm tra:
            FoodCategory c = new FoodCategory();
            c.Food_Category_Name = categoryName;
            foodCategoryDAO.insert(c);
            toast("Đã thêm danh mục: " + categoryName);

            clearInputs();
            reloadList();
            showForm(false);
            setMode(Mode.EDIT);
            return;
        }

        // --- XỬ LÝ SỬA (EDIT) ---
        if (mode == Mode.EDIT) {
            FoodCategory selected = adapter.getSelected();
            if (selected == null) {
                toast("Chọn 1 danh mục để sửa");
                return;
            }

            // 5. TC-DM-08: Kiểm tra trùng tên khi Sửa
            FoodCategory existing = foodCategoryDAO.findByName(categoryName);
            if (existing != null && existing.Food_Category_Id != selected.Food_Category_Id) {
                // Tên này đã tồn tại, VÀ nó không phải là bản ghi mình đang sửa
                toast("Danh mục đã tồn tại");
                return;
            }

            // Nếu qua hết kiểm tra:
            selected.Food_Category_Name = categoryName;
            foodCategoryDAO.update(selected);
            toast("Đã cập nhật danh mục");

            reloadList();
            showForm(false);
            return;
        }
    }

    // --- THAY THẾ TOÀN BỘ HÀM NÀY ---
    private void showDeleteDialog(FoodCategory target) {
        // 1. Tạo Builder (Dùng layout dialog_confirm_delete.xml của bạn)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.dialog_confirm_delete, null);
        builder.setView(view);

        // 2. Ánh xạ các view BÊN TRONG dialog
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        MaterialButton btnYes = view.findViewById(R.id.btnYes);
        MaterialButton btnNo  = view.findViewById(R.id.btnNo);

        // 3. Set nội dung
        tvMessage.setText("Bạn có chắc muốn xóa danh mục: " + target.Food_Category_Name + " ?");

        // 4. Tạo dialog
        AlertDialog dialog = builder.create();

        // 5. Gán sự kiện cho 2 nút
        btnYes.setOnClickListener(v -> {
            // --- BẮT ĐẦU VALIDATION XÓA ---
            // 6. TC-DM-10: Kiểm tra xem danh mục có đang được dùng không
            int foodCount = foodDAO.countFoodsByCategoryId(target.Food_Category_Id);
            if (foodCount > 0) {
                toast("Không thể xóa danh mục đang được sử dụng bởi " + foodCount + " món ăn");
                dialog.dismiss();
                return; // KHÔNG XÓA
            }
            // --- KẾT THÚC VALIDATION XÓA ---

            // 7. TC-DM-09: Xóa thành công (vì foodCount = 0)
            foodCategoryDAO.delete(target);
            toast("Đã xóa: " + target.Food_Category_Name);
            reloadList();
            dialog.dismiss();
        });

        btnNo.setOnClickListener(v -> dialog.dismiss());

        // 8. Hiển thị dialog
        dialog.show();
    }

    private String safe(TextInputEditText ed) {
        return ed.getText() == null ? "" : ed.getText().toString().trim();
    }

    private void toast(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }
}