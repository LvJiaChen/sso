package com.sso.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lvxiaozuo
 * @date 2022/1/6 15:56
 */
@Data
public class TokenVO {
    private String token;
    private LocalDateTime expireTime;
}
