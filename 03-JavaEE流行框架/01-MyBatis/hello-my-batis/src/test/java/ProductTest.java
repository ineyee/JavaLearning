import bean.ProductBean;
import bean.UserBean;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import util.MyBatisUtil;

public class ProductTest {
    @Test
    void save() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            UserBean userBean = session.selectOne("dao.UserDao.get", 36);
            if (userBean != null) {
                ProductBean productBean = new ProductBean();
                productBean.setName("诺基亚");
                productBean.setPrice(1000.0);
                productBean.setDesc("很早以前的手机");
                productBean.setUserBean(userBean);

                int ret = session.insert("dao.ProductDao.save", productBean);
                if (ret > 0) {
                    System.out.println("保存成功：" + productBean);
                } else {
                    System.out.println("保存失败");
                }

                session.commit();
            } else {
                System.out.println("查询用户失败，无法保存产品");
            }
        }
    }

    @Test
    void get() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            ProductBean productBean = session.selectOne("dao.ProductDao.get", 6);
            System.out.println(productBean);
        }
    }
}
