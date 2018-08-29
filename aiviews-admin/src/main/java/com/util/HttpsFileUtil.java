package com.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class HttpsFileUtil {
	public static String sendPostWithFile(String url,File file) {
		
		DataOutputStream out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			String BOUNDARY = "-----------------------------7da2e536604c8";
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			conn.connect();
			
			out=new DataOutputStream(conn.getOutputStream());
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			
			StringBuilder sb1 = new StringBuilder();
			sb1.append("--");
			sb1.append(BOUNDARY);
			sb1.append("\r\n");
			sb1.append("Content-Disposition: form-data;name=\"luid\"");
			sb1.append("\r\n");
			sb1.append("\r\n");
			sb1.append("123");
			sb1.append("\r\n");
			out.write(sb1.toString().getBytes());
			
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+file.getName() +"\"");
			sb.append("\r\n");
			sb.append("Content-Type:application/octet-stream");
			sb.append("\r\n");
			sb.append("\r\n");
			out.write(sb.toString().getBytes());
			
			DataInputStream in1 = new DataInputStream(new FileInputStream(file)); 
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while((bytes = in1.read(bufferOut)) != -1) {
				out.write(end_data);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(out !=null) {
					out.close();
				}
				if(in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static void testFile(String urlString ,File file) throws Exception{
		//本地图片
	    FileInputStream fileInputStream = new FileInputStream(file);
	    URL url = new URL(urlString);
	    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
	    // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
	    // http正文内，因此需要设为true, 默认情况下是false;
	    con.setDoOutput(true);
	    // 设置是否从httpUrlConnection读入，默认情况下是true;
	    con.setDoInput(true);
	    // 设定请求的方法为"POST"，默认是GET
	    con.setRequestMethod("POST");
	    // Post 请求不能使用缓存
	    con.setUseCaches(false);
	    // 设定传送的内容类型是可序列化的java对象
	    // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
//	    con.setRequestProperty("Content-type", "application/x-java-serialized-object");
	    OutputStream out = con.getOutputStream();
	 
	    //读取本地图片文件流
	    FileInputStream inputStream = new FileInputStream(file);
	    byte[] data = new byte[2048];
	    int len = 0;
	    int sum = 0;
	    while ((len = inputStream.read(data)) != -1) {
	      //将读取到的本地文件流读取到HttpsURLConnection,进行上传
	      out.write(data, 0, len);
	      sum = len + sum;
	    }
	 
	    System.out.println("上传文件大小为:" + sum);
	 
	    out.flush();
	    inputStream.close();
	    out.close();
	 
	    int code = con.getResponseCode(); //获取post请求返回状态
	    System.out.println("code=" + code + " url=" + url);
	    /*if (code == 200) {
	      InputStream inputStream2 = con.getInputStream();
	      ByteArrayOutputStream bos = new ByteArrayOutputStream();
	      while ((len = inputStream2.read(data)) != -1) {
	        bos.write(data, 0, len);
	      }
	      inputStream2.close();
	      String content = bos.toString();
	      bos.close();
	      System.out.println("result =" + content);
	    }*/
	    //断开HttpsURLConnection连接
	    con.disconnect();
	}
}
