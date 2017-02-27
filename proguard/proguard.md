# Android Proguard

 [参考ProGuard手册](https://stuff.mit.edu/afs/sipb/project/android/sdk/android-sdk-linux/tools/proguard/docs/index.html#manual/usage.html)

## ProGuard简介
ProGuard是一个压缩、优化、混淆Java字节码的开源工具，它可以删除无用的类、字段、方法和属性。可以删除没用的注释，最大限度地优化字节码文件。它还可以使用简短的无意义的名称来重命名已经存在的类、字段、方法和属性，增加项目被反编译的难度。简而言之，其主要包括以下四个功能。
* 压缩(Shrink):检测并移除代码中无用的类、字段、方法、属性。
* 优化(Optimize):对字节码进行优化，移除无用的指令。
* 混淆(Obfuscate):使用简短无意义的名称重命名已经存在的类、字段、方法等。
* 预校验(Preveirfy)：在Java平台上对处理后的代码进行预检，确保加载的class文件是可执行。

## ProGuard工作原理
根据官网的Entry Points节描述，ProGuard会根据Entry Points来确定哪些代码会被保留、移除、混淆，而Entry Points的来源是我们的配置文件。
- 在压缩阶段，ProGuard会递归遍历上述的Entry Points，查询出哪些类和成员在使用，并且移除未被使用的类和成员。
- 在优化阶段，ProGuard进一步优化代码，不在Entry Points中的类和方法会被设置为private、static或final，未使用的参数会被移除，一些方法会被内联。
- 在混淆阶段，不在Entry Points中的类和成员变量会被重命名，这个过程会确保Entry Points中的类和方法 可以使用以前的名字被访问，即Entry Points中的类和方法不会被混淆，名字保持原来一致。
- 在预检验中不需要关心Entry Points。

最后我们需要知道，ProGuard不会混淆native代码、drawable、xml。

## 开启Android混淆
```gradle
buildTypes {
        release {
            minifyEnabled true
            zipAlignEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
```
强烈建议设置zipAlignEnabled为true，zipAlign可以让安装包中的资源按4字节对齐，这样可以减少应用在运行时的内存消耗。

## 准备工作
- Jadx  
详情请查看开源项目**[jadx](https://github.com/skylot/jadx)**，该工具可以不使用任何命令行直接将apk或jar文件反编译并以较高可读性的方式查看源代码，**强烈推荐**。
- 强烈推荐查看SDK目录下android-sdk-windows\tools\proguard下的混淆文件，根据里面的示例进一步理解每个命令的含义。

# Usage
- ## [General Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/general-options.md)
- ## [Input/Output Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/io-options.md)
- ## [Keep Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/keep-options.md)
- ## [Shrinking Options](https://)
- ## [Optimization Options](https://)
- ## [Obfuscation Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/obfuscation-options.md)
- ## [Preverification Options](https://github.com/weeklynote/weeklymd/blob/master/proguard/preverification-options.md)
- ## [Class Paths](https://github.com/weeklynote/weeklymd/blob/master/proguard/class-paths.md)
- ## [File Names](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-names.md)
- ## [File Filters](https://github.com/weeklynote/weeklymd/blob/master/proguard/file-filters.md)
- ## [Filters](https://github.com/weeklynote/weeklymd/blob/master/proguard/filters.md)
- ## [实践与总结](https://github.com/weeklynote/weeklymd/blob/master/proguard/practise.md)













