package com.drean.dubbo.service;

import com.sso.common.entity.WmsMaterial;

import java.util.List;

public interface DubboTestService {
    List<WmsMaterial> queryMaterialByMaterialNo(String MaterialNo);
}
