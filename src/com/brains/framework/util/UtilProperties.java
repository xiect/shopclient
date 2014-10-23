package com.brains.framework.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.MissingResourceException;
import java.util.Properties;

/** Generic Property Accessor with Cache - Utilities for working with properties files.
 * <p>UtilProperties divides properties files into two classes: non-locale-specific -
 * which are used for application parameters, configuration settings, etc; and
 * locale-specific - which are used for UI labels, system messages, etc. Each class
 * of properties files is kept in its own cache.</p>
 * <p>The locale-specific class of properties files can be in any one of three
 * formats: the standard text-based key=value format (*.properties file), the Java
 * XML properties format, and the OFBiz-specific XML file format
 * (see the <a href="#xmlToProperties(java.io.InputStream,%20java.util.Locale,%20java.util.Properties)">xmlToProperties</a>
 * method).</p>
 */
@SuppressWarnings("serial")
public class UtilProperties implements Serializable {

    public static final String module = UtilProperties.class.getName();

    
    /** Compares the specified property to the compareString, returns true if they are the same, false otherwise
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param compareString The String to compare the property value to
     * @return True if the strings are the same, false otherwise
     */
    public static boolean propertyValueEquals(String resource, String name, String compareString) {
        String value = getPropertyValue(resource, name);

        if (value == null) return false;
        return value.trim().equals(compareString);
    }

    /** Compares Ignoring Case the specified property to the compareString, returns true if they are the same, false otherwise
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param compareString The String to compare the property value to
     * @return True if the strings are the same, false otherwise
     */
    public static boolean propertyValueEqualsIgnoreCase(String resource, String name, String compareString) {
        String value = getPropertyValue(resource, name);

        if (value == null) return false;
        return value.trim().equalsIgnoreCase(compareString);
    }

    /** Returns the value of the specified property name from the specified resource/properties file.
     * If the specified property name or properties file is not found, the defaultValue is returned.
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param defaultValue The value to return if the property is not found
     * @return The value of the property in the properties file, or if not found then the defaultValue
     */
    public static String getPropertyValue(String resource, String name, String defaultValue) {
        String value = getPropertyValue(resource, name);

        if (UtilValidate.isEmpty(value))
            return defaultValue;
        else
            return value;
    }

    public static double getPropertyNumber(String resource, String name, double defaultValue) {
        String str = getPropertyValue(resource, name);
        if (str == null) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static double getPropertyNumber(String resource, String name) {
        return getPropertyNumber(resource, name, 0.00000);
    }

    /**
     * Returns the Number as a Number-Object of the specified property name from the specified resource/properties file.
     * If the specified property name or properties file is not found, the defaultObject is returned.
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param defaultNumber Optional: The Number to return if the property is not found.
     * @param type A String of the the Object the Number is converted to (like "Integer").
     * @return A Number-Object of the property as the defined type; or if not found the defaultObject
     */
    private static Number getPropertyNumber(String resource, String name, Number defaultNumber, String type) {
        String str = getPropertyValue(resource, name);
        if (UtilValidate.isEmpty(str)) {
            Debug.logWarning("Error converting String \"" + str + "\" to " + type + "; using defaultNumber " + defaultNumber + ".", module);
            return defaultNumber;
        } else
            try {
                return (Number)(ObjectType.simpleTypeConvert(str, type, null, null));
            } catch (GeneralException e) {
                Debug.logWarning("Error converting String \"" + str + "\" to " + type + "; using defaultNumber " + defaultNumber + ".", module);
            }
            return defaultNumber;
    }

    /**
     * Returns a Boolean-Object of the specified property name from the specified resource/properties file.
     * If the specified property name or properties file is not found, the defaultValue is returned.
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param defaultValue Optional: The Value to return if the property is not found or not the correct format.
     * @return A Boolean-Object of the property; or if not found the defaultValue
     */
    public static Boolean getPropertyAsBoolean(String resource, String name, boolean defaultValue) {
        String str = getPropertyValue(resource, name);
        if ("true".equalsIgnoreCase(str)) return Boolean.TRUE;
        else if ("false".equalsIgnoreCase(str)) return Boolean.FALSE;
        else return defaultValue;
    }

    /**
     * Returns an Integer-Object of the specified property name from the specified resource/properties file.
     * If the specified property name or properties file is not found, the defaultNumber is returned.
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param defaultNumber Optional: The Value to return if the property is not found.
     * @return An Integer-Object of the property; or if not found the defaultNumber
     */
    public static Integer getPropertyAsInteger(String resource, String name, int defaultNumber) {
        return (Integer)getPropertyNumber(resource, name, defaultNumber, "Integer");
    }

    /**
     * Returns a Long-Object of the specified property name from the specified resource/properties file.
     * If the specified property name or properties file is not found, the defaultNumber is returned.
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param defaultNumber Optional: The Value to return if the property is not found.
     * @return A Long-Object of the property; or if not found the defaultNumber
     */
    public static Long getPropertyAsLong(String resource, String name, long defaultNumber) {
        return (Long)getPropertyNumber(resource, name, defaultNumber, "Long");
    }

    /**
     * Returns a Float-Object of the specified property name from the specified resource/properties file.
     * If the specified property name or properties file is not found, the defaultNumber is returned.
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param defaultNumber Optional: The Value to return if the property is not found.
     * @return A Long-Object of the property; or if not found the defaultNumber
     */
    public static Float getPropertyAsFloat(String resource, String name, float defaultNumber) {
        return (Float)getPropertyNumber(resource, name, defaultNumber, "Float");
    }

    /**
     * Returns a Double-Object of the specified property name from the specified resource/properties file.
     * If the specified property name or properties file is not found, the defaultNumber is returned.
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param defaultNumber Optional: The Value to return if the property is not found.
     * @return A Double-Object of the property; or if not found the defaultNumber
     */
    public static Double getPropertyAsDouble(String resource, String name, double defaultNumber) {
        return (Double)getPropertyNumber(resource, name, defaultNumber, "Double");
    }

    /**
     * Returns a BigInteger-Object of the specified property name from the specified resource/properties file.
     * If the specified property name or properties file is not found, the defaultNumber is returned.
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param defaultNumber Optional: The Value to return if the property is not found.
     * @return A BigInteger-Object of the property; or if not found the defaultNumber
     */
    public static BigInteger getPropertyAsBigInteger(String resource, String name, BigInteger defaultNumber) {
        String strValue = getPropertyValue(resource, name);
        BigInteger result = defaultNumber;
        try {
            result = new BigInteger(strValue);
        } catch (NumberFormatException nfe) {
            Debug.logWarning("Couldnt convert String \"" + strValue + "\" to BigInteger; using defaultNumber " + defaultNumber.toString() + ".", module);
        }
        return result;
    }

    /**
     * Returns a BigDecimal-Object of the specified property name from the specified resource/properties file.
     * If the specified property name or properties file is not found, the defaultNumber is returned.
     * @param resource The name of the resource - if the properties file is 'webevent.properties', the resource name is 'webevent'
     * @param name The name of the property in the properties file
     * @param defaultNumber Optional: The Value to return if the property is not found.
     * @return A BigDecimal-Object of the property; or if not found the defaultNumber
     */
    public static BigDecimal getPropertyAsBigDecimal(String resource, String name, BigDecimal defaultNumber) {
        String strValue = getPropertyValue(resource, name);
        BigDecimal result = defaultNumber;
        try {
            result = new BigDecimal(strValue);
        } catch (NumberFormatException nfe) {
            Debug.logWarning("Couldnt convert String \"" + strValue + "\" to BigDecimal; using defaultNumber " + defaultNumber.toString() + ".", module);
        }
        return result;
    }

    /** Returns the value of the specified property name from the specified resource/properties file
     * @param resource The name of the resource - can be a file, class, or URL
     * @param name The name of the property in the properties file
     * @return The value of the property in the properties file
     */
    public static String getPropertyValue(String resource, String name) {
        if (resource == null || resource.length() <= 0) return "";
        if (name == null || name.length() <= 0) return "";

        Properties properties = getProperties(resource);
        if (properties == null) {
            return "";
        }

        String value = null;

        try {
            value = properties.getProperty(name);
        } catch (Exception e) {
            Debug.logInfo(e, module);
        }
        return value == null ? "" : value.trim();
    }

    /** Returns the specified resource/properties file
     * @param resource The name of the resource - can be a file, class, or URL
     * @return The properties file
     */
    public static Properties getProperties(String resource) {
        if (resource == null || resource.length() <= 0) {
            return null;
        }
        Properties properties = new Properties();
        try {
        	if(resource.indexOf("properties")==-1){
        		resource += ".properties";
        	}
        	resource = "/assets/" + resource;
        	properties.load(UtilProperties.class.getResourceAsStream(resource));
        } catch (MissingResourceException e) {
            Debug.logInfo(e, module);
        } catch (IOException e) {
            Debug.logInfo(e, module);
		}
        return properties;
    }



    // ========= URL Based Methods ==========

    /** Sets the specified value of the specified property name to the specified resource/properties file
     * @param resource The name of the resource - must be a file
     * @param name The name of the property in the properties file
     * @param value The value of the property in the properties file */
     public static void setPropertyValue(String resource, String name, String value) {
         if (resource == null || resource.length() <= 0) return;
         if (name == null || name.length() <= 0) return;

         Properties properties = getProperties(resource);
         if (properties == null) {
             return;
         }

         try {
             properties.setProperty(name, value);
             FileOutputStream propFile = new FileOutputStream(resource);
             if ("XuiLabels".equals(name)) {
                 properties.store(propFile,
                     "##############################################################################\n"
                     +"# Licensed to the Apache Software Foundation (ASF) under one                   \n"
                     +"# or more contributor license agreements.  See the NOTICE file                 \n"
                     +"# distributed with this work for additional information                        \n"
                     +"# regarding copyright ownership.  The ASF licenses this file                   \n"
                     +"# to you under the Apache License, Version 2.0 (the                            \n"
                     +"# \"License\"); you may not use this file except in compliance                 \n"
                     +"# with the License.  You may obtain a copy of the License at                   \n"
                     +"#                                                                              \n"
                     +"# http://www.apache.org/licenses/LICENSE-2.0                                   \n"
                     +"#                                                                              \n"
                     +"# Unless required by applicable law or agreed to in writing,                   \n"
                     +"# software distributed under the License is distributed on an                  \n"
                     +"# \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY                     \n"
                     +"# KIND, either express or implied.  See the License for the                    \n"
                     +"# specific language governing permissions and limitations                      \n"
                     +"# under the License.                                                           \n"
                     +"###############################################################################\n"
                     +"#                                                                              \n"
                     +"# Dynamically modified by OFBiz Framework (org.ofbiz.base.util : UtilProperties.setPropertyValue)\n"
                     +"#                                                                              \n"
                     +"# By default the screen is 1024x768 wide. If you want to use another screen size,\n"
                     +"# you must create a new directory under specialpurpose/pos/screens, like the 800x600.\n"
                     +"# You must also set the 3 related parameters (StartClass, ClientWidth, ClientHeight) accordingly.\n"
                     +"#");
             } else {
                 properties.store(propFile,
                     "##############################################################################\n"
                     +"# Licensed to the Apache Software Foundation (ASF) under one                   \n"
                     +"# or more contributor license agreements.  See the NOTICE file                 \n"
                     +"# distributed with this work for additional information                        \n"
                     +"# regarding copyright ownership.  The ASF licenses this file                   \n"
                     +"# to you under the Apache License, Version 2.0 (the                            \n"
                     +"# \"License\"); you may not use this file except in compliance                 \n"
                     +"# with the License.  You may obtain a copy of the License at                   \n"
                     +"#                                                                              \n"
                     +"# http://www.apache.org/licenses/LICENSE-2.0                                   \n"
                     +"#                                                                              \n"
                     +"# Unless required by applicable law or agreed to in writing,                   \n"
                     +"# software distributed under the License is distributed on an                  \n"
                     +"# \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY                     \n"
                     +"# KIND, either express or implied.  See the License for the                    \n"
                     +"# specific language governing permissions and limitations                      \n"
                     +"# under the License.                                                           \n"
                     +"###############################################################################\n"
                     +"#                                                                              \n"
                     +"# Dynamically modified by OFBiz Framework (org.ofbiz.base.util : UtilProperties.setPropertyValue)\n"
                     +"# The comments have been removed, you may still find them on the OFBiz repository... \n"
                     +"#");
             }

             propFile.close();
         } catch (FileNotFoundException e) {
             Debug.logInfo(e, "Unable to located the resource file.", module);
         } catch (IOException e) {
             Debug.logError(e, module);
         }
     }

     /** Sets the specified value of the specified property name to the specified resource/properties in memory, does not persist it
      * @param resource The name of the resource
      * @param name The name of the property in the resource
      * @param value The value of the property to set in memory */
      public static void setPropertyValueInMemory(String resource, String name, String value) {
          if (resource == null || resource.length() <= 0) return;
          if (name == null || name.length() <= 0) return;

          Properties properties = getProperties(resource);
          if (properties == null) {
              return;
          }
          properties.setProperty(name, value);
      }

    
}
