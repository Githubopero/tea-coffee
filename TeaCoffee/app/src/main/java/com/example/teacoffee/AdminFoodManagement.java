package com.example.teacoffee;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.teacoffee.myDB.Food;
import com.example.teacoffee.myDB.FoodCategory;
import com.example.teacoffee.myDB.FoodCategoryDAO;
import com.example.teacoffee.myDB.FoodDAO;
import com.example.teacoffee.myDB.MyDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AdminFoodManagement extends AppCompatActivity {

    enum Mode { ADD, EDIT, DELETE, VIEW }
    private Mode mode = Mode.VIEW;

    private FoodDAO foodDAO;
    private FoodCategoryDAO categoryDAO;

    private View foodFormContainer;
    private TextInputEditText etFoodName, etPrice;
    private MaterialAutoCompleteTextView spCategory, spCategoryFilter;
    private MaterialButtonToggleGroup modeGroup;
    private MaterialButton btnAddSP, btnEditSP, btnDeleteSP;
    private MaterialButton btnConfirmFoodAction, btnCloseForm;
    private ListView lvFoodList;

    private Food selectedFood = null;
    private ArrayAdapter<String> foodAdapter;
    private List<Food> foodList = new ArrayList<>();
    private List<FoodCategory> categoryList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter, filterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_management);

        initDB();
        initViews();
        setupCategorySpinners();
        setupModeButtons();
        setupListView();

        refreshFoodList(null);


        // ✅ GỌI NavigationHelper để quản lý việc chuyển đổi Activity
        NavigationHelper.setupAdminTabs(this, R.id.btnTabProduct);
    }

    private void initDB() {
        MyDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                MyDatabase.class,
                "CafeManagement.db"
        ).allowMainThreadQueries().build();

        foodDAO = db.getFoodDAO();
        categoryDAO = db.getFoodCategoryDAO();
    }

    private void initViews() {
        foodFormContainer = findViewById(R.id.foodFormContainer);
        etFoodName = findViewById(R.id.etFoodName);
        etPrice = findViewById(R.id.etFoodPrice);
        spCategory = findViewById(R.id.spFoodCategory);
        spCategoryFilter = findViewById(R.id.spCategoryFilter);

        modeGroup = findViewById(R.id.modeGroup);
        btnAddSP = findViewById(R.id.btnAddSP);
        btnEditSP = findViewById(R.id.btnEditSP);
        btnDeleteSP = findViewById(R.id.btnDeleteSP);
        btnConfirmFoodAction = findViewById(R.id.btnConfirmFoodAction);

        lvFoodList = findViewById(R.id.lvFoodList);

        // Tạo nút "Đóng form"
        btnCloseForm = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
        btnCloseForm.setText("Đóng");
        btnCloseForm.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        ((android.widget.LinearLayout) foodFormContainer).addView(btnCloseForm);

        etPrice.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    // chỉ cho nhập số 0-9
                    if (!source.toString().matches("[0-9]*")) {
                        return "";
                    }
                    return null;
                }
        });

        btnCloseForm.setOnClickListener(v -> {
            clearForm();
            showForm(false);
            mode = Mode.VIEW;
        });
    }

    private void setupCategorySpinners() {
        categoryList = categoryDAO.getAll();

        List<String> filterNames = new ArrayList<>();
        filterNames.add("Tất cả");
        for (FoodCategory c : categoryList) filterNames.add(c.Food_Category_Name);
        filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filterNames);
        spCategoryFilter.setAdapter(filterAdapter);

        List<String> categoryNames = new ArrayList<>();
        for (FoodCategory c : categoryList) categoryNames.add(c.Food_Category_Name);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryNames);
        spCategory.setAdapter(categoryAdapter);

        spCategoryFilter.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) refreshFoodList(null);
            else refreshFoodList(categoryList.get(position - 1));
        });
    }

    private void setupModeButtons() {
        modeGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.btnAddSP) {
                mode = Mode.ADD;
                showForm(true);
                clearForm();
                btnConfirmFoodAction.setText("Thêm");
                Toast.makeText(this, "Chế độ: Thêm sản phẩm", Toast.LENGTH_SHORT).show();

                btnConfirmFoodAction.setOnClickListener(v -> handleAddProduct());

            } else if (checkedId == R.id.btnEditSP) {
                mode = Mode.EDIT;
                showForm(false);
                Toast.makeText(this, "Chế độ: Sửa sản phẩm", Toast.LENGTH_SHORT).show();

            } else if (checkedId == R.id.btnDeleteSP) {
                mode = Mode.DELETE;
                showForm(false);
                Toast.makeText(this, "Chế độ: Xóa sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListView() {
        foodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        lvFoodList.setAdapter(foodAdapter);

        lvFoodList.setOnItemClickListener((parent, view, position, id) -> {
            selectedFood = foodList.get(position);

            if (mode == Mode.EDIT) {
                showForm(true);
                btnConfirmFoodAction.setText("Lưu");
                etFoodName.setText(selectedFood.Food_Name);
                etPrice.setText(String.valueOf(selectedFood.Price));
                spCategory.setText(getCategoryNameById(selectedFood.Food_Category_Id), false);

                btnConfirmFoodAction.setOnClickListener(v -> handleEditProduct());
            } else if (mode == Mode.DELETE) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa món \"" + selectedFood.Food_Name + "\" không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            foodDAO.delete(selectedFood);
                            Toast.makeText(this, "Đã xóa món!", Toast.LENGTH_SHORT).show();
                            refreshFoodList(null);
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                        .show();
            }

        });
    }

    private void handleAddProduct() {
        String name = etFoodName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String categoryName = spCategory.getText().toString();

        if (name.isEmpty() || priceStr.isEmpty() || categoryName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryId = getCategoryIdByName(categoryName);
        if (categoryId == -1) {
            Toast.makeText(this, "Danh mục không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Kiểm tra trùng tên (không phân biệt hoa thường)
        Food existing = foodDAO.getByName(name);
        if (existing != null) {
            Toast.makeText(this, "Tên sản phẩm đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() > 50) {
            Toast.makeText(this, "Tên sản phẩm quá dài! (tối đa 50 ký tự)", Toast.LENGTH_SHORT).show();
            return;
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận thêm")
                .setMessage("Bạn có chắc muốn thêm món \"" + name + "\" không?")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    Food f = new Food();
                    f.Food_Name = name;
                    f.Price = Integer.parseInt(priceStr);
                    f.Food_Category_Id = categoryId;
                    f.Food_Image = null;

                    foodDAO.insert(f);
                    Toast.makeText(this, "Đã thêm sản phẩm mới!", Toast.LENGTH_SHORT).show();
                    refreshFoodList(null);
                    clearForm();
                    showForm(false);
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void handleEditProduct() {
        if (selectedFood == null) return;

        String name = etFoodName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String categoryName = spCategory.getText().toString();

        if (name.isEmpty() || priceStr.isEmpty() || categoryName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length() > 50) {
            Toast.makeText(this, "Tên sản phẩm quá dài! (tối đa 50 ký tự)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra trùng tên trừ chính nó
        Food existing = foodDAO.getByName(name);
        if (existing != null && existing.Food_Id != selectedFood.Food_Id) {
            Toast.makeText(this, "Tên sản phẩm đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryId = getCategoryIdByName(categoryName);

        new android.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận sửa")
                .setMessage("Lưu thay đổi cho món \"" + selectedFood.Food_Name + "\"?")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    selectedFood.Food_Name = name;
                    selectedFood.Price = Integer.parseInt(priceStr);
                    selectedFood.Food_Category_Id = categoryId;

                    foodDAO.update(selectedFood);
                    Toast.makeText(this, "Đã lưu thay đổi!", Toast.LENGTH_SHORT).show();
                    refreshFoodList(null);
                    showForm(false);
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void refreshFoodList(FoodCategory filter) {
        if (filter == null) foodList = foodDAO.getAll();
        else foodList = foodDAO.getByCategory(filter.Food_Category_Id);

        List<String> names = new ArrayList<>();
        for (Food f : foodList) {
            String categoryName = getCategoryNameById(f.Food_Category_Id);
            names.add(f.Food_Id +"." + f.Food_Name + " - " + f.Price + "đ (" + categoryName + ")");
        }

        foodAdapter.clear();
        foodAdapter.addAll(names);
        foodAdapter.notifyDataSetChanged();
    }

    private void clearForm() {
        etFoodName.setText("");
        etPrice.setText("");
        spCategory.setText("");
        selectedFood = null;
    }

    private void showForm(boolean visible) {
        foodFormContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private int getCategoryIdByName(String name) {
        for (FoodCategory c : categoryList)
            if (c.Food_Category_Name.equals(name)) return c.Food_Category_Id;
        return -1;
    }

    private String getCategoryNameById(int id) {
        for (FoodCategory c : categoryList)
            if (c.Food_Category_Id == id) return c.Food_Category_Name;
        return "";
    }
}
