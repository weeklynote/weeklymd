## 重命名APK名字
```gradle
android.applicationVariants.all { variant ->
        variant.outputs.each { output ->
            def file = output.outputFile
            output.outputFile = new File(file.parent,
                    file.name.replace(".apk", "-${variant.versionName}.apk"))
        }
}
```
每一个(Build Variants)有一个输出集合，对Android APP来说输出是APK文件。outputs对象有一个File对象叫做outputFile。一旦你知道文件的路径，你就可以来维护它们，比如修改其输出文件名字。