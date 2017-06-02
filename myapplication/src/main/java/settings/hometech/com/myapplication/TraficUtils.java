package settings.hometech.com.myapplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.INetworkManagementService;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuyimin on 2017/6/2.
 */

public class TraficUtils {
    public static void setFirewallUidChainRule(int uid, int networkType, boolean allow) {
        try {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{"network_management"});
            INetworkManagementService service = INetworkManagementService.Stub.asInterface(binder);
            if (service != null) {
                service.setFirewallUidChainRule(uid, networkType, allow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAppInfos(Context context, DBManager mgr) {
        PackageManager pm = context.getPackageManager();
        //List<PackageInfo>  packgeInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        List<PackageInfo> packgeInfos = pm.getInstalledPackages(0);
        ArrayList<ApplicationInfo> appInfos = new ArrayList<ApplicationInfo>();
        // 获取应用程序的名称，不是包名，而是清单文件中的labelname
        //String str_name = packageInfo.applicationInfo.loadLabel(pm).toString();
        //appInfo.setAppName(str_name);
        //
        for (PackageInfo packgeInfo : packgeInfos) {
            //String appName = packgeInfo.applicationInfo.loadLabel(pm).toString();
            //String packageName = packgeInfo.packageName;
            //Drawable drawable = packgeInfo.applicationInfo.loadIcon(pm);
            int uid = packgeInfo.applicationInfo.uid;
            //true means switch is on
            String gprs = "true";
            String wifi = "true";
            byte[] icon = BitmapToBytes(drawableToBitmap(packgeInfo.applicationInfo.loadIcon(pm)));
            String backData = "true";
            String roamData = "true";
            String packageName = packgeInfo.packageName;
            String isSystemApp = "false";
            if (uid < 10000) {
                isSystemApp = "true";
            } else {
                isSystemApp = "false";
            }
            String appName = packgeInfo.applicationInfo.loadLabel(pm).toString();
            ;
            ApplicationInfo appInfo = new ApplicationInfo(uid, gprs, wifi, icon, backData, roamData, packageName, isSystemApp, appName);
            appInfos.add(appInfo);
        }
        mgr.addApplication(appInfos);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[] BitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String getProgramNameByPackageName(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }


}
