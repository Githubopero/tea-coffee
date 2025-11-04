package com.example.teacoffee.myDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TableDAO {

    // Lấy tất cả bàn
    @Query("SELECT * FROM Cafe_Table")
    List<TableEntity> getAll();

    // Lấy danh sách bàn theo trạng thái (0 = trống, 1 = đã có khách)
    @Query("SELECT * FROM Cafe_Table WHERE Status = :status")
    List<TableEntity> getByStatus(int status);

    // Thêm bàn mới
    @Insert
    void insert(TableEntity t);

    // Cập nhật thông tin bàn
    @Update
    void update(TableEntity t);

    // Xóa bàn
    @Delete
    void delete(TableEntity t);
}
