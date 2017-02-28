## Optimization Options
```
-dontoptimize
```
指定不优化输入的class文件。默认情况是开启优化；所有的方法都会在字节码水平被优化。
```
-optimizations optimization_filter
```
**[Optimization Filter Usage]()**
指定以更细粒度水平的条件下决定是否开启优化，**该选项只在优化阶段适用**，这是一个专家级的选项。
```
-optimizationpasses n
```
指定优化的次数。默认情况下，只优化一次。多次优化可能会提升优化效果。如果在优化后没有可以提升效果的空间，那么优化将结束，**该选项只在优化阶段适用**。
```
-assumenosideeffects class_specification
```
**请查看[Class Specification Usage](https://github.com/weeklynote/weeklymd/blob/master/proguard/keep-options.md)**中的Class Specification节。
指定方法没有任何副作用(除非存在返回值)。优化阶段，如果方法的返回值未被使用，ProGuard将会删除这种方法的调用。注意ProGuard是自动分析程序代码找到这样的方法，**ProGuard将不会分析库代码**。比如：你可以指定System.currentTimeMillis()，因此那些满足条件的调用将会被移除。注意，ProGuard会在将整个方法的层级上应用这个选项。**该选项只在优化阶段适用**。总的来说，assumptions是危险的；你可以很容易的打断处理代码。**如果你很确定这样不会影响代码，你才可以这么做**。
```
-allowaccessmodification
```
指定在ProGuard混淆处理扩展类和类成员的访问权限，它能提升优化结果。比如：当内嵌一个公共的getter方法时，可能也需要将相关的field也变成公共访问权限，尽管Java的二进制兼容性规范没有要求这么做。另外，有些虚拟机在处理这样优化过的代码可能存在问题。**该选项只在优化阶段适用(可以和-repackageclasses选项一起使用)**。
重要警告：你不应该在被设计成库的代码中使用这个选项，因为类和类成员并没有被设计成公共的，但是在处理过后会变成公共的访问属性。
```
-mergeinterfacesaggressively
```
指定合并接口，即使它们的实现类没有实现所有的接口方法。它能减少输出文件里类的数量，即它可以减小输出文件的大小。注意Java的二进制兼容性规范是允许这样的重构行为的，虽然Java语言不允许这样操作。**该选项只在优化阶段适用**。

重要警告：使用了此选项后的处理代码在一些虚拟机中会影响性能。因为高级的运行时编译机制倾向于更多的接口，更少的实现类。更严重的情况是，一些虚拟机可能不能处理这种代码。
> 当遇到一个类有超过256个Miranda methods(没有被实现的接口方法)，Sun JRE 1.3将抛出一个InternalError。










































