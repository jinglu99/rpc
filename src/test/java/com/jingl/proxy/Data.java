package com.jingl.proxy;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Ben on 13/12/2017.
 */
public class Data implements Serializable {
    String name = "test";
    int age = 1;
    double d = 0.99;
    float f = 1.0f;
    short s = 0;
    byte b = 1;
    BigDecimal decimal = new BigDecimal(1.0);

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

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public short getS() {
        return s;
    }

    public void setS(short s) {
        this.s = s;
    }

    public byte getB() {
        return b;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }
}
