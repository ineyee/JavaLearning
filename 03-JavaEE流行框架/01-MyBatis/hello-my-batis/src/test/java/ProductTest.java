import bean.ProductBean;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import util.MyBatisUtil;

public class ProductTest {
    @Test
    void get() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            ProductBean productBean = session.selectOne("dao.ProductDao.get", 1);
            System.out.println(productBean);
        }
    }
}
