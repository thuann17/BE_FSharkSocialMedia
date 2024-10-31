package com.system.fsharksocialmedia.models;

import lombok.Data;

@Data
public class PlaceModel {
    private Integer id;           // ID - Khóa chính
    private String nameplace;     // NAMEPLACE - Tên địa điểm
    private String urlmap;        // URLMAP - Liên kết bản đồ
    private String address;       // ADDRESS - Địa chỉ
    private String description;   // DESCRIPTION - Mô tả
}
