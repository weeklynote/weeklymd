**对于一份开源的代码，可以通过源代码注释信息来生成帮助文档，类似于JDK帮助文档。**
```xml
请直接clone本仓库文件并查看helper目录下的jd2chm压缩文件
```
**解压之后得到以下工具**
- **htmlhelper**
- **jd2chm**

**step1 . 准备好需要生成Doc的类，使用Java自带的javadoc命令生成html文档。**
- 当你仅仅是为单个类生成文档时，仅仅需要在类所在的路径执行javadoc 类名即可。

- 当你需要批量生成文档时，执行javadoc -d doc -sourcepath D:\doc -subpackages com.doc
```xml
参数解释：
   d 指定API文档的输出目录，以上语句指的当前目录下的doc目录(该目录不存在会创建)
   -sourcepath 源代码的位置，默认是当前路径，以上语句指的是D:\doc目录下
   -subpackages 以递归的方式处理各子包，注意此时类必须要按照包的结构目录存放，否则会创建失败
```
**注意-sourcepath如果存在多个参数使用；隔开，如果-subpackages参数存在多个使用空格分开。**

**step2 . 安装htmlhelper**

   **step3 . 命令行进入html文档目录，将jd2chm.exe复制到html文档所在目录，运行jd2chm输入相应描述就可以成功生成个人chm文档**