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

* ② \# 用来添加注释，\$ 用来引用 Nginx 内置的变量

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

## 二、Nginx 配置文件的常见指令

> #### 回顾下一个 HTTP 请求的完整访问流程
>
> * ① URL
>
> http://sixpence.com 是 baseUrl，/ 是根路径，http://sixpence.com/ 是访问根路径
>
> 当我们直接访问 http://sixpence.com 时会被自动在尾部加上 / 来访问根路径，所以跟我们显式写 http://sixpence.com/ 是一样的效果，但是注意在尾部自动加 / 这个行为仅限于访问根路径缺失时，只有后面有路径了就不会再自动加了
>
> * ② IP 地址
>
> IP 地址是服务器的唯一标识，也就是说 IP 地址和服务器是一对一的关系，我们可以通过 IP 地址找到唯一一台与之对应的服务器。比如我们在阿里云购买了三台云服务器，服务器 1 的 IP 地址是 8.136.43.114、服务器 2 的 IP 地址是 8.136.43.115、服务器 3 的 IP 地址是 8.136.43.116
>
> * ③ 域名
>
> 但是 IP 地址太难记了，所以我们一般都会给 IP 地址配个见名知意的别名来方便大家使用，这个别名其实就是域名。比如我们在阿里云购买了三个域名 sixpence.com、sixpence.cn、sixpence.us
>
> 注意：我们在阿里云购买域名这一步的时候，阿里云其实就会自动把 sixpence.com --> ns1.alidns.com + ns2.alidns.com 这样的映射信息注册到 .com 顶级域名服务器（意思就是告诉 .com 顶级域名服务器将来 sixpence.com 这个域名的解析权交给 alidns）、sixpence.cn --> ns1.alidns.com + ns2.alidns.com 这样的映射信息注册到 .cn 顶级域名服务器、 sixpence.us --> ns1.alidns.com + ns2.alidns.com 这样的映射信息注册到 .us 顶级域名服务器
>
> * ④ DNS（Domain Name System，域名系统）
>
> 当我们购买完 IP 地址和域名后，接下来就可以去阿里云 DNS 配置一下 IP 地址和域名的对应关系了。不过需要知道的是 **IP 地址和域名不是一对一的关系，而是多对多的关系，也就是说一个 IP 地址可以对应多个域名（即通过多个不同的域名访问时，DNS 解析出来的是同一个 IP 地址、访问的是同一台服务器，这个一般用来实现一台服务器上部署多个前端项目或后端项目，这种情况必须使用 Nginx 来配置多个虚拟主机），一个域名也可以对应多个 IP 地址（即通过同一个域名访问时，DNS 解析出来的是多个不同的 IP 地址、可以分流到不同的服务器访问，这个一般用来实现负载均衡，不过这个负载均衡方案是 DNS 负载均衡，跟使用 Nginx 实现负载均衡是不同的方案，实际开发中我们更多会使用 Nginx 负载均衡）**。比如我们这里配置 sixpence.com、sixpence.cn => 8.136.43.114，sixpence.us => 8.136.43.115、8.136.43.116
>
> * ⑤ 端口
>
> IP 地址或域名只能保证我们顺利找到服务器，但是怎么处理我们的请求就得由监听相应端口的服务器软件来决定了，如果我们的服务器上没有安装或没有启动相应的软件来监听相应的端口，那么通过某个端口请求时服务器必然是响应“50x，无法处理此请求”。比如 Nginx 默认监听着 80 端口、Tomcat 默认监听着 8080 端口
>
> 注意：一个端口只能被一个运行中的服务器软件监听，比如我们启动 Nginx 后它默认已经监听了 80 端口，此时如果我们启动 Tomcat 后也设置它监听 80 端口就会直接报错 Address already in use
>
> * ⑥ 协议
>
> http 协议采用明文传输数据，https 协议采用密文传输数据，用 http 协议发起的请求默认访问的是 80 端口，用 https 协议发起的请求默认访问的是 443 端口，也就是说就算我们不写端口也会被自动补上。比如 http://sixpence.com/ 和 http://sixpence.com:80/ 是完全一样的效果、https://sixpence.com/ 和 https://sixpence.com:443/ 是完全一样的效果，当然我们也可以强制访问某一端口 http://sixpence.com:8888/、https://sixpence.com:9999/
>
> * ⑦ 路径
>
> 路径用来匹配具体的资源。比如 http://sixpence.com/tp/login.html 中的路径“/tp/login.html”就是访问前端 tp 项目的登录页面，http://sixpence.com/tp/user/login 中的路径“tp/user/login”就是访问后端 tp 项目的登录接口
>
> * ⑧ 一个 HTTP 请求的完整访问流程
>
> 1️⃣ 客户端输入 http://sixpence.com/tp/login.html
>
> 2️⃣ 全球根域名服务器接收到这个请求，它说：你应该去问 .com 顶级域名服务器
>
> 3️⃣ .com 顶级域名服务器接收到这个请求，它说：你应该去问 alidns 服务器
>
> 4️⃣ alidns 服务器接收到这个请求，它说：我能解析 sixpence.com 这个域名，它对应的 IP 地址是 8.136.43.114
>
> 5️⃣ 8.136.43.114 这台服务器收到这个请求，看了一下端口是 80，而 Nginx 刚好在监听 80 端口，于是这个请求就交给 Nginx 去处理了
>
> 6️⃣ Nginx 就会根据“端口+主机名”匹配到相应的虚拟主机，然后再根据路径 /tp/login.html 匹配到相应的 location，然后就会把该 location 里对应的静态资源返回给客户端了

#### 1、user 指令

user 指令一般有两个取值：root、nginx（推荐）

如果我们把 Web 项目部署在 /root/${项目名} 下，那就只能用 root，因为只有 root 用户才有权限访问 /root 目录下的东西

如果我们把 Web 项目部署在 /var/www/${项目名} 下，那就可以使用 nginx，但是需要执行下面的命令授予 nginx 用户访问我们项目的权限：chown -R nginx:nginx /var/www/${项目名}

```yaml
# 代表 Nginx 有 root 用户权限，可以读取服务器上的任何资源
# 实际开发中不推荐，因为万一 Nginx 有漏洞被攻击者利用，攻击者直接拿到 root 权限，整台服务器沦陷
user  root;

# 代表 Nginx 仅有 nginx 用户权限，只可以读取 nginx 用户有权限访问的资源
# 实际开发中默认，推荐
user  nginx;
```

#### 2、worker_processes 指令

worker_processes 指令用来指定 worker 进程数，通常设置为 CPU 核数来发挥最大性能

```yaml
# 推荐根据当前系统的 CPU 核数自动设定
worker_processes auto;

# 也可以手动指定
worker_processes  2;
```

#### 3、Nginx 本身的日志：error_log 指令

这个日志是 Nginx 本身的日志，包含 Nginx 的启动日志、重载日志、崩溃异常日志等

```yaml
# 日志的存放路径为 /var/log/nginx/error.log 这个路径，日志的级别为 notice
error_log  /var/log/nginx/error.log notice;
```

#### 4、Nginx PID 指令：pid 指令

```yaml
# pid 的存放文件为 /var/run/nginx.pid，方便进行进程管理
pid        /var/run/nginx.pid;
```

#### 7、worker_connections 指令

worker_connections 指令用来指定一个 worker 进程同一时间最多能处理多少个请求

```yaml
events {
    worker_connections  1024;
}
```

#### 6、include 指令

include 指令用来导入其它配置文件，它的值可以是其它配置文件的相对路径或绝对路径

```yaml
http {
		# 如果觉得一个配置文件里的内容太多，可以拆分成多个小配置文件，然后用 include 来导入
    include       mime.types;
}
```

#### 7、HTTP 请求的日志指令：log_format 指令、access_log 指令

这个日志只包含 HTTP 请求的日志，不是 Nginx 本身的日志

```yaml
http {
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

		# HTTP 请求的日志的存放路径为 /var/log/nginx/access.log 这个路径，日志的格式为 main
    access_log  /var/log/nginx/access.log  main;
}
```

#### 8、server 指令块

###### 8.1 server 的匹配规则

一个 server 命令块就是一台虚拟主机，所谓虚拟主机就是指虚拟出来的服务器，不是真实存在的物理服务器，也就是说是在一台真实存在的物理服务器上虚拟出多台虚拟服务器来。那么当客户端发起 HTTP 请求时，该由哪个虚拟主机处理呢？server 的匹配规则分两步：

* 第一步：先看端口
* 第二步：再看主机名

###### 8.2 server 里面的指令

```yaml
http {
    # 一台虚拟主机
    server {
        # 当前虚拟主机监听的端口是 80
        # 
        # 第一步：先看端口
        # 如果 HTTP 请求的端口不是 80，那就意味着肯定不是找当前虚拟主机，就会去找别的虚拟主机
        # 如果 HTTP 请求的端口是 80，那还得看看主机名是否匹配
        listen       80;
        # 当前虚拟主机的主机名是 localhost
        # 所有 HTTP/1.1 请求头里必然包含 Host 字段，HTTP/2、HTTP/3 请求头里改为一个伪头字段 :authority = Host
        # 
        # 第二步：再看主机名
        # 如果 HTTP 请求头里的 Host 字段不是 localhost，那就意味着肯定不是找当前虚拟主机，就会去找别的虚拟主机
        # 如果 HTTP 请求头里的 Host 字段是 localhost，那就意味着就是找当前虚拟主机，匹配成功
        server_name  localhost;

				# 成功匹配到当前虚拟主机后，接下来就是做路径匹配看看具体怎么处理 HTTP 请求了，详见 location 命令块的常见配置
        location / {
            root   html;
            index  index.html index.htm;
        }
    }
    
    # 另外一台虚拟主机
    server {
        listen       80;
        server_name  sixpence.com;

        location / {
            root   html;
            index  index.html index.htm;
        }
    }
}
```

#### 9、location 指令块

###### 9.1 location 的匹配规则

|             类型              |      写法实例      |                          匹配规则                           |                         优先级/说明                          |
| :---------------------------: | :----------------: | :---------------------------------------------------------: | :----------------------------------------------------------: |
|        **精确匹配 =**         | location = xxx {}  |       请求的路径部分必须只能是 xxx<br />才能匹配成功        |    优先级最高<br />命中后立即停止，不再匹配其它 location     |
|        **前缀匹配 ^~**        | location ^~ xxx {} |       请求的路径部分只要以 xxx 开头<br />就能匹配成功       |                          优先级第二                          |
|  **正则匹配，区分大小写 ~**   | location ~ xxx {}  | 请求的路径部分只要满足 xxx 这个正则表达式<br />就能匹配成功 |                          优先级第三                          |
| **正则匹配，不区分大小写 ~*** | location ~* xxx {} | 请求的路径部分只要满足 xxx 这个正则表达式<br />就能匹配成功 |                          优先级第四                          |
|   **通用匹配或兜底匹配 /**    |   location / {}    |                 所有的请求路径都能匹配成功                  | 优先级最低<br />当其它匹配都没有命中时，才会命中这个兜底匹配 |

假设我们依次编写了如下 location：

```yaml
server {
		# 1、这是一个精确匹配，请求的路径部分必须只能是 /（注意这里有个等号，不是兜底匹配）
    location = / {}

    # 2、这是一个精确匹配，请求的路径部分必须只能是 /login
    location = /login {}

    # 3、这是一个前缀匹配，请求的路径部分只要以 /static/ 开头就行
    location ^~ /static/ {}

    # 4、这是一个区分大小写的正则匹配，请求的路径部分只要以 .(gif|jpg|png|js|css) 任意一个做为后缀就行
    location ~ \.(gif|jpg|png|js|css)$ {}

    # 5、这是一个不区分大小写的正则匹配，请求的路径部分只要以 .png 或.PNG 做为后缀就行
    location ~* \.png$ {}

    # 6、这是一个兜底匹配，所有的请求路径都能匹配成功，当然得基于其它匹配都没有命中
    location / {}
}
```

然后又产生了如下请求：

```yaml
# 会命中 location1
# 因为这个请求的路径部分是 /
http://localhost/

# 会命中 location2
# 因为这个请求的路径部分是 /login
http://localhost/login

# 会命中 location6
# 因为这个请求的路径部分是 /register，无法命中任何其它 location
http://localhost/register

# 会命中 location3
# 因为这个请求的路径部分是 /static/a.html，以 /static/ 为前缀
http://localhost/static/a.html

# 会命中 location4
# 因为这个请求的路径部分是 /a.gif，以 .gif 为后缀
http://localhost/a.gif

# 会命中 location4
# 因为这个请求的路径部分是 /b.jpg，以 .jpg 为后缀
http://localhost/b.jpg

# 会命中 location3
# 因为这个请求的路径部分是 /static/c.png，以 /static/ 为前缀。虽然 location4 也能满足，但是 location4 的优先级低于 location3
http://localhost/static/c.png

# 会命中 location5
# 因为这个请求的路径部分是 /a.PNG，以 a.PNG 为后缀。location4 区分大小写，location5 不区分大小写
http://localhost/a.PNG

# 会命中 location6
# 因为这个请求的路径部分是 /a.html，无法命中任何其它 location
http://localhost/a.html
```

###### 9.2 location 里面的指令

* ① 作为 Web 服务器、静态资源服务器时的常用指令：root 指令、index 指令、try_files 指令，用来返回静态资源

```yaml
server {
    listen       80;
    server_name  sixpence.com;
		
    location / {
    		# root 指令：用来指定静态资源文件所在的绝对路径目录，Nginx 会用【这个目录 root + 请求的路径部分】去找资源
    		# 比如请求是 http://sixpence.com/test.html，那 Nginx 就会去 html/test.html 这个位置去找这个 html，找到就直接返回静态资源，找不到就尝试 index 那条路
    		# 又比如请求 http://sixpence.com/img/a.png，那 Nginx 就会去 html/img/a.png 这个位置去找这个 png，找到就直接返回静态资源，找不到就尝试 index 那条路
        root   html;
        
        # index 指令：这个指令是以防万一客户端发过来的【请求是一个目录】而不是一个静态资源文件这种情况，那 Nginx 就会用【root + 请求的路径部分 + index】去找资源、总不能返回个目录吧
        # 比如请求是 http://sixpence.com/，那 Nginx 就会去 html/index.html 或 html/index.htm 这个位置去找，找到就直接返回静态资源，找不到就返回兜底资源
        # 又比如请求是 http://sixpence.com/admin/，那 Nginx 就会去 html/admin/index.html 或 html/admin/index.htm 这个位置去找，找到就直接返回静态资源，找不到就返回兜底资源
        index  index.html index.htm;
        
        # try_files 指令：用来告诉 Nginx 怎么找资源
        # 优先把请求的路径部分当成【文件】看待，这样一来 Nginx 就会直接用【root + 请求的路径部分】去找资源
        # 上面找不到的话，就把请求的路径部分当成【目录】看待，这样一来 Nginx 就会用兜底的【root + 请求的路径部分 + index】去找资源
        # 上面还找不到的话，就直接返回 /html/index.html 这个路径所对应的资源（这个兜底资源可以根据实际情况修改成我们服务器上肯定存在的一个资源、但要注意这个路径是相对于 root 的路径，从而确保肯定不会给用户返回 404 Not Found 的页面）
        try_files $uri $uri/ /index.html;
    }
}
```

* ② 作为反向代理服务器时的常用指令：proxy_pass 指令，用来转发到另外一台服务器

```yaml
server {
    listen       80;
    server_name  sixpence.com;
    
    location /api/ {
    		# proxy_pass 指令：用来把请求转发给另外一台服务器处理，另一台服务器处理完成后会把结果再交由这里返回给客户端
    		# proxy_pass 后面一般跟【协议 + 域名 + 端口 + /】（建议尾部有路径），出于某些原因一般不用域名
    		#
    		# proxy_pass 截断路径和拼接路径的规则：
    		# 只要 proxy_pass 尾部有路径，不管是根路径 / 也好、还是其它长路径也好，那么在截断原请求路径的时候，都会从路径里剔除掉 location 前缀，然后再拼接在 proxy_pass 后面形成最终的转发路径
    		# 比如请求是 http://sixpence.com/api/tp/user/login，请求路径就是 /api/tp/user/login，因为 proxy_pass 尾部有根路径 /，所以最终的截断路径就是剔除 location 的前缀 /api/ 后的 tp/user/login，然后在拼接在 proxy_pass 后面，所以最终的转发路径是 http://8.136.43.120:8888/tp/user/login
    		proxy_pass http://8.136.43.120:8888/;
    }
}
```

#### 10、upstream 指令块

upstream 指令块用来定义一组服务器，主要用于配置负载均衡，必须配合 proxy_pass 指令使用

```yaml
# backend 是这个服务器组的名字
upstream backend {
		server 8.136.43.120:8888;
		server 8.136.43.121:8888;
		server 8.136.43.122:8888;
}

server {
    listen       80;
    server_name  sixpence.com;
    
    location /api/ {
    		# proxy_pass 指令：用来把请求转发给另外一台服务器处理，另一台服务器处理完成后会把结果再交由这里返回给客户端
    		# proxy_pass 后面还可以跟【协议 + 服务器组名 + /】，指向一个服务器组，Nginx 会根据负载均衡策略选中其中一台服务器
    		#
    		# proxy_pass 截断路径和拼接路径的规则：
    		# 只要 proxy_pass 尾部有路径，不管是根路径 / 也好、还是其它长路径也好，那么在截断原请求路径的时候，都会从路径里剔除掉 location 前缀，然后再拼接在 proxy_pass 后面形成最终的转发路径
    		# 比如请求是 http://sixpence.com/api/tp/user/login，请求路径就是 /api/tp/user/login，因为 proxy_pass 尾部有根路径 /，所以最终的截断路径就是剔除 location 的前缀 /api/ 后的 tp/user/login，然后在拼接在 proxy_pass 后面，假设 Nginx 给我们选中了 8.136.43.120:8888，所以最终的转发路径是 http://8.136.43.120:8888/tp/user/login
    		proxy_pass http://backend/;
    }
}
```

## 三、Nginx 作为 Web 服务器、静态资源服务器时的实际配置

> **如果只需要部署 Web 项目，那我们只需要购买一台云服务器，安装上 Nginx。把 Web 项目搞到云服务器上，配置下 Nginx 即可**

使用 Vue、React、Angular 等前端框架开发的 Web 项目，在打包后会生成一些静态资源文件（如 HTML、CSS、JS、图片等），这些静态资源文件通常会被部署到服务器的静态资源目录中，那成千上万的客户该怎么访问我们服务器上的这些静态资源文件呢？答案就是 Nginx，我们只需要在服务器上安装一下 Nginx，Nginx 这个服务器软件就会负责监听来自客户端的请求（通常是 80 或 443 端口），当客户端访问时，Nginx 就会按需主动从静态资源目录中读取相应的文件返回给客户端。

![image-20260316211749569](img/image-20260316211749569.png)

#### 1、同一台服务器上只部署一个 Web 项目时的 Nginx 配置

* 如果用 user  nginx; 的话，记得授予 nginx 用户访问我们项目的权限：chown -R nginx:nginx /var/www/fun-trip

* 找到 etc 文件夹下的 nginx 文件夹，找到 nginx.config 文件，打开它

* 在 http 命令块里面添加一个 server 虚拟主机

  ```yaml
  http {
  		include /etc/nginx/conf.d/*.conf;
      server {
          # 监听 80 端口就行
          # 因为用户访问我们网站输入 url 的时候，没人知道也没人习惯要在 ip 地址或域名后面再加个端口
          # 所以实际使用中都是用户只输入 ip 地址或域名，让 http 请求自动补 80，那就刚好能匹配上这里监听的端口了
          # 但是如果我们这里贸然改成别的端口，那用户访问时就得手动加端口才能匹配上了
          listen 80;
          # 我们这里设置当前虚拟主机的主机名为当前云服务器的 ip 地址
          # 因为我们现在就这么一台云服务器，肯定是用它来处理客户端的请求
          # 所以我们告诉用户的也是这个 ip 地址，用户访问我们网站输入 url 的时候输入的就是这个 ip 地址
          # 当然实际开发中我们一般都会给当前云服务器的 ip 地址搞个域名，这里就会填上域名而不再是 ip 地址
          # 这样一来我们告诉用户的就是域名了而不再是 ip 地址了，用户访问我们网站输入 url 的时候输入域名比输入 ip 地址要爽多了
          server_name  8.136.43.114;
  
          # 直接搞个兜底匹配就行
          # 
          # 1、当用户访问 http://8.136.43.114、http://8.136.43.114/ 的时候
          # 这访问的是个目录，Nginx 会用【root + 请求的路径部分 + index】去找资源
          # 即 /var/www/fun-trip/index.html，刚好返回 fun-trip 项目的主页
          # 
          # 2、当用户访问其它文件或目录时，找到就找到了，找不到就兜底返回 fun-trip 项目的主页
          location / {
              # fun-trip 项目部署在 /var/www/fun-trip 目录
              root /var/www/fun-trip;
              # fun-trip 项目的主页在 fun-trip 目录下
              index index.html;
              # 请求的路径优先视作文件、然后视作目录，实在找不到就返回兜底资源
              try_files $uri $uri/ /index.html;
          }
      }
  }
  ```

* 去访问 http://8.136.43.114/ 试试效果吧

#### 2、同一台服务器上部署多个 Web 项目时的 Nginx 配置

假设我们有三个 Web 项目，fun-person、fun-car、fun-house

**为了避免 /etc/nginx/nginx.config 这个主配置文件无限扩大，实际开发中我们一般都会为每个虚拟主机单独创建成一个配置文件，这些配置文件都放在 /etc/nginx/conf.d 这个目录下，我们可以看到主配置文件里已经默认 include 导入了这个目录下的所有配置文件**

###### 2.1 同一台虚拟主机 + 不同路径法（不推荐，用户访问时得输入不同项目对应的路径）

* 如果用 user  nginx; 的话，记得授予 nginx 用户访问我们项目的权限：chown -R nginx:nginx /var/www/fun-person、chown -R nginx:nginx /var/www/fun-car、chown -R nginx:nginx /var/www/fun-house
* 创建一个配置文件 /etc/nginx/conf.d/fun-all.conf，添加一个 server 虚拟主机

```yaml
# 三个项目都会匹配到这同一个虚拟主机
server {
    # 监听 80 端口就行
    # 因为用户访问我们网站输入 url 的时候，没人知道也没人习惯要在 ip 地址或域名后面再加个端口
    # 所以实际使用中都是用户只输入 ip 地址或域名，让 http 请求自动补 80，那就刚好能匹配上这里监听的端口了
    # 但是如果我们这里贸然改成别的端口，那用户访问时就得手动加端口才能匹配上了
    listen 80;
    # 我们这里设置当前虚拟主机的主机名为当前云服务器的 ip 地址
    # 因为我们现在就这么一台云服务器，肯定是用它来处理客户端的请求
    # 所以我们告诉用户的也是这个 ip 地址，用户访问我们网站输入 url 的时候输入的就是这个 ip 地址
    # 当然实际开发中我们一般都会给当前云服务器的 ip 地址搞个域名，这里就会填上域名而不再是 ip 地址
    # 这样一来我们告诉用户的就是域名了而不再是 ip 地址了，用户访问我们网站输入 url 的时候输入域名比输入 ip 地址要爽多了
    server_name  8.136.43.114;

    # 一个项目对应一个 location，搞一个前缀匹配
    #
    # 1、当用户访问 http://8.136.43.114/fun-person 的时候
    # 这访问的是个目录，Nginx 会用【root + 请求的路径部分 + index】去找资源
    # 即 /var/www/fun-person/index.html，刚好返回 fun-person 项目的主页
    #
    # 2、当用户访问其它文件或目录时，找到就找到了，找不到就兜底返回 fun-person 项目的主页
    location ^~ /fun-person {
        # fun-person 项目部署在 /var/www/fun-person 目录
        root /var/www;
        # fun-person 项目的主页在 fun-person 目录下
        index index.html;
        # 请求的路径优先视作文件、然后视作目录，实在找不到就返回兜底资源
        try_files $uri $uri/ /index.html;
    }

    # 一个项目对应一个 location，搞一个前缀匹配
    location ^~ /fun-car {
        root /var/www;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 一个项目对应一个 location，搞一个前缀匹配
    location ^~ /fun-house {
        root /var/www;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
}
```

###### 2.2 多台虚拟主机 + 不同端口法（不推荐，用户访问时得输入不同项目对应的端口）

* 如果用 user  nginx; 的话，记得授予 nginx 用户访问我们项目的权限：chown -R nginx:nginx /var/www/fun-person、chown -R nginx:nginx /var/www/fun-car、chown -R nginx:nginx /var/www/fun-house
* 创建三个配置文件 /etc/nginx/conf.d/fun-person.conf、/etc/nginx/conf.d/fun-car.conf、/etc/nginx/conf.d/fun-house.conf，为每个项目都添加一个 server 虚拟主机

```yaml
# 为每个项目都添加一个 server 虚拟主机
server {
    # 81 端口负责处理 fun-person 项目，网络与安全组里记得添加这个端口
    listen 81;
    # 我们还是只有一台云服务器，三个项目都还是访问当前云服务器
    server_name  8.136.43.114;

    # 直接搞个兜底匹配就行
    #
    # 1、当用户访问 http://8.136.43.114:8181、http://8.136.43.114:8181/ 的时候
    # 这访问的是个目录，Nginx 会用【root + 请求的路径部分 + index】去找资源
    # 即 /var/www/fun-person/index.html，刚好返回 fun-person 项目的主页
    # 
    # 2、当用户访问其它文件或目录时，找到就找到了，找不到就兜底返回 fun-person 项目的主页
    location / {
        # fun-person 项目部署在 /var/www/fun-person 目录
        root /var/www/fun-person;
        # fun-person 项目的主页在 fun-person 目录下
        index index.html;
        # 请求的路径优先视作文件、然后视作目录，实在找不到就返回兜底资源
        try_files $uri $uri/ /index.html;
    }
}
```

```yaml
# 为每个项目都添加一个 server 虚拟主机
server {
    # 82 端口负责处理 fun-car 项目，网络与安全组里记得添加这个端口
    listen 82;
    # 我们还是只有一台云服务器，三个项目都还是访问当前云服务器
    server_name  8.136.43.114;

    # 直接搞个兜底匹配就行
    #
    # 1、当用户访问 http://8.136.43.114:81、http://8.136.43.114:81/ 的时候
    # 这访问的是个目录，Nginx 会用【root + 请求的路径部分 + index】去找资源
    # 即 /var/www/fun-car/index.html，刚好返回 fun-car 项目的主页
    # 
    # 2、当用户访问其它文件或目录时，找到就找到了，找不到就兜底返回 fun-car 项目的主页
    location / {
        # fun-car 项目部署在 /var/www/fun-car 目录
        root /var/www/fun-car;
        # fun-car 项目的主页在 fun-car 目录下
        index index.html;
        # 请求的路径优先视作文件、然后视作目录，实在找不到就返回兜底资源
        try_files $uri $uri/ /index.html;
    }
}
```

```yaml
# 为每个项目都添加一个 server 虚拟主机
server {
    # 83 端口负责处理 fun-house 项目，网络与安全组里记得添加这个端口
    listen 83;
    # 我们还是只有一台云服务器，三个项目都还是访问当前云服务器
    server_name  8.136.43.114;

    # 直接搞个兜底匹配就行
    #
    # 1、当用户访问 http://8.136.43.114:81、http://8.136.43.114:81/ 的时候
    # 这访问的是个目录，Nginx 会用【root + 请求的路径部分 + index】去找资源
    # 即 /var/www/fun-house/index.html，刚好返回 fun-house 项目的主页
    # 
    # 2、当用户访问其它文件或目录时，找到就找到了，找不到就兜底返回 fun-house 项目的主页
    location / {
        # fun-house 项目部署在 /var/www/fun-house 目录
        root /var/www/fun-house;
        # fun-house 项目的主页在 fun-house 目录下
        index index.html;
        # 请求的路径优先视作文件、然后视作目录，实在找不到就返回兜底资源
        try_files $uri $uri/ /index.html;
    }
}
```

###### 2.3 多台虚拟主机 + 不同主机名法（推荐，用户访问时直接输入不同项目对应的域名即可，符合我们的日常习惯）

* 如果用 user  nginx; 的话，记得授予 nginx 用户访问我们项目的权限：chown -R nginx:nginx /var/www/fun-person、chown -R nginx:nginx /var/www/fun-car、chown -R nginx:nginx /var/www/fun-house
* 创建三个配置文件 /etc/nginx/conf.d/fun-person.conf、/etc/nginx/conf.d/fun-car.conf、/etc/nginx/conf.d/fun-house.conf，为每个项目都添加一个 server 虚拟主机

```yaml
# 为每个项目都添加一个 server 虚拟主机
server {
    # 监听 80 端口就行
    # 因为用户访问我们网站输入 url 的时候，没人知道也没人习惯要在 ip 地址或域名后面再加个端口
    # 所以实际使用中都是用户只输入 ip 地址或域名，让 http 请求自动补 80，那就刚好能匹配上这里监听的端口了
    # 但是如果我们这里贸然改成别的端口，那用户访问时就得手动加端口才能匹配上了
    listen 80;
    # 我们还是只有一台云服务器（8.136.43.114），三个项目都还是访问当前云服务器
    # 但是我们知道一个 IP 地址可以对应多个域名，所以我们可以：一台服务器 + 三域名
    # 所以我们可以为 fun-person 项目单独购买一个域名就叫 fun-person，它对应的 ip 地址是我们当前云服务器 8.136.43.114
    # 这里添 fun-person 域名即可
    # 当然我们也可以只购买一个主域名、如 ineyee.com，然后子域名就是无限免费随便加了、如 fun-person.ineyee.com，做好 DNS 映射即可
    server_name  fun-person;

    # 直接搞个兜底匹配就行
    #
    # 1、当用户访问 http://fun-person、http://fun-person/ 的时候
    # 这访问的是个目录，Nginx 会用【root + 请求的路径部分 + index】去找资源
    # 即 /var/www/fun-person/index.html，刚好返回 fun-person 项目的主页
    # 
    # 2、当用户访问其它文件或目录时，找到就找到了，找不到就兜底返回 fun-person 项目的主页
    location / {
        # fun-person 项目部署在 /var/www/fun-person 目录
        root /var/www/fun-person;
        # fun-person 项目的主页在 fun-person 目录下
        index index.html;
        # 请求的路径优先视作文件、然后视作目录，实在找不到就返回兜底资源
        try_files $uri $uri/ /index.html;
    }
}
```

```yaml
# 为每个项目都添加一个 server 虚拟主机
server {
    # 监听 80 端口就行
    # 因为用户访问我们网站输入 url 的时候，没人知道也没人习惯要在 ip 地址或域名后面再加个端口
    # 所以实际使用中都是用户只输入 ip 地址或域名，让 http 请求自动补 80，那就刚好能匹配上这里监听的端口了
    # 但是如果我们这里贸然改成别的端口，那用户访问时就得手动加端口才能匹配上了
    listen 80;
    # 我们还是只有一台云服务器（8.136.43.114），三个项目都还是访问当前云服务器
    # 但是我们知道一个 IP 地址可以对应多个域名，所以我们可以：一台服务器 + 三域名
    # 所以我们可以为 fun-car 项目单独购买一个域名就叫 fun-car，它对应的 ip 地址是我们当前云服务器 8.136.43.114
    # 这里添 fun-car 域名即可
    # 当然我们也可以只购买一个主域名、如 ineyee.com，然后子域名就是无限免费随便加了、如 fun-car.ineyee.com，做好 DNS 映射即可
    server_name  fun-car;

    # 直接搞个兜底匹配就行
    #
    # 1、当用户访问 http://fun-car、http://fun-car/ 的时候
    # 这访问的是个目录，Nginx 会用【root + 请求的路径部分 + index】去找资源
    # 即 /var/www/fun-car/index.html，刚好返回 fun-car 项目的主页
    # 
    # 2、当用户访问其它文件或目录时，找到就找到了，找不到就兜底返回 fun-car 项目的主页
    location / {
        # fun-car 项目部署在 /var/www/fun-car 目录
        root /var/www/fun-car;
        # fun-car 项目的主页在 fun-car 目录下
        index index.html;
        # 请求的路径优先视作文件、然后视作目录，实在找不到就返回兜底资源
        try_files $uri $uri/ /index.html;
    }
}
```

```yaml
# 为每个项目都添加一个 server 虚拟主机
server {
    # 监听 80 端口就行
    # 因为用户访问我们网站输入 url 的时候，没人知道也没人习惯要在 ip 地址或域名后面再加个端口
    # 所以实际使用中都是用户只输入 ip 地址或域名，让 http 请求自动补 80，那就刚好能匹配上这里监听的端口了
    # 但是如果我们这里贸然改成别的端口，那用户访问时就得手动加端口才能匹配上了
    listen 80;
    # 我们还是只有一台云服务器（8.136.43.114），三个项目都还是访问当前云服务器
    # 但是我们知道一个 IP 地址可以对应多个域名，所以我们可以：一台服务器 + 三域名
    # 所以我们可以为 fun-house 项目单独购买一个域名就叫 fun-house，它对应的 ip 地址是我们当前云服务器 8.136.43.114
    # 这里添 fun-house 域名即可
    # 当然我们也可以只购买一个主域名、如 ineyee.com，然后子域名就是无限免费随便加了、如 fun-house.ineyee.com，做好 DNS 映射即可
    server_name  fun-house;

    # 直接搞个兜底匹配就行
    #
    # 1、当用户访问 http://fun-house、http://fun-house/ 的时候
    # 这访问的是个目录，Nginx 会用【root + 请求的路径部分 + index】去找资源
    # 即 /var/www/fun-house/index.html，刚好返回 fun-house 项目的主页
    # 
    # 2、当用户访问其它文件或目录时，找到就找到了，找不到就兜底返回 fun-house 项目的主页
    location / {
        # fun-house 项目部署在 /var/www/fun-house 目录
        root /var/www/fun-house;
        # fun-house 项目的主页在 fun-house 目录下
        index index.html;
        # 请求的路径优先视作文件、然后视作目录，实在找不到就返回兜底资源
        try_files $uri $uri/ /index.html;
    }
}
```

## 四、Nginx 作为反向代理服务器时的实际配置

#### 1、Web 项目处理跨域时的 Nginx 配置

>  **同一台云服务器，既做静态资源服务器，也做反向代理服务器**

Nginx 代理服务器转发是什么？既然跨域发生在静态资源服务器上的 js 访问 API 服务器时，那我们完全可以引入一个第三者——一台代理服务器——来解决跨域，让静态资源服务器上的 js 去访问这台代理服务器，然后代理服务器再去访问 API 服务器，前面说过`跨域只发生在使用浏览器时，因为只有浏览器有同源策略`，所以代理服务器去访问 API 服务器时绝对没有跨域问题的，不过你可能会问了“那静态资源服务器上的 js 去访问代理服务器难道不会跨域吗？”答案是会，所以`我们还需要解决一下静态资源服务器上的 js 去访问代理服务器的跨域问题`，`发布阶段`具体怎么解决看下边

Nginx 代理服务器转发怎么实现？`我们只需要在 nginx.conf 文件里做一下代理服务器的配置 + vue 项目里发起请求时通过 nginx 代理地址发起请求即可`。这样做就可以解决上面提到的`静态资源服务器上的 js 去访问代理服务器的跨域问题`，因为 **nginx 作为云服务器上的一个软件，它现在充当了两个角色：监听 80 端口返回 vue 项目的静态资源、所以我们可以把它视作就是静态资源服务器；监听 80 端口转发请求，所以我们可以把它视作就是代理服务器，也就是说静态资源服务器和代理服务器现在完全就是一台服务器且监听着相同的端口，**类似于我们之前提到的方案一：把静态资源和 API 部署在同一个源下

- 首先在 nginx.conf 中配置代理服务器

```yaml
# ====== nginx.conf，可以去替换试试 ======

server {
		listen 80;
		server_name 当前静态资源服务器的 ip 地址或 client-demo 项目的域名（当然这个域名也是指向当前静态资源服务器的 ip 地址）;

    # 这里是返回静态资源
    location / {
        root /root/client-demo;
        index index.html;
        try_files $uri $uri/ /fun-car/index.html;
    }

    # 这里是 api 转发，/prd-api 后面的 / 不能少，涉及到到路径截取和拼接
    location /prd-api/ {
        # 当前虚拟主机收到以 /prd-api/ 为前缀的请求时，会把请求的 /prd-api/ 前缀截取掉，然后拼接上 proxy_pass 的值，转发给 api 服务器，后面的 / 不能少，涉及到到路径截取和拼接
        proxy_pass http://118.25.70.197:8000/;
    }
}
```

- 然后在发起请求的地方直接用 /prd-api 开头的路径就可以了，同时也不要再加协议和 ip 地址

```js
// ====== App.vue，可以去替换试试 ======

<template>
  <button @click="handleClick">获取动态列表</button>
</template>

<script>
export default {
  name: 'App',
  methods: {
    handleClick() {
      // 开发阶段用 node 代理服务器，接口请求用 node 代理地址
      // 发布阶段用 nginx 代理服务器，接口请求用 nginx 代理地址
      const baseURL = process.env.NODE_ENV === 'production'
        ? '/prd-api'
        : '/dev-api';
      fetch(`${baseURL}/moment/getMomentList?pageSize=10&pageIndex=0`).then((res) => {
        return res.json();
      }).then((data) => {
        console.log("动态列表", data);
      });
    }
  }
}
</script>
```

#### 2、后台项目单纯转发时的 Nginx 配置

> **同一台云服务器，既做 Tomcat 应用服务器，也做反向代理服务器**

假设我们只购买了一台云服务器（8.136.43.114），但是要部署两个 SpringBoot 项目（ssm 和 tp，SpringBoot 内置了 Tomcat、默认监听 8080 端口、当然我们也可以自定义要监听的端口），回想一下我们之前是怎么部署的：

* 把项目打包成一个 runnable jar
* 把 runnable jar 包上传到云服务器
* 启动 runnable jar

接下来客户端就可以访问这两个项目里提供的接口了：

* http://8.136.43.114:8080/ssm/user/list
* http://8.136.43.114:8080/tp/product/list

【8.136.43.114】能保证 HTTP 请求顺利找到我们的云服务器，【8080】能保证 HTTP 请求顺利交给 Tomcat 处理，【/ssm 和 /tp】能保证 HTTP 请求顺利找到不同的项目，【/user/list 和 /product/list】能保证 HTTP 请求顺利找到不同 controller 里的方法

***

上面的流程没有任何问题，但是实际开发中后端给客户端提供的接口更多是域名而非 IP 地址，因为域名有方便记忆、方便迁移、方便升 HTTPS 等优点。所以我们一般都会购买一个主域名，如 ineyee.com，然后再添加两个免费的子域名，如 ssm.ineyee.com、tp.ineyee.com，这两个子域名都指向我们的同一台云服务器（8.136.43.114），这样一来后端给客户端提供的接口就可以变成如下，这才是我们开发中常见接口的样子：

* http://ssm.ineyee.com/user/list
* http://tp.ineyee.com/product/list

但是此时这两个接口能访问得通吗？肯定不行，因为这两个接口的端口变成了 http 请求默认的 80 端口，而 80 端口是 Nginx 在监听，跟我们 Tomcat 监听的 8080 端口一点关系都没有了。因此我们就得在当前云服务器上安装 Nginx，让 Nginx 监听到这两个请求后把它们转发给 Tomcat，接口就能访问得通了，也就是说【当前云服务器，既做 Tomcat 应用服务器，也做反向代理服务器】

**为了避免 /etc/nginx/nginx.config 这个主配置文件无限扩大，实际开发中我们一般都会为每个虚拟主机单独创建成一个配置文件，这些配置文件都放在 /etc/nginx/conf.d 这个目录下，我们可以看到主配置文件里已经默认 include 导入了这个目录下的所有配置文件**

* 如果用 user  nginx; 的话，记得授予 nginx 用户访问我们项目的权限：chown -R nginx:nginx /usr/local/soft/ssm、chown -R nginx:nginx /usr/local/soft/tp
* 创建两个配置文件 /etc/nginx/conf.d/ssm.conf、/etc/nginx/conf.d/tp.conf，为每个项目都添加一个 server 虚拟主机

```yaml
# 为每个项目都添加一个 server 虚拟主机
server {
    # 监听 80 端口
    listen 80;
    # ssm 项目的域名
    server_name ssm.ineyee.com;

    # 直接搞个兜底匹配就行
    #
    # 当用户访问 http://ssm.ineyee.com/user/list 的时候
    # Nginx 会把原请求的请求路径 /user/list 给截取下来
    # 然后再剔除掉 location 前缀 /，最终的截断路径就是 user/list
    # 然后再拼接到 proxy_pass 后面，最终的转发路径就是 http://8.136.43.114:8080/ssm/user/list
    # 就会转发给 Tomcat 处理，因为 Tomcat 在监听 8080
    location / {
        proxy_pass http://8.136.43.114:8080/ssm/;
    }
}
```

```yaml
# 为每个项目都添加一个 server 虚拟主机
server {
    # 监听 80 端口
    listen 80;
    # tp 项目的域名
    server_name tp.ineyee.com;

    # 直接搞个兜底匹配就行
    #
    # 当用户访问 http://tp.ineyee.com/product/list 的时候
    # Nginx 会把原请求的请求路径 /product/list 给截取下来
    # 然后再剔除掉 location 前缀 /，最终的截断路径就是 product/list
    # 然后再拼接到 proxy_pass 后面，最终的转发路径就是 http://8.136.43.114:8080/tp/product/list
    # 就会转发给 Tomcat 处理，因为 Tomcat 在监听 8080
    location / {
        proxy_pass http://8.136.43.114:8080/tp/;
    }
}
```

#### 3、后台项目负载均衡时的 Nginx 配置

> **后台项目 + 负载均衡，我们需要购买 N 台云服务器，一台专门用来做 Nginx 负载均衡服务器，其它做 Tomcat 应用服务器**

反向代理服务器是指代理服务器是给**服务端**做代理，也就是说代理服务器是在给服务端干活，负载均衡服务器就是反向代理服务器。

![image-20260316221848406](img/image-20260316221848406.png)

###### 3.1 Nginx 负载均衡配置

假设随着用户量的飙升，我们上面 ssm 和 tp 项目的单机部署方案扛不住了，我们决定把它们搞成分布式部署方案。于是我们购买了四台云服务器，一台专门用来做 Nginx 负载均衡服务器（8.136.43.114)，另外三台都用来做 Tomcat 应用服务器：服务器 1（8.136.43.115)、服务器 2（8.136.43.116)、服务器 3（8.136.43.117)，我们在这三台应用服务器上都分别部署了 ssm 和 tp 项目，这就意味着客户端通过以下三组接口来访问数据都能跑得通：

* http://8.136.43.115:8080/ssm/user/list、http://8.136.43.115:8080/tp/product/list
* http://8.136.43.116:8080/ssm/user/list、http://8.136.43.116:8080/tp/product/list
* http://8.136.43.117:8080/ssm/user/list、http://8.136.43.117:8080/tp/product/list

***

然后我们又购买了一个主域名，如 ineyee.com，又添加了两个免费的子域名，如 ssm.ineyee.com、tp.ineyee.com，这两个子域名都指向我们的那台负载均衡服务器（8.136.43.114)，这样一来后端给客户端提供的接口就可以变成如下，这才是我们开发中常见接口的样子：

* http://ssm.ineyee.com/user/list
* http://tp.ineyee.com/product/list

但是此时这两个接口能访问得通吗？肯定不行，因为这两个接口的端口变成了 http 请求默认的 80 端口，而 80 端口是 Nginx 在监听，跟我们 Tomcat 监听的 8080 端口一点关系都没有了。因此我们就得在当前负载均衡服务器上配置 Nginx，让 Nginx 监听到这两个请求后把它们转发给 Tomcat，接口就能访问得通了

**为了避免 /etc/nginx/nginx.config 这个主配置文件无限扩大，实际开发中我们一般都会为每个虚拟主机单独创建成一个配置文件，这些配置文件都放在 /etc/nginx/conf.d 这个目录下，我们可以看到主配置文件里已经默认 include 导入了这个目录下的所有配置文件**

* 如果用 user  nginx; 的话，记得授予 nginx 用户访问我们项目的权限：chown -R nginx:nginx /usr/local/soft/ssm、chown -R nginx:nginx /usr/local/soft/tp
* 创建两个配置文件 /etc/nginx/conf.d/ssm.conf、/etc/nginx/conf.d/tp.conf，为每个项目都添加一个 server 虚拟主机

```yaml
# upstream 指令块用来定义一组服务器，主要用于配置负载均衡，必须配合 proxy_pass 指令使用
# backend_ssm 是这个服务器组的名字，里面就是我们三台应用服务器的 ip 地址+端口
upstream backend_ssm {
		server 8.136.43.115:8080;
		server 8.136.43.116:8080;
		server 8.136.43.117:8080;
}

# 为每个项目都添加一个 server 虚拟主机
server {
    # 监听 80 端口
    listen 80;
    # ssm 项目的域名
    server_name ssm.ineyee.com;

    # 直接搞个兜底匹配就行
    #
    # 当用户访问 http://ssm.ineyee.com/user/list 的时候
    # Nginx 会把原请求的请求路径 /user/list 给截取下来
    # 然后再剔除掉 location 前缀 /，最终的截断路径就是 user/list
    # 然后再拼接到 proxy_pass 后面，假设 Nginx 给我们选中了 8.136.43.115:8080，最终的转发路径就是 http://8.136.43.115:8080/ssm/user/list
    # 就会转发给 Tomcat 处理，因为 Tomcat 在监听 8080
    location / {
				# proxy_pass 后面还可以跟【协议 + 服务器组名 + /】，指向一个服务器组，Nginx 会根据负载均衡策略选中其中一台服务器
        proxy_pass http://backend_ssm/;
    }
}
```

```yaml
# upstream 指令块用来定义一组服务器，主要用于配置负载均衡，必须配合 proxy_pass 指令使用
# backend_tp 是这个服务器组的名字，里面就是我们三台应用服务器的 ip 地址+端口
upstream backend_tp {
		server 8.136.43.115:8080 weight=1; # 分配到 1/6 的请求
		server 8.136.43.116:8080 weight=2; # 分配到 2/6 的请求
		server 8.136.43.117:8080 weight=3; # 分配到 3/6 的请求
}

# 为每个项目都添加一个 server 虚拟主机
server {
    # 监听 80 端口
    listen 80;
    # tp 项目的域名
    server_name tp.ineyee.com;

    # 直接搞个兜底匹配就行
    #
    # 当用户访问 http://tp.ineyee.com/product/list 的时候
    # Nginx 会把原请求的请求路径 /product/list 给截取下来
    # 然后再剔除掉 location 前缀 /，最终的截断路径就是 product/list
    # 然后再拼接到 proxy_pass 后面，假设 Nginx 给我们选中了 8.136.43.115:8080，最终的转发路径就是 http://8.136.43.115:8080/tp/product/list
    # 就会转发给 Tomcat 处理，因为 Tomcat 在监听 8080
    location / {
				# proxy_pass 后面还可以跟【协议 + 服务器组名 + /】，指向一个服务器组，Nginx 会根据负载均衡策略选中其中一台服务器
        proxy_pass http://backend_tp/;
    }
}
```

###### 3.2 Nginx 负载均衡策略

 实际开发中**轮询和权重**最常见，服务器性能差不多就用轮询，性能有差异就用权重

|  负载均衡策略  |         配置方式         |                             规则                             |      适用场景      |
| :------------: | :----------------------: | :----------------------------------------------------------: | :----------------: |
|    **轮询**    |  默认策略，无需专门配置  | 把请求依次转发给每台服务器<br />比如第一个请求转发给服务器 1<br />第二个请求转发给服务器 2<br />第三个请求转发给服务器 3<br />第四个请求转发给服务器 1<br />...... |   服务器性能相近   |
|    **权重**    | weight=N，默认权重都是 1 | 可以按服务器的性能按权重转发<br />权重越大，转发到该服务器的概率越大<br />比如服务器 1 是 8 核，服务器 2 是 4 核<br />那它俩的权重就可以设计为 2 : 1 |   服务器性能不同   |
| 客户端 ip_hash |         ip_hash          | 会确保同一个客户端的所有请求都转发到同一台服务器<br />比如我们没用 Redis 缓存 session 或 token<br />那如果用别的负载均衡策略的话<br />假如客户端的登录请求是在服务器 1 上完成的<br />那该客户端后续请求很可能被转发到其它服务器<br />其它服务器就会因为没有登录凭证而让客户端重新登录<br />但如果用了这个策略的话<br />就会确保客户端登录后的后续请求肯定全都转发到服务器 1 |  需要保持登录凭证  |
|   最少连接数   |        least_conn        | 优先把请求转发给当前连接数最少的服务器<br />比如请求到来时<br />服务器 1 有 10 个连接<br />服务器 2 有 18 个连接<br />服务器 3 有 6 个连接<br />那就转发给服务器 3 | 请求处理耗时差异大 |

