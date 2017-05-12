## Usage  
```gradle
FragmentManager fragmentManager = getSupportFragmentManager();
PagerAdapterFactory factory = new PagerAdapterFactory(this);
final PagerAdapter pagerAdapter = factory.getPagerAdapter(fragmentManager);
final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
viewPager.setAdapter(pagerAdapter);
Provider<Integer> provider = new Provider<Integer>() {
      @Override
      public Integer get(int position) {
          return pagerAdapter.getColour(position);
      }
};
Property<Integer> property = new Property<Integer>() {
      @Override
      public void set(Integer value) {
          viewPager.setBackgroundColor(value);
      }
};
animator = ViewPagerAnimator.ofArgb(viewPager, provider, property);
```  
需要注意的是Provider与Property接口均采用泛型，可以实现任意类型的参数。最后需要在界面销毁时调用destroy方法。  
```gradle
@Override
protected void onDestroy() {
    animator.destroy();
    super.onDestroy();
}
```
## 原理  
通过源码可以看到，ViewPagerAnimator库的实现关键代码为：  
```gradle
@Override
public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    if (position == 0 && positionOffsetPixels == 0 && !isAnimating()) {
         onPageSelected(0);
    }
    if (isAnimating() && lastPosition != position || positionOffsetPixels == 0) {
         endAnimation(position);
    }
    if (positionOffsetPixels > 0) {
         beginAnimation(position);
    }
    if (isAnimating()) {
         float fraction = interpolator.getInterpolation(positionOffset);
         V value = evaluator.evaluate(fraction, startValue, endValue);
         property.set(value);
    }
     lastPosition = position;
}
```
要明白其实现代码，你需要了解插值相关(Interpolator、TypeEvaluator)、onPageScrolled三个参数的含义。  
在获取到两个页面的相关参数后，根据插值器计算出中间值，通过Property设置进行回调，然后界面可以直接使用这个中间值设置空间的相关属性，故实现了与手势相关的动画效果。具体实现过程还考虑了左滑和右滑。

