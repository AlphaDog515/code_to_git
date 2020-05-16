CREATE INDEX idx_age_deptid_name ON emp (age,deptid,NAME);

/*
无过滤不索引
*/
EXPLAIN SELECT * FROM emp WHERE age=40 ORDER BY deptid;

#索引使用不了 ：无过滤不索引，因为oreder by前面没有where
EXPLAIN  SELECT * FROM emp ORDER BY age,deptid;
#可以使用索引因为底层优化时加上了where
EXPLAIN  SELECT * FROM emp ORDER BY age,deptid LIMIT 10;

/*
顺序错必排序:order by后面的字段和索引上的字段顺序不一致时必重排序
*/
EXPLAIN  SELECT * FROM emp WHERE age=45 ORDER BY NAME,deptid;


/*
方向反，必排序 : order by后面的字段如果有升序有降序必排序
*/
EXPLAIN SELECT * FROM emp WHERE age=45 ORDER BY  deptid DESC, NAME DESC ;
EXPLAIN SELECT * FROM emp WHERE age=45 ORDER BY  deptid ASC, NAME DESC ;
EXPLAIN SELECT * FROM emp WHERE age=45 ORDER BY  deptid DESC, NAME ASC ;

/*
4. 索引的选择
*/
EXPLAIN SELECT SQL_NO_CACHE * FROM emp 
		WHERE age =30 AND empno <101000 ORDER BY NAME ;
#清除表中的索引
CALL proc_drop_index('mydb','emp');
#创建索引
CREATE INDEX index_age_empno ON emp(age,empno);
CREATE INDEX index_age_name ON emp(age,NAME);

/*
group by 使用索引的原则几乎跟order by一致 ，唯一区别是groupby 
即使没有过滤条件用到索引，也可以直接使用索引。
*/

/*
尽量不要使用select * 应该在select后面跟上具体的字段
*/





