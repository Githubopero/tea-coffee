package com.example.teacoffee.myDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface BillInforDAO {
    @Query("SELECT * FROM Bill_Infor WHERE Bill_Id = :billId")
    List<BillInfor> getByBill(int billId);

    @Insert void insert(BillInfor bi);
    @Update void update(BillInfor bi);
    @Delete void delete(BillInfor bi);

    // Xoá toàn bộ chi tiết của một bill
    @Query("DELETE FROM Bill_Infor WHERE Bill_Id = :billId")
    void deleteByBill(int billId);
}
