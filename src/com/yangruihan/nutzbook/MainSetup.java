package com.yangruihan.nutzbook;

import java.util.Date;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import com.yangruihan.nutzbook.bean.User;

public class MainSetup implements Setup {

	/**
	 * 配置初始化方法
	 */
	@Override
	public void init(NutConfig nc) {
		Ioc ioc = nc.getIoc();
		Dao dao = ioc.get(Dao.class);
		
		// 根据 POJO 创建数据库表
		Daos.createTablesInPackage(dao, "com.yangruihan.nutzbook", false);
		
		// 初始化默认用户
		if (dao.count(User.class) == 0) {
			User user = new User();
			user.setName("admin");
			user.setPassword("123456");
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			dao.insert(user);
		}
	}

	@Override
	public void destroy(NutConfig nc) {
		// TODO Auto-generated method stub
		
	}
}
