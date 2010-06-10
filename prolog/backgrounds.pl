% 买点 - 性能配置
% 卖点 - 用途

child_of(joe, ralf).
child_of(mary, joe).
child_of(steve, joe).

descendent_of(X, Y) :- child_of(X, Y).
descendent_of(X, Y) :- child_of(Z, Y), descendent_of(X, Z).
