------------PLAYERS, MONSTERS, RACE, PROFESSIONS---------------------------------------------------------------
create or replace procedure create_player(pname in nvarchar2, pprof in nvarchar2, prace in nvarchar2) is ----add create_eq
begin
	insert into players(player_id, player_name, player_level, player_profession, player_race) values
		(players_seq.currval, pname, 1, pprof, prace);
		
	insert into equipments(capacity_eq, owner_id) values (50, players_seq.nextval);
end create_player;

create or replace procedure delete_player(pid in number) is
begin
    delete from players
    where player_id = pid;
    
    delete from equipments
    where owner_id = pid;
end delete_player;

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
	insert into race (r_name, strength, agility, intellect) values
		(pname, ps, pa, pin);
end create_race;
create or replace procedure delete_race(pname in nvarchar2) is
begin
    delete from race
    where r_name like pname;
end delete_race;

create or replace function get_races 
    return sys_refcursor
    is
    race_cursor sys_refcursor;
begin
    open race_cursor for
        select r_name from race;
    return race_cursor;
end get_races;
delete from race
where r_name = 'USUWANY';commit;
------------QUESTS, TAKE_THE_TASK -------------------------------------------------------------------------------------
create or replace procedure create_quest(pname in nvarchar2, pexp in number, pid in number) is
begin
	insert into quests(q_name, experience_points, creator_id) values
		(pname, pexp, pid);
end create_quest;

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
		return 'You cant drop item that doesnt exists';
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

select *from race;