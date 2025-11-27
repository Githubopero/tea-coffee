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
public final class BillInforDAO_Impl implements BillInforDAO {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<BillInfor> __insertAdapterOfBillInfor;

  private final EntityDeleteOrUpdateAdapter<BillInfor> __deleteAdapterOfBillInfor;

  private final EntityDeleteOrUpdateAdapter<BillInfor> __updateAdapterOfBillInfor;

  public BillInforDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfBillInfor = new EntityInsertAdapter<BillInfor>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Bill_Infor` (`Bill_Id`,`Food_Id`,`Count`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final BillInfor entity) {
        statement.bindLong(1, entity.Bill_Id);
        statement.bindLong(2, entity.Food_Id);
        if (entity.Count == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.Count);
        }
      }
    };
    this.__deleteAdapterOfBillInfor = new EntityDeleteOrUpdateAdapter<BillInfor>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Bill_Infor` WHERE `Bill_Id` = ? AND `Food_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final BillInfor entity) {
        statement.bindLong(1, entity.Bill_Id);
        statement.bindLong(2, entity.Food_Id);
      }
    };
    this.__updateAdapterOfBillInfor = new EntityDeleteOrUpdateAdapter<BillInfor>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Bill_Infor` SET `Bill_Id` = ?,`Food_Id` = ?,`Count` = ? WHERE `Bill_Id` = ? AND `Food_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final BillInfor entity) {
        statement.bindLong(1, entity.Bill_Id);
        statement.bindLong(2, entity.Food_Id);
        if (entity.Count == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.Count);
        }
        statement.bindLong(4, entity.Bill_Id);
        statement.bindLong(5, entity.Food_Id);
      }
    };
  }

  @Override
  public void insert(final BillInfor bi) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfBillInfor.insert(_connection, bi);
      return null;
    });
  }

  @Override
  public void delete(final BillInfor bi) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfBillInfor.handle(_connection, bi);
      return null;
    });
  }

  @Override
  public void update(final BillInfor bi) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfBillInfor.handle(_connection, bi);
      return null;
    });
  }

  @Override
  public BillInfor findByBillAndFood(final int billId, final int foodId) {
    final String _sql = "SELECT * FROM Bill_Infor WHERE Bill_Id = ? AND Food_Id = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, billId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, foodId);
        final int _columnIndexOfBillId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Bill_Id");
        final int _columnIndexOfFoodId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Id");
        final int _columnIndexOfCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Count");
        final BillInfor _result;
        if (_stmt.step()) {
          _result = new BillInfor();
          _result.Bill_Id = (int) (_stmt.getLong(_columnIndexOfBillId));
          _result.Food_Id = (int) (_stmt.getLong(_columnIndexOfFoodId));
          if (_stmt.isNull(_columnIndexOfCount)) {
            _result.Count = null;
          } else {
            _result.Count = (int) (_stmt.getLong(_columnIndexOfCount));
          }
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<BillInfor> getByBillId(final int billId) {
    final String _sql = "SELECT * FROM Bill_Infor WHERE Bill_Id = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, billId);
        final int _columnIndexOfBillId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Bill_Id");
        final int _columnIndexOfFoodId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Food_Id");
        final int _columnIndexOfCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Count");
        final List<BillInfor> _result = new ArrayList<BillInfor>();
        while (_stmt.step()) {
          final BillInfor _item;
          _item = new BillInfor();
          _item.Bill_Id = (int) (_stmt.getLong(_columnIndexOfBillId));
          _item.Food_Id = (int) (_stmt.getLong(_columnIndexOfFoodId));
          if (_stmt.isNull(_columnIndexOfCount)) {
            _item.Count = null;
          } else {
            _item.Count = (int) (_stmt.getLong(_columnIndexOfCount));
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteByBill(final int billId) {
    final String _sql = "DELETE FROM Bill_Infor WHERE Bill_Id = ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, billId);
        _stmt.step();
        return null;
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
