-- select 
select * from board 
	order by group_no DESC, order_no ASC;

-- insert
-- 새글
insert into board(user_no, title, group_no, order_no, depth, contents, reg_date)
	values(?,?,ifnull((select max(group_no)+1 from board b),1),1,0,?,now());

-- 답글
-- 선 수정(원글의 order_no, group_no 필요)
update board
	set order_no = order_no + 1 
    where group_no = ? and order_no >= ? + 1;    
-- 답글 삽입 (원 글의 group_no, order_no, depth 필요하다.) 
insert into board(user_no, title, group_no, order_no, depth, contents, reg_date)
	values(?, ?, ?, ?+1,?+1,?,now());

-- 조회
select no, user_no, title, group_no, order_no, depth, contents,
date_format(reg_date,'%Y-%m-%d %H:%i:%s'), views
	from board
	order by group_no DESC, order_no ASC;
-- 삭제 
delete from board
	where no = ?;

-- 답글 존재하는 게시판 글의 삭제(답글이 달린 게시글은 삭제 불가)     
-- 먼저 가져오고 나서 시작

delete from board where no = ? and order_no = (select max(order_no) from (select * from board where group_no = ?) b) ;

    
-- 수정 (조회수 증가)
update board 
	set views = views + 1
    where no = ?;


-- 게시판 보기
select b.no, b.user_no, b.title, b.group_no, b.order_no, b.depth, b.contents,date_format(b.reg_date,'%Y-%m-%d %H:%i:%s'), views , u.name
	from board b 
    join user u on b.user_no = u.no 
    order by group_no DESC, order_no ASC;
