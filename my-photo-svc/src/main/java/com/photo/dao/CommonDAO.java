package com.photo.dao;

import com.photo.entity.admdvs.AdmdvsInDTO;
import com.photo.entity.admdvs.AdmdvsNameOutDTO;
import com.photo.entity.admdvs.AdmdvsOutDTO;
import com.photo.entity.admdvs.AdmdvsProvOutDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 公共方法
 */
@Mapper
public interface CommonDAO {
    /**
     * 查询省级区划
     * @return
     */
    List<AdmdvsProvOutDTO> queryAdmdvsProvList();

    /**
     * 根据省级区划查询市级区划
     * @param admdvsProvPT
     * @return
     */
    List<AdmdvsNameOutDTO> queryAdmdvsNameListByAdmdvsProvPT(String admdvsProvPT);

    /**
     * 增加区划
     * @param admdvs
     * @return
     */
    int addAdmdvs(AdmdvsInDTO admdvs);

    /**
     * 查询区划是否存在，用于判定是否新增
     * @param admdvs
     * @return
     */
    int queryAdmdvsCount(AdmdvsInDTO admdvs);

    /**
     * 根据传入的信息查询省级区划
     * @param admdvs
     * @return
     */
    AdmdvsOutDTO queryAdmdvsProvInfo(AdmdvsInDTO admdvs);
}
