package common;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

// 必须继承自 PooledDataSourceFactory
public class DruidDataSourceFactory extends PooledDataSourceFactory {
    public DruidDataSourceFactory() {
        // 必须把 DruidDataSource 设置给 dataSource 属性
        this.dataSource = new DruidDataSource();
    }
}