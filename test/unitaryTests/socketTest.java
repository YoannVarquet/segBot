/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unitaryTests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 *
 * @author yoann
 */
public class socketTest {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        serverSocket = new ServerSocket(4447); // 4447 is port number
        clientSocket = serverSocket.accept(); // blocks and listen until a connection is made.
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
    }

}
