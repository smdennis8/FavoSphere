drop database if exists favosphere;
create database favosphere;
use favosphere;

create table `user` (
    user_id bigint primary key auto_increment,
    first_name varchar(50) not null,
    middle_name varchar(50) not null,
    last_name varchar(50) not null ,
    phone varchar(50) not null,
    email varchar(255) not null unique,
    password_hash varchar(2048) not null,
    registered_on datetime not null,
    last_login datetime not null,
    user_enabled bit not null default(1)
);

create table `role` (
    role_id int primary key auto_increment,
    title varchar(50) not null unique,
    `description` varchar(255) not null,
    enabled bit not null default(1),
    created_on datetime not null,
    updated_on datetime not null
);

create table user_role (
    user_id bigint not null,
    role_id int not null,
    constraint pk_user_role
        primary key (user_id, role_id),
    constraint fk_user_role_user_id
        foreign key (user_id)
        references `user`(user_id),
    constraint fk_user_role_role_id
        foreign key (role_id)
        references `role`(role_id)
);

create table permission (
    permission_id int primary key auto_increment,
    title varchar(50) not null unique,
    `description` varchar(255) not null,
    created_on datetime not null,
    updated_on datetime not null,
    enabled bit not null default(1)
);

create table role_permission (
    role_id int not null,
    permission_id int not null,
    constraint pk_role_permission_id
        primary key (role_id, permission_id),
    constraint fk_role_permission_role_id
        foreign key (role_id)
        references `role`(role_id),
    constraint fk_role_permission_permission_id
        foreign key (permission_id)
        references permission(permission_id)
);

create table favorite (
    favorite_id bigint primary key auto_increment,
    user_id bigint not null,
    url varchar(500) not null,
    `source` varchar(255),
    creator varchar(50),
    `type` varchar(255),
    title text,
    `description` text,
    gif_url varchar(500),
    image_url varchar(500),
    created_on datetime not null,
    updated_on datetime not null,
	is_custom_title bit default(null),
	is_custom_description bit default(null),
    is_custom_image bit default(null),
	is_custom_gif bit default(null),
    constraint fk_user_user_id
        foreign key (user_id)
        references `user`(user_id)
);

create table tag (
    tag_id int primary key auto_increment,
    title varchar(50) not null unique,
    created_on datetime not null,
    updated_on datetime not null,
    is_custom bit not null default(0)
);

create table favorite_tag (
    favorite_id bigint not null,
    tag_id int not null,
    constraint pk_favorite_tag
        primary key (favorite_id, tag_id),
    constraint fk_favorite_tag_favorite_id
        foreign key (favorite_id)
        references favorite(favorite_id),
    constraint fk_favorite_tag_tag_id
        foreign key (tag_id)
        references tag(tag_id)
);

insert into `role` (title, `description`, enabled, created_on, updated_on) values
    ('USER', 'Manages own favorites', 1, '2023-06-26','2023-06-26'),
    ('ADMIN', 'Manages all user favorites', 1, '2023-06-26','2023-06-26');

-- passwords are set to "P@ssw0rd!"
insert into `user` (first_name, middle_name, last_name, phone, email, password_hash, registered_on, last_login, user_enabled)
    values
    ('John', 'Jingle-Heimer', 'Smith', '1-111-111-1111', 'john@smith.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', '2010-01-11', '2023-06-26', 1),
    ('Sally', 'Wally', 'Jones', '1-222-222-2222', 'sally@jones.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', '2020-02-22', '2023-06-26', 1);

insert into user_role
    values
    (1, 2),
    (2, 1);
    
insert into permission (title, `description`, created_on, updated_on, enabled)
    values
    ('READ_OWN', 'View and read own favorites.', '2010-01-11', '2023-01-11', 1),
    ('WRITE_OWN', 'Create and edit own favorites.', '2020-02-22', '2023-02-22', 1),
    ('READ_ALL', 'View and read all favorites.', '2010-03-11', '2023-03-11', 1),
    ('WRITE_ALL', 'Create and edit all favorites.', '2020-04-22', '2023-04-22', 1);
    
insert into role_permission
    values
    (1, 1),
    (1, 2),
    (2, 3),
    (2, 4);

insert into favorite (user_id, url, `source`, creator, `type`, title, `description`, gif_url, image_url, created_on, updated_on, is_custom_title, is_custom_description, is_custom_image, is_custom_gif)
    values 
        (1, 'https://www.youtube.com/watch?v=R6EFebizEKs', 'Youtube', 'News Be Funny', 'Video', 'Best Cats Work From Home News Bloopers',
        'Watch the Best Cats Work From Home News Bloopers! In this unique video with original commentary, we comment and react to the best cats news bloopers that happened during working from 
        home as part of a critical review. Voiceover by Kezhal Dashti. Subscribe: / newsbefunny About News Be Funny: News Be Funny creates unique videos with original commentary to react and 
        comment on a diverse array of topics as part of a critical review. News Be Funny has been featured in media outlets worldwide including Buzzfeed, Rolling Stone, Mashable, Time Magazine, 
        and the Washington Post.', null, 'https://i.ytimg.com/vi/R6EFebizEKs/hq720.jpg?sqp=-oaymwEcCOgCEMoBSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLA1IWSdfmEDIaLu-t6NPVRooTx52A', '2023-10-10', 
        '2023-11-11', 1, 1, 1, 1),
        (2, 'https://www.youtube.com/watch?v=GmneUncWZMg', 'Youtube', 'Sports Complex', 'Video', 'Most Unexpected Animal Interference Moments in Sports | Funny Invasions & Interruption',
        '(All rights go to the original leagues and their broadcasters, no copyright infringement intended.  If I feature clips that you own and that you don\'t want me to feature, contact me via
        my business email and I\'ll take it down as soon as I can) \"Copyright Disclaimer Under Section 107 of the Copyright Act 1976, allowance is made for \"fair use\" for purposes such as criticism,
        comment, news reporting, teaching, scholarship, and research. Fair use is a use permitted by copyright statute that might otherwise be infringing. Non-profit, educational or personal use tips 
        the balance in favor of fair use.\"', null, 'https://i.ytimg.com/vi/GmneUncWZMg/hq720.jpg?sqp=-oaymwEcCOgCEMoBSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLAoIhY5ERmG_dnz42mHjc_uCxYqyg', '2023-10-10',
        '2023-11-11', 1, 1, 1, 1);