import bean.ProductBean;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import util.MyBatisUtil;

public class ProductTest {
    @Test
    void get() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.selectOne(statement, parameter)：用来查询单条数据，查到则返回 bean，查不到则返回 null
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当参数个数为一个时，直接把参数值传进去就可以了
            ProductBean productBean = session.selectOne("com.ineyee.dao.ProductDao.get", 1);
            System.out.println(productBean);
        }
    }
}
