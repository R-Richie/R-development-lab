package com.r.designpattern.factory;

public class KfcDrinks extends AbstractDrinks{
    @Override
    public void makeCola() {
        System.out.println("kfc cola");
    }

    @Override
    public void makeSprite() {
        System.out.println("kfc sprite");
    }
}
