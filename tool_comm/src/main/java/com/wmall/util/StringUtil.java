package com.wmall.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus-pc on 2017/6/12.
 */
public class StringUtil {

    /**
     * 去掉DOUBLE 类型小数点后面的数字 变整数
     */
    public static String DoubleToInteger(Double doublestr) {
        if (doublestr == null) {
            return "";
        }
        String[] strlast = String.valueOf(doublestr).split("\\.", -1);
        if (isNotEmpty(strlast[0])) {
            return strlast[0];
        } else {
            return "";
        }
    }

    /**
     * 去掉所有空格
     *
     * @param str
     * @return
     */
    public static String trimAll(Object str) {
        String newStr = nullToString(str);
        return newStr.replace(" ", nullToString(null));
    }

    /**
     * 获取最后的方法名
     *
     * @param menuPath
     */
    public static String getActionName(String menuPath) {

        String name = "";

        String[] arr = menuPath.split("/");

        if (null == arr) {

            return "";
        } else {

            if (arr.length > 2) {
                name = arr[arr.length - 2].toString();// +"/"+ arr[arr.length -
                // 1].toString();
                // name = name.substring(0, name.indexOf("."));
            }
        }
        return name;

    }

    /**
     * 获取空格
     *
     * @param number
     * @return
     */
    public static String getBlankObject(Object number) {

        // 判断是否有值
        if (number == null || number.equals("null") || number.equals("")) {

            return "empty";
        } else {

            return number.toString();
        }

    }

    public static String likeStr(String str) {
        return "%" + str.trim() + "%";
    }

    /**
     * 本类用到的方法--null转变成空
     *
     * @param obj
     * @return
     */
    public static String nullToString(Object obj) {
        String resource = "";
        if (obj == null || obj.equals("null")) {
            return resource;
        } else {
            resource = obj.toString().trim();
        }
        return resource;
    }

    /**
     * 本类用到的方法--null转变成空
     *
     * @param obj
     * @return
     */
    public static String nullToNumber(Object obj) {
        String resource = "0";
        if (obj == null || obj.equals("null")) {
            return resource;
        } else {
            resource = obj.toString().trim();
        }
        return resource;
    }

    // 0100888888880099 => 0100888888880100
    public static String getNextCardCode(String oldCode) {
        String newCode = "" + (Long.parseLong(oldCode) + 1);
        while (newCode.length() < 16) {
            newCode = "0" + newCode;
        }
        return newCode;
    }

    /**
     * 判断对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {

        if (obj == null) {
            return true;
        }
        if (StringUtil.isBlank(obj.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 判断对象是否不为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object obj) {

        if (obj == null) {
            return false;
        }
        if (StringUtil.isBlank(obj.toString())) {
            return false;
        }
        return true;
    }

    /**
     * 分隔字符 , 注意：如果前后有分隔符，String.split会多出来一个。该方法自动去掉前后分隔符再调用 String.split
     * 注意：特殊字符 $ % 等，需要使用 转义 $, 改为 \\$ aibo zeng 2009-06-09
     *
     * @param str
     * @param ch
     * @return
     */
    public static String[] split(String str, char ch) {
        if (str == null) {
            return null;
        }
        if (str.charAt(0) == ch) {
            str = str.substring(1);
        }
        if (str.charAt(str.length() - 1) == ch) {
            str = str.substring(0, str.length() - 1);
        }
        return str.split(ch + "");
    }

    /**
     * 判断字符串是否为null或空字符串,在模糊查询的时候很有意义
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    public static boolean isEmpty(Long o) {
        return (o == null);
    }

    public static boolean isEmpty(Integer o) {
        return (o == null);
    }

    public static boolean isEmpty(Date o) {
        return (o == null);
    }

    public static boolean isEmpty(BigDecimal o) {
        return (o == null);
    }

    public static boolean isEmpty(Object o) {
        return (o == null);
    }

    /**
     * 判断字符串是否为null或空字符串,在模糊查询的时候很有意义
     */
    public static boolean isNotEmpty(String str) {
        return (str != null && !"".equals(str.trim()));
    }

    public static boolean isNotEmpty(Long o) {
        return (o != null);
    }

    public static boolean isNotEmpty(Integer o) {
        return (o != null);
    }

    public static boolean isNotEmpty(Date o) {
        return (o != null);
    }

    public static boolean isNotEmpty(BigDecimal o) {
        return (o != null);
    }

    public static boolean isNotEmpty(Object o) {
        return (o != null);
    }

    /**
     * 抓取网站源代码
     *
     * @param surl
     * @return
     */
    public static String getStaticPage(String surl) {
        String htmlContent = "";
        InputStream inputStream = null;
        try {
            URL url = new URL(surl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();
            byte bytes[] = new byte[1024 * 2000];
            int index = 0;
            int count = inputStream.read(bytes, index, 1024 * 2000);
            while (count != -1) {
                index += count;
                count = inputStream.read(bytes, index, 1);
            }
            htmlContent = new String(bytes, "UTF-8");
            connection.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return htmlContent.trim();
    }

    /**
     * 把MAP里不为空的key按ASCII排序
     *
     * @param hash
     * @return 排序后的字符串
     */
    public static String packageSupSign(HashMap<String, String> hash, String partnet_key, String supUser_key) {
        TreeMap<String, String> tempInfo = new TreeMap<String, String>(new Comparator<String>() {

            /**
             * int compare(Object o1, Object o2) 返回一个基本类型的整型， 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
             */
            public int compare(String o1, String o2) {
				/*
				 * o1 = o1.toLowerCase(); o2 = o2.toLowerCase();
				 */
                // 指定排序器按照降序排列
                return o1.compareTo(o2);
            }
        });
        if (null != hash) {
            Iterator<String> it = hash.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = hash.get(key);
                if (StringUtil.isNotEmpty(value)) {
                    tempInfo.put(key, value);

                }

            }

        }
        Set<String> set = tempInfo.keySet();
        Iterator<String> it = set.iterator();
        StringBuffer buffer = new StringBuffer();
        while (it.hasNext()) {
            String key = it.next();
            String value = hash.get(key);
            if (StringUtil.isNotEmpty(value)) {
                buffer.append(key).append(value);

            }
        }
        System.out.println("sign------------------------" + buffer.toString() + partnet_key + supUser_key);
        // 返回签名串
        // yuzhijia 20130105 edit 不能去掉，
        return buffer.toString() + partnet_key + supUser_key;
        // return tempInfo.toString().replace("{", "").replace("}",
        // "").replace("=", "").replace(",", "").replace(" ", "") + partnet_key
        // + supUser_key;
    }

    /**
     * 返回URL中的参数字符串（MAP里VALUE为空不加入参数，VALUE经过URLEncode编码）
     *
     * @param hash
     * @return 排序后的字符串
     */
    public static String urlParameters(HashMap<String, String> hash) {
        String urlParameters = "";
        StringBuffer buffer = new StringBuffer();
        if (null != hash) {
            Iterator<String> it = hash.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = hash.get(key);
                if (StringUtil.isNotEmpty(value)) {
                    buffer.append(key + "=" + StringUtil.parameterEncode(value, "GBK") + "&");
                }

            }
            if (StringUtil.isNotEmpty(buffer.toString())) {
                urlParameters = buffer.toString().substring(0, buffer.toString().length() - 1);
            }

        }
        return urlParameters;
    }

    /**
     * 把参数通过URLEncode转码
     *
     * @param beforeFormat
     * @return
     */
    public static String parameterEncode(String beforeFormat, String format) {
        String afterFormat = "";
        try {
            afterFormat = URLEncoder.encode(beforeFormat, format);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return afterFormat;
    }

    /**
     * 判断对象是否为空
     *
     * @param o
     * @return
     */
    public static boolean isBlank(Object o) {
        // o = o.toString().trim();
        if (o == null) {
            return true;
        }
        if (StringUtils.isBlank(o.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 判断对象是否不为空
     *
     * @param o
     * @return
     */
    public static boolean isNotBlank(Object o) {

        // o = o.toString().trim();
        if (o == null) {
            return false;
        }
        if (StringUtils.isBlank(o.toString())) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是正整数
     *
     * @param number
     * @return
     */
    public static boolean isTrueInteger(String number) {
        String test = "\\d+";
        if (number != null && Pattern.compile(test).matcher(number).matches()) {
            return  true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是数字
     *
     * @param number
     * @return
     */
    public static boolean isTrueNumber(String number) {

        boolean rs = false;

        // 判断是否带小数点
        if (number.indexOf(".") > -1) {

            String test = "\\d+.\\d+";

            if (number != null && Pattern.compile(test).matcher(number).matches()) {

                rs = true;
            } else {

                rs = false;
            }

        } else {

            String test = "\\d+";

            if (number != null && Pattern.compile(test).matcher(number).matches()) {

                rs = true;
            } else {

                rs = false;
            }

        }

        return rs;
    }

    // 根据对应的开始时间查询对应的表名信息
    public static String getTableName(String time) {

        String table = "tj_ownerfunds";

        String[] elements = time.split("-");
        if (elements.length != 3) {
            return table;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) - 3;

        // 判断年份是不是小于当前年
        if (Integer.parseInt(elements[0]) < year) {

            table = "tj_ownerfunds_history";
        } else {

            // 判断对应的月份是不是小于当前月份
            if (Integer.parseInt(elements[1]) <= month) {

                table = "tj_ownerfunds_history";
            }
        }
        return table;

    }

    /**
     * 将字符串转换成List对象
     *
     * @param obj
     * @return
     */
    public static List<Integer> getStringToList(String obj) {

        if (obj == null || obj.equals("")) {
            return null;
        }

        String[] arr = obj.split(",");

        List<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < arr.length; i++) {

            list.add(Integer.parseInt(arr[i].toString()));
        }

        return list;
    }

    /**
     * Chica.Yu 20130309 add 把所有标准的
     * http://www.cxxcom/sss/sxx/x/x/或者htpps://www.cxx.com/sdfsdfx/x/x/x/截取到第3个/
     * 符号为止 结果形式：http://www.cxxcom/ htpps://www.cxx.com/
     *
     * @param urlstr
     *            需要截取的URL
     * @return
     */
    public static String cutUrl(String urlstr) {
        String http = "http://";
        String https = "https://";
        if (isNotEmpty(urlstr)) {
            if (urlstr.startsWith(http)) {
                urlstr = urlstr.substring(http.length(), urlstr.length());
                if (urlstr.contains("/")) {
                    urlstr = urlstr.substring(0, urlstr.indexOf("/") + 1);
                }
                urlstr = http + urlstr;
            }
            if (urlstr.startsWith(https)) {
                urlstr = urlstr.substring(https.length(), urlstr.length());
                if (urlstr.contains("/")) {
                    urlstr = urlstr.substring(0, urlstr.indexOf("/") + 1);
                }
                urlstr = https + urlstr;
            }

        } else {
            urlstr = "";
        }
        return urlstr;
    }

    // Chica.Yu 2013年3月18日17:56:37 空数字转化--------------------------------
    public static Double null20(Double d) {
        if (StringUtil.isBlank(d)) {
            return 0d;
        }
        return d;
    }

    public static Long null20(Long d) {
        if (StringUtil.isBlank(d)) {
            return 0l;
        }
        return d;
    }

    public static Integer null20(Integer d) {
        if (StringUtil.isBlank(d)) {
            return 0;
        }
        return d;
    }

    public static Integer str2int(String s) {
        if (StringUtil.isEmpty(s)) {
            return 0;
        }
        Integer reuslt=0;
        try{
            reuslt=Integer.parseInt(s);
        }catch(Exception e){

        }
        return reuslt;
    }

    public static Long int2Long(Integer d) {
        return Long.parseLong(null20(d) + "");
    }

    public static Integer logn2int(Long d) {
        return Integer.parseInt(null20(d) + "");
    }

    public static Integer Double2int(Double d) {
        String t = DoubleToInteger(d);
        return Integer.parseInt(StringUtil.isBlank(t) ? "0" : t);
    }

    public static Double null20(String d) {
        if (StringUtil.isBlank(d)) {
            return 0d;
        }
        return Double.parseDouble(d);
    }

    // ------------------------------------------------end

    // 判断Integer是否大于0
    public static boolean checkInteger(Integer num) {
        if (isEmpty(num)) {
            return false;
        } else {
            if (num > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 获取网页源码
     *
     * @param pageURL
     *            URL地址
     * @param encoding
     *            编码
     * @return
     */
    public static String getHTML(String pageURL, String encoding) {

        StringBuilder pageHTML = new StringBuilder();
        try {
            URL url = new URL(pageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "MSIE 7.0");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            String line = null;
            while ((line = br.readLine()) != null) {
                pageHTML.append(line);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageHTML.toString();
    }

    /**
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
     *
     * @param c, 需要判断的字符
     * @return boolean, 返回true,Ascill字符
     */
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param s ,需要得到长度的字符串
     * @return int, 得到的字符串长度
     */
    public static int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
     *
     * @author patriotlml
     * @param origin, 原始字符串
     * @param len, 截取长度(一个汉字长度按2算的)
     * @return String, 返回的字符串
     */
    public static String substring(String origin, int len) {
        if (origin == null || origin.equals("") || len < 1)
            return "";
        byte[] strByte = new byte[len];
        if (len > length(origin)) {
            return origin;
        }
        char[] c = origin.toCharArray();
        int len1 = 0;// 字符串在页面上的长度
        int len2 = 0;// 字符的个数
        String restr = null;
        for (int i = 0; i < c.length; i++) {
            if (len1 < len) {
                len1++;
                len2++;
                if (!isLetter(c[i])) {
                    len1++;
                }
            } else {
                if (len1 == len) {
                    restr = origin.substring(0, len2);
                } else {
                    restr = origin.substring(0, len2 - 1);
                }
                break;
            }
        }
        return restr;
    }

    /**
     * 字符串信息处理 将处于字符串前后的","去除
     *
     * @param strids
     * @return
     */
    public static String getSplitStr(String strids) {
        if (strids == null) {
            return "";
        }
        if (strids.contains("，")) {
            strids = strids.replaceAll("，", ",");
        }
        if (strids.startsWith(",")) {
            strids = strids.substring(1, strids.length());
        }
        if (strids.endsWith(",")) {
            strids = strids.substring(0, strids.length() - 1);
        }
        return strids;
    }


    /**
     * unicode转中文
     * @param theString
     * @return
     */
    public static String decodeUnicode(String theString) {

        char aChar;

        int len = theString.length();

        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len; ) {

            aChar = theString.charAt(x++);

            if (aChar == '\\') {

                aChar = theString.charAt(x++);

                if (aChar == 'u') {

                    // Read the xxxx

                    int value = 0;

                    for (int i = 0; i < 4; i++) {

                        aChar = theString.charAt(x++);

                        switch (aChar) {

                            case '0':

                            case '1':

                            case '2':

                            case '3':

                            case '4':

                            case '5':

                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';

                    else if (aChar == 'n')

                        aChar = '\n';

                    else if (aChar == 'f')

                        aChar = '\f';

                    outBuffer.append(aChar);

                }

            } else

                outBuffer.append(aChar);

        }

        return outBuffer.toString();

    }


    public static Boolean isIPAddress(String ip) {
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static Long getIPRNByIP(String ip) {
        Long ipRN = 0L;
        try {
            String[] ipArr = ip.split("\\.");
            if (ipArr.length == 4) {
                Long a1 = Long.valueOf(ipArr[0]);
                Long a2 = Long.valueOf(ipArr[1]);
                Long a3 = Long.valueOf(ipArr[2]);
                Long a4 = Long.valueOf(ipArr[3]);
                ipRN = a1 * 1000000000 + a2 * 1000000 + a3 * 1000 + a4;
                return ipRN;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
