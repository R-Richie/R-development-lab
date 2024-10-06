package com.r.designpattern.factory;

public class CustomerA {
    public static void main(String[] args){
        Index customerA = new DianpingSoftware();
        Hamburger kfcShop = customerA.findFood(KfcHamburger.class);
        kfcShop.make();

        Hamburger mcShop = customerA.findFood(McHamburger.class);
        mcShop.make();
    }
}
