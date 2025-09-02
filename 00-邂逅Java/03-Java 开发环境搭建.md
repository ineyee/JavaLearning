## 安装 JDK

要想顺利开发 Java 程序，必须安装 JDK

* JDK 下载地址：https://www.oracle.com/tw/java/technologies/downloads/
* 这里选择下载 JDK 21
* 下载完双击安装即可，macOS 上会默认安装在 Macintosh HD/Library/Java/JavaVirtualMachines
* 在 .bash_profile 里配置一下环境变量：export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home

## 安装 IDE

常见的 Java IDE 有：NetBeans、Eclipse、MyEclipse、Intellij IDEA

企业中用的最多的是：Intellij IDEA、Eclipse，这里选择 Intellij IDEA

* 如果电脑上之前安装过 IDEA，请先卸载干净：https://www.quanxiaoha.com/idea/uninstall-idea.html

* IDEA 下载地址：https://www.jetbrains.com/zh-cn/idea/download/?section=mac

* 这里选择下载 IDEA 2025.2.1

* 下载完双击安装即可，macOS 上会默认安装在应用程序

* 安装完打开 IDEA，语言和地区建议不要选择大陆、选择 Skip Import

* 随便创建一个 Java 项目，就会提示我们是在试用 30 天

* 此时退出 IDEA

* 打开终端，cd 到激活包所在目录（主要不要有中文，可以把激活包放到桌面上）

* 要激活哪款软件就打开对应的激活脚本

  ```shell
  如果你要激活idea： 则输入 bash idea.sh
  如果你要激活pycharm： 则输入 bash pycharm.sh
  如果你要激活datagrip： 则输入 bash datagrip.sh
  如果你要激活clion： 则输入 bash clion.sh
  如果你要激活goland： 则输入 bash goland.sh
  如果你要激活webstorm 则输入 bash webstorm.sh
  如果你要激活phpstorm 则输入 bash phpstorm.sh
  如果你要激活dataspell 则输入 bash dataspell.sh
  如果你要激活rider 则输入 bash rider.sh
  如果显示没权限则在bash 前面加sudo 
  ```

* 显示下面内容则激活成功

  ```
  /Users/xxx/Library/Application Support/JetBrains
  Success! Activate idea to 2099
  ```
  
* 此时打开 IDEA，左下角设置 - Manage Subscriptions... - 可以看到已经激活到 2099/12/31

* 关闭 IDEA 的自动检查更新，**IntelliJ IDEA** - **Settings** - **Appearance & Behavior"** - **"System Settings"** - **Updates"** - **取消勾选**