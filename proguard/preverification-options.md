## Preverification Options
```
-dontpreverify
```
指定不预先验证处理类文件。默认情况下,在Java ME版或在Java 6及更高版本上类文件是会进行预校验的。对于Java ME，预校验是必需的,如果指定了这个选项，你会在处理过的代码上运行外部预检验。对于Java 6,预校验不是必须的,但它提高了Java虚拟机加载类的效率。

```
-microedition
```
指定处理类文件是针对Java ME。预校验将添加适当的StackMap属性,这不同于Java SE上默认StackMapTable属性。