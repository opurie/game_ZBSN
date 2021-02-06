create table races(
    r_name varchar2(30) primary key,
    strength integer not null,
    agility integer not null,
    intellect integer not null);



create table professions(
    p_name varchar2(30) primary key);
    
create table items(
    i_name varchar2(30) primary key,
    strength integer not null,
    agility integer not null,
    intellect integer not null,
    weight number(6,2)not null,
    profession varchar2(30)
        references
        professions(p_name) on delete cascade);
drop sequence players_seq;
create sequence players_seq
start with 1
increment by 1;        
create table players(
    player_id integer primary key,
    player_name varchar2(30) not null,
    player_level number(10,2) not null,
    player_profession varchar2(30)
        references
        professions(p_name)on delete cascade,
    player_race varchar2(30)
        references
        races(r_name)on delete cascade
        );
create table quests(
    q_name varchar2(30) primary key,
    experience_points number(10,2) not null,
    creator_id integer 
        references
        players(player_id) on delete cascade);
    
create table performances(
    done varchar2(1) not null,
    player_id integer
        references
        players(player_id)on delete cascade,
    quest_name varchar2(30)
        references
        quests(q_name) on delete cascade,
    primary key(player_id, quest_name));

create table equipments(
    capacity_eq number(6,2) not null,
    owner_id integer
        references
        players(player_id)on delete cascade,
    primary key(owner_id));

create table items_ownership(
    number_of integer not null,
    equipment_id integer
        references
        equipments(owner_id) on delete cascade,
    item_name varchar2(30)
        references
        items(i_name) on delete cascade,
    primary key(equipment_id, item_name)
    );
    
create sequence monster_seq
start with 1
increment by 1;
create table monsters(
    monster_id integer primary key,
    monster_name varchar2(30) not null,
    monster_type varchar2(30) not null,
    owned_item varchar2(30)
        references 
        items(i_name) on delete set null,
    monster_race varchar2(30)
        references
        races(r_name) on delete cascade
    );
    
create table clans(
    clan_name varchar2(30) primary key,
    clan_level integer not null,
    headquater varchar2(30) not null);
    
create table membership(
    founder varchar2(1) not null,
    member_id integer
        references
        players(player_id) on delete cascade,
    clan_name varchar2(30)
        references
        clans(clan_name) on delete cascade,
    primary key(member_id, clan_name)
    );
    
