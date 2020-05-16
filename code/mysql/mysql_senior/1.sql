
CREATE DATABASE mydb2;


DROP TABLE employee;
CREATE TABLE employee(id INT PRIMARY KEY,NAME VARCHAR(20),age INT);
#创建索引
CREATE INDEX index_name ON employee(NAME);
#创建唯一索引
CREATE UNIQUE INDEX idx_customer_no ON customer(customer_no);