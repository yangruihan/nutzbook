package com.yangruihan.nutzbook;

import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@IocBy(type=ComboIocProvider.class, args={
		"*js", "ioc/",
		"*anno", "com.yangruihan.nutzbook",
		"*tx"
}) // 设置 IOC 配置文件
@Modules(scanPackage=true) // 设置自动扫描模块选项
@SetupBy(value=MainSetup.class) // 设置初始化设置
public class MainModule {

}
