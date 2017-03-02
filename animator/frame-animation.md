## Frame Animation
将一系列图片按照一定的顺序展示，每一帧对应一个Drawable，与幻灯片的播放机制类似。
```gradle
AnimationDrawable animationDrawable = new AnimationDrawable();
Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_media_route_on_holo_light);
animationDrawable.addFrame(drawable, 300);
drawable = ContextCompat.getDrawable(this, R.drawable.ic_media_route_on_0_holo_light);
animationDrawable.addFrame(drawable, 300);
drawable = ContextCompat.getDrawable(this, R.drawable.ic_media_route_on_1_holo_light);
animationDrawable.addFrame(drawable, 300);
drawable = ContextCompat.getDrawable(this, R.drawable.ic_media_route_on_2_holo_light);
animationDrawable.addFrame(drawable, 300);
animationDrawable.setOneShot(false);
mAnimatedView.setBack[](http://)ground(animationDrawable);
animationDrawable.start();
```
```gradle
frame_animation.xml
<?xml version="1.0" encoding="utf-8"?>
<animation-list
    xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/ic_media_route_on_holo_light" android:duration="300" />
    <item android:drawable="@drawable/ic_media_route_on_0_holo_light" android:duration="300" />
    <item android:drawable="@drawable/ic_media_route_on_1_holo_light" android:duration="300" />
    <item android:drawable="@drawable/ic_media_route_on_2_holo_light" android:duration="300" />
</animation-list>
// 播放帧动画
mAnimatedView.setBackgroundResource(R.anim.frame_animation);
AnimationDrawable animationDrawable = (AnimationDrawable) mAnimatedView.getBackground();
animationDrawable.start();
```
帧动画的写法比较简单，但是在使用时需要注意帧动画的内存占用问题。
###[使用Frame Animation导致的内存问题讨论](http://stackoverflow.com/questions/8692328/causing-outofmemoryerror-in-frame-by-frame-animation-in-android)
经过整理得出如下结论：
- AnimationDrawable对应xml中的**animation-list属性**，其会在加载时把全部的Drawable加载，因此很容易造成内存溢出。
**[请查看下文源代码分析](http://)**。
- 可以自己实现xml的解析或直接创建BitmapDrawable对象，使用**BitmapFactory.Options的inPreferredConfig参数**创建更节约内存的Bitmap。如果仅仅是处理这一步还是会有可能导致内存溢出。
- 最重要的一点，在实现Frame Animation的时候，不使用AnimationDrawable，而是一帧一帧的将Drawable设置到对应的View上。

**[示例代码](https://github.com/tigerjj/FasterAnimationsContainer/blob/master/src/com/tigerlee/libs/FasterAnimationsContainer.java)**

## xml中定义的animation-list怎么与AnimationDrawable对应以及初始化？
```gradle
使用View.setBackgroundResource方法设置Drawable，Drawable最终会执行Resources的loadDrawableForCookie方法获得Drawable对象。
该方法的代码片段如下：
if (file.endsWith(".xml")) {
    final XmlResourceParser rp = loadXmlResourceParser(file, id, value.assetCookie, "drawable");
    dr = Drawable.createFromXml(this, rp, theme);
    rp.close();
} else {
    ...
}
继续查看Drawable.createFromXml方法
调用createFromXmlInner方法
public static Drawable createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs,
            Theme theme) throws XmlPullParserException, IOException {
        final Drawable drawable;
        final String name = parser.getName();
        switch (name) {
            case "selector":
                drawable = new StateListDrawable();
                break;
            case "animated-selector":
                drawable = new AnimatedStateListDrawable();
                break;
            case "level-list":
                drawable = new LevelListDrawable();
                break;
            case "layer-list":
                drawable = new LayerDrawable();
                break;
            case "transition":
                drawable = new TransitionDrawable();
                break;
            case "ripple":
                drawable = new RippleDrawable();
                break;
            case "color":
                drawable = new ColorDrawable();
                break;
            case "shape":
                drawable = new GradientDrawable();
                break;
            case "vector":
                drawable = new VectorDrawable();
                break;
            case "animated-vector":
                drawable = new AnimatedVectorDrawable();
                break;
            case "scale":
                drawable = new ScaleDrawable();
                break;
            case "clip":
                drawable = new ClipDrawable();
                break;
            case "rotate":
                drawable = new RotateDrawable();
                break;
            case "animated-rotate":
                drawable = new AnimatedRotateDrawable();
                break;
            case "animation-list":
                drawable = new AnimationDrawable();
                break;
            case "inset":
                drawable = new InsetDrawable();
                break;
            case "bitmap":
                drawable = new BitmapDrawable();
                break;
            case "nine-patch":
                drawable = new NinePatchDrawable();
                break;
            default:
                throw new XmlPullParserException(parser.getPositionDescription() +
                        ": invalid drawable tag " + name);

        }
        drawable.inflate(r, parser, attrs, theme);
        return drawable;
    }
在方法的最后，会调用AnimationDrawable对象的inflate方法。
继续调用inflateChildElements方法，在其中可以看到AnimationDrawable把所有的item都加载进去了。
```
**对XML文件解析不熟悉的同学请自行补习三种解析方式，Android中采用的pull方式**。