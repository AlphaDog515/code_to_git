##本单元目标
	一、为什么要学习数据库
	二、数据库的相关概念      
		DBMS、DB、SQL
	三、数据库存储数据的特点
	四、初始MySQL
		MySQL产品的介绍        
		MySQL产品的安装                 
		MySQL服务的启动和停止    
		MySQL服务的登录和退出           
		MySQL的常见命令和语法规范      
	五、DQL语言的学习                 
		基础查询                     
		条件查询  	   			
		排序查询  	  				
		常见函数                       
		分组函数                      
		分组查询		   			
		连接查询	 				
		子查询                         
		分页查询                     
		union联合查询				
		
	六、DML语言的学习               
		插入语句						
		修改语句						
		删除语句						
	七、DDL语言的学习  
		库和表的管理	 				
		常见数据类型介绍           
		常见约束  	  		
	八、TCL语言的学习
		事务和事务处理                 
	九、视图的讲解           
	十、变量                      
	十一、存储过程和函数   
	十二、流程控制结构       

##数据库的好处
	1.持久化数据到本地
	2.可以实现结构化查询，方便管理
	


##数据库相关概念
	1、DB：数据库，保存一组有组织的数据的容器
	2、DBMS：数据库管理系统，又称为数据库软件（产品），用于管理DB中的数据
	3、SQL:结构化查询语言，用于和DBMS通信的语言

##数据库存储数据的特点
	1、将数据放到表中，表再放到库中
	2、一个数据库中可以有多个表，每个表都有一个的名字，用来标识自己。表名具有唯一性。
	3、表具有一些特性，这些特性定义了数据在表中如何存储，类似java中 “类”的设计。
	4、表由列组成，我们也称为字段。所有表都是由一个或多个列组成的，每一列类似java 中的”属性”
	5、表中的数据是按行存储的，每一行类似于java中的“对象”。



##MySQL产品的介绍和安装

###MySQL服务的启动和停止
	方式一：计算机——右击管理——服务
	方式二：通过管理员身份运行
	net start 服务名（启动服务）
	net stop 服务名（停止服务）


###MySQL服务的登录和退出   
	方式一：通过mysql自带的客户端
	只限于root用户

	方式二：通过windows自带的客户端
	登录：
	mysql 【-h主机名 -P端口号 】-u用户名 -p密码

	退出：
	exit或ctrl+C	
	
	
###MySQL的常见命令 

	1.查看当前所有的数据库
	show databases;
	2.打开指定的库
	use 库名
	3.查看当前库的所有表
	show tables;
	4.查看其它库的所有表
	show tables from 库名;
	5.创建表
	create table 表名(
		列名 列类型,
		列名 列类型，
		。。。
	);
	6.查看表结构
	desc 表名;


	7.查看服务器的版本
	方式一：登录到mysql服务端
	select version();
	方式二：没有登录到mysql服务端
	mysql --version
	或
	mysql --V



###MySQL的语法规范
	1.不区分大小写,但建议关键字大写，表名、列名小写
	2.每条命令最好用分号结尾
	3.每条命令根据需要，可以进行缩进 或换行
	4.注释
		单行注释：#注释文字
		单行注释：-- 注释文字
		多行注释：/* 注释文字  */
		


###SQL的语言分类
	DQL（Data Query Language）：数据查询语言
		select 
	DML(Data Manipulate Language):数据操作语言
		insert 、update、delete
	DDL（Data Define Languge）：数据定义语言
		create、drop、alter
	TCL（Transaction Control Language）：事务控制语言
		commit、rollback
	



###SQL的常见命令

	show databases； 查看所有的数据库
	use 库名； 打开指定 的库
	show tables ; 显示库中的所有表
	show tables from 库名;显示指定库中的所有表
	create table 表名(
		字段名 字段类型,	
		字段名 字段类型
	); 创建表

	desc 表名; 查看指定表的结构
	select * from 表名;显示表中的所有数据



##DQL语言的学习
###进阶1：基础查询
		语法：
		SELECT 要查询的东西
		【FROM 表名】;

		类似于Java中 :System.out.println(要打印的东西);
		特点：
		①通过select查询完的结果 ，是一个虚拟的表格，不是真实存在
		② 要查询的东西 可以是常量值、可以是表达式、可以是字段、可以是函数
		
	一、语法
	select 查询列表
	from 表名;
	二、特点
	1、查询列表可以是字段、常量、表达式、函数，也可以是多个
	2、查询结果是一个虚拟表

	三、示例
	1、查询单个字段
	select 字段名 from 表名;
	2、查询多个字段
	select 字段名，字段名 from 表名;
	3、查询所有字段
	select * from 表名
	4、查询常量
	select 常量值;
	注意：字符型和日期型的常量值必须用单引号引起来，数值型不需要
	5、查询函数
	select 函数名(实参列表);
	6、查询表达式
	select 100/1234;
	7、起别名
	①as
	②空格
	8、去重
	select distinct 字段名 from 表名;

	9、+
	作用：做加法运算
	select 数值+数值; 直接运算
	select 字符+数值;先试图将字符转换成数值，如果转换成功，则继续运算；否则转换成0，再做运算
	select null+值;结果都为null

	10、【补充】concat函数
	功能：拼接字符
	select concat(字符1，字符2，字符3,...);

	11、【补充】ifnull函数
	功能：判断某字段或表达式是否为null，如果为null 返回指定的值，否则返回原本的值
	select ifnull(commission_pct,0) from employees;

	12、【补充】isnull函数
	功能：判断某字段或表达式是否为null，如果是，则返回1，否则返回0
	
	
	

###进阶2：条件查询
	条件查询：根据条件过滤原始表的数据，查询到想要的数据
	语法：
	select 
		要查询的字段|表达式|常量值|函数
	from 
		表
	where 
		条件 ;

	分类：
	一、条件表达式
		示例：salary>10000
		条件运算符：
		> < >= <= = != <>
	
	二、逻辑表达式
	示例：salary>10000 && salary<20000
	
	逻辑运算符：

		and（&&）:两个条件如果同时成立，结果为true，否则为false
		or(||)：两个条件只要有一个成立，结果为true，否则为false
		not(!)：如果条件成立，则not后为false，否则为true

	三、模糊查询
	示例：last_name like 'a%'
	
	
	一、语法
	select 查询列表
	from 表名
	where 筛选条件

	二、筛选条件的分类
	1、简单条件运算符
	> < = <> != >= <=  <=>安全等于
	2、逻辑运算符
	&& and
	|| or
	!  not
	3、模糊查询
	like:一般搭配通配符使用，可以判断字符型或数值型
	通配符：%任意多个字符，_任意单个字符

	between and
	in
	
	is null /is not null：用于判断null值

	is null PK <=>
				普通类型的数值	null值		可读性
	is null		×				√			√
		<=>		√				√			×
	
	
	

###进阶3：排序查询	
	
	语法：
	select
		要查询的东西
	from
		表
	where 
		条件
	
	order by 排序的字段|表达式|函数|别名 【asc|desc】
	
	一、语法
	select 查询列表
	from 表
	where 筛选条件
	order by 排序列表 【asc}desc】

	二、特点
	1、asc ：升序，如果不写默认升序
	   desc：降序

	2、排序列表 支持 单个字段、多个字段、函数、表达式、别名

	3、order by的位置一般放在查询语句的最后（除limit语句之外）

	
###进阶4：常见函数
	一、单行函数
	1、字符函数
		concat拼接
		substr截取子串
		upper转换成大写
		lower转换成小写
		trim去前后指定的空格和字符
		ltrim去左边空格
		rtrim去右边空格
		replace替换
		lpad左填充
		rpad右填充
		instr返回子串第一次出现的索引
		length 获取字节个数
		
	2、数学函数
		round 四舍五入
		rand 随机数
		floor向下取整
		ceil向上取整
		mod取余
		truncate截断
	3、日期函数
		now当前系统日期+时间
		curdate当前系统日期
		curtime当前系统时间
		str_to_date 将字符转换成日期
		date_format将日期转换成字符
	4、流程控制函数
		if 处理双分支
		case语句 处理多分支
			情况1：处理等值判断
			情况2：处理条件判断
		
	5、其他函数
		version版本
		database当前库
		user当前连接用户


	一、概述
	功能：类似于java中的方法
	好处：提高重用性和隐藏实现细节
	调用：select 函数名(实参列表);
	二、单行函数
	1、字符函数
	concat:连接
	substr:截取子串
	upper:变大写
	lower：变小写
	replace：替换
	length：获取字节长度
	trim:去前后空格
	lpad：左填充
	rpad：右填充
	instr:获取子串第一次出现的索引
	2、数学函数
	ceil:向上取整
	round：四舍五入
	mod:取模
	floor：向下取整
	truncate:截断
	rand:获取随机数，返回0-1之间的小数

	3、日期函数

	now：返回当前日期+时间
	year:返回年
	month：返回月
	day:返回日
	date_format:将日期转换成字符
	curdate:返回当前日期
	str_to_date:将字符转换成日期
	curtime：返回当前时间
	hour:小时
	minute:分钟
	second：秒
	datediff:返回两个日期相差的天数
	monthname:以英文形式返回月


	4、其他函数
	version 当前数据库服务器的版本
	database 当前打开的数据库
	user当前用户
	password('字符')：返回该字符的密码形式
	md5('字符'):返回该字符的md5加密形式



	5、流程控制函数

	①if(条件表达式，表达式1，表达式2)：如果条件表达式成立，返回表达式1，否则返回表达式2
	②case情况1
	case 变量或表达式或字段
	when 常量1 then 值1
	when 常量2 then 值2
	...
	else 值n
	end

	③case情况2
	case 
	when 条件1 then 值1
	when 条件2 then 值2
	...
	else 值n
	end

	三、分组函数
	1、分类
	max 最大值
	min 最小值
	sum 和
	avg 平均值
	count 计算个数

	2、特点

	①语法
	select max(字段) from 表名;

	②支持的类型
	sum和avg一般用于处理数值型
	max、min、count可以处理任何数据类型

	③以上分组函数都忽略null
	④都可以搭配distinct使用，实现去重的统计
	select sum(distinct 字段) from 表;
	⑤count函数
	count(字段)：统计该字段非空值的个数
	count(*):统计结果集的行数
	案例：查询每个部门的员工个数
	1 xx    10
	2 dd    20
	3 mm    20
	4 aa    40
	5 hh    40

	count(1):统计结果集的行数

	效率上：
	MyISAM存储引擎，count(*)最高
	InnoDB存储引擎，count(*)和count(1)效率>count(字段)

	⑥ 和分组函数一同查询的字段，要求是group by后出现的字段





二、分组函数


		sum 求和
		max 最大值
		min 最小值
		avg 平均值
		count 计数
	
		特点：
		1、以上五个分组函数都忽略null值，除了count(*)
		2、sum和avg一般用于处理数值型
			max、min、count可以处理任何数据类型
	    3、都可以搭配distinct使用，用于统计去重后的结果
		4、count的参数可以支持：
			字段、*、常量值，一般放1
	
		   建议使用 count(*)


##进阶5：分组查询
	语法：
	select 查询的字段，分组函数
	from 表
	group by 分组的字段
	
	
	特点：
	1、可以按单个字段分组
	2、和分组函数一同查询的字段最好是分组后的字段
	3、分组筛选
			针对的表	位置			关键字
	分组前筛选：	原始表		group by的前面		where
	分组后筛选：	分组后的结果集	group by的后面		having
	
	4、可以按多个字段分组，字段之间用逗号隔开
	5、可以支持排序
	6、having后可以支持别名
	
	
	一、语法
	select 分组函数，分组后的字段
	from 表
	【where 筛选条件】
	group by 分组的字段
	【having 分组后的筛选】
	【order by 排序列表】


不能在WHERE 子句中使用组函数。
可以在HAVING 子句中使用组函数。

	二、特点

				使用关键字		筛选的表	位置
	分组前筛选	where			原始表		group by的前面
	分组后筛选	having		分组后的结果	group by 的后面



##进阶6：多表连接查询

	笛卡尔乘积：如果连接条件省略或无效则会出现
	解决办法：添加上连接条件
	
一、传统模式下的连接 ：等值连接——非等值连接


	1.等值连接的结果 = 多个表的交集
	2.n表连接，至少需要n-1个连接条件
	3.多个表不分主次，没有顺序要求
	4.一般为表起别名，提高阅读性和性能
	
二、sql99语法：通过join关键字实现连接

	含义：1999年推出的sql语法
	支持：
	等值连接、非等值连接 （内连接）
	外连接
	交叉连接
	
	语法：
	
	select 字段，...
	from 表1
	【inner|left outer|right outer|cross】join 表2 on  连接条件
	【inner|left outer|right outer|cross】join 表3 on  连接条件
	【where 筛选条件】
	【group by 分组字段】
	【having 分组后的筛选条件】
	【order by 排序的字段或表达式】
	
	好处：语句上，连接条件和筛选条件实现了分离，简洁明了！

	
	三、自连接

	案例：查询员工名和直接上级的名称

	sql99

		SELECT e.last_name,m.last_name
		FROM employees e
		JOIN employees m ON e.`manager_id`=m.`employee_id`;

	sql92

		
		SELECT e.last_name,m.last_name
		FROM employees e,employees m 
		WHERE e.`manager_id`=m.`employee_id`;



	一、含义
	当查询中涉及到了多个表的字段，需要使用多表连接
	select 字段1，字段2
	from 表1，表2,...;

	笛卡尔乘积：当查询多个表时，没有添加有效的连接条件，导致多个表所有行实现完全连接
	如何解决：添加有效的连接条件




	二、分类

	按年代分类：
		sql92：
			等值
			非等值
			自连接

			也支持一部分外连接（用于oracle、sqlserver，mysql不支持）
		sql99【推荐使用】
			内连接
				等值
				非等值
				自连接
			外连接
				左外
				右外
				全外（mysql不支持）
			交叉连接
				




	三、SQL92语法
	1、等值连接
	语法：
		select 查询列表
		from 表1 别名,表2 别名
		where 表1.key=表2.key
		【and 筛选条件】
		【group by 分组字段】
		【having 分组后的筛选】
		【order by 排序字段】

	特点：
		① 一般为表起别名
		②多表的顺序可以调换
		③n表连接至少需要n-1个连接条件
		④等值连接的结果是多表的交集部分


	2、非等值连接
	语法：
		select 查询列表
		from 表1 别名,表2 别名
		where 非等值的连接条件
		【and 筛选条件】
		【group by 分组字段】
		【having 分组后的筛选】
		【order by 排序字段】
	3、自连接

	语法：
		select 查询列表
		from 表 别名1,表 别名2
		where 等值的连接条件
		【and 筛选条件】
		【group by 分组字段】
		【having 分组后的筛选】
		【order by 排序字段】


	四、SQL99语法
	1、内连接
	语法：
	select 查询列表
	from 表1 别名
	【inner】 join 表2 别名 on 连接条件
	where 筛选条件
	group by 分组列表
	having 分组后的筛选
	order by 排序列表
	limit 子句;

	特点：
	①表的顺序可以调换
	②内连接的结果=多表的交集
	③n表连接至少需要n-1个连接条件

	分类：
	等值连接
	非等值连接
	自连接




	2、外连接
	语法：
	select 查询列表
	from 表1 别名
	left|right|full【outer】 join 表2 别名 on 连接条件
	where 筛选条件
	group by 分组列表
	having 分组后的筛选
	order by 排序列表
	limit 子句;
	特点：
	①查询的结果=主表中所有的行，如果从表和它匹配的将显示匹配行，如果从表没有匹配的则显示null
	②left join 左边的就是主表，right join 右边的就是主表
	  full join 两边都是主表
	③一般用于查询除了交集部分的剩余的不匹配的行

	3、交叉连接

	语法：
	select 查询列表
	from 表1 别名
	cross join 表2 别名;

	特点：
	类似于笛卡尔乘积


	##进阶7：子查询

	含义：

		一条查询语句中又嵌套了另一条完整的select语句，其中被嵌套的select语句，称为子查询或内查询
		在外面的查询语句，称为主查询或外查询

	特点：

		1、子查询都放在小括号内
		2、子查询可以放在from后面、select后面、where后面、having后面，但一般放在条件的右侧
		3、子查询优先于主查询执行，主查询使用了子查询的执行结果
		4、子查询根据查询结果的行数不同分为以下两类：
		① 单行子查询
			结果集只有一行
			一般搭配单行操作符使用：> < = <> >= <= 
			非法使用子查询的情况：
			a、子查询的结果为一组值
			b、子查询的结果为空
			
		② 多行子查询
			结果集有多行
			一般搭配多行操作符使用：any、all、in、not in
			in： 属于子查询结果中的任意一个就行
			any和all往往可以用其他查询代替
			
			
			
		
	一、含义
	嵌套在其他语句内部的select语句称为子查询或内查询，
	外面的语句可以是insert、update、delete、select等，一般select作为外面语句较多
	外面如果为select语句，则此语句称为外查询或主查询

	二、分类
	1、按出现位置
	select后面：
			仅仅支持标量子查询
	from后面：
			表子查询
	where或having后面：
			标量子查询
			列子查询
			行子查询
	exists后面：
			标量子查询
			列子查询
			行子查询
			表子查询

	2、按结果集的行列
	标量子查询（单行子查询）：结果集为一行一列
	列子查询（多行子查询）：结果集为多行一列
	行子查询：结果集为多行多列
	表子查询：结果集为多行多列


	三、示例
	where或having后面
	1、标量子查询
	案例：查询最低工资的员工姓名和工资
	①最低工资
	select min(salary) from employees

	②查询员工的姓名和工资，要求工资=①
	select last_name,salary
	from employees
	where salary=(
		select min(salary) from employees
	);

	2、列子查询
	案例：查询所有是领导的员工姓名
	①查询所有员工的 manager_id
	select manager_id
	from employees

	②查询姓名，employee_id属于①列表的一个
	select last_name
	from employees
	where employee_id in(
		select manager_id
		from employees
	);










	
##进阶8：分页查询

应用场景：

	实际的web项目中需要根据用户的需求提交对应的分页查询的sql语句

语法：

	select 字段|表达式,...
	from 表
	【where 条件】
	【group by 分组字段】
	【having 条件】
	【order by 排序的字段】
	limit 【起始的条目索引，】条目数;

特点：

	1.起始条目索引从0开始
	
	2.limit子句放在查询语句的最后
	
	3.公式：select * from  表 limit （page-1）*sizePerPage,sizePerPage
	假如:
	每页显示条目数sizePerPage
	要显示的页数 page




	一、应用场景
	当要查询的条目数太多，一页显示不全
	二、语法

	select 查询列表
	from 表
	limit 【offset，】size;
	注意：
	offset代表的是起始的条目索引，默认从0卡死
	size代表的是显示的条目数

	公式：
	假如要显示的页数为page，每一页条目数为size
	select 查询列表
	from 表
	limit (page-1)*size,size;





##进阶9：联合查询

引入：
	union 联合、合并

语法：

	select 字段|常量|表达式|函数 【from 表】 【where 条件】 union 【all】
	select 字段|常量|表达式|函数 【from 表】 【where 条件】 union 【all】
	select 字段|常量|表达式|函数 【from 表】 【where 条件】 union  【all】
	.....
	select 字段|常量|表达式|函数 【from 表】 【where 条件】

特点：

	1、多条查询语句的查询的列数必须是一致的
	2、多条查询语句的查询的列的类型几乎相同
	3、union代表去重，union all代表不去重



一、含义
union：合并、联合，将多次查询结果合并成一个结果
二、语法
查询语句1
union 【all】
查询语句2
union 【all】
...

三、意义
1、将一条比较复杂的查询语句拆分成多条语句
2、适用于查询多个表的时候，查询的列基本是一致

四、特点
1、要求多条查询语句的查询列数必须一致
2、要求多条查询语句的查询的各列类型、顺序最好一致
3、union 去重，union all包含重复项



		语法：
		select 查询列表    ⑦
		from 表1 别名       ①
		连接类型 join 表2   ②
		on 连接条件         ③
		where 筛选          ④
		group by 分组列表   ⑤
		having 筛选         ⑥
		order by排序列表    ⑧
		limit 起始条目索引，条目数;  ⑨






##DML语言

###插入

语法：
	insert into 表名(字段名，...)
	values(值1，...);

特点：

	1、字段类型和值类型一致或兼容，而且一一对应
	2、可以为空的字段，可以不用插入值，或用null填充
	3、不可以为空的字段，必须插入值
	4、字段个数和值的个数必须一致
	5、字段可以省略，但默认所有字段，并且顺序和表中的存储顺序一致


	一、方式一
	语法：
	insert into 表名(字段名,...) values(值,...);
	特点：
	1、要求值的类型和字段的类型要一致或兼容
	2、字段的个数和顺序不一定与原始表中的字段个数和顺序一致
	但必须保证值和字段一一对应
	3、假如表中有可以为null的字段，注意可以通过以下两种方式插入null值
	①字段和值都省略
	②字段写上，值使用null
	4、字段和值的个数必须一致
	5、字段名可以省略，默认所有列

	二、方式二
	语法：
	insert into 表名 set 字段=值,字段=值,...;


	两种方式 的区别：
	1.方式一支持一次插入多行，语法如下：
	insert into 表名【(字段名,..)】 values(值，..),(值，...),...;
	2.方式一支持子查询，语法如下：
	insert into 表名
	查询语句;







###修改

修改单表语法：

	update 表名 set 字段=新值,字段=新值
	【where 条件】
修改多表语法：

	update 表1 别名1,表2 别名2
	set 字段=新值，字段=新值
	where 连接条件
	and 筛选条件

	一、修改单表的记录 ★
	语法：update 表名 set 字段=值,字段=值 【where 筛选条件】;

	二、修改多表的记录【补充】
	语法：
	update 表1 别名 
	left|right|inner join 表2 别名 
	on 连接条件  
	set 字段=值,字段=值 
	【where 筛选条件】;

###删除

方式1：delete语句 

单表的删除： ★
	delete from 表名 【where 筛选条件】

多表的删除：
	delete 别名1，别名2
	from 表1 别名1，表2 别名2
	where 连接条件
	and 筛选条件;


方式2：truncate语句

	truncate table 表名


两种方式的区别【面试题】
	
	#1.truncate不能加where条件，而delete可以加where条件
	
	#2.truncate的效率高一丢丢
	
	#3.truncate 删除带自增长的列的表后，如果再插入数据，数据从1开始
	#delete 删除带自增长列的表后，如果再插入数据，数据从上一次的断点处开始
	
	#4.truncate删除不能回滚，delete删除可以回滚


		方式一：使用delete
	一、删除单表的记录★
	语法：delete from 表名 【where 筛选条件】【limit 条目数】
	二、级联删除[补充]
	语法：
	delete 别名1,别名2 from 表1 别名 
	inner|left|right join 表2 别名 
	on 连接条件
	 【where 筛选条件】

	方式二：使用truncate
	语法：truncate table 表名

	两种方式的区别【面试题】★

	1.truncate删除后，如果再插入，标识列从1开始
	  delete删除后，如果再插入，标识列从断点开始
	2.delete可以添加筛选条件
	 truncate不可以添加筛选条件
	3.truncate效率较高
	4.truncate没有返回值
	delete可以返回受影响的行数
	5.truncate不可以回滚
	delete可以回滚


















##DDL语句
### 库和表的管理
库的管理：

	一、创建库
	create database 库名
	二、删除库
	drop database 库名

	一、创建库
	create database 【if not exists】 库名【 character set 字符集名】;

	二、修改库
	alter database 库名 character set 字符集名;
	三、删除库
	drop database 【if exists】 库名;









表的管理：
	#1.创建表
	
	CREATE TABLE IF NOT EXISTS stuinfo(
		stuId INT,
		stuName VARCHAR(20),
		gender CHAR,
		bornDate DATETIME
		
	
	);

	DESC studentinfo;
	#2.修改表 alter
	语法：ALTER TABLE 表名 ADD|MODIFY|DROP|CHANGE COLUMN 字段名 【字段类型】;
	
	#①修改字段名
	ALTER TABLE studentinfo CHANGE  COLUMN sex gender CHAR;
	
	#②修改表名
	ALTER TABLE stuinfo RENAME [TO]  studentinfo;
	#③修改字段类型和列级约束
	ALTER TABLE studentinfo MODIFY COLUMN borndate DATE ;
	
	#④添加字段
	
	ALTER TABLE studentinfo ADD COLUMN email VARCHAR(20) first;
	#⑤删除字段
	ALTER TABLE studentinfo DROP COLUMN email;
	
	
	#3.删除表
	
	DROP TABLE [IF EXISTS] studentinfo;

	
	
		一、创建表 ★
	create table 【if not exists】 表名(
		字段名 字段类型 【约束】,
		字段名 字段类型 【约束】,
		。。。
		字段名 字段类型 【约束】 

	)

	二、修改表

	1.添加列
	alter table 表名 add column 列名 类型 【first|after 字段名】;
	2.修改列的类型或约束
	alter table 表名 modify column 列名 新类型 【新约束】;
	3.修改列名
	alter table 表名 change column 旧列名 新列名 类型;
	4 .删除列
	alter table 表名 drop column 列名;
	5.修改表名
	alter table 表名 rename 【to】 新表名;

	三、删除表
	drop table【if exists】 表名;

	四、复制表
	1、复制表的结构
	create table 表名 like 旧表;
	2、复制表的结构+数据
	create table 表名 
	select 查询列表 from 旧表【where 筛选】;







	
	

###常见类型

	整型：
		
	小数：
		浮点型
		定点型
	字符型：
	日期型：
	Blob类型：

一、数值型
1、整型
tinyint、smallint、mediumint、int/integer、bigint
1         2        3          4            8

特点：
①都可以设置无符号和有符号，默认有符号，通过unsigned设置无符号
②如果超出了范围，会报out or range异常，插入临界值
③长度可以不指定，默认会有一个长度
长度代表显示的最大宽度，如果不够则左边用0填充，但需要搭配zerofill，并且默认变为无符号整型


2、浮点型
定点数：decimal(M,D)
浮点数:
	float(M,D)   4
	double(M,D)  8

特点：
①M代表整数部位+小数部位的个数，D代表小数部位
②如果超出范围，则报out or range异常，并且插入临界值
③M和D都可以省略，但对于定点数，M默认为10，D默认为0
④如果精度要求较高，则优先考虑使用定点数

二、字符型
char、varchar、binary、varbinary、enum、set、text、blob

char：固定长度的字符，写法为char(M)，最大长度不能超过M，其中M可以省略，默认为1
varchar：可变长度的字符，写法为varchar(M)，最大长度不能超过M，其中M不可以省略

三、日期型
year年
date日期
time时间
datetime 日期+时间          8      
timestamp 日期+时间         4   比较容易受时区、语法模式、版本的影响，更能反映当前时区的真实时间
















###常见约束

	NOT NULL
	DEFAULT
	UNIQUE
	CHECK
	PRIMARY KEY
	FOREIGN KEY

一、常见的约束
NOT NULL：非空，该字段的值必填
UNIQUE：唯一，该字段的值不可重复
DEFAULT：默认，该字段的值不用手动插入有默认值
CHECK：检查，mysql不支持
PRIMARY KEY：主键，该字段的值不可重复并且非空  unique+not null
FOREIGN KEY：外键，该字段的值引用了另外的表的字段

主键和唯一
1、区别：
①、一个表至多有一个主键，但可以有多个唯一
②、主键不允许为空，唯一可以为空
2、相同点
都具有唯一性
都支持组合键，但不推荐
外键：
1、用于限制两个表的关系，从表的字段值引用了主表的某字段值
2、外键列和主表的被引用列要求类型一致，意义一样，名称无要求
3、主表的被引用列要求是一个key（一般就是主键）
4、插入数据，先插入主表
删除数据，先删除从表
可以通过以下两种方式来删除主表的记录
#方式一：级联删除
ALTER TABLE stuinfo ADD CONSTRAINT fk_stu_major FOREIGN KEY(majorid) REFERENCES major(id) ON DELETE CASCADE;

#方式二：级联置空
ALTER TABLE stuinfo ADD CONSTRAINT fk_stu_major FOREIGN KEY(majorid) REFERENCES major(id) ON DELETE SET NULL;

二、创建表时添加约束
create table 表名(
	字段名 字段类型 not null,#非空
	字段名 字段类型 primary key,#主键
	字段名 字段类型 unique,#唯一
	字段名 字段类型 default 值,#默认
	constraint 约束名 foreign key(字段名) references 主表（被引用列）

)
注意：
			支持类型		可以起约束名			
列级约束		除了外键		不可以
表级约束		除了非空和默认	可以，但对主键无效

列级约束可以在一个字段上追加多个，中间用空格隔开，没有顺序要求

三、修改表时添加或删除约束
1、非空
添加非空
alter table 表名 modify column 字段名 字段类型 not null;
删除非空
alter table 表名 modify column 字段名 字段类型 ;

2、默认
添加默认
alter table 表名 modify column 字段名 字段类型 default 值;
删除默认
alter table 表名 modify column 字段名 字段类型 ;
3、主键
添加主键
alter table 表名 add【 constraint 约束名】 primary key(字段名);
删除主键
alter table 表名 drop primary key;

4、唯一
添加唯一
alter table 表名 add【 constraint 约束名】 unique(字段名);
删除唯一
alter table 表名 drop index 索引名;
5、外键
添加外键
alter table 表名 add【 constraint 约束名】 foreign key(字段名) references 主表（被引用列）;
删除外键
alter table 表名 drop foreign key 约束名;


四、自增长列
特点：
1、不用手动插入值，可以自动提供序列值，默认从1开始，步长为1
auto_increment_increment
如果要更改起始值：手动插入值
如果要更改步长：更改系统变量
set auto_increment_increment=值;
2、一个表至多有一个自增长列
3、自增长列只能支持数值型
4、自增长列必须为一个key

一、创建表时设置自增长列
create table 表(
	字段名 字段类型 约束 auto_increment
)
二、修改表时设置自增长列
alter table 表 modify column 字段名 字段类型 约束 auto_increment
三、删除自增长列
alter table 表 modify column 字段名 字段类型 约束 












##数据库事务
###含义
	通过一组逻辑操作单元（一组DML——sql语句），将数据从一种状态切换到另外一种状态

###特点
	（ACID）
	原子性：要么都执行，要么都回滚
	一致性：保证数据的状态操作前和操作后保持一致
	隔离性：多个事务同时操作相同数据库的同一个数据时，一个事务的执行不受另外一个事务的干扰
	持久性：一个事务一旦提交，则数据将持久化到本地，除非其他事务对其进行修改

相关步骤：

	1、开启事务
	2、编写事务的一组逻辑操作单元（多条sql语句）
	3、提交事务或回滚事务

###事务的分类：

隐式事务，没有明显的开启和结束事务的标志

	比如
	insert、update、delete语句本身就是一个事务


显式事务，具有明显的开启和结束事务的标志

		1、开启事务
		取消自动提交事务的功能
		
		2、编写事务的一组逻辑操作单元（多条sql语句）
		insert
		update
		delete
		
		3、提交事务或回滚事务
###使用到的关键字

	set autocommit=0;
	start transaction;
	commit;
	rollback;
	
	savepoint  断点
	commit to 断点
	rollback to 断点


###事务的隔离级别:

事务并发问题如何发生？

	当多个事务同时操作同一个数据库的相同数据时
事务的并发问题有哪些？

	脏读：一个事务读取到了另外一个事务未提交的数据
	不可重复读：同一个事务中，多次读取到的数据不一致
	幻读：一个事务读取数据时，另外一个事务进行更新，导致第一个事务读取到了没有更新的数据
	
如何避免事务的并发问题？

	通过设置事务的隔离级别
	1、READ UNCOMMITTED
	2、READ COMMITTED 可以避免脏读
	3、REPEATABLE READ 可以避免脏读、不可重复读和一部分幻读
	4、SERIALIZABLE可以避免脏读、不可重复读和幻读
	
设置隔离级别：

	set session|global  transaction isolation level 隔离级别名;
查看隔离级别：

	select @@tx_isolation;
	



一、含义
事务：一条或多条sql语句组成一个执行单位，一组sql语句要么都执行要么都不执行
二、特点（ACID）
A 原子性：一个事务是不可再分割的整体，要么都执行要么都不执行
C 一致性：一个事务可以使数据从一个一致状态切换到另外一个一致的状态
I 隔离性：一个事务不受其他事务的干扰，多个事务互相隔离的
D 持久性：一个事务一旦提交了，则永久的持久化到本地

三、事务的使用步骤 ★
了解：
隐式（自动）事务：没有明显的开启和结束，本身就是一条事务可以自动提交，比如insert、update、delete
显式事务：具有明显的开启和结束

使用显式事务：
①开启事务
set autocommit=0;
start transaction;#可以省略

②编写一组逻辑sql语句
注意：sql语句支持的是insert、update、delete

设置回滚点：
savepoint 回滚点名;

③结束事务
提交：commit;
回滚：rollback;
回滚到指定的地方：rollback to 回滚点名;
四、并发事务
1、事务的并发问题是如何发生的？
多个事务 同时 操作 同一个数据库的相同数据时
2、并发问题都有哪些？
脏读：一个事务读取了其他事务还没有提交的数据，读到的是其他事务“更新”的数据
不可重复读：一个事务多次读取，结果不一样
幻读：一个事务读取了其他事务还没有提交的数据，只是读到的是 其他事务“插入”的数据
3、如何解决并发问题
通过设置隔离级别来解决并发问题
4、隔离级别
							 脏读		不可重复读		幻读
read uncommitted:读未提交     ×                ×              ×        
read committed：读已提交      √                ×              ×
repeatable read：可重复读     √                √              ×
serializable：串行化          √                √              √

















##视图
含义：理解成一张虚拟的表

视图和表的区别：
	
		使用方式	占用物理空间
	
	视图	完全相同	不占用，仅仅保存的是sql逻辑
	
	表	完全相同	占用

视图的好处：


	1、sql语句提高重用性，效率高
	2、和表实现了分离，提高了安全性

###视图的创建
	语法：
	CREATE VIEW  视图名
	AS
	查询语句;
###视图的增删改查
	1、查看视图的数据 ★
	
	SELECT * FROM my_v4;
	SELECT * FROM my_v1 WHERE last_name='Partners';
	
	2、插入视图的数据
	INSERT INTO my_v4(last_name,department_id) VALUES('虚竹',90);
	
	3、修改视图的数据
	
	UPDATE my_v4 SET last_name ='梦姑' WHERE last_name='虚竹';
	
	
	4、删除视图的数据
	DELETE FROM my_v4;
###某些视图不能更新
	包含以下关键字的sql语句：分组函数、distinct、group  by、having、union或者union all
	常量视图
	Select中包含子查询
	join
	from一个不能更新的视图
	where子句的子查询引用了from子句中的表
###视图逻辑的更新
	#方式一：
	CREATE OR REPLACE VIEW test_v7
	AS
	SELECT last_name FROM employees
	WHERE employee_id>100;
	
	#方式二:
	ALTER VIEW test_v7
	AS
	SELECT employee_id FROM employees;
	
	SELECT * FROM test_v7;
###视图的删除
	DROP VIEW test_v1,test_v2,test_v3;
###视图结构的查看	
	DESC test_v7;
	SHOW CREATE VIEW test_v7;
	
	
	
	
	一、含义
mysql5.1版本出现的新特性，本身是一个虚拟表，它的数据来自于表，通过执行时动态生成。
好处：
1、简化sql语句
2、提高了sql的重用性
3、保护基表的数据，提高了安全性
二、创建
create view 视图名
as
查询语句;


三、修改
方式一：
create or replace view 视图名
as
查询语句;
方式二：
alter view 视图名
as
查询语句

四、删除
drop view 视图1，视图2,...;
五、查看
desc 视图名;
show create view 视图名;
六、使用
1.插入
insert
2.修改
update
3.删除
delete
4.查看
select
注意：视图一般用于查询的，而不是更新的，所以具备以下特点的视图都不允许更新
①包含分组函数、group by、distinct、having、union、
②join
③常量视图
④where后的子查询用到了from中的表
⑤用到了不可更新的视图


七、视图和表的对比
		关键字		是否占用物理空间			使用
视图	view		占用较小，只保存sql逻辑		一般用于查询
表		table		保存实际的数据			增删改查




分类
一、系统变量
说明：变量由系统提供的，不用自定义
语法：
①查看系统变量
show 【global|session 】variables like ''; 如果没有显式声明global还是session，则默认是session
②查看指定的系统变量的值
select @@【global|session】.变量名; 如果没有显式声明global还是session，则默认是session
③为系统变量赋值
方式一：
set 【global|session 】 变量名=值; 如果没有显式声明global还是session，则默认是session
方式二：
set @@global.变量名=值;
set @@变量名=值；




1、全局变量
服务器层面上的，必须拥有super权限才能为系统变量赋值，作用域为整个服务器，也就是针对于所有连接（会话）有效

2、会话变量
服务器为每一个连接的客户端都提供了系统变量，作用域为当前的连接（会话）




二、自定义变量
说明：
1、用户变量
作用域：针对于当前连接（会话）生效
位置：begin end里面，也可以放在外面
使用：

①声明并赋值：
set @变量名=值;或
set @变量名:=值;或
select @变量名:=值;

②更新值
方式一：
	set @变量名=值;或
	set @变量名:=值;或
	select @变量名:=值;
方式二：
	select xx into @变量名 from 表;

③使用
select @变量名;



2、局部变量
作用域：仅仅在定义它的begin end中有效
位置：只能放在begin end中，而且只能放在第一句
使用：
①声明
declare 变量名 类型 【default 值】;
②赋值或更新
方式一：
	set 变量名=值;或
	set 变量名:=值;或
	select @变量名:=值;
方式二：
	select xx into 变量名 from 表;
③使用
select 变量名;



















	
	
	
	
	
	
	
	

##存储过程

含义：一组经过预先编译的sql语句的集合
好处：

	1、提高了sql语句的重用性，减少了开发程序员的压力
	2、提高了效率
	3、减少了传输次数

分类：

	1、无返回无参
	2、仅仅带in类型，无返回有参
	3、仅仅带out类型，有返回无参
	4、既带in又带out，有返回有参
	5、带inout，有返回有参
	注意：in、out、inout都可以在一个存储过程中带多个
###创建存储过程
语法：

	create procedure 存储过程名(in|out|inout 参数名  参数类型,...)
	begin
		存储过程体

	end

类似于方法：

	修饰符 返回类型 方法名(参数类型 参数名,...){

		方法体;
	}

注意

	1、需要设置新的结束标记
	delimiter 新的结束标记
	示例：
	delimiter $

	CREATE PROCEDURE 存储过程名(IN|OUT|INOUT 参数名  参数类型,...)
	BEGIN
		sql语句1;
		sql语句2;

	END $

	2、存储过程体中可以有多条sql语句，如果仅仅一条sql语句，则可以省略begin end

	3、参数前面的符号的意思
	in:该参数只能作为输入 （该参数不能做返回值）
	out：该参数只能作为输出（该参数只能做返回值）
	inout：既能做输入又能做输出


#调用存储过程
	call 存储过程名(实参列表)
	
	
	
	
	一、创建 ★
create procedure 存储过程名(参数模式 参数名 参数类型)
begin
		存储过程体
end
注意：
1.参数模式：in、out、inout，其中in可以省略
2.存储过程体的每一条sql语句都需要用分号结尾

二、调用
call 存储过程名(实参列表)
举例：
调用in模式的参数：call sp1（‘值’）;
调用out模式的参数：set @name; call sp1(@name);select @name;
调用inout模式的参数：set @name=值; call sp1(@name); select @name;
三、查看
show create procedure 存储过程名;
四、删除
drop procedure 存储过程名;

	
	
	
	
	
	
##函数
一、创建
create function 函数名(参数名 参数类型) returns  返回类型
begin
	函数体
end

注意：函数体中肯定需要有return语句
二、调用
select 函数名(实参列表);
三、查看
show create function 函数名;
四、删除
drop function 函数名；

###创建函数

学过的函数：LENGTH、SUBSTR、CONCAT等
语法：

	CREATE FUNCTION 函数名(参数名 参数类型,...) RETURNS 返回类型
	BEGIN
		函数体
	
	END

###调用函数
	SELECT 函数名（实参列表）





###函数和存储过程的区别

			关键字		调用语法	返回值			应用场景
	函数		FUNCTION	SELECT 函数()	只能是一个		一般用于查询结果为一个值并返回时，当有返回值而且仅仅一个
	存储过程	PROCEDURE	CALL 存储过程()	可以有0个或多个		一般用于更新


##流程控制结构

###系统变量
一、全局变量

作用域：针对于所有会话（连接）有效，但不能跨重启

	查看所有全局变量
	SHOW GLOBAL VARIABLES;
	查看满足条件的部分系统变量
	SHOW GLOBAL VARIABLES LIKE '%char%';
	查看指定的系统变量的值
	SELECT @@global.autocommit;
	为某个系统变量赋值
	SET @@global.autocommit=0;
	SET GLOBAL autocommit=0;

二、会话变量

作用域：针对于当前会话（连接）有效

	查看所有会话变量
	SHOW SESSION VARIABLES;
	查看满足条件的部分会话变量
	SHOW SESSION VARIABLES LIKE '%char%';
	查看指定的会话变量的值
	SELECT @@autocommit;
	SELECT @@session.tx_isolation;
	为某个会话变量赋值
	SET @@session.tx_isolation='read-uncommitted';
	SET SESSION tx_isolation='read-committed';

###自定义变量
一、用户变量

声明并初始化：

	SET @变量名=值;
	SET @变量名:=值;
	SELECT @变量名:=值;
赋值：

	方式一：一般用于赋简单的值
	SET 变量名=值;
	SET 变量名:=值;
	SELECT 变量名:=值;


	方式二：一般用于赋表 中的字段值
	SELECT 字段名或表达式 INTO 变量
	FROM 表;

使用：

	select @变量名;

二、局部变量

声明：

	declare 变量名 类型 【default 值】;
赋值：

	方式一：一般用于赋简单的值
	SET 变量名=值;
	SET 变量名:=值;
	SELECT 变量名:=值;


	方式二：一般用于赋表 中的字段值
	SELECT 字段名或表达式 INTO 变量
	FROM 表;

使用：

	select 变量名



二者的区别：

			作用域			定义位置		语法
用户变量	当前会话		会话的任何地方		加@符号，不用指定类型
局部变量	定义它的BEGIN END中 	BEGIN END的第一句话	一般不用加@,需要指定类型

###分支
一、if函数
	语法：if(条件，值1，值2)
	特点：可以用在任何位置

二、case语句

语法：

	情况一：类似于switch
	case 表达式
	when 值1 then 结果1或语句1(如果是语句，需要加分号) 
	when 值2 then 结果2或语句2(如果是语句，需要加分号)
	...
	else 结果n或语句n(如果是语句，需要加分号)
	end 【case】（如果是放在begin end中需要加上case，如果放在select后面不需要）

	情况二：类似于多重if
	case 
	when 条件1 then 结果1或语句1(如果是语句，需要加分号) 
	when 条件2 then 结果2或语句2(如果是语句，需要加分号)
	...
	else 结果n或语句n(如果是语句，需要加分号)
	end 【case】（如果是放在begin end中需要加上case，如果放在select后面不需要）


特点：
	可以用在任何位置

三、if elseif语句

语法：

	if 情况1 then 语句1;
	elseif 情况2 then 语句2;
	...
	else 语句n;
	end if;

特点：
	只能用在begin end中！！！！！！！！！！！！！！！


三者比较：
			应用场合
	if函数		简单双分支
	case结构	等值判断 的多分支
	if结构		区间判断 的多分支


###循环

语法：


	【标签：】WHILE 循环条件  DO
		循环体
	END WHILE 【标签】;
	
特点：

	只能放在BEGIN END里面

	如果要搭配leave跳转语句，需要使用标签，否则可以不用标签

	leave类似于java中的break语句，跳出所在循环！！！
	


特点：
1、if函数
功能：实现简单双分支
语法：
if(条件，值1，值2)
位置：
可以作为表达式放在任何位置
2、case结构
功能：实现多分支
语法1：
case 表达式或字段
when 值1 then 语句1;
when 值2 then 语句2；
..
else 语句n;
end [case];

语法2：
case 
when 条件1 then 语句1;
when 条件2 then 语句2；
..
else 语句n;
end [case];


位置：
可以放在任何位置，
如果放在begin end 外面，作为表达式结合着其他语句使用
如果放在begin end 里面，一般作为独立的语句使用
3、if结构
功能：实现多分支
语法：
if 条件1 then 语句1;
elseif 条件2 then 语句2;
...
else 语句n;
end if;
位置：
只能放在begin end中



位置：
只能放在begin end中

特点：都能实现循环结构

对比：

①这三种循环都可以省略名称，但如果循环中添加了循环控制语句（leave或iterate）则必须添加名称
②
loop 一般用于实现简单的死循环
while 先判断后执行
repeat 先执行后判断，无条件至少执行一次


1、while
语法：
【名称:】while 循环条件 do
		循环体
end while 【名称】;
2、loop
语法：
【名称：】loop
		循环体
end loop 【名称】;

3、repeat
语法：
【名称:】repeat
		循环体
until 结束条件 
end repeat 【名称】;

二、循环控制语句
leave：类似于break，用于跳出所在的循环
iterate：类似于continue，用于结束本次循环，继续下一次










































