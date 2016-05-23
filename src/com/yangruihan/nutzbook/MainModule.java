package com.yangruihan.nutzbook;

import org.nutz.mvc.annotation.ChainBy;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@IocBy(type=ComboIocProvider.class, args={
		"*js", "ioc/",
		"*anno", "com.yangruihan.nutzbook",
		"*tx",
		"*org.nutz.integration.quartz.QuartzIocLoader"
}) // 设置 IOC 配置文件
@Modules(scanPackage=true) // 设置自动扫描模块选项
@SetupBy(value=MainSetup.class) // 设置初始化设置
@Ok("json:full") // 配置默认成功返回值
@Fail("jsp:jsp.500") // 配置默认失败返回值
@Localization(value="msg/", defaultLocalizationKey="zh-CN") // 配置国际化
@ChainBy(args="mvc/nutzbook-mvc-chain.js") // 配置动作链
public class MainModule {

}
