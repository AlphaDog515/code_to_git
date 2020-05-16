

/*
子查询的优化 ：1.加索引  2.尽量不要使用子查询。 
	3.可以通过使用 left join xxx where xxx的方式来替换 not in
*/

#取所有不为掌门人的员工，按年龄分组！
EXPLAIN SELECT age AS '年龄', COUNT(*) AS '人数' FROM t_emp WHERE id  NOT IN 
(
	SELECT ceo FROM t_dept WHERE ceo IS NOT NULL
) GROUP BY age;

CALL proc_drop_index('mydb','t_dept');
CALL proc_drop_index('mydb','t_emp');
CREATE INDEX index_ceo ON t_dept(ceo);
CREATE INDEX index_age ON t_emp(age);

 EXPLAIN SELECT age AS '年龄',COUNT(*) AS '人数' FROM t_emp e 
 LEFT JOIN t_dept d ON e.id=d.ceo 
 WHERE d.id IS NULL 
 GROUP BY age;