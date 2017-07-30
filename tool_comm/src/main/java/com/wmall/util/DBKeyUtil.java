package com.wmall.util;

import org.apache.log4j.Logger;

import java.text.*;
import java.util.Calendar;

/**
 *  
 * @author flj
 *
 */
public class DBKeyUtil { 
 
    /**
     * logger.
     */
    private static final Logger logger = Logger.getLogger(DBKeyUtil.class); 
 
    /** The FieldPosition. */ 
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0); 
 
    /** This Format for format the data to special format. */ 
    private static final Format dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS"); 
 
    /** This Format for format the number to special format. */ 
    private static final NumberFormat numberFormat = new DecimalFormat("000"); 
 
    /** This int is the sequence number ,the default value is 0. */ 
    private static int seq = 0; 
 
    private static final int MAX = 999; 
    /** 
     * 时间格式生成序列号 ，syscode只能是两位字母或数字.
     * @return String 
     */ 
    public static synchronized String getDBKey(String syscode) { 
 
        Calendar rightNow = Calendar.getInstance(); 
 
        StringBuffer sb = new StringBuffer(syscode); 
 
        dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION); 
 
        numberFormat.format(seq, sb, HELPER_POSITION); 
 
        if (seq == MAX) { 
            seq = 0; 
        } else { 
            seq++; 
        } 
        logger.info("THE SQUENCE IS :" + sb.toString()); 
        return sb.toString(); 
    } 
    
    public static Long getIntDBKey(String syscode) { 
    	return Long.parseLong(getDBKey(syscode));
    }

} 
