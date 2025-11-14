package com.ineyee;

import com.ineyee.domain.User;
import com.github.pagehelper.PageHelper;
import com.ineyee.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Random;

public class UserTest {
    // 读取 Spring 的配置文件
    private final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    // 通过 bean 对象的 id 获取对象
    private final UserDao userDao = (UserDao) applicationContext.getBean("userDao");

    // 查询成功一般是给客户端返回 data=[bean]，查询失败一般是给客户端返回 data=[]
    @Test
    void listPageHelper() {
        Integer pageSize = 2; // 每页显示多少条数据
        Integer pageNum = 1; // 当前页码，从 1 开始
        PageHelper.startPage(pageNum, pageSize);
        List<User> userBeanList = userDao.listPageHelper();
        if (!userBeanList.isEmpty()) {
            System.out.println("查询成功：" + userBeanList);
        } else {
            System.out.println("查询失败");
        }
    }

    // 保存成功一般是给客户端返回刚保存成功的那条完整数据 data=bean，这样客户端就不用再查询一遍了
    // 保存失败一般是给客户端返回 data=null
    @Test
    void save() {
        User userBean = new User();
        userBean.setName("Kobe");
        userBean.setAge(41);
        userBean.setHeight(1.98);
        userBean.setEmail("kobe" + new Random().nextInt(1000) + "@gmail.com");

        int ret = userDao.save(userBean);
        if (ret > 0) {
            System.out.println("保存成功：" + userBean);
        } else {
            System.out.println("保存失败");
        }
    }
}
