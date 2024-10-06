package com.r.designpattern.factory;

public class KfcHamburger extends Hamburger{
    @Override
    public void make() {
        //烤面包坯
        System.out.println("kfc 烤面包坯");
        //加生菜
        System.out.println("kfc 加生菜");
        //加番茄
        System.out.println("kfc 加番茄");
        //烤肉饼
        System.out.println("kfc 烤肉饼");
        //面包夹其他
        System.out.println("kfc 面包夹其他");
        //装KFC盒子中
        System.out.println("kfc 装KFC盒子中");
    }
}
