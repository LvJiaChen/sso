package com.sso.service.redis;

import java.util.List;

public interface CacheObjectCallback {
    <T> T getLatestValue();
}
