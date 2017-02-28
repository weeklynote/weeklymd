## Shrinking Options
```
-dontshrink
```
指定不压缩输入的class文件。默认情况是开启压缩；除了列在-keep中的类和类成员和直接或间接引用的类或类成员，其他的都会被移除。压缩会在每一次优化后执行，因为优化过后可能需要删除一些没有使用的类或类成员。
```
-printusage [filename]
```
**[File Name Usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-names.md)**
指定列出输入class文件中不需要的代码，这个列表将会输出到标准输出流或给定文件。
```
-whyareyoukeeping class_specification
```
请查看**[Class Specification Usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/keep-options.md)**中Class Specifications节。
指定输出给定类和类成员在压缩阶段被保留的原因。如果你想知道为什么一些元素被保留在输出文件，这个选项将很有用。总体上说，保留的原因有很多。这个选项打印最短的方法链对应类和类成员所指定的Entry Point。在现有的实现机制下，打印出的最短方法链有时会包含间接的删除信息-这并不影响压缩流程。如果你指定了**-verbose**选项，打印轨迹将包含域和方法签名。**该选项只在压缩阶段适用**。