## 一、cookie 是什么

cookie 是某些网站为了辨别用户身份而存储在用户客户端（这里的客户端尤指浏览器）上的数据，cookie 可以分为：
* 内存 cookie：没有设置过期时间的 cookie 就是内存 cookie，内存 cookie 会在关闭浏览器时自动清除
* 磁盘 cookie：设置了过期时间的 cookie 就是磁盘 cookie，磁盘 cookie 会在到了过期时间时或者手动删除时清除

## 二、cookie 是怎么工作的

> 我们可以通过浏览器的【检查】-【Application】-【Storage】-【Cookies】来查看 cookie

* 客户端走登录接口
* **服务端判断到登录接口走成功后，服务端给客户端设置 cookie，cookie 会通过响应头里的“Set-Cookie”字段传递给客户端**
* 浏览器会自动把 cookie 存储在内存或磁盘中
* 后续客户端走同一域名 + 同一项目下其它接口时，浏览器会自动携带上 cookie 发起请求，cookie 会通过请求头里的“Cookie”字段传递给服务端
* **服务端可以在这些接口里验证 cookie 来确定用户是否成功登录过，没登录过就返回登录凭证无效，登录过就返回相应的数据**