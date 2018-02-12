/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitaryTests;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import com.google.protobuf.*;
import java.io.InputStream;
import java.io.BufferedOutputStream;

/**
 *
 * @author yoann
 */
public class MPUReceiver {
    

    public static void main(String args[]) {

        String host = "127.0.0.1";
        int port = 8000;

        People people = People.newBuilder()
                .setName("deli")
                .setId(2)
                .setEmail("lytsing@hotmail.com").build();

        try {
            Socket client = new Socket(host, port);


            // Send message to Server
            byte[] result = people.toByteArray() ;
            client.getOutputStream().write(result); 

            // Receive message from Server
            InputStream input = client.getInputStream();

            byte[] msg = recvMsg(input);
            People p = People.parseFrom(msg);

            System.out.println(p.getName());
            System.out.println(p.getId());
            System.out.println(p.getEmail());

            input.close();
            client.close();
        } catch (UnknownHostException e){
            System.out.println("UnknownHostException:" + e.toString());
        } catch (java.io.IOException e) {
            System.out.println("IOException :" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Receive mssage from Server
     * 
     * @return
     */
    public static byte[] recvMsg(InputStream inpustream) {
        try {

            byte len[] = new byte[1024];
            int count = inpustream.read(len); 

            byte[] temp = new byte[count];
            for (int i = 0; i < count; i++) { 
                temp[i] = len[i]; 
            } 
            return temp;
        } catch (Exception e) {
            System.out.println("recvMsg() occur exception!" + e.toString());
        }
        return null;
    }

}
