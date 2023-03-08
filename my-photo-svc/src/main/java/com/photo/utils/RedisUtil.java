package com.photo.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Redis工具类
 */
@Repository
public class RedisUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 以 2022年1月1日作为开始时间戳(生成id使用)
     */
    private static final Long BEGIN_TIMESTAMP = 1640995200L;
    /**
     * 位运算常量（生成id使用）
     */
    private static final int COUNT_BITS = 32;

    /**
     * 查询一个JSON对象，并且函数无入参
     * @param queryFunction 查询方法
     * @param redisKey redis中的键
     * @param rClass 返回值类型
     * @param <R> 返回类型
     * @return 返回一个json对象
     */
    public <R> R queryToJSON(Supplier<R> queryFunction, String redisKey, Class<R> rClass) {
        // 查询Redis
        String jsonStr = stringRedisTemplate.opsForValue().get(redisKey);
        R r;
        if (StringUtils.hasText(jsonStr)) {
            // 如果存在，直接返回存储在redis中的对象
            r = JSONObject.parseObject(jsonStr, rClass);
        } else {
            // 如果不存在，则查询数据库，并写入Redi，写入时间为12小时
            r = queryFunction.get();
            if (r != null) {
                stringRedisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(r), 12, TimeUnit.HOURS);
            }
        }
        return r;
    }

    /**
     * 将查出的list作为字符串存储
     * @param queryFuntion 查询方法
     * @param redisKey 主键
     * @param rClass 返回类型
     * @param <R> 数据类型
     * @return 返回转为对象的list
     */
    public <R>List<R> queryArrayToJSON(Supplier<List<R>> queryFuntion, String redisKey, Class<R> rClass) {
        String jsonStr = stringRedisTemplate.opsForValue().get(redisKey);
        List<R> rList;
        if (StringUtils.hasText(jsonStr)) {
            rList = JSONObject.parseArray(jsonStr, rClass);
        } else {
            rList = queryFuntion.get();
            if (rList.size() > 0) {
                stringRedisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(rList), 12, TimeUnit.HOURS);
            }
        }
        return rList;
    }

    /**
     * 生成全局id
     * @param keyPrefix id名称
     * @return 返回id结果
     */
    public String nextId(String keyPrefix) {
        // 生成时间戳(当前时间)
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        // 生成序列号
        //获取当前日期
        String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 后缀添加当前日期后，使得Redis不会超出自增的上限
        long count = stringRedisTemplate.opsForValue().increment(DictConst.INCR_ID_KEY + keyPrefix + ":" + yyyyMMdd);
        // 拼接并返回
        return String.valueOf(timestamp << COUNT_BITS | count);
    }
}
