package com.example.teacoffee.myDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AccountDAO {
    // Lấy toàn bộ danh sách tài khoản trong bảng Account.
    //   SELECT * FROM Account
    @Query("SELECT * FROM Account")
    List<Account> getAll();
    //Lấy 1 tài khoản theo User_Id (khóa chính).
    //Nếu không tìm thấy thì Room trả về null.
    @Query("SELECT * FROM Account WHERE User_Id = :id LIMIT 1")
    Account getById(int id);

    //Dùng để đăng nhập:
    //Tìm tài khoản khớp cả User_Name và Password.
    //LIMIT 1: lấy tối đa 1 dòng.
    // Trả về 1 tài khoản khớp username + password (nếu không có -> null)
    @Query("SELECT * FROM Account WHERE User_Name = :username AND Password = :password LIMIT 1")
    Account login(String username, String password);

    // kiểm tra tồn tại username
    @Query("SELECT COUNT(*) FROM Account WHERE User_Name = :username")
    int exists(String username);

    // Khớp AdminHomeActivity ===
    //Tìm 1 tài khoản theo username (không kiểm tra password).
    //Dùng khi cần load thông tin tài khoản theo username.
    @Query("SELECT * FROM Account WHERE User_Name = :username LIMIT 1")
    Account findByUsername(String username);
    //Lấy danh sách tài khoản theo loại (Type) : "ADMIN" hoặc "STAFF".
    @Query("SELECT * FROM Account WHERE UPPER(Type) = :type ORDER BY User_Id")
    List<Account> getByType(String type);
    //Xóa tài khoản theo User_Id.
    //Trả về số dòng bị xóa 0/1.
    @Query("DELETE FROM Account WHERE User_Id = :id")
    int deleteById(int id);
    //Đếm số tài khoản có username này .
    @Query("SELECT COUNT(*) FROM Account WHERE User_Name = :username")
    int countByUserName(String username);
    // Đếm số tài khoản có username này nhưng KHÔNG phải tài khoản hiện tại (User_Id != :id).
    // Dùng khi chỉnh sửa (update) tài khoản (username sau chỉnh sửa không được trùng )
    @Query("SELECT COUNT(*) FROM Account WHERE User_Name = :username AND User_Id != :id")
    int countByUserNameExceptId(String username, int id);
    //Thêm 1 tài khoản mới vào bảng Account.
    @Insert void insert(Account a);
    //Cập nhật 1 tài khoản đã tồn tại
    @Update void update(Account a);
    //Xóa 1 tài khoản theo object Account.
    @Delete void delete(Account a);
}
