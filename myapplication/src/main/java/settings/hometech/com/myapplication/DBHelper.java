package settings.hometech.com.myapplication;

/**
 * Created by zhuyimin on 2017/5/24.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "appNetInfo.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS application" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, uid INTEGER, gprs TEXT, wifi TEXT,icon BLOB,backData TEXT,roamData TEXT,packageName TEXT,isSystemApp TEXT,appName TEXT)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
    }

}