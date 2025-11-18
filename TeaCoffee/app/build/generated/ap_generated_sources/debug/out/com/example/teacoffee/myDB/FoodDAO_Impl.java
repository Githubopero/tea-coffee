package com.example.teacoffee.myDB;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class FoodDAO_Impl implements FoodDAO {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Food> __insertAdapterOfFood;

  private final EntityDeleteOrUpdateAdapter<Food> __deleteAdapterOfFood;

  private final EntityDeleteOrUpdateAdapter<Food> __updateAdapterOfFood;

  public FoodDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfFood = new EntityInsertAdapter<Food>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Food` (`Food_Id`,`Food_Name`,`Food_Image`,`Price`,`Food_Category_Id`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Food entity) {
        statement.bindLong(1, entity.Food_Id);
        if (entity.Food_Name == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.Food_Name);
        }
        if (entity.Food_Image == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.Food_Image);
        }
        statement.bindLong(4, entity.Price);
        statement.bindLong(5, entity.Food_Category_Id);
      }
    };
    this.__deleteAdapterOfFood = new EntityDeleteOrUpdateAdapter<Food>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Food` WHERE `Food_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Food entity) {
        statement.bindLong(1, entity.Food_Id);
      }
    };
    this.__updateAdapterOfFood = new EntityDeleteOrUpdateAdapter<Food>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Food` SET `Food_Id` = ?,`Food_Name` = ?,`Food_Image` = ?,`Price` = ?,`Food_Category_Id` = ? WHERE `Food_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Food entity) {
        statement.bindLong(1, entity.Food_Id);
        if (entity.Food_Name == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.Food_Name);
        }
        if (entity.Food_Image == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.Food_Image);
        }
        statement.bindLong(4, entity.Price);
        statement.bindLong(5, entity.Food_Category_Id);
        statement.bindLong(6, entity.Food_Id);
      }
    };
  }

  @Override
  public void insert(final Food f) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfFood.insert(_connection, f);
      return null;
    });
  }

  @Override
  public void delete(final Food f) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfFood.handle(_connection, f);
      return null;
    });
  }

  @Override
  public void update(final Food f) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfFood.handle(_connection, f);
      return null;
    });
  }

  @Override
  public List<Food> getAll() {
    final String _sql = "SELECT * FROM Food";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfFoodId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Id");
        final int _columnIndexOfFoodName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Name");
        final int _columnIndexOfFoodImage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Image");
        final int _columnIndexOfPrice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Price");
        final int _columnIndexOfFoodCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Category_Id");
        final List<Food> _result = new ArrayList<Food>();
        while (_stmt.step()) {
          final Food _item;
          _item = new Food();
          _item.Food_Id = (int) (_stmt.getLong(_columnIndexOfFoodId));
          if (_stmt.isNull(_columnIndexOfFoodName)) {
            _item.Food_Name = null;
          } else {
            _item.Food_Name = _stmt.getText(_columnIndexOfFoodName);
          }
          if (_stmt.isNull(_columnIndexOfFoodImage)) {
            _item.Food_Image = null;
          } else {
            _item.Food_Image = _stmt.getText(_columnIndexOfFoodImage);
          }
          _item.Price = (int) (_stmt.getLong(_columnIndexOfPrice));
          _item.Food_Category_Id = (int) (_stmt.getLong(_columnIndexOfFoodCategoryId));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Food> getByCategory(final int categoryId) {
    final String _sql = "SELECT * FROM Food WHERE Food_Category_Id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, categoryId);
        final int _columnIndexOfFoodId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Id");
        final int _columnIndexOfFoodName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Name");
        final int _columnIndexOfFoodImage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Image");
        final int _columnIndexOfPrice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Price");
        final int _columnIndexOfFoodCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Category_Id");
        final List<Food> _result = new ArrayList<Food>();
        while (_stmt.step()) {
          final Food _item;
          _item = new Food();
          _item.Food_Id = (int) (_stmt.getLong(_columnIndexOfFoodId));
          if (_stmt.isNull(_columnIndexOfFoodName)) {
            _item.Food_Name = null;
          } else {
            _item.Food_Name = _stmt.getText(_columnIndexOfFoodName);
          }
          if (_stmt.isNull(_columnIndexOfFoodImage)) {
            _item.Food_Image = null;
          } else {
            _item.Food_Image = _stmt.getText(_columnIndexOfFoodImage);
          }
          _item.Price = (int) (_stmt.getLong(_columnIndexOfPrice));
          _item.Food_Category_Id = (int) (_stmt.getLong(_columnIndexOfFoodCategoryId));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public int countAll() {
    final String _sql = "SELECT COUNT(*) FROM Food";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _result;
        if (_stmt.step()) {
          _result = (int) (_stmt.getLong(0));
        } else {
          _result = 0;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Food getById(final int id) {
    final String _sql = "SELECT * FROM Food WHERE Food_Id = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfFoodId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Id");
        final int _columnIndexOfFoodName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Name");
        final int _columnIndexOfFoodImage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Image");
        final int _columnIndexOfPrice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Price");
        final int _columnIndexOfFoodCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Category_Id");
        final Food _result;
        if (_stmt.step()) {
          _result = new Food();
          _result.Food_Id = (int) (_stmt.getLong(_columnIndexOfFoodId));
          if (_stmt.isNull(_columnIndexOfFoodName)) {
            _result.Food_Name = null;
          } else {
            _result.Food_Name = _stmt.getText(_columnIndexOfFoodName);
          }
          if (_stmt.isNull(_columnIndexOfFoodImage)) {
            _result.Food_Image = null;
          } else {
            _result.Food_Image = _stmt.getText(_columnIndexOfFoodImage);
          }
          _result.Price = (int) (_stmt.getLong(_columnIndexOfPrice));
          _result.Food_Category_Id = (int) (_stmt.getLong(_columnIndexOfFoodCategoryId));
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
