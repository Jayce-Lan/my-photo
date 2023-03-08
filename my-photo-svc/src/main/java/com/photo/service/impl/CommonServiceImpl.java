package com.photo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.photo.dao.CommonDAO;
import com.photo.entity.admdvs.AdmdvsInDTO;
import com.photo.entity.admdvs.AdmdvsNameOutDTO;
import com.photo.entity.admdvs.AdmdvsOutDTO;
import com.photo.entity.admdvs.AdmdvsProvOutDTO;
import com.photo.service.CommonService;
import com.photo.utils.AdmdvsToPTUtil;
import com.photo.utils.DictConst;
import com.photo.utils.RedisUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("commonService")
@Transactional
public class CommonServiceImpl implements CommonService {
    @Resource
    private CommonDAO commonDAO;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 区划查询：查询省+市的区划，做联级查询
     * 查出1级区划后根据其查出2级区划
     * 1级区划为省，标识是 省的拼音字段 = 市的拼音字段
     * 2级区划为市，标识是 省的拼音字段 != 市的拼音字段
     * @return
     */
    @Override
    public List<AdmdvsProvOutDTO> queryAdmdvsProvList() {
        String jsonStr = stringRedisTemplate.opsForValue().get(DictConst.ADMDVS_ALL_JSON);
        List<AdmdvsProvOutDTO> admdvsProvOutDTOS;
        if (StringUtils.hasText(jsonStr)) {
            admdvsProvOutDTOS = JSONObject.parseArray(jsonStr, AdmdvsProvOutDTO.class);
        } else {
            admdvsProvOutDTOS = commonDAO.queryAdmdvsProvList();
            for (AdmdvsProvOutDTO item : admdvsProvOutDTOS) {
                List<AdmdvsNameOutDTO> admdvsNameOutDTOS = commonDAO.queryAdmdvsNameListByAdmdvsProvPT(item.getValue());
                item.setChildren(admdvsNameOutDTOS);
            }
            if (admdvsProvOutDTOS.size() > 0) {
                stringRedisTemplate.opsForValue().set(DictConst.ADMDVS_ALL_JSON, JSONObject.toJSONString(admdvsProvOutDTOS),
                        12, TimeUnit.HOURS);
            }
        }
        return admdvsProvOutDTOS;
    }

    /**
     * 区划添加，添加成功写入持久层后，删除Redis中的缓存，让下次读取不再读取Redis
     * @param admdvs 区划信息
     */
    @Override
    public void addAdmdvs(AdmdvsInDTO admdvs) {
        // 用于区分是否经历了修改
        boolean changeFlag = false;
        String admdvsProvPt = AdmdvsToPTUtil.admdvsToPT(admdvs.getAdmdvsProv());
        admdvs.setAdmdvsProvPt(admdvsProvPt);
        // 判定省级区划是否存在于区划信息表中
        int checkAdmdvsProvCount = commonDAO.queryAdmdvsCount(admdvs);
        // 不存在，则直接写表
        if (checkAdmdvsProvCount == 0) {
            AdmdvsInDTO admdvsProv = new AdmdvsInDTO();
            admdvsProv.setAdmdvsName(admdvs.getAdmdvsProv());
            admdvsProv.setAdmdvsProv(admdvs.getAdmdvsProv());
            admdvsProv.setAdmdvsNamePt(admdvsProvPt);
            admdvsProv.setAdmdvsProvPt(admdvsProvPt);
            int addAdmdvsProvCount = commonDAO.addAdmdvs(admdvsProv);
            if (addAdmdvsProvCount != 1) {
                throw new RuntimeException("数据插入失败");
            }
            changeFlag = true;
        }
        // 当市级区划存在时，才进行下一步
        if (StringUtils.hasText(admdvs.getAdmdvsName())) {
            admdvs.setAdmdvsNamePt(AdmdvsToPTUtil.admdvsToPT(admdvs.getAdmdvsName()));
            int checkAdmdvsNameCount = commonDAO.queryAdmdvsCount(admdvs);
            if (checkAdmdvsNameCount == 0) {
                int addAdmdvsNameCount = commonDAO.addAdmdvs(admdvs);
                if (addAdmdvsNameCount != 1) {
                    throw new RuntimeException("数据插入失败");
                }
                changeFlag = true;
            }
        }
        if (changeFlag) {
            stringRedisTemplate.delete(DictConst.ADMDVS_ALL_JSON);
        }
    }

    /**
     * 只查询省级区划，由于区划拼音加了索引，因此可以使用"省级区划%"做查询
     * @return
     */
    @Override
    public List<AdmdvsProvOutDTO> queryAdmdvsOnlyProvList() {
        List<AdmdvsProvOutDTO> rList = redisUtil.queryArrayToJSON(() -> commonDAO.queryAdmdvsProvList(),
                DictConst.ADMDVS_ONLY_PROV, AdmdvsProvOutDTO.class);
        return rList;
    }

    @Override
    public AdmdvsOutDTO queryAdmdvsProvInfo(AdmdvsInDTO admdvs) {
        AdmdvsOutDTO admdvsOutDTO = redisUtil.queryToJSON(() -> commonDAO.queryAdmdvsProvInfo(admdvs),
                DictConst.ADMDVS_PROV_KEYS + admdvs, AdmdvsOutDTO.class);
        return admdvsOutDTO;
    }
}
