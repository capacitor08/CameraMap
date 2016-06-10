package jp.astra.cameramap;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import jp.astra.cameramap.helper.ArrayUtil;
import jp.astra.cameramap.helper.BaseActivity;
import jp.astra.cameramap.helper.Notify;
import jp.astra.cameramap.helper.Util;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Lau
 */
public class GalleryListView extends BaseActivity {

	private ListAdapter listAdapter;
	private ListView listview;
//	private String fileSelected;
//	private Dictionary filesByDate = new Dictionary();
	private File imageFolder;
	private HashMap<String, String[]> filesGroupByDate = new HashMap<String, String[]>();

	@Override
    public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_view);
		this.inflateControls(this);
        Button buttonTakePicture = (Button)this.findViewById(R.id.takepicture);
        buttonTakePicture.setOnClickListener(this.captureButtonListener);

        Button buttonMap = (Button)this.findViewById(R.id.photomap);
        buttonMap.setOnClickListener(this.mapButtonListener);

        Button buttonScreenshots = (Button)this.findViewById(R.id.screenshots);
        buttonScreenshots.setOnClickListener(this.reportButtonListener);

    }

	@Override
    public void onResume() {
    	super.onResume();
    	MediaScannerConnection.scanFile(GalleryListView.this, new String[] {GalleryListView.this.imageFolder.getAbsolutePath()}, null, GalleryListView.this.scanMediaListener);

	}

    @Override
    public void setContentView(int layoutId) {
        super.setContentView(layoutId);

        this.imageFolder = new  File(Util.getExternalStoragePath(this));
        this.listAdapter = new ListAdapter();

        this.listview = (ListView) this.findViewById(R.id.gallery_list_view);
        this.listview.setAdapter(this.listAdapter);
        this.listview.setOnItemClickListener(this.imageListener);

        if (this.listAdapter.isEmpty()) {
        	TextView emptyListText = (TextView) this.findViewById(R.id.gallery_no_images_text);
        	emptyListText.setVisibility(View.VISIBLE);
        }

    }

    private MediaScannerConnection.OnScanCompletedListener scanMediaListener = new MediaScannerConnection.OnScanCompletedListener() {

		@Override
		public void onScanCompleted(String path, Uri uri) {
			GalleryListView.this.refreshGalleryHandler.sendEmptyMessage(0);
		}
	};

	public final Handler refreshGalleryHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
        	Notify.toast(GalleryListView.this, "scanning complete!");

        	GalleryListView.this.listAdapter.notifyDataSetChanged();
	    	GalleryListView.this.listview.invalidateViews();
	    	GalleryListView.this.hideProgressHandler.sendEmptyMessage(0);

        	super.handleMessage(msg);
        }
    };

	private OnItemClickListener imageListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long extra) {

			Intent intent = new Intent(GalleryListView.this, GalleryView.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("filePath", GalleryListView.this.filesGroupByDate.get(GalleryListView.this.listAdapter.getItem(position)));

	    	GalleryListView.this.startActivity(intent);

		}

	};

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	this.closeAppHandler.sendEmptyMessage(0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class ListAdapter extends BaseAdapter {

    	private String[] keys;

    	public ListAdapter() {
			this.getImageFiles();
		}

    	public boolean isEmpty() {
    		return this.getCount() < 1;
    	}

    	public int getCount() {
            return this.keys.length;
        }

        public Object getItem(int position) {
            return this.keys[position];
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

        	View v = convertView;

        	if (v == null) {
        		LayoutInflater inflater = GalleryListView.this.getLayoutInflater();
        		v = inflater.inflate(R.layout.list_row_view, null);
    	   }

            ImageView imageView = (ImageView)v.findViewById(R.id.thumbnail);
            TextView textView = (TextView)v.findViewById(R.id.album_title);
            TextView imageCount = (TextView)v.findViewById(R.id.count);

            String[] imageFiles = GalleryListView.this.filesGroupByDate.get(this.keys[position]);
            String imageFile = imageFiles[0];
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = 4;
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile, o2);
            imageView.setImageBitmap(imageBitmap);
            textView.setText(this.keys[position]);
            imageCount.setText("(" + imageFiles.length + ") Photos");
            return v;

        }

        public void getImageFiles() {

        	int count = 0;
        	this.keys = new String[0];
        	GalleryListView.this.filesGroupByDate = new HashMap<String, String[]>();

        	for (File file : GalleryListView.this.imageFolder.listFiles()) {
        		if (!file.isDirectory()) {
        			String dateString = new SimpleDateFormat("yyyy/MM/dd").format(new Date(file.lastModified()));
        			if(GalleryListView.this.filesGroupByDate.containsKey(dateString)){
        				String[] files = GalleryListView.this.filesGroupByDate.get(dateString);
        				files = ArrayUtil.insertToStringArray(files, file.getAbsolutePath(), files.length + 1);
        				GalleryListView.this.filesGroupByDate.put(dateString, files);
        			}else{
        				String[] files = new String[0];
        				files = ArrayUtil.insertToStringArray(files, file.getAbsolutePath(), 0);
        				GalleryListView.this.filesGroupByDate.put(dateString, files);
        			}
        			count++;
        		}
        	}
        	if (count > 0) {
        		int position = 0;
        		for(String key: GalleryListView.this.filesGroupByDate.keySet()){
        			this.keys = ArrayUtil.insertToStringArray(this.keys, key, position);
        			position++;
        		}
        		Arrays.sort(this.keys, new Comparator<String>() {
        	        @Override
        	        public int compare(String object1, String object2) {
        	        	return object1.compareTo(object2);
        	        }
        	    });
        		GalleryListView.this.findViewById(R.id.gallery_no_images_text).setVisibility(View.INVISIBLE);
        	} else {
        		GalleryListView.this.findViewById(R.id.gallery_no_images_text).setVisibility(View.VISIBLE);
        	}
        }

        @Override
        public void notifyDataSetChanged() {
        	this.getImageFiles();
        	super.notifyDataSetChanged();
        }
    }
}
