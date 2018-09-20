package com.bmn.rt.beans;

public class User {
    private String name;
    private int age;
    private int credits;
    private int image = 2;

    public User(String name, int age, int credits, int image) {
        this.name = name;
        this.age = age;
        this.credits = credits;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return name + "-" + age + "-" + credits + "-" + image;
    }
}
