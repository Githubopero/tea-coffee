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
public final class TableDAO_Impl implements TableDAO {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TableEntity> __insertAdapterOfTableEntity;

  private final EntityDeleteOrUpdateAdapter<TableEntity> __deleteAdapterOfTableEntity;

  private final EntityDeleteOrUpdateAdapter<TableEntity> __updateAdapterOfTableEntity;

  public TableDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTableEntity = new EntityInsertAdapter<TableEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Cafe_Table` (`Table_Id`,`Table_Name`,`Status`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final TableEntity entity) {
        statement.bindLong(1, entity.Table_Id);
        if (entity.Table_Name == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.Table_Name);
        }
        statement.bindLong(3, entity.Status);
      }
    };
    this.__deleteAdapterOfTableEntity = new EntityDeleteOrUpdateAdapter<TableEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Cafe_Table` WHERE `Table_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final TableEntity entity) {
        statement.bindLong(1, entity.Table_Id);
      }
    };
    this.__updateAdapterOfTableEntity = new EntityDeleteOrUpdateAdapter<TableEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Cafe_Table` SET `Table_Id` = ?,`Table_Name` = ?,`Status` = ? WHERE `Table_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final TableEntity entity) {
        statement.bindLong(1, entity.Table_Id);
        if (entity.Table_Name == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.Table_Name);
        }
        statement.bindLong(3, entity.Status);
        statement.bindLong(4, entity.Table_Id);
      }
    };
  }

  @Override
  public void insert(final TableEntity t) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTableEntity.insert(_connection, t);
      return null;
    });
  }

  @Override
  public void delete(final TableEntity t) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfTableEntity.handle(_connection, t);
      return null;
    });
  }

  @Override
  public void update(final TableEntity t) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfTableEntity.handle(_connection, t);
      return null;
    });
  }

  @Override
  public List<TableEntity> getAll() {
    final String _sql = "SELECT * FROM Cafe_Table";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfTableId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Table_Id");
        final int _columnIndexOfTableName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Table_Name");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Status");
        final List<TableEntity> _result = new ArrayList<TableEntity>();
        while (_stmt.step()) {
          final TableEntity _item;
          _item = new TableEntity();
          _item.Table_Id = (int) (_stmt.getLong(_columnIndexOfTableId));
          if (_stmt.isNull(_columnIndexOfTableName)) {
            _item.Table_Name = null;
          } else {
            _item.Table_Name = _stmt.getText(_columnIndexOfTableName);
          }
          _item.Status = (int) (_stmt.getLong(_columnIndexOfStatus));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<TableEntity> getByStatus(final int status) {
    final String _sql = "SELECT * FROM Cafe_Table WHERE Status = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, status);
        final int _columnIndexOfTableId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Table_Id");
        final int _columnIndexOfTableName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Table_Name");
        final int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Status");
        final List<TableEntity> _result = new ArrayList<TableEntity>();
        while (_stmt.step()) {
          final TableEntity _item;
          _item = new TableEntity();
          _item.Table_Id = (int) (_stmt.getLong(_columnIndexOfTableId));
          if (_stmt.isNull(_columnIndexOfTableName)) {
            _item.Table_Name = null;
          } else {
            _item.Table_Name = _stmt.getText(_columnIndexOfTableName);
          }
          _item.Status = (int) (_stmt.getLong(_columnIndexOfStatus));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void updateStatus(final int status, final int id) {
    final String _sql = "UPDATE Cafe_Table SET Status = ? WHERE Table_Id = ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, status);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
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
