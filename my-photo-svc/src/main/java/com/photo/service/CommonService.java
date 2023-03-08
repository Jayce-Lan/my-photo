package com.photo.service;

import com.photo.entity.admdvs.AdmdvsInDTO;
import com.photo.entity.admdvs.AdmdvsOutDTO;
import com.photo.entity.admdvs.AdmdvsProvOutDTO;

import java.util.List;

/**
 * 公共方法业务层
 */
public interface CommonService {
    List<AdmdvsProvOutDTO> queryAdmdvsProvList();
    void addAdmdvs(AdmdvsInDTO admdvs);
    List<AdmdvsProvOutDTO> queryAdmdvsOnlyProvList();
    AdmdvsOutDTO queryAdmdvsProvInfo(AdmdvsInDTO admdvs);
}
