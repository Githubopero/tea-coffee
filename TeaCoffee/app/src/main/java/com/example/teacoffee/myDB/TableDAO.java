package com.example.teacoffee.myDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TableDAO {
    @Query("SELECT * FROM Cafe_Table")
    List<TableEntity> getAll();

    @Query("SELECT * FROM Cafe_Table WHERE Status = :status")
    List<TableEntity> getByStatus(int status);

    // Thứ tự tham số phải khớp tên placeholder hoặc chuyển thành (status, id)
    @Query("UPDATE Cafe_Table SET Status = :status WHERE Table_Id = :id")
    void updateStatus(int status, int id);
    //thêm bàn theo id
    @Query("SELECT * FROM Cafe_Table WHERE Table_Id = :id LIMIT 1")
    TableEntity getById(int id);

    @Insert void insert(TableEntity t);
    @Update void update(TableEntity t);
    @Delete void delete(TableEntity t);
}
