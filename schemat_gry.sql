create table race(
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
        professions(p_name)not null);

create sequence players_seq
start with 1
increment by 1;        
create table players(
    player_id integer primary key,
    player_name varchar2(30) not null,
    player_level number(10,2) not null,
    player_profession varchar2(30)
        references
        professions(p_name)not null,
    player_race varchar2(30)
        references
        race(r_name) not null
        );
    
create table quests(
    q_name varchar2(30) primary key,
    experience_points number(10,2) not null,
    creator_id integer 
        references
        players(player_id) not null);
    
create table performances(
    done varchar2(1) not null,
    player_id integer
        references
        players(player_id)not null,
    quest_name varchar2(30)
        references
        quests(q_name) not null,
    primary key(player_id, quest_name));
    

create table equipments(
    capacity_eq number(6,2) not null,
    owner_id integer
        references
        players(player_id)not null,
    primary key(owner_id));

create table items_ownership(
    number_of integer not null,
    equipment_id integer
        references
        equipments(owner_id) not null,
    item_name varchar2(30)
        references
        items(i_name) not null,
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
        items(i_name) not null,
    monster_race varchar2(30)
        references
        race(r_name) not null
    );
    
create table clans(
    clan_name varchar2(30) primary key,
    clan_level integer not null,
    headquater varchar2(30) not null);
    

create table membership(
    founder varchar2(1) not null,
    member_id integer
        references
        players(player_id) not null,
    clan_name varchar2(30)
        references
        clans(clan_name) not null,
    primary key(member_id, clan_name)
    );