package com.photo.entity;

import lombok.Data;

@Data
public class PageParamDTO {
    /**
     * 页数
     */
    private Integer pageNum;
    /**
     * 每页长度
     */
    private Integer pageSize;
}
