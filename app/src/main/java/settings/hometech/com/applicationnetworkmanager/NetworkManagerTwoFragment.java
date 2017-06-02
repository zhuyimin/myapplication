package settings.hometech.com.applicationnetworkmanager;
import settings.hometech.com.applicationnetworkmanager.Api.DroidApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by yimin on 2017-05-13.
 */
public class NetworkManagerTwoFragment extends Fragment implements
		CompoundButton.OnCheckedChangeListener, View.OnClickListener {
	private View view = null;
	private Context mContext = getActivity();
	private ListView application_list = null;
	private CheckBox check_box_all = null;
	//private RadioButton radio_button_all = null;
	//private boolean isRadioButtonChecked = true;
	//private boolean isItemRadioButtonChecked = true;
	private ArrayAdapter arrayAdapter= null;
	private DroidApp[] apps = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.network_manger_fragment_two, null);
		Log.i("yimin","two fragment create");
		/*initView();
		//showOrLoadApplications();
		final String pwd = getActivity().getSharedPreferences(Api.PREFS_NAME, 0).getString(
				Api.PREF_PASSWORD, "");
		if (pwd.length() == 0) {
			Log.i("TrafficWALL","pwd is null");
			// No password lock
			showOrLoadApplications();
		} else {
			// Check the password
			//requestPassword(pwd);
		}*/
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		Log.i("yimin","two fragment resume");
		initView();
		showOrLoadApplications();
		super.onResume();
	}

	public void initView() {
		if (application_list == null) {
			application_list = (ListView) view.findViewById(R.id.application_list_two);
		}
		if(check_box_all == null) {
			check_box_all = (CheckBox) view.findViewById(R.id.check_box_all_two);
		}
		/*
		if(radio_button_all == null) {
			radio_button_all = (RadioButton) view.findViewById(R.id.radio_button_all);
		}*/
		check_box_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.i("TrafficWALL","check_box_all clicked");
				if(isChecked) {
						/*
						for(int i = 0; i < application_list.getChildCount(); i++){
							View view = application_list.getChildAt(i);
							CheckBox cb = (CheckBox)view.findViewById(R.id.check_box);
							cb.setChecked(true);
							//listAdapter.notifyDataSetChanged();
						}*/
						/*
						for(int i = 0;i <listAdapter.getCount();i++) {
							CheckBox checkBox = (CheckBox) listAdapter.getItem(i);
							checkBox.setChecked(true);
						}*/
					for(int i = 0; i <apps.length;i++) {
						DroidApp app = apps[i];
						app.setCheckBox(true);
					}
				} else {
						/*
						for(int i = 0; i < application_list.getChildCount(); i++){
							View view = application_list.getChildAt(i);
							CheckBox cb = (CheckBox)view.findViewById(R.id.check_box);
							cb.setChecked(false);
							listAdapter.notifyDataSetChanged();
						}*/
						/*
						for(int i = 0;i <listAdapter.getCount();i++) {
							CheckBox checkBox = (CheckBox) listAdapter.getItem(i);
							checkBox.setChecked(false);
						}*/
					for(int i = 0; i <apps.length;i++) {
						DroidApp app = apps[i];
						app.setCheckBox(false);
					}
				}
				//application_list.deferNotifyDataSetChanged();
				arrayAdapter.notifyDataSetChanged();
			}
		});
/*
		radio_button_all.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isRadioButtonChecked) {
					isRadioButtonChecked = false;
					radio_button_all.setChecked(false);
					for(int i = 0; i <apps.length;i++) {
						DroidApp app = apps[i];
						app.setRadioButton(false);
					}
				} else {
					isRadioButtonChecked = true;
					radio_button_all.setChecked(true);

						for(int i = 0; i < application_list.getChildCount(); i++){
							View view = application_list.getChildAt(i);
							RadioButton cb = (RadioButton) view.findViewById(R.id.radio_button);
							cb.setChecked(true);
						}
					for(int i = 0; i <apps.length;i++) {
						DroidApp app = apps[i];
						app.setRadioButton(true);
					}
				}
				arrayAdapter.notifyDataSetChanged();
			}
		});*/
/*
			radio_button_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Log.i("yimin","check_box_all clicked");
					if(isChecked) {
						for(int i = 0; i < application_list.getChildCount(); i++){
							View view = application_list.getChildAt(i);
							RadioButton cb = (RadioButton) view.findViewById(R.id.radio_button);
							cb.setChecked(true);
						}
					} else {
						for(int i = 0; i < application_list.getChildCount(); i++){
							View view = application_list.getChildAt(i);
							RadioButton cb = (RadioButton) view.findViewById(R.id.radio_button);
							cb.setChecked(false);
						}
					}
				}
			});*/
	}

	private void showApplications() {
		//this.dirty = false;
		//apps = Api.getApps(getContext().getApplicationContext());
		apps = Api.getSystemApps(getContext());
		//droidAppss = apps;
		// Sort applications - selected first, then alphabetically
		Arrays.sort(apps, new Comparator<DroidApp>() {

			@Override
			public int compare(DroidApp o1, DroidApp o2) {
				if (o1.firstseem != o2.firstseem) {
					return (o1.firstseem ? -1 : 1);
				}
				if ((o1.selected_wifi | o1.selected_3g) == (o2.selected_wifi | o2.selected_3g)) {
					return String.CASE_INSENSITIVE_ORDER.compare(o1.names[0],
							o2.names[0]);
				}
				if (o1.selected_wifi || o1.selected_3g)
					return -1;
				return 1;
			}
		});
		final LayoutInflater inflater = getActivity().getLayoutInflater();
		final ArrayAdapter adapter = new ArrayAdapter<DroidApp>(getActivity(),
				R.layout.application_list_item_two, R.id.application_name, apps) {
			@Override
			public View getView(final int position, View convertView,
								ViewGroup parent) {
				final ListEntry entry;
				Log.d("TrafficWALL", "getView");
				if (convertView == null) {
					// Inflate a new view
					convertView = inflater.inflate(R.layout.application_list_item_two, parent,
							false);
					Log.d("TrafficWALL", ">> inflate(" + convertView + ")");
					entry = new ListEntry();
					entry.text = (TextView) convertView
							.findViewById(R.id.application_name);
					entry.icon = (ImageView) convertView
							.findViewById(R.id.application_icon);
					/*
					entry.box_wifi = (RadioButton) convertView
							.findViewById(R.id.radio_button);*/
					entry.box_3g = (CheckBox) convertView
							.findViewById(R.id.check_box_two);
					/*
					entry.box_wifi
							.setOnCheckedChangeListener(NetworkManagerTwoFragment.this);
					entry.box_wifi.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if(isItemRadioButtonChecked) {
								isItemRadioButtonChecked = false;
								entry.box_wifi.setChecked(false);
							} else {
								isItemRadioButtonChecked = true;
								entry.box_wifi.setChecked(true);
							}
						}
					});*/
					entry.box_3g
							.setOnCheckedChangeListener(NetworkManagerTwoFragment.this);
					convertView.setTag(entry);
				} else {
					// Convert an existing view
					entry = (ListEntry) convertView.getTag();
				}
				final DroidApp app = apps[position];
				entry.app = app;
				String[] nameStrings = app.toString().split(":");
				entry.text.setText(nameStrings.length > 1 ? nameStrings[1]
						: nameStrings[0]);
				entry.icon.setImageDrawable(app.cached_icon);
				if (!app.icon_loaded && app.appinfo != null) {
					// this icon has not been loaded yet - load it on a
					// separated thread
					new LoadIconTask().execute(app, getActivity().getPackageManager(),
							convertView);
				}

				/*final RadioButton box_wifi = entry.box_wifi;
				box_wifi.setTag(app);
				box_wifi.setChecked(app.selected_wifi);*/
				final CheckBox box_3g = entry.box_3g;
				box_3g.setTag(app);
				box_3g.setChecked(app.selected_3g);
				Log.d("TrafficWALL", "position:" + position + "appname:" + nameStrings + "box_wifi:" + app.selected_wifi + "box_3g" + app.selected_3g);
				return convertView;
			}
		};
		arrayAdapter = adapter;
		this.application_list.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		final DroidApp app = (DroidApp) buttonView.getTag();
		//applyOrSaveRules();
		if (app != null) {
			switch (buttonView.getId()) {
				case R.id.check_box_two:
					Log.i("TrafficWALL","check_box changed:" + isChecked);
					if (app.selected_3g != isChecked) {
						app.selected_3g = isChecked;
						//this.dirty = true;
					}
					break;
			}
		}
	}


	private static class ListEntry {
		//private RadioButton box_wifi;
		private CheckBox box_3g;
		private TextView text;
		private ImageView icon;
		private DroidApp app;
	}

	private static class LoadIconTask extends AsyncTask<Object, Void, View> {
		@Override
		protected View doInBackground(Object... params) {
			try {
				final DroidApp app = (DroidApp) params[0];
				final PackageManager pkgMgr = (PackageManager) params[1];
				final View viewToUpdate = (View) params[2];
				if (!app.icon_loaded) {
					app.cached_icon = pkgMgr.getApplicationIcon(app.appinfo);
					app.icon_loaded = true;
				}
				// Return the view to update at "onPostExecute"
				// Note that we cannot be sure that this view still references
				// "app"
				return viewToUpdate;
			} catch (Exception e) {
				Log.e("TrafficWALL", "Error loading icon", e);
				return null;
			}
		}

		protected void onPostExecute(View viewToUpdate) {
			try {
				// This is executed in the UI thread, so it is safe to use
				// viewToUpdate.getTag()
				// and modify the UI
				final ListEntry entryToUpdate = (ListEntry) viewToUpdate
						.getTag();
				entryToUpdate.icon
						.setImageDrawable(entryToUpdate.app.cached_icon);
			} catch (Exception e) {
				Log.e("TrafficWALL", "Error showing icon", e);
			}
		};
	}

	private void showOrLoadApplications() {
		final Resources res = getResources();
		if (Api.applications == null) {
			// The applications are not cached.. so lets display the progress
			// dialog
			/*final ProgressDialog progress = ProgressDialog.show(context,
					"工作中",
					"读取应用信息", true);*/
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					//Api.getApps(getContext());
					Api.getSystemApps(getContext());
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					try {
						//progress.dismiss();
					} catch (Exception ex) {
					}
					showApplications();
				}
			}.execute();
		} else {
			// the applications are cached, just show the list
			showApplications();
		}
	}

	public void applyOrSaveRules() {
		final Resources res = getResources();
		final boolean enabled = Api.isEnabled(getContext());
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				try {
					//progress.dismiss();
				} catch (Exception ex) {
				}
				if (enabled) {
					Log.d("TrafficWALL", "Applying rules.");
					if (Api.hasRootAccess(getContext(), true)
							&& Api.applyIptablesRules(getContext(), true)) {
						Toast.makeText(getContext(),
								"规则应用成功", Toast.LENGTH_SHORT)
								.show();
					} else {
						Log.d("TrafficWALL", "Failed - Disabling firewall.");
						Toast.makeText(getContext(), "规则保存失败",
								Toast.LENGTH_SHORT).show();
						Api.setEnabled(getContext(), false);
					}
				} else {
					Log.d("TrafficWALL", "Saving rules.");
					Api.saveSystemRules(mContext);
					Toast.makeText(mContext, "规则保存成功",
							Toast.LENGTH_SHORT).show();
				}
				//TrafficControl.this.dirty = false;
			}
		};
		handler.sendEmptyMessageDelayed(0, 100);
	}
/*
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		// Handle the back button when dirty
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							applyOrSaveRules();
							break;
						case DialogInterface.BUTTON_NEGATIVE:
							// Propagate the event back to perform the desired
							// action
							onKeyDown(keyCode, event);
							break;
					}
				}
			};
			final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("未保存的修改")
					.setMessage("舍弃未保存的修改吗")
					.setPositiveButton("应用", dialogClickListener)
					.setNegativeButton("舍弃", dialogClickListener)
					.show();
			// Say that we've consumed the event
			return true;
		}
		return onKeyDown(keyCode, event);
	}
*/
}
