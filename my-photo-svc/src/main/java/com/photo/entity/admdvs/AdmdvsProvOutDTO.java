package com.photo.entity.admdvs;

import lombok.Data;

import java.util.List;

@Data
public class AdmdvsProvOutDTO {
    /**
     * 拼音
     */
    private String value;
    /**
     * 中文
     */
    private String label;
    /**
     * 存储子集
     */
    private List<AdmdvsNameOutDTO> children;
}
