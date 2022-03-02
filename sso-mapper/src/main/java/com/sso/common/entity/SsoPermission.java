package com.sso.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-03-02
 */
@Getter
@Setter
@TableName("sso_permission")
@ApiModel(value = "SsoPermission对象", description = "")
public class SsoPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("菜单编码")
    private String code;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("上级菜单编码")
    private String pCode;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("权限值")
    private String permissionValue;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("前端路径")
    private String component;

    @ApiModelProperty("状态")
    private String status;

    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updater;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


}
