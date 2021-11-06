package com.qiuxn.redis.spring.bean;

import org.springframework.beans.factory.FactoryBean;

/*
 * @autor: qiuxn19225
 * @date: 2018/1/28
 * @desc:
 */
public class CarFactoryBean implements FactoryBean<Car> {

    private String carInfo;

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public Car getObject() throws Exception {

        String[] infoArr = carInfo.split(",");
        Car car = new Car();
        car.setBrand(infoArr[0]);
        car.setSpeed(Integer.parseInt(infoArr[1]));
        car.setPrice(Double.parseDouble(infoArr[2]));
        return car;
    }

    public Class<?> getObjectType() {
        return Car.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
