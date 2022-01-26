package com.sso.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.common.entity.WmsMaterial;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-16
 */
public interface IWmsMaterialService extends IService<WmsMaterial> {

    IPage<WmsMaterial> queryMaterialList(Map param);

    void deleteMaterial(Map param);

    void saveMaterial(Map param);

    List<WmsMaterial> selectMaterial(Map param) throws Exception;
}
