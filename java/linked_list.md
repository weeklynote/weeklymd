## LinkedList
实现**List**与**Deque**接口，包含**List**的所有操作，并且允许**null**元素，**LinkedList**是一个双链表结构。  
需要注意的是**LinkedList**的操作不是同步的，意味着多线程操作会导致冲突。此时可以使用一个**Object**对象，通过同步该对象来完成**ArrayList**同步操作，或者：  
```gradle
List list = Collections.synchronizedList(new LinkedList(...));
```
该方法已经封装好了同步实现，两种方式本质是一致的。  
## 构造LinkedList
```gradle
public LinkedList(Collection<? extends E> c) {
    this();
    addAll(c);
}
```
```gradle
public LinkedList() {}
```
```gradle
public boolean addAll(Collection<? extends E> c) {
    return addAll(size, c);
}
```
```gradle
public boolean addAll(int index, Collection<? extends E> c) {
    checkPositionIndex(index);
    Object[] a = c.toArray();
    int numNew = a.length;
    if (numNew == 0)
        return false;
    Node<E> pred, succ;
    if (index == size) {
        succ = null;
        pred = last;
    } else {
        succ = node(index);
        pred = succ.prev;
    }
    for (Object o : a) {
        @SuppressWarnings("unchecked")
        E e = (E) o;
        Node<E> newNode = new Node<>(pred, e, null);
        if (pred == null)
            first = newNode;
        else
            pred.next = newNode;
        pred = newNode;
    }
    if (succ == null) {
        last = pred;
    } else {
        pred.next = succ;
        succ.prev = pred;
    }
    size += numNew;
    modCount++;
    return true;
}
```
如果是将元素的位置插入在末尾，那么将新增加的所有元素以链式添加的末尾，可以通过**LinkedList.Node**定义进行查看；  
如果是将元素插入到链表中间位置，此时需要根据插入位置来判断是从头还是从尾进行插入操作，如下进行判断。
```gradle
Node<E> node(int index) {
    if (index < (size >> 1)) {
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}
```
**此时将元素插入到对应索引节点的上一个节点之后**。并将插入后的最后元素的next节点指向索引节点，索引节点的pre节点指向插入的最后元素节点。[如图所示](http://)
## 添加值
```gradle
public boolean add(E e) {
    linkLast(e);
    return true;
}
```
```gradle
void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;
    size++;
    modCount++;
}
```
元素在添加在链表的末尾。如果需要在指定位置添加元素，那么需要调用另一个重载方法。

```gradle
public void add(int index, E element) {
    checkPositionIndex(index);
    if (index == size)
        linkLast(element);
    else
        linkBefore(element, node(index));
}
```
如果插入的位置是末尾，那么就在末尾插入元素；否则在索引位置前插入元素。
```gradle
void linkBefore(E e, Node<E> succ) {
    final Node<E> pred = succ.prev;
    final Node<E> newNode = new Node<>(pred, e, succ);
    succ.prev = newNode;
    if (pred == null)
        first = newNode;
    else
        pred.next = newNode;
    size++;
    modCount++;
}
```
在索引位置处插入元素，即索引位置元素的上一个节点被设置为插入节点的上一个节点，索引位置处的节点被设置为插入节点的下一个节点，形成新的链表，**[如图所示](http://)**。
## 删除值
包括按照索引删除**remove(int index)**和按照值删除**remove(Object o)**。
```gradle
public E remove(int index) {
    checkElementIndex(index);
    return unlink(node(index));
}
```
```gradle
E unlink(Node<E> x) {
    final E element = x.item;
    final Node<E> next = x.next;
    final Node<E> prev = x.prev;
    if (prev == null) {
        first = next;
    } else {
        prev.next = next;
        x.prev = null;
    }
    if (next == null) {
        last = prev;
    } else {
        next.prev = prev;
        x.next = null;
    }
    x.item = null;
    size--;
    modCount++;
    return element;
}
```
查找到索引位置处的元素，如果查找到的元素的前一个节点为空，那么说明删除的是头结点，此时需要将头节点指向下一个节点；否则将待删除节点的上一个节点的next指向待删除节点的下一个节点将待删除的节点的prev设置为空。如果待删除节点的next为空，说明此时链表无元素，那么此时last(链表尾)将指向待删除节点的上一个元素；否则将待删除节点的下一个节点的prev设置成待删除节点的上一个节点。最后将待删除节点设置为空，达到删除的效果，**[如图所示](http://)**。
```gradle
public boolean remove(Object o) {
    if (o == null) {
        for (Node<E> x = first; x != null; x = x.next) {
            if (x.item == null) {
                unlink(x);
                return true;
            }
        }
    } else {
        for (Node<E> x = first; x != null; x = x.next) {
            if (o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
    }
    return false;
}
```
如果待删除对象为空，那么将第一个为空的元素从链表移除；否则就查找与数组中相等的元素进行链表移除。
## 获取值
```gradle
public E get(int index) {
    checkElementIndex(index);
    return node(index).item;
}
```
node方法如上所述，请翻阅。另外还存在**pop**、**push**、**pollLast**、**pollFirst**、**peek**等方法用于获取一个元素，使用到的代码已经在上述内容有所涉及，不在累述。
