package jp.astra.cameramap.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.maps.model.Marker;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
/**
 * @category Helper
 * @author Kelvzs
 * @updated Kat
 */
public final class Util {

    public static boolean isMediaAvailable(Context context) {
    	return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getExternalStoragePath(Context context) {

        String path = "CameraMap/";

//        File dir = new File(Environment.getExternalStorageDirectory(), path);
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), path);
        dir.mkdirs();

        return dir.getAbsolutePath() + "/";
    }

    public static String getExternalStoragePathScreenshots(Context context) {

        String path = "CameraMap/Screenshots/";

//        File dir = new File(Environment.getExternalStorageDirectory(), path);
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), path);
        dir.mkdirs();

        return dir.getAbsolutePath() + "/";
    }

    public static String getCachePath(Context context) {

        String path = context.getCacheDir().getAbsolutePath() + "/cache/";

        File dir = new File(path);
        dir.mkdirs();

        return dir.getAbsolutePath() + "/";
    }

    public static String getLogPath(Context context) {

        String path = "Android/data/" + context.getPackageName() + "/logs/";

        File dir = new File(Environment.getExternalStorageDirectory(), path);
        dir.mkdirs();

        return dir.getAbsolutePath() + "/";
    }

    public static void deleteFilePath(File file) {

	    if (file.isDirectory()) {
	        for (File child : file.listFiles()) {
	            Util.deleteFilePath(child);
	        }
	    }
	    file.delete();
    }

    public static void removeCache(Context context) {
    	Util.deleteFilePath(new File(Util.getCachePath(context)));
    }

    public static String getImageCacheFile(Context context, String filename) {
    	return Util.getCachePath(context) + filename.replace("http://", "").replace("/", "_").replace(".", "_");
    }

    public static boolean saveImage(Context context, String filename, Drawable drawable) {

    	if (drawable != null && filename != null) {

	    	try {
	    		FileOutputStream stream = new FileOutputStream(Util.getImageCacheFile(context, filename));
	    		((BitmapDrawable)drawable).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
	    		stream.close();

	    		return true;

	    	} catch (Throwable t) {
	            Logger.error(context, "Util.saveImage", t);
	    	}
    	}

    	return false;
    }

    public static Drawable loadImage(Context context, String filename) {

    	BitmapDrawable drawable = null;

    	if (filename != null) {

        	try {
        		FileInputStream stream = new FileInputStream(Util.getImageCacheFile(context, filename));
        		drawable = new BitmapDrawable(stream);
        		stream.close();

        	} catch (Throwable t) {
                Logger.error(context, "Util.loadImage", t);
        	}
    	}

    	return drawable;
    }

    public static void sleep(long milliseconds) {

    	try {
    		Thread.sleep(milliseconds);
    	} catch (Throwable t) {}
    }

    public static boolean keyCheck(int keyCode, KeyEvent event) {

        return (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK || (keyCode == KeyEvent.KEYCODE_MENU && !event.isLongPress())
                || keyCode == KeyEvent.KEYCODE_POWER || keyCode == KeyEvent.KEYCODE_CALL || keyCode == KeyEvent.KEYCODE_ENDCALL
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_MUTE
                || keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == KeyEvent.KEYCODE_ENVELOPE || keyCode == KeyEvent.KEYCODE_EXPLORER
                || keyCode == KeyEvent.KEYCODE_NOTIFICATION || keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
                || keyCode == KeyEvent.KEYCODE_MEDIA_NEXT || keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS);
    }

    public static boolean isReachable(Activity activity) {

        ConnectivityManager connectivity = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean reachable = network != null && network.isAvailable() && network.isConnected();

        if (!reachable) {
            reachable = Util.isWifiReachable(activity);
        }

        return reachable;
    }

    public static boolean isWifiReachable(Activity activity) {

        ConnectivityManager connectivity = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        boolean reachable = false;

        if(network != null){
        	reachable = network != null && network.isAvailable() && network.isConnected();
        } else {
        	reachable = false;
        }

        /* test for WiMax; same behavior as Wifi */
        if (!reachable) {
            reachable = Util.isWiMaxReachable(activity);
        }

        return reachable;
    }

    public static boolean isWiMaxReachable(Activity activity) {

        ConnectivityManager connectivity = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

        boolean reachable = false;

        if(network != null){
        	reachable = network != null && network.isAvailable() && network.isConnected();
        } else {
        	reachable = false;
        }

        return reachable;
    }

    public static void sendEmail(Context context, String filePath){
    	Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
		intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
		intent.setData(Uri.parse("mailto:default@recipient.com")); // or just "mailto:" for blank
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
		intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
		final PackageManager pm = context.getPackageManager();
	    final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
	    ResolveInfo best = null;
	    for (final ResolveInfo info : matches) {
			if (info.activityInfo.packageName.endsWith(".gm") ||
			      info.activityInfo.name.toLowerCase().contains("gmail")) {
				best = info;
			}
		}
	    if (best != null) {
			intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
		}
		context.startActivity(intent);
    }

    public static void sendBlueTooth(Context context, String filePath){
    	Intent intent = new Intent(Intent.ACTION_SEND); // it's not ACTION_SEND
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
		intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
		final PackageManager pm = context.getPackageManager();
	    final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
	    ResolveInfo best = null;
	    for (final ResolveInfo info : matches) {
			if (info.activityInfo.packageName.endsWith("bluetooth") ||
			      info.activityInfo.name.toLowerCase().contains("bluetooth")) {
				best = info;
			}
		}
	    if (best != null) {
			intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
		}
		context.startActivity(intent);
    }

    public static void sendGeneric(Context context, String filePath){
    	Intent intent = new Intent(Intent.ACTION_SEND); // it's not ACTION_SEND
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
		intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));

		context.startActivity(intent);
    }


    public static double roundDecimals(double d) {
    	DecimalFormat df = new DecimalFormat("#.######");
    	return Double.valueOf(df.format(d));
    }

    public static String markersToString(ArrayList<Marker> markers) {
    	String result="";
    	for (Marker marker : markers) {
    		result = result + Util.roundDecimals(marker.getPosition().latitude)
    				+ "," + Util.roundDecimals(marker.getPosition().longitude)
    				+ "%7C";
    	}

    	return result;
    }

    public static Bitmap getBitmapFromView(View view) {
    	Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
    	Canvas canvas = new Canvas(bitmap);
    	Drawable bgDrawable = view.getBackground();
    	if (bgDrawable != null) {
			bgDrawable.draw(canvas);
		} else {
			canvas.drawColor(Color.WHITE);
		}
    	view.draw(canvas);
    	return bitmap;
    }
    
    public static boolean saveScreenshot(Context context, Bitmap origBitmap) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	String timestamp = sdf.format(new Date());
    	String path = Util.getExternalStoragePathScreenshots(context) + "screenshot_" + timestamp + ".png";

    	Util.saveBitmapToImage(origBitmap, path);
    	
    	return false;
    }
    
    public static boolean saveBitmapToImage(Bitmap origBitmap, String path) {

    	File file = new File(path);

    	try {
    		if (!file.exists()) {
    			file.createNewFile();
    		}
    		FileOutputStream out = new FileOutputStream(file);

    		Bitmap bitmap = origBitmap;
    		bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
    		out.flush();
    		out.close();

    		return true;

    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	return false;
    }
    
    public static String saveToPdf(String imagePath) {
    	
    	try {
    		
    		Image baseImage = Image.getInstance(imagePath);
    		
    		Document document=new Document(PageSize.A4);
        	float docW = document.getPageSize().getWidth() - document.topMargin() - document.bottomMargin();
    		float docH = document.getPageSize().getHeight() - document.leftMargin() - document.rightMargin();
    		
    		float dstH = docH * (baseImage.getWidth() / docW); 
    		
    		String pdfPath = Util.getPdfName(imagePath);
    		File pdfFile = new File(pdfPath);
    		if (!pdfFile.exists()) {

        		File imageFile = new  File(imagePath);
        		Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        		
        		int numPages = (int) Math.ceil(baseImage.getHeight() / dstH);
        		
        		for (int curPg = 0; curPg < numPages; ++curPg) {
        			int cropH;
        			if (curPg < numPages-1) 
        				cropH = (int)dstH;
        			else 
        				cropH = (int)(baseImage.getHeight() - (curPg*dstH));
        			
        			Bitmap cropped = Bitmap.createBitmap(bitmap, 0, (int)(curPg*dstH), (int)baseImage.getWidth(), cropH);
        			
        			String path = Util.getBaseName(imagePath) + "_" + curPg + ".png";
        			Util.saveBitmapToImage(cropped, path);
        		}
        		
        		PdfWriter.getInstance(document,new FileOutputStream(pdfPath));
        		document.open();
        		
        		for (int curPg = 0; curPg < numPages; ++curPg) {
        			String path = Util.getBaseName(imagePath) + "_" + curPg + ".png"; 
        			Image image = Image.getInstance(path);
        			image.scaleToFit(docW, docH);
        			document.add(image);
        			document.newPage();
        			File file = new File(path);
        			file.delete();
        		}
        		
//        		document.add(new Paragraph("Your Heading for the Image Goes Here"));
        		document.close();
    		} 
    		
    		return pdfPath;
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	return "";
    }
    
    public static String getPdfName(String name) {
		return Util.getBaseName(name) + ".pdf";
    }
    
    public static String getBaseName(String name) {
    	int pos = name.lastIndexOf(".");
		if (pos > 0) name = name.substring(0, pos);
		return name;
    }

}
