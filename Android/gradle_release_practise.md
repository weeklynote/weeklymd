以下内容讲解使用Task获取签名文件的密码信息
## 创建签名文件信息
在Android Studio工程代码根目录添加keys.txt文件，并添加以下内容。  
```gradle
release.password = 123134131sdd
```
## 创建Task
```gradle
task getReleasePassword << {
     def password = ''
     if(rootProject.file('keys.txt').exists()){
         println 'find keys.txt file'
         Properties properties = new Properties();
         properties.load(rootProject.file('keys.txt').newDataInputStream())
         password = properties.getProperty("release.password")
     }
     if (!password?.trim()) {
         password = new String(System.console().readPassword("\nWhat's the secret password? "))
     }
    println password
}
```
上述Task将从根目录查找keys.txt文件，如果找到文件，则会加载里面的内容，并赋值给password；如果没找到keys.txt文件，那么将会要求用户输入密码。在Groovy中判断字符串时候为空或null比较复杂，?会做一个null检查并且如果password为null将会阻止其调用trim()方法。如果password为null或空都会返回false。new String()仅仅是将System.console.readPassword()读取到的字符数组转化为字符串。  
当我们获取到password后，就可以使用了，大致如下：  
```gradle
android.signingConfigs.release.storePassword = password
android.signingConfigs.release.keyPassword = password
```
## 给Task添加依赖
为了保证我们的Task在release编译时执行，需要将其进行依赖。  
```gradle
tasks.whenTaskAdded { theTask ->
	if (theTask.name.equals("packageRelease")) {
		theTask.dependsOn "getReleasePassword"
	}
}
```
上述代码将Hook到Gradle与Android插件，如果没有执行packageRelease任务，那么上述我们自定义Task将不会执行。请注意我们没有直接使用packageRelease.dependsOn()方法是因为Gradle根据(Build Variants)自动生成打包Task，直接使用时有可能并不存在该Task。







































