package com.example.teacoffee.myDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Food_Category")
public class FoodCategory {
    @PrimaryKey(autoGenerate = true)
    public int Food_Category_Id;

    public String Food_Category_Name;
}
