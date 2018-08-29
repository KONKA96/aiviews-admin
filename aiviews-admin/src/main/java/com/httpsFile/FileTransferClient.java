package com.httpsFile;

import java.io.DataOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.net.Socket;  
  
/** 
 * 文件传输Client端<br> 
 * 功能说明： 
 * 
 * @author 大智若愚的小懂 
 * @Date 2016年09月01日 
 * @version 1.0 
 */  
public class FileTransferClient extends Socket {  
  
    private static final String SERVER_IP = "47.93.189.169"; // 服务端IP  
    private static final int SERVER_PORT = 16384; // 服务端端口  
  
    private Socket client;  
  
    private FileInputStream fis;  
  
    private DataOutputStream dos;  
  
    /** 
     * 构造函数<br/> 
     * 与服务器建立连接 
     * @throws Exception 
     */  
    public FileTransferClient() throws Exception {  
        super(SERVER_IP, SERVER_PORT);  
        this.client = this;  
        System.out.println("Cliect[port:" + client.getLocalPort() + "] 成功连接服务端");  
    }  
  
    /** 
     * 向服务端传输文件 
     * @throws Exception 
     */  
    public void sendFile(File file) throws Exception {  
        try {  
           // File file = new File("E:\\JDK1.6中文参考手册(JDK_API_1_6_zh_CN).CHM");  
            if(file.exists()) {  
                fis = new FileInputStream(file);  
                dos = new DataOutputStream(client.getOutputStream());  
  
                // 文件名和长度  
                dos.writeUTF(file.getName());  
                dos.flush();  
                dos.writeLong(file.length());  
                dos.flush();  
  
                // 开始传输文件  
                System.out.println("======== 开始传输文件 ========");  
                byte[] bytes = new byte[1024];  
                int length = 0;  
                long progress = 0;  
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {  
                    dos.write(bytes, 0, length);  
                    dos.flush();  
                    progress += length;  
                    System.out.print("| " + (100*progress/file.length()) + "% |");  
                }  
                System.out.println();  
                System.out.println("======== 文件传输成功 ========");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if(fis != null)  
                fis.close();  
            if(dos != null)  
                dos.close();  
            client.close();  
        }  
    }  
  
}  
