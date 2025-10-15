import bean.SongBean1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.LocalDateTimeAdapterUtil;
import util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class SongTest2 {
    @Test
    void list() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            List<SongBean1> songBean1List = session.selectList("dao.songDao2.list");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterUtil())
                    .create();
            String json = gson.toJson(songBean1List);

            if (!songBean1List.isEmpty()) {
                System.out.println("查询成功：" + json);
            } else {
                System.out.println("查询失败");
            }
        }
    }
}
