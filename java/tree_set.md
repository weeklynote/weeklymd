## TreeSet
**TreeSet**基于**TreeMap**，是**NavigableSet接口**的实现。元素按照**Comparable**自然排序或按照提供的**Comparator**进行排序，使用时可以选择不同的构造方法来达到上述排序要求。  
**TreeSet**提供时间复杂度为**log(n)**的基本操作(add、remove、contains)。  
**请注意方法实现不是同步的，因此多线程操作时需要自己单独做同步处理。**
或者使用SortedSet s = Collections.synchronizedSortedSet(new TreeSet(...))。
## 创建TreeSet对象
```gradle
	// 指向TreeMap实例对象
    private transient NavigableMap<E,Object> m;
    private static final Object PRESENT = new Object();
```
```gradle
public TreeSet() {
        this(new TreeMap<E,Object>());
}
```