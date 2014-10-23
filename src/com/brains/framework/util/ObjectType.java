package com.brains.framework.util;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * Utilities for analyzing and converting Object types in Java
 * Takes advantage of reflection
 */
public class ObjectType {	

    public static final String module = ObjectType.class.getName();
	
    @SuppressWarnings("unchecked")
    public static boolean isEmpty(Object value) {
        if (value == null) return true;
        if (value instanceof String) return UtilValidate.isEmpty((String) value);
        if (value instanceof Collection) return UtilValidate.isEmpty((Collection<? extends Object>) value);
        if (value instanceof Map) return UtilValidate.isEmpty((Map<? extends Object, ? extends Object>) value);
        if (value instanceof CharSequence) return UtilValidate.isEmpty((CharSequence) value);

        // These types would flood the log
        // Number covers: BigDecimal, BigInteger, Byte, Double, Float, Integer, Long, Short
        if (value instanceof Boolean) return false;
        if (value instanceof Number) return false;
        if (value instanceof Character) return false;
        if (value instanceof java.sql.Timestamp) return false;

        if (Debug.verboseOn()) {
            Debug.logVerbose("In ObjectType.isEmpty(Object value) returning false for " + value.getClass() + " Object.", module);
        }
        return false;
    }
    
    public static Object simpleTypeConvert(Object obj, String type, String format, Locale locale, boolean noTypeFail) throws GeneralException {
        return new Object();
    }
    
    public static Object simpleTypeConvert(Object obj, String type, String format, Locale locale) throws GeneralException {
        return simpleTypeConvert(obj, type, format, locale, true);
    }

    
}
