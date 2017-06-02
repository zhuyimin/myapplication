package settings.hometech.com.myapplication;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class NetworkManagerActivity extends Activity {

    private DBManager mgr;
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private LinearLayout textView1 = null;
    private LinearLayout textView2 = null;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private ActionBar myactionbar;
    private TextView myactionbar_title;
    private ImageButton myactionbar_back_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netmork_manager);
        initCustomActionBar();
        mgr = new DBManager(this);
        fragmentManager = getFragmentManager();
        textView1 = (LinearLayout) findViewById(R.id.normal_app);
        textView2 = (LinearLayout) findViewById(R.id.system_app);
        fragment = new FragmentFactory().getInstanceByIndex(1);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setBackgroundResource(R.drawable.tab_bg_click);
                textView2.setBackgroundResource(R.drawable.tab_bg_not_click);
                fragment = new FragmentFactory().getInstanceByIndex(1);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, fragment);
                transaction.commit();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setBackgroundResource(R.drawable.tab_bg_not_click);
                textView2.setBackgroundResource(R.drawable.tab_bg_click);
                fragment = new FragmentFactory().getInstanceByIndex(2);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, fragment);
                transaction.commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
    }

    public void add(View view) {
        ArrayList<ApplicationInfo> persons = new ArrayList<ApplicationInfo>();
        ApplicationInfo person1 = new ApplicationInfo(10000, "true", "true", null, "false", "false", null, "true", null);
        persons.add(person1);
        mgr.addApplication(persons);
    }

    public void update(View view) {
        ApplicationInfo person = new ApplicationInfo();
        person.gprs = "true";
        person.wifi = "false";
        mgr.updateApplicationInfo(person);
    }

    public void delete(View view) {
        ApplicationInfo person = new ApplicationInfo();
        person.uid = 10000;
        mgr.deleteApplication(person);
    }

    public void query(View view) {
        List<ApplicationInfo> persons = mgr.queryApplication();
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (ApplicationInfo person : persons) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("gprs", person.gprs);
            map.put("wifi", person.wifi);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,
                new String[]{"gprs", "wifi"}, new int[]{android.R.id.text1, android.R.id.text2});
    }

    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> packlist = pManager.getInstalledPackages(0);
        for (int i = 0; i < packlist.size(); i++) {
            PackageInfo pak = (PackageInfo) packlist.get(i);
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // 添加自己已经安装的应用程序
                apps.add(pak);
            }

        }
        return apps;
    }
    /*
    Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {//覆盖handleMessage方法
            switch (msg.what) {//根据收到的消息的what类型处理
                case SAVE_DATABASE:
                    //saveAppInfos();
                    saveDatabaseTask = new SaveDatabaseTask();
                    saveDatabaseTask.execute();
                    break;
                case SAVE_DATABASE_SUCCESS:
                    progressDialog.dismiss();
                    fragment = new FragmentFactory().getInstanceByIndex(1);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.content, fragment);
                    transaction.commit();
                    break;
                default:
                    super.handleMessage(msg);//这里最好对不需要或者不关心的消息抛给父类，避免丢失消息
                    break;
            }
        }
    };*/

    /*
    public class SaveDatabaseTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // We do the actual work of authenticating the user
            // in the NetworkUtilities class.
            saveAppInfos();
            return "";
        }

        @Override
        protected void onPostExecute(final String authToken) {
            // On a successful authentication, call back into the Activity to
            // communicate the authToken (or null for an error).
            // onAuthenticationResult(authToken);
        }

        @Override
        protected void onCancelled() {
            // If the action was canceled (by the user clicking the cancel
            // button in the progress dialog), then call back into the
            // activity to let it know.
            // onAuthenticationCancel();
        }
    }
*/
    public final class ViewHolder {
        public ImageView icon;
        public CheckBox gprsBtn;
        public CheckBox wifiBtn;
        public TextView applicationName;
    }

    private boolean initCustomActionBar() {
        myactionbar = getActionBar();
        if (myactionbar == null) {
            return false;
        } else {
            myactionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            myactionbar.setDisplayShowCustomEnabled(true);
            myactionbar.setCustomView(R.layout.my_actionbar_style);
            myactionbar_title = (TextView) myactionbar.getCustomView().findViewById(R.id.my_actionbar_title);
            myactionbar_back_btn = (ImageButton) myactionbar.getCustomView().findViewById(R.id.ps_back_btn);
            myactionbar_back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //((NetworkManagerOneFragment) fragment).applyOrSaveRules();
                    finish();
                }
            });
            myactionbar_title.setText(getResources().getString(R.string.app_name));
            return true;
        }
    }

    public void getTestResover() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ApplicationProviderMetaData.CONTENT_URI, new String[]{ApplicationProviderMetaData.COLUMN_ID, ApplicationProviderMetaData.COLUMN_APPLICATION_UID}, null, null, null);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(cursor.getColumnIndex(ApplicationProviderMetaData.COLUMN_APPLICATION_UID));
            Log.i("yimin", "the first id is:" + s);
        }
    }
}
