package jp.astra.cameramap.helper;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.maps.GeoPoint;

/**
 * @category Helper
 * @author Lau
 */
public final class StringUtil {

    public static boolean isEmpty(String string) {

        if (string == null || string.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEqual(String string1, String string2, boolean strict) {

        boolean empty1 = StringUtil.isEmpty(string1);
        boolean empty2 = StringUtil.isEmpty(string2);

        if (empty1 && empty2) {
            return true;

        } else if (empty1 || empty2) {
            return false;

        } else {

            if (strict && string1.compareTo(string2) == 0) {
                return true;

            } else if (!strict && string1.compareToIgnoreCase(string2) == 0) {
                return true;
            }

            return false;
        }
    }

    public static boolean contains(String[] mainString, String subString) {

    	for (int i = 0; i < mainString.length; i++) {
    		if (StringUtil.isEqual(mainString[i], subString, false)) {
				return true;
			}
    	}

		return false;
    }

    public static String convertNullToBlank(String string) {

    	if (StringUtil.isEmpty(string)) {
    		return "";
    	} else {
    		return string;
    	}
    }

    public static String convertString(Context context, String mainString, int replacement) {
    	return mainString.replaceFirst("%@", String.valueOf(replacement));
    }

    public static String convertString(Context context, int mainString, int replacement) {
    	return StringUtil.convertString(context, context.getString(mainString), replacement);
    }

    public static String convertString(Context context, String mainString, String replacement) {
    	return mainString.replaceFirst("%@", replacement);
    }

    public static String convertString(Context context, int mainString, String replacement) {
    	return StringUtil.convertString(context, context.getString(mainString), replacement);
    }

    public static String convertString(Context context, String mainString, String[] replacements) {

        for (int i = 0; i < replacements.length; i++) {
            mainString = mainString.replaceFirst("%@", replacements[i]);
        }

        return mainString;
    }

    public static String convertString(Context context, int mainString, String[] replacements) {
    	return StringUtil.convertString(context, context.getString(mainString), replacements);
    }

    public static String[] convertResourcesArray(Context context, int[] resources) {
    	String[] stringResources = new String[resources.length];

    	for (int i = 0; i < resources.length; i++) {
    		stringResources[i] = context.getString(resources[i]);
    	}

    	return stringResources;
    }

    public static String convertCamelCaseToReadable(String s) {
	   return s.replaceAll(
	      String.format("%s|%s|%s",
	         "(?<=[A-Z])(?=[A-Z][a-z])",
	         "(?<=[^A-Z])(?=[A-Z])",
	         "(?<=[A-Za-z])(?=[^A-Za-z])"
	      ),
	      " "
	   );
    }

    public static String urlEncode(Context context, String string) {

        try {
            return URLEncoder.encode(string, "UTF-8").replace("+", "%20").replace(".", "%2E").replace("-", "%2D").replace("*", "%2A").replace("_", "%5F");

        } catch (Throwable t) {
        	Log.e("CameraMap", "StringUtil.urlEncode", t);
            return string;
        }
    }

    public static String urlDecode(Context context, String string) {

        try {
            return URLDecoder.decode(string, "UTF-8");

        } catch (Throwable t) {
        	Log.e("CameraMap", "StringUtil.urlDecode", t);
            return string;
        }
    }

    public static String fileBaseName(String filePath) {

    	int idx = filePath.lastIndexOf('/');
    	if (idx == -1) {
    		return filePath;
    	}
    	if (idx == 0) {
			return new String("");
		}
    	return filePath.substring(idx + 1,filePath.length());
	}

    public static String convertDate(String dateTime){
    	if(StringUtil.isEmpty(dateTime)){
    		return "";
    	}
    	SimpleDateFormat dateParser = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    	SimpleDateFormat dateConverter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Date d = null;
		try {
			d = dateParser.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return dateConverter.format(d);
    }

    @SuppressLint("UseValueOf")
	public static Float convertToDegree(String stringDMS){
      	 Float result = null;
      	 String[] DMS = stringDMS.split(",", 3);

      	 String[] stringD = DMS[0].split("/", 2);
      	    Double D0 = new Double(stringD[0]);
      	    Double D1 = new Double(stringD[1]);
      	    Double FloatD = D0/D1;

      	 String[] stringM = DMS[1].split("/", 2);
      	 Double M0 = new Double(stringM[0]);
      	 Double M1 = new Double(stringM[1]);
      	 Double FloatM = M0/M1;

      	 String[] stringS = DMS[2].split("/", 2);
      	 Double S0 = new Double(stringS[0]);
      	 Double S1 = new Double(stringS[1]);
      	 Double FloatS = S0/S1;

      	    result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

      	 return result;
      	};

      	public static String convertToDegreeString(String stringDMS, String Direction){
         	 String[] DMS = stringDMS.split(",", 3);
         	 String converted = null;
         	 String[] stringD = DMS[0].split("/", 2);
         	    Double D0 = new Double(stringD[0]);
         	    Double D1 = new Double(stringD[1]);
         	    Double FloatD = D0/D1;

         	 String[] stringM = DMS[1].split("/", 2);
         	 Double M0 = new Double(stringM[0]);
         	 Double M1 = new Double(stringM[1]);
         	 Double FloatM = M0/M1;

         	 String[] stringS = DMS[2].split("/", 2);
         	 Double S0 = new Double(stringS[0]);
         	 Double S1 = new Double(stringS[1]);
         	 Double FloatS = S0/S1;

         	 converted = FloatD + "° " + FloatM + "\' " + FloatS + "\" " + Direction;

         	 return converted;
         	};

        public static String ConvertPointToLocation(Context context, GeoPoint point) {
            String address = "";
            Geocoder geoCoder = new Geocoder(
                    context, Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(
                    point.getLatitudeE6()  / 1E6,
                    point.getLongitudeE6() / 1E6, 1);

                if (addresses.size() > 0) {
                    for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex()+ 1; index++) {
						address += addresses.get(0).getAddressLine(index) + " ";
					}
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return address;
        }
}
