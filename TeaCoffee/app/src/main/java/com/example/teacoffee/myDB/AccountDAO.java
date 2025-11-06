package com.example.teacoffee.myDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AccountDAO {
    @Query("SELECT * FROM Account")
    List<Account> getAll();

    @Query("SELECT * FROM Account WHERE User_Id = :id LIMIT 1")
    Account getById(int id);
    // Trả về 1 tài khoản khớp username + password (nếu không có -> null)
    @Query("SELECT * FROM Account WHERE User_Name = :username AND Password = :password LIMIT 1")
    Account login(String username, String password);

    // (Tuỳ chọn) kiểm tra tồn tại username
    @Query("SELECT COUNT(*) FROM Account WHERE User_Name = :username")
    int exists(String username);
    // === Thêm để khớp AdminHomeActivity ===
    @Query("SELECT * FROM Account WHERE User_Name = :username LIMIT 1")
    Account findByUsername(String username);
    @Query("SELECT * FROM Account WHERE UPPER(Type) = :type ORDER BY User_Id")
    List<Account> getByType(String type); // type: "ADMIN" | "STAFF"
    @Query("DELETE FROM Account WHERE User_Name = :username")
    int deleteByUsername(String username);
    @Insert void insert(Account a);
    @Update void update(Account a);
    @Delete void delete(Account a);
}
