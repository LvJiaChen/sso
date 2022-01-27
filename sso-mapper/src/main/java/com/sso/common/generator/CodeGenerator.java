package com.sso.common.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lvxiaozuo
 * @date 2021/11/28 19:47
 */
public class CodeGenerator {

    public static void main(String[] args) {
        Map<OutputFile, String> pathInfo = new HashMap<>();
        pathInfo.put(OutputFile.controller, "E:\\IdeaProject\\sso\\sso-web\\src\\main\\java\\com\\sso\\web\\application\\Controller");
        pathInfo.put(OutputFile.mapper, "E:\\IdeaProject\\sso\\sso-mapper\\src\\main\\java\\com\\sso\\common\\mapper");
        pathInfo.put(OutputFile.serviceImpl, "E:\\IdeaProject\\sso\\sso-service\\src\\main\\java\\com\\sso\\service\\impl");
        pathInfo.put(OutputFile.service, "E:\\IdeaProject\\sso\\sso-service\\src\\main\\java\\com\\sso\\service");
        pathInfo.put(OutputFile.entity, "E:\\IdeaProject\\sso\\sso-mapper\\src\\main\\java\\com\\sso\\common\\entity");
        pathInfo.put(OutputFile.mapperXml, "E:\\IdeaProject\\sso\\sso-mapper\\src\\main\\resources\\mapper");
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/sso", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("lvxiaozuo") // 设置作者
                            .dateType(DateType.ONLY_DATE)
                            .enableSwagger()// 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("E:\\IdeaProject\\sso\\sso-mapper\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.sso") // 设置父包名
                            .mapper("common.mapper") // 设置父包模块名
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("web.application.Controller")
                            .entity("common.entity")
                            .pathInfo(pathInfo);
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sso_serial_number") // 设置需要生成的表名
                            //.addTablePrefix("sso_") // 设置过滤表前缀
                            .entityBuilder()
                            .versionColumnName("version")
                            .addTableFills(new Column("version", FieldFill.INSERT))
                            .addTableFills(new Column("creator", FieldFill.INSERT))
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Column("updater", FieldFill.UPDATE))
                            .addTableFills(new Column("update_time", FieldFill.UPDATE))
                            .enableLombok();
                })
                .templateConfig(builder -> {
                    builder.disable(TemplateType.CONTROLLER,TemplateType.SERVICE,TemplateType.SERVICEIMPL,TemplateType.MAPPER,TemplateType.XML);
                }).templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
