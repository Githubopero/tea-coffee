package com.example.teacoffee.myDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface BillDAO {
    @Query("SELECT * FROM Bill")
    List<Bill> getAll();

    @Query("SELECT * FROM Bill WHERE Table_Id = :tableId AND Status = :status")
    List<Bill> getByTableAndStatus(int tableId, String status);
    @Query("SELECT * FROM Bill WHERE Table_Id = :tableId AND Status = :status LIMIT 1")
    Bill getOpenByTable(int tableId, String status);
    @Insert
    long insert(Bill b);
    @Update void update(Bill b);
    @Delete void delete(Bill b);
}
