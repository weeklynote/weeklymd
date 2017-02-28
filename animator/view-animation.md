## View Animation
视图动画提供了**TranslateAnimation**、**ScaleAnimation**、**RotateAnimation**、**AlphaAnimation**，同时还提供了**AnimationSet**动画集合。

#### TranslateAnimation
为视图移动时增加移动动画效果。
```gradle
TranslateAnimation translateAnimation = new TranslateAnimation(0, 50, 0, 50);
// 设置执行时间长是为了便于观看动画效果
translateAnimation.setDuration(8000);
mAnimView.startAnimation(translateAnimation);
```