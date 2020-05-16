事务、视图
	
	#TCL
	/*
	Transaction Control Language 事务控制语言

	事务：一个或一组sql语句组成一个执行单元，这个执行单元要么全部执行，要么全部不执行。

	案例：转账
	张三丰  1000
	郭襄	1000

	update 表 set 张三丰的余额=500 where name='张三丰'
	意外
	update 表 set 郭襄的余额=1500 where name='郭襄'

	事务的ACID(acid)属性
		1. 原子性（Atomicity）
			原子性是指事务是一个不可分割的工作单位，事务中的操作要么都发生，要么都不发生。
	
		2. 一致性（Consistency）
			事务必须使数据库从一个一致性状态变换到另外一个一致性状态。
		
		3. 隔离性（Isolation）
			事务的隔离性是指一个事务的执行不能被其他事务干扰，
			即一个事务内部的操作及使用的数据对并发的其他事务是隔离的，
			并发执行的各个事务之间不能互相干扰。
		
		4. 持久性（Durability）
			持久性是指一个事务一旦被提交，它对数据库中数据的改变就是永久性的，
			接下来的其他操作和数据库故障不应该对其有任何影响	


	事务的创建
		隐式事务：事务没有明显的开启和结束的标记
		比如insert、update、delete语句

	delete from 表 where id =1;

	显式事务：事务具有明显的开启和结束的标记
		前提：必须先设置自动提交功能为禁用

	set autocommit=0;

	步骤1：开启事务
	set autocommit=0;
	start transaction;可选的
	步骤2：编写事务中的sql语句(select insert update delete)
	语句1;
	语句2;
	...

	步骤3：结束事务
	commit;提交事务
	rollback;回滚事务

	savepoint 节点名;设置保存点

	对于同时运行的多个事务, 当这些事务访问数据库中相同的数据时, 如果没有采取必要的隔离机制, 
	就会导致各种并发问题:

	脏读: 
		对于两个事务T1, T2, T1 读取了已经被T2 更新但还没有被提交的字段. 
		之后, 若T2 回滚, T1读取的内容就是临时且无效的.
	
	不可重复读: 
		对于两个事务T1, T2, T1 读取了一个字段, 然后T2 更新了该字段. 
		之后, T1再次读取同一个字段, 值就不同了.

	幻读: 对于两个事务T1, T2, T1 从一个表中读取了一个字段, 然后T2 在该表中插入了一些新的行. 
		之后, 如果T1 再次读取同一个表, 就会多出几行.
	

	read uncommitted(读未提交) ：
		允许事务读取未被其他事务提交的变更，脏读，不可重复读，幻读的问题都会出现；

	read committed(读已提交)：
		只允许事务读取已经被其他事务提交的变更，可以避免脏读，但是不可重复读和幻读的问题可以出现；

	repeatable read(可重复读)：
		确保事务可以多次从一个字段读取相同的值，在这个事务持续期间，禁止其他事务对这个字段的更新
		可以避免脏读和不可重复读，幻读的问题可以出现

	serializable(串行化)：
		确保事务可以从一个表中读取相同的行，在这个事务持续期间，禁止其他事物对该表的插入更新和删除，
		并发问题可以避免，但是效率比较低

	
	事务的隔离级别：
					 脏读  不可重复读	幻读
	read uncommitted：√		  √		     √
	read committed：  ×		  √			 √
	repeatable read： ×		  ×			 √
	serializable	  ×       ×     	 ×


	mysql中默认 第三个隔离级别 repeatable read 
	oracle中默认第二个隔离级别 read committed  # 1521
	
	查看隔离级别
	select @@tx_isolation;
	
	设置隔离级别
	set session|global transaction isolation level 隔离级别;


	开启事务的语句;
	update 表 set 张三丰的余额=500 where name='张三丰'
	update 表 set 郭襄的余额=1500 where name='郭襄' 
	结束事务的语句;
	*/

	SHOW VARIABLES LIKE 'autocommit';
	SHOW ENGINES;

	#1.演示事务的使用步骤
	#开启事务
		SET autocommit=0;
		START TRANSACTION;
	
	#编写一组事务的语句
		UPDATE account SET balance = 1000 WHERE username='张无忌';
		UPDATE account SET balance = 1000 WHERE username='赵敏';

	#结束事务
		ROLLBACK;
		#commit;
		
	SELECT * FROM account;
	

	#2.演示事务对于delete和truncate的处理的区别
	SET autocommit=0;
	START TRANSACTION;
	DELETE FROM account;
	ROLLBACK;	// 数据可以恢复
	
	SET autocommit=0;
	START TRANSACTION;
	truncate table account;
	ROLLBACK;  // 数据不能恢复



	#3.演示savepoint 的使用
	SET autocommit=0;
	START TRANSACTION;
	DELETE FROM account WHERE id=25;
	SAVEPOINT a;#设置保存点
	DELETE FROM account WHERE id=28;
	ROLLBACK TO a;#回滚到保存点


	SELECT * FROM account;







	#视图
	/*
	含义：虚拟表，和普通表一样使用
	mysql 5.1版本出现的新特性，是通过表动态生成的数据

	比如：舞蹈班和普通班级的对比
	
		    创建语法的关键字	是否实际占用物理空间		使用
	视图	create view			只是保存了sql逻辑			增删改查，只是一般不能增删改
	表		create table		保存了数据					增删改查
	*/

	#案例：查询姓张的学生名和专业名
	SELECT stuname,majorname
	FROM stuinfo s
	INNER JOIN major m ON s.`majorid`= m.`id`
	WHERE s.`stuname` LIKE '张%';

	CREATE VIEW v1 AS
	SELECT stuname,majorname
	FROM stuinfo s
	INNER JOIN major m ON s.`majorid`= m.`id`;

	SELECT * FROM v1 WHERE stuname LIKE '张%';


	#一、创建视图
	/*
	语法：
	create view 视图名
	as
	查询语句;
	*/
	USE myemployees;

	#1.查询姓名中包含a字符的员工名、部门名和工种信息
	#①创建
	CREATE VIEW myv1
	AS
	SELECT last_name,department_name,job_title
	FROM employees e
	JOIN departments d ON e.department_id = d.department_id
	JOIN jobs j ON j.job_id  = e.job_id;

	#②使用
	SELECT * FROM myv1 WHERE last_name LIKE '%a%';



	#2.查询各部门的平均工资级别
	#①创建视图查看每个部门的平均工资
	CREATE VIEW myv2
	AS
	SELECT AVG(salary) ag,department_id
	FROM employees
	GROUP BY department_id;

	#②使用
	SELECT myv2.`ag`,g.grade_level
	FROM myv2
	JOIN job_grades g
	ON myv2.`ag` BETWEEN g.`lowest_sal` AND g.`highest_sal`;



	#3.查询平均工资最低的部门信息
	SELECT * FROM myv2 ORDER BY ag LIMIT 1;

	
	#4.查询平均工资最低的部门名和工资
	CREATE VIEW myv3 AS SELECT * FROM myv2 ORDER BY ag LIMIT 1;

	SELECT d.*,m.ag
	FROM myv3 m
	JOIN departments d
	ON m.`department_id`=d.`department_id`;




	#二、视图的修改
	#方式一：
	/*
	create or replace view  视图名 as 查询语句;
	*/
	SELECT * FROM myv3 

	CREATE OR REPLACE VIEW myv3
	AS
	SELECT AVG(salary),job_id
	FROM employees
	GROUP BY job_id;

	
	#方式二：
	/*
	语法：
	alter view 视图名 as 查询语句;
	*/
	ALTER VIEW myv3	AS SELECT * FROM employees;

	
	
	#三、删除视图
	/*
	语法：drop view 视图名,视图名,...;
	*/
	DROP VIEW emp_v1,emp_v2,myv3;


	
	#四、查看视图
	DESC myv3;
	SHOW CREATE VIEW myv3;


	
	#五、视图的更新
	CREATE OR REPLACE VIEW myv1
	AS
	SELECT last_name,email,salary*12*(1+IFNULL(commission_pct,0)) "annual salary"
	FROM employees;

	CREATE OR REPLACE VIEW myv1
	AS
	SELECT last_name,email
	FROM employees;

	SELECT * FROM myv1;
	SELECT * FROM employees;
	
	
	#1.插入
	INSERT INTO myv1 VALUES('张飞','zf@qq.com');	// 原始表也有

	#2.修改
	UPDATE myv1 SET last_name = '张无忌' WHERE last_name='张飞';  // 原始表也更新了

	#3.删除
	DELETE FROM myv1 WHERE last_name = '张无忌';  // 原始表也删除了

	
	#具备以下特点的视图不允许更新
	#①包含以下关键字的sql语句：分组函数、distinct、group  by、having、union或者union all
		CREATE OR REPLACE VIEW myv1
		AS
		SELECT MAX(salary) m,department_id
		FROM employees
		GROUP BY department_id;
		SELECT * FROM myv1;
		#更新
		UPDATE myv1 SET m=9000 WHERE department_id=10; // 更新失败

		
	#②常量视图
		CREATE OR REPLACE VIEW myv2
		AS
		SELECT 'john' NAME;
		SELECT * FROM myv2;
		
		#更新
		UPDATE myv2 SET NAME='lucy';


	#③select中包含子查询
		CREATE OR REPLACE VIEW myv3
		AS
		SELECT department_id,(SELECT MAX(salary) FROM employees) 最高工资
		FROM departments;
		
		#更新
		SELECT * FROM myv3;
		UPDATE myv3 SET 最高工资=100000;


	#④join
		CREATE OR REPLACE VIEW myv4
		AS
		SELECT last_name,department_name
		FROM employees e
		JOIN departments d
		ON e.department_id = d.department_id;
		
		#更新
		SELECT * FROM myv4;
		UPDATE myv4 SET last_name  = '张飞' WHERE last_name='Whalen';
		INSERT INTO myv4 VALUES('陈真','xxxx');



	#⑤from一个不能更新的视图
		CREATE OR REPLACE VIEW myv5
		AS
		SELECT * FROM myv3;
		
		#更新
		SELECT * FROM myv5;
		UPDATE myv5 SET 最高工资=10000 WHERE department_id=60;



	#⑥where子句的子查询引用了from子句中的表
		CREATE OR REPLACE VIEW myv6
		AS
		SELECT last_name,email,salary
		FROM employees
		WHERE employee_id IN(
			SELECT manager_id
			FROM employees
			WHERE manager_id IS NOT NULL
		);	
		
		#更新
		SELECT * FROM myv6;
		UPDATE myv6 SET salary=10000 WHERE last_name = 'k_ing';






案例分析：
	#一、创建视图emp_v1,要求查询电话号码以‘011’开头的员工姓名和工资、邮箱
	CREATE OR REPLACE VIEW emp_v1
	AS
	SELECT last_name,salary,email
	FROM employees
	WHERE phone_number LIKE '011%';

	
	#二、创建视图emp_v2，要求查询部门的最高工资高于12000的部门信息
	CREATE OR REPLACE VIEW emp_v2
	AS
	SELECT MAX(salary) mx_dep,department_id
	FROM employees
	GROUP BY department_id
	HAVING MAX(salary)>12000;

	SELECT d.*,m.mx_dep
	FROM departments d
	JOIN emp_v2 m
	ON m.department_id = d.`department_id`;
















