package com.yangruihan.nutzbook.module;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.yangruihan.nutzbook.bean.User;

@IocBean
@At("/user")
@Ok("json")
@Fail("http:500")
public class UserModule {

	@Inject
	protected Dao dao;
	
	@At
	public int count() {
		return dao.count(User.class);
	}
	
	/**
	 * 用户登录
	 * @param name
	 * @param password
	 * @param session
	 * @return
	 */
	@At
	public Object login(@Param("username")String name, @Param("password")String password, HttpSession session) {
		User user = dao.fetch(User.class, Cnd.where("name", "=", name).and("password", "=", password));
		if (user == null) {
			return false;
		} else {
			session.setAttribute("me", user.getId());
			return true;
		}
	}
	
	/**
	 * 用户注销
	 * @param session
	 */
	@At
	@Ok(">>:/")
	public void logout(HttpSession session) {
		session.invalidate();
	}
}
