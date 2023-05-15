package com.group1.group1_backend;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://47.99.103.79/group1_2";
        String username = "group1";
        String password = "888888";
        String moduleName = "sys";
        String mapperLocation = "C:\\Users\\16654\\Desktop\\Project\\Java\\group1_project2_backend\\src\\main\\resources\\mapper\\" + moduleName;
        String tables = "user,role,user_role,db_link,history,history_gather_field";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("linqingchuan") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\Users\\16654\\Desktop\\Project\\Java\\group1_project2_backend\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.group1.group1_backend") // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀成的表名
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
