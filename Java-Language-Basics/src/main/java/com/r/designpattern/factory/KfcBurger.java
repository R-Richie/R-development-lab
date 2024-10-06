package com.r.designpattern.factory;

public class KfcBurger extends AbstractHamburger{
    @Override
    public void makeChickenLegBurger() {
        System.out.println("kfc 鸡腿堡");
    }

    @Override
    public void makeCodburger() {
        System.out.println("kfc 鱼堡");
    }
}
