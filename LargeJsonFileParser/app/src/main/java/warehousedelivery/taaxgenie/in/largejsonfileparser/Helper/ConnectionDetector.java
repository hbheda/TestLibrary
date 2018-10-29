package warehousedelivery.taaxgenie.in.largejsonfileparser.Helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;

public class ConnectionDetector {

	private static Activity _activity;
	private static View _view;

	private final static int TYPE_WIFI = 1;
	private final static int TYPE_MOBILE = 2;
	private final static int TYPE_NOT_CONNECTED = 0;

	// create an object of SingleObject
	private static ConnectionDetector instance = new ConnectionDetector();

	// make the constructor private so that this class cannot be instantiated
	private ConnectionDetector() {
	}

	// Get the only object available
	public static ConnectionDetector getInstance(Activity activity) {
		_activity = activity;
		_view=null;
		return instance;
	}

	// Get the only object available
	public static ConnectionDetector getInstance(Activity activity, View view) {
		_activity = activity;
		_view=view;
		return instance;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivityManager = (ConnectivityManager) _activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Network[] networks;
			if (connectivityManager != null) {
				networks = connectivityManager.getAllNetworks();
				NetworkInfo networkInfo;
				for (Network mNetwork : networks) {
					networkInfo = connectivityManager.getNetworkInfo(mNetwork);
					if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
						return true;
					}
				}
			}
		}else {
			if (connectivityManager != null) {
				//noinspection deprecation
				NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
				if (info != null) {
					for (NetworkInfo anInfo : info) {
						if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
							//LogUtils.d("Network",
							//		"NETWORKNAME: " + anInfo.getTypeName());
							return true;
						}
					}
				}
			}
		}
		/*if(_view!=null)
		{
			Snackbar.make(_view, "Please connect to internet", Snackbar.LENGTH_LONG).setAction("Action", null).show();
		}
		else
		{
				Toast.makeText(_activity,"Please connect to internet", Toast.LENGTH_LONG).show();
		}
*/
		return false;
	}

	public int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = null;
		if (cm != null) {
			activeNetwork = cm.getActiveNetworkInfo();
		}

		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
				return TYPE_WIFI;
			}

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
				return TYPE_MOBILE;
			}
		}
		return TYPE_NOT_CONNECTED;
	}
}
