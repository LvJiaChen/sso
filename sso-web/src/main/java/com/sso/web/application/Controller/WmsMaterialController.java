package com.sso.web.application.Controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.common.entity.WmsMaterial;
import com.sso.service.IWmsMaterialService;
import com.sso.web.application.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-16
 */
@Controller
@RequestMapping("/wms-material")
@ResponseBody
public class WmsMaterialController {

    @Autowired
    private IWmsMaterialService iWmsMaterialService;

    /**
     * 查询物料表数据
     *
     * @param
     * @return
     */
    @PostMapping("/queryMaterialList")
    public Result queryMaterialList(@RequestBody Map param) {
        IPage<WmsMaterial> materialIPage= iWmsMaterialService.queryMaterialList(param);
        return Result.ok(materialIPage);
    }

    /**
     * 保存物料
     *
     * @param
     * @return
     */
    @PostMapping("/saveMaterial")
    public Result saveMaterial(@RequestBody Map param) {
        iWmsMaterialService.saveMaterial(param);
        return Result.ok();
    }

    /**
     * 删除物料
     *
     * @param
     * @return
     */
    @PostMapping("/deleteMaterial")
    public Result deleteMaterial(@RequestBody Map param) {
        iWmsMaterialService.deleteMaterial(param);
        return Result.ok();
    }

    /**
     * 查询物料表信息（下拉选择）
     *
     * @param
     * @return
     */
    @PostMapping("/selectMaterial")
    public Result selectMaterial(@RequestBody Map param) throws Exception {
        List<WmsMaterial> materials= iWmsMaterialService.selectMaterial(param);
        return Result.ok(materials);
    }
}
