drop schema if exists `computer-database-db-gatling`;
  create schema if not exists `computer-database-db-gatling`;
  
  ALTER DATABASE `computer-database-db-gatling` charset=utf8;
  
  use `computer-database-db-gatling`;

  drop table if exists computer;
  drop table if exists company;
  

  create table company (
    id                        bigint not null auto_increment,
    name                      varchar(255),
    constraint pk_company primary key (id)
    ) ENGINE=MyISAM;

  create table computer (
    id                        bigint not null auto_increment,
    name                      varchar(255),
    introduced                timestamp NULL,
    discontinued              timestamp NULL,
    company_id                bigint default NULL,
    constraint pk_computer primary key (id)
    ) ENGINE=MyISAM;

  ALTER TABLE computer ADD FULLTEXT computer_name_full_text (name);
  ALTER TABLE company ADD FULLTEXT company_name_full_text (name);

  alter table computer add constraint fk_computer_company_1 foreign key (company_id) references company (id) on delete restrict on update restrict;
  create index ix_computer_company_1 on computer (company_id);