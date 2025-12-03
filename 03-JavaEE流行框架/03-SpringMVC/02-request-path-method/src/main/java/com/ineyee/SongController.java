package com.ineyee;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// 歌曲模块的接口
@Controller
@RequestMapping(path = "/song")
public class SongController {
    // 获取歌曲列表接口
    // 请求路径为 "/song/list"，请求方法为 GET
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    @ResponseBody
    public String getSongList() {
        return "getSongList success";
    }

    // 获取歌曲详情接口
    // 请求路径为 "/song/detail"，请求方法为 GET
    @GetMapping(path = "/detail")
    @ResponseBody
    public String getSongDetail() {
        return "getSongDetail success";
    }
}
