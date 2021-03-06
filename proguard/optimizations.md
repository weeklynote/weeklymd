## Optimizations
ProGuard的优化步骤可以通过**-dontoptimize **关闭。更细粒度的控制可以通过**-optimizations**(专家级命令),结合下面罗列出的过滤器。
> ?&nbsp;&nbsp;匹配待优化名字中的任何单个字符。
> \*&nbsp;&nbsp;匹配待优化名字中的任意部分。

优化之前使用”!”是排除在与后续进一步尝试匹配过滤器优化的名字。确保正确地指定过滤器,因为他们不检查潜在的错误。
比如：**"code/simplification/variable,code/simplification/arithmetic"**只执行指定的两个窥孔优化。
**"!method/propagation/\*"**执行所有的优化，除了方法之间的传递值。
**"!code/simplification/advanced,code/simplification/*"**仅仅只会执行所有的窥孔优化。
一些优化必然地蕴含其他优化。请注意列表可能会随时间改变,优化可能被添加和重组。
```
class/marking/final
```
将class标记成final。
```
class/merging/vertical
```
在类层次结构上垂直合并类。
```
class/merging/horizontal
```
在类层次结构上水平合并类。
```
field/removal/writeonly
```
删除只写的域。
```
field/marking/private
```
将域标记为private。
```
field/propagation/value
```
在方法间传递域的值。
```
method/marking/private
```
将方法标记为private。
```
method/marking/static
```
将方法标记为static。
```
method/marking/final
```
将方法标记为final。
```
method/removal/parameter
```
删除未被使用的方法参数。
```
method/propagation/parameter
```
从方法调用中传播方法参数值到被调用的方法。
```
method/propagation/returnvalue
```
从方法传递方法的返回值到调用处。
```
method/inlining/short
```
内联短方法。
```
method/inlining/unique
```
内联只被调用了一次的方法。
```
method/inlining/tailrecursion
```
简化尾递归调用。
```
code/merging
```
通过修改分支目标合并相同的代码块。
```
code/simplification/variable
```
执行窥孔优化变量加载和存储。
```
code/simplification/arithmetic
```
执行窥孔优化运算指令。
```
code/simplification/cast
```
执行窥孔优化转换操作。
```
code/simplification/field
```
执行窥孔优化域加载和存储。
```
code/simplification/branch
```
执行窥孔优化分支命令。
```
code/simplification/string
```
执行窥孔优化常量字符串。
```
code/simplification/advanced
```
基于控制流和数据流分析简化代码。
```
code/removal/advanced
```
基于控制流和数据流分析删除无用代码。
```
code/removal/simple
```
基于控制流分析删除无用代码。
```
code/removal/variable
```
从本地变量框架中删除未使用的变量。
```
code/removal/exception
```
从空的try语句块中删除异常。
```
code/allocation/variable
```
在本地变量框架上优化变量内存分配。