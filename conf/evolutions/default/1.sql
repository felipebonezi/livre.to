# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table material (
  id                        bigint not null,
  author_id                 bigint,
  title                     varchar(255),
  price_policy              varchar(1),
  price                     varchar(255),
  description               varchar(255),
  created                   timestamp,
  modified_at               timestamp,
  material_file             blob,
  material_thumbnail        blob,
  score                     integer default 0,
  constraint ck_material_price_policy check (price_policy in ('M','V','F')),
  constraint pk_material primary key (id))
;

create table user (
  id                        bigint not null,
  access_token              varchar(255),
  login                     varchar(255),
  password                  varchar(255),
  name                      varchar(255),
  mail                      varchar(255),
  status                    varchar(7),
  gender                    varchar(6),
  modified_at               timestamp not null,
  constraint ck_user_status check (status in ('ACTIVE','BLOCKED')),
  constraint ck_user_gender check (gender in ('FEMALE','MALE')),
  constraint pk_user primary key (id))
;


create table user_has_material (
  user_id                        bigint not null,
  material_id                    bigint not null,
  constraint pk_user_has_material primary key (user_id, material_id))
;
create sequence material_seq;

create sequence user_id_seq;

alter table material add constraint fk_material_author_1 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_material_author_1 on material (author_id);



alter table user_has_material add constraint fk_user_has_material_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_has_material add constraint fk_user_has_material_material_02 foreign key (material_id) references material (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists material;

drop table if exists user_has_material;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists material_seq;

drop sequence if exists user_id_seq;

