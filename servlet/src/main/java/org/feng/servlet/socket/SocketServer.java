package org.feng.servlet.socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("all")
public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(8080);
        serverSocket.setReuseAddress(true);
        while (true) {
            final Socket accept = serverSocket.accept();
            new InputStreamHandler(new BufferedInputStream(accept.getInputStream())).start();

        }
    }
}

@SuppressWarnings("all")
class InputStreamHandler extends Thread{

    BufferedInputStream inputStream;
    public InputStreamHandler(BufferedInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run()
    {
        try {
            int i = inputStream.available();
            int offset = 0;
            final byte[] bytes = new byte[99999];
            while (i > 0) {
                inputStream.read(bytes, offset, i);
                offset += i;
                i = inputStream.available();
            }
            inputStream.close();
            System.out.println("Done===========================================");
            System.out.println(new String(bytes));
        } catch (Exception e) {

        }

    }
}