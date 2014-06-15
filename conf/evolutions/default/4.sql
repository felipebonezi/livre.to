# --- Sample dataset

# --- !Ups

insert into user_has_material (user_id, material_id) values (2,1);
insert into user_has_material (user_id, material_id) values (2,2);
insert into user_has_material (user_id, material_id) values (2,3);

insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (nextVal('user_id_seq'),'token','teste1','teste','Teste','ACTIVE','FEMALE',now(),'teste1@livreto.com.br');
insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (nextVal('user_id_seq'),'token','teste2','teste','Teste','ACTIVE','FEMALE',now(),'teste2@livreto.com.br');
insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (nextVal('user_id_seq'),'token','teste3','teste','Teste','ACTIVE','FEMALE',now(),'teste3@livreto.com.br');
insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (nextVal('user_id_seq'),'token','teste4','teste','Teste','ACTIVE','FEMALE',now(),'teste4@livreto.com.br');
insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (nextVal('user_id_seq'),'token','teste5','teste','Teste','ACTIVE','FEMALE',now(),'teste5@livreto.com.br');
insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (nextVal('user_id_seq'),'token','teste6','teste','Teste','ACTIVE','FEMALE',now(),'teste6@livreto.com.br');
insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (nextVal('user_id_seq'),'token','teste7','teste','Teste','ACTIVE','FEMALE',now(),'teste7@livreto.com.br');
insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (nextVal('user_id_seq'),'token','teste8','teste','Teste','ACTIVE','FEMALE',now(),'teste8@livreto.com.br');
insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (nextVal('user_id_seq'),'token','teste9','teste','Teste','ACTIVE','FEMALE',now(),'teste9@livreto.com.br');

insert into user_has_material (user_id, material_id, when) values (3,1, '2014-01-01 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (3,2,'2014-01-01 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (3,3,'2014-01-01 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (4,1,'2014-02-02 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (4,2,'2014-02-02 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (4,3,'2014-02-02 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (5,1,'2014-03-03 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (5,2,'2014-03-03 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (5,3,'2014-03-03 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (6,1,'2014-04-04 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (6,2,'2014-04-04 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (6,3,'2014-04-04 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (7,1,'2014-05-05 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (7,2,'2014-05-05 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (7,3,'2014-05-05 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (8,1,'2014-06-06 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (8,2,'2014-06-06 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (8,3,'2014-06-06 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (9,1,'2014-07-07 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (9,2,'2014-07-07 00:00:00.00');
insert into user_has_material (user_id, material_id, when) values (9,3,'2014-07-07 00:00:00.00');
# --- !Downs

delete from user_has_material;
