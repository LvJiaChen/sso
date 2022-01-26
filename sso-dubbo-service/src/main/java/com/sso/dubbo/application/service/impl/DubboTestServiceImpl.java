package com.sso.dubbo.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sso.common.entity.WmsMaterial;
import com.sso.common.mapper.WmsMaterialMapper;
import com.drean.dubbo.service.DubboTestService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lvxiaozuo
 * @date 2022/1/26 16:59
 */
@DubboService
@Component
public class DubboTestServiceImpl implements DubboTestService {
    @Autowired
    private WmsMaterialMapper wmsMaterialMapper;

    @Override
    public List<WmsMaterial> queryMaterialByMaterialNo(String materialNo) {
        QueryWrapper<WmsMaterial> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("material_no",materialNo);
        List<WmsMaterial> materials=wmsMaterialMapper.selectList(queryWrapper);
        materials.get(0).setMaterialName("dubbo测试");
        wmsMaterialMapper.updateById(materials.get(0));
        return materials;
    }
}
