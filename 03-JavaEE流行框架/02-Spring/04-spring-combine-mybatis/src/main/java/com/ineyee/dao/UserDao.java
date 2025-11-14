package com.ineyee.dao;

import com.ineyee.domain.User;

import java.util.List;

// 一张表对应的 dao，我们一般会把它定义成接口，然后为这个接口编写多个实现类
// 因为实际开发中我们很可能需要根据实际情况切换访问数据库的方案，比如 JDBC、Hibernate、MyBatis 等，这种面向接口编程的方式方便切换方案
public interface UserDao {
    /**
     * 分页获取用户列表
     * 参数由 PageHelper 插件管理，所以这个方法就不用定义参数了
     *
     * @return 用户 bean 列表
     * ① [bean] 代表本次查询有数据
     * ② [] 代表本次查询没有数据
     */
    List<User> listPageHelper();

    /**
     * 新增一个用户
     *
     * @param user 用户 bean
     * @return 影响的数据条数
     * ① 影响的数据条数 > 0 代表保存成功，此时 MyBatis 会自动回填传进来 userBean 的其它参数，我们一般是给客户端响应刚保存成功的那条完整数据 data=bean，这样客户端就不用再查询一遍了
     * ② 影响的数据条数 <= 0 代表保存失败，我们一般是给客户端响应 data=null
     */
    int save(User user);
}
