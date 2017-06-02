package settings.hometech.com.applicationnetworkmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

public class NetworkManagerActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    //private RadioGroup radioGroup;
    private TextView textView1 = null;
    private TextView textView2 = null;
    private Fragment fragment;
    private Context mContext = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_manager);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mContext = this.getApplicationContext();
        fragmentManager = getFragmentManager();
        textView1 = (TextView) findViewById(R.id.normal_app);
        textView2 = (TextView) findViewById(R.id.system_app);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new FragmentFactory().getInstanceByIndex(1);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, fragment);
                transaction.commit();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new FragmentFactory().getInstanceByIndex(2);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, fragment);
                transaction.commit();
            }
        });
        //radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        /*
        radioGroup.setVisibility(View.GONE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment = FragmentFactory.getInstanceByIndex(1);
        transaction.replace(R.id.content, fragment);
        transaction.commit();*/
        /*
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("yimin", "radio checked:" + checkedId);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                fragment = new FragmentFactory().getInstanceByIndex(checkedId);
                Log.i("yimin", "fragment is:" + fragment.toString());
                if(fragment!=null) {
                    transaction.replace(R.id.content, fragment);
                    transaction.commit();
                }
            }
        });*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (fragment instanceof NetworkManagerOneFragment) {
                //((NetworkManagerOneFragment) fragment).cancleSelect();
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    ((NetworkManagerOneFragment) fragment).applyOrSaveRules();
                                    finish();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    // Propagate the event back to perform the desired
                                    // action
                                    //onKeyDown(keyCode, event);
                                    finish();
                                    break;
                            }
                        }
                    };
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("未保存的修改")
                            .setMessage("舍弃未保存的修改吗")
                            .setPositiveButton("应用", dialogClickListener)
                            .setNegativeButton("舍弃", dialogClickListener)
                            .show();
                    return true;
                }
            } else if (fragment instanceof NetworkManagerTwoFragment) {
                //((NetworkManagerOneFragment) fragment).cancleSelect();
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    ((NetworkManagerTwoFragment) fragment).applyOrSaveRules();
                                    finish();
                                    //moveToHome();
                                    //moveTaskToBack(true);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    // Propagate the event back to perform the desired
                                    // action
                                    //onKeyDown(keyCode, event);
                                    finish();
                                    //moveToHome();
                                    //moveTaskToBack(true);
                                    break;
                            }
                        }
                    };
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("未保存的修改")
                            .setMessage("舍弃未保存的修改吗")
                            .setPositiveButton("应用", dialogClickListener)
                            .setNegativeButton("舍弃", dialogClickListener)
                            .show();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void moveToHome() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
}
