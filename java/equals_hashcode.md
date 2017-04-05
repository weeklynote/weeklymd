**Obejct**是一个具体的类，它的非final方法(hashCode、equals)都有明确的通用约定，这些方法被设计成需要被覆盖的。任何一个类在覆盖这些方法时都有责任来遵守这些约定，如果不能遵守这些约定，那么将有可能导致其他类(HashMap、HashCode)不能正常工作。
## 覆盖equals时请遵守通用约定
**最容易避免因为覆盖equals方法而导致问题的办法就是不覆盖equals方法**，在这种情况下，类的实例都只与自身相等，通常包含以下几种情况：
- 类的每个实例本质上都是唯一的  
例如Thread，代表其实例的并不是其中的值。
- 不关心类是否提供了“逻辑相等”的测试功能  
例如java.util.Random覆盖了equals，以检查两个Random实例是否产生相同的随机数序列，但是设计者并不认为开发者需要或期望这样的功能。在这样的情况下，继承自**Object**equals方法已经满足需要，不需要重写这个方法。  
- 超类已经覆盖了equals，从超类继承过来的行为对于子类也是合适的。  
例如大多数的**Set**都是继承自**AbstractSet**的equals方法；**List**实现从**AbstractList**继承equals方法；**Map**从**AbstractMap**继承equals方法。  
- 采用实例受控确保每个值最多只存在一个对象的类  
例如枚举，对于这样的类而言，逻辑相同与对象等同是一回事。

**如果类具有自己特有的“逻辑相等”概念(不同于Object总的对象等同概念)，而且超类还没有覆盖equals以实现期望的行为**，这时我们就需要覆盖equals方法，这通常属于“值类”的情形。值类仅仅是一个表示值的类，例如**Integer或者Date**。开发者在利用equals方法来比较值对象引用时，希望知道它们在逻辑上是否相等，而不是想了解它们是否指向同一个对象。为了满足这个要求，不仅必须覆盖equals方法，而且这样做也使得类的实例可以被用作**Map**的键(key)，或者集合(Set)的元素，使这类数据结构表现出预期的行为。  
equals方法实现了等价关系，必须遵守以下通用约定：
- 自反性  
对于任何非null的对象x，x.equals(x)必须返回true。
- 对称性  
对于任何非null的对象x、y，当且仅当y.equals(x)返回true时，x.equals(y)必须返回true。
- 传递性  
对于任何非null的对象x、y、z，如果x。equals(y)返回true，并且y.equals(z)也返回true，那么x.equals(z)也必须返回true。
- 一致性  
对于任何非null的对象x、y，只要equals的比较操作在对象中所用的信息没被修改，多次调用x.equals(y)就会一致地返回true，或者一致地返回false。
- 对于任何非null的对象x，x.equals(null)必须返回false。  

##### 自反性  
对象必须等于其自身，很难想象会无意识的违反这一条。假如违背了这一条，然后把该类的添加到集合(Collection)中，该集合的contains方法将会出现不正常的行为。  
##### 对称性  
任何两个对象对于它们是否相等的问题必须保持一致，这种情形可以参考以下类的实例。
```gradle
public class CaseInsensitiveString {
	private final String s;
	
	public CaseInsensitiveString(String s){
		this.s = s;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CaseInsensitiveString){
			return s.equalsIgnoreCase(((CaseInsensitiveString) obj).s);
		}
		if(obj instanceof String){
			return s.equalsIgnoreCase((String)obj);
		}
		return false;
	}
	
	public static void main(String[] args) {
		CaseInsensitiveString cis = new CaseInsensitiveString("polish");
		String s = "PoliSh";
        // true
		System.out.println(cis.equals(s));
        // false
		System.out.println(s.equals(cis));
	}

}
```
其中**CaseInsensitiveString**中的equals方法知道处理**String**对象，但是反过来，**String**中的equals方法却并不知道不区分大小写，违反对称性原则，将会遇到以下问题：  
```gradle
CaseInsensitiveString cis = new CaseInsensitiveString("polish");  
String s = "PoliSh";  
List<CaseInsensitiveString> list = new ArrayList<>(4);  
list.add(cis);  
// false  
System.out.println(list.contains(s));  
// true  
System.out.println(list.contains(new CaseInsensitiveString(s)));  
```
没人会知道list.contains方法将返回什么结果。解决这个问题只需要把企图与**String**互操作的代码从equals方法去掉就可以了。  
```gradle
@Override
public boolean equals(Object obj) {
	return (obj instanceof CaseInsensitiveString)
		&& (s.equalsIgnoreCase(((CaseInsensitiveString) obj).s));
}
```
##### 传递性
如果第一个对象等于第二个对象，第二个对象等于第三个对象，那么第一个对象与第三个对象相等。违反这条规则的情形不难想象，考虑子类添加新的值，新加的信息会影响到equals比较结果。
```gradle

public class Point {
	private final int x;
	private final int y;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Point))
			return false;
		Point p = (Point) obj;
		return p.x == x && p.y == y;
	}
}

```
```gradle
public class ColorPoint extends Point{
	
	private final Color color;

	public ColorPoint(int x, int y, Color color) {
		super(x, y);
		this.color = color;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ColorPoint))
			return false;
		return super.equals(obj) && ((ColorPoint)obj).color == color;
	}
	
}
```
首先可以看到上面的例子是违反对称性的，因此需要修改**ColorPoint**中的equals方法。
```gradle
@Override
public boolean equals(Object obj) {
	if(!(obj instanceof Point))
		return false;
	if(!(obj instanceof ColorPoint))
		return obj.equals(this);
	return super.equals(obj) && ((ColorPoint)obj).color == color;
}
```
在上述代码中虽然对称性问题解决了，但是传递性是不满足的。
```gradle
Point p1 = new Point(1, 11);
ColorPoint p2 = new ColorPoint(1,  11, Color.BLUE);
ColorPoint p3 = new ColorPoint(1,  11, Color.WHITE);
// true
System.out.println(p2.equals(p1));
// true
System.out.println(p1.equals(p3));
// false
System.out.println(p2.equals(p3));
```
事实上，这是面向对象语言中关于等价关系的一个基本问题。**我们无法再扩张可实例化的类的同时，即增加新的属性，同时保留equals约定**。除非放弃面向对象的抽象所带来的优势。  
你可能会想到使用getClass替代instanceof来保持equals约定，将**Point**equals方法修改如下：  
```gradle
@Override
public boolean equals(Object obj) {
	if((obj == null) || obj.getClass() != getClass())
		return false;
	Point p = (Point) obj;
	return p.x == x && p.y == y;
}
```
虽然这样确实是可以满足equals约定，但是在以下的示例中可能是很荒谬的(仅仅用来展示效果，实际过程可能不会采用这种实现方式)。  
```gradle
private static final Set<Point> unitCircle;
	static{
		unitCircle = new HashSet<>();
		unitCircle.add(new Point(1, 0));
		unitCircle.add(new Point(-1,  0));
		unitCircle.add(new Point(0,  1));
		unitCircle.add(new Point(0,  -1));
	}
	
	public static boolean onUnitCircle(Point p){
		return unitCircle.contains(p);
	}
```
```gradle
public class CounterPoint extends Point{
	public CounterPoint(int x, int y) {
		super(x, y);
	}
}
```
可以看到此时有一个查看当前**Point**是否包含在单位圆中，如果**onUnitCircle**方法传递的参数是**CounterPoint**类型时，无论其值是什么，结果都会是返回false，这显然是难以接受的！  
折中的方法是采用内聚的方式而不是继承来实现扩展，可以改写如下：
```gradle
public class ColorPoint{
	private Point point;
	private final Color color;

	public ColorPoint(int x, int y, Color color) {
		point = new Point(x, y);
		this.color = color;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ColorPoint))
			return false;
		ColorPoint cp = (ColorPoint) obj;
		return cp.point.equals(obj) && cp.color.equals(color);
	}
}
```
注意，可以在抽象类(abstract)的子类中增加新的属性，而不会违反equals约定。例如创建一个抽象的Shape类，它没有任何属性；Circle类添加一个radius属性，Rectangle类添加length和width属性。**只要不可能直接创建超类实例，前面说的情况将不会发生**。














































