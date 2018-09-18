package com.bmn.rt.beans;

public class User {
    private String name;
    private int age;
    private int credits;

    public User(String name, int age, int credits) {
        this.name = name;
        this.age = age;
        this.credits = credits;
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

    @Override
    public String toString() {
        return name + "-" + age + "-" + credits;
    }
}
