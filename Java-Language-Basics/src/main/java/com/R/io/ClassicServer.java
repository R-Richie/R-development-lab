package com.R.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClassicServer implements Runnable{
    int PORT = 10;
    static int MAX_INPUT = 1024;
    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            while (!Thread.interrupted()) {

            }
        }catch (IOException ex){

        }

    }
    static class ClassicHandler implements Runnable{
        final Socket socket;

        ClassicHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                byte[] input = new byte[MAX_INPUT];
                socket.getInputStream().read(input);
                byte[] output = process(input);
                socket.getOutputStream().write(output);
            }catch (IOException ex){

            }
        }
        private byte[] process(byte[] cmd){
            return null;
        }
    }
}

