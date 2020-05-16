分组，排序，函数
	
	#进阶3：排序查询
	/*
	语法：
	select 查询列表
	from 表名
	【where  筛选条件】
	order by 排序的字段或表达式;

	特点：
	1、asc代表的是升序，可以省略
	desc代表的是降序

	2、order by子句可以支持 单个字段、别名、表达式、函数、多个字段

	3、order by子句在查询语句的最后面，除了limit子句
	*/

	#1、按单个字段排序
	SELECT * FROM employees ORDER BY salary DESC;

	
	#2、添加筛选条件再排序
	#案例：查询部门编号>=90的员工信息，并按员工编号降序
	SELECT *
	FROM employees
	WHERE department_id>=90
	ORDER BY employee_id DESC;


	#3、按表达式排序
	#案例：查询员工信息 按年薪降序
	SELECT *,salary*12*(1+IFNULL(commission_pct,0))
	FROM employees
	ORDER BY salary*12*(1+IFNULL(commission_pct,0)) DESC;


	#4、按别名排序
	#案例：查询员工信息 按年薪升序
	SELECT *,salary*12*(1+IFNULL(commission_pct,0)) 年薪
	FROM employees
	ORDER BY 年薪 ASC;

	
	#5、按函数排序
	#案例：查询员工名，并且按名字的长度降序
	SELECT LENGTH(last_name),last_name 
	FROM employees
	ORDER BY LENGTH(last_name) DESC;

	
	#6、按多个字段排序
	#案例：查询员工信息，要求先按工资降序，再按employee_id升序
	SELECT * FROM employees
	ORDER BY salary DESC,employee_id ASC;

	
	案例分析：
	#1.查询员工的姓名和部门号和年薪，按年薪降序，按姓名升序
	SELECT last_name,department_id,salary*12*(1+IFNULL(commission_pct,0)) 年薪
	FROM employees ORDER BY 年薪 DESC,last_name ASC;


	#2.选择工资不在8000到17000的员工的姓名和工资，按工资降序
	SELECT last_name,salary FROM employees
	WHERE salary NOT BETWEEN 8000 AND 17000
	ORDER BY salary DESC;

	
	#3.查询邮箱中包含e的员工信息，并先按邮箱的字节数降序，再按部门号升序
	SELECT *,LENGTH(email) FROM employees WHERE email LIKE '%e%'
	ORDER BY LENGTH(email) DESC,department_id ASC;








	#进阶4：常见函数
	/*
	概念：类似于java的方法，将一组逻辑语句封装在方法体中，对外暴露方法名
	好处：1、隐藏了实现细节  2、提高代码的重用性
	调用：select 函数名(实参列表) 【from 表】;
	特点：
		①叫什么（函数名）
		②干什么（函数功能）

	分类：
		1、单行函数
			如 concat、length、ifnull等
		2、分组函数		
			功能：做统计使用，又称为统计函数、聚合函数、组函数
		
	常见函数：
		一、单行函数
		字符函数：
			length:获取字节个数(utf-8一个汉字代表3个字节,gbk为2个字节)
			concat
			substr
			INSTR(STR,SUBSTR)：	在字符串STR里面,字符串SUBSTR出现的第一个位置(INDEX)，
								INDEX是从1开始计算，如果没有找到就直接返回0，没有返回负数的情况。
			trim
			upper
			lower
			lpad
			rpad
			replace
		
		数学函数：
			round
			ceil
			floor
			truncate
			mod
		
		日期函数：
			now
			curdate
			curtime
			year
			month
			monthname
			day
			hour
			minute
			second
			str_to_date
			date_format
		
		其他函数：
			version
			database
			user
		
		控制函数
			if
			case
	*/


	#一、字符函数
	#1.length 获取参数值的字节个数
	SELECT LENGTH('john');
	SELECT LENGTH('张三丰hahaha'); #15

	SHOW VARIABLES LIKE '%char%'

	
	#2.concat 拼接字符串
	SELECT CONCAT(last_name,'_',first_name) 姓名 FROM employees;

	
	#3.upper、lower
	SELECT UPPER('john');
	SELECT LOWER('joHn');
	#示例：将姓变大写，名变小写，然后拼接
	SELECT CONCAT(UPPER(last_name),LOWER(first_name))  姓名 FROM employees;

	
	#4.substr、substring
	注意：索引从1开始
	#截取从指定索引处后面所有字符
	SELECT SUBSTR('李莫愁爱上了陆展元',7)  out_put;  	//陆展元

	#截取从指定索引处指定字符长度的字符
	SELECT SUBSTR('李莫愁爱上了陆展元',1,3) out_put;	//李莫愁
	
	#案例：姓名中首字符大写，其他字符小写然后用_拼接，显示出来
	SELECT CONCAT(UPPER(SUBSTR(last_name,1,1)),'_',LOWER(SUBSTR(last_name,2)))  out_put
	FROM employees;

	
	#5.instr 返回子串第一次出现的索引，如果找不到返回0
	SELECT INSTR('杨不殷六侠悔爱上了殷六侠','殷八侠') AS out_put;

	
	#6.trim
	SELECT LENGTH(TRIM('    张翠山    ')) AS out_put;
	SELECT TRIM('aa' FROM 'aaaaa张aaa翠山aaaa')  AS out_put; 	# a张aaa翠山

	
	#7.lpad 用指定的字符实现左填充指定长度
	SELECT LPAD('殷素ab',1,'*') AS out_put; # 殷
	SELECT LPAD('殷素ab',2,'*') AS out_put; # 殷素
	SELECT LPAD('殷素ab',3,'*') AS out_put; # 殷素a
	SELECT LPAD('殷素ab',4,'*') AS out_put; # 殷素ab
	SELECT LPAD('殷素ab',5,'*') AS out_put; # *殷素ab

	
	#8.rpad 用指定的字符实现右填充指定长度
	SELECT RPAD('殷素',1,'ab') AS out_put;  #殷
	SELECT RPAD('殷素',2,'ab') AS out_put; 	#殷素
	SELECT RPAD('殷素',3,'ab') AS out_put; 	#殷素a
	SELECT RPAD('殷素',4,'ab') AS out_put; 	#殷素ab
	SELECT RPAD('殷素',6,'ab') AS out_put; 	#殷素abab


	#9.replace 替换
	SELECT REPLACE('周芷若周芷若周芷若周芷若张无忌爱上了周芷若','周芷若','赵敏') AS out_put;



	#二、数学函数
	#round 四舍五入
	SELECT ROUND(-1.5);  	# -2
	SELECT ROUND(1.567,2); 	# 1.57
	SELECT ROUND(1.4747,2); # 1.47
	SELECT ROUND(1.47);  	#1

	#ceil 向上取整,返回>=该参数的最小整数
	SELECT CEIL(-1.02);

	#floor 向下取整，返回<=该参数的最大整数
	SELECT FLOOR(-9.99);

	#truncate 截断
	SELECT TRUNCATE(1.69999,1); 	# 保留一位小数截断1.6

	#mod取余
	/*
	mod(a,b) ：  a-a/b*b
	mod(-10,-3):-10- (-10)/(-3)*（-3）= -1
	*/
	SELECT MOD(10,-3);	// 1
	SELECT 10%3;		// 1


	#三、日期函数
	#now 返回当前系统日期+时间
	SELECT NOW(); # 2019-11-25 09:02:46

	#curdate 返回当前系统日期，不包含时间
	SELECT CURDATE(); # 2019-11-25

	#curtime 返回当前时间，不包含日期
	SELECT CURTIME();  # 09:03:23

	#可以获取指定的部分，年、月、日、小时、分钟、秒
	SELECT YEAR(NOW()) 年;
	SELECT YEAR('1998-1-1') 年;

	SELECT  YEAR(hiredate) 年 FROM employees;

	SELECT MONTH(NOW()) 月; 		# 11
	SELECT MONTHNAME(NOW()) 月; 	# November


	#str_to_date 将字符通过指定的格式转换成日期
	SELECT STR_TO_DATE('1998-3-2','%Y-%c-%d') AS out_put;  // 1998-03-02
	SELECT STR_TO_DATE('1998-3-2','%Y-%m-%d') AS out_put;  // 1998-03-02

	
	#查询入职日期为1992-4-3的员工信息
	SELECT * FROM employees WHERE hiredate = '1992-4-3';
	SELECT * FROM employees WHERE hiredate = STR_TO_DATE('4-3 1992','%c-%d %Y');


	#date_format 将日期转换成字符
	SELECT DATE_FORMAT(NOW(),'%y年%m月%d日') AS out_put;   # 19年11月25日
	SELECT DATE_FORMAT(NOW(),'%Y年%m月%d日') AS out_put;   # 2019年11月25日

	#查询有奖金的员工名和入职日期(xx月/xx日 xx年)
	SELECT last_name,DATE_FORMAT(hiredate,'%m月/%d日 %y年') 入职日期
	FROM employees WHERE commission_pct IS NOT NULL;


	
	#四、其他函数
	SELECT VERSION();
	SELECT DATABASE();
	SELECT USER();


	#五、流程控制函数
	#1.if函数： if else 的效果
	SELECT IF(10<5,'大','小');

	SELECT last_name,commission_pct,IF(commission_pct IS NULL,'没奖金，呵呵','有奖金，嘻嘻') 备注
	FROM employees;




	#2.case函数的使用一： switch case 的效果
	/*
	java中
	switch(变量或表达式){
		case 常量1：语句1;break;
		...
		default:语句n;break;
	}

	mysql中
	case 要判断的字段或表达式
	when 常量1 then 要显示的值1或语句1;
	when 常量2 then 要显示的值2或语句2;
	...
	else 要显示的值n或语句n;
	end
	*/

	/*案例：查询员工的工资，要求
	部门号=30，显示的工资为1.1倍
	部门号=40，显示的工资为1.2倍
	部门号=50，显示的工资为1.3倍
	其他部门，显示的工资为原工资
	*/
	SELECT salary 原始工资,department_id,
	CASE department_id
	WHEN 30 THEN salary*1.1   # salary=100 这样写会显示为0 这样写可以：100
	WHEN 40 THEN salary*1.2
	WHEN 50 THEN salary*1.3
	ELSE salary
	END AS 新工资
	FROM employees;

	#3.case 函数的使用二：类似于多重if
	/*
	java中：
	if(条件1){
		语句1；
	}else if(条件2){
		语句2；
	}
	...
	else{
		语句n;
	}

	mysql中：
	case 
	when 条件1 then 要显示的值1或语句1
	when 条件2 then 要显示的值2或语句2
	...
	else 要显示的值n或语句n
	end
	*/
	#案例：查询员工的工资的情况
	如果工资>20000,显示A级别
	如果工资>15000,显示B级别
	如果工资>10000，显示C级别
	否则，显示D级别

	SELECT salary,
	CASE 
	WHEN salary>20000 THEN 'A'
	WHEN salary>15000 THEN 'B'
	WHEN salary>10000 THEN 'C'
	ELSE 'D'
	END AS 工资级别
	FROM employees;


案例分析：
	#1.显示系统时间(注：日期+时间)
	SELECT NOW();

	
	#2.查询员工号，姓名，工资，以及工资提高百分之20%后的结果（new salary）
	SELECT employee_id,last_name,salary,salary*1.2 "new salary"
	FROM employees;
	
	
	#3.将员工的姓名按首字母排序，并写出姓名的长度（length）
	SELECT LENGTH(last_name) 长度,SUBSTR(last_name,1,1) 首字符,last_name
	FROM employees	ORDER BY 首字符;


	#4.	做一个查询，产生下面的结果
	<last_name> earns <salary> monthly but wants <salary*3>
	Dream Salary King earns 24000 monthly but wants 72000
	
	SELECT CONCAT(last_name,' earns ',salary,' monthly but wants ',salary*3) AS "Dream Salary"
	FROM employees WHERE salary=24000;


	#5.	使用case-when，按照下面的条件：
	job                   grade
	AD_PRES            		A
	ST_MAN             		B
	IT_PROG            		C
	SA_REP              	D
	ST_CLERK           		E
	产生下面的结果
	Last_name	Job_id	 Grade
	king		AD_PRES	  A

	SELECT last_name,job_id AS  job,
	CASE job_id
	WHEN 'AD_PRES' 	THEN 'A' 
	WHEN 'ST_MAN' 	THEN 'B' 
	WHEN 'IT_PROG' 	THEN 'C' 
	WHEN 'SA_PRE' 	THEN 'D'
	WHEN 'ST_CLERK' THEN 'E'
	END AS Grade
	FROM employees WHERE job_id = 'AD_PRES';







	#进阶5：分组查询
	/*
	语法：
	select 查询列表
	from 表
	【where 筛选条件】
	group by 分组的字段
	【order by 排序的字段】;

	特点：
	1、和分组函数一同查询的字段必须是group by后出现的字段
	2、筛选分为两类：分组前筛选和分组后筛选
			   针对的表			  	   位置		连接的关键字
	分组前筛选	原始表				 group by前	    where	
	分组后筛选	group by后的结果集   group by后	    having

	问题：where——group by——having

	一般来讲，能用分组前筛选的，尽量使用分组前筛选，提高效率

	3、分组可以按单个字段也可以按多个字段
	4、可以搭配着排序使用

	# 分组函数不要与普通列连用，返回第一个列，可以与group by的字段一起连用
	# where是过滤分组前的原表数据
	# having是过滤分组之后的组
	*/
	#引入：查询每个部门的员工个数
	SELECT COUNT(*) FROM employees WHERE department_id=90;
	
	#1.简单的分组
	#案例1：查询每个工种的员工平均工资
	SELECT AVG(salary),job_id
	FROM employees
	GROUP BY job_id;

	
	#案例2：查询每个位置的部门个数
	SELECT COUNT(*),location_id
	FROM departments
	GROUP BY location_id;


	#2、可以实现分组前的筛选
	#案例1：查询邮箱中包含a字符的 每个部门的最高工资
	SELECT MAX(salary),department_id
	FROM employees
	WHERE email LIKE '%a%'
	GROUP BY department_id;


	#案例2：查询有奖金的每个领导手下员工的平均工资
	SELECT AVG(salary),manager_id
	FROM employees
	WHERE commission_pct IS NOT NULL
	GROUP BY manager_id;


	#3、分组后筛选
	#案例：查询哪个部门的员工个数>5
	#①查询每个部门的员工个数
	SELECT COUNT(*),department_id
	FROM employees
	GROUP BY department_id;

	#② 筛选刚才①结果
	SELECT COUNT(*),department_id
	FROM employees
	GROUP BY department_id
	HAVING COUNT(*)>5;


	#案例2：每个工种有奖金的员工的最高工资>12000的工种编号和最高工资
	SELECT job_id,MAX(salary)
	FROM employees
	WHERE commission_pct IS NOT NULL
	GROUP BY job_id
	HAVING MAX(salary)>12000;


	#案例3：每个领导手下的最低工资大于5000的领导编号和最低工资
	SELECT manager_id,MIN(salary)
	FROM employees
	GROUP BY manager_id
	HAVING MIN(salary)>5000;


	#4.添加排序
	#案例：每个工种有奖金的员工的最高工资>6000的工种编号和最高工资,按最高工资升序
	SELECT job_id,MAX(salary) m
	FROM employees
	WHERE commission_pct IS NOT NULL
	GROUP BY job_id
	HAVING m>6000
	ORDER BY m;


	#5.按多个字段分组
	#案例：查询每个工种每个部门的最低工资,并按最低工资降序
	SELECT MIN(salary),job_id,department_id
	FROM employees
	GROUP BY department_id,job_id
	ORDER BY MIN(salary) DESC;
	


	#二、分组函数
	/*
	功能：用作统计使用，又称为聚合函数或统计函数或组函数

	分类：
		sum 求和、avg 平均值、max 最大值 、min 最小值 、count 计算个数

	特点：
	1、sum、avg一般用于处理数值型
	   max、min、count可以处理任何类型
	
	2、以上分组函数都忽略null值

	3、可以和distinct搭配实现去重的运算

	4、count函数的单独介绍，一般使用count(*)用作统计行数

	5、和分组函数一同查询的字段要求是group by后的字段
	*/

	#1、简单 的使用
	SELECT SUM(salary) FROM employees;
	SELECT AVG(salary) FROM employees;
	SELECT MIN(salary) FROM employees;
	SELECT MAX(salary) FROM employees;
	SELECT COUNT(salary) FROM employees;


	SELECT SUM(salary) 和,AVG(salary) 平均,
	MAX(salary) 最高,MIN(salary) 最低,COUNT(salary) 个数
	FROM employees;

	SELECT SUM(salary) 和,ROUND(AVG(salary),2) 平均,
	MAX(salary) 最高,MIN(salary) 最低,COUNT(salary) 个数
	FROM employees;

	
	#2、参数支持哪些类型
	SELECT SUM(last_name) ,AVG(last_name) FROM employees;
	SELECT SUM(hiredate) ,AVG(hiredate) FROM employees;

	SELECT MAX(last_name),MIN(last_name) FROM employees;

	SELECT MAX(hiredate),MIN(hiredate) FROM employees;

	SELECT COUNT(commission_pct) FROM employees;
	SELECT COUNT(last_name) FROM employees;

	
	#3、是否忽略null
	SELECT SUM(commission_pct) ,AVG(commission_pct),
	SUM(commission_pct)/35,SUM(commission_pct)/107 FROM employees;

	SELECT MAX(commission_pct) ,MIN(commission_pct) FROM employees;

	SELECT COUNT(commission_pct) FROM employees;
	
	SELECT commission_pct FROM employees;


	#4、和distinct搭配
	SELECT SUM(DISTINCT salary),SUM(salary) FROM employees;

	SELECT COUNT(DISTINCT salary),COUNT(salary) FROM employees;



	#5、count函数的详细介绍
	SELECT COUNT(salary) FROM employees;

	SELECT COUNT(*) FROM employees;

	SELECT COUNT(1) FROM employees;

	效率：
	MYISAM存储引擎下，COUNT(*)的效率高
	INNODB存储引擎下，COUNT(*)和COUNT(1)的效率差不多，比COUNT(字段)要高一些


	#6、和分组函数一同查询的字段有限制
	SELECT AVG(salary),employee_id  FROM employees;



	案例分析：
	#1.查询公司员工工资的最大值，最小值，平均值，总和
	SELECT MAX(salary) 最大值,MIN(salary) 最小值,AVG(salary) 平均值,SUM(salary) 和
	FROM employees;
	
	#2.查询员工表中的最大入职时间和最小入职时间的相差天数 （DIFFRENCE）
	SELECT MAX(hiredate) 最大,MIN(hiredate) 最小,
	(MAX(hiredate)-MIN(hiredate))/1000/3600/24 DIFFRENCE
	FROM employees;

	SELECT DATEDIFF(MAX(hiredate),MIN(hiredate)) DIFFRENCE
	FROM employees;

	SELECT DATEDIFF('1995-2-7','1995-2-6');


	#3.查询部门编号为90的员工个数
	SELECT COUNT(*) FROM employees WHERE department_id = 90;



案例分析：
	#1.查询各job_id的员工工资的最大值，最小值，平均值，总和，并按job_id升序
	SELECT MAX(salary),MIN(salary),AVG(salary),SUM(salary),job_id
	FROM employees
	GROUP BY job_id
	ORDER BY job_id;


	#2.查询员工最高工资和最低工资的差距（DIFFERENCE）
	SELECT MAX(salary)-MIN(salary) DIFFRENCE
	FROM employees;
	
	
	#3.查询各个管理者手下员工的最低工资，其中最低工资不能低于6000，没有管理者的员工不计算在内
	SELECT MIN(salary),manager_id
	FROM employees
	WHERE manager_id IS NOT NULL
	GROUP BY manager_id
	HAVING MIN(salary)>=6000;


	#4.查询所有部门的编号，员工数量和工资平均值,并按平均工资降序
	SELECT department_id,COUNT(*),AVG(salary) a
	FROM employees
	GROUP BY department_id
	ORDER BY a DESC;
	
	#5.选择具有各个job_id的员工人数
	SELECT COUNT(*) 个数,job_id
	FROM employees
	GROUP BY job_id;




































