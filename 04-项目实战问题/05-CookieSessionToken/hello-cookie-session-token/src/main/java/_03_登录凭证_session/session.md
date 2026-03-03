## 一、cookie 存在的问题

正如上一篇所演示，服务端给客户端设置 cookie 后，**cookie 是直接把“userId=123456”这样的敏感用户数据存储在浏览器里，**而浏览器相对于服务器来说更容易被攻击，所以用户数据一旦被截获，攻击者就可以修改用户数据，进而访问任意 userId 的账号，也就是说 cookie 直接存储在浏览器里是不安全的

## 二、session 是什么

session 可以在一定程度上解决这个问题，因为**“userId=123456”这样的敏感用户数据改成存储在服务器上了，具体地说是存储在 Java 项目内存里的 session 会话对象里，而浏览器里存储的仅仅是服务器上某个 session 会话对象的 id**，我们根本无法从 sessionId 推敲出其对应的用户数据，所以就算攻击者截获并修改了 sessionId，服务器上却找不到对应的 session、访问就会直接失效。**但其实 session 就是基于 cookie 实现的，具体地说就是 cookieName = JSESSIONID、cookieValue = 服务器上某个 session 会话对象的 id**

## 三、session 是怎么工作的

> 我们可以通过浏览器的【检查】-【Application】-【Storage】-【Cookies】来查看 session

* 客户端走登录接口
* **服务端判断到登录接口走成功后，服务端给客户端设置 session 信息，即 JSESSIONID=某个 session 会话对象的 id，会通过响应头里的“Set-Cookie”字段传递给客户端**
* 浏览器会自动把 session 存储在内存或磁盘中
* 后续客户端走同一域名 + 同一项目下其它接口时，浏览器会自动携带上 session 发起请求，会通过请求头里的“Cookie”字段传递给服务端
* **服务端可以在这些接口里验证 session 来确定用户是否成功登录过，没登录过就返回登录凭证无效，登录过就返回相应的数据**