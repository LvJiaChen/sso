package com.sso.service;

import com.sso.common.entity.SsoSerialNumber;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-23
 */
public interface ISsoSerialNumberService extends IService<SsoSerialNumber> {
    public String generateSerialNumberByModelCode(String moduleCode);

    /**
     * 设置最小值
     * @param value 最小值，要求：大于等于零
     * @return      流水号生成器实例
     */
    ISsoSerialNumberService setMin(int value);

    /**
     * 设置最大值
     * @param value 最大值，要求：小于等于Long.MAX_VALUE ( 9223372036854775807 )
     * @return      流水号生成器实例
     */
    ISsoSerialNumberService setMax(long value);

    /**
     * 设置预生成流水号数量
     * @param count 预生成数量
     * @return      流水号生成器实例
     */
    ISsoSerialNumberService setPrepare(int count);
}
