package com.ineyee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// 歌曲列表模块的接口
@Controller
public class SongListController {
    // 获取歌曲列表接口
    // 请求路径为 "/song/list"，请求方法为 GET
    @RequestMapping(path = "/song/list", method = RequestMethod.GET)
    @ResponseBody
    public String getSongList() {
        return "getSongList success";
    }
}
