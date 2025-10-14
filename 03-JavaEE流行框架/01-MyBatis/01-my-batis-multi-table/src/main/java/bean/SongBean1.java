package bean;

public class SongBean1 extends BaseBean {
    private String name;
    private String cover;
    // 一对一表结构时，在 Java 代码里我们会定义一个对象引用对方的“一”
    // 这样方便 Java 层面读写数据，而且 Java 层面一看就是一对一关系
    //
    // 也就是说，数据库表是通过外键来表征一对一关系的，Java 代码是通过对象引用和对象引用来表征一对一关系的
    private SingerBean1 singerBean1;

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

    public SingerBean1 getSingerBean1() {
        return singerBean1;
    }

    public void setSingerBean1(SingerBean1 singerBean1) {
        this.singerBean1 = singerBean1;
    }

    public String toString() {
        return "SongBean1{" +
                "id=" + getId() +
                ", createTime='" + getCreateTime() + '\'' +
                ", updateTime='" + getUpdateTime() + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", singerBean1=" + singerBean1 +
                '}';
    }
}
