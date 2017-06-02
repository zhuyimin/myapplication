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
public class NetworkManagerOneFragment extends Fragment implements
		CompoundButton.OnCheckedChangeListener, View.OnClickListener {
	private View view = null;
	//final Context context = getActivity();
	private ListView application_list = null;
	private CheckBox check_box_all = null;
	private RadioButton radio_button_all = null;
	private boolean isRadioButtonChecked = true;
	private boolean isItemRadioButtonChecked = true;
	private ArrayAdapter arrayAdapter= null;
	private DroidApp[] apps = null;
	private Context mContext = null;
	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        view = inflater.inflate(R.layout.network_manger_fragment_one, null);
			//initView();
			//showOrLoadApplications();
			Log.i("yimin","one fragment create");
	        return view;
	    }

	@Override
	public void onAttach(Activity activity) {
		mContext = activity;
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		Log.i("yimin","one fragment resume");
		initView();
		showOrLoadApplications();
		super.onResume();
	}

	public void initView() {
			if (application_list == null) {
				application_list = (ListView) view.findViewById(R.id.application_list);
			}
			if(check_box_all == null) {
				check_box_all = (CheckBox) view.findViewById(R.id.check_box_all);
			}
			if(radio_button_all == null) {
				radio_button_all = (RadioButton) view.findViewById(R.id.radio_button_all);
			}
			check_box_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Log.i("TrafficWALL","check_box_all clicked");
					if(isChecked) {
						for(int i = 0; i <apps.length;i++) {
							DroidApp app = apps[i];
							app.setCheckBox(true);
						}
					} else {
						for(int i = 0; i <apps.length;i++) {
							DroidApp app = apps[i];
							app.setCheckBox(false);
						}
					}
					//application_list.deferNotifyDataSetChanged();
					arrayAdapter.notifyDataSetChanged();
				}
			});

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
						for(int i = 0; i <apps.length;i++) {
							DroidApp app = apps[i];
							app.setRadioButton(true);
						}
					}
					arrayAdapter.notifyDataSetChanged();
				}
			});
		}

	private void showApplications() {
		apps = Api.getNotSystemApps(mContext);
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
				R.layout.application_list_item, R.id.application_name, apps) {
			@Override
			public View getView(final int position, View convertView,
								ViewGroup parent) {
				final ListEntry entry;
				Log.d("TrafficWALL", "getView");
				if (convertView == null) {
					// Inflate a new view
					convertView = inflater.inflate(R.layout.application_list_item, parent,
							false);
					Log.d("TrafficWALL", ">> inflate(" + convertView + ")");
					entry = new ListEntry();
					entry.text = (TextView) convertView
							.findViewById(R.id.application_name);
					entry.icon = (ImageView) convertView
							.findViewById(R.id.application_icon);

					entry.box_wifi = (RadioButton) convertView
							.findViewById(R.id.radio_button);
					entry.box_3g = (CheckBox) convertView
							.findViewById(R.id.check_box);

					entry.box_wifi
							.setOnCheckedChangeListener(NetworkManagerOneFragment.this);
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
					});
					entry.box_3g
							.setOnCheckedChangeListener(NetworkManagerOneFragment.this);
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
					new LoadIconTask().execute(app, getActivity().getPackageManager(),
							convertView);
				}

				final RadioButton box_wifi = entry.box_wifi;
				box_wifi.setTag(app);
				box_wifi.setChecked(app.selected_wifi);
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
		if (app != null) {
			switch (buttonView.getId()) {
				case R.id.check_box:
					Log.i("TrafficWALL","check_box changed");
					Log.i("TrafficWALL","check_box changed");
					if (app.selected_3g != isChecked) {
						app.selected_3g = isChecked;
					}
					break;

				case R.id.radio_button:
					Log.i("TrafficWALL","check_box changed");
					if (app.selected_wifi != isChecked) {
						app.selected_wifi = isChecked;
					}
					break;
			}
		}
	}


	private static class ListEntry {
		private RadioButton box_wifi;
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
				return viewToUpdate;
			} catch (Exception e) {
				Log.e("TrafficWALL", "Error loading icon", e);
				return null;
			}
		}

		protected void onPostExecute(View viewToUpdate) {
			try {
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
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					//Api.getApps(getContext());
					Api.getNotSystemApps(getContext());
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
						Toast.makeText(mContext, "规则保存失败",
								Toast.LENGTH_SHORT).show();
						Api.setEnabled(mContext, false);
					}
				} else {
					Log.d("TrafficWALL", "Saving rules.");
					Log.i("yimin","getActivity():" + getActivity());
					Api.saveRules(mContext);
					Toast.makeText(mContext, "规则保存成功",
							Toast.LENGTH_SHORT).show();
				}
				//TrafficControl.this.dirty = false;
			}
		};
		handler.sendEmptyMessageDelayed(0, 100);
	}
}
