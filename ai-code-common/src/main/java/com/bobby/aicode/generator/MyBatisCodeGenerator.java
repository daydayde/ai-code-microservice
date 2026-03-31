package com.bobby.aicode.generator;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Map;

public class MyBatisCodeGenerator {
    public static final String[] TABLE_NAMES = new String[]{"chat_history"};

    public static void main(String[] args) {
        //配置数据源
        Dict dict = YamlUtil.loadByPath("application.yml");
        Map<String, Object>dataSourceConfig = dict.getByPath("spring.datasource");
        String url = dataSourceConfig.get("url").toString();
        String username = dataSourceConfig.get("username").toString();
        String password = dataSourceConfig.get("password").toString();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        //创建配置内容，两种风格都可以。
        //GlobalConfig globalConfig = createGlobalConfigUseStyle1();
        GlobalConfig globalConfig = createGlobalConfigUseStyle2();

        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        //生成代码
        generator.generate();
    }


    public static GlobalConfig createGlobalConfigUseStyle2() {
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        //设置根包
        globalConfig.getPackageConfig()
                .setBasePackage("com.bobby.aicode.genresult");

        //设置表前缀和只生成哪些表，setGenerateTable 未配置时，生成所有表
        globalConfig.getStrategyConfig()
                .setGenerateTable(TABLE_NAMES)
                .setLogicDeleteColumn("isDelete");

        //设置生成 entity 并启用 Lombok
        globalConfig.enableEntity()
                .setWithLombok(true)
                .setJdkVersion(21);

        //设置生成 mapper
        globalConfig.enableMapper();
        globalConfig.enableMapperXml();

        // 设置生成Service
        globalConfig.enableService();
        globalConfig.enableServiceImpl();

        // 设置Controller
        globalConfig.enableController();

        // 设置生成的注释
        globalConfig.getJavadocConfig()
                .setAuthor("<a href=\"https://github.com/daydayde\">Bobby</a>")
                .setSince("");

        return globalConfig;
    }
}