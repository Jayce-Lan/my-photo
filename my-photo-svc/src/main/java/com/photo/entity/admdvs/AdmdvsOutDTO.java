package com.photo.entity.admdvs;

import lombok.Data;

@Data
public class AdmdvsOutDTO {
    /**
     * 市级区划
     */
    private String admdvsName;
    /**
     * 市级区划拼音
     */
    private String admdvsNamePt;
    /**
     * 所属省级区划
     */
    private String admdvsProv;
    /**
     * 所属省级区划拼音
     */
    private String admdvsProvPt;
}
