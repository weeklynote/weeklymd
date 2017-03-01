## Animation
本系列内容主要是为了解决实现特定的动画，将从基础API到原理部分进行详细讲解。

## 主要内容
- ###[View Animation](https://github.com/weeklynote/weeklymd/blob/master/animator/view-animation.md) 
视图动画，在较早的Android版本中就已经提供，其定义了透明度、旋转、缩放、位移四种常见的动画，其内部通过矩阵来完成动画。最大的缺点是经过变换后的视图不具备交互性，响应事件的位置依然在动画之前的位置。

- ###[Frame Animation](https://github.com/weeklynote/weeklymd/blob/master/animator/frame-animation.md)  |  [Drawable Animation](https://github.com/weeklynote/weeklymd/blob/master/animator/frame-animation.md)
帧动画，可以像放电影一样，一帧一帧的定义视图，这种动画的实质其实是Drawable，使用帧动画时需要注意内存的占用情况。

- ###[Property Animation](https://github.com/weeklynote/weeklymd/blob/master/animator/property-animation.md)
属性动画，Android3.0之后的强大动画框架，由于其通过getter和setter真实的改变了View的属性，所以解决了视图动画交互性问题。另外一个改进就是对动画的控制可以更精细了、能实现视图动画的一些不能实现的效果。

- ###[Layout Animation](https://github.com/weeklynote/weeklymd/blob/master/animator/layout-animation.md)
布局动画，作用在ViewGroup上，为添加View时增加过渡效果。

- ###[5.X SVG](https://github.com/weeklynote/weeklymd/blob/master/animator/svg.md)
Android&nbsp;&nbsp;5.X增加了对SVG矢量图形的支持，这有利于创建高效率动画。如果要在低版本上使用SVG，需要引入appcompat-v7:23.2.+的库。
```gradle
compile 'com.android.support:appcompat-v7:23.2.1'
```
首先来了解一下什么是SVG：
##### 1.SVG(Scalable Vector Graphics)可伸缩矢量图形#####
##### 2.图片改变尺寸不会影响图形质量#####
##### 3.通过xml定义图形可以创建丰富的动画效果#####
##### 4.不需要在为不同分辨率准备不同的素材，可以减小应用大小#####
