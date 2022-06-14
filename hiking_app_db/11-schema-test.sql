drop database if exists hiking_app_test;
create database hiking_app_test;
use hiking_app_test;

-- User entity

create table app_user (
	app_user_id int primary key auto_increment,
	email varchar(50) not null unique,
	password_hash varchar(2048) not null,
    enabled boolean not null default (1)
);

create table app_user_info (
	app_user_id int unique not null,
	first_name varchar(20) null,
	last_name varchar(20) null,
	city varchar(30) null,
	state varchar(2) null,
    constraint fk_app_user_info_app_user_id
		foreign key (app_user_id)
        references app_user (app_user_id)
);

-- User Security
create table app_role(
	app_role_id int primary key auto_increment,
	role_name varchar(20) not null unique
);


create table app_user_role(
	app_user_id int not null,
	app_role_id int not null,
	constraint pk_app_user_role
		primary key (app_user_id, app_role_id),
	constraint fk_app_user_role_user_id
		foreign key (app_user_id) 
		references app_user (app_user_id),
	constraint fk_app_user_role_role_id
		foreign key (app_role_id) 
		references app_role (app_role_id)
);

-- Models



create table trail (
	trail_id int primary key auto_increment,
	`name` varchar(100) not null,
	city varchar(30) not null,
	state varchar(2) not null,
	trail_length int not null,
	rating varchar(20) not null,
	trail_map varchar(1024) null,
	description varchar(4096) null,
	app_user_id int not null,
	constraint fk_app_user_app_user_id_trail
		foreign key(app_user_id)
		references app_user(app_user_id)
);


create table spot(
	spot_id int primary key auto_increment,
	`name` varchar(50) null,
	gps_lat double not null,
	gps_long double not null,
	rating int not null,
    rating_count int not null,
	description varchar(4096) null,
	app_user_id int not null,
	constraint fk_app_user_app_user_id_spot
		foreign key(app_user_id)
		references app_user(app_user_id)
);

create table photo (
	photo_id int primary key auto_increment,
    photo_url varchar(500)
);

create table spot_photo (
	photo_id int not null,
    spot_id int not null,
    constraint pk_spot_photo
		primary key (photo_id, spot_id),
    constraint fk_spot_photo_photo_id
		foreign key (photo_id)
        references photo(photo_id),
    constraint fk_spot_photo_spot_id
		foreign key (spot_id)
        references spot(spot_id)
);

create table trail_photo (
	photo_id int not null,
    trail_id int not null,
    constraint pk_trail_photo
		primary key (photo_id, trail_id),
	constraint fk_trail_photo_photo_id
		foreign key (photo_id)
        references photo(photo_id),
    constraint fk_trail_photo_trail_id
		foreign key (trail_id)
        references trail(trail_id)
);

-- Many to Many table(s)
create table trail_spot(
	trail_id int not null,
	spot_id int not null,
	constraint pk_trail_spot
		primary key (trail_id, spot_id),
	constraint fk_trail_trail_id
		foreign key(trail_id)
		references trail(trail_id),
	constraint fk_spot_spot_id
		foreign key(spot_id)
		references spot(spot_id)
);

delimiter //
create procedure set_known_good_state()
begin
/* Tables that have foreign keys must be deleted first before deleting the table that contains the foreign key */
delete from trail_spot;
delete from app_user_role;
delete from trail_photo;
alter table trail_photo auto_increment=1;
delete from trail;
alter table trail auto_increment=1;
delete from spot_photo;
alter table spot_photo auto_increment=1;
delete from photo;
alter table photo auto_increment=1;
delete from spot;
alter table spot auto_increment=1;
delete from app_user_role;
alter table app_user_role auto_increment=1;
delete from app_user_info;
delete from app_user;
alter table app_user auto_increment=1;
delete from app_role;
alter table app_role auto_increment=1;

insert into app_role (role_name) values
	('USER'), ('ADMIN');
insert into app_user(email, password_hash) values
    ("test@gmail.com", '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa'), ("test@dev-10.com", '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa'), 
    ("test@apple.com", '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa');
    
insert into app_user_info (app_user_id, first_name, last_name, city, state) values
(1, "Patrick", "Alarcon", "Austin", "TX"), (2, "John", "Doe", "Minneapolis", "MN"),
(3, "Steve", "Jobs", "Cupertino", "CA");
    
insert into app_user_role values (1,2), (2,1), (3,2);

insert into spot (name, gps_lat, gps_long, rating, description, app_user_id, rating_count) 
	values ("Super cool test spot", 75.675, 80.945, 4, "A great spot to test the database with", 2, 5),
			("Second awesome test spot", 45.378, 26.942, 3, "Another spot that can test the repo", 3, 7),
            ("Test3", 64.236, 89.2346, 2, "One more test spot", 1, 9);
            
insert into trail (name, city, state, trail_length, rating, app_user_id)
	values ("Fun trail", "Minneapolis", "MN", 4, "Intermediate", 2), ("Cool test trail", "San Francisco", "CA", 2, "Beginner", 3);
            
insert into photo (photo_url) values ("somefakeurl"), ("testurl"), ("anothertestphoto"), ("onemoretest"), ("deletethisspot"),
									("sometrailurl"), ("testurltrail"), ("anothertesttrailphoto"), ("onemoretrailtest"), ("deletethistrail");

insert into spot_photo (photo_id, spot_id) values (1, 1), (2, 1), (3, 2), (4, 2), (5,1);

insert into trail_photo (photo_id, trail_id) values (6, 1), (1, 1), (8, 2), (9, 2), (10, 1);


    
insert into trail_spot (trail_id, spot_id) values (1, 1), (2, 1), (2, 2), (1,3);

end //

delimiter ;
