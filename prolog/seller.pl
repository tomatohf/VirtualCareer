name(seller, 小, 堂).
gender(seller, 1).

state_transition(_, greet, Args) :- Args = [abc, 123].

state_action(seller_role, wait_continue, Args) :- Args = [abc, 123].
state_action(greet, greet, Args) :- Y = 1.

action_perform(seller, greet, Args) :- X = Args.
