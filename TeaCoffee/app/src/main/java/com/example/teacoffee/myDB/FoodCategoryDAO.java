package com.example.teacoffee.myDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface FoodCategoryDAO {
    @Query("SELECT * FROM Food_Category")
    List<FoodCategory> getAll();

    @Insert void insert(FoodCategory c);
    @Update void update(FoodCategory c);
    @Delete void delete(FoodCategory c);
}

