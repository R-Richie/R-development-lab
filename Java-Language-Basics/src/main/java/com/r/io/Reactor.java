package com.r.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable{
    final Selector selector;
    final ServerSocketChannel serverSocket;

    public Reactor(int port) throws IOException{
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while(it.hasNext())
                    dispatch((SelectionKey) it.next());
                selected.clear();

            }
        }catch (IOException ex){

        }
    }
    void dispatch(SelectionKey k){
        Runnable r = (Runnable) k.attachment();
        if(r != null){
            r.run();
        }
    }
    class Acceptor implements Runnable{

        @Override
        public void run() {
            try{
                SocketChannel c = serverSocket.accept();
                if(c!= null){
                    new Handler(selector, c);
                }
            }catch (IOException ex){

            }
        }
    }
    final class Handler implements Runnable{
        int MAXIN = 0;
        int MAXOUT = 1000;
        final SocketChannel socket;
        final SelectionKey sk;
        ByteBuffer input = ByteBuffer.allocate(MAXIN);
        ByteBuffer output = ByteBuffer.allocate(MAXOUT);
        static final int READING = 0, SENDING = 1;
        int state = READING;

        public Handler(Selector sel, SocketChannel c) throws IOException{
            this.socket = c;
            c.configureBlocking(false);
            sk = socket.register(sel, 0);
            sk.attach(this);
            sk.interestOps(SelectionKey.OP_READ);
            sel.wakeup();
        }
        boolean inputIsComplete(){return true;}
        boolean outputIsComplete(){return true;}
        void process(){}
        @Override
        public void run() {
            try{
                if(state == READING) read();
                else if(state == SENDING)send();
            }catch (IOException ex){

            }

        }
        void read() throws IOException{
            socket.read(input);
            if(inputIsComplete()){
                process();
                state = SENDING;
                sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        void send() throws IOException {
            socket.write(output);
            if(outputIsComplete())sk.cancel();
        }
    }
}
