package com.r.integration.unit.parameterized;

public class Book {
    private final String title;

    public Book(String title) {
        this.title = title;
    }
    public static Book formTitle(String title){
        return new Book(title);
    }
    public String getTitle(){
        return this.title;
    }
}
