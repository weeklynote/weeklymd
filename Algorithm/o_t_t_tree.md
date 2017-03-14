##2-3-4树  
**2-3-4树**把数据存储在叫做元素的单独单元中，是红黑树结构的一种等同结构。所以理解**2-3-4树**成为理解红黑树背后的重要工具。其每个节点都是下列之一：
- 2-节点，就是说，它包含** 1 个元素和 2 个儿子**；
- 3-节点，就是说，它包含** 2 个元素和 3 个儿子**；
- 4-节点，就是说，它包含** 3 个元素和 4 个儿子**。

######2-节点  
**A**为元素节点，子节点**P**小于**A**节点，子节点**Q**大于**A**节点。  
<img src="https://github.com/weeklynote/weeklymd/blob/master/images/2_tree_struct.png?raw=true" alt="screenshot" title="screenshot" />
######3-节点  
**A B**为元素节点，子节点**x**小于**A**，子节点**y**介于**A与B**之间，**z**大于**B**。  
<img src="https://github.com/weeklynote/weeklymd/blob/master/images/3_tree_struct.png?raw=true" alt="screenshot" title="screenshot" />
######4-节点  
**A B C**为元素节点，子节点**M**小于**A**，子节点**N**介于**A B**之间，子节点**P**介于**B C**之间，子节点**Q**大于**C**。  
<img src="https://github.com/weeklynote/weeklymd/blob/master/images/4_tree_struct.png?raw=true" alt="screenshot" title="screenshot" />
## 2-3-4树的插入  
- 1.如果当前节点是4-节点
  - 移除并保存节点中间的元素值，然后生成一个3-节点
  - 将该3-节点分裂成两个2-节点
  - 如果当前节点是根节点，被保存的中间值作为新的根节点
- 2.查找子节点中可以包含插入值的区间
- 3.找到的节点是一个叶子节点，则将被插入的值放入该节点；如果满足条件的节点是4-节点，那么执行步骤1，然后继续查找子节点。
下面举例说明将**29**插入到如下**2-3-4树**的过程，如图所示：
<img src="https://github.com/weeklynote/weeklymd/blob/master/images/demo_234.png?raw=true" alt="screenshot" title="screenshot" />
图一：初始**2-3-4树**的状态，此时从根节点(18 25)开始查找，直到找到子节点(26 28 30),因为29满足这个区间，但是此时子节点为4-节点，因此将28推到父节点，得到图二。
图二：将节点(26 30)分裂成两个2-节点，也就是(26)、(30)，得到图三。
图三：继续查找插入29的位置，找到节点(30)满足条件，将29插入，得到图四。
图四：最终插入效果。

## 节点的分裂  
**4-节点**为根节点的分裂方式如下：  
<img src="https://github.com/weeklynote/weeklymd/blob/master/images/no_root.png?raw=true" alt="screenshot" title="screenshot" />
**4-节点**的父节点是一个**2-节点**，则分裂方式如下：  
<img src="https://github.com/weeklynote/weeklymd/blob/master/images/p_two.png?raw=true" alt="screenshot" title="screenshot" />
**4-节点**的父节点是一个**3-节点**，则分裂方式如下：  
<img src="https://github.com/weeklynote/weeklymd/blob/master/images/p_three.png?raw=true" alt="screenshot" title="screenshot" />























