## HashMap
**HashMap**大致与HashTable等价，除了其未使用**synchronized**和允许null的key或value，**HashMap**不保证元素的顺序。  
**HashMap**提供put与get方法操作元素，元素被散列的分布。如果遍历的性能很重要，需要注意初始化的容量和加载因子两个参数，他们会影响性能，至于原因请看下文。
同时需要注意到**HashMap**并未使用**synchronized**关键字，所以并发操作是不安全的。  
如果在**iterators**创建之后，Map的结构被修改了(包括增加、删除操作，不包含内容修改操作)，**iterators**将抛出**ConcurrentModificationException**。
## 创建HashMap
在解析代码之前需要对一些变量进行说明，如下：
```gradle
// 默认的初始化大小
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
// 最大的容量大小
static final int MAXIMUM_CAPACITY = 1 << 30;
// 加载因子，值为0.75是出于时间空间消耗的平衡
static final float DEFAULT_LOAD_FACTOR = 0.75f;
static final Entry<?,?>[] EMPTY_TABLE = {};
// 可以看到HashMap的内容使用数组进行存储，Entry将在后续解析其数据结构
transient Entry<K,V>[] table = (Entry<K,V>[]) EMPTY_TABLE;
```
```gradle
public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +  initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor:" + loadFactor);
        this.loadFactor = loadFactor;
        threshold = initialCapacity;
        init();
    }
```
以上构造方法会被最终调用，可以看到**HashMap**最大容量为**MAXIMUM_CAPACITY**，请牢记构造方法的两个参数会影响性能，后续会继续说明其如何影响性能。
## put键值对
```gradle
public V put(K key, V value) {
    if (table == EMPTY_TABLE) {
        inflateTable(threshold);
    }
    if (key == null)
        return putForNullKey(value);
    int hash = hash(key);
    int i = indexFor(hash, table.length);
    for (Entry<K,V> e = table[i]; e != null; e = e.next) {
         Object k;
         if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
             V oldValue = e.value;
             e.value = value;
             e.recordAccess(this);
             return oldValue;
         }
     }
     modCount++;
     addEntry(hash, key, value, i);
     return null;
}
```
初次调用put方法时，会创建table
```gradle
private void inflateTable(int toSize) {
        int capacity = roundUpToPowerOf2(toSize);
        threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
        table = new Entry[capacity];
        initHashSeedAsNeeded(capacity);
}
```
**其中capacity的大小需要注意**，此时阀值threshold会被更新为capacity与loadFactor的乘积。
```gradle
private static int roundUpToPowerOf2(int number) {
        return number >= MAXIMUM_CAPACITY
                ? MAXIMUM_CAPACITY
                : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;
}
```
可以看到如果HashMap的大小未达到MAXIMUM_CAPACITY时，总会返回比传入参数大的最近的2次幂的容量大小。
比如传入9，比9大的最近的一个2次幂是2的四次方(即16)。  
**如果put方法时，key为null**。
```gradle
private V putForNullKey(V value) {
        for (Entry<K,V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }
        modCount++;
        addEntry(0, null, value, 0);
        return null;
}
```
可以看到初次添加key为null的键值对时，我们将其添加到了table中索引为0的位置上。如果非初次添加key为null的键值对时，会将value更新并返回以前的value。
**接着看put方法下面的逻辑，key不为null，首先根据key计算散列值**。
```gradle
final int hash(Object k) {
        int h = hashSeed;
        if (0 != h && k instanceof String) {
            return sun.misc.Hashing.stringHash32((String) k);
        }
        h ^= k.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
}
```
hash方法有多个版本的实现，可以看到对key为**String**的类型进行了特殊处理，我们知道**String**的hashCode即使相同，其内容也可以不同，所以上述方法增强了在计算散列值时的处理。
**拿到散列值后，算出散列值对应的数组索引值**。
```gradle
static int indexFor(int h, int length) {
        return h & (length-1);
}
```
由于我们知道**HashMap**的大小是2的幂次方，所以(length - 1)为奇数。所以经过计算之后的索引不会超过(length - 1),并且索引可能是奇数或偶数，刚好根据不同的hash分布在table数组不同位置中。但是笔者还是没理解hash为什么要按照hash方法所述的方式去实现。
**put方法后面的逻辑就比较好理解了**。
```gradle
1.如果在索引位置没有添加过数据，那么就将数据添加在索引位置。
2.如果在索引位置已经有值了，并且key对应的键值对已经存在，那么将会用新的value代替旧的value，并返回旧的value。
3.如果索引位置已经有值了，但是key对应的键值对不存在，将会执行addEntry方法，但是此时会将索引位置处的值赋值成新的键值对，并且让新的键值对指向原来索引位置处的值，请好好理解次数据结构类型。
4.这里需要注意Entry的数据结构，它是个链表结构，有个next变量指向下一个节点。
```
**下面看看addEntry方法怎么实现上述描述细节的**
```gradle
void addEntry(int hash, K key, V value, int bucketIndex) {
        if ((size >= threshold) && (null != table[bucketIndex])) {
            resize(2 * table.length);
            hash = (null != key) ? hash(key) : 0;
            bucketIndex = indexFor(hash, table.length);
        }
        createEntry(hash, key, value, bucketIndex);
}
```
这里先不讨论扩容的问题
```gradle
void createEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K,V> e = table[bucketIndex];
        table[bucketIndex] = new Entry<>(hash, key, value, e);
        size++;
}
```
请注意上述方法中的table[bucketIndex]值，在hash冲突的情况下，其值不会为null；否则其值为null。
```gradle
Entry(int h, K k, V v, Entry<K,V> n) {
            value = v;
            next = n;
            key = k;
            hash = h;
}
```
可以看到在hash冲突的情况下，新添加的Entry对象的next会指向旧的Entry对象。至此可以想象一下HashMap保存的真正数据结构类型，即数组链表结构。
## get
```gradle
public V get(Object key) {
        if (key == null)
            return getForNullKey();
        Entry<K,V> entry = getEntry(key);
        return null == entry ? null : entry.getValue();
}
```
如果key为null
```gradle
private V getForNullKey() {
        if (size == 0) {
            return null;
        }
        for (Entry<K,V> e = table[0]; e != null; e = e.next) {
            if (e.key == null)
                return e.value;
        }
        return null;
}
```
代码比较简单，不再赘述，下面重点看看key不为null的情况
```gradle
final Entry<K,V> getEntry(Object key) {
        if (size == 0) {
            return null;
        }
        int hash = (key == null) ? 0 : hash(key);
        for (Entry<K,V> e = table[indexFor(hash, table.length)];
             e != null;
             e = e.next) {
            Object k;
            if (e.hash == hash &&
                ((k = e.key) == key || (key != null && key.equals(k))))
                return e;
        }
        return null;
}
```
可以看到计算索引时采用了与put一致的方式，但是此处有两种情况需要说明
- 如果**HashMap**中table数组索引处是一个长度为1的Entry链表，如果找到相同的key，那么将其返回；否则返回null。
- -如果**HashMap**中table数组索引处是一个长度大于1的Entry链表，即产生了hash冲突。那么将遍历这个完整的链表，如果找到相同的key，则返回其value；否则返回null。
- 其他情况都返回null

## 性能问题
初始化容量和加载因子，这两个参数影响**HashMap**性能的重要参数。初始化容量是**HashMap**刚刚创建时table数组的长度，加载因子是当容量自动增加到某种界限时，需要对table进行resize操作。也就是说初始化容量和加载因子共同决定了在什么样的情况下进行扩容。
```gradle
void addEntry(int hash, K key, V value, int bucketIndex) {
        if ((size >= threshold) && (null != table[bucketIndex])) {
            resize(2 * table.length);
            hash = (null != key) ? hash(key) : 0;
            bucketIndex = indexFor(hash, table.length);
        }
        createEntry(hash, key, value, bucketIndex);
}
```
可以看到在不超过阀值并且产生了hash冲突时会进行扩容一倍
```gradle
void resize(int newCapacity) {
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable, initHashSeedAsNeeded(newCapacity));
        table = newTable;
        threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
}
```
扩容之后修改阀值，重新创建table，并且会重新计算新数组中的索引，这是个耗时操作，因此建议不触发扩容或少触发扩容操作以提升性能。
```gradle
void transfer(Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Entry<K,V> e : table) {
            while(null != e) {
                Entry<K,V> next = e.next;
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
}
```
正常情况下只需要使用默认的加载因子，其保证了空间与时间的平衡性，如果加载因子越大，会增加对空间的利用率，即对数组的利用率会增大，但是查找效率会降低(链表冲突会导致链表越来越长，链表在做查找操作时有性能缺陷问题)；如果加载因子太小，那么对空间的利用率会降低，造成内存浪费。此外，我们需要评估我们的代码，设置一个合适的容量大小以提高性能。