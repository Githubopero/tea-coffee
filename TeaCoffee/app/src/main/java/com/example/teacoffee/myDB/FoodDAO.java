package com.example.teacoffee.myDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface FoodDAO {
    @Query("SELECT * FROM Food")
    List<Food> getAll();

    @Query("SELECT * FROM Food WHERE Food_Category_Id = :categoryId")
    List<Food> getByCategory(int categoryId);
    @Query("SELECT COUNT(*) FROM Food")
    int countAll();
    @Query("SELECT * FROM Food WHERE Food_Id = :id LIMIT 1")
    Food getById(int id);
    @Query("SELECT * FROM Food WHERE Food_Name = :name LIMIT 1")
    Food getByName(String name);
    @Insert void insert(Food f);
    @Update void update(Food f);
    @Delete void delete(Food f);
    @Query("SELECT COUNT(*) FROM Food WHERE Food_Category_Id = :categoryId")
    int countFoodsByCategoryId(int categoryId);
}
