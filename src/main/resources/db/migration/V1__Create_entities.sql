create table if not exists creative_tools
(
    id   bigserial
    primary key,
    name varchar(255)
    );

alter table creative_tools
    owner to postgres;

create table if not exists users
(
    id         bigserial
    primary key,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255)
    );

alter table users
    owner to postgres;

create table if not exists user_profiles
(
    id               bigserial
    primary key,
    cover_photourl   varchar(255),
    profile_photourl varchar(255),
    user_id          bigint not null
    constraint uk_e5h89rk3ijvdmaiig4srogdc6
    unique
    constraint fkjcad5nfve11khsnpwj1mv8frj
    references users
    );

alter table user_profiles
    owner to postgres;

create table if not exists user_projects
(
    id              bigserial
    primary key,
    creation_date   timestamp,
    description     varchar(255),
    name            varchar(255),
    user_profile_id bigint
    constraint fkkxkgtcs9iyducrsvem4bo1blu
    references user_profiles
    );

alter table user_projects
    owner to postgres;

create table if not exists project_items
(
    id              bigserial
    primary key,
    imageurl        varchar(255),
    user_project_id bigint not null
    constraint fk4c7hrjuxmxfriwh4hg9wh8b5l
    references user_projects
    );

alter table project_items
    owner to postgres;

create table if not exists user_profiles_user_projects
(
    user_profile_id  bigint not null
    constraint fkc5q28slu5o1tde07s6f2nsju9
    references user_profiles,
    user_projects_id bigint not null
    constraint uk_op6unk18e6kjm13mlophaafm4
    unique
    constraint fkcabhhv60nk9q9d80kttmawvai
    references user_projects
);

alter table user_profiles_user_projects
    owner to postgres;

create table if not exists user_projects_project_items
(
    user_project_id  bigint not null
    constraint fkohbuttj1ixod451uc5oyb509q
    references user_projects,
    project_items_id bigint not null
    constraint uk_3xlxlw6eew6b1xkx2ecuyrg01
    unique
    constraint fkmj3whw3dqkxteqikdd9xkx9ou
    references project_items,
    primary key (user_project_id, project_items_id)
    );

alter table user_projects_project_items
    owner to postgres;

create table if not exists user_skills
(
    id               bigserial
    primary key,
    description      varchar(255),
    level            varchar(255),
    creative_tool_id bigint
    constraint fk5p9eutdflru3pb1cjcf8rhvf2
    references creative_tools,
    user_profile_id  bigint
    constraint fkem4jmtpyq4lkqenj9osdmyaoy
    references user_profiles
    );

alter table user_skills
    owner to postgres;

create table if not exists user_profiles_user_skills
(
    user_profile_id bigint not null
    constraint fkl1ctybc28gw6w4ocoussoo2n9
    references user_profiles,
    user_skills_id  bigint not null
    constraint uk_lq30kygj0bjy4uecqpwlkeajd
    unique
    constraint fkj1oi77eq428quv7lbbhib1vv1
    references user_skills,
    primary key (user_profile_id, user_skills_id)
    );

alter table user_profiles_user_skills
    owner to postgres;

