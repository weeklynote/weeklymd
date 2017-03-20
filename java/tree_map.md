## TreeMap
**TreeMap**基于**NavigableMap**接口的**[红-黑二叉树结构](https://github.com/weeklynote/weeklymd/blob/master/Algorithm/red_black_tree.md)**。元素按照**Comparable**自然排序或按照提供的**Comparator**进行**排序**，使用时可以选择不同的构造方法来达到上述排序要求。  
**TreeMap**提供时间复杂度为**log(n)**的基本操作(containsXXX、get、put、remove)。  
**请注意方法实现不是同步的，因此多线程操作时需要自己单独做同步处理**  
或者  
``` gradle
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
``` gradle
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
首先查看root的赋值，它的值代表**[红黑二叉树](https://github.com/weeklynote/weeklymd/blob/master/Algorithm/red_black_tree.md)**的根节点。**首个添加的元素会被作为根节点**。  
如果继续添加元素，那么会根据**Comparator**或者**Comparable**来决定新元素插入的位置，以**Comparator**为例。  
``` gradle
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
do...while循环至少会执行一次，初次会将**t**赋值为root。
> 1.将parent也指向t；  
> 2.将key值与t.key进行compare方法比较；  
> 3.如果compare方法比较的结果小于0，那么将**t**重新赋值为t.left，然后再次执行1；  
> 4.如果compare方法比较的结果大于0，；那么将**t**重新赋值为t.right，然后再次执行1；  
> 5.如果compare方法比较的结果等于0，那么将使用新值替换旧值，循环结束；  
 
**请注意循环结束的条件是向root的左或右查找直到left或right为null，请结合[红黑树示例](https://github.com/weeklynote/weeklymd/blob/master/images/red_black_tree.png?raw=true)理解这个遍历过程，节点中left与right如果为null未被标记出来**。  
**红黑树的数据结构包括left、right、parent、color、key、value，节点的结构可以查看[红黑树节点](https://github.com/weeklynote/weeklymd/blob/master/images/tree_map_entry.png?raw=true)**  
**请在结合以上两图进行理解**TreeMap**的数据结构**。  
经过上面的节点查找，将找到新节点的插入位置。  
``` gradle
Entry<K,V> e = new Entry<>(key, value, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
```
cmp的值与上一步的查找节点中的compare值一致，至此节点就被插入到对应的位置处。**Comparable的实现方式与Comparator类似，略**。  
插入节点之后需要判断是否会导致红黑树失去平衡，即通过父节点的颜色来进行判断。  
```gradle
private void fixAfterInsertion(Entry<K,V> x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Entry<K,V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Entry<K,V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }
```
首先我们需要明确在节点插入之前，**红黑树**也是平衡的。从着色的方法来看，每个节点插入时默认的颜色为红色，尽管Entry的构造默认颜色为黑色。其他添加的情况具体可以查看**[红黑树的插入部分内容](https://github.com/weeklynote/weeklymd/blob/master/Algorithm/red_black_tree.md######插入)**。  
## get键值对
``` gradle
public V get(Object key) {
        Entry<K,V> p = getEntry(key);
        return (p==null ? null : p.value);
}
```
``` gradle
final Entry<K,V> getEntry(Object key) {
        if (comparator != null)
            return getEntryUsingComparator(key);
        if (key == null)
            throw new NullPointerException();
        Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K,V> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
}
```
以**Comparable**的情况为例，将root(根节点)赋值给p，即从根节点开始判断遍历的方向。  
> 将对象进行compareTo方法比较，值为cmp；  
> 如果cmp小于0，那么将p赋值为p.left，从节点的左边开始查找，继续遍历；  
> 如果cmp大于0，那么将p赋值为p.right，从节点的右边开始查找，继续遍历；  
> 如果cmp等于0，说明已找到满足key条件的节点，遍历结束；  

上述过程与**put**键值对时的查找算法一致，下面来看看**Comparator**的实现方式。  
``` gradle
final Entry<K,V> getEntryUsingComparator(Object key) {
        K k = (K) key;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            Entry<K,V> p = root;
            while (p != null) {
                int cmp = cpr.compare(k, p.key);
                if (cmp < 0)
                    p = p.left;
                else if (cmp > 0)
                    p = p.right;
                else
                    return p;
            }
        }
        return null;
}
```
可以看到代码与**Comparable**实现非常类似，只是比较方式从**compareTo**变成了**compare**，不在累述。  
## 删除键值对
``` gradle
public V remove(Object key) {
        Entry<K,V> p = getEntry(key);
        if (p == null)
            return null;
        V oldValue = p.value;
        deleteEntry(p);
        return oldValue;
}
```
首先查找到需要删除的**Entry**对象，如果**Entry**对象为null，说明未找到对应的节点，不需要执行删除操作。如果找到**Entry**对象，则执行删除操作。
``` gradle
private void deleteEntry(Entry<K,V> p) {
        modCount++;
        size--;
        // 待删除的节点存在两个子节点(left和right)
        if (p.left != null && p.right != null) {
            Entry<K,V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }
        // 这里需要注意待删除节点存在两个子节点的处理情况
        Entry<K,V> replacement = (p.left != null ? p.left : p.right);
        if (replacement != null) {
            // 将替换节点的父节点指向待删除节点的父节点，为后面的删除做准备
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;
            // 这里执行的是替换删除，而不是直接删除，将引用置空达到删除效果
            p.left = p.right = p.parent = null;

            // 删除节点为黑色很明显会破坏平衡，因此需要重新着色
            if (p.color == BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { 
            // 没有父节点说明其已经是根节点
            root = null;
        } else {
        	// 没有孩子的情况，此时可以直接删除节点(置空)
            if (p.color == BLACK)
                fixAfterDeletion(p);
            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
}
```
上述的代码分析更多详情可以参考**[红黑树的原理分析](https://github.com/weeklynote/weeklymd/blob/master/Algorithm/red_black_tree.md)**。
## 遍历
**请参考[红黑树的原理分析](https://github.com/weeklynote/weeklymd/blob/master/Algorithm/red_black_tree.md)**。
## 其他
其他操作相对比较简单，不再累述。