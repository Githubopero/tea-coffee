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
public final class BillDAO_Impl implements BillDAO {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Bill> __insertAdapterOfBill;

  private final EntityDeleteOrUpdateAdapter<Bill> __deleteAdapterOfBill;

  private final EntityDeleteOrUpdateAdapter<Bill> __updateAdapterOfBill;

  public BillDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfBill = new EntityInsertAdapter<Bill>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Bill` (`Bill_Id`,`Date_Checkin`,`Date_Checkout`,`Status`,`Note`,`Discount`,`Total_Price`,`Table_Id`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Bill entity) {
        statement.bindLong(1, entity.Bill_Id);
        if (entity.Date_Checkin == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.Date_Checkin);
        }
        if (entity.Date_Checkout == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.Date_Checkout);
        }
        if (entity.Status == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.Status);
        }
        if (entity.Note == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.Note);
        }
        if (entity.Discount == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.Discount);
        }
        if (entity.Total_Price == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.Total_Price);
        }
        statement.bindLong(8, entity.Table_Id);
      }
    };
    this.__deleteAdapterOfBill = new EntityDeleteOrUpdateAdapter<Bill>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Bill` WHERE `Bill_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Bill entity) {
        statement.bindLong(1, entity.Bill_Id);
      }
    };
    this.__updateAdapterOfBill = new EntityDeleteOrUpdateAdapter<Bill>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Bill` SET `Bill_Id` = ?,`Date_Checkin` = ?,`Date_Checkout` = ?,`Status` = ?,`Note` = ?,`Discount` = ?,`Total_Price` = ?,`Table_Id` = ? WHERE `Bill_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Bill entity) {
        statement.bindLong(1, entity.Bill_Id);
        if (entity.Date_Checkin == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.Date_Checkin);
        }
        if (entity.Date_Checkout == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.Date_Checkout);
        }
        if (entity.Status == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.Status);
        }
        if (entity.Note == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.Note);
        }
        if (entity.Discount == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.Discount);
        }
        if (entity.Total_Price == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.Total_Price);
        }
        statement.bindLong(8, entity.Table_Id);
        statement.bindLong(9, entity.Bill_Id);
      }
    };
  }

  @Override
  public long insert(final Bill b) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfBill.insertAndReturnId(_connection, b);
    });
  }

  @Override
  public void delete(final Bill b) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfBill.handle(_connection, b);
      return null;
    });
  }

  @Override
  public void update(final Bill b) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfBill.handle(_connection, b);
      return null;
    });
  }

  @Override
  public List<Bill> getAll() {
    final String _sql = "SELECT * FROM Bill";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfBillId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Bill_Id");
        final int _columnIndexOfDateCheckin = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkin");
        final int _columnIndexOfDateCheckout = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkout");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Status");
        final int _columnIndexOfNote = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Note");
        final int _columnIndexOfDiscount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Discount");
        final int _columnIndexOfTotalPrice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Total_Price");
        final int _columnIndexOfTableId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Table_Id");
        final List<Bill> _result = new ArrayList<Bill>();
        while (_stmt.step()) {
          final Bill _item;
          _item = new Bill();
          _item.Bill_Id = (int) (_stmt.getLong(_columnIndexOfBillId));
          if (_stmt.isNull(_columnIndexOfDateCheckin)) {
            _item.Date_Checkin = null;
          } else {
            _item.Date_Checkin = _stmt.getLong(_columnIndexOfDateCheckin);
          }
          if (_stmt.isNull(_columnIndexOfDateCheckout)) {
            _item.Date_Checkout = null;
          } else {
            _item.Date_Checkout = _stmt.getLong(_columnIndexOfDateCheckout);
          }
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _item.Status = null;
          } else {
            _item.Status = _stmt.getText(_columnIndexOfStatus);
          }
          if (_stmt.isNull(_columnIndexOfNote)) {
            _item.Note = null;
          } else {
            _item.Note = _stmt.getText(_columnIndexOfNote);
          }
          if (_stmt.isNull(_columnIndexOfDiscount)) {
            _item.Discount = null;
          } else {
            _item.Discount = (int) (_stmt.getLong(_columnIndexOfDiscount));
          }
          if (_stmt.isNull(_columnIndexOfTotalPrice)) {
            _item.Total_Price = null;
          } else {
            _item.Total_Price = (int) (_stmt.getLong(_columnIndexOfTotalPrice));
          }
          _item.Table_Id = (int) (_stmt.getLong(_columnIndexOfTableId));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Bill> getByTableAndStatus(final int tableId, final String status) {
    final String _sql = "SELECT * FROM Bill WHERE Table_Id = ? AND Status = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tableId);
        _argIndex = 2;
        if (status == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, status);
        }
        final int _columnIndexOfBillId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Bill_Id");
        final int _columnIndexOfDateCheckin = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkin");
        final int _columnIndexOfDateCheckout = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkout");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Status");
        final int _columnIndexOfNote = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Note");
        final int _columnIndexOfDiscount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Discount");
        final int _columnIndexOfTotalPrice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Total_Price");
        final int _columnIndexOfTableId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Table_Id");
        final List<Bill> _result = new ArrayList<Bill>();
        while (_stmt.step()) {
          final Bill _item;
          _item = new Bill();
          _item.Bill_Id = (int) (_stmt.getLong(_columnIndexOfBillId));
          if (_stmt.isNull(_columnIndexOfDateCheckin)) {
            _item.Date_Checkin = null;
          } else {
            _item.Date_Checkin = _stmt.getLong(_columnIndexOfDateCheckin);
          }
          if (_stmt.isNull(_columnIndexOfDateCheckout)) {
            _item.Date_Checkout = null;
          } else {
            _item.Date_Checkout = _stmt.getLong(_columnIndexOfDateCheckout);
          }
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _item.Status = null;
          } else {
            _item.Status = _stmt.getText(_columnIndexOfStatus);
          }
          if (_stmt.isNull(_columnIndexOfNote)) {
            _item.Note = null;
          } else {
            _item.Note = _stmt.getText(_columnIndexOfNote);
          }
          if (_stmt.isNull(_columnIndexOfDiscount)) {
            _item.Discount = null;
          } else {
            _item.Discount = (int) (_stmt.getLong(_columnIndexOfDiscount));
          }
          if (_stmt.isNull(_columnIndexOfTotalPrice)) {
            _item.Total_Price = null;
          } else {
            _item.Total_Price = (int) (_stmt.getLong(_columnIndexOfTotalPrice));
          }
          _item.Table_Id = (int) (_stmt.getLong(_columnIndexOfTableId));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Bill getOpenByTable(final int tableId, final String status) {
    final String _sql = "SELECT * FROM Bill WHERE Table_Id = ? AND Status = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tableId);
        _argIndex = 2;
        if (status == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, status);
        }
        final int _columnIndexOfBillId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Bill_Id");
        final int _columnIndexOfDateCheckin = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkin");
        final int _columnIndexOfDateCheckout = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkout");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Status");
        final int _columnIndexOfNote = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Note");
        final int _columnIndexOfDiscount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Discount");
        final int _columnIndexOfTotalPrice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Total_Price");
        final int _columnIndexOfTableId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Table_Id");
        final Bill _result;
        if (_stmt.step()) {
          _result = new Bill();
          _result.Bill_Id = (int) (_stmt.getLong(_columnIndexOfBillId));
          if (_stmt.isNull(_columnIndexOfDateCheckin)) {
            _result.Date_Checkin = null;
          } else {
            _result.Date_Checkin = _stmt.getLong(_columnIndexOfDateCheckin);
          }
          if (_stmt.isNull(_columnIndexOfDateCheckout)) {
            _result.Date_Checkout = null;
          } else {
            _result.Date_Checkout = _stmt.getLong(_columnIndexOfDateCheckout);
          }
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _result.Status = null;
          } else {
            _result.Status = _stmt.getText(_columnIndexOfStatus);
          }
          if (_stmt.isNull(_columnIndexOfNote)) {
            _result.Note = null;
          } else {
            _result.Note = _stmt.getText(_columnIndexOfNote);
          }
          if (_stmt.isNull(_columnIndexOfDiscount)) {
            _result.Discount = null;
          } else {
            _result.Discount = (int) (_stmt.getLong(_columnIndexOfDiscount));
          }
          if (_stmt.isNull(_columnIndexOfTotalPrice)) {
            _result.Total_Price = null;
          } else {
            _result.Total_Price = (int) (_stmt.getLong(_columnIndexOfTotalPrice));
          }
          _result.Table_Id = (int) (_stmt.getLong(_columnIndexOfTableId));
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
  public List<Bill> getClosedBillsInPeriod(final long startTime, final long endTime) {
    final String _sql = "SELECT * FROM Bill WHERE Status = '1' AND Date_Checkout BETWEEN ? AND ? ORDER BY Date_Checkout DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, startTime);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, endTime);
        final int _columnIndexOfBillId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Bill_Id");
        final int _columnIndexOfDateCheckin = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkin");
        final int _columnIndexOfDateCheckout = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkout");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Status");
        final int _columnIndexOfNote = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Note");
        final int _columnIndexOfDiscount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Discount");
        final int _columnIndexOfTotalPrice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Total_Price");
        final int _columnIndexOfTableId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Table_Id");
        final List<Bill> _result = new ArrayList<Bill>();
        while (_stmt.step()) {
          final Bill _item;
          _item = new Bill();
          _item.Bill_Id = (int) (_stmt.getLong(_columnIndexOfBillId));
          if (_stmt.isNull(_columnIndexOfDateCheckin)) {
            _item.Date_Checkin = null;
          } else {
            _item.Date_Checkin = _stmt.getLong(_columnIndexOfDateCheckin);
          }
          if (_stmt.isNull(_columnIndexOfDateCheckout)) {
            _item.Date_Checkout = null;
          } else {
            _item.Date_Checkout = _stmt.getLong(_columnIndexOfDateCheckout);
          }
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _item.Status = null;
          } else {
            _item.Status = _stmt.getText(_columnIndexOfStatus);
          }
          if (_stmt.isNull(_columnIndexOfNote)) {
            _item.Note = null;
          } else {
            _item.Note = _stmt.getText(_columnIndexOfNote);
          }
          if (_stmt.isNull(_columnIndexOfDiscount)) {
            _item.Discount = null;
          } else {
            _item.Discount = (int) (_stmt.getLong(_columnIndexOfDiscount));
          }
          if (_stmt.isNull(_columnIndexOfTotalPrice)) {
            _item.Total_Price = null;
          } else {
            _item.Total_Price = (int) (_stmt.getLong(_columnIndexOfTotalPrice));
          }
          _item.Table_Id = (int) (_stmt.getLong(_columnIndexOfTableId));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Bill> getOpenBills() {
    final String _sql = "SELECT * FROM Bill WHERE Status = '0' ORDER BY Date_Checkin DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfBillId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Bill_Id");
        final int _columnIndexOfDateCheckin = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkin");
        final int _columnIndexOfDateCheckout = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkout");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Status");
        final int _columnIndexOfNote = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Note");
        final int _columnIndexOfDiscount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Discount");
        final int _columnIndexOfTotalPrice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Total_Price");
        final int _columnIndexOfTableId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Table_Id");
        final List<Bill> _result = new ArrayList<Bill>();
        while (_stmt.step()) {
          final Bill _item;
          _item = new Bill();
          _item.Bill_Id = (int) (_stmt.getLong(_columnIndexOfBillId));
          if (_stmt.isNull(_columnIndexOfDateCheckin)) {
            _item.Date_Checkin = null;
          } else {
            _item.Date_Checkin = _stmt.getLong(_columnIndexOfDateCheckin);
          }
          if (_stmt.isNull(_columnIndexOfDateCheckout)) {
            _item.Date_Checkout = null;
          } else {
            _item.Date_Checkout = _stmt.getLong(_columnIndexOfDateCheckout);
          }
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _item.Status = null;
          } else {
            _item.Status = _stmt.getText(_columnIndexOfStatus);
          }
          if (_stmt.isNull(_columnIndexOfNote)) {
            _item.Note = null;
          } else {
            _item.Note = _stmt.getText(_columnIndexOfNote);
          }
          if (_stmt.isNull(_columnIndexOfDiscount)) {
            _item.Discount = null;
          } else {
            _item.Discount = (int) (_stmt.getLong(_columnIndexOfDiscount));
          }
          if (_stmt.isNull(_columnIndexOfTotalPrice)) {
            _item.Total_Price = null;
          } else {
            _item.Total_Price = (int) (_stmt.getLong(_columnIndexOfTotalPrice));
          }
          _item.Table_Id = (int) (_stmt.getLong(_columnIndexOfTableId));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Bill> getClosedBills() {
    final String _sql = "SELECT * FROM Bill WHERE Status = '1' ORDER BY Date_Checkout DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfBillId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Bill_Id");
        final int _columnIndexOfDateCheckin = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkin");
        final int _columnIndexOfDateCheckout = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Date_Checkout");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Status");
        final int _columnIndexOfNote = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Note");
        final int _columnIndexOfDiscount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Discount");
        final int _columnIndexOfTotalPrice = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Total_Price");
        final int _columnIndexOfTableId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Table_Id");
        final List<Bill> _result = new ArrayList<Bill>();
        while (_stmt.step()) {
          final Bill _item;
          _item = new Bill();
          _item.Bill_Id = (int) (_stmt.getLong(_columnIndexOfBillId));
          if (_stmt.isNull(_columnIndexOfDateCheckin)) {
            _item.Date_Checkin = null;
          } else {
            _item.Date_Checkin = _stmt.getLong(_columnIndexOfDateCheckin);
          }
          if (_stmt.isNull(_columnIndexOfDateCheckout)) {
            _item.Date_Checkout = null;
          } else {
            _item.Date_Checkout = _stmt.getLong(_columnIndexOfDateCheckout);
          }
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _item.Status = null;
          } else {
            _item.Status = _stmt.getText(_columnIndexOfStatus);
          }
          if (_stmt.isNull(_columnIndexOfNote)) {
            _item.Note = null;
          } else {
            _item.Note = _stmt.getText(_columnIndexOfNote);
          }
          if (_stmt.isNull(_columnIndexOfDiscount)) {
            _item.Discount = null;
          } else {
            _item.Discount = (int) (_stmt.getLong(_columnIndexOfDiscount));
          }
          if (_stmt.isNull(_columnIndexOfTotalPrice)) {
            _item.Total_Price = null;
          } else {
            _item.Total_Price = (int) (_stmt.getLong(_columnIndexOfTotalPrice));
          }
          _item.Table_Id = (int) (_stmt.getLong(_columnIndexOfTableId));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<FoodSalesStats> getSoldFoodsInPeriod(final long startTime, final long endTime) {
    final String _sql = "SELECT f.Food_Name AS foodName, SUM(bi.Count) AS quantity FROM Bill_Infor bi JOIN Food f ON bi.Food_Id = f.Food_Id JOIN Bill b ON bi.Bill_Id = b.Bill_Id WHERE b.Status = '1' AND b.Date_Checkout BETWEEN ? AND ? GROUP BY f.Food_Id, f.Food_Name ORDER BY quantity DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, startTime);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, endTime);
        final int _columnIndexOfFoodName = 0;
        final int _columnIndexOfQuantity = 1;
        final List<FoodSalesStats> _result = new ArrayList<FoodSalesStats>();
        while (_stmt.step()) {
          final FoodSalesStats _item;
          final String _tmpFoodName;
          if (_stmt.isNull(_columnIndexOfFoodName)) {
            _tmpFoodName = null;
          } else {
            _tmpFoodName = _stmt.getText(_columnIndexOfFoodName);
          }
          final long _tmpQuantity;
          _tmpQuantity = _stmt.getLong(_columnIndexOfQuantity);
          _item = new FoodSalesStats(_tmpFoodName,_tmpQuantity);
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
