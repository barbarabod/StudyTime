package com.example.studytimer.dboperation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.studytimer.dboperation.dbobjects.Times;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "study_time.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        try {
            TableUtils.createTable(cs, Times.class);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {

    }

    public List<Times> getAllTimes() throws SQLException {
        Dao<Times, Integer> dao = getDao(Times.class);
        return dao.queryForAll();
    }

    public void create(Times times) throws SQLException {
        getDao(Times.class).create(times);
    }
}
