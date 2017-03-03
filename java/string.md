## String













## String、StringBuffer与StringBuilder
String是只读的字符串，其内容是不能被修改的，在做大量字符串拼接时很消耗性能。StringBuffer、StringBuilder可以对字符串进行修改。StringBuilder是**JDK1.5**引入，它的使用方法与StringBuffer完全相同，唯一的区别在于StringBuilder的方法是非线程安全的，如果确认只会在单线程使用，那么使用StringBuilder的效率比使用StringBuffer高。StringBuffer是线程安全的，可以再多线程环境使用。通过查看两者的源代码可以发现，StringBuffer在相应方法中加入了**synchronized关键字**导致了上述差异。