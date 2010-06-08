% 产品
% 	Apple / ThinkPad / HP / 宏基 / 神舟
% VOC
% 	价格 (price)
% 	品牌 (brand)
% 	内存 (memory)
% 	硬盘 (disk)
% 	电池 (battery)
% 	质量 (quality)
% 	售后 (service)
% 	屏幕 (screen)
% 	颜色 (color)
% 	配件 (fitting)
%
% 	体力 (stamina)
% 	心情 (mood)
%
% 买点
% 	性能配置
% 卖点
% 	用途

child_of(joe, ralf).
child_of(mary, joe).
child_of(steve, joe).

descendent_of(X, Y) :- child_of(X, Y).
descendent_of(X, Y) :- child_of(Z, Y), descendent_of(X, Z).
