% 产品
% 	Apple / ThinkPad / HP / 宏基 / 神舟
% VOC
% 	价格
% 	品牌
% 	性能配置
% 	存储容量
% 	续航时间
% 	质量
% 	售后服务
% 	屏幕大小
% 	颜色
% 	赠送的配件
%
% 	体力
% 	关系程度
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
