package com.example.teacoffee.myDB;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteConnectionUtil;
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
public final class AccountDAO_Impl implements AccountDAO {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Account> __insertAdapterOfAccount;

  private final EntityDeleteOrUpdateAdapter<Account> __deleteAdapterOfAccount;

  private final EntityDeleteOrUpdateAdapter<Account> __updateAdapterOfAccount;

  public AccountDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfAccount = new EntityInsertAdapter<Account>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Account` (`User_Id`,`User_Name`,`Password`,`Type`,`Display_Name`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Account entity) {
        statement.bindLong(1, entity.User_Id);
        if (entity.User_Name == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.User_Name);
        }
        if (entity.Password == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.Password);
        }
        if (entity.Type == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.Type);
        }
        if (entity.Display_Name == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.Display_Name);
        }
      }
    };
    this.__deleteAdapterOfAccount = new EntityDeleteOrUpdateAdapter<Account>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Account` WHERE `User_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Account entity) {
        statement.bindLong(1, entity.User_Id);
      }
    };
    this.__updateAdapterOfAccount = new EntityDeleteOrUpdateAdapter<Account>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Account` SET `User_Id` = ?,`User_Name` = ?,`Password` = ?,`Type` = ?,`Display_Name` = ? WHERE `User_Id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Account entity) {
        statement.bindLong(1, entity.User_Id);
        if (entity.User_Name == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.User_Name);
        }
        if (entity.Password == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.Password);
        }
        if (entity.Type == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.Type);
        }
        if (entity.Display_Name == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.Display_Name);
        }
        statement.bindLong(6, entity.User_Id);
      }
    };
  }

  @Override
  public void insert(final Account a) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfAccount.insert(_connection, a);
      return null;
    });
  }

  @Override
  public void delete(final Account a) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfAccount.handle(_connection, a);
      return null;
    });
  }

  @Override
  public void update(final Account a) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfAccount.handle(_connection, a);
      return null;
    });
  }

  @Override
  public List<Account> getAll() {
    final String _sql = "SELECT * FROM Account";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "User_Id");
        final int _columnIndexOfUserName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "User_Name");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Password");
        final int _columnIndexOfType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Type");
        final int _columnIndexOfDisplayName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Display_Name");
        final List<Account> _result = new ArrayList<Account>();
        while (_stmt.step()) {
          final Account _item;
          _item = new Account();
          _item.User_Id = (int) (_stmt.getLong(_columnIndexOfUserId));
          if (_stmt.isNull(_columnIndexOfUserName)) {
            _item.User_Name = null;
          } else {
            _item.User_Name = _stmt.getText(_columnIndexOfUserName);
          }
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _item.Password = null;
          } else {
            _item.Password = _stmt.getText(_columnIndexOfPassword);
          }
          if (_stmt.isNull(_columnIndexOfType)) {
            _item.Type = null;
          } else {
            _item.Type = _stmt.getText(_columnIndexOfType);
          }
          if (_stmt.isNull(_columnIndexOfDisplayName)) {
            _item.Display_Name = null;
          } else {
            _item.Display_Name = _stmt.getText(_columnIndexOfDisplayName);
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
  public Account getById(final int id) {
    final String _sql = "SELECT * FROM Account WHERE User_Id = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "User_Id");
        final int _columnIndexOfUserName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "User_Name");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Password");
        final int _columnIndexOfType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Type");
        final int _columnIndexOfDisplayName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Display_Name");
        final Account _result;
        if (_stmt.step()) {
          _result = new Account();
          _result.User_Id = (int) (_stmt.getLong(_columnIndexOfUserId));
          if (_stmt.isNull(_columnIndexOfUserName)) {
            _result.User_Name = null;
          } else {
            _result.User_Name = _stmt.getText(_columnIndexOfUserName);
          }
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _result.Password = null;
          } else {
            _result.Password = _stmt.getText(_columnIndexOfPassword);
          }
          if (_stmt.isNull(_columnIndexOfType)) {
            _result.Type = null;
          } else {
            _result.Type = _stmt.getText(_columnIndexOfType);
          }
          if (_stmt.isNull(_columnIndexOfDisplayName)) {
            _result.Display_Name = null;
          } else {
            _result.Display_Name = _stmt.getText(_columnIndexOfDisplayName);
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
  public Account login(final String username, final String password) {
    final String _sql = "SELECT * FROM Account WHERE User_Name = ? AND Password = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (username == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, username);
        }
        _argIndex = 2;
        if (password == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, password);
        }
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "User_Id");
        final int _columnIndexOfUserName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "User_Name");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Password");
        final int _columnIndexOfType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Type");
        final int _columnIndexOfDisplayName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Display_Name");
        final Account _result;
        if (_stmt.step()) {
          _result = new Account();
          _result.User_Id = (int) (_stmt.getLong(_columnIndexOfUserId));
          if (_stmt.isNull(_columnIndexOfUserName)) {
            _result.User_Name = null;
          } else {
            _result.User_Name = _stmt.getText(_columnIndexOfUserName);
          }
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _result.Password = null;
          } else {
            _result.Password = _stmt.getText(_columnIndexOfPassword);
          }
          if (_stmt.isNull(_columnIndexOfType)) {
            _result.Type = null;
          } else {
            _result.Type = _stmt.getText(_columnIndexOfType);
          }
          if (_stmt.isNull(_columnIndexOfDisplayName)) {
            _result.Display_Name = null;
          } else {
            _result.Display_Name = _stmt.getText(_columnIndexOfDisplayName);
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
  public int exists(final String username) {
    final String _sql = "SELECT COUNT(*) FROM Account WHERE User_Name = ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (username == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, username);
        }
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
  public Account findByUsername(final String username) {
    final String _sql = "SELECT * FROM Account WHERE User_Name = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (username == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, username);
        }
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "User_Id");
        final int _columnIndexOfUserName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "User_Name");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Password");
        final int _columnIndexOfType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Type");
        final int _columnIndexOfDisplayName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Display_Name");
        final Account _result;
        if (_stmt.step()) {
          _result = new Account();
          _result.User_Id = (int) (_stmt.getLong(_columnIndexOfUserId));
          if (_stmt.isNull(_columnIndexOfUserName)) {
            _result.User_Name = null;
          } else {
            _result.User_Name = _stmt.getText(_columnIndexOfUserName);
          }
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _result.Password = null;
          } else {
            _result.Password = _stmt.getText(_columnIndexOfPassword);
          }
          if (_stmt.isNull(_columnIndexOfType)) {
            _result.Type = null;
          } else {
            _result.Type = _stmt.getText(_columnIndexOfType);
          }
          if (_stmt.isNull(_columnIndexOfDisplayName)) {
            _result.Display_Name = null;
          } else {
            _result.Display_Name = _stmt.getText(_columnIndexOfDisplayName);
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
  public List<Account> getByType(final String type) {
    final String _sql = "SELECT * FROM Account WHERE UPPER(Type) = ? ORDER BY User_Id";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (type == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, type);
        }
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "User_Id");
        final int _columnIndexOfUserName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "User_Name");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Password");
        final int _columnIndexOfType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Type");
        final int _columnIndexOfDisplayName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "Display_Name");
        final List<Account> _result = new ArrayList<Account>();
        while (_stmt.step()) {
          final Account _item;
          _item = new Account();
          _item.User_Id = (int) (_stmt.getLong(_columnIndexOfUserId));
          if (_stmt.isNull(_columnIndexOfUserName)) {
            _item.User_Name = null;
          } else {
            _item.User_Name = _stmt.getText(_columnIndexOfUserName);
          }
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _item.Password = null;
          } else {
            _item.Password = _stmt.getText(_columnIndexOfPassword);
          }
          if (_stmt.isNull(_columnIndexOfType)) {
            _item.Type = null;
          } else {
            _item.Type = _stmt.getText(_columnIndexOfType);
          }
          if (_stmt.isNull(_columnIndexOfDisplayName)) {
            _item.Display_Name = null;
          } else {
            _item.Display_Name = _stmt.getText(_columnIndexOfDisplayName);
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
  public int deleteByUsername(final String username) {
    final String _sql = "DELETE FROM Account WHERE User_Name = ?";
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (username == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, username);
        }
        _stmt.step();
        return SQLiteConnectionUtil.getTotalChangedRows(_connection);
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
