package com.ineyee.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ineyee.mapper.SingerMapper;
import com.ineyee.pojo.po.Singer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SingerServiceImpl extends ServiceImpl<SingerMapper, Singer> implements SingerService {

}
