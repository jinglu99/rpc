package com.jingl.common;

/**
 * Created by Ben on 26/11/2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GetRemoteInfo {

    public static boolean getRemoteInfo(String urlStr,String method,Map<String, String>headers,String data,StringBuilder response,Set<Integer>status) {

        URL url = null;
        BufferedReader br = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            if(method=="POST"){
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
            }else {
                conn.setRequestMethod(method);
            }

            if(headers!=null)
                for(Map.Entry<String, String>entry:headers.entrySet()){
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }

            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            if(data!=null&&data.length()>0){
                PrintWriter pw = new PrintWriter(conn.getOutputStream());
                pw.write(data);
                pw.flush();
                pw.close();
            }
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
//			System.out.println(conn.getResponseCode());
            String line = "";
            String result = "";
            while((line = br.readLine())!=null){
                result += line+"\n";
            }
            if(status.contains(conn.getResponseCode())){

                response.append(result);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            if(conn!=null)
                conn.disconnect();
            if(br!=null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return false;
    }
}

