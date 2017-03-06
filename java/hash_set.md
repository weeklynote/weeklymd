## HashSet
**HashSet**实现于**Set**接口，使用**[HashMap](https://github.com/weeklynote/weeklymd/blob/master/java/hashmap.md)**作为数据支撑，它不会保证遍历的顺序，**允许null元素**。  
**注意方法不是同步的，如果有多线程进行操作需要进行同步处理**。也可以使用Set s = Collections.synchronizedSet(new HashSet(...))来创建一个线程安全的**Set**。
## 创建HashSet
```gradle
// HashSet的数据真正的储存在HashMap中
private transient HashMap<E,Object> map;
// HashSet中存储的所有value值同一个静态变量值
private static final Object PRESENT = new Object();
```
如果你还不了解**HashMap的原理**，**[请查看](https://github.com/weeklynote/weeklymd/blob/master/java/hashmap.md)**。
```gradle
public HashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
}
```
仅仅是初始化了**HashMap**。
## add值
```gradle
public boolean add(E e) {
        return map.put(e, PRESENT)==null;
}
```
通过**HashMap**的内部原理可以知道，其put方法会在新添加元素至**HashMap**时；或者如果新添加的值产生了hash冲突，并且在**Node**链中找不到与key对应的值时才会返回null。所以我们得知在这两种情况下，**HashSet才算把元素添加成功**！
## remove值
```gradle
public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
}
```
通过**HashMap**的内部原理可以知道，当待删除的对象被成功删除后会返回一个非null的**Node**。而我们存储的是同一个**PRESENT**对象，所以可以使用等于符号判断是否是删除成功。
## iterator遍历
```gradle
public Iterator<E> iterator() {
        return map.keySet().iterator();
}
```