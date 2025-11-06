package com.example.teacoffee.myDB;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(
        entities = {
                Account.class,
                TableEntity.class,
                Bill.class,
                BillInfor.class,
                Food.class,
                FoodCategory.class
        },
        version = 1,
        exportSchema = false
)
public abstract class MyDatabase extends RoomDatabase {

    // Expose tất cả DAO
    public abstract AccountDAO getAccountDAO();
    public abstract TableDAO getTableDAO();
    public abstract BillDAO getBillDAO();
    public abstract BillInforDAO getBillInforDAO();
    public abstract FoodDAO getFoodDAO();
    public abstract FoodCategoryDAO getFoodCategoryDAO();

    private static volatile MyDatabase INSTANCE;

    public static MyDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    MyDatabase.class,
                                    "CafeManagement.db"
                            )
                            .allowMainThreadQueries()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);

                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        // ===== 0) Account mẫu =====
                                        // admin / 123456
                                        Account admin = new Account();
                                        admin.User_Name = "admin";
                                        admin.Password  = "123456";
                                        admin.Type      = "ADMIN";
                                        admin.Display_Name = "Quản trị viên";
                                        getINSTANCE(context).getAccountDAO().insert(admin);

                                        // user / 123456
                                        Account user = new Account();
                                        user.User_Name = "user";
                                        user.Password  = "123456";
                                        user.Type      = "STAFF";
                                        user.Display_Name = "Nhân Viên";
                                        getINSTANCE(context).getAccountDAO().insert(user);
                                        // ===== 1) 20 bàn =====
                                        for (int i = 1; i <= 20; i++) {
                                            // Vì bạn đang dùng @Entity(tableName = "Cafe_Table") + Status int
                                            db.execSQL("INSERT INTO `Cafe_Table`(Table_Name, Status) VALUES(?, ?)",
                                                    new Object[]{"Bàn " + i, 0});
                                        }

                                        // ===== 2) Danh mục =====
                                        String[] cats = {"Coffee","Trà","Nước ép","Sinh tố","Soda","Đồ ăn vặt","Khác"};
                                        for (String c : cats) {
                                            db.execSQL("INSERT INTO Food_Category(Food_Category_Name) VALUES(?)",
                                                    new Object[]{c});
                                        }

                                        // ===== 3) Món ăn (cat id theo thứ tự đã insert) =====
                                        // Helper insert
                                        insertFood(db, 1, "Cà phê đen",      50000);
                                        insertFood(db, 1, "Cà phê sữa đá",   30000);
                                        insertFood(db, 1, "Capuchino",       35000);
                                        insertFood(db, 1, "Latte",           35000);
                                        insertFood(db, 1, "Cà phê muối",     45000);
                                        insertFood(db, 1, "Espresso",        30000);

                                        insertFood(db, 2, "Trà đào",         30000);
                                        insertFood(db, 2, "Trà sữa",         30000);
                                        insertFood(db, 2, "Trà lài",         25000);
                                        insertFood(db, 2, "Trà ô long",      30000);
                                        insertFood(db, 2, "Trà chanh",       20000);

                                        insertFood(db, 3, "Nước ép táo",     30000);
                                        insertFood(db, 3, "Nước ép cam",     30000);
                                        insertFood(db, 3, "Nước ép dứa",     30000);
                                        insertFood(db, 3, "Nước ép cà rốt",  25000);
                                        insertFood(db, 3, "Nước ép ổi",      25000);

                                        insertFood(db, 4, "Sinh tố bơ",      35000);
                                        insertFood(db, 4, "Sinh tố xoài",    35000);
                                        insertFood(db, 4, "Sinh tố dâu",     35000);
                                        insertFood(db, 4, "Sinh tố chuối",   30000);
                                        insertFood(db, 4, "Sinh tố việt quất",40000);

                                        insertFood(db, 5, "Soda việt quất",  30000);
                                        insertFood(db, 5, "Soda bạc hà",     30000);
                                        insertFood(db, 5, "Soda chanh",      25000);
                                        insertFood(db, 5, "Sting lon",       15000);
                                        insertFood(db, 5, "7Up lon",         15000);

                                        insertFood(db, 6, "Bò khô",          50000);
                                        insertFood(db, 6, "Khoai tây chiên", 35000);
                                        insertFood(db, 6, "Xúc xích",        30000);
                                        insertFood(db, 6, "Bánh ngọt",       30000);
                                        insertFood(db, 6, "Hạt hướng dương", 20000);

                                        insertFood(db, 7, "Nước suối",       10000);
                                        insertFood(db, 7, "Yaourt",          15000);
                                        insertFood(db, 7, "Sữa tươi",        20000);
                                        insertFood(db, 7, "Cacao nóng",      30000);
                                        insertFood(db, 7, "Trà gừng nóng",   25000);
                                    });
                                }
                            })
                            .build();

                }
            }
        }
        return INSTANCE;
    }
    private static void insertFood(SupportSQLiteDatabase db, int catId, String name, int price) {
        db.execSQL("INSERT INTO Food(Food_Name, Food_Image, Price, Food_Category_Id) VALUES(?, ?, ?, ?)",
                new Object[]{name, "", price, catId});
    }


}
