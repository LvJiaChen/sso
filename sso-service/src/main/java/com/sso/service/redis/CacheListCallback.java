package com.sso.service.redis;

import java.util.List;

/**
 * @author lvxiaozuo
 * @date 2022/1/25 14:30
 */
public interface CacheListCallback {
    <T> List<T> getLatestValues();
}
