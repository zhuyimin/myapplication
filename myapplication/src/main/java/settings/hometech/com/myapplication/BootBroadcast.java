package settings.hometech.com.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Broadcast receiver that set iptables rules on system startup.
 * This is necessary because the rules are not persistent.
 */
public class BootBroadcast extends BroadcastReceiver {
    private boolean mFirst;
    private Context mContext;
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private DBManager mgr;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        mContext = context;
        mgr = new DBManager(mContext);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            mFirst = isFirstEnter(context);
            Log.i("yimin", "isFirst boot :" + mFirst);
            if (mFirst) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("my_pref", context.MODE_PRIVATE);
                sharedPreferences.edit().putString(KEY_GUIDE_ACTIVITY, "false").commit();
                TraficUtils.saveAppInfos(mContext, mgr);
                Log.i("yimin", "first in---add database data");
            } else {
                Log.i("yimin", "not first boot");
                Intent resetService = new Intent(context, ResetDataService.class);
                context.startService(resetService);
            }
        }
    }

    private boolean isFirstEnter(Context context) {
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)
                .getString(KEY_GUIDE_ACTIVITY, "true");//取得所有类名 如 com.my.MainActivity
        if (mResultStr.equalsIgnoreCase("true"))
            return true;
        else
            return false;
    }

}
