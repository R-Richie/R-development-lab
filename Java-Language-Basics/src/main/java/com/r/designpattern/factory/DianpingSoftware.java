package com.r.designpattern.factory;

public class DianpingSoftware extends Index {
    @Override
    public <T extends Hamburger> T findFood(Class<T> c) {
        Hamburger food = null;
        try{
            food = (Hamburger)Class.forName(c.getName()).newInstance();

        }catch (Exception e){

        }
        return (T)food;
    }
}
