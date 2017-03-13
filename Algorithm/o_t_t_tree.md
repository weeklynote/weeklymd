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
