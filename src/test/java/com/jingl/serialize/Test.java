package com.jingl.serialize;

import com.alibaba.fastjson.JSON;
import com.jingl.proxy.Data;

import java.io.Serializable;

/**
 * Created by Ben on 14/12/2017.
 */
public class Test {
    public static void main(String[] args) {
        D2 d2 = new Test().new D2();
        System.out.println(JSON.toJSONString(d2));

    }

    private class D2 implements Serializable {
        private Data data = new Data();
        String name = "1";

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
