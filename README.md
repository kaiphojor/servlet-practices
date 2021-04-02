# 모듈 설명

* /mysite02  - JSP/servlet Model 2 site + MySQL
* /dbreplication  - site + MySQL (DB Replication)
* /mongodbsite - site + MongoDB (동일 기능 수행)
* /apitest - XML + JSON ajax request



# JSP servlet Model 2 MVC site (/mysite02)

* 게시판 사이트
* 210313 ~ 210318
* 기능
	* 회원
	  * 등록, 수정, 로그인 / 로그아웃
	* 방명록
		  * 글쓰기, 전체 목록보기, 삭제 
	* 게시판
	  * 쓰기, 읽기, 수정, 삭제
	  * 페이징, 조회수 증가, 답글 달기
	  * 키워드 검색(제목, 본문, 닉네임)
* keyword
  * JSP, Servlet, MySQL, JDBC, MVC, JSTL, EL, Git
* version
  * DB : MySQL 8.0.23 (JDBC 연동)
  * WAS : Tomcat 8.5.63
* Architecture 

![architecture](./model2mvc.png)





## DB 생성 SQL

DB 초기 설정

```
set names = utf8mb4;
create database webdb;
use webdb;
```



user 부터 순서대로 테이블을 생성 한다.

* user ( 사용자 )

```
-- 회원
CREATE TABLE `user` (
`no`        INT UNSIGNED           NOT NULL COMMENT '번호',
`name`      VARCHAR(50)            NOT NULL COMMENT '이름',
`email`     VARCHAR(200)           NOT NULL COMMENT '이메일',
`password`  VARCHAR(20)            NOT NULL COMMENT '비밀번호',
`gender`    ENUM('male','female') NOT NULL COMMENT '성별',
`join_date` DATETIME               NOT NULL COMMENT '가입일'
);

ALTER TABLE `user` ADD CONSTRAINT `PK_user` PRIMARY KEY (`no` );
ALTER TABLE `user` MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';


-- 인덱스 추가
CREATE INDEX idx_user ON user(no);

```

* guestbook ( 방명록 )

```
-- 방명록
CREATE TABLE `guestbook` (
`no`       INT UNSIGNED NOT NULL COMMENT '번호',
`name`     VARCHAR(50)  NOT NULL COMMENT '이름',
`password` VARCHAR(20)  NOT NULL COMMENT '비밀번호',
`contents` TEXT         NOT NULL COMMENT '내용',
`reg_date` DATETIME     NOT NULL COMMENT '등록일'
);

ALTER TABLE `guestbook` ADD CONSTRAINT `PK_guestbook` PRIMARY KEY ( `no` );

ALTER TABLE `guestbook` MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '번호';
	
-- 인덱스 추가
CREATE INDEX idx_guestbook ON guestbook(no);
```

* board ( 게시판)

```
-- 게시글
CREATE TABLE `board` (
`no`       INT UNSIGNED NOT NULL COMMENT '글번호',
`user_no`  INT UNSIGNED NOT NULL COMMENT '회원번호',
`title`    VARCHAR(50)  NOT NULL COMMENT '타이틀',
`group_no` INT UNSIGNED NOT NULL COMMENT '그룹번호',
`order_no` INT UNSIGNED NOT NULL COMMENT '그룹내 순서',
`depth`    INT UNSIGNED NOT NULL COMMENT '글의 깊이',
`contents` TEXT         NOT NULL COMMENT '내용',
`reg_date` DATETIME     NOT NULL COMMENT '등록일',
`views`    INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '조회수'
);


ALTER TABLE `board` ADD CONSTRAINT `PK_board` PRIMARY KEY ( `no` );
ALTER TABLE `board` MODIFY COLUMN `no` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '글번호';
ALTER TABLE `board` ADD CONSTRAINT `FK_user_TO_board` FOREIGN KEY ( `user_no` ) REFERENCES `user` ( `no` ) ON DELETE NO ACTION;

-- 인덱스 추가
CREATE INDEX idx_board ON board(no);

```



# DB 이중화(/dbreplication)

* 결과 LINK - https://enchiridion.tistory.com/63

![db_replication_architecture](./db_replication.png)

* 상세 - DB 이중화
  * docker container 사용해서 mysql master db, slave db를 생성, 이중화 
  * master 에서는 insert, update, delete 수행
  * slave에서는 select만  수행

* 참고
  * https://jupiny.com/2017/11/07/docker-mysql-replicaiton/


# mongo DB로 교체 (/mongodbsite)

* DAO file 만 교체 
* MySQL query -> MongoDB query



## 변경사항

* Mongodb java driver
* slf4j ( option )

mysite02 모듈 복사 후 모듈 내 pom.xml 에 dependency 추가

## 초기설정

* src/test/java 내 InitMongoDB 실행
* mongodb -> webdb database ->  bcounter collection -> document -> seq 속성 int64 변경 





