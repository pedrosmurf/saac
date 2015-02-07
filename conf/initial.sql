create table "users" ("enrollment" VARCHAR(254) NOT NULL PRIMARY KEY,"password" VARCHAR(254) NOT NULL,"name" VARCHAR(254) NOT NULL,"role" VARCHAR(254) NOT NULL);

create table "user_maps" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"user_enrollment" VARCHAR(254) NOT NULL,"active" BOOLEAN NOT NULL,"workload" BIGINT NOT NULL,"valid_workload" BIGINT NOT NULL,"status" VARCHAR(254) NOT NULL);
alter table "user_maps" add constraint "user_fk" foreign key("user_enrollment") references "users"("enrollment") on update NO ACTION on delete NO ACTION;

create table "requests" ("id" BIGSERIAL NOT NULL PRIMARY KEY,"status" VARCHAR(254) NOT NULL,"user_enrollment" VARCHAR(254) NOT NULL,"activity" VARCHAR(254) NOT NULL,"event" VARCHAR(254) NOT NULL,"description" VARCHAR(254) NOT NULL,"participation" VARCHAR(254) NOT NULL,"institution" VARCHAR(254) NOT NULL,"period" VARCHAR(254) NOT NULL,"workload" BIGINT NOT NULL,"valid_workload" BIGINT NOT NULL,"document" VARCHAR(254) NOT NULL,"comment" VARCHAR(254) NOT NULL,"user_map_id" BIGINT NOT NULL);
alter table "requests" add constraint "userMap_fk" foreign key("user_map_id") references "user_maps"("id") on update NO ACTION on delete NO ACTION;
alter table "requests" add constraint "user_fk" foreign key("user_enrollment") references "users"("enrollment") on update NO ACTION on delete NO ACTION;


insert into "users" ("enrollment","password","name","role")  values ('12345678','123','Pedro','student');
insert into "user_maps" values ('1','12345678',true,0,0,'created');
insert into "users" ("enrollment","password","name","role")  values ('87654321','123','Maria','teacher');

select nextval('user_maps_id_seq');
select nextval('user_maps_id_seq');

GRANT ALL ON users TO saac;
GRANT ALL ON requests TO saac;
GRANT ALL ON requests_id_seq TO saac;
GRANT ALL ON user_maps TO saac;
GRANT ALL ON user_maps_id_seq TO saac;