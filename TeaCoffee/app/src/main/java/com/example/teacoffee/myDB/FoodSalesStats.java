package com.example.teacoffee.myDB;

public class FoodSalesStats {
    public String foodName;
    public long quantity;

    // Nếu muốn dùng với RecyclerView sau này thì thêm constructor
    public FoodSalesStats(String foodName, long quantity) {
        this.foodName = foodName;
        this.quantity = quantity;
    }
}
