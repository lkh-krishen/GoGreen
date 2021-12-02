package com.pulps.gogreen.Model;

public class Item {

    public String item;
    public double price;
    public String bank;
    public int account;
    public String address;
    public String email;
    public int mobile;
    public String status;
    public int weight;
    public double total;

    public Item(){ }

    public String getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public String getBank() {
        return bank;
    }

    public int getAccount() {
        return account;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public int getMobile() {
        return mobile;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public void setStatus(String status) {this.status = status; }

    public String getStatus() {return status; }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

