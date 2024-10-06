package com.r.designpattern.factory;

public abstract class Index {
    public abstract <T extends Hamburger> T findFood(Class<T> c);
}
