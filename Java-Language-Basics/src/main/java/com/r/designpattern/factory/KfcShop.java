package com.r.designpattern.factory;

public class KfcShop extends FastFoodShop{
    @Override
    public void makeChickenBurger() {
        AbstractHamburger kfcHamburger = new KfcBurger();
        kfcHamburger.makeChickenLegBurger();
    }

    @Override
    public void makeCola() {
        AbstractDrinks kfcHamburger = new KfcDrinks();
        kfcHamburger.makeCola();
    }
}
