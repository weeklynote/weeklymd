## 动态创建Task
本例实现在执行installDebug任务之后启动对应的launcher Activity。  
```gradle
android.applicationVariants.all { variant ->
	if (variant.install) {
		tasks.create(name: "run${variant.name.capitalize()}",
		dependsOn: variant.install) {
			description "Installs the ${variant.description} and runsthe main launcher activity."
		}
	}
}
```
上述代码根据variant名创建Task， 同时我们确保自定义的Task会在执行前触发install任务，同时我们在闭包里面还增加了提示信息，这些信息将在gradlew tasks时显示，最后我们还需要增加实际的Action。  
使用**Android Debug Tool(ADB)**启动APP命令如下。  
```gradle
adb shell am start -n com.package.name/com.package.name.Activity
```
开始组装ADB命令，使用Gradle的exec()方法执行命令号指令。  
```gradle
doFirst {
      exec{
          executable = 'adb'
          args = ['shell', 'am', 'start', '-n', "${variant.applicationId}/.SplashActivity"]
      }
}
```
使用variant的applicationId获取包名，包含后缀。但是如果我们使用了后缀会存在问题一个问题，使用后缀虽然导致包名改变了，但是类的路径是不会改变的。  
```gradle

```