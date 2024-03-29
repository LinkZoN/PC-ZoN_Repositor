package dataLayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "SmartTimesheet.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	Context currentContext;

	// the DAO object we use to access the TimesheetEntity table
	private Dao<EntityTimesheet, Integer> TimesheetEntity_Dao = null;
	private RuntimeExceptionDao<EntityTimesheet, Long> TimesheetEntity_RuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		currentContext = context;
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		
		try {
		
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, EntityTimesheet.class);
		
		} catch (SQLException e) {
		
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		
		}

	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			
			// Version 1
			if (oldVersion < 1) {

				TableUtils.dropTable(connectionSource, EntityTimesheet.class,
						true);
				onCreate(db, connectionSource);

			}
			
			// Version 2
			if (oldVersion < 2) {

			}
			
			// Version ...
			

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It
	 * will create it or just give the cached value.
	 */
	public Dao<EntityTimesheet, Integer> getTimesheetEntity_Dao()
			throws SQLException {
		if (TimesheetEntity_Dao == null) {
			TimesheetEntity_Dao = getDao(EntityTimesheet.class);
		}
		return TimesheetEntity_Dao;
	}

	/**
	 * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao
	 * for our SimpleData class. It will create it or just give the cached
	 * value. RuntimeExceptionDao only through RuntimeExceptions.
	 */
	public RuntimeExceptionDao<EntityTimesheet, Long> getTimesheetEntity_DataDao() {
		if (TimesheetEntity_RuntimeDao == null) {
			TimesheetEntity_RuntimeDao = getRuntimeExceptionDao(EntityTimesheet.class);
		}
		return TimesheetEntity_RuntimeDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		TimesheetEntity_RuntimeDao = null;
		Log.d(DatabaseHelper.class.getName(), "Database Closed!");
	}

}
