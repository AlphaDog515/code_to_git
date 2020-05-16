库表操作、数据操作、数据类型

	#DDL
	/*
	数据定义语言

	库和表的管理
	一、库的管理
		创建、修改、删除
	
	二、表的管理
		创建、修改、删除

	创建： create
	修改： alter
	删除： drop
	*/

	#一、库的管理
	#1、库的创建
	/*
	语法：
	create database  [if not exists]库名;
	*/


	#案例：创建库Books
	CREATE DATABASE IF NOT EXISTS books ;


	#2、库的修改
	RENAME DATABASE books TO 新库名;

	#更改库的字符集
	ALTER DATABASE books CHARACTER SET gbk;


	#3、库的删除
	DROP DATABASE IF EXISTS books;



	#二、表的管理
	#1.表的创建 ★
	/*
	语法：
	create table 表名(
		列名 列的类型【(长度) 约束】,
		列名 列的类型【(长度) 约束】,
		列名 列的类型【(长度) 约束】,
		...
		列名 列的类型【(长度) 约束】
	)


	*/
	#案例：创建表Book
	CREATE TABLE book(
		id INT,#编号
		bName VARCHAR(20),#图书名
		price DOUBLE,#价格
		authorId  INT,#作者编号
		publishDate DATETIME#出版日期
	);
	DESC book;

	
	#案例：创建表author
	CREATE TABLE IF NOT EXISTS author(
		id INT,
		au_name VARCHAR(20),
		nation VARCHAR(10)
	)
	DESC author;


	
	#2.表的修改
	/*
	语法
	alter table 表名 add|drop|modify|change column 列名 【列类型 约束】;
	*/

	#①修改列名
	ALTER TABLE book CHANGE COLUMN publishdate pubDate DATETIME;


	#②修改列的类型或约束
	ALTER TABLE book MODIFY COLUMN pubdate TIMESTAMP;

	
	#③添加新列
	ALTER TABLE author ADD COLUMN annual DOUBLE; 

	
	#④删除列
	ALTER TABLE book_author DROP COLUMN  annual;
	
	
	#⑤修改表名
	ALTER TABLE author RENAME TO book_author;
	DESC book;



	#3.表的删除
	DROP TABLE IF EXISTS book_author;
	SHOW TABLES;

	#通用的写法：
	DROP DATABASE IF EXISTS 旧库名;
	CREATE DATABASE 新库名;

	DROP TABLE IF EXISTS 旧表名;
	CREATE TABLE  表名();


	#4.表的复制
	INSERT INTO author VALUES
	(1,'村上春树','日本'),
	(2,'莫言','中国'),
	(3,'冯唐','中国'),
	(4,'金庸','中国');

	SELECT * FROM Author;
	SELECT * FROM copy2;
	
	#1.仅仅复制表的结构
	CREATE TABLE copy LIKE author;

	#2.复制表的结构+数据
	CREATE TABLE copy2 SELECT * FROM author;

	#只复制部分数据
	CREATE TABLE copy3 SELECT id,au_name FROM author WHERE nation='中国';

	#仅仅复制某些字段
	CREATE TABLE copy4 
	SELECT id,au_name
	FROM author
	WHERE 0;


案例分析：
	#1.	创建表dept1	
	USE test;
	CREATE TABLE dept1(
		id INT(7),
		NAME VARCHAR(25)
	);
	
	#2.	将表departments中的数据插入新表dept2中
	CREATE TABLE dept2
	SELECT department_id,department_name
	FROM myemployees.departments;


	#3.	创建表emp5
	CREATE TABLE emp5(
	id INT(7),
	first_name VARCHAR(25),
	last_name VARCHAR(25),
	dept_id INT(7)
	);

	#4.	将列Last_name的长度增加到50
	ALTER TABLE emp5 MODIFY COLUMN last_name VARCHAR(50);
	
	#5.	根据表employees创建employees2
	CREATE TABLE employees2 LIKE myemployees.employees;

	#6.	删除表emp5
	DROP TABLE IF EXISTS emp5;

	#7.	将表employees2重命名为emp5
	ALTER TABLE employees2 RENAME TO emp5;

	#8.在表dept和emp5中添加新列test_column，并检查所作的操作
	ALTER TABLE emp5 ADD COLUMN test_column INT;
	
	#9.直接删除表emp5中的列dept_id
	DESC emp5;
	ALTER TABLE emp5 DROP COLUMN test_column;





	#DML语言
	/*
	数据操作语言：
	插入：insert
	修改：update
	删除：delete
	*/

	#一、插入语句
	#方式一：经典的插入
	/*
	语法：
	insert into 表名(列名,...) values(值1,...);
	*/
	SELECT * FROM beauty;
	
	#1.插入的值的类型要与列的类型一致或兼容
	INSERT INTO beauty(id,NAME,sex,borndate,phone,photo,boyfriend_id)
	VALUES(13,'唐艺昕','女','1990-4-23','1898888888',NULL,2);

	
	#2.不可以为null的列必须插入值。可以为null的列如何插入值？
	#方式一：
	INSERT INTO beauty(id,NAME,sex,borndate,phone,photo,boyfriend_id)
	VALUES(13,'唐艺昕','女','1990-4-23','1898888888',NULL,2);

	#方式二：
	INSERT INTO beauty(id,NAME,sex,phone)
	VALUES(15,'娜扎','女','1388888888');


	#3.列的顺序是否可以调换
	INSERT INTO beauty(NAME,sex,id,phone)
	VALUES('蒋欣','女',16,'110');


	#4.列数和值的个数必须一致
	INSERT INTO beauty(NAME,sex,id,phone)
	VALUES('关晓彤','女',17,'110');

	#5.可以省略列名，默认所有列，而且列的顺序和表中列的顺序一致
	INSERT INTO beauty
	VALUES(18,'张飞','男',NULL,'119',NULL,NULL);

	
	
	#方式二：
	/*
	语法：
	insert into 表名
	set 列名=值,列名=值,...
	*/
	INSERT INTO beauty SET id=19,NAME='刘涛',phone='999';

	#两种方式大pk ★
	#1、方式一支持插入多行,方式二不支持
	INSERT INTO beauty
	VALUES(23,'唐艺昕1','女','1990-4-23','1898888888',NULL,2)
	,(24,'唐艺昕2','女','1990-4-23','1898888888',NULL,2)
	,(25,'唐艺昕3','女','1990-4-23','1898888888',NULL,2);

	#2、方式一支持子查询，方式二不支持
	INSERT INTO beauty(id,NAME,phone) SELECT 26,'宋茜','11809866';
	INSERT INTO beauty(id,NAME,phone) SELECT id,boyname,'1234567' FROM boys WHERE id<3;

	
	
	
	#二、修改语句
	/*
	1.修改单表的记录★
	语法：
	update 表名
	set 列=新值,列=新值,...
	where 筛选条件;

	2.修改多表的记录【补充】
	语法：
	sql92语法：
	update 表1 别名,表2 别名
	set 列=值,...
	where 连接条件
	and 筛选条件;

	sql99语法：
	update 表1 别名
	inner|left|right join 表2 别名
	on 连接条件
	set 列=值,...
	where 筛选条件;
	*/
	#1.修改单表的记录
	#案例1：修改beauty表中姓唐的女神的电话为13899888899
	UPDATE beauty SET phone = '13899888899'
	WHERE NAME LIKE '唐%';

	
	#案例2：修改boys表中id好为2的名称为张飞，魅力值 10
	UPDATE boys SET boyname='张飞',usercp=10
	WHERE id=2;


	#2.修改多表的记录
	#案例 1：修改张无忌的女朋友的手机号为114
	UPDATE boys bo
	INNER JOIN beauty b ON bo.`id`=b.`boyfriend_id`
	SET b.`phone`='119',bo.`userCP`=1000
	WHERE bo.`boyName`='张无忌';



	#案例2：修改没有男朋友的女神的男朋友编号都为2号
	UPDATE boys bo
	RIGHT JOIN beauty b ON bo.`id`=b.`boyfriend_id`
	SET b.`boyfriend_id`=2
	WHERE bo.`id` IS NULL;

	SELECT * FROM boys;


	
	
	#三、删除语句
	/*
	方式一：delete
	语法：
	1、单表的删除【★】
	delete from 表名 where 筛选条件

	2、多表的删除【补充】
	sql92语法：
	delete 表1的别名,表2的别名
	from 表1 别名,表2 别名
	where 连接条件
	and 筛选条件;

	sql99语法：
	delete 表1的别名,表2的别名
	from 表1 别名
	inner|left|right join 表2 别名 on 连接条件
	where 筛选条件;


	方式二：truncate
	语法：truncate table 表名;
	*/

	#方式一：delete
	#1.单表的删除
	#案例：删除手机号以9结尾的女神信息
	DELETE FROM beauty WHERE phone LIKE '%9';
	SELECT * FROM beauty;


	#2.多表的删除
	#案例：删除张无忌的女朋友的信息
	DELETE b
	FROM beauty b
	INNER JOIN boys bo ON b.`boyfriend_id` = bo.`id`
	WHERE bo.`boyName`='张无忌';


	#案例：删除黄晓明的信息以及他女朋友的信息
	DELETE b,bo
	FROM beauty b
	INNER JOIN boys bo ON b.`boyfriend_id`=bo.`id`
	WHERE bo.`boyName`='黄晓明';



	#方式二：truncate语句
	#案例：将魅力值>100的男神信息删除
	TRUNCATE TABLE boys ;

	#delete pk truncate【面试题★】

	/*
	1.delete 可以加where条件，truncate不能加

	2.truncate删除，效率高一丢丢
	
	3.假如要删除的表中有自增长列，
		如果用delete删除后，再插入数据，自增长列的值从断点开始，
		而truncate删除后，再插入数据，自增长列的值从1开始。
	
	4.truncate删除没有返回值，delete删除有返回值

	5.truncate删除不能回滚，delete删除可以回滚.
	*/
	SELECT * FROM boys;

	DELETE FROM boys;
	TRUNCATE TABLE boys;
	INSERT INTO boys (boyname,usercp)
	VALUES('张飞',100),('刘备',100),('关云长',100);






案例分析：
	#1.	运行以下脚本创建表my_employees
	USE myemployees;
	CREATE TABLE my_employees(
		Id INT(10),
		First_name VARCHAR(10),
		Last_name VARCHAR(10),
		Userid VARCHAR(10),
		Salary DOUBLE(10,2)
	);
	
	CREATE TABLE users(
		id INT,
		userid VARCHAR(10),
		department_id INT
	);
	
	
	#2.显示表my_employees的结构
	DESC my_employees;

	
	#3.	向my_employees表中插入下列数据
	ID	FIRST_NAME	LAST_NAME	USERID		SALARY
	1	patel		Ralph		Rpatel		895
	2	Dancs		Betty		Bdancs		860
	3	Biri		Ben			Bbiri		1100
	4	Newman		Chad		Cnewman		750
	5	Ropeburn	Audrey		Aropebur	1550

	
	#方式一：
	INSERT INTO my_employees
	VALUES(1,'patel','Ralph','Rpatel',895),
	(2,'Dancs','Betty','Bdancs',860),
	(3,'Biri','Ben','Bbiri',1100),
	(4,'Newman','Chad','Cnewman',750),
	(5,'Ropeburn','Audrey','Aropebur',1550);
	DELETE FROM my_employees;
	
	#方式二：
	INSERT INTO my_employees
	SELECT 1,'patel','Ralph','Rpatel',895 UNION
	SELECT 2,'Dancs','Betty','Bdancs',860 UNION
	SELECT 3,'Biri','Ben','Bbiri',1100 UNION
	SELECT 4,'Newman','Chad','Cnewman',750 UNION
	SELECT 5,'Ropeburn','Audrey','Aropebur',1550;

					
	#4.	 向users表中插入数据
	1	Rpatel		10
	2	Bdancs		10
	3	Bbiri		20
	4	Cnewman		30
	5	Aropebur	40

	INSERT INTO users VALUES(1,'Rpatel',10),(2,'Bdancs',10),(3,'Bbiri',20);



	#5.将3号员工的last_name修改为“drelxer”
	UPDATE my_employees SET last_name='drelxer' WHERE id = 3;


	#6.将所有工资少于900的员工的工资修改为1000
	UPDATE my_employees SET salary=1000 WHERE salary<900;

	
	#7.将userid为Bbiri的user表和my_employees表的记录全部删除
	DELETE u,e
	FROM users u
	JOIN my_employees e ON u.`userid`=e.`Userid`
	WHERE u.`userid`='Bbiri';

	
	#8.删除所有数据
	DELETE FROM my_employees;
	DELETE FROM users;
	
	
	#9.检查所作的修正
	SELECT * FROM my_employees;
	SELECT * FROM users;

	#10.清空表my_employees
	TRUNCATE TABLE my_employees;




	


	#常见的数据类型
	/*
	数值型：
		整型
		小数：
			定点数
			浮点数
	
	字符型：
		较短的文本：char、varchar
		较长的文本：text、blob（较长的二进制数据）

	日期型：
	*/

	#一、整型
	/*
	分类：
	tinyint		smallint	mediumint	int/integer		bigint
	1	 		2			3			4				8

	特点：
	① 如果不设置无符号还是有符号，默认是有符号，如果想设置无符号，需要添加unsigned关键字
	② 如果插入的数值超出了整型的范围,会报out of range异常，并且插入临界值
	③ 如果不设置长度，会有默认的长度
	
	长度代表了显示的最大宽度，如果不够会用0在左边填充，但必须搭配zerofill使用！
	*/

	#1.如何设置无符号和有符号
	DROP TABLE IF EXISTS tab_int;
	CREATE TABLE tab_int(
		t1 INT(7) ZEROFILL,
		t2 INT(7) ZEROFILL 
	);

	DESC tab_int;

	INSERT INTO tab_int VALUES(-123456);
	INSERT INTO tab_int VALUES(-123456,-123456);
	INSERT INTO tab_int VALUES(2147483648,4294967296);
	INSERT INTO tab_int VALUES(123,123);

	SELECT * FROM tab_int;


	
	#二、小数
	/*
	分类：
	1.浮点型
		float(M,D)
		double(M,D)
	
	2.定点型
		dec(M，D)
		decimal(M,D)

	特点：	
		M：整数部位+小数部位
		D：小数部位
		如果超过范围，则插入临界值
	
		M和D都可以省略
		如果是decimal，则M默认为10，D默认为0
		如果是float和double，则会根据插入的数值的精度来决定精度
	
		定点型的精确度较高，如果要求插入数值的精度较高如货币运算等则考虑使用
	*/
	#测试M和D
	DROP TABLE tab_float;
	CREATE TABLE tab_float(
		f1 FLOAT,
		f2 DOUBLE,
		f3 DECIMAL
	);
	SELECT * FROM tab_float;
	DESC tab_float;

	INSERT INTO tab_float VALUES(123.4523,123.4523,123.4523);
	INSERT INTO tab_float VALUES(123.456,123.456,123.456);
	INSERT INTO tab_float VALUES(123.4,123.4,123.4);
	INSERT INTO tab_float VALUES(1523.4,1523.4,1523.4);
	
	#原则：
	/*
	所选择的类型越简单越好，能保存数值的类型越小越好
	*/

	
	
	#三、字符型
	/*
	较短的文本：
	char
	varchar

	其他：
	binary和varbinary用于保存较短的二进制
	enum用于保存枚举
	set用于保存集合

	较长的文本：
	text
	blob(较大的二进制)
	
	特点：
			写法	    M的意思	 						 特点	         空间的耗费	 效率
	char	char(M)	    最大的字符数，可以省略，默认为1	 固定长度的字符  比较耗费	 高
	varchar varchar(M)	最大的字符数，不可以省略         可变长度的字符	 比较节省	 低
	*/
	
	CREATE TABLE tab_char(
		c1 ENUM('a','b','c')
	);

	INSERT INTO tab_char VALUES('a');
	INSERT INTO tab_char VALUES('b');
	INSERT INTO tab_char VALUES('c');
	INSERT INTO tab_char VALUES('m');
	INSERT INTO tab_char VALUES('A');

	SELECT * FROM tab_set;

	CREATE TABLE tab_set(
		s1 SET('a','b','c','d')
	);
	
	INSERT INTO tab_set VALUES('a');
	INSERT INTO tab_set VALUES('A,B');
	INSERT INTO tab_set VALUES('a,c,d');


	
	
	#四、日期型
	/*
	分类：
	date只保存日期
	time 只保存时间
	year只保存年

	datetime保存日期+时间
	timestamp保存日期+时间

	特点：
			   字节		范围		   时区等的影响
	datetime	8		1000——9999	      不受
	timestamp	4	    1970-2038	       受
	*/

	CREATE TABLE tab_date(
		t1 DATETIME,
		t2 TIMESTAMP
	);

	INSERT INTO tab_date VALUES(NOW(),NOW());

	SELECT * FROM tab_date;

	SHOW VARIABLES LIKE 'time_zone';

	SET time_zone='+9:00';





	
	#标识列
	/*
	又称为自增长列
	含义：可以不用手动的插入值，系统提供默认的序列值

	特点：
	1、标识列必须和主键搭配吗？不一定，但要求是一个key
	2、一个表可以有几个标识列？至多一个！
	3、标识列的类型只能是数值型
	4、标识列可以通过 SET auto_increment_increment=3;设置步长
	   可以通过手动插入值，设置起始值
	*/

	#一、创建表时设置标识列
	DROP TABLE IF EXISTS tab_identity;
	CREATE TABLE tab_identity(
		id INT,
		NAME FLOAT UNIQUE AUTO_INCREMENT,
		seat INT);
	
	TRUNCATE TABLE tab_identity;

	INSERT INTO tab_identity(id,NAME) VALUES(NULL,'john');
	INSERT INTO tab_identity(NAME) VALUES('lucy');
	SELECT * FROM tab_identity;

	SHOW VARIABLES LIKE '%auto_increment%';

	SET auto_increment_increment=3;



	#常见约束
	/*
	含义：一种限制，用于限制表中的数据，为了保证表中的数据的准确和可靠性

	分类：六大约束
		NOT NULL: 非空，用于保证该字段的值不能为空，比如姓名、学号等
		
		DEFAULT: 默认，用于保证该字段有默认值，比如性别
		
		PRIMARY KEY: 主键，用于保证该字段的值具有唯一性，并且非空，比如学号、员工编号等
		
		UNIQUE: 唯一，用于保证该字段的值具有唯一性，可以为空，比如座位号
		
		CHECK: 检查约束【mysql中不支持】，比如年龄、性别
		
		FOREIGN KEY: 外键，用于限制两个表的关系，用于保证该字段的值必须来自于主表的关联列的值
					 在从表添加外键约束，用于引用主表中某列的值
					 比如学生表的专业编号，员工表的部门编号，员工表的工种编号		

	添加约束的时机：
		1.创建表时
		2.修改表时		

	约束的添加分类：
		列级约束：
			六大约束语法上都支持，但外键约束没有效果
			
		表级约束：			
			除了非空、默认，其他的都支持
			
			
	主键和唯一的大对比：
			保证唯一性  是否允许为空    一个表中可以有多少个    是否允许组合
		主键	√			×			至多有1个           	√，但不推荐
		唯一	√			√			可以有多个          	√，但不推荐
	
	外键：
		1、要求在从表设置外键关系
		2、从表的外键列的类型和主表的关联列的类型要求一致或兼容，名称无要求
		3、主表的关联列必须是一个key（一般是主键或唯一）
		4、插入数据时，先插入主表，再插入从表; 删除数据时，先删除从表，再删除主表
	*/

	CREATE TABLE 表名(
		字段名 字段类型 列级约束,
		字段名 字段类型,
		表级约束
	)
	
	CREATE DATABASE students;
	#一、创建表时添加约束
	#1.添加列级约束
	/*
	语法：
	直接在字段名和类型后面追加 约束类型即可。
	只支持：默认、非空、主键、唯一
	*/
	USE students;
	DROP TABLE stuinfo;
	CREATE TABLE stuinfo(
		id INT PRIMARY KEY,									#主键
		stuName VARCHAR(20) NOT NULL UNIQUE,				#非空
		gender CHAR(1) CHECK(gender='男' OR gender ='女'),	#检查
		seat INT UNIQUE,									#唯一
		age INT DEFAULT  18,								#默认约束
		majorId INT REFERENCES major(id)					#外键
	);

	CREATE TABLE major(
		id INT PRIMARY KEY,
		majorName VARCHAR(20)
	);

	#查看stuinfo中的所有索引，包括主键、外键、唯一
	SHOW INDEX FROM stuinfo;


	#2.添加表级约束
	/*
	语法：在各个字段的最下面
	 【constraint 约束名】 约束类型(字段名) 
	*/
	DROP TABLE IF EXISTS stuinfo;
	CREATE TABLE stuinfo(
		id INT,
		stuname VARCHAR(20),
		gender CHAR(1),
		seat INT,
		age INT,
		majorid INT,
		
		CONSTRAINT pk PRIMARY KEY(id),# 主键
		CONSTRAINT uq UNIQUE(seat), # 唯一键
		CONSTRAINT ck CHECK(gender ='男' OR gender  = '女'),#检查 mysql不支持
		
		CONSTRAINT fk_stuinfo_major FOREIGN KEY(majorid) REFERENCES major(id)#外键		
	);

	SHOW INDEX FROM stuinfo;

	
	#通用的写法：★
	CREATE TABLE IF NOT EXISTS stuinfo(
		id INT PRIMARY KEY,
		stuname VARCHAR(20),
		sex CHAR(1),
		age INT DEFAULT 18,
		seat INT UNIQUE,
		majorid INT,
		CONSTRAINT fk_stuinfo_major FOREIGN KEY(majorid) REFERENCES major(id)
	);



	#二、修改表时添加约束,修改表时表中没有约束
	/*
	1、添加列级约束
	alter table 表名 modify column 字段名 字段类型 新约束;

	2、添加表级约束
	alter table 表名 add 【constraint 约束名】 约束类型(字段名) 【外键的引用】;


	*/
	DROP TABLE IF EXISTS stuinfo;
	CREATE TABLE stuinfo(
		id INT,
		stuname VARCHAR(20),
		gender CHAR(1),
		seat INT,
		age INT,
		majorid INT
	)
	DESC stuinfo;
	
	#1.添加非空约束
	ALTER TABLE stuinfo MODIFY COLUMN stuname VARCHAR(20)  NOT NULL;
	
	#2.添加默认约束
	ALTER TABLE stuinfo MODIFY COLUMN age INT DEFAULT 18;
	
	#3.添加主键
	#①列级约束
	ALTER TABLE stuinfo MODIFY COLUMN id INT PRIMARY KEY;
	#②表级约束
	ALTER TABLE stuinfo ADD PRIMARY KEY(id);

	#4.添加唯一
	#①列级约束
	ALTER TABLE stuinfo MODIFY COLUMN seat INT UNIQUE;
	#②表级约束
	ALTER TABLE stuinfo ADD UNIQUE(seat);

	#5.添加外键
	ALTER TABLE stuinfo ADD CONSTRAINT fk_stuinfo_major FOREIGN KEY(majorid) REFERENCES major(id); 

	
	#三、修改表时删除约束
	#1.删除非空约束
	ALTER TABLE stuinfo MODIFY COLUMN stuname VARCHAR(20) NULL;

	#2.删除默认约束
	ALTER TABLE stuinfo MODIFY COLUMN age INT ;

	#3.删除主键
	ALTER TABLE stuinfo DROP PRIMARY KEY;

	#4.删除唯一
	ALTER TABLE stuinfo DROP INDEX seat;

	#5.删除外键
	ALTER TABLE stuinfo DROP FOREIGN KEY fk_stuinfo_major;

	SHOW INDEX FROM stuinfo;



案例分析：
	#1.向表emp2的id列中添加PRIMARY KEY约束（my_emp_id_pk）
	ALTER TABLE emp2 MODIFY COLUMN id INT PRIMARY KEY;
	ALTER TABLE emp2 ADD CONSTRAINT my_emp_id_pk PRIMARY KEY(id);

	
	#2.	向表dept2的id列中添加PRIMARY KEY约束（my_dept_id_pk）	
	#3.	向表emp2中添加列dept_id，并在其中定义FOREIGN KEY约束，与之相关联的列是dept2表中的id列。
	ALTER TABLE emp2 ADD COLUMN dept_id INT;
	ALTER TABLE emp2 ADD CONSTRAINT fk_emp2_dept2 FOREIGN KEY(dept_id) REFERENCES dept2(id);

	
				位置			支持的约束类型					是否可以起约束名
	列级约束：	列的后面		语法都支持，但外键没有效果		不可以
	表级约束：	所有列的下面	默认和非空不支持，其他支持		可以（主键没有效果）
		
	
