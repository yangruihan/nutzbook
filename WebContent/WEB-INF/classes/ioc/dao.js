var ioc = {
		dataSource : {
			type : "com.alibaba.druid.pool.DruidDataSource",
			events : {
				create : "init",
				depose : "close"
			},
			fields : {
				driverClassName : 'com.mysql.jdbc.Driver',
				url : 'jdbc:mysql:///nutz_test?useUnicode=true&amp;characterEncoding=UTF-8',
				username : 'root',
				password : '123456',
				testWhileIdle : true,
				validationQuery : "select 1",
				maxActive : 100
			}
		},
		dao : {
			type : "org.nutz.dao.impl.NutDao",
			args : [{refer:"dataSource"}]
		}
}