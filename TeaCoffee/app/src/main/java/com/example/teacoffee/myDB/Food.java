package com.example.teacoffee.myDB;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "Food",
        foreignKeys = @ForeignKey(
                entity = FoodCategory.class,
                parentColumns = "Food_Category_Id",
                childColumns = "Food_Category_Id",
                onDelete = CASCADE
        ),
        indices = {@Index("Food_Category_Id")}
)
public class Food {
    @PrimaryKey(autoGenerate = true)
    public int Food_Id;

    public String Food_Name;
    public String Food_Image; // path/URL nếu có
    public int Price;

    @ColumnInfo(name = "Food_Category_Id")
    public int Food_Category_Id;
}
