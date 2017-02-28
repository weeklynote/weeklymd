## Keep Options
```
-keep [,modifier,...] class_specification
```
保留指定类和类成员变量(属性和方法)保留作为代码的入口，**即使未被使用**。比如对于Java程序运行，需要保留其程序入口的main方法；对于库文件，需要保留所有可以公共访问的类。
```
-keepclassmembers [,modifier,...] class_specification
```
在类被保留的情况下保留类成员
```
-keepclassmembernames class_specification
-keepclassmembers,allowshrinking class_specification的缩写写法
```
保留特定名字的类成员，如果他们没有在压缩阶段被移除的话。
```
-keepclasseswithmembers [,modifier,...] class_specification
```
保留指定类和类成员，可以通过一定的条件设置，而不用显示的列出所有的类。
```
-keepnames class_specification
-keep,allowshrinking class_specification的缩写写法
```
保留特定名字的类或类成员，如果他们没在压缩阶段被移除的话。
```
-keepclasseswithmembernames class_specification
-keepclasseswithmembers,allowshrinking class_specification的缩写写法
```
保留特定名字的类成员，如果他们没有在压缩阶段被移除的话。
```
-printseeds [filename]
```
将匹配-keep选项的类或类成员从标准输出打印出来。

## Modifier
```
allowshrinking
```
允许被压缩，即使他们必须保留。另外，如果被压缩的部分是必须要使用到的，那么他们不会被优化和混淆。
```
allowoptimization
```
允许被优化，但是不会移除和混淆，使用情况较少。
```
allowobfuscation
```
允许被混淆。但是不会被移除和优化，使用情况较少。
**另外，可以看到-keep的缩写指令都是使用allowshrinking作为modifier。**

## Class Specifications(难点)
类和类成员(属性和方法)的匹配模板，在**-keep**和**-assumenosideeffects**命令中使用，只有满足匹配模板的类和类成员才会被应用到相应的配置上。
###**请重点查看如下表达式**
```
[@annotationtype] [[!]public|final|abstract|@ ...] [!]interface|class|enum classname [extends|implements[@annotationtype] classname]
[{
    [@annotationtype] [[!]public|private|protected|static|volatile|transient ...] <fields> | (fieldtype fieldname);
    [@annotationtype] [[!]public|private|protected|static|synchronized|native|abstract|strictfp ...] <methods> |
                                                                                      <init>(argumenttype,...) |
                                                                                   classname(argumenttype,...) |
        (returntype methodname(argumenttype,...));
        [@annotationtype] [[!]public|private|protected|static ... ] *;
    ...
}]
```
方括号**[]**表示内容是可选的；**...**表示可以有若干个前面紧邻项；**|**界定两个选项；**()**仅仅是将说明里面的内容是一个整体；实际配置文件空白是无关紧要的。
```
class关键字涉及任意类或接口；interface关键字指代接口；enum关键字代表枚举；在enum或interface前加上！代表非枚举和非接口。
classname必须是全路径的，比如java.lang.String，内部类使用$符号分隔，classname也可以使用通配符。
```
> ######?
> 匹配任意单个字符，但是不能匹配包分隔符点(.)。
> eg："mypackage.Test?"匹配"mypackage.Test1"和"mypackage.Test2"，但是不匹配"mypackage.Test12".
> ######\*
> 匹配任意多个字符，但是不能匹配包分隔符点(.)。
> eg："mypackage.\*Test\*"匹配"mypackage.Test"、"mypackage.YourTestApplication"，
> 但是不匹配"mypackage.mysubpackage.MyTest"。
> 更通用的说，"mypackage.*"只会匹配"mypackage"下的类，而不是其子包下的类。
> ######**
> 匹配类名的任意部分，包括包分隔符点(.)。
> **"\*\*.Test"**匹配所有除了根目录下的所有目录中包含的Test类。"mypackage.\*\*"匹配**”mypackage"**包及其子包中的所有类。

为了适度的灵活性，类名列表可以以逗号分开，使用**!**表示取反，就如同文件名过滤器一样。这个规则与java不是非常相似，所以请适度使用。
为了方便和后向兼容，*可以表示任何类名，与包名无关。
```
extends和implements关键字常与通配符一起使用。它们是等价的，指定只有那些extending或者implementing给定类的类才能通过。请注意，给定的类(被继承或实现的接口)不包含在这个限定规则里。若想把给定的类也指定，应该单独写个规则进行限定。
```
```
@标示符可以用于限定那些使用指定注解的类和类成员。注解的指定和类名一样(即需要全路径)。
```
```
声明属性和方法与Java类似，除了方法的参数不会包含参数名(会包含参数的数据类型)。
```
规范可以包含下面的通配符：
> ######&lt;init>
> 匹配任何构造方法
> ######&lt;fields>
> 匹配任何属性
> ######&lt;methods>
> 匹配任何方法
> ######*
> 匹配任何属性和方法

请注意以上的通配符没有返回类型，仅仅只有&lt;init>通配符才有参数列表。
属性和方法也可以使用正则表达式，如下：
> ######?
> 匹配方法名中的任意单个字符
> ######*
> 匹配任意方法名

属性或参数的类型可以通过以下通配符：
> ######%
> 匹配任意原生数据类型(boolean、int等，不包含Void)。
> ######？
> 匹配类名中的任意单个字符。
> ######*
> 匹配类名中的任意多个字符，但是不能匹配包分隔符点(.)。
> ######**
> 匹配类名中的任意部分，包括包分隔符点(.)。
> ######\***
> 匹配任意数据类型(原生数据类型、数组、非原生数据类型、非数组)。

请注意?、\*、\**不会匹配任何原生数据类型，*\*\**会匹配任意大小的数据类型。
```
** get*()匹配java.lang.Object getObject()，但是不匹配float getFloat()，也不匹配java.lang.Object[] getObjects()。
```
```
构造方法可以通过设置类名(不包含包名)或全类名指定。在java中，构造器有参数列表，但没有返回类型。
```
```
类和类成员的访问修饰符常用于约束使用通配符限定的类和类成员，通过他们限定相应访问权限的成员，！是不能被设置在访问权限前。
使用多个标识是可以的(例如 public static)。这就意味着，两种作用域标示符(public和static)都可以设置，除非他们之间有冲突。也就是说冲突的作用域标示符只能设置一个(例如:设置public或者protected)。
ProGuard也支持在编译器上设置的标识如synthetic、bridge、varargs。
```
## 关于Keep选项的总结概述
<img src="https://github.com/weeklynote/weeklymd/blob/master/images/overview-keep-options.png?raw=true" alt="screenshot" title="screenshot" />

每一个keep指令必须要制定一个类或者类成员。
如果你不确定你需要使用哪一个选项，你可以选择使用**-keep**指令。这样可以确保你指定的类或类成员不会再压缩阶段被删除，同时在混淆阶段被重命名。
请注意：
> 指定类而没有指定类成员时，仅仅是会保留类，任何其他的类成员依然会被删除、优化、混淆。  
> 
> 指定类成员仅仅是会保留类成员，任何其他的相关代码依然会被优化和移除。  
> 
> -keep指令保护的类或类成员时，即使类或类成员未被使用也会被保留。  
> 
> -keepnames指令保护的类或类成员，如果类或类成员未被使用，那么其代码将不会被保留。  
> 
> -keepclassmembers指令保护的类成员，如果包含类成员的类被移除，则想要保护的类成员也会被移除；如果类未被移除，才会保护相应的类成员**(即使它们未被使用，并且由于保留了相应的类成员，其对应的类型class文件也会保留，只是会被重命名)**。  
> 
> -keepclassmembernames指令保护类成员，如果包含类成员的类被移除，那么想要保护的类成员也会被移除；如果类未被移除，才会保护相应的类成员不会被重命名**(未被使用的类成员依然会被移除，但是测试过后发现static变量不会被移除)**。  
> 
> -keepclasswithmembers如果存在某个类成员，那么会保留类和类成员**(即使类未被使用也会保留类和类成员，请注意在测试过程中发现静态变量如果未被使用依然会保留，其他未被使用的类成员同样会被移除)**。  
> 
> --keepclasseswithmembernames如果指定的类成员对应的类被移除，那么类成员也会被移除。如果类成员对应的类被保留，那么结果与-keepclasswithmembers一致。