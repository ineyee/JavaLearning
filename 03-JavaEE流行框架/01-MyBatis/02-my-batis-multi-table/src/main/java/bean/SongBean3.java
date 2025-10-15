package bean;

import java.util.List;

public class SongBean3 extends BaseBean {
    private String name;
    private String cover;
    // 多对多表结构时，在 Java 代码里我们会定义一个数组引用对方的“多”
    // 这样方便 Java 层面读写数据，而且 Java 层面一看就是多对多关系
    //
    // 也就是说，数据库表是通过外键来表征多对多关系的，Java 代码是通过数组引用和数组引用来表征多对多关系的
    private List<SingerBean3> singerBean3List;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<SingerBean3> getSingerBean3List() {
        return singerBean3List;
    }

    public void setSingerBean3List(List<SingerBean3> singerBean3List) {
        this.singerBean3List = singerBean3List;
    }

    public String toString() {
        return "SongBean1{" +
                "id=" + getId() +
                ", createTime='" + getCreateTime() + '\'' +
                ", updateTime='" + getUpdateTime() + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", singerBean3List=" + singerBean3List +
                '}';
    }
}
