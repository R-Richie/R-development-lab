package com.R;

import com.R.io.Reactor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("start world!");
        Reactor reactor = new Reactor(9999);
        Thread thread = new Thread(reactor);
        thread.start();


        System.out.println("Hello world!");
    }
}