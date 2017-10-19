package com.heking.hzz.Model;

/**
 * Created by Administrator on 2017/10/18.
 */

public class Zhengfudaohang {

    /**
     * ID : 1
     * Name : 犍为县食药监局玉津监督管理所
     * Address : 圣泉路南段213国道交叉口东150米
     * Phone : null
     * Delete : false
     */

    private int ID;
    private String Name;
    private String Address;
    private Object Phone;
    private boolean Delete;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public Object getPhone() {
        return Phone;
    }

    public void setPhone(Object Phone) {
        this.Phone = Phone;
    }

    public boolean isDelete() {
        return Delete;
    }

    public void setDelete(boolean Delete) {
        this.Delete = Delete;
    }
}
