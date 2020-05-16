 EXPLAIN SELECT SQL_NO_CACHE * FROM emp WHERE emp.age=30  
 
 EXPLAIN SELECT SQL_NO_CACHE * FROM emp WHERE emp.age=30 AND deptid=4
 
 EXPLAIN SELECT SQL_NO_CACHE * FROM emp WHERE emp.age=30 AND deptid=4 AND emp.name = 'abcd'  

#删除索引
CALL proc_drop_index('mydb','emp');#第一个是库名，第二个是表名
/*
system > const > eq_ref > ref > fulltext > ref_or_null > 
	index_merge > unique_subquery > index_subquery > range > index > ALL
*/
#创建索引
 CREATE INDEX index_age ON emp(age);
 CREATE INDEX index_age_deptid ON emp(age,deptid);
 CREATE INDEX index_age_deptid_name ON emp(age,deptid,NAME);
 
 
 
 CREATE INDEX index_age_deptid_name ON emp(NAME,age,deptid);
 EXPLAIN SELECT SQL_NO_CACHE * FROM emp WHERE emp.age=30 AND deptid=4
 
 
 
 
 
 