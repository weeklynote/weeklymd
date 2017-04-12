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