package com.yangruihan.nutzbook.module;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;

public class BaseModule {

	// 注入同名的一个 ioc 对象
	@Inject protected Dao dao;
}
