CREATE SCHEMA androiddb 
DEFAULT CHARACTER SET utf8 
COLLATE utf8_bin ;

USE androiddb;

CREATE TABLE employee(
id int(11) not null auto_increment primary key,
name varchar(100),
designation varchar(100),
salary varchar(100)
);