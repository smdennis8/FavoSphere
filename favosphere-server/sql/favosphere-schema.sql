drop database if exists favosphere;
create database favosphere;
use favosphere;

create table `app_user` (
    app_user_id bigint primary key auto_increment,
    first_name varchar(50),
    middle_name varchar(50),
    last_name varchar(50),
    phone varchar(50),
    email varchar(255) not null unique,
    password_hash varchar(2048) not null,
    registered_on date not null,
    last_login date not null,
    user_enabled bit not null default(1)
);

create table `app_role` (
    app_role_id int primary key auto_increment,
    title varchar(50) not null unique,
    `description` varchar(255) not null,
    enabled bit not null default(1),
    created_on date not null,
    updated_on date not null
);

create table app_user_role (
    app_user_id bigint not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_app_user_id
        foreign key (app_user_id)
        references `app_user`(app_user_id),
    constraint fk_app_user_role_app_role_id
        foreign key (app_role_id)
        references `app_role`(app_role_id)
);

create table permission (
    permission_id int primary key auto_increment,
    title varchar(50) not null unique,
    `description` varchar(255) not null,
    created_on date not null,
    updated_on date not null,
    enabled bit not null default(1)
);

create table role_permission (
    app_role_id int not null,
    permission_id int not null,
    constraint pk_role_permission_id
        primary key (app_role_id, permission_id),
    constraint fk_role_permission_app_role_id
        foreign key (app_role_id)
        references `app_role`(app_role_id),
    constraint fk_role_permission_permission_id
        foreign key (permission_id)
        references permission(permission_id)
);

create table favorite (
    favorite_id bigint primary key auto_increment,
    app_user_id bigint not null,
    url varchar(500) not null,
    `source` varchar(255),
    creator varchar(50),
    `type` varchar(255),
    title text,
    `description` text,
    gif_url varchar(500),
    image_url varchar(500),
    created_on date not null,
    updated_on date not null,
	is_custom_title bit default(null),
	is_custom_description bit default(null),
    is_custom_image bit default(null),
	is_custom_gif bit default(null),
    constraint fk_favorite_app_user_id
        foreign key (app_user_id)
        references `app_user`(app_user_id)
);

create table tag (
    tag_id int primary key auto_increment,
    title varchar(50) not null unique,
    created_on date not null,
    updated_on date not null,
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

create table email (
    email_id bigint primary key auto_increment,
    app_user_id bigint not null,
    url varchar(500) not null,
    sent_on datetime not null,
    constraint fk_email_app_user_id
        foreign key (app_user_id)
        references `app_user`(app_user_id)
);

insert into `app_role` (title, `description`, enabled, created_on, updated_on) values
    ('USER', 'Manages own favorites', 1, '2023-06-26','2023-06-26'),
    ('ADMIN', 'Manages all user favorites', 1, '2023-06-26','2023-06-26');

-- passwords are set to "P@ssw0rd!"
insert into `app_user` (first_name, middle_name, last_name, phone, email, password_hash, registered_on, last_login, user_enabled)
    values
    ('John', 'Jingle-Heimer', 'Smith', '1-111-111-1111', 'john@smith.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', '2010-01-11', '2023-06-26', 1),
    ('Sally', 'Wally', 'Jones', '1-222-222-2222', 'sally@jones.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', '2020-02-22', '2023-06-26', 1),
    ('Admin', 'Service', 'Account', '1-210-843-0428', 'favosphere.app.inbox@gmail.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', '2023-06-30', '2023-06-30', 1),
    ('Seth', 'Matthew', 'Dennis', '1-210-843-0428', 'sethmatthews90@gmail.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', '2023-07-03', '2023-07-03', 1),
    ('Zack', '', 'Alexander', '1-222-222-2222', 'zda1994@gmail.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', '2023-07-03', '2023-07-03', 1),
    ('Sirak', '', 'Negash', '1-222-222-2222', 'srkneg@gmail.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', '2023-07-03', '2023-07-03', 1);

insert into app_user_role
    values
    (1, 2),
    (2, 1),
    (3, 2),
    (4, 1),
    (5, 1),
    (6, 1);
    
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

insert into favorite (app_user_id, url, `source`, creator, `type`, title, `description`, gif_url, image_url, created_on, updated_on, is_custom_title, is_custom_description, is_custom_image, is_custom_gif)
    values 
        (1, 'https://www.youtube.com/watch?v=R6EFebizEKs', 'Youtube', 'News Be Funny', 'Video', 'Best Cats Work From Home News Bloopers',
        'Watch the Best Cats Work From Home News Bloopers! In this unique video with original commentary, we comment and react to the best cats news bloopers that happened during working from 
        home as part of a critical review. Voiceover by Kezhal Dashti. Subscribe: / newsbefunny About News Be Funny: News Be Funny creates unique videos with original commentary to react and 
        comment on a diverse array of topics as part of a critical review. News Be Funny has been featured in media outlets worldwide including Buzzfeed, Rolling Stone, Mashable, Time Magazine, 
        and the Washington Post.', null, 'https://i.ytimg.com/vi/R6EFebizEKs/hq720.jpg?sqp=-oaymwEcCOgCEMoBSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLA1IWSdfmEDIaLu-t6NPVRooTx52A', '2023-07-02', 
        '2023-07-02', 1, 1, 1, 1),
        (2, 'https://www.youtube.com/watch?v=GmneUncWZMg', 'Youtube', 'Sports Complex', 'Video', 'Most Unexpected Animal Interference Moments in Sports | Funny Invasions & Interruption',
        '(All rights go to the original leagues and their broadcasters, no copyright infringement intended.  If I feature clips that you own and that you don\'t want me to feature, contact me via
        my business email and I\'ll take it down as soon as I can) \"Copyright Disclaimer Under Section 107 of the Copyright Act 1976, allowance is made for \"fair use\" for purposes such as criticism,
        comment, news reporting, teaching, scholarship, and research. Fair use is a use permitted by copyright statute that might otherwise be infringing. Non-profit, educational or personal use tips 
        the balance in favor of fair use.\"', null, 'https://i.ytimg.com/vi/GmneUncWZMg/hq720.jpg?sqp=-oaymwEcCOgCEMoBSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLAoIhY5ERmG_dnz42mHjc_uCxYqyg', '2023-07-02',
        '2023-07-02', 1, 1, 1, 1),
        (4, 'https://www.youtube.com/watch?v=R6EFebizEKs', 'Youtube', 'News Be Funny', 'Video', 'Best Cats Work From Home News Bloopers',
        'Watch the Best Cats Work From Home News Bloopers! In this unique video with original commentary, we comment and react to the best cats news bloopers that happened during working from 
        home as part of a critical review. Voiceover by Kezhal Dashti. Subscribe: / newsbefunny About News Be Funny: News Be Funny creates unique videos with original commentary to react and 
        comment on a diverse array of topics as part of a critical review. News Be Funny has been featured in media outlets worldwide including Buzzfeed, Rolling Stone, Mashable, Time Magazine, 
        and the Washington Post.', null, 'https://i.ytimg.com/vi/R6EFebizEKs/hq720.jpg?sqp=-oaymwEcCOgCEMoBSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLA1IWSdfmEDIaLu-t6NPVRooTx52A', '2023-07-02', 
        '2023-07-02', 1, 1, 1, 1),
        (4, 'https://www.youtube.com/watch?v=GmneUncWZMg', 'Youtube', 'Sports Complex', 'Video', 'Most Unexpected Animal Interference Moments in Sports | Funny Invasions & Interruption',
        '(All rights go to the original leagues and their broadcasters, no copyright infringement intended.  If I feature clips that you own and that you don\'t want me to feature, contact me via
        my business email and I\'ll take it down as soon as I can) \"Copyright Disclaimer Under Section 107 of the Copyright Act 1976, allowance is made for \"fair use\" for purposes such as criticism,
        comment, news reporting, teaching, scholarship, and research. Fair use is a use permitted by copyright statute that might otherwise be infringing. Non-profit, educational or personal use tips 
        the balance in favor of fair use.\"', null, 'https://i.ytimg.com/vi/GmneUncWZMg/hq720.jpg?sqp=-oaymwEcCOgCEMoBSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLAoIhY5ERmG_dnz42mHjc_uCxYqyg', '2023-07-02',
        '2023-07-02', 1, 1, 1, 1),
        
--         app_user_id, url, `source`, creator, `type`, title, 
--         `description`, 
--         gif_url, image_url, 
--         created_on, updated_on, is_custom_title, is_custom_description, is_custom_image, is_custom_gif
        
        (5, 'https://en.wikipedia.org/wiki/Capsule_hotel', 'Wikipedia', 'Japan', 'Article', 'Capsule Hotel',
        'Capsule hotel (Japanese: カプセルホテル, romanized: kapuseru hoteru), also known in the Western world as a pod hotel,[1] is a type of hotel developed in Japan that features many small bed-sized rooms known as capsules. Capsule hotels provide cheap, basic overnight accommodation for guests who do not require or who cannot afford larger, more expensive rooms offered by more conventional hotels.', 
        null, 'https://cdn.shopify.com/s/files/1/0344/6469/files/cats_refuse_to_admit_box_too_small_7aea7a59-470d-4d43-acf9-3fc1b705b70d.png?v=1661192187',
        '2023-07-05', '2023-07-05', 1, 1, 1, 0),
        
        (5, 'http://gomokuworld.com/articles/16_useful_tips_to_become_a_better_gomoku_player', 'Gomoku World', 'Japan', 'Resource', 'Gomoku',
        '16 Useful Tips To Become A Better Gomoku Player', 
        'https://media.tenor.com/HY4TfuF_wfEAAAAd/pretty-sneaky-sis-connect-four.gif', null, 
        '2023-07-05', '2023-07-05', 1, 1, 0, 1),
        
        (5, 'https://en.wikipedia.org/wiki/Solar_panel', 'Wikipedia', 'Bell Labs', 'Article', 'Solar Farm',
        'A solar panel is a device that converts sunlight into electricity by using photovoltaic (PV) cells. PV cells are made of materials that generate electrons when exposed to light. The electrons flow through a circuit and produce direct current (DC) electricity, which can be used to power various devices or be stored in batteries. Solar panels are also known as solar cell panels, solar electric panels, or PV modules.',
        'https://img.buzzfeed.com/buzzfeed-static/static/2017-08/29/11/asset/buzzfeed-prod-fastlane-03/anigif_sub-buzz-8824-1504020317-7.gif', null,
        '2023-07-05', '2023-07-05', 1, 1, 0, 1),
        
        (5, 'https://www.fieldmag.com/articles/sustainable-ethical-foraging-guide', 'Field Mag', 'Zoe Baillargeon', 'Resource', 'Sustainable Foraging',
        'Foraging or wildcrafting—the practice of finding and sourcing edible wild plants like fruit, flowers, leaves, and mushrooms in nature—has been a part of Indigenous foodways and culture for centuries. But in the past five or so years, foraging for wild plants has found new popularity in the worlds of fine dining and outdoors culture.',
        null, 'https://ih1.redbubble.net/image.3330778651.9513/raf,750x1000,075,t,FFFFFF:97ab1c12de.jpg',
        '2023-07-05', '2023-07-05', 1, 1, 1, 0),
        
        (5, 'https://www.airbnb.com/', 'Airbnb', 'Brian Chesky, Nathan Blecharczyk, and Joe Gebbia', 'App', 'Don\'t Wreck My House',
        'Book Your Stay for Vacation, Work, and More. Feel the Comforts of Home, Wherever You Go. Book Top Rated Rentals for Your Next Trip - Houses, Cabins, Condos, Unique Stays & More. Best Prices. 5 Star Hosts. 100,000 Cities. Instant Confirmation. Amenities: WiFi, Pet Friendly.',
        null, 'https://media.istockphoto.com/id/850872594/photo/hangover-after-a-party.jpg?s=612x612&w=0&k=20&c=sqQH5LTdChsIzjK_xM51mg6lSWTqUof7Ll03ZTHnohU=',
        '2023-07-05', '2023-07-05', 1, 1, 1, 0),
        
        (5, 'https://www.broadway.com/', 'Broadway', 'NYC', 'Live Theatre', 'Tiny Theatres',
        'Broadway theatre, or Broadway, are the theatrical performances presented in the 41 professional theatres, each with 500 or more seats, located in the Theater District and the Lincoln Center along Broadway, in Midtown Manhattan, New York City.Broadway and London\'s West End together represent the highest commercial level of live theater in the English-speaking world.',
        'https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExZWI0eWFkYWwzMGh6Z3F2MWhrNmt2OWp4MGNrbjVjbm5sMnFrZXh5ZyZlcD12MV9naWZzX3NlYXJjaCZjdD1n/KjClcwQ8jAHy8/200.gif', null,
        '2023-07-05', '2023-07-05', 1, 1, 0, 1),
        
		(5, 'https://www.cia.gov/', 'CIA', 'Harry Truman', 'Government Agency', 'Field Agent API',
        'The Central Intelligence Agency, known informally as the Agency and historically as the company, is a civilian foreign intelligence service of the federal government of the United States, officially tasked with gathering, processing, and analyzing national security information from around the world, primarily through the use of human intelligence (HUMINT) and conducting covert action through its Directorate of Operations. As a principal member of the United States Intelligence Community (IC), the CIA reports to the Director of National Intelligence and is primarily focused on providing intelligence for the President and Cabinet of the United States. Following the dissolution of the Office of Strategic Services (OSS) at the end of World War II, President Harry S. Truman created the Central Intelligence Group under the direction of a Director of Central Intelligence by presidential directive on January 22, 1946,[8] and this group was transformed into the Central Intelligence Agency by implementation of the National Security Act of 1947.',
        null, 'https://image.spreadshirtmedia.net/image-server/v1/compositions/T1482A729PA5807PT17X70Y65D185875723W20830H24997/views/2,width=550,height=550,appearanceId=729,backgroundColor=716969,noPt=true/ive-got-a-great-cia-joke-law-enforcement-gift-unisex-baseball-jacket.jpg',
        '2023-07-05', '2023-07-05', 1, 1, 0, 1),

        (5, 'https://en.wikipedia.org/wiki/Threat_Level_Midnight', 'Threat Level Midnight', 'Michael Scott', 'Movie', 'Web Field Agent',
        '"Threat Level Midnight" is the seventeenth episode of the seventh season of the American comedy television series The Office, and the show\'s 143rd episode overall. It originally aired on NBC on February 17, 2011. The episode was written by B. J. Novak and directed by Tucker Gates.',
        null, 'https://upload.wikimedia.org/wikipedia/en/a/af/Threat-Level-Midnight.jpg',
        '2023-07-05', '2023-07-05', 1, 1, 1, 0),

        (5, 'https://en.wikipedia.org/wiki/James_Bond', 'James Bond', 'Ian Fleming', 'Book/Movie Series', 'React Field Agent',
        'The James Bond series focuses on James Bond, a fictional British Secret Service agent created in 1953 by writer Ian Fleming, who featured him in twelve novels and two short-story collections. Since Fleming\'s death in 1964, eight other authors have written authorised Bond novels or novelisations: Kingsley Amis, Christopher Wood, John Gardner, Raymond Benson, Sebastian Faulks, Jeffery Deaver, William Boyd, and Anthony Horowitz. The latest novel is With a Mind to Kill by Anthony Horowitz, published in May 2022. Additionally Charlie Higson wrote a series on a young James Bond, and Kate Westbrook wrote three novels based on the diaries of a recurring series character, Moneypenny.',
        'https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExdmJxb3VwY3k5c2h4a3FsbDJtOTRkY2FxYjRvNTRsb2U0amM1em5kbCZlcD12MV9naWZzX3NlYXJjaCZjdD1n/nu4DN4nKos3m0/giphy.gif', null,
        '2023-07-05', '2023-07-05', 1, 1, 0, 1),
        (6, 'https://www.youtube.com/watch?v=R6EFebizEKs', 'Youtube', 'News Be Funny', 'Video', 'Best Cats Work From Home News Bloopers',
        'Watch the Best Cats Work From Home News Bloopers! In this unique video with original commentary, we comment and react to the best cats news bloopers that happened during working from 
        home as part of a critical review. Voiceover by Kezhal Dashti. Subscribe: / newsbefunny About News Be Funny: News Be Funny creates unique videos with original commentary to react and 
        comment on a diverse array of topics as part of a critical review. News Be Funny has been featured in media outlets worldwide including Buzzfeed, Rolling Stone, Mashable, Time Magazine, 
        and the Washington Post.', null, 'https://i.ytimg.com/vi/R6EFebizEKs/hq720.jpg?sqp=-oaymwEcCOgCEMoBSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLA1IWSdfmEDIaLu-t6NPVRooTx52A', '2023-07-02', 
        '2023-07-02', 1, 1, 1, 1),
        (6, 'https://www.youtube.com/watch?v=GmneUncWZMg', 'Youtube', 'Sports Complex', 'Video', 'Most Unexpected Animal Interference Moments in Sports | Funny Invasions & Interruption',
        '(All rights go to the original leagues and their broadcasters, no copyright infringement intended.  If I feature clips that you own and that you don\'t want me to feature, contact me via
        my business email and I\'ll take it down as soon as I can) \"Copyright Disclaimer Under Section 107 of the Copyright Act 1976, allowance is made for \"fair use\" for purposes such as criticism,
        comment, news reporting, teaching, scholarship, and research. Fair use is a use permitted by copyright statute that might otherwise be infringing. Non-profit, educational or personal use tips 
        the balance in favor of fair use.\"', null, 'https://i.ytimg.com/vi/GmneUncWZMg/hq720.jpg?sqp=-oaymwEcCOgCEMoBSFXyq4qpAw4IARUAAIhCGAFwAcABBg==&rs=AOn4CLAoIhY5ERmG_dnz42mHjc_uCxYqyg', '2023-07-02',
        '2023-07-02', 1, 1, 1, 1);
        
insert into email (app_user_id, url, sent_on)
	values
		(1,'https://www.youtube.com/watch?v=xtZI23hxetw','2023-11-11 11:11:11.11'),
        (1,'https://www.youtube.com/watch?v=j7q8Zzw46oQ','2023-11-11 12:12:12.12'),
        (2,'https://www.youtube.com/watch?v=dUqJ1U2QhAo','2023-11-11 13:13:13.13'),
        (4,'https://www.youtube.com/watch?v=dUqJ1U2QhAo','2023-11-11 13:13:13.13'),
        (5,'https://www.youtube.com/watch?v=dUqJ1U2QhAo','2023-11-11 13:13:13.13'),
        (6,'https://www.youtube.com/watch?v=dUqJ1U2QhAo','2023-11-11 13:13:13.13');
