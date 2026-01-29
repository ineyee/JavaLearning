package com.ineyee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ineyee.pojo.dto.SongListDto;
import com.ineyee.pojo.po.Song;
import com.ineyee.pojo.query.SongListQuery;

import java.util.List;

public interface SongMapper extends BaseMapper<Song> {
    // 我们可以用 MyBatisPlus 提供的分页插件，自动把查询结果搞进一个 Page 对象里，接口方法加个参数即可
    List<SongListDto> selectList(Page<SongListDto> page, SongListQuery query);
}
