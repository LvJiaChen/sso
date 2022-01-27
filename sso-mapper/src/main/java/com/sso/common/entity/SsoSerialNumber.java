package com.sso.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
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
 * @since 2022-01-23
 */
@Getter
@Setter
@TableName("Sso_serial_number")
@ApiModel(value = "SsoSerialNumber对象", description = "")
public class SsoSerialNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("模块名称")
    private String moduleName;

    @ApiModelProperty("模块编码")
    private String moduleCode;

    @ApiModelProperty("当前模块 使用的序列号模板")
    private String configTemplet;

    @ApiModelProperty("存放当前序列号的值")
    private String maxSerial;

    @ApiModelProperty("存放当前序列号的值")
    private String preMaxNum;

    @ApiModelProperty("存放当前序列号的值")
    private String isAutoIncrement;

    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updater;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
