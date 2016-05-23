package com.yangruihan.nutzbook.module;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Attr;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;

import com.yangruihan.nutzbook.bean.User;

@IocBean // 声明为 Ioc 容器中的一个 Bean
@At("/user") // 整个模块的路径前缀
@Ok("json:{locked:'password|salt', ignoreNull:true}") // 避免密码和盐值发送到浏览器
@Fail("http:500") // 抛出异常的话，就走500页面
@Filters(@By(type=CheckSession.class, args = {"me", "/"})) // Session检查，如果当前Session没有带me这个attribute，就跳转到/页面
public class UserModule {

	@Inject
	protected Dao dao; // 注入同名的一个 Ioc 对象
	
	@At("/")
	@Ok("jsp:jsp.user.list") // 添加页面中转方法，真实路径使 /WEB-INF/jsp/user/list.jsp
	public void index() {
		
	}
	
	/**
	 * 用户登录
	 * @param name
	 * @param password
	 * @param session
	 * @return
	 */
	@At
	@Filters() // 设置空过滤器，覆盖整个类的过滤器，允许用户登录
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
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@At
	public Object add(@Param("..")User user) {
		NutMap re = new NutMap();
		String msg = checkUser(user, true);
		if (msg != null) {
			return re.setv("ok", false).setv("msg", msg);
		}
		
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		user = dao.insert(user);
		return re.setv("ok", true).setv("data", user);
	}
	
	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	@At
	public Object update(@Param("..")User user) {
		NutMap re = new NutMap();
		String msg = checkUser(user, false);
		if (msg != null) {
			return re.setv("ok", false).setv("msg", msg);
		}
		
		user.setName(null); // 不允许更新用户名
		user.setCreateTime(null); // 不允许更新创建时间
		user.setUpdateTime(new Date()); // 设置正确的更新时间
		dao.updateIgnoreNull(user);  // 更新并且忽略 Null 值，即真正更新的只有 password 和 salt
		return re.setv("ok", true);
	}
	
	/**
	 * 删除用户
	 * @param id
	 * @param me
	 * @return
	 */
	@At
	public Object delete(@Param("id")int id, @Attr("me")int me) {
		if (me == id) {
			return new NutMap().setv("ok", false).setv("msg", "不能删除当前用户");
		}
		
		if (id > 0) {
			dao.delete(User.class, id);
		} else {
			return new NutMap().setv("ok", false).setv("msg", "用户Id非法");
		}
		
		return new NutMap().setv("ok", true);
	}
	
	/**
	 * 查询用户
	 * @param name
	 * @param pager
	 * @return
	 */
	@At
	public Object query(@Param("name")String name, @Param("..")Pager pager) {
		Cnd cnd = Strings.isBlank(name) ? null : Cnd.where("name", "like", "%" + name + "%");
		QueryResult qr = new QueryResult();
		qr.setList(dao.query(User.class, cnd, pager));
		pager.setRecordCount(dao.count(User.class, cnd));
		qr.setPager(pager);
		return qr; // 默认分页是第一页，每页20条
	}
	
	protected String checkUser(User user, boolean create) {
		if (user == null) {
			return "空对象";
		}
		
		if (create) {
			if (Strings.isBlank(user.getName()) || Strings.isBlank(user.getPassword())) {
				return "用户名/密码不能为空";
			}
		} else {
			if (Strings.isBlank(user.getPassword())) {
				return "密码不能为空";
			}
		}
		
		String passwd = user.getPassword().trim();
		if (6 > passwd.length() || passwd.length() > 12) {
			return "密码长度错误";
		}
		
		user.setPassword(passwd);
		if (create) {
			int count = dao.count(User.class, Cnd.where("name", "=", user.getName()));
			if (count != 0) {
				return "用户名已存在";
			}
		} else {
			if (user.getId() < 1) {
                return "用户Id非法";
            }
		}
		if (user.getName() != null)
            user.setName(user.getName().trim());
        return null;
	}
}
