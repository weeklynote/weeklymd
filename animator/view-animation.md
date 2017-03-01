## View Animation
视图动画提供了**TranslateAnimation**、**ScaleAnimation**、**RotateAnimation**、**AlphaAnimation**，同时还提供了**AnimationSet**动画集合。

### TranslateAnimation
为视图移动时增加移动动画效果。
```gradle
TranslateAnimation translateAnimation = new TranslateAnimation(0, 100, 0, 100);
// 设置执行时间长是为了便于观看动画效果
translateAnimation.setDuration(4000);
mAnimatedView.startAnimation(translateAnimation);
```

``` gradle
translation.xml
<?xml version="1.0" encoding="utf-8"?>
<translate
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXDelta="0"
    android:toXDelta="100"
    android:fromYDelta="0"
    android:toYDelta="100"
    android:duration="4000" />
    // 加载动画
    mAnimatedView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translation));
```
其中有三个参数来表示坐标的值类型，请首先查看**Animation**如下代码：
```gradle
protected float resolveSize(int type, float value, int size, int parentSize) {
        switch (type) {
            case ABSOLUTE:
                return value;
            case RELATIVE_TO_SELF:
                return size * value;
            case RELATIVE_TO_PARENT:
                return parentSize * value;
            default:
                return value;
        }
    }
```
##### Animation.ABSOLUTE
**默认选项**，代表Animation设置的参数值就是真实的绝对值。**在xml设置中使用具体的值表示，比如100、200**。
##### Animation.RELATIVE_TO_SELF
代表Animation设置的参数值相对于自身的宽或高的比值。**在xml设置中使用百分比表示，比如100%相对于自身100%的宽或高的距离**。
##### Animation.RELATIVE_TO_PARENT
代表Animation设置的参数值相对于父空间的宽或高的比值。**在xml设置中使用百分比+p表示，比如50%p代表相对于父控件的50%的距离**。
### ScaleAnimation
为视图的缩放增加动画效果。
```gradle
ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1, 0.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.8f, 
Animation.RELATIVE_TO_SELF, 0.9f);
scaleAnimation.setDuration(4000);
mAnimatedView.startAnimation(scaleAnimation);
```
```gradle
scale.xml
<?xml version="1.0" encoding="utf-8"?>
<scale
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXScale="0.1"
    android:toXScale="1.0"
    android:fromYScale="0.1"
    android:toYScale="1.0"
    android:pivotX="50%"
    android:pivotY="50%"
    android:duration="4000" />
mAnimatedView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
```
其中Animation.RELATIVE_TO_SELF、Animation.RELATIVE_TO_PARENT、Animation.ABSOLUTE与上述内容的含义并无二致。
在上述构造方法中第六、第八个参数的含义为：
- 当设置为Animation.ABSOLUTE，代表以自身左上角为中心点进行缩放
- 当设置为Animation.RELATIVE_TO_SELF，代表以自身为中心点进行缩放
- Animation.RELATIVE_TO_PARENT，代表以父控件为中心进行缩放

其代码原理实现原理如下：
```gradle
mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
resolveSize方法与TranslateAnimation内容处的方法一致，此处不在贴代码。
```
在applyTransformation方法中有如下代码片段：
```gradle
if (mPivotX == 0 && mPivotY == 0) {
   t.getMatrix().setScale(sx, sy);
} else {
   t.getMatrix().setScale(sx, sy, scale * mPivotX, scale * mPivotY);
}
```
可以看到如果resolveSize方法获取到的mPivotX、mPivotY不是同时为0，将会将其作为中心点进行缩放。
### RotateAnimation
为视图增加旋转时的动画。
```gradle
RotateAnimation rotateAnimation = new RotateAnimation(45, 270, 
Animation.RELATIVE_TO_SELF, 0.5f, 
Animation.RELATIVE_TO_SELF, 0.5f);
rotateAnimation.setDuration(4000);
mAnimatedView.startAnimation(rotateAnimation);
```
```gradle
rotate.xml
<?xml version="1.0" encoding="utf-8"?>
<rotate
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromDegrees="45"
    android:toDegrees="270"
    android:pivotX="50%"
    android:pivotY="50%"
    android:duration="4000" />
mAnimatedView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
```
使用旋转动画时请注意构造函数的第四、第六个参数的含义与上述相关内容一致。
### AlphaAnimation
对视图的透明度变化时的动画。
```gradle
AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
alphaAnimation.setDuration(4000);
mAnimatedView.startAnimation(alphaAnimation);
```
0代表全透明，1代表全不透明。
```gradle
alpha.xml
<?xml version="1.0" encoding="utf-8"?>
<alpha
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromAlpha="0"
    android:toAlpha="1"
    android:duration="4000" />
mAnimatedView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha))
```
### AnimationSet
动画集合就是可以让多个动画一起运行，不能做到把集合中的动画按一定顺序进行组织并执行。这种缺陷可以利用duration延迟来勉强达到顺序执行的效果。
























































