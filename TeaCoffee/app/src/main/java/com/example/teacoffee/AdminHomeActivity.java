package com.example.teacoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacoffee.adapter.StaffAdapter;
import com.example.teacoffee.myDB.Account;
import com.example.teacoffee.myDB.AccountDAO;
import com.example.teacoffee.myDB.MyDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

public class AdminHomeActivity extends AppCompatActivity {

    //Khai báo dữ liệu nút sản phẩm
    private Button btnSP;
    //=================================//
    enum Mode { ADD, EDIT, DELETE }
    private Mode mode = null;

    private View formContainer;
    private StaffAdapter adapter;

    // form
    private TextView tvId;
    private TextInputEditText etDisplayName, etUserName, etPassword;
    private MaterialAutoCompleteTextView spType;

    // filter
    private MaterialAutoCompleteTextView spAccountTypeFilter;

    private MaterialButton btnConfirm, btnAddMode, btnEditMode, btnDeleteMode, btnCancel;

    private AccountDAO accountDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        accountDAO = MyDatabase.getINSTANCE(getApplicationContext()).getAccountDAO();

        // Tabs
        MaterialButtonToggleGroup tabs = findViewById(R.id.tabsGroup);
        tabs.check(R.id.btnTabStaff);

//        MaterialButton tabCategory = findViewById(R.id.btnTabCategory);
//        tabCategory.setOnClickListener(v -> {
//            Intent intent = new Intent(AdminHomeActivity.this, AdminCategoryActivity.class);
//            startActivity(intent);
//        });

        // ==== find views
        formContainer          = findViewById(R.id.formContainer);
        tvId                   = findViewById(R.id.tvId);
        etDisplayName          = findViewById(R.id.etDisplayName);
        etUserName             = findViewById(R.id.etUserName);
        etPassword             = findViewById(R.id.etPassword);
        spType                 = findViewById(R.id.spType);
        spAccountTypeFilter    = findViewById(R.id.spAccountTypeFilter);
        btnConfirm             = findViewById(R.id.btnConfirm);
        btnCancel              = findViewById(R.id.btnCancel);      // <-- thêm
        btnAddMode             = findViewById(R.id.btnAddMode);
        btnEditMode            = findViewById(R.id.btnEditMode);
        btnDeleteMode          = findViewById(R.id.btnDeleteMode);

        //Xử lý nút sản phẩm
        btnSP = findViewById(R.id.btnTabProduct);
        btnSP.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminFoodManagement.class);
            startActivity(intent);
        });
        btnCancel.setOnClickListener(v -> {
            // Ẩn form
            showForm(false);

            // Xoá dữ liệu trong form (nếu có)
            clearInputs();

            // Đặt lại chế độ mặc định (ví dụ về trạng thái xem danh sách)
            mode = Mode.EDIT;  // hoặc Mode.NONE nếu bạn có kiểu đó

            // Ẩn nút HỦY
            btnCancel.setVisibility(View.GONE);

        });

        //======================

        // ==== combobox Type trong form: mặc định "staff"
        spType.setSimpleItems(new String[]{"staff", "admin"});
        spType.setText("STAFF", false);

        // ==== combobox Filter
        spAccountTypeFilter.setSimpleItems(new String[]{"(Tất cả)", "ADMIN", "STAFF"});
        spAccountTypeFilter.setText("(Tất cả)", false);
        spAccountTypeFilter.setOnItemClickListener((parent, view, position, id) -> {
            adapter.clearSelection();
            showForm(false);                    // ẩn form nếu đang mở
            btnCancel.setVisibility(View.GONE); // ẩn nút Hủy
            reloadList();
        });

        // ==== RecyclerView
        RecyclerView rv = findViewById(R.id.rvStaff);
        adapter = new StaffAdapter((acc, pos) -> {
            if (mode == Mode.EDIT) {
                showForm(true);
                fillForm(acc);
                btnConfirm.setText("Xác nhận sửa");
                btnCancel.setVisibility(View.VISIBLE); // có form thì hiện Hủy
            } else if (mode == Mode.DELETE) {
                // nếu bạn muốn xóa khi bấm vào item ở chế độ xóa
                showDeleteDialog(acc);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        reloadList();

        // ==== Mode buttons
        btnAddMode.setOnClickListener(v -> {
            adapter.clearSelection();
            setMode(Mode.ADD);
        });
        btnEditMode.setOnClickListener(v -> {
            adapter.clearSelection();
            setMode(Mode.EDIT);
        });
        btnDeleteMode.setOnClickListener(v -> {
            adapter.clearSelection(); // tránh dùng selection từ chế độ khác
            setMode(Mode.DELETE);
        });

        // Xác nhận
        btnConfirm.setOnClickListener(v -> onConfirm());


        // ✅ GỌI NavigationHelper để quản lý việc chuyển đổi Activity
        NavigationHelper.setupAdminTabs(this, R.id.btnTabStaff);
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
                btnCancel.setVisibility(View.VISIBLE); // có form -> có HỦY
                break;

            case EDIT:
                btnConfirm.setText("Xác nhận sửa");
                showForm(false);                // chỉ mở form khi chọn item
                btnCancel.setVisibility(View.GONE);
                break;

            case DELETE:
                btnConfirm.setText("Xác nhận xóa");
                showForm(false);                // xóa không dùng form
                btnCancel.setVisibility(View.GONE);
                toast("Hãy chọn 1 tài khoản để xóa");
                break;
        }
        updateModeButtons();
    }

    private void clearInputs() {
        etDisplayName.setText(null);
        etUserName.setText(null);
        etPassword.setText(null);
        spType.setText("STAFF", false);
    }

    private void reloadList() {
        String sel = spAccountTypeFilter.getText() == null ? "" :
                spAccountTypeFilter.getText().toString().trim();

        if ("ADMIN".equalsIgnoreCase(sel)) {
            adapter.submit(accountDAO.getByType("ADMIN"));
        } else if ("STAFF".equalsIgnoreCase(sel)) {
            adapter.submit(accountDAO.getByType("STAFF"));
        } else {
            adapter.submit(accountDAO.getAll());
        }
    }

    private void fillForm(Account a) {
        if (a == null) return;
        tvId.setText("ID: " + a.User_Id);
        etDisplayName.setText(a.Display_Name);
        etUserName.setText(a.User_Name);
        etPassword.setText(a.Password);
        spType.setText(a.Type == null ? "STAFF" : a.Type, false);
    }

    private void onConfirm() {
        final String displayName = safe(etDisplayName);
        final String userName    = safe(etUserName);
        final String password    = safe(etPassword);
        final String typeRaw     = spType.getText() == null ? "" : spType.getText().toString().trim();
        final String type        = typeRaw.isEmpty() ? "STAFF" : typeRaw;
        //  KIỂM TRA TÊN HIỂN THỊ
        if (!isValidDisplayName(displayName)) {
            toast("Tên hiển thị chỉ được chứa chữ, số và khoảng trắng, không được có ký tự đặc biệt");
            return;
        }
        if (mode == Mode.ADD) {
            if (userName.isEmpty() || password.isEmpty()) {
                toast("Nhập Tên đăng nhập và Mật khẩu");
                return;
            }
            //  KIỂM TRA TRÙNG USERNAME
            int count = accountDAO.countByUserName(userName);
            if (count > 0) {
                toast("Tên đăng nhập đã tồn tại, vui lòng chọn tên khác");
                return;
            }
            Account a = new Account();
            a.Display_Name = displayName;
            a.User_Name    = userName;
            a.Password     = password;
            a.Type         = type;

            accountDAO.insert(a);
            toast("Đã thêm tài khoản");

            // quay về danh sách
            clearInputs();
            showForm(false);
            adapter.clearSelection();
            reloadList();
            setMode(Mode.EDIT);
            return;
        }

        if (mode == Mode.EDIT) {
            Account selected = adapter.getSelected();
            if (selected == null) {
                toast("Chọn 1 tài khoản để sửa");
                return;
            }
            if (userName.isEmpty() || password.isEmpty()) {
                toast("Nhập Tên đăng nhập và Mật khẩu");
                return;
            }

            Account exist = accountDAO.findByUsername(selected.User_Name);
            if (exist == null) {
                toast("Không tìm thấy: " + selected.User_Name);
                return;
            }
            // 🔴 KIỂM TRA: username này đã được tài khoản khác dùng chưa?
            int conflict = accountDAO.countByUserNameExceptId(userName, exist.User_Id);
            if (conflict > 0) {
                toast("Tên đăng nhập đã tồn tại ở tài khoản khác, vui lòng chọn tên khác");
                return;
            }
            exist.Display_Name = displayName;
            exist.User_Name    = userName;
            exist.Password     = password;
            exist.Type         = type;
            accountDAO.update(exist);
            toast("Đã cập nhật");

            // quay về danh sách
            clearInputs();
            showForm(false);
            adapter.clearSelection();
            reloadList();
            setMode(Mode.EDIT);
        }
        // Xóa xử lý trong showDeleteDialog()
    }
    private boolean isValidDisplayName(String name) {
        if (name == null) return true;          // hoặc return false nếu bạn bắt buộc phải nhập
        name = name.trim();
        if (name.isEmpty()) return true;        // cho phép bỏ trống
        // Chỉ cho phép: chữ (mọi ngôn ngữ), số, và khoảng trắng
        return name.matches("^[\\p{L}\\p{N} ]+$");
    }
    private void showDeleteDialog(Account target) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        View view = getLayoutInflater().inflate(R.layout.dialog_confirm_delete, null);
        builder.setView(view);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        MaterialButton btnYes = view.findViewById(R.id.btnYes);
        MaterialButton btnNo  = view.findViewById(R.id.btnNo);

        tvMessage.setText("Bạn có chắc muốn xóa tài khoản ID: " + target.User_Id + " ?");

        AlertDialog dialog = builder.create();
        btnYes.setOnClickListener(v -> {
            int rows = accountDAO.deleteById(target.User_Id);
            toast(rows > 0 ? "Đã xóa " + target.User_Name : "Không tìm thấy tài khoản để xóa");

            adapter.clearSelection();
            reloadList();
            dialog.dismiss();
            setMode(Mode.EDIT);
        });

//        btnYes.setOnClickListener(v -> {
//            accountDAO.deleteByUsername(target.User_Name);
//            toast("Đã xóa " + target.User_Name);
//
//            // quay về danh sách
//            adapter.clearSelection();
//            reloadList();
//            dialog.dismiss();
//            setMode(Mode.EDIT);
//        });

        btnNo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private String safe(TextInputEditText ed) {
        return ed.getText() == null ? "" : ed.getText().toString().trim();
    }

    private void toast(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }

    private void updateModeButtons() { /* highlight theo mode nếu muốn */ }
}
