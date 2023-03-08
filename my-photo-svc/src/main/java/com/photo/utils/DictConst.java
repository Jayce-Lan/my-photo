package com.photo.utils;

/**
 * 存储常数的工具类
 */
public class DictConst {
    /**
     * 成功返回状态-200
     */
    public static final String CODE_SUCCESS = "200";


    /**
     * tar标签存储的Redis路径(实质是一个set集合)
     */
    public static final String TAR_SET_PATH = "file:tar";
    /**
     * 存储地理位置的主键
     */
    public static final String ADMDVS_ALL_JSON = "admdvs:admdvs_all";
    /**
     * 存储单个省区划
     */
    public static final String ADMDVS_PROV_KEYS = "admdvs:prov:";
    /**
     * 存储省的主键
     */
    public static final String ADMDVS_ONLY_PROV = "admdvs:admdvs_only_prov";
    /**
     * 存储Redis生成的ID的key地址
     */
    public static final String INCR_ID_KEY = "file:id:";
    /**
     * 存储文件id的key
     */
    public static final String FILE_ID_KEY = "file_id";
    /**
     * 存储文件名序列的key
     */
    public static final String FILE_NAME_KEY = "file_name";
    /**
     * 存储全部查询出来的文件的列
     */
    public static final String FILE_ALL_FILE_OUT_LIST = "file:file_all_list";


    /**
     * 文件前缀
     */
    public static final String FILE_INDEX_FIRST = "FILE_";
    /**
     * 文件是否在回收站中: 1，否，文件有效
     */
    public static final String FILE_DELETE_FLAG_VALID = "1";
    /**
     * 文件是否在回收站中: 0，是，文件无效
     */
    public static final String FILE_DELETE_FLAG_DELETE = "0";
}
