联合查询
	
	#进阶9：联合查询
	/*
	union 联合，合并：将多条查询语句的结果合并成一个结果

	语法：
	查询语句1
	union
	查询语句2
	union
	...

	应用场景：
		要查询的结果来自于多个表，且多个表没有直接的连接关系，但查询的信息一致时

	特点：★
	1、要求多条查询语句的查询列数是一致的！
	2、要求多条查询语句的查询的每一列的类型和顺序最好一致
	3、union关键字默认去重，如果使用union all可以包含重复项
	*/


	#引入的案例：查询部门编号>90或邮箱包含a的员工信息
	SELECT * FROM employees WHERE email LIKE '%a%' OR department_id>90;

	SELECT * FROM employees  WHERE email LIKE '%a%'
	UNION
	SELECT * FROM employees  WHERE department_id>90;


	#案例：查询中国用户中男性的信息以及外国用户中年男性的用户信息
	SELECT id,cname FROM t_ca WHERE csex='男'
	UNION ALL
	SELECT t_id,tname FROM t_ua WHERE tGender='male';

	
	

案例分析：
	#一、查询每个专业的学生人数
	SELECT majorid,COUNT(*)
	FROM student
	GROUP BY majorid;

	
	#二、查询参加考试的学生中，每个学生的平均分、最高分
	SELECT AVG(score),MAX(score),studentno
	FROM result
	GROUP BY studentno;

	
	#三、查询姓张的每个学生的最低分大于60的学号、姓名
	SELECT s.studentno,s.`studentname`,MIN(score)
	FROM student s
	JOIN result r
	ON s.`studentno`=r.`studentno`
	WHERE s.`studentname` LIKE '张%'
	GROUP BY s.`studentno`
	HAVING MIN(score)>60;
	
	
	#四、查询每个专业生日在“1988-1-1”后的学生姓名、专业名称
	SELECT m.`majorname`,s.`studentname`
	FROM student s
	JOIN major m
	ON m.`majorid`=s.`majorid`
	WHERE DATEDIFF(borndate,'1988-1-1')>0
	GROUP BY m.`majorid`;


	#五、查询每个专业的男生人数和女生人数分别是多少
	SELECT COUNT(*),sex,majorid
	FROM student
	GROUP BY sex,majorid;


	#六、查询专业和张翠山一样的学生的最低分
	#①查询张翠山的专业编号
	SELECT majorid
	FROM student
	WHERE studentname = '张翠山'

	#②查询编号=①的所有学生编号
	SELECT studentno
	FROM student
	WHERE majorid=(
		SELECT majorid
		FROM student
		WHERE studentname = '张翠山'
	)
	
	#②查询最低分
	SELECT MIN(score)
	FROM result
	WHERE studentno IN(
		SELECT studentno
		FROM student
		WHERE majorid=(
			SELECT majorid
			FROM student
			WHERE studentname = '张翠山'
		)
	)

	
	
	#七、查询大于60分的学生的姓名、密码、专业名
	SELECT studentname,loginpwd,majorname
	FROM student s
	JOIN major m ON s.majorid=  m.majorid
	JOIN result r ON s.studentno=r.studentno
	WHERE r.score>60;
	
	
	#八、按邮箱位数分组，查询每组的学生个数
	SELECT COUNT(*),LENGTH(email)
	FROM student
	GROUP BY LENGTH(email);
	
	
	#九、查询学生名、专业名、分数
	SELECT studentname,score,majorname
	FROM student s
	JOIN major m ON s.majorid=  m.majorid
	LEFT JOIN result r ON s.studentno=r.studentno


	#十、查询哪个专业没有学生，分别用左连接和右连接实现
	#左
	SELECT m.`majorid`,m.`majorname`,s.`studentno`
	FROM major m
	LEFT JOIN student s ON m.`majorid` = s.`majorid`
	WHERE s.`studentno` IS NULL;

	#右
	SELECT m.`majorid`,m.`majorname`,s.`studentno`
	FROM student s
	RIGHT JOIN  major m ON m.`majorid` = s.`majorid`
	WHERE s.`studentno` IS NULL;
	
	
	
	#十一、查询没有成绩的学生人数
	SELECT COUNT(*)
	FROM student s
	LEFT JOIN result r ON s.`studentno` = r.`studentno`
	WHERE r.`id` IS NULL











