package jp.astra.cameramap;


import jp.astra.cameramap.helper.BaseActivity;
import jp.astra.cameramap.helper.CameraMapException;
import jp.astra.cameramap.helper.Logger;
import jp.astra.cameramap.helper.Util;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

/**
 * @author Lau
 */
public class CameraMap extends BaseActivity {

    private Intent intent;

    private boolean isStartup = true;
    public boolean hasMessage = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ThreadExceptionHandler(this.getString(R.string.app_name)));

        this.setContentView(R.layout.splash_view);

        if (Util.isMediaAvailable(this)) {
			this.run(this.startup);
		}
    }

    @Override
    public void onResume() {

        super.onResume();

        if (!this.isStartup) {
            this.finish();
        }

        this.isStartup = false;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        try {

	        if (this.intent != null) {
	        	this.stopService(this.intent);
	        }

        } catch (Throwable t) {
            new CameraMapException(this, "CamEditor.onDestroy", t);
        }

        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            return true;

        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.showMainHandler.removeMessages(0);
            return super.onKeyDown(keyCode, event);

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void run(Runnable function) {
        new Thread(function).start();
    }

    private final Runnable startup = new Runnable() {

        @Override
        public void run() {
        	CameraMap.this.showMainHandler.sendEmptyMessageDelayed(0, 1500);
        }
    };

    public final Handler showMainHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

    		Intent intent = new Intent(CameraMap.this, GalleryListView.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            CameraMap.this.startActivity(intent);

            super.handleMessage(msg);
        }
    };

    private final class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {

        private String title;

        public ThreadExceptionHandler(String title) {
            this.title = title;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            //Something got messed up...

            String message = ex.getLocalizedMessage();
            StackTraceElement stack[] = ex.getStackTrace();

            for (int i = 0; i < stack.length; i++) {
                message = message + "\n                Stack " + i + ": " + stack[i].getClassName() + ":" + stack[i].getMethodName() + "(" + stack[i].getFileName() + ":" + stack[i].getLineNumber() + ")";
            }

            try {
            	Logger.error(CameraMap.this, message);
            } catch (Throwable t) {
            	Log.e(this.title, message);
            }

            System.exit(0);
        }
    }
}
