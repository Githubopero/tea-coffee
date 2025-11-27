package com.example.teacoffee.myDB;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "Bill",
        foreignKeys = @ForeignKey(
                entity = TableEntity.class,
                parentColumns = "Table_Id",
                childColumns = "Table_Id",
                onDelete = CASCADE
        ),
        indices = {@Index("Table_Id")}
)
public class Bill {
    @PrimaryKey(autoGenerate = true)
    public int Bill_Id;

    // Dùng epoch millis cho DATETIME
    public Long Date_Checkin;   // null khi chưa mở
    public Long Date_Checkout;  // null khi chưa thanh toán

    public String Status;       // ví dụ: Open/Closed/Cancelled
    public String Note;         // ghi chú
    public Integer Discount;    // %
    public Integer Total_Price; // tổng tiền (sau chiết khấu)

    @ColumnInfo(name = "Table_Id")
    public int Table_Id;
}
