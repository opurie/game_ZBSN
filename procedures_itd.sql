------------PLAYERS, MONSTERS, RACE, PROFESSIONS---------------------------------------------------------------
create or replace procedure create_player(pname in nvarchar2, pprof in nvarchar2, prace in nvarchar2) is ----add create_eq
begin
	insert into players(player_id, player_name, player_level, player_profession, player_race) values
		(players_seq.nextval, pname, 1, pprof, prace);
		
	insert into equipments(capacity_eq, owner_id) values (50, players_seq.currval);
end create_player;

create or replace procedure delete_player(pid in number) is
begin
    delete from players
    where player_id = pid;
end delete_player;

create or replace function get_players 
    return sys_refcursor
    is
    player_cursor sys_refcursor;
begin
    open player_cursor for
        select player_id || '. '||player_name 
        from players
        order by player_id;
    return player_cursor;
end get_players;

create or replace procedure create_monster(pname in nvarchar2, ptype in nvarchar2, pitem in nvarchar2, prace in nvarchar2) is
begin
	insert into monsters(monster_id, monster_name, monster_type, owned_item, monster_race) values
		(monster_seq.nextval, pname, ptype, pitem, prace);
end create_monster;

create or replace procedure delete_monster(pid in number) is
begin
    delete from monsters
    where monster_id = pid;
end delete_monster;

create or replace function get_monsters 
    return sys_refcursor
    is
    monster_cursor sys_refcursor;
begin
    open monster_cursor for
        select monster_id || '. '||monster_name 
        from monsters
        order by monster_id;
    return monster_cursor;
end get_monsters;

create or replace procedure create_profession(pname in nvarchar2) is
begin
	insert into professions(p_name) values(pname);
end create_profession;

create or replace procedure delete_profession(pname in nvarchar2) is
begin
    delete from professions
    where p_name like pname;
end delete_profession;

create or replace function get_professions 
    return sys_refcursor
    is
    profession_cursor sys_refcursor;
begin
    open profession_cursor for
        select p_name from professions;
    return profession_cursor;
end get_professions;

create or replace procedure create_race(pname in nvarchar2, ps in number, pa in number, pin in number) is
begin
	insert into races (r_name, strength, agility, intellect) values
		(pname, ps, pa, pin);
end create_race;

create or replace procedure delete_race(pname in nvarchar2) is
begin
    delete from races
    where r_name like pname;
end delete_race;

create or replace function get_races 
    return sys_refcursor
    is
    race_cursor sys_refcursor;
begin
    open race_cursor for
        select r_name from races;
    return race_cursor;
end get_races;

create or replace procedure update_race(pname in nvarchar2, ps in number, pa in number, pin in number) is
begin
    update races
    set r_name = pname,
        strength = ps,
        agility = pa,
        intellect = pin;
end update_race;
------------QUESTS, TAKE_THE_TASK -------------------------------------------------------------------------------------
create or replace procedure create_quest(pname in nvarchar2, pexp in number, pid in number) is
begin
	insert into quests(q_name, experience_points, creator_id) values
		(pname, pexp, pid);
end create_quest;

create or replace procedure delete_quest(pname in nvarchar2) is
begin
    delete from quests
    where q_name like pname;
end delete_quest;

create or replace function get_quests 
    return sys_refcursor
    is
    quest_cursor sys_refcursor;
begin
    open quest_cursor for
        select q_name from quests;
    return quest_cursor;
end get_quests;


create or replace function take_the_task(pid in number, pname in nvarchar2)
    return nvarchar2 is
	buf performances.done%type;
begin
	select done
	into buf
	from performances
	where player_id = pid and quest_name = pname;
	
	if buf is null then
		insert into performances(done, player_id, quest_name) values
								('N', pid, pname);
        return 'Quest taken';
        
	elsif buf = 'N' then
		return 'Quest is already taken';
	elsif buf = 'Y' then
		return 'Quest is done';
	end if;
end take_the_task;
------------ITEMS, PICK_UP, DROP----------------------------------------------------------------------------------------
create or replace procedure create_item(pname in nvarchar2, ps in number, pa in number, pin in number, pweight in number,
										pprof in nvarchar2) is
begin
	insert into items(i_name, strength, agility, intellect, weight, profession) values
		(pname, ps, pa, pin, pweight, pprof);
end create_item;

create or replace procedure delete_item(pname in nvarchar2) is
begin
    delete from items
    where i_name like pname;
end delete_item;

create or replace function get_items 
    return sys_refcursor
    is
    item_cursor sys_refcursor;
begin
    open item_cursor for
        select i_name from items;
    return item_cursor;
end get_items;

create or replace procedure pick_up(pid in number, pname in nvarchar2)is
	i items_ownership.number_of%type;
begin
	select number_of
	into i
	from items_ownership
	where equipment_id = pid and item_name = pname;
	
	if i is null then
		insert into items_ownership(number_of, equipment_id, item_name) values
									(1, pid, pname);
	elsif i>=0 then
		update items_ownership
		set number_of = number_of + 1
		where equipment_id = pid and item_name = pname;
	end if;
end pick_up;

create or replace function drop_item(pid in number, pname in nvarchar2) 
    return nvarchar2 is
	i items_ownership.number_of%type;
begin
	select number_of
	into i
	from items_ownership
	where equipment_id = pid and item_name = pname;
	
	if i>0 then
		update items_ownership
		set number_of = number_of - 1
		where equipment_id = pid and item_name = pname;
        return 'Item dropped';
    else
		return 'You can not drop item that does not exists';
	end if;
end drop_item;
-----CLANS, JOIN, LEAVE-------------------------------------------------------------------------------------------------
create or replace procedure create_clan(pname in nvarchar2, founder_id in number, pheadquater in nvarchar2) is
begin
	insert into clans(clan_name, clan_level, headquater) values
		(pname, 1, pheadquater);
	insert into membership(founder, member_id, clan_name) values
		('Y', founder_id, pname);
end create_clan;

create or replace function join_clan(pname in nvarchar2, player_id in number)
    return boolean is
    cfounder membership.founder%type;
begin
    select founder
    into cfounder
    from membership
    where member_id = player_id;
    if cfounder is null then
        insert into membership(founder, member_id, clan_name)values
            ('N', player_id, pname);
        return true;
    else
        return false;
    end if;
end join_clan;

create or replace function leave_clan(player_id in number)
    return nvarchar2 is
    cfounder membership.founder%type;
    cname membership.clan_name%type;
begin
    select founder, clan_name
    into cfounder, cname
    from membership
    where member_id = player_id;
    if cfounder is null then
        return 'Player was not a clan member';
    elsif cfounder = 'Y' then
        delete from clans
        where clan_name like cname;
        return 'Clan successfully deleted';
    else
        delete from membership
        where member_id = player_id;
        return 'Clan member ejected';
    end if;
end leave_clan;

create or replace procedure delete_clan(pname in nvarchar2) is
begin
    delete from clans
    where clan_name like pname;
end delete_clan;

create or replace function get_clans 
    return sys_refcursor
    is
    clan_cursor sys_refcursor;
begin
    open clan_cursor for
        select clan_name from clans;
    return clan_cursor;
end get_clans;
