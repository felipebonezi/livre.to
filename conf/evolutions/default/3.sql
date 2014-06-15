# --- Sample dataset

# --- !Ups
alter table user_has_material ADD COLUMN when TIMESTAMP not null DEFAULT now();

# --- !Downs
delete from material;
delete from user;

