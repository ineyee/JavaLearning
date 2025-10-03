package dao;

import bean.ProductBean;
import bean.UserBean;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.DatabaseUtil;
import util.LocalDateTimeAdapterUtil;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public int save(ProductBean productBean) throws SQLException {
        String sql = """
                INSERT INTO t_product (
                    name,
                    price,
                    `desc`,
                    user_id
                ) VALUES (
                    ?,
                    ?,
                    ?,
                    ?
                );
                """;
        return DatabaseUtil.executeUpdate(sql, productBean.getName(), productBean.getPrice(), productBean.getDesc(), productBean.getUserBean().getId());
    }

    @Override
    public ProductBean get(Integer id) throws SQLException {
        // 因为有外键，所以查询时就是一对多表结构的多表查询
        String sql = """
                SELECT
                	t_product.id,
                	t_product.create_time,
                	t_product.update_time,
                	t_product.name,
                	t_product.price,
                	t_product.desc,
                    JSON_OBJECT(
                       'id', t_user.id,
                       'create_time', t_user.create_time,
                       'update_time', t_user.update_time,
                       'name', t_user.name,
                       'age', t_user.age,
                       'height', t_user.height,
                       'email', t_user.email
                    ) AS user
                FROM t_product
                LEFT JOIN t_user ON t_product.user_id = t_user.id
                WHERE t_product.id = ?;
                """;
        List<ProductBean> productBeanList = DatabaseUtil.executeQuery(sql, resultSet -> {
            // 把 json 字符串转成 bean
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterUtil())
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            UserBean userBean = gson.fromJson(resultSet.getString("user"), UserBean.class);

            ProductBean productBean = new ProductBean();
            productBean.setId(resultSet.getInt("id"));
            productBean.setCreateTime(resultSet.getTimestamp("create_time").toLocalDateTime());
            productBean.setUpdateTime(resultSet.getTimestamp("update_time").toLocalDateTime());
            productBean.setName(resultSet.getString("name"));
            productBean.setPrice(resultSet.getDouble("price"));
            productBean.setDesc(resultSet.getString("desc"));
            productBean.setUserBean(userBean);

            return productBean;
        }, id);
        return productBeanList.isEmpty() ? null : productBeanList.get(0);
    }
}
