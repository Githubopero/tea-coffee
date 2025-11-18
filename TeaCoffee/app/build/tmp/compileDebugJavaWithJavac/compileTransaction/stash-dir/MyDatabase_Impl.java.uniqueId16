package com.example.teacoffee.myDB;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class MyDatabase_Impl extends MyDatabase {
  private volatile AccountDAO _accountDAO;

  private volatile TableDAO _tableDAO;

  private volatile BillDAO _billDAO;

  private volatile BillInforDAO _billInforDAO;

  private volatile FoodDAO _foodDAO;

  private volatile FoodCategoryDAO _foodCategoryDAO;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "022198bc499b952d81dc9518d42d5410", "76197f6ecbd12d17b8001b1d67ecf408") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `Account` (`User_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `User_Name` TEXT, `Password` TEXT, `Type` TEXT, `Display_Name` TEXT)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `Cafe_Table` (`Table_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Table_Name` TEXT, `Status` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `Bill` (`Bill_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Date_Checkin` INTEGER, `Date_Checkout` INTEGER, `Status` TEXT, `Note` TEXT, `Discount` INTEGER, `Total_Price` INTEGER, `Table_Id` INTEGER NOT NULL, FOREIGN KEY(`Table_Id`) REFERENCES `Cafe_Table`(`Table_Id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_Bill_Table_Id` ON `Bill` (`Table_Id`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `Bill_Infor` (`Bill_Id` INTEGER NOT NULL, `Food_Id` INTEGER NOT NULL, `Count` INTEGER, PRIMARY KEY(`Bill_Id`, `Food_Id`), FOREIGN KEY(`Bill_Id`) REFERENCES `Bill`(`Bill_Id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`Food_Id`) REFERENCES `Food`(`Food_Id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_Bill_Infor_Bill_Id` ON `Bill_Infor` (`Bill_Id`)");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_Bill_Infor_Food_Id` ON `Bill_Infor` (`Food_Id`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `Food` (`Food_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Food_Name` TEXT, `Food_Image` TEXT, `Price` INTEGER NOT NULL, `Food_Category_Id` INTEGER NOT NULL, FOREIGN KEY(`Food_Category_Id`) REFERENCES `Food_Category`(`Food_Category_Id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        SQLite.execSQL(connection, "CREATE INDEX IF NOT EXISTS `index_Food_Food_Category_Id` ON `Food` (`Food_Category_Id`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `Food_Category` (`Food_Category_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Food_Category_Name` TEXT)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '022198bc499b952d81dc9518d42d5410')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `Account`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `Cafe_Table`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `Bill`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `Bill_Infor`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `Food`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `Food_Category`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsAccount = new HashMap<String, TableInfo.Column>(5);
        _columnsAccount.put("User_Id", new TableInfo.Column("User_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccount.put("User_Name", new TableInfo.Column("User_Name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccount.put("Password", new TableInfo.Column("Password", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccount.put("Type", new TableInfo.Column("Type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccount.put("Display_Name", new TableInfo.Column("Display_Name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysAccount = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesAccount = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccount = new TableInfo("Account", _columnsAccount, _foreignKeysAccount, _indicesAccount);
        final TableInfo _existingAccount = TableInfo.read(connection, "Account");
        if (!_infoAccount.equals(_existingAccount)) {
          return new RoomOpenDelegate.ValidationResult(false, "Account(com.example.teacoffee.myDB.Account).\n"
                  + " Expected:\n" + _infoAccount + "\n"
                  + " Found:\n" + _existingAccount);
        }
        final Map<String, TableInfo.Column> _columnsCafeTable = new HashMap<String, TableInfo.Column>(3);
        _columnsCafeTable.put("Table_Id", new TableInfo.Column("Table_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCafeTable.put("Table_Name", new TableInfo.Column("Table_Name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCafeTable.put("Status", new TableInfo.Column("Status", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysCafeTable = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesCafeTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCafeTable = new TableInfo("Cafe_Table", _columnsCafeTable, _foreignKeysCafeTable, _indicesCafeTable);
        final TableInfo _existingCafeTable = TableInfo.read(connection, "Cafe_Table");
        if (!_infoCafeTable.equals(_existingCafeTable)) {
          return new RoomOpenDelegate.ValidationResult(false, "Cafe_Table(com.example.teacoffee.myDB.TableEntity).\n"
                  + " Expected:\n" + _infoCafeTable + "\n"
                  + " Found:\n" + _existingCafeTable);
        }
        final Map<String, TableInfo.Column> _columnsBill = new HashMap<String, TableInfo.Column>(8);
        _columnsBill.put("Bill_Id", new TableInfo.Column("Bill_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBill.put("Date_Checkin", new TableInfo.Column("Date_Checkin", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBill.put("Date_Checkout", new TableInfo.Column("Date_Checkout", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBill.put("Status", new TableInfo.Column("Status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBill.put("Note", new TableInfo.Column("Note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBill.put("Discount", new TableInfo.Column("Discount", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBill.put("Total_Price", new TableInfo.Column("Total_Price", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBill.put("Table_Id", new TableInfo.Column("Table_Id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysBill = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBill.add(new TableInfo.ForeignKey("Cafe_Table", "CASCADE", "NO ACTION", Arrays.asList("Table_Id"), Arrays.asList("Table_Id")));
        final Set<TableInfo.Index> _indicesBill = new HashSet<TableInfo.Index>(1);
        _indicesBill.add(new TableInfo.Index("index_Bill_Table_Id", false, Arrays.asList("Table_Id"), Arrays.asList("ASC")));
        final TableInfo _infoBill = new TableInfo("Bill", _columnsBill, _foreignKeysBill, _indicesBill);
        final TableInfo _existingBill = TableInfo.read(connection, "Bill");
        if (!_infoBill.equals(_existingBill)) {
          return new RoomOpenDelegate.ValidationResult(false, "Bill(com.example.teacoffee.myDB.Bill).\n"
                  + " Expected:\n" + _infoBill + "\n"
                  + " Found:\n" + _existingBill);
        }
        final Map<String, TableInfo.Column> _columnsBillInfor = new HashMap<String, TableInfo.Column>(3);
        _columnsBillInfor.put("Bill_Id", new TableInfo.Column("Bill_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillInfor.put("Food_Id", new TableInfo.Column("Food_Id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillInfor.put("Count", new TableInfo.Column("Count", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysBillInfor = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysBillInfor.add(new TableInfo.ForeignKey("Bill", "CASCADE", "NO ACTION", Arrays.asList("Bill_Id"), Arrays.asList("Bill_Id")));
        _foreignKeysBillInfor.add(new TableInfo.ForeignKey("Food", "CASCADE", "NO ACTION", Arrays.asList("Food_Id"), Arrays.asList("Food_Id")));
        final Set<TableInfo.Index> _indicesBillInfor = new HashSet<TableInfo.Index>(2);
        _indicesBillInfor.add(new TableInfo.Index("index_Bill_Infor_Bill_Id", false, Arrays.asList("Bill_Id"), Arrays.asList("ASC")));
        _indicesBillInfor.add(new TableInfo.Index("index_Bill_Infor_Food_Id", false, Arrays.asList("Food_Id"), Arrays.asList("ASC")));
        final TableInfo _infoBillInfor = new TableInfo("Bill_Infor", _columnsBillInfor, _foreignKeysBillInfor, _indicesBillInfor);
        final TableInfo _existingBillInfor = TableInfo.read(connection, "Bill_Infor");
        if (!_infoBillInfor.equals(_existingBillInfor)) {
          return new RoomOpenDelegate.ValidationResult(false, "Bill_Infor(com.example.teacoffee.myDB.BillInfor).\n"
                  + " Expected:\n" + _infoBillInfor + "\n"
                  + " Found:\n" + _existingBillInfor);
        }
        final Map<String, TableInfo.Column> _columnsFood = new HashMap<String, TableInfo.Column>(5);
        _columnsFood.put("Food_Id", new TableInfo.Column("Food_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("Food_Name", new TableInfo.Column("Food_Name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("Food_Image", new TableInfo.Column("Food_Image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("Price", new TableInfo.Column("Price", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFood.put("Food_Category_Id", new TableInfo.Column("Food_Category_Id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysFood = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysFood.add(new TableInfo.ForeignKey("Food_Category", "CASCADE", "NO ACTION", Arrays.asList("Food_Category_Id"), Arrays.asList("Food_Category_Id")));
        final Set<TableInfo.Index> _indicesFood = new HashSet<TableInfo.Index>(1);
        _indicesFood.add(new TableInfo.Index("index_Food_Food_Category_Id", false, Arrays.asList("Food_Category_Id"), Arrays.asList("ASC")));
        final TableInfo _infoFood = new TableInfo("Food", _columnsFood, _foreignKeysFood, _indicesFood);
        final TableInfo _existingFood = TableInfo.read(connection, "Food");
        if (!_infoFood.equals(_existingFood)) {
          return new RoomOpenDelegate.ValidationResult(false, "Food(com.example.teacoffee.myDB.Food).\n"
                  + " Expected:\n" + _infoFood + "\n"
                  + " Found:\n" + _existingFood);
        }
        final Map<String, TableInfo.Column> _columnsFoodCategory = new HashMap<String, TableInfo.Column>(2);
        _columnsFoodCategory.put("Food_Category_Id", new TableInfo.Column("Food_Category_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFoodCategory.put("Food_Category_Name", new TableInfo.Column("Food_Category_Name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysFoodCategory = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesFoodCategory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFoodCategory = new TableInfo("Food_Category", _columnsFoodCategory, _foreignKeysFoodCategory, _indicesFoodCategory);
        final TableInfo _existingFoodCategory = TableInfo.read(connection, "Food_Category");
        if (!_infoFoodCategory.equals(_existingFoodCategory)) {
          return new RoomOpenDelegate.ValidationResult(false, "Food_Category(com.example.teacoffee.myDB.FoodCategory).\n"
                  + " Expected:\n" + _infoFoodCategory + "\n"
                  + " Found:\n" + _existingFoodCategory);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Account", "Cafe_Table", "Bill", "Bill_Infor", "Food", "Food_Category");
  }

  @Override
  public void clearAllTables() {
    super.performClear(true, "Account", "Cafe_Table", "Bill", "Bill_Infor", "Food", "Food_Category");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AccountDAO.class, AccountDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(TableDAO.class, TableDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(BillDAO.class, BillDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(BillInforDAO.class, BillInforDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(FoodDAO.class, FoodDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(FoodCategoryDAO.class, FoodCategoryDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AccountDAO getAccountDAO() {
    if (_accountDAO != null) {
      return _accountDAO;
    } else {
      synchronized(this) {
        if(_accountDAO == null) {
          _accountDAO = new AccountDAO_Impl(this);
        }
        return _accountDAO;
      }
    }
  }

  @Override
  public TableDAO getTableDAO() {
    if (_tableDAO != null) {
      return _tableDAO;
    } else {
      synchronized(this) {
        if(_tableDAO == null) {
          _tableDAO = new TableDAO_Impl(this);
        }
        return _tableDAO;
      }
    }
  }

  @Override
  public BillDAO getBillDAO() {
    if (_billDAO != null) {
      return _billDAO;
    } else {
      synchronized(this) {
        if(_billDAO == null) {
          _billDAO = new BillDAO_Impl(this);
        }
        return _billDAO;
      }
    }
  }

  @Override
  public BillInforDAO getBillInforDAO() {
    if (_billInforDAO != null) {
      return _billInforDAO;
    } else {
      synchronized(this) {
        if(_billInforDAO == null) {
          _billInforDAO = new BillInforDAO_Impl(this);
        }
        return _billInforDAO;
      }
    }
  }

  @Override
  public FoodDAO getFoodDAO() {
    if (_foodDAO != null) {
      return _foodDAO;
    } else {
      synchronized(this) {
        if(_foodDAO == null) {
          _foodDAO = new FoodDAO_Impl(this);
        }
        return _foodDAO;
      }
    }
  }

  @Override
  public FoodCategoryDAO getFoodCategoryDAO() {
    if (_foodCategoryDAO != null) {
      return _foodCategoryDAO;
    } else {
      synchronized(this) {
        if(_foodCategoryDAO == null) {
          _foodCategoryDAO = new FoodCategoryDAO_Impl(this);
        }
        return _foodCategoryDAO;
      }
    }
  }
}
