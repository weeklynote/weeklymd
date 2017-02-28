## General Options
```
-verbose
```
指定处理过程中输出更多的信息。如果程序因为异常而结束，这个选项将输出整个堆栈信息而不是仅仅一条异常信息。

```
-dontnote [class_filter]
```
###[class_filter usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/filters.md)
指定不输出潜在的错误或遗漏信息，比如类名的拼写错误，或者遗漏了什么有用的配置项。filter是一个正则表达式，Proguard不会输出匹配的名字的相关说明信息。

```
-dontwarn [class_filter]
```
###[class_filter usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/filters.md)
指定不对未解决的引用和重要信息进行警告，filter是一个正则表达式，Proguard不会输出匹配的名字的相关警告信息。忽略警告信息是很危险的行为，比如未解决的类或类成员其实是必要的，那么混淆过后的代码可能会不能正常工作。**在很明确不会影响功能的情况下才可以使用这个选项**
```
-ignorewarnings
```
指定打印所有关于引用错误和其他重要信息的警告信息，**但是会继续处理代码。**忽略警告信息是很危险的行为，比如未解决的类或类成员其实是必要的，那么混淆过后的代码可能会不能正常工作。**在很明确不会影响功能的情况下才可以使用这个选项**
```
-printconfiguration [filename]
```
###[file name usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-names.md)
指定输出已经解析过的整个配置信息到标准输出流或给定文件中。通常用来调试配置信息或将xml配置转化为可读性更好的格式。
```
-dump [filename]
```
###[file name usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-names.md)
指定在任何处理之后输出指定类文件的内部结构到标准输出流或给定的文件中。