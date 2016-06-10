package jp.astra.cameramap.helper;


import jp.astra.cameramap.CameraActivity;
import jp.astra.cameramap.GalleryListView;
import jp.astra.cameramap.PhotoMapActivity;
import jp.astra.cameramap.R;
import jp.astra.cameramap.ScreenshotGalleryView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * @author Kat
 * @category Common
 */

public class BaseActivity extends Activity implements OnClickListener, OnKeyListener {

    protected final static int UI_UPDATE_DELAY = 300;

    public ProgressDialog progress;
    protected Dictionary imageCache = new Dictionary();
    protected boolean willClose = false;
    protected boolean willRecommend = false;
	protected boolean offline = false;

	protected LayoutInflater controlInflater = null;

    protected int tabbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.progress = new ProgressDialog(this);
        this.progress.setCancelable(false);
        this.progress.setIndeterminate(true);
        this.progress.setOnKeyListener(BaseActivity.this.progressKeyListener);
        this.progress.setMessage(this.getString(R.string.loading));

    }

    public void inflateControls(Context context){
    	this.controlInflater = LayoutInflater.from(context);
        View viewControl = this.controlInflater.inflate(R.layout.control, null);
        LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addContentView(viewControl, layoutParamsControl);
    }

    @Override
    public void onResume(){

    	super.onResume();

    }

    @Override
	public void onLowMemory() {

		super.onLowMemory();

		this.cleanup();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		this.cleanup();
	}

	@Override
    public void setContentView(int layoutResID) {

    	super.setContentView(layoutResID);

    }

	public View setViewOnClickListener(int resourceId) {

		View view = this.findViewById(resourceId);
		view.setOnClickListener(this);

		return view;

	}

	private void cleanup() {

		for (String key : this.imageCache.getKeys()) {
			try {
				Bitmap bitmap = (Bitmap)this.imageCache.getObject(key);
				bitmap.recycle();
			} catch (Exception e) {
			}
		}

		this.imageCache = new Dictionary();

		System.gc();
	}

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        new MenuInflater(this.getApplication()).inflate(R.layout.main_menu, menu);
//
//        if (BaseActivity.this instanceof CameraView) {
//        	menu.findItem(R.id.menu_camera).setVisible(false);
//        } else if (BaseActivity.this instanceof GalleryView) {
//        	menu.findItem(R.id.menu_gallery).setVisible(false);
//        }
//
//        return super.onCreateOptionsMenu(menu);
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch(item.getItemId()) {
//	        case R.id.menu_camera:
//	        	ActivityUtil.startActivity(this, CameraView.class, null);
//	        	return true;
//
//            case R.id.menu_gallery:
//            	ActivityUtil.startActivity(this, GalleryView.class, null);
//            	return true;
//
//            case R.id.menu_info:
//            	Notify.toast(this, "INFO MENU");
//            	return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

    	if (Util.keyCheck(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        } else {
            return false;
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {

//        if (Util.keyCheck(keyCode, event)) {
//            return false;
//        } else {
            return true;
//        }
    }

    @Override
    public void onClick(View view) {

    }

    protected void loadImage(RelativeLayout placeholder, String source) {

        Drawable drawable = (Drawable)this.imageCache.getObject(source);

        if (drawable == null) {
            //new ImageLoader(this, this.imageCache, placeholder, source);
        } else {
        	placeholder.setBackgroundDrawable(drawable);
        	placeholder.removeAllViews();
        }
    }

    protected void run(Runnable function) {

        this.progress.show();

        new Thread(function).start();
    }

    public final Handler hideProgressHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            BaseActivity.this.progress.hide();
            super.handleMessage(msg);
        }
    };

    protected final Handler sleepScreenHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
        	BaseActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        	super.handleMessage(msg);
        }
    };

    protected final Handler wakeScreenHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
        	BaseActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        	super.handleMessage(msg);
        }
    };

    public final Handler closeAppHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

        	Notify.promptCancelWithCustomButton(BaseActivity.this, R.string.alert_close, R.string.alert_button_exit, BaseActivity.this.closeAppListener);
        	super.handleMessage(msg);
        }
    };

    protected class DialogOnClickListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
        	((AlertDialog)dialog).setOnDismissListener(null);
		}
    }

    protected class DialogOnKeyListener implements DialogInterface.OnKeyListener {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

			return false;
		}
    }

    public DialogInterface.OnClickListener listener = null;

    private DialogOnClickListener cancelListener = new DialogOnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

        	super.onClick(dialog, which);
        	BaseActivity.this.cancelDialogHandler.sendEmptyMessage(0);
        }
    };

    private final DialogInterface.OnDismissListener dismissDialogListener = new DialogInterface.OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			BaseActivity.this.cancelDialogHandler.sendEmptyMessage(0);
		}
	};

	private final Handler cancelDialogHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
        }
    };

    private final DialogOnClickListener closeAppListener = new DialogOnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

        	super.onClick(dialog, which);
        	ActivityUtil.closeApp(BaseActivity.this);
        }
    };

    private final DialogInterface.OnDismissListener dismissAppListener = new DialogInterface.OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			ActivityUtil.closeApp(BaseActivity.this);
		}
	};

	public final DialogOnKeyListener progressKeyListener = new DialogOnKeyListener(){

    	@Override
    	public boolean onKey(DialogInterface dialog, int KeyCode, KeyEvent event){

    		return true;
    	}
    };

    /*
     * Button Listener
     */

	protected final OnClickListener captureButtonListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
        	ActivityUtil.startActivity(BaseActivity.this, CameraActivity.class, null);
        }
    };

    protected final OnClickListener galleryListButtonListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
        	ActivityUtil.startActivity(BaseActivity.this, GalleryListView.class, null);
        }
    };

    protected final OnClickListener mapButtonListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
        	ActivityUtil.startActivity(BaseActivity.this, PhotoMapActivity.class, null);
        }
    };

    protected final OnClickListener printButtonListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
        	ActivityUtil.startActivity(BaseActivity.this, CameraActivity.class, null);
        }
    };

    protected final OnClickListener reportButtonListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
        	ActivityUtil.startActivity(BaseActivity.this, ScreenshotGalleryView.class, null);
        }
    };
}

