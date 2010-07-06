increase_counter(X) :- (counter(Y); Y = 0), X is Y + 1, retractall(counter(_)), asserta(counter(X)).
