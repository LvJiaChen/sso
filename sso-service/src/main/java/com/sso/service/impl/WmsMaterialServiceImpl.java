package com.sso.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.common.entity.WmsMaterial;
import com.sso.common.mapper.WmsMaterialMapper;
import com.sso.service.IWmsMaterialService;
import com.sso.service.IWmsSerialNumberService;
import com.sso.service.redis.CacheListCallback;
import com.sso.service.redis.CacheObjectCallback;
import com.sso.service.redis.CacheTemplate;
import com.drean.dubbo.service.DubboTestService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-16
 */
@Service
public class WmsMaterialServiceImpl extends ServiceImpl<WmsMaterialMapper, WmsMaterial> implements IWmsMaterialService {
    @DubboReference
    private DubboTestService dubboTestService;
    @Autowired
    private WmsMaterialMapper materialMapper;
    @Autowired
    private IWmsSerialNumberService iWmsSerialNumberService;
    @Autowired
    private CacheTemplate cacheTemplate;

    @Override
    public IPage<WmsMaterial> queryMaterialList(Map param) {
        QueryWrapper<WmsMaterial> queryWrapper=new QueryWrapper<>();
        if (!StrUtil.isBlank((String)param.get("materialNo")))
            queryWrapper.like("material_no",param.get("materialNo"));
        if (!StrUtil.isBlank((String)param.get("materialName")))
            queryWrapper.like("material_name",param.get("materialName"));
        Page<WmsMaterial> materialPage = new Page<>((Integer)param.get("pageIndex") , (Integer)param.get("pageSize") , false);
        materialPage.setSearchCount(true);
        IPage<WmsMaterial> materialIPage =  materialMapper.selectPage(materialPage,queryWrapper);
        return materialIPage;
    }

    @Override
    public void deleteMaterial(Map param) {
        materialMapper.deleteById((Integer)param.get("id"));
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void saveMaterial(Map param) {
        WmsMaterial material=BeanUtil.toBean(param,WmsMaterial.class, CopyOptions.create());
        if (material.getId()==null){
            String materialNo= iWmsSerialNumberService.generateSerialNumberByModelCode("wms_material");
            material.setMaterialNo(materialNo);
            //添加
            materialMapper.insert(material);
        }else {
            //编辑
            materialMapper.updateById(material);
        }
    }

    @Override
    public List<WmsMaterial> selectMaterial(Map param) throws Exception {
        List<WmsMaterial> materials= dubboTestService.queryMaterialByMaterialNo("M000008-20220125");
        materials.get(0).setMaterialName("cunsumer-dubbo");
        materialMapper.updateById(materials.get(0));
        if (true) {
            throw new Exception("回滚");
        }
        List<WmsMaterial> materialList= cacheTemplate.listCacheWrapper("测试", WmsMaterial.class, new CacheListCallback() {
            @Override
            public List<WmsMaterial> getLatestValues() {
                QueryWrapper<WmsMaterial> queryWrapper=new QueryWrapper<>();
                queryWrapper.like("material_name","测试");
                List<WmsMaterial> materialList=materialMapper.selectList(queryWrapper);
                return materialList;
            }
        });

        WmsMaterial material= cacheTemplate.objectCacheWrapper("M000007-20220124", WmsMaterial.class, new CacheObjectCallback() {
            @Override
            public WmsMaterial getLatestValue() {
                QueryWrapper<WmsMaterial> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("material_no","M000007-20220124");
                WmsMaterial material=materialMapper.selectOne(queryWrapper);
                return material;
            }
        });
        return Arrays.asList(material);
    }
}
