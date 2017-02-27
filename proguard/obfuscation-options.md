## Obfuscation Options
```
-dontobfuscate
```
指定不混淆输入的类文件。默认情况下，混淆是开启的。类和类成员名会变成随机的名字，使用了**-keep**指令的除外。内部的一些有用的用来调试的属性会被删除，比如源文件名、变量名、行号。

```
-printmapping [filename]
```
###[filename usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-names.md)
指定打印出被更改过名字的类和类成员的旧名和新名之间的映射。映射被打印到标准输出流或指定的文件中。**该选项只在混淆时适用**。

```
-applymapping filename
```
###[filename usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-names.md)
指定重用以前的混淆后的产生的映射文件，即类和类成员会接收映射文件列出的名字而不是新的名字。映射对输入的类文件和库文件同样适用。这个选项在使用增量混淆时很有用，比如：处理插件或小补丁到现有的代码中。在这样的情况下，你需要考虑是否使用**-useuniqueclassmembernames**选项。**只能接收一个映射文件，并且该选项只在混淆时适用**。
```
-obfuscationdictionary filename
```
###[filename usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-names.md)
指定一个文本文件并且使用文本文件里面的有效字符作为混淆后属性和方法的名字。默认情况下，混淆后的名字就像a、b子类的。对于一个混淆字典，你可以指定一系列的保留关键字、外国字符，比如：空格、标点符号、重复的单词，#后面的注释会被忽略。**请注意混淆字典几乎不会提高混淆的质量**，稍微专业一点的编译器就可以自动的替换它们，其结果就是导致混淆撤销，然后重新进行混淆。最有用的应用场景是指定在类文件中已经存在字符作为字典，这样可以适当减小输出文件大小，**并且该选项只在混淆时适用**。
```
-classobfuscationdictionary filename
```
###[filename usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-names.md)
同**-obfuscationdictionary**，只是这个选项是用来混淆类名。**该选项只在混淆时适用**。
```
-packageobfuscationdictionary filename
```
###[filename usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-names.md)
同**-obfuscationdictionary**，只是这个选项是用来混淆包名。**该选项只在混淆时适用**。
```
-overloadaggressively
```
指定混淆时采用侵略式的重载。只要多个属性和方法的参数和返回类型不一致，它们就可以使用相同的名字。**这个选项可以时代码变得更短小，该选项只在混淆时适用**。
重要警告：即使这种重载方式不符合Java语言规范，但是最终产生的结果类文件符合Java字节码规范。一些工具在处理这样的结果类文件时会出问题。
> - JDK1.2.2javadoc编译器在编译这样的库时会产生一个异常。所以你不应该使用这个选项来处理库。
> - JRE1.4及以上版本在序列化经过重载的原生数据类型属性时会失败。
> - 据说JRE1.5pack200工具在处理重载过的类成员时会存在问题。
> - Google Dalvik VM不能处理重载过的静态属性。

```
-useuniqueclassmembernames
```
指定名字相同的类成员使用相同的混淆的名字，名字不同的类成员使用不同的混淆的名字。如果没有指定这个选项，那么更多的类成员将会被映射成a、b、c子类的名字。这个选项会导致输出文件变大，但是它确保保留下来的映射名会在后续的增量混淆中依然有用。
比如：想象一下两个不同的接口存在一个名字和方法签名都一样的方法。不使用这个选项，这两个方法将会在第一次混淆时被混淆成不同的名字。如果此时添加了一个补丁，这个补丁包含一个类，这个类同时实现了这两个接口，ProGuard将会强制将两个方法混淆成同一个方法名。此时以前的混淆的代码发生了变化，为了保持代码功能的一致性。使用这个选项，在初始化混淆时，不会发生这样的重命名过程。
**这个选项只在混淆时适用**。实际上，如果你正在计划增量混淆，你可能需要避免压缩和优化，因为这些步骤可以删除或修改部分代码,而这些代码在后面版本中将会至关重要。
```
-dontusemixedcaseclassnames
```
指定混淆过程不生成大小写混用的类名。默认情况下，混淆后的类名可能同时包含大写字母和小写字母。这样生成的jars并没有什么问题。但是如果在大小写敏感的系统上打开jar，可能会产生文件覆盖，导致jar损坏。开发者如果想要在Windows系统上打开jar可以关闭这个行为。**注意到混淆后的jar会增大，这个选项只在混淆时适用**。
```
-keeppackagenames [package_filter]
```
###[package_filter usage](http://)
指定不混淆给定的包名。**package_filter**是使用逗号分隔的包名列表，包名可以包含？、\*、\*\*通配符，也可以在包名前使用!(取反)。**这个选项只在混淆时适用**。
```
-flattenpackagehierarchy [package_name]
```
###[package_name usage](http://)
指定将重命名过的包重新打包，将其移动到一个给定的父包目录中。无参数或使用一个空字符串，所有的包都会被移动到根包目录下，这个选项是对混淆包名的进一步混淆。可以使处理过得代码更小和更容易理解。**这个选项只在混淆时适用**。
```
-repackageclasses [package_name]
```
###[package_name usage](http://)
指定对重命名过的类文件进行重命名，将其移动到一个指定的包中。无参数或使用一个空字符串，包将会被彻底删除。
这个选项会覆盖**-flattenpackagehierarchy**，这个选项是对混淆包名的进一步混淆。可以使处理过得代码更小和更容易理解。这个选项有一个不建议使用的名字**-defaultpackage**，**这个选项只在混淆时适用**。
重要警告：如果类通过包目录查找资源文件将不能正常的工作，因为已经被移动到其他位置去了。**如果不能确定使用这个选项是否会存在问题时，建议不要使用这个选项**。
```
-keepattributes [attribute_filter]
```
###[attribute_filter usage](http://)
指定保留指定的属性。这些属性可以通过一个或多个**-keepattributes**指令指定。**attribute_filter**使用逗号分隔多个属性名。属性名可以包含？、\*、\**通配符，并且可以使用！(非)操作符。典型的可选属性包括Signature、Deprecated、SourceFile、SourceDir、LineNumberTable、 LocalVariableTable、LocalVariableTypeTable、Synthetic、EnclosingMethod、RuntimeVisibleAnnotations、 RuntimeInvisibleAnnotations、RuntimeVisibleParameterAnnotations、RuntimeInvisibleParameterAnnotations、AnnotationDefault 。内部类的属性名也可以指定，指向属性的源文件名称。比如：你在处理库时至少需要保留Exceptions、InnerClasses、Signature属性。你可以保留SourceFile和LineNumberTable属性来产生有用的混淆堆栈信息。最后，你可能需要保留代码需要的注解。**这个选项只在混淆时适用**。
```
-keepparameternames
```
指定保留参数名称和方法的类型。实际上这个选项可以不断精简调试属性LocalVariableTypeTable LocalVariableTable。在处理一个库时是有用的。一些集成开发环境可以利用这些信息来帮助开发人员使用库,例如工具提示或自动完成。**这个选项只在混淆时适用**。
```
-renamesourcefileattribute [string]
```
指定一个常量字符串用于放在类文件的属性中。请注意属性必须是已经出现过的，所以必须是显示的使用**-keepattributes**指令。例如,您可能想要处理过的库和应用程序产生有用的混淆过的堆栈跟踪信息，**这个选项只在混淆时适用**。
```
-adaptclassstrings [class_filter]
```
###[class_filter usage](http://)
指定字符串常量(对应于类名)应该被混淆。不使用过滤器,所有对应于类名的字符串常量都会被修改。使用过滤器,只在匹配类里面的字符串常量才会被修改。例如,如果您的代码包含大量的硬编码的字符串指向一个类,你最好不要保留它们名字,你可能需要使用这个选项。**主要适用于混淆时,尽管相应类已经在压缩阶段自动压缩了**。
```
-adaptresourcefilenames [file_filter]
```
###[file_filter usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-filters.md)
指定在混淆过的相应的类文件的基础上对资源文件重命名。没有使用过滤器参数,所有对应于类文件的资源文件将被重命名。使用了过滤器,只有匹配文件被重命名。**主要适用于混淆时**。
```
-adaptresourcefilecontents [file_filter]
```
###[file_filter usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-filters.md)
指定了资源文件的内容将被更新。资源文件中提到的任何类的名字会在混淆的基础上重新命名。使用过滤器参数,所有资源文件的内容会被更新。使用了过滤器参数,只有匹配的文件被更新。资源文件的解析和书写使用平台的默认字符集。你可以改变这种默认字符集通过设置环境变量LANG或者Java系统属性file.encoding。**主要适用于混淆时**。































































































































