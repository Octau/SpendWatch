package com.octavius.spendwatch.balanceflow;

import java.util.Date;

public class BalanceFlow{
    String desc;
    Integer id, balance;
    Date date;

    public BalanceFlow(Integer id, String desc, Integer balance, Date date){
        this.id = id;
        this.desc = desc;
        this.balance =balance;
        this.date = date;
    }
    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}