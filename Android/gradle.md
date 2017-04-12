## Gradle命令
### Task
```gradle
gradlew tasks  查看可用的tasks
```
执行此命令可以看到Android tasks、Build tasks、Build Setup tasks、Help tasks、Install tasks、Verification tasks、Other tasks。  
Android插件依赖于Java插件，Java插件依赖于Base插件。  
Base插件有基本的task生命周期和通用属性。  
Base插件定义了如assemble和clean任务，Java插件定义了check和build任务。  
对于task的约定：
- assemble：打包所有的output
- clean：清除所有的output
- check：执行所有的check检查，通常是unit测试和instrumentation测试
- build：执行所有的assemble和check

Java插件同时还添加了source sets的概念。
### Android Task
Android插件继承了上述的基本tasks，并且实现了自己的行为：
- assemble：针对每个版本创建apk文件
- clean：删除所有的构建任务，包含apk文件
- check：执行lint检查并且能够在lint检测到错误后停止执行脚本
- build：执行assemble和check

默认情况下assemble task定义了assembleDebug和assembleRelease，可以定制更多的task。Android插件也提供了一些新的task：
- connectedCheck：在测试机上执行所有的测试任务
- deviceCheck：在远程设备上执行所有的测试
- installDebug和installRelease在设备上安装测试和正式版本（默认），可以定制更多的task
- 所有的install task对应有uninstall task

以上的命令可以通过Android studio右上角的gradle窗口进行查看，双击执行。
### BuildConfig和resources
```gradle
release {
            buildConfigField "boolean", "IS_RELEASE", "true"
            buildConfigField "String", "DONAIN_NAME", '"http://www.baidu.com"'
        }
        debug {
            buildConfigField "boolean", "IS_RELEASE", "false"
            buildConfigField "String", "DONAIN_NAME", '"http://www.google.com"'
        }
```
也可以使用引用的形式  
```gradle
resValue("string", "app_name", "ic_launcher")
```
### 全局设置
如果多个模块需要使用一些公有的属性，可以在项目根目录中的build.gradle文件中定义  
```gradle
allprojects {
       apply plugin: 'com.android.application'
       android {
           compileSdkVersion 22
           buildToolsVersion "22.0.1"
       }
}
```
这样做的坏处是必须添加Android插件才能访问Android tasks，更好的做法采用以下方式  
```gradle
ext {
       compileSdkVersion = 22
       buildToolsVersion = "22.0.1"
} 
```
然后再子模块使用这些属性即可
```gradle
android {
       compileSdkVersion rootProject.ext.compileSdkVersion
       buildToolsVersion rootProject.ext.buildToolsVersion
}
```
### Project Properties文件
- ext方法
- gradle.properties
- -p参数

在build.gradle文件中定义如下内容  
```gradle
ext{
    local = 'hello from root build.gradle!!'
}
task printProperties << {
    println local;
    println propertiesFile
    if(project.hasProperty('cmd')){
        print cmd
    }
}
```
其中propertiesFile可以在gradle.properties中定义  
```gradle
propertiesFile = Hello from gradle.properties
```
最后可以在命令行输入以下命令，请注意-p与参数之间没有空格  
```gradle
gradlew printProperties -Pcmd='xxxxxxxxxxxxxxxxxxxxxxxxxxx'
```
最终得到以下输出  
```gradle
:printProperties
hello from root build.gradle!!
'Hello from gradle.properties'
'xxxxxxxxxxxxxxxxxxxxxxxxxxx'
```
### 依赖管理
#### 仓库
这里所说的仓库一般是指远程仓库，仓库可以被视为文件的集合体。Gradle不会默认为你的项目添加任何仓库，所以需要将其添加到repositories方法体内。使用Android Studio已经自动把上述操作执行。  
```gradle
repositories {
    jcenter()
}
```
Gradle支持三种仓库：Maven、ivy及文件夹。依赖包会在执行build时从远程仓库下载，Gradle会为每个特定版本的依赖包保留缓存。一个依赖需要定义三个元素：group、name和version。  
- group：创建该依赖包的组织名，通常为包名
- name：依赖包的唯一标识
- version：依赖包的版本号

依赖声明举例  
```gradle
dependencies {
	compile 'com.squareup.okhttp3:okhttp:3.6.0'
}
```
上述代码基于groovy语法，其完整的表述应该如下所示  
```gradle
dependencies {
    compile group: 'com.squareup.okhttp3', name: 'okhttp', version:'3.6.0'
}
```
Gradle会预定义三个maven仓库：jcenter、mavenCentral及mavenLocal。jcenter是maven中心库的一个分支，jcenter支持https，而maven不支持。mavenLocal是你曾使用过的所有依赖包的集合，你也可以添加自己的依赖包。  
在使用其他的一些远程仓库时，只需要在maven方法中加入url地址就好：
```gradle
repositories {
       maven {
           url "http://repo.acmecorp.com/maven2"
       }
}
```
Apache ivy在ant世界是很出名的依赖管理工具。如果你有自己的仓库并且需要权限才能访问，你可以这么编写：
```gradle
repositories {
       maven {
           url "http://repo.acmecorp.com/maven2"
           credentials {
               username 'user'
               password 'secretpassword'
           }
        } 
}
```
实际开发过程中，最好把用户名信息放到gradle.properties文件中。  
### 文件依赖
```gradle
dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
}
```
将libs文件夹下的所有的jar文件作为文件依赖。  
```gradle
apply plugin: 'com.android.library'
```
使用上述方式创建一个工程模块，该模块可以直接在settings.gradle文件中添加其为模块：
```gradle
include ':app', ':AndroidLib'
```
同时在依赖处添加：  
```gradle
dependencies {
	compile project(':AndroidLib')
}
```
另外还可以使用aar文件依赖，在构建library项目时，aar文件将会在build/output/aar/下生成。将aar文件拷贝到指定目录，然后添加该目录为依赖库。
```gradle
repositories {
    flatDir {
        dirs 'aars' 
    }
}
```
这样就会把aars文件夹下的所有文件作为依赖，最后你需要添加：  
```gradle
dependencies {
       compile(name:'libraryname', ext:'aar')
}
```
通过上述配置就可以告知gradle，在aars文件夹下，添加一个叫做libraryname的文件，且其后缀是aar的文件作为依赖。  
### 依赖的配置
- compile
- apk
- provided
- testCompile
- androidTestCompile

compile：默认的配置，即包含所有的依赖包。
apk：会在apk中存在，但是不会加入编译，用的较少。
provided：提供编译支持，但是不会写入apk。
testCompile和androidTestCompile会添加额外的library支持针对测试。
### 构建版本
构建版本通过buildTypes方法指定  
```gradle
buildTypes {
    release {
         minifyEnabled false
         proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
    }
}
```
上述内容定义了release版本，然后定义了proguard的位置。如果默认的构建版本不够用时，可以使用自定义方式写入自己的版本。  
```gradle
buildTypes {
        staging {
            applicationIdSuffix ".staging"
            versionNameSuffix "-staging"
            buildConfigField "String", "API_URL",
            "\"http://staging.example.com/api\""
         }
}
```
我们不用每个属性都去书写，可以采用继承已有的版本。  
```gradle
buildTypes {
           staging.initWith(buildTypes.debug)
           staging {
               applicationIdSuffix ".staging"
               versionNameSuffix "-staging"
               debuggable = false
           } 
}
```
initWith方法创建一个版本的同时，复制所有存在的构建版本，类似继承，当然我们也可以复写该存在版本的内容属性。  
当你创建一个新的构建版本时，Gradle也创建了新的source set。默认情况下，该文件夹不会自动为你创建，需要手动创建。  
当使用不同的source set时，资源文件处理需要特殊的方式。Drawable和layout文件将会复写在main中的重名文件，但是values文件下的资源不会。Gradle会把这些资源连同main里的资源一起合并。  
举个栗子，在main中创建string.xml：  
```gradle
<resources>
       <string name="app_name">TypesAndFlavors</string>
       <string name="hello_world">Hello world!</string>
</resources>
```
在staging版本中也添加了string.xml文件：  
```gradle
<resources>
       <string name="app_name">TypesAndFlavors STAGING</string>
</resources>
```
最后合并的string.xml文件将会是如下内容，而不是直接覆盖  
```gradle
<resources>
       <string name="app_name">TypesAndFlavors STAGING</string>
       <string name="hello_world">Hello world!</string>
</resources>
```
manifest和value文件夹下的文件一样，如果你为你的构建版本创建了一个manifest文件，那么你不必去拷贝main文件夹下的manifest文件，你需要做的就是加标签，Android插件会合并它们。  
### 构建版本依赖
每一个构建版本都有自己的依赖包，Gradle自动为每一个构建的版本创建不同的依赖配置。如果你想为debug版本添加一个logging框架  
```gradle
dependencies {
   compile fileTree(dir: 'libs', include: ['*.jar'])
   compile 'com.android.support:appcompat-v7:22.2.0'
   debugCompile 'de.mindpipe.android:android-logging-log4j:1.0.3'
}
```
这种方式使得不同的构建版本使用不同的依赖包称为可能。  
### productFlavors
productFlavors用来为一个APP创建不同的版本，和构建版本不同。国内可以参考多渠道包的创建理解这个参数，与构建版本的不同在于代码是否相同。构建版本可能对应于不同的代码；而productFlavors是同一套代码。  
创建productFlavors，在productFlavors方法块中添加代码：  
```gradle
productFlavors {
        red {
             applicationId 'com.gradleforandroid.red'
             versionCode 3
        }
        blue {
             applicationId 'com.gradleforandroid.blue'
             minSdkVersion 14
             versionCode 4
        }
}
```
productFlavors有自己的ProductFlavors类，就像defaultConfig，这意味着你的所有productFlavors都分享一样的属性。productFlavors可以有自己的文件夹，比如blue flavors对应的文件夹叫做blueRelease。
### 多个flavors变体
有时候可能需要创建多个productFlavors的合并版本，例如client A与client B都想要一个free和paid的版本，而它们又是基于一样的代码，但是不同的颜色。常规思维是创建四个flavors，但是这意味着重复。合并flavors最简单的做法是使用flavor dimensions
```gradle
flavorDimensions "color", "price"
       productFlavors {
           red {
               flavorDimension "color"
           }
           blue {
               flavorDimension "color"
           }
           free {
               flavorDimension "price"
           }
           paid {
               flavorDimension "price"
           }
       }
```
当添加了flavor dimensions，需要为每个flavor添加flavorDimension，否则会提示错误。flavorDimensions定义了不同的dimensions，其顺序也很重要。  
### Groovy入门
大多数Android开发者都是Java开发者，Groovy源于Java，并且也在jvm上运行。其目标是创建更简单直接的脚本和编译语言，以下内容将Groovy语法与Java语法做对比。如果要体验Groovy语法，可以在Android Studio中选择tools/Groovy Console，在该环境下书写Groovy代码并执行。  
在Java中，输出一个字符串  
```gradle
System.out.println("Hello, world!");
```
在Groovy中，输出一个字符串  
```gradle
println 'Hello, world!'
```
可以看到它们的区别  
- 没有System.out
- 没有参数外的圆括号
- 代码结束没有分号

上述的例子中字符串使用的单引号，你也可以使用双引号，但是两者有一点区别。双引号可以使用插值表达式，插值是计算包含placeholder的字符串过程，placeholder会被替换为表达式的值。placeholder表达式可以是变量、甚至是方法，placeholder表达式包含方法或多个变量需要使用大括号{}和前缀$。placeholder表达式只包含一个变量的只需要使用前缀$即可。  
```gradle
def name = 'Andy'
def greeting = "Hello, $name!"
def name_size "Your name is ${name.size()} characters long."
```
```gradle
"Hello, Andy"
```
```gradle
"Your name is 4 characters long.".
```
这种写法类似于javascript的值替换，可以动态的实现一些字符串操作，甚至可以使用下面的方式来动态调用方法。  
```gradle
def method = "toString"
println new Date()."$method"()
```
定义类和成员变量  
```gradle
class GroovyClass{
    String greeting
    String getGreeting(){
        return greeting
    }
}
def obj = new GroovyClass();
obj.setGreeting("Hello world!")
println obj.getGreeting()
```
```gradle
Hello world!
```
可以看到类和成员变量都没有权限修饰符，默认情况下类是public的，成员变量是private的。使用关键字def创建变量，创建变量之后你可以操作其成员变量。getter和setter会被自动添加，因此上述代码我们未定义setGreeting方法依然可以访问。如果你直接访问成员变量，其实你是访问的getter方法。  
```gradle
println obj.getGreeting()
println obj.greeting
两者是等价的
```
定义方法没有必要定义返回类型，在Groovy中方法的最后一行默认总是作为返回值，即使并没有使用return关键字。
在Java中定义方法  
```gradle
public int square(int num) {
	return num * num;
}
square(2);
```
在Groovy中定义相同的方法  
```gradle
def square(def num) {
	num * num
}
square 4
```
在Groovy中并没有显示定义返回类型以及参数类型。def关键字被用来替换类型，不管怎样，还是建议使用return关键字显示声明返回。同时还需要注意到，调用方法时不需要圆括号和分号结尾标志。  
还可以使用闭包的方式定义方法。  
```gradle
def square = {num -> num * num}
square 8
```
闭包是匿名的代码块，可以接收参数和返回类型。闭包可以被赋值给变量或作为方法的参数。如果你没有给闭包指定参数，Groovy会自动为其加一个参数，并且它的名字始终叫做it。这样就可以在闭包中使用it，如果调用者没有传递任何参数，it就为null。这种情况下可以使代码更简洁，但是仅仅是在只有一个参数的情况下适用。到此，结合build.gradle文件，可以发现android和dependencies方法快都可以看做闭包。  
集合：lists和maps。
定义List  
```gradle
List list = [1, 2, 3, 4, 5]
```
遍历List  
```gradle
list.each(){
    element -> println element
}
```
可以使用it简化上述代码  
```gradle
list.each() {
	println it
}
```
定义Map  
```gradle
Map pizzaPrices = [margherita:10, pepperoni:12]
```
访问特定项  
```gradle
Map pizzaPrices = [margherita:10, pepperoni:12]
println pizzaPrices.margherita
println pizzaPrices.get('pepperoni')
println pizzaPrices['pepperoni']
```
```gradle
10
12
12
```
### Groovy in Gradle
通过上面的学习，下面我们阅读build.gradle文件就会更容易了。  
```gradle
apply plugin: 'com.android.application'
```
它的更复杂书写方式为  
```gradle
project.apply([plugin: 'com.android.application'])
```
即调用Project类的apply()方法，apply()方法接受一个Map类型的参数。
```gradle
dependencies {
	compile 'com.google.code.gson:gson:2.3'
}
```
即调用Project类的dependencies()方法，这个闭包被传递给DependencyHandler，DependencyHandler包含一个add()方法。add()方法接收三个参数：一个String定义配置；一个对象对象依赖符号；一个闭包包含依赖的一些额外属性。  
```gradle
project.dependencies({
	add('compile', 'com.google.code.gson:gson:2.3', {
		// Configuration statements
})
})
```
### 重新认识task
Gradle编译生命周期包括：初始化、配置、执行。新手定义task的常见误区  
```gradle
task hello {
	println 'Configuration!'
}
```
按照上述定义task，命令行执行gradlew hello时也会看到打印，这很容易让人觉得task已被执行，但是实际上仅仅是在设置task的配置。  
```gradle
task hello << {
	println 'Execution!'
}
```
在闭包前面加<<，这样告诉Gradle代码在执行阶段处理，而不是配置阶段。  
因为Groovy存在很多简写方式，所以定义task方式也有多种。  
```gradle
task(hello) << {
	println 'Hello, world!'
}
task('hello') << {
	println 'Hello, world!'
}
tasks.create(name: 'hello') << {
	println 'Hello, world!'
}
```
前两种是类似写法，你可以使用圆括号或者不使用，你也没有必要在参数两边使用单引号。task()方法属于Project类，需要传入两个参数：一个字符串代表task名；一个闭包。最后一个方法定义task使用了对象tasks，这个tasks是一个TaskContainer对象，可以在Project对象中直接使用。tasks提供了一个create()方法需要传入Map和闭包作为参数，并且返回一个Task。初学阶段不建议这么书写，建议书写非简化方式便于理解。  
### Task剖析
Task接口是所有Task的基础，其定义了一个属性集合和方法。被DefaultTask实现。这是实现Task的标准写法，如果你需要创建一个新的Task，最好是基于DefaultTask。  
严格意义上说，DefaultTask并不是实现了所有的Task接口方法。Gradle有自己的内部AbstractTask，它实现了所有的task接口方法。但是AbstractTask是内部的，我们不能复写。因此我们专注于DefaultTask，它源于AbstractTask并且可以被复写。  
每个Task包含一个Action集合。当一个Task执行时，所有的Action都会按照顺序执行。要添加Action至Task，你可以使用doFirst()和doLast()方法。这两个方法需要传入一个闭包作为参数，然后将闭包封装到Action。  
可以使用doFirst()或doLast()来添加代码到Task，添加的代码将在执行阶段执行。<<符号是doFirst()的简写方式。  
```gradle
task hello{
        println 'Configuration'
        doLast{
            println 'goodbye'
        }
        doFirst{
            println 'Hello'
        }
}
```
```gradle
Configuration                                                                  
:app:hello                                                                     
Hello                     
goodbye 
```
注意：从打印可以看到Task的执行在：app：hello后，配置阶段的打印在执行前就被打印。**我们可以多次使用doFirst()和doLast()方法多次。**  
```gradle
task hello{
        println 'Configuration'
        doLast{
            println 'goodbye1'
        }
        doLast{
            println 'goodbye2'
        }
        doFirst{
            println 'Hello1'
        }
        doFirst{
            println 'Hello2'
        }
    }
```
```gradle
Configuration                                                                  
:app:hello                                                                     
Hello2                    
Hello1                    
goodbye1                  
goodbye2 
```
因此，我们知道doFirst()总是将Action添加到Task的开始，doLast()总是将Action添加到末尾。这种特性需要在方法的执行有顺序要求时特别注意。  
但需要对Task排序时，你可以使用mustRunAfter()方法。  
```gradle
task task1 << {
	println 'task1'
}
task task2 << {
	println 'task2'
}
task2.mustRunAfter task1
```
这将导致task1总是比task2先执行。  
```gradle
:app:task1                                                                     
task1                     
:app:task2                 
task2  
```
mustRunAfter()方法并不会将两个Task相互依赖；还是可以单独执行task2而不执行task1。如果要让两个task产生依赖，可以使用dependsOn()方法。  
```gradle
task task3 << {
    println 'task3'
}
task task4 << {
    println 'task4'
}
task4.dependsOn task3
```
```gradle
gradlew task4
:app:task3                                                                     
task3                     
:app:task4                 
task4 
```
[自定义Task获取签名文件密码](http://)
### Hook Android Plugin
在Android开发中，我们左右可能想关联的Task就是Android插件。Hook Android插件可以增强Task的功能。一种Hook的方式就是操作(Build Variant)，这种方式是最直接的，可以使用下面的代码来遍历App里面的(Build Variants)的值。  
```gradle
android.applicationVariants.all { variant ->
	// Do something
}
```
你可以使用applicationVariants来获取(Build Variants)，如果是Android Library你可以使用libraryVariants来获取(Build Variants)。  
请注意上述的遍历是使用的all()而不是each()方法。这是很有必要的，因为each()会在求值阶段触发，此时(Build Variants)还没有被Android插件创建。all()方法会在新的variant加入时触发。  














































































































































