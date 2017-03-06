## Hashtable
**Hashtable**是**map**的一种同步实现，即其操作是线程安全的。但是不允许key或value为null，如果你需要key或value为null，你可以使用**[HashMap](https://github.com/weeklynote/weeklymd/blob/master/java/hashmap.md)**或**[LinkedHashMap](http://)**。
## 创建Hashtable
Hashtable与HashMap的代码有很多类似。
```gradle
	// Hashtable中存储数据的数组
    private transient Entry<?,?>[] table;
    // Hashtable中总的Entry个数
    private transient int count;
    // 阈值，超过会进行扩容，应该尽量减少扩容次数
    private int threshold;
    // 加载因子，和性能相关，后有详细讲解
    private float loadFactor;
```
```gradle
public Hashtable(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity);
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal Load: "+loadFactor);
        if (initialCapacity==0)
            initialCapacity = 1;
        this.loadFactor = loadFactor;
        table = new Entry<?,?>[initialCapacity];
        threshold = (int)Math.min(initialCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
    }
```
通过与**[HashMap](https://github.com/weeklynote/weeklymd/blob/master/java/hashmap.md)**的对比可以看到，**Hashtable**与**HashMap**的数据储存结构相同，对于阈值的设计原理也是相同的。**但是Hashtable不强制要求数组的大小是2的幂次方**。
## put键值对
```gradle
public synchronized V put(K key, V value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Entry<?,?> tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        @SuppressWarnings("unchecked")
        Entry<K,V> entry = (Entry<K,V>)tab[index];
        for(; entry != null ; entry = entry.next) {
            if ((entry.hash == hash) && entry.key.equals(key)) {
                V old = entry.value;
                entry.value = value;
                return old;
            }
        }
        addEntry(hash, key, value, index);
        return null;
    }
```
从上述代码中可以看出，**Hashtable**使用hashCode来计算出对应的在table中的索引，如果在索引处存在与key对应的value，那么将使用新值替换旧值，**注意此时的对应Entry上的链表结构未发生变化**。如果没有找到对应的key储存的value，那么将继续执行。
```gradle
private void addEntry(int hash, K key, V value, int index) {
        modCount++;
        Entry<?,?> tab[] = table;
        if (count >= threshold) {
            rehash();
            tab = table;
            hash = key.hashCode();
            index = (hash & 0x7FFFFFFF) % tab.length;
        }
        @SuppressWarnings("unchecked")
        Entry<K,V> e = (Entry<K,V>) tab[index];
        tab[index] = new Entry<>(hash, key, value, e);
        count++;
    }
```
添加值之前先判断是否需要扩容处理，然后将key-value存放到数组的对应索引位置，请注意**Entry**构造方法的最后一个参数，如果tab[index]不为空，意即为此时产生了hash冲突，那么此时table数组中的**Entry**元素将是一个链表的数据结构，可以查看**Entry**的next属性进行理解这种数据结构。
## get键值对
```gradle
public synchronized V get(Object key) {
        Entry<?,?> tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry<?,?> e = tab[index] ; e != null ; e = e.next) {
            if ((e.hash == hash) && e.key.equals(key)) {
                return (V)e.value;
            }
        }
        return null;
    }
```
在获取键值对时，原理是首先根据key的hashCode值进行算法处理得到索引，在查看table数组的索引位置处是否存在对应的**Entry**，条件是key的equals方法要相等。如果以上条件都不满足那么将返回null。
## 性能问题
初始化容量和加载因子，这两个参数影响**Hashtable**性能的重要参数。初始化容量是**Hashtable**刚刚创建时table数组的长度，加载因子是当容量自动增加到某种界限时，需要对table进行扩容操作。也就是说初始化容量和加载因子共同决定了在什么样的情况下进行扩容。扩容会导致table数组增大一倍，还会对里面的元素进行重新的计算操作，这是有损性能的，应该尽量减少扩容的次数。
## 其他
**Hashtable**中的其他方法不在累述。