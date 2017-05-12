## Getting Started
### Basic Syntax
#### Defining packages  
定义包名必须在源文件的最开始部分。  
```gradle
package my.demo
import java.util.*
// ...content...
```
#### Defining functions  
定义两个Int类型参数，并且返回值类型也是Int的函数：  
```gradle
fun sum(a: Int, b: Int): Int {
    return a + b
}
```
定义包含一个表达式，并且返回类型是可推测的函数：  
```gradle
fun sum(a: Int, b: Int) = a + b
```
定义一个返回没有什么意义的函数：  
```gradle
fun printSum(a: Int, b: Int): Unit {
	println("sum of $a and $b is ${a + b}")
}
```
其中Unit关键字是可以省略的，即可以简写如下：  
```gradle
fun printSum(a: Int, b: Int) {
	println("sum of $a and $b is ${a + b}")
}
```
其余请查看[Functions](http://)
#### Defining local variables  
赋值一次(只读)的本地变量：  
```gradle
val a: Int = 1 // 立即赋值
val b = 2 // `Int`类型是推测出来的
val c: Int // 如果没有提供初始化操作，此时定义需要类型
c = 3 // 延迟的赋值，与上一句赋值语句结合使用
```
可变的变量：  
```gradle
var x = 5 // `Int`类型是推测出来的
x += 1
```
其余请查看[Properties And Fields](http://)  
#### 注释  
支持注释的嵌套。  
```gradle
// This is an end-of-line comment
/* This is a block comment on multiple lines. */
```
请查看[Documenting Kotlin Code](http://)查看更多语法详情。  
#### 使用String模板
```gradle
var a = 1
val s1 = "a is $a"
a = 2
val s2 = "${s1.replace("is", "was")}, but now is $a"
```
更多详情请参考[String templates](http://)。  
#### 使用条件表达式  
```gradle
fun maxOf(a: Int, b: Int): Int {
	if (a > b) {
		return a
	} else {
		return b
	}
}
```
也可以使用下面的简化形式：  
```gradle
fun maxOf(a: Int, b: Int) = if (a > b) a else b
```
```gradle
fun foo(param: Int) {
val result = if (param == 1) {
	"one"
} else if (param == 2) {
	"two"
} else {
	"three"
}
}
```
请查看[if-expressions](http://)。
#### 使用可为null的值和检查null  
一个应用可以显示的被标记为可以为null，如果存在该值为null的可能性。  
定义如果str不含有整数那么返回null：  
```gradle
fun parseInt(str: String): Int? {
	// ...
}
```
使用函数返回可为null值：  
```gradle
fun printProduct(arg1: String, arg2: String) {
	val x = parseInt(arg1)
	val y = parseInt(arg2)
	// 对`x * y`进行null检查
	if (x != null && y != null) {
		// x和y会被自动的转换为非null值
        println(x * y)
	}
	else {
		println("either '$arg1' or '$arg2' is not a number")
	}
}
```
或者：  
```gradle
if (x == null) {
	println("Wrong number format in arg1: '${arg1}'")
	return
}
if (y == null) {
	println("Wrong number format in arg2: '${arg2}'")
	return
}
println(x * y)
```
请查看[Null-safety](http://)。
#### 使用类型检查和自动转换  
**is**操作符用来检查一个实例是否是某种类型的实例。如果一个不可变的局部变量或属性被检测出来是一种特定的类型，就没有必要显示的转换它。  
```gradle
fun getStringLength(obj: Any): Int? {
	if (obj is String) {
		// `obj`会在这个分支自动转换为String类型
		return obj.length
	}
	// `obj`仍然是Any类型
	return null
}
```
或者：  
```gradle
fun getStringLength(obj: Any): Int? {
	if (obj !is String) return null
	return obj.length
}
```
更甚这样：  
```gradle
fun getStringLength(obj: Any): Int? {
	// `obj`会在&&的右边被自动的转换为String
	if (obj is String && obj.length > 0) {
		return obj.length
	}
	return null
}
```
请查看[Classes](http://)和[Type casts](http://)。
#### Using for loop  
```gradle
val items = listOf("apple", "banana", "kiwi")
for (item in items) {
	println(item)
}
```
或者：  
```gradle
val items = listOf("apple", "banana", "kiwi")
for (index in items.indices) {
	println("item at $index is ${items[index]}")
}
```
请查看[for loop](http://)。
#### Using while loop  
```gradle
val items = listOf("apple", "banana", "kiwi")
var index = 0
while (index < items.size) {
	println("item at $index is ${items[index]}")
	index++
}
```
请查看[while loop](http://)。  
#### Using when expression  
```gradle
fun describe(obj: Any): String =
	when (obj) {
		1 -> "One"
		"Hello" -> "Greeting"
		is Long -> "Long"
		!is String -> "Not a string"
		else -> "Unknown"
}
```
请查看[when expression](http://)。
#### Using ranges  
使用**in**操作符检查一个数字是否在某个范围内。  
```gradle
val x = 10
val y = 9
if (x in 1..y+1) {
	println("fits in range")
}
```
请注意**in**操作符是闭区间。  
如果要使用左闭右开的范围限制，可以使用until关键字。  
```gradle
for (i in 1 until 100) { ... }
```
检查一个数字是否不再某个范围内。  
```gradle
val list = listOf("a", "b", "c")
if (-1 !in 0..list.lastIndex) {
	println("-1 is out of range")
}
if (list.size !in list.indices) {
	println("list size is out of valid list indices range too")
}
```
遍历范围内的值：  
```gradle
for (x in 1..5) {
	print(x)
}
```
按照一定步数进行遍历：  
```gradle
for (x in 1..10 step 2) {
	println(x)
}
for (x in 9 downTo 0 step 3) {
	println(x)
}
```
请查看[Ranges](http://)。  
#### Using collections  
```gradle
val list = listOf("a", "b", "c")
for(item in list){
	println(item)
}
```
使用**in**操作符检查集合是否有一个对象在其中。  
```gradle
when {
	"orange" in items -> println("juicy")
	"apple" in items -> println("apple is fine too")
}
```
使用lambda表达式filter和map集合，请注意这里的map不是Java中的映射。  
```gradle
var fruits = listOf("apple", "atlas", "aaaaa", "bbbbbbbb)
fruits
	.filter { it.startsWith("a") }
	.sortedBy { it }
	.map { it.toUpperCase() }
	.forEach { println(it) }
```
请查看[Higher-order functions and Lambdas](http://)。  
### Idioms  
在kotlin中的一些常用术语，如果你想贡献自己的一份力量，可以提交一个Pull Request。  
创建DTOs(POJOs/POCOs)  
```gradle
data class Customer(val name: String, val email: String)
```
提供了一个**Customer**类，该类包含以下方法：  
- getters，对所有的属性适用
- equals()
- hashCode()
- toString()
- copy()
- component1()、component2().....

在函数中使用默认值：  
```gradle
fun foo(a: Int = 0, b: String = "") { ... }
```
使用了默认值的函数参数，如果默认值满足需求，那么可以不传入该参数。  
Filter列表：  
```gradle
val positives = list.filter { x -> x > 0 }
```
或者更简化的方式：  
```gradle
val positives = list.filter { it > 0 }
```
#### 字符串插值
```gradle
println("Name $name")
```
#### 实例检查
```gradle
when (x) {
	is Foo -> println("Foo instance")
	is Bar -> println("Bar instance")
	else -> println("unkown instance")
}
```
#### 遍历map/list对  
```gradle
for ((k, v) in map) {
	println("$k -> $v")
}
```
k、v可以被叫做任何名字。  
#### 只读list  
```gradle
val list = listOf("a", "b", "c")
```
#### 只读map  
```gradle
val map = mapOf("a" to 1, "b" to 2, "c" to 3)
```
#### 访问map的键值对  
```gradle
println(map["key"])
map["key"] = value
```
#### Lazy property  TODO
```gradle
val p: String by lazy {}
```
#### 扩展函数  
```gradle
fun String.spaceToCamelCase() { ... }
"Convert this to camelcase".spaceToCamelCase()
```
#### 创建单例  
```gradle
object Resource {
	val name = "Name"
}
```
#### 不为null的符号  
```gradle
val files = File("Test").listFiles()
println(files?.size)
```
#### 如果不为null和else符号  
```gradle
val files = File("Test").listFiles()
println(files?.size ?: "empty")
```
#### 如果为null执行一个语句  
```gradle
val data = ...
val email = data["email"] ?: throw IllegalStateException("Email is missing!")
```
#### 如果不为null执行一个语句  
```gradle
val data = ...
data?.let {}
```
#### 在when语句中返回  
```gradle
fun transform(color: String): Int {
	return when (color) {
				"Red" -> 0
				"Green" -> 1
				"Blue" -> 2
				else -> throw IllegalArgumentException("Invalid color param value")
	}
}
```
#### try/catch表达式  
```gradle
fun test() {
	val result = try {
		count()
	} catch (e: ArithmeticException) {
		throw IllegalStateException(e)
	}
}
```
#### Builder样式的方法并且返回类型为Unit  
```gradle
fun arrayOfMinusOnes(size: Int): IntArray {
	return IntArray(size).apply { fill(-1) }
}
```
#### 一个表达式的函数  
```gradle
fun theAnswer() = 42
```
这等价于  
```gradle
fun theAnswer(): Int {
	return 42
}
```
#### 调用实例的多个方法使用with关键字  
```gradle
class Turtle {
	fun penDown()
	fun penUp()
	fun turn(degrees: Double)
	fun forward(pixels: Double)
}
val myTurtle = Turtle()
with(myTurtle) { 
	penDown()
	for(i in 1..4) {
		forward(100.0)
		turn(90.0)
	}
	penUp()
}
```
#### Java 7的资源访问  
```gradle
val stream = Files.newInputStream(Paths.get("/some/file.txt"))
stream.buffered().reader().use { reader -> println(reader.readText())}
```
#### 使用泛型  
```gradle
inline fun <reified T: Any> Gson.fromJson(json): T = this.fromJson(json, T::class.java)
```
#### 消费一个可以为null的对象  
```gradle
val b: Boolean? = ...
if (b == true) {
	...
} else {
	// `b` is false or null
}
```
### Coding Conventions  







































