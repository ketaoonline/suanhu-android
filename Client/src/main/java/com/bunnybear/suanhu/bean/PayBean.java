package com.bunnybear.suanhu.bean;

import com.bunnybear.suanhu.util.pay.WXPayInfo;

public class PayBean {

    WXPayInfo wxpay_info;
    String alipay_info;

    public WXPayInfo getWxpay_info() {
        return wxpay_info;
    }

    public void setWxpay_info(WXPayInfo wxpay_info) {
        this.wxpay_info = wxpay_info;
    }

    public String getAlipay_info() {
        return alipay_info;
    }

    public void setAlipay_info(String alipay_info) {
        this.alipay_info = alipay_info;
    }
}
