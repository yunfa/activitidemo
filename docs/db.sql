create table tb_user(
	id int(20) not null AUTO_INCREMENT comment '主键ID程序自增',
	name varchar(60) default '' comment '用户名称',
	pwd varchar(60) default '' comment '用户密码',
	primary key(id)
);
insert into tb_user(name,pwd) values('11','11pwd'),('22','22pwd');