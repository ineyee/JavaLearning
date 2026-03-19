## 一、Nginx 服务器

Nginx（engine x）跟 Tomcat 一样也是一个服务器软件，运行 Nginx 的服务器通常被称为 Nginx 服务器，通常被用来做：

* Web 服务器、静态资源服务器

* 反向代理服务器

一个 Nginx 实例通常包含 1 个 master 进程 + N 个 worker 进程，master 进程主要用来接收客户端的请求、调度各个 worker 进程，而 worker 进程才主要用来处理客户端的请求、worker 进程数可以设置为跟服务器的核数保持一致来充分发挥性能，运行一个 Nginx 实例可以支持 3w~5w 的并发。

#### 1、Web 服务器、静态资源服务器

使用 Vue、React、Angular 等前端框架开发的 Web 项目，在打包后会生成一些静态资源文件（如 HTML、CSS、JS、图片等），这些静态资源文件通常会被部署到服务器的静态资源目录中。而服务器上安装的 Nginx 服务器软件则负责监听来自客户端的请求（通常是 80 或 443 端口），当客户端访问时，Nginx 就会按需主动从静态资源目录中读取相应的文件返回给客户端。因此运行 Nginx 的服务器通常被称为 Web 服务器或静态资源服务器。

![image-20260316211749569](img/image-20260316211749569.png)

#### 2、反向代理服务器

* 正向代理服务器

正向代理服务器是指代理服务器是给**客户端**做代理，也就是说代理服务器是在给客户端干活，翻墙用的代理服务器就是正向代理服务器。

![image-20260316220621094](img/image-20260316220621094.png)

* 反向代理服务器

反向代理服务器是指代理服务器是给**服务端**做代理，也就是说代理服务器是在给服务端干活，负载均衡服务器就是反向代理服务器。

![image-20260316221848406](img/image-20260316221848406.png)

## 一、Nginx 配置文件的语法

* ① 配置文件里由指令和指令块组成

```yaml
# 指令，没用 {} 包着的就是指令
# 每条指令必须以分号结尾，指令名和指令值之间用一个或多个空格隔开
worker_processes  1;
```

```yaml
# 指令块，用 {} 包着的就是指令块
# 指令块里可以写多条指令
events {
    worker_connections  1024;
}
```

* ② include 用来导入其它配置文件

```yaml
http {
		# 如果觉得一个配置文件里的内容太多，可以拆分成多个小配置文件，然后用 include 来导入
    include       mime.types;
}
```

* ③ \# 用来添加注释，\$ 用来引用 Nginx 内置的变量

```yaml
# 定义一个名为 main 的日志格式，具体的日志格式为
#
# 示例：192.168.1.10 - (空) [18/Mar/2026:18:30:12 +0800] "GET /api/user HTTP/1.1" 200 532 "https://google.com" "Mozilla/5.0..." "1.2.3.4"
#
# $remote_addr：客户端的 IP 地址（谁访问了你）
# $remote_user：认证用户名（一般为空，除非用了 HTTP Basic Auth）
# [$time_local]：请求发生的时间（服务器本地时间）
# $request：完整的 HTTP 请求行
# $status：HTTP 响应状态码
# $body_bytes_sent：返回给客户端的字节数（不含 header）
# "$http_referer"：用户从哪个页面跳过来的
# "$http_user_agent"：用户设备信息
# "$http_x_forwarded_for"：经过代理后的真实客户端 IP（可能有多个，用逗号分隔）
log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                  '$status $body_bytes_sent "$http_referer" '
                  '"$http_user_agent" "$http_x_forwarded_for"';
```

## 二、Nginx 作为 Web 服务器、静态资源服务器时的配置

使用 Vue、React、Angular 等前端框架开发的 Web 项目，在打包后会生成一些静态资源文件（如 HTML、CSS、JS、图片等），这些静态资源文件通常会被部署到服务器的静态资源目录中，那成千上万的客户该怎么访问我们服务器上的这些静态资源文件呢？答案就是 Nginx，我们只需要在服务器上安装一下 Nginx，Nginx 这个服务器软件就会负责监听来自客户端的请求（通常是 80 或 443 端口），当客户端访问时，Nginx 就会按需主动从静态资源目录中读取相应的文件返回给客户端。

#### 1、服务器上只部署一个 web 项目时的 Nginx 配置

* 找到 etc 文件夹下的 nginx 文件夹，找到 nginx.config 文件，打开它

* 找到下面的 server，可见nginx默认监听的是80端口，当nginx监听到80端口有访问时会把root对应的页面给返回来——即“/usr/share/nginx/html”这个页面、这个页面是nginx在usr目录下内置的一个页面，我们当然不是给用户返回这个页面，所以要把它注释掉，然后去location那里配置针对每个项目我们想返回给用户的界面即可

  ```yaml
  
  ```

  

#### 2、服务器上部署多个 web 项目时的 Nginx 配置



## 二、Nginx 配置文件的常见配置

```
events
http
	- upstream1（主要用于配置负载均衡，设置一系列后端服务器）
	- server1（主要用于配置虚拟主机，指定主机和端口。server 跟项目什么关系？一个项目可以部署在多个服务器上，那 server 是跟每个项目有关系，还是跟这多个项目都是一样的“系统”有关）
		- location1（主要用于配置不同情况下该找哪个网页返回给客户端）
    - location2
	- upstream2
	- server2
  	- location1
    - location2
```

* server 命令块配置

1先说多个 server 如何匹配，先找到虚拟主机（可以设置同一端口下的默认 server，不同的端口可以设置不同的默认 server，同一个端口组下如果不设置默认 server，那第一个 server 就是默认 server）先别管 location

2再说虚拟主机里的 location 规则

3再演示真实项目如何配置单项目、多项目

```yaml
# 一个 server 命令块代表一个虚拟主机
# 所谓虚拟主机是指虚拟出来的主机，而不是真实存在的物理主机，也就是说在一台真实存在的物理服务器上虚拟出多台虚拟服务器来
# 比如我们有一台真实存在的物理服务器，它的 ip 为 8.136.43.114
# 我们可以在这台物理服务器上配置多个虚拟服务器，如虚拟服务器 1、服务器 2、服务器 3，它们的 ip 分别为 8.136.43.115、8.136.43.116、8.136.43.117
# 这样一来当用户访问 8.136.43.116 时就会访问到虚拟服务器 1、访问 8.136.43.117 时就会访问到虚拟服务器 2、访问 8.136.43.118 时就会访问到虚拟服务器 3
```

```yaml
# 一个 server 就是一个虚拟主机
server {
		# 当前虚拟主机监听的端口是 80
    listen       80;
    server_name  localhost;

    location / {
        root   html;
        index  index.html index.htm;
    }
    
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   html;
    }
}
```









```yaml

#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

		# 虽然注释了，但默认的日志格式就是这个
    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}
}
```

