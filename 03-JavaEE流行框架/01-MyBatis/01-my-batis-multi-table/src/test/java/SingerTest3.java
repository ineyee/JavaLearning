import bean.SingerBean1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import util.LocalDateTimeAdapterUtil;
import util.MyBatisUtil;

import java.time.LocalDateTime;
import java.util.List;

public class SingerTest3 {
    @Test
    void list() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            List<SingerBean1> singerBean1List = session.selectList("dao.singerDao3.list");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterUtil())
                    .create();
            String json = gson.toJson(singerBean1List);
            if (!singerBean1List.isEmpty()) {
                System.out.println("查询成功：" + json);
            } else {
                System.out.println("查询失败");
            }
        }
    }
}
