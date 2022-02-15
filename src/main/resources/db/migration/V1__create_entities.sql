SELECT 'CREATE DATABASE fd'
WHERE NOT EXISTS(SELECT FROM pg_database WHERE datname = 'fd');

CREATE SCHEMA IF NOT EXISTS finedesk;

create table if not exists finedesk.creative_tools
(
    id                 bigserial
    primary key,
    name               varchar(255),
    creative_tool_uuid varchar(255) not null
    );

alter table finedesk.creative_tools
    owner to postgres;

create table if not exists finedesk.roles
(
    user_role_id bigserial
    primary key,
    role         varchar(255)
    );

alter table finedesk.roles
    owner to postgres;

create table if not exists finedesk.user_skills
(
    id               bigserial
    primary key,
    description      varchar(255),
    level            varchar(255),
    user_skills_uuid varchar(255) not null
    );

alter table finedesk.user_skills
    owner to postgres;

create table if not exists finedesk.creative_tools_user_skills
(
    creative_tool_id bigint not null
    constraint fk4mnk9ij3k4sgedun4h9q69npc
    references finedesk.creative_tools,
    user_skills_id   bigint not null
    constraint uk_jmh6dut36a0tbjstbpsrn97bn
    unique
    constraint fk3y2wj8f7o3smpx5faankewi8e
    references finedesk.user_skills,
    primary key (creative_tool_id, user_skills_id)
    );

alter table finedesk.creative_tools_user_skills
    owner to postgres;

create table if not exists finedesk.user_verification_token
(
    id              bigserial
    primary key,
    expiration_time timestamp,
    type            varchar(255),
    uuid            varchar(255)
    );

alter table finedesk.user_verification_token
    owner to postgres;

create table if not exists finedesk.users
(
    id                         bigserial
    primary key,
    first_name                 varchar(255),
    is_active                  boolean      not null,
    is_blocked                 boolean      not null,
    is_deleted                 boolean      not null,
    last_name                  varchar(255),
    password                   varchar(255),
    username                   varchar(255)
    constraint ukr43af9ap4edm43mmtq01oddj6
    unique,
    user_uuid                  varchar(255) not null
    constraint uuid_index
    unique,
    user_verification_token_id bigint
    constraint fk1nq1da0ylounwgdgf41sjddqj
    references finedesk.user_verification_token
    );

alter table finedesk.users
    owner to postgres;

create table if not exists finedesk.user_id_role_id
(
    user_id bigint not null
    constraint fkpx49j7je8x1ddadvmpikr6lc3
    references finedesk.users,
    role_id bigint not null
    constraint fkeppe7c77dt72ntq9bbae245da
    references finedesk.roles,
    primary key (user_id, role_id)
    );

alter table finedesk.user_id_role_id
    owner to postgres;

create table if not exists finedesk.user_profiles
(
    id                bigserial
    primary key,
    cover_photourl    varchar(255),
    profile_photourl  varchar(255),
    user_profile_uuid varchar(255) not null,
    user_id           bigint       not null
    constraint uk_e5h89rk3ijvdmaiig4srogdc6
    unique
    constraint fkjcad5nfve11khsnpwj1mv8frj
    references finedesk.users
    );

alter table finedesk.user_profiles
    owner to postgres;

create table if not exists finedesk.user_profiles_user_skills
(
    user_profile_id bigint not null
    constraint fkl1ctybc28gw6w4ocoussoo2n9
    references finedesk.user_profiles,
    user_skills_id  bigint not null
    constraint uk_lq30kygj0bjy4uecqpwlkeajd
    unique
    constraint fkj1oi77eq428quv7lbbhib1vv1
    references finedesk.user_skills,
    primary key (user_profile_id, user_skills_id)
    );

alter table finedesk.user_profiles_user_skills
    owner to postgres;

create table if not exists finedesk.user_projects
(
    id                bigserial
    primary key,
    creation_date     timestamp,
    description       varchar(255),
    name              varchar(255),
    user_project_uuid varchar(255) not null,
    user_profile_id   bigint
    constraint fkkxkgtcs9iyducrsvem4bo1blu
    references finedesk.user_profiles
    );

alter table finedesk.user_projects
    owner to postgres;

create table if not exists finedesk.likes
(
    like_id         bigserial
    primary key,
    like_date       timestamp,
    like_uuid       varchar(255) not null,
    user_project_id bigint
    constraint fk3khh1fwkhfk6k10wahk92teo2
    references finedesk.user_projects
    );

alter table finedesk.likes
    owner to postgres;

create table if not exists finedesk.profile_like
(
    profile_id bigint not null
    constraint fk3u0uralrf4ku7k9cb16qljwn0
    references finedesk.user_profiles,
    like_id    bigint not null
    constraint fkccxv5mx370cgk80qha89fhhx2
    references finedesk.likes,
    primary key (profile_id, like_id)
    );

alter table finedesk.profile_like
    owner to postgres;

create table if not exists finedesk.project_items
(
    id                bigserial
    primary key,
    imageurl          varchar(255),
    project_item_uuid varchar(255) not null,
    user_project_id   bigint
    constraint fk4c7hrjuxmxfriwh4hg9wh8b5l
    references finedesk.user_projects
    );

alter table finedesk.project_items
    owner to postgres;

create table if not exists finedesk.user_projects_likes
(
    user_project_id bigint not null
    constraint fknsviqxbbv2hie3vbf5mx3tthy
    references finedesk.user_projects,
    likes_like_id   bigint not null
    constraint uk_oqr8np8end3pafe6r1wp2p6km
    unique
    constraint fkmqc7qr1jt08vydvrr64df2l7g
    references finedesk.likes,
    primary key (user_project_id, likes_like_id)
    );

alter table finedesk.user_projects_likes
    owner to postgres;

