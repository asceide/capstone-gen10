drop database if exists hiking_app;
create database hiking_app;
use hiking_app;

-- User entity

create table app_user (
	app_user_id int primary key auto_increment,
	email varchar(50) not null unique,
	password_hash varchar(2048) not null,
	first_name varchar(20) null,
	last_name varchar(20) null,
	city varchar(30) null,
	state varchar(2) null
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

create table photos(
	photo_id int primary key auto_increment,
	photo_url varchar(500)
);

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


create table spot (
	spot_id int primary key auto_increment,
	`name` varchar(50) null,
	photo_id int null,
	gps_lat double not null,
	gps_long double not null,
	rating int not null,
	description varchar(4096) null,
	app_user_id int not null,
	constraint fk_photos_photo_id
		foreign key(photo_id)
		references photos(photo_id),
	constraint fk_app_user_app_user_id_spot
		foreign key(app_user_id)
		references app_user(app_user_id)
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

