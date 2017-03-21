## ArrayList
实现**List**接口，包含**List**的所有操作，并且允许**null**元素。**ArrayList**与**Vector**等价，除了**同步操作**。另外其内部存在一个**capacity**，用来指明存储元素的列表大小，该值的大小至少与**size**值相等，由于**capacity**会自动增长以满足元素的存储。所以在实际使用的过程中要注意减少**capacity**的增长次数，减少开销。比如在添加大量元素时，最好指定一个初始化容量大小来减少**capacity**增长次数，减少内存的重新分配次数等。  
需要注意的是**ArrayList**的操作不是同步的，意味着多线程操作会导致冲突。此时可以使用一个**Object**对象，通过同步该对象来完成**ArrayList**同步操作，或者：  
```gradle
Collections.synchronizedListdList(List<T> list)
```
该方法已经封装好了同步实现，两种方式本质是一致的。  
## 构造ArrayList
```gradle
public ArrayList(int initialCapacity) {
    super();
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal Capacity: "+initialCapacity);
    this.elementData = new Object[initialCapacity];
}
```
其他的构造方法类似，可以自行了解。  
可以看到**ArrayList**的内部存储数据结构为数组，因此其特性与数组特性一致。  
- 只能存储同一种数据类型的数据  
- 一旦初始化，长度固定  
- 数组中的元素与元素之间的内存地址是连续的，元素存在先后顺序  
- 元素的索引速度很快  

## 添加值
```gradle
public boolean add(E e) {
    ensureCapacityInternal(size + 1);
    elementData[size++] = e;
    return true;
}
```
元素在添加之前需要判断是否需要增大容量。
```gradle
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == EMPTY_ELEMENTDATA) {
         minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    ensureExplicitCapacity(minCapacity);
}
```
如果在初始化**ArrayList**时并未指定容量大小，此时将容量大小指定为默认容量大小(1.7版本该值为10)。
```gradle
 private void ensureExplicitCapacity(int minCapacity) {
     modCount++;
     if (minCapacity - elementData.length > 0)
         grow(minCapacity);
}
```
可以看出要增大容量时有条件的。如果构造**ArrayList**时未传入容量大小，那么此时一定会满足上述代码的if条件从而进入下面的代码；或者指定了容量大小并且**size**已经大于容量大小也将进入下面的代码。
```gradle
private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```
上述代码将容量按照一定规则增大为一定的值。可以看到经过上面的操作之后，就可以直接在数组的**(size)**位置直接插入元素了。
## 删除值
包括按照索引删除**remove(int index)**和按照值删除**remove(Object o)**。
```gradle
public E remove(int index) {
    rangeCheck(index);
    modCount++;
    E oldValue = elementData(index);
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index, numMoved);
    elementData[--size] = null;
    return oldValue;
}
```
先对索引范围进行检查，在拿到待删除索引的对应值，如果不是删除的末尾元素，那么还需要对数组内容进行移动，这里有个小技巧，使用**索引+1**的位置开始的内容覆盖**索引**位置开始的内容，并将最后一个元素清空达到间接删除的效果。  
```gradle
public boolean remove(Object o) {
    if (o == null) {
        for (int index = 0; index < size; index++)
            if (elementData[index] == null) {
                fastRemove(index);
                return true;
            }
    } else {
        for (int index = 0; index < size; index++)
            if (o.equals(elementData[index])) {
                fastRemove(index);
                return true;
            }
    }
    return false;
}
```
如果待删除对象为空，那么将第一个为空的元素删除；否则就查找与数组中相等的元素进行删除。
```gradle
private void fastRemove(int index) {
    modCount++;
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index, numMoved);
    elementData[--size] = null;
}
```
可以看到代码与上述的根据索引删除的代码实现类似，不在累述。  
## 获取值
```gradle
public E get(int index) {
    rangeCheck(index);
    return elementData(index);
}
```
不在累述。至于**iterator**方法与次类似，请自行查看源码。
