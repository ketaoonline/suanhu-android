package com.bunnybear.suanhu.master.bean;

import java.util.List;

public class IncomeResponse extends MainBaseBean{
    double income;
    String bank_number;
    List<Income> info;

    public String getBank_number() {
        return bank_number;
    }

    public void setBank_number(String bank_number) {
        this.bank_number = bank_number;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public List<Income> getInfo() {
        return info;
    }

    public void setInfo(List<Income> info) {
        this.info = info;
    }
}
