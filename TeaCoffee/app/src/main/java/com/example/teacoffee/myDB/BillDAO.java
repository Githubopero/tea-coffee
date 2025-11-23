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



    // ✅ TRUY VẤN ĐÃ SỬA: SỬ DỤNG Status = '1' cho hóa đơn đã đóng
    @Query("SELECT * FROM Bill WHERE Status = '1' AND Date_Checkout BETWEEN :startTime AND :endTime ORDER BY Date_Checkout DESC")
    List<Bill> getClosedBillsInPeriod(long startTime, long endTime);

    // Lấy tất cả hóa đơn chưa thanh toán
    @Query("SELECT * FROM Bill WHERE Status = '0' ORDER BY Date_Checkin DESC")
    List<Bill> getOpenBills();

    // Lấy tất cả hóa đơn đã thanh toán
    @Query("SELECT * FROM Bill WHERE Status = '1' ORDER BY Date_Checkout DESC")
    List<Bill> getClosedBills();


}
