package jp.astra.cameramap.helper;

import jp.astra.cameramap.CameraMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Manages the activity transitions.
 *
 * @category Helper
 * @author Kelvzs
 * @updated Kat
 */
public final class ActivityUtil {

	private static Intent setBundle(Intent intent, Dictionary data) {

        if (data != null) {
        	String[] keys = data.getKeys();
	        for (int i = 0; i < keys.length; i++) {
	            intent.putExtra(keys[i], data.getString(keys[i]));
	        }
        }

        return intent;
	}

	public static void openActivityOnTop(Context context, Class<?> c, Dictionary data) {

		Intent intent = new Intent(context, c);
		intent = ActivityUtil.setBundle(intent, data);

    	context.startActivity(intent);

	}

	public static void startActivity(Context context, Class<?> c, Dictionary data) {
        if (Util.isMediaAvailable(context)) {

        	Intent intent = new Intent(context, c);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

	        intent = ActivityUtil.setBundle(intent, data);

        	context.startActivity(intent);

        }
	}

    public static void closeApp(Activity context) {

        Intent intent = new Intent(context, CameraMap.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }
}
