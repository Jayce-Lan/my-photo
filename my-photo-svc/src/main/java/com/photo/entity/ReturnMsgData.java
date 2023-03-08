package com.photo.entity;

import com.photo.utils.DictConst;
import lombok.Data;

/**
 * 标准化返回值
 * @param <T> 返回值的数据类型
 */
@Data
public class ReturnMsgData<T> {
    /**
     * 返回状态：
     * 200 成功
     */
    private String code;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 返回错误信息
     * 用于失败时使用
     */
    private String errorMsg;

    public ReturnMsgData() {

    }

    /**
     * 当数据错误时返回
     * @param code
     * @param errorMsg
     */
    public ReturnMsgData(String code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    /**
     * 当处理成功时，返回的参数
     * @param data
     */
    public ReturnMsgData(T data) {
        this.code = DictConst.CODE_SUCCESS;
        this.data = data;
    }
}
