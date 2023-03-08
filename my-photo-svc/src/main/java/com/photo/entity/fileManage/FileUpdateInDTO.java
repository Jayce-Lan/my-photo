package com.photo.entity.fileManage;

import lombok.Data;

@Data
public class FileUpdateInDTO {
    /**
     * 文件主键
     */
    private String fileId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件所在省份
     */
    private String admdvsProv;
    /**
     * 文件所在市
     */
    private String admdvsCity;
    /**
     * 用于区分省市
     */
    private String admdvsPt;
    /**
     * 删除标识
     */
    private String fileDeleteFlag;
    /**
     * 删除时间
     */
    private Integer deleteDay;
}
