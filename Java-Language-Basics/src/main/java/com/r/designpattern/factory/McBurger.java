package com.r.designpattern.factory;

public class McBurger extends AbstractHamburger{
    @Override
    public void makeChickenLegBurger() {
        System.out.println("mc 鸡腿堡");
    }

    @Override
    public void makeCodburger() {
        System.out.println("mc 鱼堡");
    }
}
