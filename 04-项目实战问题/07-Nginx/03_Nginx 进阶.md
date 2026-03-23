## 一、支持 HTTPS

**要让 Nginx 服务器支持 HTTPS，我们需要：① 申请 SSL 证书 → ② 把证书上传到 Nginx 服务器 → ③ 配置 Nginx 监听 443 端口，把 HTTP 请求重定向到 HTTPS**

#### 1、申请 SSL 证书

SSL 证书可以从云服务商免费申请，以阿里云为例：

* 登录阿里云控制台，搜索"SSL 证书"
* 点击"免费证书"，申请一张免费的 DV 单域名证书（有效期 3 个月，到期续签即可）
* SSL 证书管理处，填写要绑定的域名，如 sixpense.com
* SSL 证书管理处，完成 DNS 验证（阿里云会提示你在 DNS 解析里添加一条 TXT 记录，用来证明你是这个域名的所有者，先去去填一下，再回来验证）
* 验证通过后，证书签发完成，下载证书

下载时选择 **Nginx** 类型，解压后会得到两个文件：

```
sixpense.com.pem   # 证书文件（公钥 + 证书链）
sixpense.com.key   # 私钥文件（绝对不能泄露）
```

#### 2、把证书上传到 Nginx 服务器

把这两个文件上传到云服务器，建议放在 /etc/nginx/ssl/ 目录下：

```bash
# 在服务器上创建 ssl 目录
mkdir -p /etc/nginx/ssl

# 从本地上传证书文件到服务器（在本地终端执行）
scp sixpense.com.pem root@8.136.43.114:/etc/nginx/ssl/
scp sixpense.com.key root@8.136.43.114:/etc/nginx/ssl/

# 限制私钥文件的权限，只有 root 能读
chmod 600 /etc/nginx/ssl/sixpense.com.key
```

#### 3、配置 Nginx 监听 443 端口，把 HTTP 请求重定向到 HTTPS

创建或修改 /etc/nginx/conf.d/sixpense.conf，添加两个 server 虚拟主机：一个监听 443 处理 HTTPS 请求，一个监听 80 把 HTTP 请求重定向到 HTTPS

```yaml
# ① 监听 443 端口，处理 HTTPS 请求
server {
    # ssl 参数告诉 Nginx 这个端口走 SSL/TLS 加密
    listen 443 ssl;
    server_name sixpense.com;

    # 证书文件路径
    ssl_certificate     /etc/nginx/ssl/sixpense.com.pem;
    # 私钥文件路径
    ssl_certificate_key /etc/nginx/ssl/sixpense.com.key;

    # 只允许 TLS 1.2 和 1.3，禁用老旧不安全的协议版本
    ssl_protocols TLSv1.2 TLSv1.3;
    # 推荐的加密套件，安全性和兼容性兼顾
    ssl_ciphers ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:DHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384;
    # 优先使用服务端的加密套件
    ssl_prefer_server_ciphers off;

    # SSL 会话缓存，提升性能，避免每次请求都重新握手
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 1d;

    # 部署前端静态资源
    location / {
        root /var/www/sixpense;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 如果有后端接口，配置反向代理
    location /api/ {
        proxy_pass http://8.136.43.114:8080/;
    }
}

# ② 监听 80 端口，把所有 HTTP 请求永久重定向到 HTTPS
# 这样用户就算输入 http://sixpense.com 也会自动跳转到 https://sixpense.com
server {
    listen 80;
    server_name sixpense.com;

    # 301 永久重定向，$host 是请求的域名，$request_uri 是请求的路径+参数
    return 301 https://$host$request_uri;
}
```

#### 4、开放 443 端口

云服务器的安全组默认只开放了 80 端口，需要手动添加 443 端口的入站规则：

* 登录阿里云控制台 → 云服务器 ECS → 安全组 → 配置规则
* 添加入站规则：协议 TCP、端口 443、来源 0.0.0.0/0

#### 5、重载 Nginx 配置

```bash
# 先检查配置文件语法是否正确
nginx -t

# 语法没问题后，重载配置（不会中断现有连接）
nginx -s reload
```

#### 6、验证效果

* 访问 https://sixpense.com，浏览器地址栏出现小锁图标，说明 HTTPS 配置成功
* 访问 http://sixpense.com，应该自动跳转到 https://sixpense.com，说明 HTTP → HTTPS 重定向生效

#### 7、证书到期续签

免费证书有效期只有 3 个月，到期前阿里云会发邮件提醒，续签步骤和申请一样，重新下载证书文件替换掉服务器上的旧文件，然后 nginx -s reload 即可

