# --- Sample dataset

# --- !Ups
insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (  2,'token','teste','teste','Teste','ACTIVE','FEMALE',null,'teste@livreto.com.br');

insert into material (id,author_id,title,price_policy,price,created,modified_at,material_file) values (  9,2,'Simulado ENEM 2014','F','120','2011-09-01 00:00:00.00','2011-09-01 00:00:00.00',null);
# --- !Downs

delete from user;
delete from material;
