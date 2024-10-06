package com.r.designpattern.factory;

public class McDrinks extends AbstractDrinks{
    @Override
    public void makeCola() {
        System.out.println("mc cola");
    }

    @Override
    public void makeSprite() {
        System.out.println("mc sprite");
    }
}
