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
public final class FoodCategoryDAO_Impl implements FoodCategoryDAO {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<FoodCategory> __insertAdapterOfFoodCategory;

  private final EntityDeleteOrUpdateAdapter<FoodCategory> __deleteAdapterOfFoodCategory;

  private final EntityDeleteOrUpdateAdapter<FoodCategory> __updateAdapterOfFoodCategory;

  public FoodCategoryDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfFoodCategory = new EntityInsertAdapter<FoodCategory>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Food_Category` (`Food_Category_Id`,`Food_Category_Name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final FoodCategory entity) {
        statement.bindLong(1, entity.Food_Category_Id);
        if (entity.Food_Category_Name == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.Food_Category_Name);
        }
      }
    };
    this.__deleteAdapterOfFoodCategory = new EntityDeleteOrUpdateAdapter<FoodCategory>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Food_Category` WHERE `Food_Category_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final FoodCategory entity) {
        statement.bindLong(1, entity.Food_Category_Id);
      }
    };
    this.__updateAdapterOfFoodCategory = new EntityDeleteOrUpdateAdapter<FoodCategory>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Food_Category` SET `Food_Category_Id` = ?,`Food_Category_Name` = ? WHERE `Food_Category_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final FoodCategory entity) {
        statement.bindLong(1, entity.Food_Category_Id);
        if (entity.Food_Category_Name == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.Food_Category_Name);
        }
        statement.bindLong(3, entity.Food_Category_Id);
      }
    };
  }

  @Override
  public void insert(final FoodCategory c) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfFoodCategory.insert(_connection, c);
      return null;
    });
  }

  @Override
  public void delete(final FoodCategory c) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfFoodCategory.handle(_connection, c);
      return null;
    });
  }

  @Override
  public void update(final FoodCategory c) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfFoodCategory.handle(_connection, c);
      return null;
    });
  }

  @Override
  public List<FoodCategory> getAll() {
    final String _sql = "SELECT * FROM Food_Category";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfFoodCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Category_Id");
        final int _columnIndexOfFoodCategoryName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Category_Name");
        final List<FoodCategory> _result = new ArrayList<FoodCategory>();
        while (_stmt.step()) {
          final FoodCategory _item;
          _item = new FoodCategory();
          _item.Food_Category_Id = (int) (_stmt.getLong(_columnIndexOfFoodCategoryId));
          if (_stmt.isNull(_columnIndexOfFoodCategoryName)) {
            _item.Food_Category_Name = null;
          } else {
            _item.Food_Category_Name = _stmt.getText(_columnIndexOfFoodCategoryName);
          }
          _result.add(_item);
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
