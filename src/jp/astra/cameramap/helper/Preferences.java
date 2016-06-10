package jp.astra.cameramap.helper;

import jp.astra.cameramap.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Manages the reading and writing of the SharedPreferences.
 *
 * @category Helper
 * @author Kat
 */
public final class Preferences {

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName() + "." + context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static void setFirstStartup(Context context) {

        Editor editor = Preferences.getSharedPreferences(context).edit();

        editor.putBoolean("first_startup", false);
        editor.commit();
    }

    public static boolean isFirstStartup(Context context) {
        return Preferences.getSharedPreferences(context).getBoolean("first_startup", true);
    }

}
