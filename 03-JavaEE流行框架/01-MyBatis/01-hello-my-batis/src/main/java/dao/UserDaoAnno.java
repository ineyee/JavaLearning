package dao;

import bean.UserBean;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserDaoAnno {
    @Select("""
            SELECT id,
                    name,
                    age,
                    height,
                    email,
                    create_time,
                    update_time
            FROM t_user
            WHERE id = #{id};
            """)
    UserBean get(Integer id);

    @Select("""
            SELECT id,
                   name,
                   age,
                   height,
                   email,
                   create_time,
                   update_time
            FROM t_user
            ORDER BY create_time DESC
                     -- limit、offset 是参数名，因为外界传进来一个 Map，所以参数名只要和 Map 中的 key 一一对应，就能顺利匹配到参数值
                LIMIT #{limit}
            OFFSET #{offset};
            """)
    List<UserBean> list(Map<String, Object> params);

    @Select("""
            SELECT id,
                   name,
                   age,
                   height,
                   email,
                   create_time,
                   update_time
            FROM t_user
            ORDER BY create_time DESC
            """)
    List<UserBean> listPageHelper();

    // 需自动回填字段
    int save(UserBean userBean);

    // 需自动回填字段 + foreach 批量处理
    int saveBatch(List<UserBean> userBeanList);

    @Delete("""
            DELETE
            FROM t_user
            WHERE id = #{id};
            """)
    int remove(Integer id);

    // 需 foreach 批量处理
    int removeBatch(List<Integer> idList);

    // 需 if 动态 SQL
    int update(Map<String, Object> params);

    // 需 if 动态 SQL + foreach 批量处理
    int updateBatch(Map<String, Object> params);
}