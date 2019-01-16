package com.shengzhe.disan.xuetangparent.bean;

/**
 * 参数名	类型	示例/默认值	说明
 campusDiscountId	long		折扣id
 campusDiscountPercent	long		折扣值
 type	int		优惠类型 0：课程优惠 1：校区优惠 2:调价优惠 3:线下优惠
 discountType	int		课程折扣类型 0打折 1满送 2抵现
 * Created by acer on 2018/1/2.
 */


public class OrderDiscount {
    private long campusDiscountId;

    public long getCampusDiscountId() {
        return campusDiscountId;
    }

    public void setCampusDiscountId(long campusDiscountId) {
        this.campusDiscountId = campusDiscountId;
    }

    public long getCampusDiscountPercent() {
        return campusDiscountPercent;
    }

    public void setCampusDiscountPercent(long campusDiscountPercent) {
        this.campusDiscountPercent = campusDiscountPercent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    private long campusDiscountPercent;
    private int type;
    private int discountType;

}
