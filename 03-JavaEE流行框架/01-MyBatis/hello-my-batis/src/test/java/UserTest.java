import bean.UserBean;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import util.MyBatisUtil;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserTest {
    // 查询成功一般是给客户端返回 data=bean，查询失败一般是给客户端返回 data=null
    @Test
    void get() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.selectOne(statement, parameter)：用来查询单条数据，查到则返回 bean，查不到则返回 null
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当 SQL 语句的参数个数为一个时，直接把参数值传进去就可以了
            UserBean userBean = session.selectOne("dao.UserDao.get", 21);
            if (userBean != null) {
                System.out.println("查询成功：" + userBean);
            } else {
                System.out.println("查询失败");
            }
        }
    }

    // 查询成功一般是给客户端返回 data=[bean]，查询失败一般是给客户端返回 data=[]
    @Test
    void list() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.selectList(statement, parameter)：用来查询多条数据，查到则返回 [bean]，查不到则返回 []
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当 SQL 语句的参数个数为多个时，可以把多个参数包装进一个 Bean 或 Map 传进去（value 值一定要在外界算好再传进去，里面只能读取，不能进行运算）
            Integer pageSize = 2; // 每页显示多少条数据
            Integer currentPage = 1; // 当前页码，从 1 开始
            List<UserBean> userBeanList = session.selectList("dao.UserDao.list", Map.of(
                    "limit", pageSize,
                    "offset", (currentPage - 1) * pageSize
            ));
            if (!userBeanList.isEmpty()) {
                System.out.println("查询成功：" + userBeanList);
            } else {
                System.out.println("查询失败");
            }
        }
    }


    // 保存成功一般是给客户端返回刚保存成功的哪条完整数据 data=bean，这样客户端就不用再查询一遍了
    // 保存失败一般是给客户端返回 data=null
    @Test
    void save() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.insert(statement, parameter)：用来插入数据，成功则返回影响的数据条数，失败则返回 0，出错则抛异常
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当 SQL 语句的参数个数为多个时，可以把多个参数包装进一个 Bean 或 Map 传进去（value 值一定要在外界算好再传进去，里面只能读取，不能进行运算）
            UserBean userBean = new UserBean();
            userBean.setName("Kobe");
            userBean.setAge(41);
            userBean.setHeight(1.98);
            userBean.setEmail("kobe" + new Random().nextInt(1000) + "@gmail.com");

            int ret = session.insert("dao.UserDao.save", userBean);
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

    // 批量保存成功一般是给客户端返回刚保存成功哪批数据的 id data = [bean.id]，客户端可以根据 id 数组再手动查询一次数据库拿到完整数据
    // 批量保存失败一般是给客户端返回 data=null
    @Test
    void saveBatch() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 这个 userBean 演示全部属性都有值
            UserBean userBean1 = new UserBean();
            userBean1.setName("Kobe");
            userBean1.setAge(41);
            userBean1.setHeight(1.98);
            userBean1.setEmail("kobe" + new Random().nextInt(1000) + "@gmail.com");

            // 这个 userBean 演示有的可选属性没值
            UserBean userBean2 = new UserBean();
            userBean2.setName("James");
            userBean2.setEmail("james" + new Random().nextInt(1000) + "@gmail.com");

            List<UserBean> userBeanList = List.of(userBean1, userBean2);

            int ret = session.insert("dao.UserDao.saveBatch", userBeanList);
            if (ret > 0) {
                List<Integer> newIdList = userBeanList.stream().map(UserBean::getId).toList();
                System.out.println("批量保存成功：" + newIdList);
            } else {
                System.out.println("批量保存失败");
            }

            session.commit();
        }
    }


    // 删除成功一般是给客户端返回成功信息、因为客户端本来就知道删除的是哪条数据，删除失败一般是给客户端返回失败信息
    @Test
    void remove() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.delete(statement, parameter)：用来删除数据，成功则返回影响数据条数，失败则返回 0，出错则抛异常
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当 SQL 语句的参数个数为一个时，直接把参数值传进去就可以了
            int ret = session.delete("dao.UserDao.remove", 38);
            if (ret > 0) {
                System.out.println("删除成功");
            } else {
                System.out.println("删除失败");
            }

            // MyBatis 默认开启了事务，关闭了自动提交
            // 所以针对“增删改”操作，我们需要手动提交事务，“查”操作不影响数据库，不需要提交事务
            session.commit();
        }
    }

    // 批量删除成功一般是给客户端返回成功信息、因为客户端本来就知道删除的是哪批数据，批量删除失败一般是给客户端返回失败信息
    @Test
    void removeBatch() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            List<Integer> idList = List.of(38, 41, 42);

            int ret = session.delete("dao.UserDao.removeBatch", idList);
            if (ret > 0) {
                System.out.println("批量删除成功");
            } else {
                System.out.println("批量删除失败");
            }

            session.commit();
        }
    }


    // 更新成功一般是给客户端返回成功信息、因为客户端本来就知道更新的是哪条数据、哪些字段，更新失败一般是给客户端返回失败信息
    @Test
    void update() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.update(statement, parameter)：用来更新数据，成功则返回影响数据条数，失败则返回 0，出错则抛异常
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当 SQL 语句的参数个数为多个时，可以把多个参数包装进一个 Bean 或 Map 传进去（value 值一定要在外界算好再传进去，里面只能读取，不能进行运算）
            int ret = session.update("dao.UserDao.update", Map.of(
                    "id", 44,
                    "name", "James"
            ));
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

    // 批量更新成功一般是给客户端返回成功信息、因为客户端本来就知道更新的是哪条数据、哪些字段，批量更新失败一般是给客户端返回失败信息
    @Test
    void updateBatch() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.update(statement, parameter)：用来更新数据，成功则返回影响数据条数，失败则返回 0，出错则抛异常
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当 SQL 语句的参数个数为多个时，可以把多个参数包装进一个 Bean 或 Map 传进去（value 值一定要在外界算好再传进去，里面只能读取，不能进行运算）
            int ret = session.update("dao.UserDao.updateBatch", Map.of(
                    "idList", List.of(43, 44, 45),
                    "height", 2.03
            ));
            if (ret > 0) {
                System.out.println("批量更新成功");
            } else {
                System.out.println("批量更新失败");
            }

            // MyBatis 默认开启了事务，关闭了自动提交
            // 所以针对“增删改”操作，我们需要手动提交事务，“查”操作不影响数据库，不需要提交事务
            session.commit();
        }
    }
}
