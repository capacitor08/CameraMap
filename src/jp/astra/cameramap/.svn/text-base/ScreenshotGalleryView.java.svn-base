package jp.astra.cameramap;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import jp.astra.cameramap.helper.ActivityUtil;
import jp.astra.cameramap.helper.ArrayUtil;
import jp.astra.cameramap.helper.BaseActivity;
import jp.astra.cameramap.helper.Dictionary;
import jp.astra.cameramap.helper.ImageLoader;
import jp.astra.cameramap.helper.Notify;
import jp.astra.cameramap.helper.StringUtil;
import jp.astra.cameramap.helper.Util;
import android.content.DialogInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jem
 */
public class ScreenshotGalleryView extends BaseActivity{

	private ImageAdapter imageAdapter;
	private GridView gridview;
	private File imageFolder;
	private String[] filePaths;
	private String fileSelected;

	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.screenshot_gallery_view);

		this.inflateControls(this);

		this.findViewById(R.id.ChangeCamera).setVisibility(View.INVISIBLE);

        Button buttonTakePicture = (Button)this.findViewById(R.id.takepicture);
        buttonTakePicture.setOnClickListener(this.captureButtonListener);

        this.findViewById(R.id.photomap).setVisibility(View.INVISIBLE);

        Button buttonGallery = (Button)this.findViewById(R.id.gallerylist);
        buttonGallery.setOnClickListener(this.galleryListButtonListener);

    }

	@Override
    public void onResume() {
    	super.onResume();
    	Notify.toast(ScreenshotGalleryView.this, "scanning...");
    	this.progress.show();
		MediaScannerConnection.scanFile(ScreenshotGalleryView.this, new String[] {ScreenshotGalleryView.this.imageFolder.getAbsolutePath()}, null, ScreenshotGalleryView.this.scanMediaListener);

    }

    @Override
    public void setContentView(int layoutId) {
        super.setContentView(layoutId);

        this.imageFolder = new  File(Util.getExternalStoragePathScreenshots(this));
        this.imageAdapter = new ImageAdapter();

        this.gridview = (GridView) this.findViewById(R.id.gallery_grid_view);
        this.gridview.setAdapter(this.imageAdapter);
        this.gridview.setOnItemClickListener(this.imageListener);
        this.gridview.setOnItemLongClickListener(this.imageLongClickListener);

        if (this.imageAdapter.isEmpty()) {
        	TextView emptyListText = (TextView) this.findViewById(R.id.gallery_no_images_text);
        	emptyListText.setVisibility(View.VISIBLE);
        }
    }

    private MediaScannerConnection.OnScanCompletedListener scanMediaListener = new MediaScannerConnection.OnScanCompletedListener() {

		@Override
		public void onScanCompleted(String path, Uri uri) {
			ScreenshotGalleryView.this.refreshGalleryHandler.sendEmptyMessage(0);
		}
	};

    private OnItemClickListener imageListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long extra) {
			Dictionary bundle = new Dictionary();
	        bundle.setData("filepath", ScreenshotGalleryView.this.imageAdapter.getItem(position));
	        ActivityUtil.openActivityOnTop(ScreenshotGalleryView.this, ScreenshotPreviewView.class, bundle);
		}

	};

	private OnItemLongClickListener imageLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> adapter, View view,
				int position, long extra) {
			ScreenshotGalleryView.this.fileSelected = (String) ScreenshotGalleryView.this.imageAdapter.getItem(position);
			Notify.prompt(ScreenshotGalleryView.this, "Delete " + StringUtil.fileBaseName(ScreenshotGalleryView.this.fileSelected) + "?", "Delete", "Cancel", ScreenshotGalleryView.this.deleteListener, null);
			return false;
		}

	};

	private final DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			File file = new File(ScreenshotGalleryView.this.fileSelected);
			if(file.delete()){
		    	ScreenshotGalleryView.this.onResume();
			}
		}

	};

	public final Handler refreshGalleryHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
        	Notify.toast(ScreenshotGalleryView.this, "scanning complete!");

        	ScreenshotGalleryView.this.imageAdapter.notifyDataSetChanged();
	    	ScreenshotGalleryView.this.gridview.invalidateViews();
	    	ScreenshotGalleryView.this.progress.hide();

	    	super.handleMessage(msg);
        }
    };

    private class ImageAdapter extends BaseAdapter {

    	private String[] filePaths;
    	private final List<String> imageFileExtensions =  Arrays.asList("jpg", "png", "gif", "jpeg");

    	public ImageAdapter() {
			this.getImageFiles();
		}

    	@Override
		public boolean isEmpty() {
    		return this.getCount() < 1;
    	}

    	@Override
		public int getCount() {
            return this.filePaths.length;
        }

        @Override
		public Object getItem(int position) {
            return this.filePaths[position];
        }

        @Override
		public long getItemId(int position) {
            return 0;
        }

        @Override
		public View getView(int position, View convertView, ViewGroup parent) {

        	View v = convertView;
        	if (v == null) {
        		LayoutInflater inflater = ScreenshotGalleryView.this.getLayoutInflater();
        		v = inflater.inflate(R.layout.gallery_row_view, null);
    	   }

            ImageView imageView = (ImageView)v.findViewById(R.id.imageGrid);
            TextView textView = (TextView)v.findViewById(R.id.filename);

            String imageFile = this.filePaths[position];

            ImageLoader.loadImage(ScreenshotGalleryView.this,
            		ScreenshotGalleryView.this.imageCache,
            		imageView,
            		imageFile);

            textView.setText(StringUtil.fileBaseName(imageFile));

            return v;
        }

        public void getImageFiles() {

        	int count = 0;
        	this.filePaths = new String[0];

        	for (File file : ScreenshotGalleryView.this.imageFolder.listFiles()) {
        		String path = file.getPath();
        		if (!file.isDirectory() && this.imageFileExtensions.contains(path.substring(path.lastIndexOf(".")+1))) {
        			this.filePaths = ArrayUtil.insertToStringArray(this.filePaths, file.getAbsolutePath(), count);
        			ScreenshotGalleryView.this.filePaths = ArrayUtil.insertToStringArray(ScreenshotGalleryView.this.filePaths, file.getAbsolutePath(), count);
        			count++;
        		}
        	}

        	if (count > 0) {
        		ScreenshotGalleryView.this.findViewById(R.id.gallery_no_images_text).setVisibility(View.INVISIBLE);
        	} else {
        		ScreenshotGalleryView.this.findViewById(R.id.gallery_no_images_text).setVisibility(View.VISIBLE);
        	}
        }

        @Override
        public void notifyDataSetChanged() {
        	this.getImageFiles();
        	super.notifyDataSetChanged();
        }
    }

}
