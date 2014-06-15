# --- Sample dataset

# --- !Ups

insert into user_has_material (user_id, material_id) values (2,1);
insert into user_has_material (user_id, material_id) values (2,2);
insert into user_has_material (user_id, material_id) values (2,3);

# --- !Downs

delete from user_has_material;
