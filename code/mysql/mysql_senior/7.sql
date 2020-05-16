EXPLAIN SELECT ed.name '人物',c.name '掌门' 
FROM (
	SELECT e.name,d.ceo FROM t_emp e LEFT JOIN t_dept d ON e.deptid=d.id
) ed LEFT JOIN t_emp c ON ed.ceo= c.id;


EXPLAIN SELECT e.name '人物',tmp.name '掌门'
FROM t_emp e LEFT JOIN (
	SELECT d.id did,e.name FROM t_dept d LEFT JOIN t_emp e ON d.ceo=e.id
)tmp ON e.deptId=tmp.did;


EXPLAIN SELECT e1.name '人物',e2.name '掌门' 
FROM t_emp e1 
LEFT JOIN t_dept d ON e1.deptid = d.id
LEFT JOIN t_emp e2 ON d.ceo = e2.id ;
 
 
EXPLAIN SELECT e2.name '人物',
(
	SELECT e1.name FROM t_emp e1 WHERE e1.id= d.ceo
) '掌门'
FROM t_emp e2 
LEFT JOIN t_dept d ON e2.deptid=d.id;

