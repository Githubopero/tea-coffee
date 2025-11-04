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

    @Insert void insert(Account a);
    @Update void update(Account a);
    @Delete void delete(Account a);
}
