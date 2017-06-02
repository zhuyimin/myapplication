package settings.hometech.com.myapplication;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by zhuyimin on 2017/5/31.
 */

public class ApplicationProviderMetaData {

    private final static String AUTHORITY = "settings.hometech.com.myapplication";
    public static final String TABLE_NAME = "application";
    public static final String COLUMN_APPLICATION_UID = "uid";
    public static final String COLUMN_ID = "_id";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/application");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/application";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/application";


}