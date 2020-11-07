五子棋算法
===
## 简介
本项目实现了连珠、逻辑AI算两个五子棋游戏相关的主要算法，能判断胜负、自动下棋。项目是经过优化的后的版本，初期版本已应用于生产项目中。
在测试过程中，虚拟机预热后逻辑AI算法花费时间稳定在 10 毫秒左右。经多个朋友验证，胜率在70%左右，适合作为初级难度的挑战。
逻辑AI算法参考了网上的思路，通过计算每个空棋位在棋盘四个方向上产生的棋型来评定分数，选举分数最高的棋位作为计算结果。
棋型参考：<http://game.onegreen.net/wzq/HTML/142336.html>
## 环境及依赖
Jdk 1.8
## 运行步骤
执行 TestStarter 类中的 main() 方法即可在控制台看到效果
## 目录结构描述
``` lua
gobang
├── algorithm
│   ├── ai
│   │   ├── AiAlgorithms.java            逻辑AI算法
│   │   ├── GamingInfo.java              游戏过程信息
│   │   ├── PatternChecker.java          棋型算法
│   │   ├── PatternEnum.java             棋型以及评分
│   │   └── PieceType.java               棋位棋子类型
│   ├── CommonAlgorithms.java            棋盘相关公共算法
│   └── RenjuChecker.java                连珠算法
├── bean
│   ├── ColorEnum.java                   棋位棋子颜色
│   ├── DirectionEnum.java               二维棋盘四个方向
│   └── Position.java                    棋位坐标，有内部类
└── TestStarter.java                     自动下棋测试类
```