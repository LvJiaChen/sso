package com.sso.service.redis;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lvxiaozuo
 * @date 2022/1/25 11:31
 */
@Service
public class RedisService {

    @Autowired
    private RedisUtils redisUtils;

    public <T> T getHashToBean(String key,Class<T> classzz) throws Exception {
        T returnResult =null;
        if (redisUtils.exists(key)){
            returnResult = classzz.newInstance();

            Field[] fields= classzz.getDeclaredFields();

            Set<Object> hashKeys= redisUtils.hmKeys(key);
            if (hashKeys.size()>0){
                for (Object o:hashKeys){
                    if (o instanceof String){
                        String hashKey=(String)o;
                        Object value= redisUtils.hmGet(key,hashKey);
                        for (Field field:fields){
                            if (field.getName().equals(hashKey)){
                                if (field.getType().equals(String.class)){
                                    field.setAccessible(true);
                                    field.set(returnResult,String.valueOf(value));
                                    break;
                                }else if (field.getType().equals(BigDecimal.class)){
                                    field.setAccessible(true);
                                    if (value instanceof Number){
                                        field.set(returnResult,NumberUtil.toBigDecimal((Number) value));
                                    }
                                    if (value instanceof String){
                                        field.set(returnResult,NumberUtil.toBigDecimal((String) value));
                                    }
                                    break;
                                }else if (field.getType().equals(LocalDateTime.class)){
                                    field.setAccessible(true);
                                    field.set(returnResult, LocalDateTimeUtil.parse(String.valueOf(value)));
                                    break;
                                }else if (field.getType().equals(Date.class)){
                                    field.setAccessible(true);
                                    field.set(returnResult, DateUtil.parse(String.valueOf(value)));
                                    break;
                                } else if (field.getType().equals(Long.class)){
                                    field.setAccessible(true);
                                    field.set(returnResult,Long.valueOf(String.valueOf(value)));
                                }else if (field.getType().equals(Integer.class)){
                                    field.setAccessible(true);
                                    field.set(returnResult,Integer.valueOf(String.valueOf(value)));
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnResult;
    }

    public <T> boolean setBeanToHash(String key,T value) throws Exception {
        Field[] fields= value.getClass().getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            if (field.get(value)!=null){
                redisUtils.hmSet(key,field.getName(), StrUtil.toString(field.get(value)));
            }
        }
        return true;
    }

    public <T> boolean setList(String key, List<T> returnResult) {
        returnResult.forEach(a->{
            redisUtils.lPush(key, JSONUtil.toJsonStr(a));
        });
        return true;
    }

    public <T> List<T> getList(String key, Class<T> clazz) throws Exception {
        List<T> returnResult=new ArrayList<>();
        if (redisUtils.exists(key)){
            T bean=clazz.newInstance();
            Object len=redisUtils.lSize(key);
            List object=(List)redisUtils.lRange(key,0,Integer.parseInt(String.valueOf(len)));
            for (Object beanJson: object){
                bean=JSONUtil.toBean((String) beanJson,clazz);
                returnResult.add(bean);
            }
        }
        return returnResult;
    }
}
