import bean.UserBean;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import util.MyBatisUtil;

import java.util.List;
import java.util.Map;

public class UserTest {
    // 保存成功，一般是给客户端返回保存成功那条数据的完整数据，这样就不用客户端再查询一遍了
    @Test
    void save() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.insert(statement, parameter)：用来插入数据，成功则返回影响的数据条数，失败则返回 0，出错则抛异常
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当参数个数为一个时，直接把参数值传进去就可以了

            // 这个 userBean 演示全部属性都有值
//            UserBean userBean = new UserBean();
//            userBean.setName("Kobe");
//            userBean.setAge(41);
//            userBean.setHeight(1.98);
//            userBean.setEmail("kobe@gmail.com");

            // 这个 userBean 演示有的可选属性没值
            UserBean userBean = new UserBean();
            userBean.setName("James");
            userBean.setHeight(2.03);
            userBean.setEmail("james@gmail.com");

            int ret = session.insert("com.ineyee.dao.UserDao.save", userBean);
            if (ret > 0) {
                System.out.println("保存成功：" + userBean);
            } else {
                System.out.println("保存失败");
            }

            // MyBatis 默认开启了事务，关闭了自动提交
            // 所以针对“增删改”操作，我们需要手动提交事务，“查”操作不影响数据库，不需要提交事务
            session.commit();
        }
    }

    // 更新成功，一般是给客户端返回成功或失败的信息即可，客户端根据返回结果决定要不要更新内存数据就行
    @Test
    void update() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.update(statement, parameter)：用来更新数据，成功则返回影响数据条数，失败则返回 0，出错则抛异常
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当参数个数为一个时，直接把参数值传进去就可以了

            // 这个 userBean 演示有的可选属性没值
            UserBean userBean = new UserBean();
            userBean.setId(31);
            userBean.setAge(40);

            int ret = session.update("com.ineyee.dao.UserDao.update", userBean);
            if (ret > 0) {
                System.out.println("更新成功");
            } else {
                System.out.println("更新失败");
            }

            // MyBatis 默认开启了事务，关闭了自动提交
            // 所以针对“增删改”操作，我们需要手动提交事务，“查”操作不影响数据库，不需要提交事务
            session.commit();
        }
    }

    @Test
    void get() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.selectOne(statement, parameter)：用来查询单条数据，查到则返回 bean，查不到则返回 null
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当参数个数为一个时，直接把参数值传进去就可以了
            UserBean userBean = session.selectOne("com.ineyee.dao.UserDao.get", 21);
            System.out.println(userBean);
        }
    }

    @Test
    void list() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.selectList(statement, parameter)：用来查询多条数据，查到则返回 [bean]，查不到则返回 []
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当参数个数为多个时，可以搞成 Map 把参数传进去（value 值在外界算好再传进去，里面只能读取、不能进行运算）
            Integer pageSize = 2;
            Integer currentPage = 2;
            List<UserBean> userBeanList = session.selectList("com.ineyee.dao.UserDao.list", Map.of(
                    "limit", pageSize,
                    "offset", (currentPage - 1) * pageSize
            ));
            for (UserBean userBean : userBeanList) {
                System.out.println(userBean);
            }
        }
    }
}
