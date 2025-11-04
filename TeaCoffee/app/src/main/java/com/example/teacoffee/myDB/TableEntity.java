package com.example.teacoffee.myDB;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cafe_Table")
public class TableEntity {
    @PrimaryKey(autoGenerate = true) public int Table_Id;
    public String Table_Name;
    public int Status; // 0/1
}
