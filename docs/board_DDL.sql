-- 게시글
CREATE TABLE `board` (
	`no`       INT UNSIGNED NOT NULL COMMENT '글번호', -- 글번호
	`user_no`  INT UNSIGNED NOT NULL COMMENT '회원번호', -- 회원번호
	`title`    VARCHAR(50)  NOT NULL COMMENT '타이틀', -- 타이틀
	`group_no` INT UNSIGNED NOT NULL COMMENT '그룹번호', -- 그룹번호
	`order_no` INT UNSIGNED NOT NULL COMMENT '그룹내 순서', -- 그룹내 순서
	`depth`    INT UNSIGNED NOT NULL COMMENT '글의 깊이', -- 글의 깊이
	`contents` TEXT         NOT NULL COMMENT '내용', -- 내용
	`reg_date` DATETIME     NOT NULL COMMENT '등록일', -- 등록일
	`views`    INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '조회수' -- 조회수
)
COMMENT '게시글';

-- 게시글
ALTER TABLE `board`
	ADD CONSTRAINT `PK_board` -- 게시글 기본키
		PRIMARY KEY (
			`no` -- 글번호
		);

ALTER TABLE `board`
	MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '글번호';

-- 게시글
ALTER TABLE `board`
	ADD CONSTRAINT `FK_user_TO_board` -- 회원 -> 게시글
		FOREIGN KEY (
			`user_no` -- 회원번호
		)
		REFERENCES `user` ( -- 회원
			`no` -- 번호
		)
		ON DELETE NO ACTION;