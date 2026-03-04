package com.ineyee.common.context;

import lombok.Data;

// 这个类用来映射 token 里存储的业务信息，可以根据实际情况添加属性
@Data
public class TokenInfo {
    private String email;
}
