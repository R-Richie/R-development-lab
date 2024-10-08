package com.r.designpattern.factory;

public abstract class Creator {
    public abstract <T extends Product> T createProduct(Class<T> c);
}
