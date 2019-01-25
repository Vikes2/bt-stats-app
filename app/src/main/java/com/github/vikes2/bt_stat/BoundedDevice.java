package com.github.vikes2.bt_stat;

public class BoundedDevice {
    private String name;
    private String mac;

    public BoundedDevice(){

    }

    public BoundedDevice(String name){
        this.name = name;
    }
    public BoundedDevice(String name, String mac){
        this.name = name; this.mac = mac;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

}
