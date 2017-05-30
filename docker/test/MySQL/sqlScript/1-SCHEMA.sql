drop schema if exists `computer-database-db2`;
  create schema if not exists `computer-database-db2`;
  use `computer-database-db2`;

  drop table if exists computer;
  drop table if exists company;

  create table company (
    id                        bigint not null auto_increment,
    name                      varchar(255),
    constraint pk_company primary key (id))
  ;

  create table computer (
    id                        bigint not null auto_increment,
    name                      varchar(255),
    introduced                timestamp NULL,
    discontinued              timestamp NULL,
    company_id                bigint default NULL,
    constraint pk_computer primary key (id))
  ;

  create table users (
    username                  varchar(255) not null,
    password                  varchar(60) not null,
    enabled		      boolean default false,
    constraint pk_users primary key (username))
  ;

  create table users_roles (
    id			      bigint not null auto_increment,
    user		      varchar(255) not null,
    role		      varchar(45) not null,
    constraint pk_users_roles primary key (id),
    constraint unique_users_roles unique (user, role))
  ;

  alter table computer add constraint fk_computer_company_1 foreign key (company_id) references company (id) on delete restrict on update restrict;
  alter table users_roles add constraint fk_users_roles_users foreign key (user) references users (username) on delete restrict on update restrict;

  create index ix_computer_company_1 on computer (company_id);
  create index ix_computer_name on computer (name);
  create index ix_computer_introduced on computer (introduced);
  create index ix_computer_discontinued on computer (discontinued);

  create index ix_company_name on company (name);
