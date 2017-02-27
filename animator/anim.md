## Animation
本系列内容主要是为了解决实现特定的动画，将从基础API到原理部分进行详细讲解。

## 主要内容
- ###[View Animation](http://) 
视图动画，在较早的Android版本中就已经提供，其定义了透明度、旋转、缩放、位移四种常见的动画，其内部通过矩阵来完成动画。最大的缺点是经过变换后的视图不具备交互性，响应事件的位置依然在动画之前的位置。

- ###[Frame Animation](http://)  |  [Drawable Animation](http://)
帧动画，可以像放电影一样，一帧一帧的定义视图，这种动画的实质其实是Drawable，使用帧动画时需要注意内存的占用情况。

- ###[Property Animation](http://)
属性动画，Android3.0之后的强大动画框架，由于其通过getter和setter真实的改变了View的属性，所以解决了视图动画交互性问题。另外一个改进就是对动画的控制可以更精细了、能实现视图动画的一些不能实现的效果。

- ###[Layout Animation](http://)
布局动画，作用在ViewGroup上，为添加View时增加过渡效果。

- ###[5.X SVG](http://)

