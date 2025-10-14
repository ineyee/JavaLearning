package bean;

public class SongBean2 extends BaseBean {
    private String name;
    private String cover;
    // 一对多表结构时，这里是多方、从表，在 Java 代码里我们会定义一个对象引用对方的“一”
    // 这样方便 Java 层面读写数据，而且 Java 层面一看就是一对多关系
    //
    // 也就是说，数据库表是通过外键来表征一对多关系的，Java 代码是通过对象引用和数组引用来表征一对多关系的
    private SingerBean2 singerBean2;

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

    public SingerBean2 getSingerBean2() {
        return singerBean2;
    }

    public void setSingerBean2(SingerBean2 singerBean2) {
        this.singerBean2 = singerBean2;
    }

    public String toString() {
        return "SongBean1{" +
                "id=" + getId() +
                ", createTime='" + getCreateTime() + '\'' +
                ", updateTime='" + getUpdateTime() + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", SingerBean2=" + singerBean2 +
                '}';
    }
}
