# --- Sample dataset

# --- !Ups
insert into user (id,access_token,login,password,name,status,gender,modified_at,mail) values (nextVal('user_id_seq'),'accesstoken1','admin','admin','Administrador','ACTIVE','MALE',now(),'admin@livreto.com.br');
insert into material (id,author_id,title,price_policy,price,created,modified_at,material_file) values (nextVal('material_seq'),1,'Eletrônica Digital - Tradução da 5ª Ed. Norte','M','10','2011-09-01 00:00:00.00','2011-09-01 00:00:00.00',null);
insert into material (id,author_id,title,price_policy,price,created,modified_at,material_file) values (nextVal('material_seq'),1,'Eletrônica de Potência - Conservadores de Energia','F','0','2011-09-01 00:00:00.00','2011-09-01 00:00:00.00',null);
insert into material (id,author_id,title,price_policy,price,created,modified_at,material_file) values (nextVal('material_seq'),1,'Eletrônica Digital Moderna e Vhdl','V','2.5','2011-09-01 00:00:00.00','2011-09-01 00:00:00.00',null);
insert into material (id,author_id,title,price_policy,price,created,modified_at,material_file) values (nextVal('material_seq'),1,'Sistemas Digitais - Princípios e Aplicações','F','10','2011-09-01 00:00:00.00','2011-09-01 00:00:00.00',null);
insert into material (id,author_id,title,price_policy,price,created,modified_at,material_file) values (nextVal('material_seq'),1,'A Culpa É Das Estrelas','M','6.75','2011-09-01 00:00:00.00','2011-09-01 00:00:00.00',null);
insert into material (id,author_id,title,price_policy,price,created,modified_at,material_file) values (nextVal('material_seq'),1,'Game Of Thrones - Um Guia Pop-Up de Westeros','F','0','2011-09-01 00:00:00.00','2011-09-01 00:00:00.00',null);
insert into material (id,author_id,title,price_policy,price,created,modified_at,material_file) values (nextVal('material_seq'),1,'Pelé - A Importância do Futebol','M','1','2011-09-01 00:00:00.00','2011-09-01 00:00:00.00',null);
insert into material (id,author_id,title,price_policy,price,created,modified_at,material_file) values (nextVal('material_seq'),1,'Box - As Crônicas de Gelo e Fogo+ o Cavaleiro Dos Sete Reinos - 6 Volumes Pocket','F','120','2011-09-01 00:00:00.00','2011-09-01 00:00:00.00',null);
# --- !Downs

delete from user;
delete from material;
