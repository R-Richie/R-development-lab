package com.r.designpattern.factory;

public class McHamburger extends Hamburger{
    @Override
    public void make() {
        //烤面包坯
        System.out.println("mc 烤面包坯");
        //加生菜
        System.out.println("mc 加生菜");
        //加番茄
        System.out.println("mc 加番茄");
        //烤肉饼
        System.out.println("mc 烤肉饼");
        //面包夹其他
        System.out.println("mc 面包夹其他");
        //装麦当劳盒子中
        System.out.println("mc 装麦当劳盒子中");
    }
}
