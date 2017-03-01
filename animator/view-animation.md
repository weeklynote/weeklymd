## View Animation
视图动画提供了**TranslateAnimation**、**ScaleAnimation**、**RotateAnimation**、**AlphaAnimation**，同时还提供了**AnimationSet**动画集合。

#### TranslateAnimation
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
**默认选项**，代表Animation设置的参数值就是真实的绝对值。使用具体的值表示，比如100、200。
##### Animation.RELATIVE_TO_SELF
代表Animation设置的参数值相对于自身的宽或高的比值。在xml设置中使用百分比表示，比如100%相对于自身100%的宽或高的距离。
##### Animation.RELATIVE_TO_PARENT
代表Animation设置的参数值相对于父空间的宽或高的比值。在xml设置中使用百分比+p表示，比如50%p代表相对于父控件的50%的距离。
## ScaleAnimation
为视图的缩放增加动画效果。

























































