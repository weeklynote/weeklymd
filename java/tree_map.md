## TreeMap
**TreeMap**基于**NavigableMap**接口的**[红-黑二叉树结构](http://)**。元素按照**Comparable**自然排序或按照提供的**Comparator**进行排序，使用时可以选择不同的构造方法来达到上述排序要求。  
**TreeMap**提供时间复杂度为**log(n)**的基本操作(add、remove、contains)。  
**请注意方法实现不是同步的，因此多线程操作时需要自己单独做同步处理**  
或者
```gradle
SortedSet s = Collections.synchronizedSortedSet(new TreeMap(...))。
```
## 创建TreeMap对象
```gradle
	// 单独的比较器(Comparator)，如果比较的对象实现了Comparable接口可以不设置这个参数
	private final Comparator<? super K> comparator;
    // 红-黑二叉树的根节点
    private transient Entry<K,V> root;
```
```gradle
public TreeMap(Comparator<? super K> comparator) {
		// 请注意是按照key的规则排序
        this.comparator = comparator;
}
```
## put键值对
```gradle
public V put(K key, V value) {
        Entry<K,V> t = root;
        if (t == null) {
            compare(key, key);
            root = new Entry<>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        Entry<K,V> parent;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        else {
            if (key == null)
                throw new NullPointerException();
                Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        Entry<K,V> e = new Entry<>(key, value, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
}
```
可以发现**TreeMap**与其他**Map**添加键值对的方式并无二致，但是需要注意的是与其他**Map**的数据存储结构的巨大差异。  
首先查看root的赋值，它的值代表**[红-黑二叉树](http://)**的根节点。**首个添加的元素会被作为根节点**。  
如果继续添加元素，那么会根据**Comparator**或者**Comparable**来决定新元素插入的位置，以**Comparator**为例。
```gradle
Comparator<? super K> cpr = comparator;
        ...
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        ...
```
do...while循环至少会执行一次，循环的作用是从root(根节点)开始，如果compare方法比较的结果小于0，那么将从根节点的左边再次开始遍历查找；如果compare方法比较的结果大于0，；那么将从根节点的右边开始遍历查找；如果compare方法比较的结果等于0，那么将使用新值替换旧值。**请注意循环结束的条件是向root的左或右查找直到left或right为null，请结合下图进行理解这个遍历过程**。