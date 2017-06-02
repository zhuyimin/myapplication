package settings.hometech.com.applicationnetworkmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Broadcast receiver that set iptables rules on system startup.
 * This is necessary because the rules are not persistent.
 */
public class NMBootBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			//Intent MainService=new Intent(context, TrafficService.class);
			Log.i("yimin","boot complete");
        	//context.startService(MainService);
			Api.saveRules(context);
			Api.saveSystemRules(context);
		}
	}

}
