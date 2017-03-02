## Property Animation
View Animation仅仅是改变了视图的显示，并不能真正的做交互，其也不能做到一些复杂的动画效果(比如3D旋转)，对动画的顺序控制也存在不足，如是种种，悲伤之情难以名状。  
基于这样的现状，出现了Property Animation，其通过调用属性的setter、getter方法真实的改变了View的属性，几乎可以实现所有的动画效果。  
本文涉及的例子均来自APIDemos，常用的类如下所示：
- **[ObjectAnimator](http://)**
- **[ValueAnimator](http://)**
- **[AnimatorSet](http://)**
- **[PropertyValuesHolder](http://)**

## ObjectAnimator
提供了ofFloat、ofInt、ofMultiInt、ofObject、ofPropertyValuesHolder静态类工厂方法创建ObjectAnimator，以ofFloat方法为例进行讲解，其他使用方式请查看工程代码。
```gradle
ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "translationX", 200);
objectAnimator.setDuration(4000);
objectAnimator.start();
```
```gradle
object_animator.xml
<?xml version="1.0" encoding="utf-8"?>
<objectAnimator
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:propertyName="translationX"
    android:valueTo="300"
    android:duration="4000" />
// 加载动画
Animator objectAnimator = AnimatorInflater.loadAnimator(this, R.anim.object_animator);
objectAnimator.setTarget(v);
objectAnimator.start();
```
其中xml中的属性包括：
- propertyName:View的属性名称，例如translationX。
- interpolator：插值器。
- duration：动画持续时间。
- startOffset：动画开始延迟时间。
- valueType：变化值类型。
> **VALUE_TYPE_FLOAT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;即数字0**
> **VALUE_TYPE_INT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;即数字1**
> **VALUE_TYPE_PATH&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;即数字2**
> **VALUE_TYPE_COLOR&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;即数字3**
> **VALUE_TYPE_UNDEFINED&nbsp;&nbsp;&nbsp;即数字4**
- valueFrom：变化开始值。
- valueTo：变化结束值。
- repeatCount：动画重复次数。
> **INFINITE&nbsp;&nbsp;&nbsp;&nbsp;即数字-1代表无限次循环，或使用其他大于0的值**
- repeatMode：重复模式，前提是重复次数为-1(无限重复)或大于0。
> RESTART&nbsp;&nbsp;&nbsp;&nbsp;代表重新开始动画
> REVERSE&nbsp;&nbsp;&nbsp;&nbsp;代表反向开始动画

另外需要注意一些常用属性可以通过View的静态常量ALPHA、TRANSLATION_X、TRANSLATION_Y、TRANSLATION_Z、X、Y、Z、ROTATION、ROTATION_X、ROTATION_Y、SCALE_X、SCALE_Y查看。