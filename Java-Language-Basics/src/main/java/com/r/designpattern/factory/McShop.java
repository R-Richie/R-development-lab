package com.r.designpattern.factory;

public class McShop extends FastFoodShop{
    @Override
    public void makeChickenBurger() {
        McBurger mcBurger = new McBurger();
        mcBurger.makeChickenLegBurger();
    }

    @Override
    public void makeCola() {
        AbstractDrinks mcBurger = new McDrinks();
        mcBurger.makeCola();
    }
}
