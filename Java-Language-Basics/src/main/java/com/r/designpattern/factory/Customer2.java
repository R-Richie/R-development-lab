package com.r.designpattern.factory;

public class Customer2 {
    public static void main(String[] args) {
        FastFoodShop kfcShop = new KfcShop();
        FastFoodShop mcShop = new McShop();

        kfcShop.makeChickenBurger();
        kfcShop.makeCola();

        mcShop.makeChickenBurger();
        mcShop.makeCola();
    }
}
