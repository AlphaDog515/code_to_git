存储过程、变量、函数、流程
	
	#变量
	/*
	系统变量：
		全局变量
		会话变量

	自定义变量：
		用户变量
		局部变量
	
	set names gbk ：把客户端改成“gdk”编码，服务器端不受影响，数据库的编码还是默认的。
	*/
	#一、系统变量
	/*
	说明：变量由系统定义，不是用户定义，属于服务器层面
	注意：全局变量需要添加global关键字，会话变量需要添加session关键字，如果不写，默认会话级别
	
	使用步骤：
	1、查看所有系统变量
		show global|【session】variables;
	
	2、查看满足条件的部分系统变量
		show global|【session】 variables like '%char%';
	
	3、查看指定的系统变量的值
		select @@global|【session】.系统变量名;
	
	4、为某个系统变量赋值
		方式一：
		set global|【session】系统变量名=值;
		
		方式二：
		set @@global|【session】.系统变量名=值;
	*/
	
	#1》全局变量
	/*
	作用域：针对于所有会话（连接）有效，但不能跨重启
	*/
	#①查看所有全局变量
	SHOW GLOBAL VARIABLES;
	
	#②查看满足条件的部分系统变量
	SHOW GLOBAL VARIABLES LIKE '%char%';
	
	#③查看指定的系统变量的值
	SELECT @@global.autocommit;  // 1 自动提交打开
	
	#④为某个系统变量赋值
	SET @@global.autocommit=0;
	SET GLOBAL autocommit=0;

	
	
	#2》会话变量
	/*
	作用域：针对于当前会话（连接）有效
	*/
	#①查看所有会话变量
	SHOW SESSION VARIABLES;
	
	#②查看满足条件的部分会话变量
	SHOW SESSION VARIABLES LIKE '%char%';
	
	#③查看指定的会话变量的值
	SELECT @@autocommit;
	SELECT @@session.tx_isolation;
	
	#④为某个会话变量赋值
	SET @@session.tx_isolation='read-uncommitted';
	SET SESSION tx_isolation='read-committed';

	
	
	#二、自定义变量
	/*
	说明：变量由用户自定义，而不是系统提供的
	使用步骤：
	1、声明
	2、赋值
	3、使用（查看、比较、运算等）
	*/

	#1》用户变量
	/*
	作用域：针对于当前会话（连接）有效，作用域同于会话变量
	*/
	#赋值操作符：=或:=
	#①声明必须初始化
	SET @变量名=值;
	SET @变量名:=值;
	SELECT @变量名:=值;  // 也可以赋值，不能用：@变量名=值

	#②赋值（更新变量的值）
	#方式一：
		SET @变量名=值;
		SET @变量名:=值;
		SELECT @变量名:=值;	
	#方式二：
		SELECT 字段 INTO @变量名 FROM 表;
	
	#③使用（查看变量的值）	
	SELECT @变量名;

	#案例：
		#声明并初始化
		SET @NAME='jack'
		SET @NAME=100
		SET @COUNT=1
		
		#赋值
		SELECT COUNT(*) INTO @COUNT FROM employees
		
		#查看
		SELECT @count
	
	
	

	#2》局部变量
	/*
	作用域：仅仅在定义它的begin end块中有效
	必须用在 begin end中的第一句话
	*/
	#①声明
	DECLARE 变量名 类型;
	DECLARE 变量名 类型 【DEFAULT 值】;

	#②赋值（更新变量的值）
	#方式一：
		SET 局部变量名=值;
		SET 局部变量名:=值;
		SELECT 局部变量名:=值;
	#方式二：
		SELECT 字段 INTO 具备变量名	FROM 表;
	
	#③使用（查看变量的值）
	SELECT 局部变量名;

	#案例：声明两个变量，求和并打印
	#用户变量
	SET @m=1;
	SET @n=1;
	SET @sum=@m+@n;
	SELECT @sum;

	#局部变量，下面报错，放在begin end中的第一句话
	DECLARE m INT DEFAULT 1;
	DECLARE n INT DEFAULT 1;
	DECLARE SUM INT;
	SET SUM=m+n;
	SELECT SUM;


	#用户变量和局部变量的对比
				作用域					定义位置				语法
	用户变量	当前会话				会话的任何地方			加@符号，不用指定类型
	局部变量	定义它的BEGIN END中 	BEGIN END的第一句话		一般不用加@,需要指定类型
	
	
	
	
	
	
	#存储过程和函数
	/*
	存储过程和函数：类似于java中的方法
	好处：
	1、提高代码的重用性
	2、简化操作
	*/
	
	#存储过程
	/*
	含义：一组预先编译好的SQL语句的集合，理解成批处理语句
	1、提高代码的重用性
	2、简化操作
	3、减少了编译次数并且减少了和数据库服务器的连接次数，提高了效率
	*/

	#一、创建语法procedure
	CREATE PROCEDURE 存储过程名(参数列表)
	BEGIN
		存储过程体（一组合法的SQL语句）
	END

	#注意：
	/*
	1、参数列表包含三部分
	参数模式  参数名  参数类型
	举例：
	in stuname varchar(20)

	参数模式：
	in：   该参数可以作为输入，也就是该参数需要调用方传入值，默认，可省略
	out：  该参数可以作为输出，也就是该参数可以作为返回值
	inout：该参数既可以作为输入又可以作为输出，也就是该参数既需要传入值，又可以返回值

	2、 如果存储过程体仅仅只有一句话，begin end可以省略；
		
		存储过程体中的每条sql语句的结尾要求必须加分号；
		
		存储过程的结尾可以使用delimiter重新设置：
		语法：delimiter 结束标记
		案例：delimiter $
	*/
	
	#二、调用语法
	CALL 存储过程名(实参列表);

	
	#--------------------------------案例演示-----------------------------------
	#1.空参列表
	#案例：插入到admin表中五条记录
	SELECT * FROM admin;

	DELIMITER $
	CREATE PROCEDURE myp1()
	BEGIN
		INSERT INTO admin(username,`password`) 
		VALUES('john1','0000'),('lily','0000'),('rose','0000'),('jack','0000'),('tom','0000');
	END $

	#调用
	CALL myp1()$ 	// 命令行中$结尾

	
	
	#2.创建带in模式参数的存储过程
	#案例1：创建存储过程实现，根据女神名，查询对应的男神信息
	CREATE PROCEDURE myp2(IN beautyName VARCHAR(20))
	BEGIN
		SELECT bo.*
		FROM boys bo
		RIGHT JOIN beauty b ON bo.id = b.boyfriend_id
		WHERE b.name=beautyName;
	END $

	#调用
	CALL myp2('柳岩')$

	
	
	#案例2 ：创建存储过程实现，用户是否登录成功
	CREATE PROCEDURE myp4(IN username VARCHAR(20),IN PASSWORD VARCHAR(20))
	BEGIN
		DECLARE result INT DEFAULT 0;		#声明并初始化		
		SELECT COUNT(*) INTO result			#赋值
		FROM admin WHERE admin.username = username AND admin.password = PASSWORD;		
		SELECT IF(result>0,'成功','失败');	#使用
	END $

	#调用
	CALL myp4('张飞','8888')$


	
	#3.创建out 模式参数的存储过程
	#案例1：根据输入的女神名，返回对应的男神名
	CREATE PROCEDURE myp6(IN beautyName VARCHAR(20),OUT boyName VARCHAR(20))
	BEGIN
		SELECT bo.boyname INTO boyname
		FROM boys bo
		RIGHT JOIN
		beauty b ON b.boyfriend_id = bo.id
		WHERE b.name=beautyName ;		
	END $
	
	#调用
	CALL myp6('小昭',@name)$
	SELECT @name$
	

	#案例2：根据输入的女神名，返回对应的男神名和魅力值
	CREATE PROCEDURE myp7(IN beautyName VARCHAR(20),OUT boyName VARCHAR(20),OUT usercp INT) 
	BEGIN
		SELECT boys.boyname,boys.usercp INTO boyname,usercp
		FROM boys 
		RIGHT JOIN
		beauty b ON b.boyfriend_id = boys.id
		WHERE b.name=beautyName ;		
	END $

	#调用
	CALL myp7('小昭',@name,@cp)$
	SELECT @name,@cp$



	#4.创建带inout模式参数的存储过程
	#案例1：传入a和b两个值，最终a和b都翻倍并返回
	CREATE PROCEDURE myp8(INOUT a INT,INOUT b INT)
	BEGIN
		SET a=a*2;
		SET b=b*2;
	END $

	#调用
	SET @m=10$
	SET @n=20$
	CALL myp8(@m,@n)$
	SELECT @m,@n$


	
	
	#三、删除存储过程
	#语法：drop procedure 存储过程名
	DROP PROCEDURE p1;
	DROP PROCEDURE p2,p3;	#× 只能一次删除一个

	#四、查看存储过程的信息
	DESC myp2;		// ×
	SHOW CREATE PROCEDURE  myp2;





案例分析：
	#一、创建存储过程实现传入用户名和密码，插入到admin表中
	CREATE PROCEDURE test_pro1(IN username VARCHAR(20),IN loginPwd VARCHAR(20))
	BEGIN
		INSERT INTO admin(admin.username,PASSWORD)
		VALUES(username,loginpwd);
	END $

	
	#二、创建存储过程实现传入女神编号，返回女神名称和女神电话
	CREATE PROCEDURE test_pro2(IN id INT,OUT NAME VARCHAR(20),OUT phone VARCHAR(20))
	BEGIN
		SELECT b.name,b.phone INTO NAME,phone
		FROM beauty b
		WHERE b.id = id;
	END $
	
	
	#三、创建存储存储过程或函数实现传入两个女神生日，返回大小
	CREATE PROCEDURE test_pro3(IN birth1 DATETIME,IN birth2 DATETIME,OUT result INT)
	BEGIN
		SELECT DATEDIFF(birth1,birth2) INTO result;
	END $


	#四、创建存储过程或函数实现传入一个日期，格式化成xx年xx月xx日并返回
	CREATE PROCEDURE test_pro4(IN mydate DATETIME,OUT strDate VARCHAR(50))
	BEGIN
		SELECT DATE_FORMAT(mydate,'%y年%m月%d日') INTO strDate;
	END $
	#调用
	CALL test_pro4(NOW(),@str)$
	SELECT @str $
	
	
	#五、创建存储过程或函数实现传入女神名称，返回：女神 and 男神格式的字符串
	如：传入：小昭
	    返回：小昭 AND 张无忌
	DROP PROCEDURE test_pro5 $
	CREATE PROCEDURE test_pro5(IN beautyName VARCHAR(20),OUT str VARCHAR(50))
	BEGIN
		SELECT CONCAT(beautyName,' and ',IFNULL(boyName,'null')) INTO str
		FROM boys bo
		RIGHT JOIN beauty b ON b.boyfriend_id = bo.id
		WHERE b.name=beautyName;		
	END $

	CALL test_pro5('柳岩',@str)$
	SELECT @str $



	#六、创建存储过程或函数，根据传入的条目数和起始索引，查询beauty表的记录
	DROP PROCEDURE test_pro6$
	CREATE PROCEDURE test_pro6(IN startIndex INT,IN size INT)
	BEGIN
		SELECT * FROM beauty LIMIT startIndex,size;
	END $

	CALL test_pro6(3,5)$






	#函数
	/*
	含义：一组预先编译好的SQL语句的集合，理解成批处理语句
	1、提高代码的重用性
	2、简化操作
	3、减少了编译次数并且减少了和数据库服务器的连接次数，提高了效率

	区别：
	存储过程：可以有0个返回，也可以有多个返回，适合做批量插入、批量更新
	函数：	  有且仅有1个返回，适合做处理数据后返回一个结果
	*/

	#一、创建语法
	CREATE FUNCTION 函数名(参数列表) RETURNS 返回类型
	BEGIN
		函数体
	END
	/*
	注意：
	1.参数列表包含两部分：
	参数名 参数类型

	2.函数体：肯定会有return语句，如果没有会报错
	如果return语句没有放在函数体的最后也不报错，但不建议

	return 值;
	3.函数体中仅有一句话，则可以省略begin end
	4.使用 delimiter语句设置结束标记
	*/

	#二、调用语法
	SELECT 函数名(参数列表)


	#------------------------------案例演示----------------------------
	#1.无参有返回
	#案例：返回公司的员工个数
	CREATE FUNCTION myf1() RETURNS INT
	BEGIN
		DECLARE c INT DEFAULT 0;	#定义局部变量
		SELECT COUNT(*) INTO c		#赋值
		FROM employees;
		RETURN c;		
	END $

	SELECT myf1()$


	
	#2.有参有返回
	#案例1：根据员工名，返回它的工资
	CREATE FUNCTION myf2(empName VARCHAR(20)) RETURNS DOUBLE
	BEGIN
		SET @sal=0;					#定义用户变量 
		SELECT salary INTO @sal   	#赋值，salary只能是一个
		FROM employees
		WHERE last_name = empName;		
		RETURN @sal;
	END $

	SELECT myf2('k_ing') $

	
	
	#案例2：根据部门名，返回该部门的平均工资
	CREATE FUNCTION myf3(deptName VARCHAR(20)) RETURNS DOUBLE
	BEGIN
		DECLARE sal DOUBLE ;
		SELECT AVG(salary) INTO sal FROM employees e
		JOIN departments d ON e.department_id = d.department_id
		WHERE d.department_name=deptName;
		RETURN sal;
	END $

	SELECT myf3('IT')$

	
	
	#三、查看函数
	SHOW CREATE FUNCTION myf3;

	#四、删除函数
	DROP FUNCTION myf3;

	
	#案例
	#一、创建函数，实现传入两个float，返回二者之和
	CREATE FUNCTION test_fun1(num1 FLOAT,num2 FLOAT) RETURNS FLOAT
	BEGIN
		DECLARE SUM FLOAT DEFAULT 0;
		SET SUM=num1+num2;
		RETURN SUM;
	END $

	SELECT test_fun1(1,2) $









	#流程控制结构
	/*
	顺序、分支、循环
	*/

	#一、分支结构
	#1.if函数
	/*
	语法：if(条件,值1，值2)
	功能：实现双分支
	应用在begin end中或外面
	*/

	#2.case结构
	/*
	语法：
	情况1：类似于switch，一般用于等值判断
		case 变量或表达式
		when 值1 then 语句1;
		when 值2 then 语句2;
		...
		else 语句n;
		end 

	情况2：类似于多重if，一般实现区间判断
		case 
		when 条件1 then 语句1;
		when 条件2 then 语句2;
		...
		else 语句n;
		end 

	应用在begin end 中或外面
	*/

	#3.if结构
	/*
	语法：
	if 条件1 then 语句1;
	elseif 条件2 then 语句2;
	....
	else 语句n;
	end if;
	功能：类似于多重if

	只能应用在begin end 中
	*/

	#案例1：创建函数，实现传入成绩，如果成绩>90,返回A，如果成绩>80,返回B，
			如果成绩>60,返回C，否则返回D
	CREATE FUNCTION test_if(score FLOAT) RETURNS CHAR
	BEGIN
		DECLARE ch CHAR DEFAULT 'A';
		IF score>90 THEN SET ch='A';
		ELSEIF score>80 THEN SET ch='B';
		ELSEIF score>60 THEN SET ch='C';
		ELSE SET ch='D';
		END IF;
		RETURN ch;		
	END $

	SELECT test_if(87)$

	
	
	
	#案例2：创建存储过程，如果工资<2000,则删除，如果5000>工资>2000,则涨工资1000，否则涨工资500
	CREATE PROCEDURE test_if_pro(IN sal DOUBLE)
	BEGIN
		IF sal<2000 THEN DELETE FROM employees WHERE employees.salary=sal;
		ELSEIF sal>=2000 AND sal<5000 THEN UPDATE employees SET salary=salary+1000 WHERE employees.`salary`=sal;
		ELSE UPDATE employees SET salary=salary+500 WHERE employees.`salary`=sal;
		END IF;		
	END $

	CALL test_if_pro(2100)$

	
	
	
	#案例3：创建函数，实现传入成绩，如果成绩>90,返回A，如果成绩>80,返回B，
			如果成绩>60,返回C，否则返回D
	CREATE FUNCTION test_case(score FLOAT) RETURNS CHAR
	BEGIN 
		DECLARE ch CHAR DEFAULT 'A';		
		CASE 
		WHEN score>90 THEN SET ch='A';
		WHEN score>80 THEN SET ch='B';
		WHEN score>60 THEN SET ch='C';
		ELSE SET ch='D';
		END CASE;		
		RETURN ch;
	END $

	SELECT test_case(56)$


	create procedure test_case(in score int)
	begin
		case
		when score>=90 and score <= 100 then select 'A';
		when score>=80 then select 'B';
		when score>=60 then select 'C';
		else select 'D';
		end case;
	end $
	call test_case(95)$
	
	

	#二、循环结构
	/*
	分类：
	while、loop、repeat

	循环控制：
	iterate		类似于continue，继续，结束本次循环，继续下一次
	leave 		类似于break，跳出，结束当前所在的循环
	*/

	#1.while
	/*
	语法：可选标签
	【标签:】while 循环条件 do
		循环体;
	end while【 标签】;

	联想：
	while(循环条件){
		循环体;
	}
	*/

	
	#2.loop
	/*
	语法：
	【标签:】loop
		循环体;
	end loop 【标签】;
	
	可以用来模拟简单的死循环
	*/

	
	#3.repeat
	/*
	语法：
	【标签：】repeat
		循环体;
	until 结束循环的条件
	end repeat 【标签】;
	*/

	#1.没有添加循环控制语句
	#案例：批量插入，根据次数插入到admin表中多条记录
	DROP PROCEDURE pro_while1$
	CREATE PROCEDURE pro_while1(IN insertCount INT)
	BEGIN
		DECLARE i INT DEFAULT 1;
		WHILE i<=insertCount DO
			INSERT INTO admin(username,`password`) VALUES(CONCAT('Rose',i),'666');
			SET i=i+1;
		END WHILE;		
	END $

	CALL pro_while1(100)$
	
	/*
	int i=1;
	while(i<=insertcount){
		//插入		
		i++;
	}
	*/


	#2.添加leave语句
	#案例：批量插入，根据次数插入到admin表中多条记录，如果次数>20则停止
	TRUNCATE TABLE admin$
	DROP PROCEDURE test_while1$
	CREATE PROCEDURE test_while1(IN insertCount INT)
	BEGIN
		DECLARE i INT DEFAULT 1;
		a:WHILE i<=insertCount DO
			INSERT INTO admin(username,`password`) VALUES(CONCAT('xiaohua',i),'0000');
			IF i>=20 THEN LEAVE a;
			END IF;
			SET i=i+1;
		END WHILE a;
	END $
	
	CALL test_while1(100)$


	
	#3.添加iterate语句
	#案例：批量插入，根据次数插入到admin表中多条记录，只插入偶数次
	TRUNCATE TABLE admin$
	DROP PROCEDURE test_while1$
	CREATE PROCEDURE test_while1(IN insertCount INT)
	BEGIN
		DECLARE i INT DEFAULT 0;
		a:WHILE i<=insertCount DO
			SET i=i+1;
			IF MOD(i,2)!=0 THEN ITERATE a;
			END IF;			
			INSERT INTO admin(username,`password`) VALUES(CONCAT('xiaohua',i),'0000');			
		END WHILE a;
	END $

	CALL test_while1(100)$

	/*
	int i=0;
	while(i<=insertCount){
		i++;
		if(i%2==0){
			continue;
		}
		插入		
	}
	*/



案例分析：
	/*一、已知表stringcontent
	其中字段：
	id 自增长
	content varchar(20)
	向该表插入指定个数的，随机的字符串
	*/
	DROP TABLE IF EXISTS stringcontent;
	CREATE TABLE stringcontent(
		id INT PRIMARY KEY AUTO_INCREMENT,
		content VARCHAR(20)		
	);
	DELIMITER $
	CREATE PROCEDURE test_randstr_insert(IN insertCount INT)
	BEGIN
		DECLARE i INT DEFAULT 1;
		DECLARE str VARCHAR(26) DEFAULT 'abcdefghijklmnopqrstuvwxyz';
		DECLARE startIndex INT;		#代表初始索引
		DECLARE len INT;			#代表截取的字符长度
		WHILE i<=insertcount DO
			SET startIndex=FLOOR(RAND()*26+1);			#代表初始索引，随机范围1-26
			SET len=FLOOR(RAND()*(20-startIndex+1)+1);	#代表截取长度，随机范围1-（20-startIndex+1）
			INSERT INTO stringcontent(content) VALUES(SUBSTR(str,startIndex,len));
			SET i=i+1;
		END WHILE;
	END $

	CALL test_randstr_insert(10)$







