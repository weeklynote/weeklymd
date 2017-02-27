## Input/Output Options
```
@filename    '-include filename'的缩写方式。
```
```
-include filename
```
###[filename usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-names.md)
递归的从指定文件读取配置信息。
比如：**java -jar proguard.jar @myconfig.pro**
等价于**java -jar proguard.jar -include myconfig.pro**
```
-basedirectory directoryname
```
###[directoryname usage](http://)
指定当前配置文件中相对路径的基础目录。
```
-injars class_path
```
###[class_path usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/class-paths.md)
指定需要处理的jars(wars/ears/zips/directories)，jars中的文件将会被处理并且被写到output jars。默认情况下，非类文件会直接复制并且不会修改里面的内容。请尤其注意临时文件，尤其是你正在通过目录读取文件。在**[class_path](https://github.com/weeklynote/weeklymd/blob/master/proguard/class-paths.md)**中的内容可以过滤，可以参考**[filters](https://github.com/weeklynote/weeklymd/blob/master/proguard/filters.md)**。为了更好的可读性，建议使用多个**-injars**来指定多个**class_path**。
```
-outjars class_path
```
###[class_path usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/class-paths.md)
指定输出jars(wars/ears/zips/directories)的名字，前面输入的**-injars**选项将会被写入指定的jars中。这样就可以将输入jars的信息收集到相应的输出jars中。另外可以过滤输出内容，可以参考**[filters](https://github.com/weeklynote/weeklymd/blob/master/proguard/filters.md)**。每个处理过的类文件或资源文件会被写到第一个匹配的filter中。你还需要避免输出文件覆盖输入文件。为了更好的可读性，建议使用多个**-outjars**。没有**-outjars**配置时，不会有输出文件。
```
-libraryjars class_path
```
###[class_path usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/class-paths.md)
指定相关必需的 jar(wars/ears/zips/directories)类库。这些文件不会包含在输出的文件中。在这些类库中至少要包含在应用中已使用到的类。类库中的文件如果仅仅是被调用将不会被保留，即使这些文件会起到优化作用。在**[class_path](https://github.com/weeklynote/weeklymd/blob/master/proguard/class-paths.md)**中的内容可以过滤，可以参考**[filters](http://)**。为了更好的可读性，建议使用多个**-libraryjars**来指定多个**class_path**。
请注意启动路径与执行Proguard的类路径并没有包括在内，这意味着你需要显示的指明运行时jar，这样可以允许用户自己定义不同的运行环境。
```
-skipnonpubliclibraryclasses
```
指定读取类库时跳过非公有类，该操作可以加快处理并减少ProGuard的内存使用。默认情况下 ProGuard会读取公共类和非公共类。然而，大多时候非公共类不会影响jars的程序代码。如果它不会影响到jars的文件，建议使用该选项。但是不幸的是，一些类库(即使像JSE的运行库)包含了一些被继承的非公共库，继承者是公共类，此时不能使用这个选项，如果使用该选项导致找不到类，ProGuard 会输出相应的警告信息。
```
-dontskipnonpubliclibraryclasses
```
指定不忽略非公共类，4.5之后，默认开启此设置。
```
-dontskipnonpubliclibraryclassmembers
```
指定不忽略包内可见的类的成员(字段及方法)。默认情况下，ProGuard解析类库时会跳过这些类成员，因为类一般不会使用它们。然而有时同一个包下的类使用了这些包内可见成员。在这种情况下，为了保持程序的一致性，读取这些类成员是有用的。
```
-keepdirectories [directory_filter]
```
###[directory_filter usage](http://)
指定要保留的输出jars目录。默认情况下，目录会被移除。这会减少输出文件的大小，但如果你的代码使用如下代码片段时可能会导致程序不符合预期。
例如：MyCalss.class.getResource("")
如果没有指定过滤器，所有目录会被保留。使用了过滤器则只会保留匹配的目录。
```
-target version
```
指定处理类文件的版本号。版本号可以是 1.0、1.1、1.2、1.3、1.4、1.5(5)、1.6(6)、1.7(7)、1.8(8)。默认情况下，类文件的版本号保持不变。
```
-forceprocessing
```
指定处理输入,即使输出看起来一致。现代化程度测试是基于一个指定的日期戳的比较输入,输出,和配置文件或目录。










































