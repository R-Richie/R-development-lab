package com.r.designpattern.factory;

public class ConcreteCreator extends Creator{
    @Override
    public <T extends Product> T createProduct(Class<T> c) {
        Product product = null;
        try{
            product = (Product)Class.forName(c.getName()).newInstance();

        }catch (Exception e){

        }
        return (T)product;
    }
}
