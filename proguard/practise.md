## 实践与总结
打开sdk安装目录下android-sdk-windows\tools\proguard\proguard-android.txt文件。
```
-dontusemixedcaseclassnames
```
###[Obfuscation Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/obfuscation-options.md)
指定混淆时不使用大小写混用的类名。
```
-dontskipnonpubliclibraryclasses
```
###[Input/Output Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/io-options.md)
不跳过非public类。
```
-verbose
```
###[General Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/general-options.md)
混淆过程中输出更多的信息。
```
-dontoptimize
```
###[Optimization Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/optimization-options.md)
不进行优化处理。
```
-dontpreverify
```
###[Preverification Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/preverification-options.md)
不进行预校验。
```
-keepattributes *Annotation*
```
存在问题
```
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
```
保护两个包下的ILicensingService不被混淆，**即使这两个类未被使用**。
```
-keepclasseswithmembernames class * {
    native <methods>;
}
```
###[Keep Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/keep-options.md)
保留native方法，理解这个写法时请注意查看**Keep Options**的最后总结部分，以及**Keep**命令的组成部分。
```
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
```
保留继承自View(不包含View)的控件的set和get方法，请注意\*、\*\*、\*\*\*的区别。
```
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
```
保留Activity中参数是View的方法，这个命令的用处大概就是为了xml中定义的onClick属性可以使用。
```
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
```
保留所有枚举类的两个方法。
```
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}
```
保留实现Parcelable接口的类及其内部类。
```
-keepclassmembers class **.R$* {
    public static <fields>;
}
```
保留自动生成的R类里的属性(attr,string等)。
```
-dontwarn android.support.**
```
不警告support包下的警告。

## 不混淆的类型
- 反射用到的类
- jni方法不混淆
- AndroidManifest.xml文件中的四大组件不能混淆(默认是开启的，不需要特别指定)
- Parcelable子类和Creator静态成员变量不混淆
- 将json转化为对应实体类，这些实体类不能混淆。
- js调用接口不能混淆
- 其他第三方sdk一般会有相应的配置说明，请参考它们。

## 组件化的混淆
gradle文件内的defaultConfig内的consumerProguardFiles属性，它指定了编译时，库自动引入的混淆规则。也就是说应用打包时候会自动的寻找库里的混淆文件，不需要手工配置了。这个属性可以解决组件化打包混淆的问题，因为主工程再也不用为添加进来的组件单独添加混淆文件了。
## 总结与经验
- 
> -obfuscationdictionary dictionary.txt
> -classobfuscationdictionary filename
> -packageobfuscationdictionary filename
> 这三个属性可以在混淆的时候使名字变成自己指定的特殊字符串，但是这样存在风险，请参考[Obfuscation Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/obfuscation-options.md)。
- 每次打包保留mapping文件，方便后续排查问题。
- 每次混淆之后，反编译查看是否可以进一步优化，以减小apk大小。
- 好好理解keep相关指令，足以解决平常的大多数问题，请熟悉其语法结构及代表的意思。
- 在看过阿里的apk加固技术分享时，里面提到了一个全量混淆的概念，对此重新查看了一下ProGuard手册。