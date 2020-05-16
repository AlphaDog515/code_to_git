

/*
	关联查询:
	两个表关联（外连接）： 左边的表叫作驱动表，右边的表叫作被驱动表。
			一般索引都是加在被驱动表上。
*/
EXPLAIN SELECT * FROM class LEFT JOIN book ON class.card = book.card;
/*
	创建索引：
		思考 ：给class还是book的字段加索引？
*/
CALL proc_drop_index('mydb','book');
CREATE INDEX index_card ON class(card);
CREATE INDEX index_card ON book(card);


/*
  内连接 :
	思考 ：被驱动是谁？
	特殊 ：两张表的数量有多有少？
*/
DELETE FROM book WHERE bookid > 10;
DELETE FROM book;
EXPLAIN SELECT SQL_NO_CACHE * FROM book INNER JOIN class ON class.card = book.card;




