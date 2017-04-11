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
































































































