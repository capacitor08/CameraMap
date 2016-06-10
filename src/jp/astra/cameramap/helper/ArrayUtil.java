package jp.astra.cameramap.helper;

/**
 * @category Helper
 * @author Kelvzs
 * @updated Kat
 */
public final class ArrayUtil {
	
	public static boolean checkArrayForElement(int[] array, int element) {
		
		for (int count = 0; count < array.length; count++) {
			if (array[count] == element) {
				return true;
			}
		}
		
		return false;
	}
	
	public static int getIndexOfElement(int[] array, int element) {
		
		for (int count = 0; count < array.length; count++) {
			if (array[count] == element) {
				return count;
			}
		}
		
		return -1;
	}
	
    public static Object[] increaseArraySize(Object[] oldArray, int length) {

    	if (length <= 0) {
    		length = 1;
    	}
        if (oldArray == null || oldArray.length == 0) {
            return new Object[length];
        }

        Object[] newArray = new Object[oldArray.length + length];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

        return newArray;
    }

    public static Object[] reduceArraySize(Object[] oldArray, int length) {

    	if (length <= 0) {
    		length = 1;
    	}
        if (oldArray == null || oldArray.length <= length) {
            return null;
        }

        Object[] newArray = new Object[oldArray.length - length];
        System.arraycopy(oldArray, 0, newArray, 0, newArray.length);

        return newArray;
    }

    public static String[] increaseArraySize(String[] oldArray, int length) {

    	if (length <= 0) {
    		length = 1;
    	}
        if (oldArray == null || oldArray.length == 0) {
            return new String[length];
        }

        String[] newArray = new String[oldArray.length + length];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

        return newArray;
    }

    public static String[] reduceArraySize(String[] oldArray, int length) {

    	if (length <= 0) {
    		length = 1;
    	}
        if (oldArray == null || oldArray.length <= length) {
            return null;
        }

        String[] newArray = new String[oldArray.length - length];
        System.arraycopy(oldArray, 0, newArray, 0, newArray.length);

        return newArray;
    }

    public static CharSequence[] increaseArraySize(CharSequence[] oldArray, int length) {

    	if (length <= 0) {
    		length = 1;
    	}
        if (oldArray == null || oldArray.length == 0) {
            return new String[length];
        }

        CharSequence[] newArray = new CharSequence[oldArray.length + length];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

        return newArray;
    }

    public static CharSequence[] reduceArraySize(CharSequence[] oldArray, int length) {

    	if (length <= 0) {
    		length = 1;
    	}
        if (oldArray == null || oldArray.length <= length) {
            return null;
        }

        CharSequence[] newArray = new CharSequence[oldArray.length - length];
        System.arraycopy(oldArray, 0, newArray, 0, newArray.length);

        return newArray;
    }

    public static Dictionary[] increaseDictionarySize(Dictionary[] oldArray, int length) {

    	if (length <= 0) {
    		length = 1;
    	}
        if (oldArray == null || oldArray.length == 0) {
            return new Dictionary[length];
        }

        Dictionary[] newArray = new Dictionary[oldArray.length + length];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

        return newArray;
    }

    public static Dictionary[] reduceDictionarySize(Dictionary[] oldArray, int length) {

    	if (length <= 0) {
    		length = 1;
    	}
        if (oldArray == null || oldArray.length <= length) {
            return null;
        }

        Dictionary[] newArray = new Dictionary[oldArray.length - length];
        System.arraycopy(oldArray, 0, newArray, 0, newArray.length);

        return newArray;
    }

    public static String[] combineStringArray(String[] mainString, String[] subString) {

    	if (mainString == null && subString == null) {
    		return null;
    	} else if (mainString == null) {
    		return subString;
    	} else if (subString == null) {
    		return mainString;
    	}

        int mainSize = mainString.length;
        int subSize = subString.length;
        String[] newString = new String[mainSize + subSize];

        for (int i = 0; i < mainSize; i++) {
            newString[i] = mainString[i];
        }

        for (int i = 0; i < subSize; i++) {
            newString[mainSize + i] = subString[i];
        }

        return newString;
    }

    public static String[] insertToStringArray(String[] mainString, String subString, int position) {

    	if (mainString == null) {
    		mainString = new String[0];
    	}
    	if (subString == null) {
    		return mainString;
    	}

        String[] newString = new String[mainString.length + 1];

        if (position < 0 || position > newString.length - 1) {
        	position = newString.length - 1;
        }

        int j = 0;
        for (int i = 0; i < newString.length; i++) {

        	if (i == position) {
        		newString[i] = subString;

        	} else {
	            newString[i] = mainString[j];
	            j++;
        	}
        }

        return newString;
    }

    public static String[] removeString(String[] mainString, String subString) {

    	if (mainString == null) {
    		return null;
    	} else if (StringUtil.isEmpty(subString)) {
    		return mainString;
    	}

        String[] frontData = new String[0];
        String[] endData = new String[0];
        boolean found = false;

        for (int i = 0; i < mainString.length; i++) {

            if (StringUtil.isEqual(subString, mainString[i], true)) {
                found = true;

            } else if (found) {
                endData = ArrayUtil.increaseArraySize(endData, 1);
                endData[endData.length - 1] = mainString[i];

            } else {
                frontData = ArrayUtil.increaseArraySize(frontData, 1);
                frontData[frontData.length - 1] = mainString[i];
            }
        }

        return ArrayUtil.combineStringArray(frontData, endData);
    }

    public static String combineString(String[] data, String separator) {

        String dataString = "";
    	separator = StringUtil.convertNullToBlank(separator);

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                dataString = dataString + data[i] + separator;
            }
        }

        return dataString.substring(0, dataString.length() - separator.length());
    }

    public static Dictionary[] combineDictionaryArray(Dictionary[] mainDictionary, Dictionary[] subDictionary) {

    	if (mainDictionary == null && subDictionary == null) {
    		return null;
    	} else if (mainDictionary == null) {
    		return subDictionary;
    	} else if (subDictionary == null) {
    		return mainDictionary;
    	}

        int mainSize = mainDictionary.length;
        int subSize = subDictionary.length;
        Dictionary[] newDictionary = new Dictionary[mainSize + subSize];

        for (int i = 0; i < mainSize; i++) {
            newDictionary[i] = mainDictionary[i];
        }

        for (int i = 0; i < subSize; i++) {
            newDictionary[mainSize + i] = subDictionary[i];
        }

        return newDictionary;
    }

    public static Dictionary[] insertToDictionaryArray(Dictionary[] mainDictionary, Dictionary subDictionary, int position) {

    	if (mainDictionary == null) {
    		mainDictionary = new Dictionary[0];
    	}
    	if (subDictionary == null) {
    		return mainDictionary;
    	}

    	Dictionary[] newDictionary= new Dictionary[mainDictionary.length + 1];

        if (position < 0 || position > newDictionary.length - 1) {
        	position = newDictionary.length - 1;
        }

        int j = 0;
        for (int i = 0; i < newDictionary.length; i++) {

        	if (i == position) {
        		newDictionary[i] = subDictionary;

        	} else {
	            newDictionary[i] = mainDictionary[j];
	            j++;
        	}
        }

        return newDictionary;
    }
}
