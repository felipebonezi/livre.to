# --- Sample dataset

# --- !Ups

insert into user (id,access_token,login,password,name,status,gender,modified_at) values (  1,'token','admin','admin','Administrador','ACTIVE','MALE',null);
# --- !Downs

delete from user;
