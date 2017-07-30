package com.wmall.comm;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
/**
 * Created by asus-pc on 2017/6/26.
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);    //日志记录
    /**
     * post请求
     * @param requrl         url地址
     * @param param     参数
     * @return
     */
    public static String httpPost(String requrl,String param){
        URL url;
        String sTotalString="";
        try {
            url = new URL(requrl);
            URLConnection connection = url.openConnection();

            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "text/xml");
            // connection.setRequestProperty("Content-Length", body.getBytes().length+"");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");


            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
            out.write(param); // 向页面传递数据。post的关键所在！
            out.flush();
            out.close();
            // 一旦发送成功，用以下方法就可以得到服务器的回应：
            String sCurrentLine;

            sCurrentLine = "";
            sTotalString = "";
            InputStream l_urlStream;
            l_urlStream = connection.getInputStream();
            // 传说中的三层包装阿！
            BufferedReader l_reader = new BufferedReader(new InputStreamReader(
                    l_urlStream));
            while ((sCurrentLine = l_reader.readLine()) != null) {
                sTotalString += sCurrentLine + "\r\n";

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(sTotalString);
        return sTotalString;
    }
    /*
    * 模拟get请求
    * @param url
    * @param charset
    * @param timeout
    * @return
            */
    public static String sendGet(String url, String charset, int timeout)
    {
        String result = "";
        try
        {
            URL u = new URL(url);
            try
            {
                URLConnection conn = u.openConnection();
                conn.connect();
                conn.setConnectTimeout(timeout);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
                String line="";
                while ((line = in.readLine()) != null)
                {

                    result = result + line;
                }
                in.close();
            } catch (IOException e) {
                return result;
            }
        }
        catch (MalformedURLException e)
        {
            return result;
        }

        return result;
    }
    public static void main(String[] args) {
        String appid="wxd43dfadd35d8c5de";//应用ID
        String appSecret="b19fab04aef0b80385539d508b3eec42";//(应用密钥)
        String getUrl ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appSecret+"";
        String backData=sendGet(getUrl, "utf-8", 10000);
        System.out.println("返回："+backData);
        JSONObject jsonObject = JSONObject.fromObject(backData);
        String access_token=jsonObject .getString("access_token");
        String add_url = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token="+access_token;
        String param="{\"kf_account\" : \"test1@auto2788\",\"nickname\" : \"客服1\"}";
        String data = httpPost(add_url, param);
        System.out.println(data);
    }
}
