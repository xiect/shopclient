package com.brains.framework.util;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;


public class Debug {
    public static final String SYS_DEBUG = System.getProperty("DEBUG");
    
    public static final int ALWAYS = 0;
    public static final int VERBOSE = 1;
    public static final int TIMING = 2;
    public static final int INFO = 3;
    public static final int IMPORTANT = 4;
    public static final int WARNING = 5;
    public static final int ERROR = 6;
    public static final int FATAL = 7;
    public static final int NOTIFY = 8;

    protected static Map<String, Integer> levelStringMap = new HashMap<String, Integer>();

    protected static boolean levelOnCache[] = new boolean[9];
    protected static final boolean useLevelOnCache = true;
    public static final String[] levelProps = {"", "print.verbose", "print.timing", "print.info", "print.important", "print.warning", "print.error", "print.fatal", "print.notify"};

    
    static {
        levelStringMap.put("verbose", Debug.VERBOSE);
        levelStringMap.put("timing", Debug.TIMING);
        levelStringMap.put("info", Debug.INFO);
        levelStringMap.put("important", Debug.IMPORTANT);
        levelStringMap.put("warning", Debug.WARNING);
        levelStringMap.put("error", Debug.ERROR);
        levelStringMap.put("fatal", Debug.FATAL);
        levelStringMap.put("always", Debug.ALWAYS);
        levelStringMap.put("notify", Debug.NOTIFY);

        // initialize levelOnCache
        for (int i = 0; i < 9; i++) {
            levelOnCache[i] = (i == Debug.ALWAYS || UtilProperties.propertyValueEqualsIgnoreCase("debug.properties", levelProps[i], "true"));
        }
    }    
	
    public static boolean verboseOn() {
        return isOn(Debug.VERBOSE);
    }
    
    public static boolean isOn(int level) {
        if (useLevelOnCache) {
            return levelOnCache[level];
        } else {
            return (level == Debug.ALWAYS || UtilProperties.propertyValueEqualsIgnoreCase("debug", levelProps[level], "true"));
        }
    }
	
	public static void logWarning(String msg,String module){
		Log.w(module, msg);
	}
	
	public static void logVerbose(String msg,String module){
		Log.v(module, msg);
	}

	public static void logInfo(Exception e,String module){
		logInfo(e.getMessage(),module);
	}

	public static void logInfo(String msg,String module){
		Log.i(module, msg);
	}

	public static void logInfo(Exception e,String msg,String module){
		Log.i(module, e.getMessage()+" | "+msg);
	}

	public static void logError(Exception e,String msg,String module){
		Log.e(module, e.getMessage()+" | "+msg);
	}

	public static void logError(Exception e,String module){
		Log.e(module, e.getMessage());
	}
	
}
