连接查询与子查询

	#进阶6：连接查询
	/*
	含义：又称多表查询，当查询的字段来自于多个表时，就会用到连接查询

	笛卡尔乘积现象：表1 有m行，表2有n行，结果=m*n行

	发生原因：没有有效的连接条件
	如何避免：添加有效的连接条件

	分类：
		按年代分类：
		sql92标准: 仅仅支持内连接
		sql99标准【推荐】：支持内连接+外连接（左外和右外）+交叉连接
		
		按功能分类：
			内连接：
				等值连接
				非等值连接
				自连接
			外连接：
				左外连接
				右外连接
				全外连接
			
			交叉连接
	*/

	SELECT * FROM beauty;

	SELECT * FROM boys;

	SELECT NAME,boyName FROM boys,beauty
	WHERE beauty.boyfriend_id= boys.id;

	
	#一、sql92标准
	#1、等值连接
	/*
	①多表等值连接的结果为多表的交集部分
	②n表连接，至少需要n-1个连接条件
	③多表的顺序没有要求
	④一般需要为表起别名
	⑤可以搭配前面介绍的所有子句使用，比如排序、分组、筛选
	*/
	#案例1：查询女神名和对应的男神名
	SELECT NAME,boyName 
	FROM boys,beauty
	WHERE beauty.boyfriend_id= boys.id;

	
	#案例2：查询员工名和对应的部门名
	SELECT last_name,department_name
	FROM employees,departments
	WHERE employees.`department_id`=departments.`department_id`;



	#2、为表起别名
	/*
	①提高语句的简洁度
	②区分多个重名的字段

	注意：如果为表起了别名，则查询的字段就不能使用原来的表名去限定
	*/
	#查询员工名、工种号、工种名
	SELECT e.last_name,e.job_id,j.job_title
	FROM employees  e,jobs j
	WHERE e.`job_id`=j.`job_id`;


	#3、两个表的顺序是否可以调换
	#查询员工名、工种号、工种名
	SELECT e.last_name,e.job_id,j.job_title
	FROM jobs j,employees e
	WHERE e.`job_id`=j.`job_id`;


	#4、可以加筛选
	#案例：查询有奖金的员工名、部门名
	SELECT last_name,department_name,commission_pct
	FROM employees e,departments d
	WHERE e.`department_id`=d.`department_id`
	AND e.`commission_pct` IS NOT NULL;

	
	#案例2：查询城市名中第二个字符为o的部门名和城市名
	SELECT department_name,city
	FROM departments d,locations l
	WHERE d.`location_id` = l.`location_id`
	AND city LIKE '_o%';

	
	#5、可以加分组
	#案例1：查询每个城市的部门个数
	SELECT COUNT(*) 个数,city
	FROM departments d,locations l
	WHERE d.`location_id`=l.`location_id`
	GROUP BY city;


	#案例2：查询有奖金的每个部门的部门名和部门的领导编号和该部门的最低工资
	SELECT department_name,d.`manager_id`,MIN(salary)
	FROM departments d,employees e
	WHERE d.`department_id`=e.`department_id`
	AND commission_pct IS NOT NULL
	GROUP BY department_name,d.`manager_id`;
	
	
	#6、可以加排序
	#案例：查询每个工种的工种名和员工的个数，并且按员工个数降序
	SELECT job_title,COUNT(*)
	FROM employees e,jobs j
	WHERE e.`job_id`=j.`job_id`
	GROUP BY job_title
	ORDER BY COUNT(*) DESC;



	#7、可以实现三表连接？
	#案例：查询员工名、部门名和所在的城市
	SELECT last_name,department_name,city
	FROM employees e,departments d,locations l
	WHERE e.`department_id`=d.`department_id`
	AND d.`location_id`=l.`location_id`
	AND city LIKE 's%'
	ORDER BY department_name DESC;



	#2、非等值连接
	#案例1：查询员工的工资和工资级别
	SELECT salary,grade_level
	FROM employees e,job_grades g
	WHERE salary BETWEEN g.`lowest_sal` AND g.`highest_sal`
	AND g.`grade_level`='A';

	/*
	select salary,employee_id from employees;
	select * from job_grades;
	CREATE TABLE job_grades
	(grade_level VARCHAR(3),
	 lowest_sal  int,
	 highest_sal int);

	INSERT INTO job_grades
	VALUES ('A', 1000, 2999);

	INSERT INTO job_grades
	VALUES ('B', 3000, 5999);

	INSERT INTO job_grades
	VALUES('C', 6000, 9999);

	INSERT INTO job_grades
	VALUES('D', 10000, 14999);

	INSERT INTO job_grades
	VALUES('E', 15000, 24999);

	INSERT INTO job_grades
	VALUES('F', 25000, 40000);
	*/


	#3、自连接
	#案例：查询 员工名和上级的名称
	SELECT e.employee_id,e.last_name,m.employee_id,m.last_name
	FROM employees e,employees m
	WHERE e.`manager_id`=m.`employee_id`;





	#二、sql99语法
	/*
	语法：
		select 查询列表
		from 表1 别名 【连接类型】
		join 表2 别名 
		on 连接条件
		【where 筛选条件】
		【group by 分组】
		【having 筛选条件】
		【order by 排序列表】		

	分类：
	内连接（★）：inner
	外连接
		左外(★): left  【outer】
		右外(★)：right 【outer】
		全外：   full  【outer】
	交叉连接：cross 
	*/


	#一）内连接
	/*
	语法：
	select 查询列表
	from 表1 别名
	inner join 表2 别名
	on 连接条件;

	分类：
	等值
	非等值
	自连接

	特点：
	①添加排序、分组、筛选
	②inner可以省略
	③筛选条件放在where后面，连接条件放在on后面，提高分离性，便于阅读
	④inner join连接和sql92语法中的等值连接效果是一样的，都是查询多表的交集
	*/
	#1、等值连接
	#案例1.查询员工名、部门名
	SELECT last_name,department_name
	FROM departments d
	JOIN  employees e
	ON e.`department_id` = d.`department_id`;


	#案例2.查询名字中包含e的员工名和工种名（添加筛选）
	SELECT last_name,job_title
	FROM employees e
	INNER JOIN jobs j
	ON e.`job_id`=  j.`job_id`
	WHERE e.`last_name` LIKE '%e%';



	#3. 查询部门个数>3的城市名和部门个数，（添加分组+筛选）
	#①查询每个城市的部门个数
	#②在①结果上筛选满足条件的
	SELECT city,COUNT(*) 部门个数
	FROM departments d
	INNER JOIN locations l
	ON d.`location_id`=l.`location_id`
	GROUP BY city
	HAVING COUNT(*)>3;


	#案例4.查询哪个部门的员工个数>3的部门名和员工个数，并按个数降序（添加排序）
	#①查询每个部门的员工个数
	SELECT COUNT(*),department_name
	FROM employees e
	INNER JOIN departments d
	ON e.`department_id`=d.`department_id`
	GROUP BY department_name

	#② 在①结果上筛选员工个数>3的记录，并排序
	SELECT COUNT(*) 个数,department_name
	FROM employees e
	INNER JOIN departments d
	ON e.`department_id`=d.`department_id`
	GROUP BY department_name
	HAVING COUNT(*)>3
	ORDER BY COUNT(*) DESC;

	

	#5.查询员工名、部门名、工种名，并按部门名降序（添加三表连接）
	SELECT last_name,department_name,job_title
	FROM employees e
	INNER JOIN departments d ON e.`department_id`=d.`department_id`
	INNER JOIN jobs j ON e.`job_id` = j.`job_id`
	ORDER BY department_name DESC;



	#二）非等值连接
	#查询员工的工资级别
	SELECT salary,grade_level
	FROM employees e
	JOIN job_grades g
	ON e.`salary` BETWEEN g.`lowest_sal` AND g.`highest_sal`;
	 
	 
	#查询工资级别的个数>20的个数，并且按工资级别降序
	SELECT COUNT(*),grade_level
	FROM employees e
	JOIN job_grades g
	ON e.`salary` BETWEEN g.`lowest_sal` AND g.`highest_sal`
	GROUP BY grade_level
	HAVING COUNT(*)>20
	ORDER BY grade_level DESC;
	 
	 
	 
	#三）自连接	 
	#查询员工的名字、上级的名字
	SELECT e.last_name,m.last_name
	FROM employees e
	JOIN employees m
	ON e.`manager_id`= m.`employee_id`;
	 
	 
	#查询姓名中包含字符k的员工的名字、上级的名字
	SELECT e.last_name,m.last_name
	FROM employees e
	JOIN employees m
	ON e.`manager_id`= m.`employee_id`
	WHERE e.`last_name` LIKE '%k%';
	 
	 
	 
	#二、外连接	 
	 /*
	 应用场景：用于查询一个表中有，另一个表没有的记录
	 
	 特点：
	 1、外连接的查询结果为主表中的所有记录
		如果从表中有和它匹配的，则显示匹配的值
		如果从表中没有和它匹配的，则显示null
		外连接查询结果=内连接结果+主表中有而从表没有的记录
	
	 2、左外连接，left join左边的是主表
		右外连接，right join右边的是主表
	
	 3、左外和右外交换两个表的顺序，可以实现同样的效果 
	
	 4、全外连接=内连接的结果+表1中有但表2没有的+表2中有但表1没有的
	*/
	 #引入：查询男朋友不在男神表的的女神名	 
	 SELECT * FROM beauty;
	 SELECT * FROM boys;
	 
	 #左外连接
	 SELECT b.*,bo.*
	 FROM boys bo
	 LEFT OUTER JOIN beauty b
	 ON b.`boyfriend_id` = bo.`id`
	 WHERE b.`id` IS NULL;
	 
	 
	 #案例1：查询哪个部门没有员工
	 #左外
	 SELECT d.*,e.employee_id
	 FROM departments d
	 LEFT OUTER JOIN employees e
	 ON d.`department_id` = e.`department_id`
	 WHERE e.`employee_id` IS NULL;
	 
	 
	 #右外	 
	 SELECT d.*,e.employee_id
	 FROM employees e
	 RIGHT OUTER JOIN departments d
	 ON d.`department_id` = e.`department_id`
	 WHERE e.`employee_id` IS NULL;
	 
	 
	 #全外	 
	 USE girls;
	 SELECT b.*,bo.*
	 FROM beauty b
	 FULL OUTER JOIN boys bo
	 ON b.`boyfriend_id` = bo.id;
	 

	 #交叉连接	 
	 SELECT b.*,bo.*
	 FROM beauty b
	 CROSS JOIN boys bo;
	 
	 
	 
	 #sql92和 sql99pk
	 /*
	 功能：	 sql99支持的较多
	 可读性：sql99实现连接条件和筛选条件的分离，可读性较高
	 */
	 
 
 
 
案例分析：
	#1.显示所有员工的姓名，部门号和部门名称。
	USE myemployees;
	SELECT last_name,d.department_id,department_name
	FROM employees e,departments d
	WHERE e.`department_id` = d.`department_id`;


	#2.查询90号部门员工的job_id和90号部门的location_id
	SELECT job_id,location_id
	FROM employees e,departments d
	WHERE e.`department_id`=d.`department_id`
	AND e.`department_id`=90;



	#3.	选择所有有奖金的员工的 last_name, department_name, location_id, city
	SELECT last_name, department_name, l.location_id, city
	FROM employees e,departments d,locations l
	WHERE e.department_id = d.department_id
	AND d.location_id=l.location_id
	AND e.commission_pct IS NOT NULL;
		# 员工是主体，连接了部门，员工的部门信息就有了，
		# 连接了位置就有了城市名称，然后筛选出奖金不为空的
		



	#4.选择city在Toronto工作的员工的last_name , job_id , department_id , department_name 
	SELECT last_name, job_id, d.department_id, department_name 
	FROM employees e,departments d ,locations l
	WHERE e.department_id = d.department_id
	AND d.location_id=l.location_id
	AND city = 'Toronto';



	#5.查询每个工种、每个部门的部门名、工种名和最低工资
	SELECT department_name,job_title,MIN(salary) 最低工资
	FROM employees e,departments d,jobs j
	WHERE e.`department_id`=d.`department_id`
	AND e.`job_id`=j.`job_id`
	GROUP BY department_name,job_title;



	#6.查询每个国家下的部门个数大于2的国家编号
	SELECT country_id,COUNT(*) 部门个数
	FROM departments d,locations l
	WHERE d.`location_id`=l.`location_id`
	GROUP BY country_id
	HAVING 部门个数>2;

	location_id1 country_id1 department_id1
	location_id1 country_id1 department_id2
	location_id1 country_id1 department_id3
	location_id1 country_id4 department_id1
	location_id2 country_id5 department_id1



	#7、选择指定员工的姓名，员工号，以及他的管理者的姓名和员工号，结果类似于下面的格式
	employees	Emp#	manager		Mgr#
	kochhar		101		king		100
	SELECT e.last_name employees,e.employee_id "Emp#",m.last_name manager,m.employee_id "Mgr#"
	FROM employees e,employees m
	WHERE e.manager_id = m.employee_id
	AND e.last_name='kochhar';



案例分析：
	#一、查询编号>3的女神的男朋友信息，如果有则列出详细，如果没有，用null填充
	SELECT b.id,b.name,bo.*
	FROM beauty b
	LEFT OUTER JOIN boys bo
	ON b.`boyfriend_id` = bo.`id`
	WHERE b.`id`>3;
	# 女神是主体按照其boyfriend_id 在boys表都匹配到，然后筛选


	#二、查询哪个城市没有部门
	SELECT city
	FROM departments d
	RIGHT OUTER JOIN locations l 
	ON d.`location_id`=l.`location_id`
	WHERE  d.`department_id` IS NULL;

	
	#三、查询部门名为SAL或IT的员工信息
	SELECT e.*,d.department_name,d.`department_id`
	FROM departments  d
	LEFT JOIN employees e
	ON d.`department_id` = e.`department_id`
	WHERE d.`department_name` IN('SAL','IT');

	SELECT * FROM departments WHERE `department_name` IN('SAL','IT');





















 
 
 
 
 
 
 




































