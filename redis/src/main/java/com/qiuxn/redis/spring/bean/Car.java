package com.qiuxn.redis.spring.bean;

/*
 * @autor: qiuxn19225
 * @date: 2018/1/28
 * @desc:
 */
public class Car {

    private int speed;

    private String brand;

    private double price;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
