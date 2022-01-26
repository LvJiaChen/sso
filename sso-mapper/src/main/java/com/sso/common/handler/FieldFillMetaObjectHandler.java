package com.sso.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sso.common.vo.UserInfo;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author lvxiaozuo
 * @date 2022/1/16 14:20
 */
@Component
public class FieldFillMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.setFieldValByName("creator", UserInfo.getUser().getUserNo(), metaObject);
        this.setFieldValByName("version",1,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updater", UserInfo.getUser().getUserNo(), metaObject);
    }
}
