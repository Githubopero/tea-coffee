package com.example.teacoffee.myDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Account")
public class Account {
    @PrimaryKey(autoGenerate = true)
    public int User_Id;

    public String User_Name;
    public String Password;
    public String Type;         // ví dụ: admin/staff
    public String Display_Name;
}
