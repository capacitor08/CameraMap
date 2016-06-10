package jp.astra.cameramap.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * @category ImageLoader
 * @author Kelvzs
 * @updated Kat
 */
public final class ImageLoader {

    private Context context;
    private Dictionary imageCache;
    private ImageView imageView = null;
    private String source = null;
//    private Drawable drawable = null;
    private Bitmap drawable = null;

    public ImageLoader(Context context, Dictionary imageCache, ImageView imageView, String source) {

        this.context = context;
        this.imageCache = imageCache;
        this.imageView = imageView;
        this.source = source;

        Thread loader = new Thread(new Loader());
        loader.setPriority(Thread.MIN_PRIORITY);
        loader.start();
    }

    private final class Loader implements Runnable {

        @Override
        public void run() {

//        	ImageLoader.this.drawable = Util.loadImage(ImageLoader.this.context, ImageLoader.this.source);
        	
        	BitmapFactory.Options o2 = new BitmapFactory.Options();
    		o2.inSampleSize = 4;
    		Bitmap imageBitmap = BitmapFactory.decodeFile(ImageLoader.this.source, o2);
    		ImageLoader.this.drawable = imageBitmap;

        	if (ImageLoader.this.drawable != null) {
//        		Util.saveImage(ImageLoader.this.context, ImageLoader.this.source, ImageLoader.this.drawable);
            	ImageLoader.this.imageCache.setData(ImageLoader.this.source, ImageLoader.this.drawable);

                ImageLoader.this.refreshViewHandler.sendEmptyMessage(0);
        	}
        }
    }

    private final Handler refreshViewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

        	if (ImageLoader.this.imageView != null) {
        		ImageLoader.this.imageView.setImageBitmap(ImageLoader.this.drawable);
//        		ImageLoader.this.imageView.setBackgroundColor(ImageLoader.this.context.getResources().getColor(R.color.clear));
        	}

            super.handleMessage(msg);
        }
    };

    public static void loadImage(Context context, Dictionary imageCache, ImageView imageView, String source) {

    	if (!StringUtil.isEmpty(source)) {

    		//Drawable drawable = (Drawable)imageCache.getObject(source);
    		
    		Bitmap drawable = (Bitmap)imageCache.getObject(source);
    		
            if (drawable == null) {
                new ImageLoader(context, imageCache, imageView, source);

            } else {
//            	imageView.setImageDrawable(drawable);
//            	imageView.setBackgroundColor(context.getResources().getColor(R.color.clear));
            	
            	imageView.setImageBitmap(drawable);
            }
    	}
    }
}
