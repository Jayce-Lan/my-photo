package com.photo.entity.fileManage;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FileOutDTO {
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
     * 文件大小
     */
    private BigDecimal fileSize;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 上传修改人员
     */
    private String userName;
    /**
     * 回收站内剩余时间，刚删除时为30，依靠定时任务每天减少1天
     */
    private String fileDeleteFlag;
    /**
     * 文件标签用逗号隔开
     */
    private String fileTar;
    /**
     * 文件说明
     */
    private String fileRemarks;
    /**
     * 回收站剩余时间
     */
    private Integer deleteDay;
    /**
     * 数据修改时间
     */
    private String updateTime;
    /**
     * 数据创建时间
     */
    private String createTime;
    /**
     * 备用字段1
     */
    private String reserved1;
    /**
     * 备用字段2
     */
    private String reserved2;
    /**
     * 备用字段3
     */
    private String reserved3;
    /**
     * 备用字段4
     */
    private BigDecimal reserved4;
    /**
     * 备用字段5
     */
    private BigDecimal reserved5;
    /**
     * 备用字段6
     */
    private BigDecimal reserved6;

}
