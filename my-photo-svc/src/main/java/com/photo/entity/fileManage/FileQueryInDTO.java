package com.photo.entity.fileManage;

import lombok.Data;

import java.util.List;

/**
 * 查询入参
 */
@Data
public class FileQueryInDTO {
    /**
     * 所在省市
     */
    private String admdvsPt;
    /**
     * 所在省
     */
    private String admdvsProvPt;
    /**
     * 承接时间
     */
    private List<String> selectDate;
    /**
     * 创建开始时间
     */
    private String startDate;
    /**
     * 创建结束时间
     */
    private String endDate;
    /**
     * 文件标签
     */
    private String fileTar;
    /**
     * 文件是否删除标识
     */
    private String fileDeleteFlag;
    /**
     * 页数
     */
    private Integer pageNum;
    /**
     * 每页长度
     */
    private Integer pageSize;
}
