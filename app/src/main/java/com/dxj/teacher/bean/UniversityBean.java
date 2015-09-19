package com.dxj.teacher.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片实体
 */
public class UniversityBean  implements Serializable {
//    "id": 449,
//            "name": "清华大学",
//            "province": "北京",
//            "city": "北京市",
//            "type": null,
//            "remark": "985/212工程",
//            "price": null

        private Long id; //用户id
        private String name;
        private String city;//城市
        private String type;//
        private String remark;//类别
        private String price;//
        private int cityId;//

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getRemark() {
        return remark;
    }

    public String getType() {
        return type;
    }
}
