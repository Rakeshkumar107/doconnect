-- Run this command initially before starting the backend 
use do_connect;

-- To get list of all tables
show tables;

-- To retrieve data from tables
select * from users;
select * from questions;
select * from answers;
select * from logout_token;
select * from messages;

-- To drop existing tables
DROP Tables users, logout_token, questions, answers, messages;
