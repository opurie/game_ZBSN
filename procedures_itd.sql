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
        select player_id || '. '||player_name || ' - lvl '|| player_level
        from players
        order by player_id;
    return player_cursor;
end get_players;

create or replace procedure create_monster(pname in nvarchar2,  pitem in nvarchar2, prace in nvarchar2) is
begin
	insert into monsters(monster_id, monster_name,  owned_item, monster_race) values
		(monster_seq.nextval, pname, pitem, prace);
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
    insert into performances(done, player_id, quest_name) values
        ('Y', pid, pname);
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

create or replace function get_player_quests(pid in number)
    return sys_refcursor is
    quest_cursor sys_refcursor;
begin
    open quest_cursor for
        select quest_name ||' -> '||done
        from performances
        where player_id = pid;
    return quest_cursor;
end get_player_quests;

create or replace function take_the_task(pid in number, pname in nvarchar2)
    return nvarchar2 is
	buf performances.done%type;
begin
	select done
	into buf
	from performances
	where player_id = pid and quest_name = pname;
	
    if buf = 'N' then
		return 'Quest is already taken';
	elsif buf = 'Y' then
		return 'Quest is already done';
	end if;
    exception
        when no_data_found then
        insert into performances(done, player_id, quest_name) values
                                    ('N', pid, pname);
        return 'Quest taken';
        
end take_the_task;

create or replace function submit_task(pid in number, pname in nvarchar2)
    return nvarchar2 is
    buf performances.done%type;
begin
    select done
    into buf
    from performances
    where player_id = pid and quest_name like pname;
    if buf like 'N' then
        update performances
        set done = 'Y'
        where player_id = pid and quest_name like pname;
        update players p 
        set p.player_level = p.player_level + (select experience_points
                                                from quests
                                                where q_name like pname)
        where p.player_id = pid;
        return 'Quest accomplished';
    else
        return 'You cannot submit this quest';
    end if;
    exception
        when no_data_found then
        return 'You cannot submit this quest';
end submit_task;
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

create or replace function get_equipment(pid in number)
    return sys_refcursor
    is
    item_cursor sys_refcursor;
begin
    open item_cursor for
        select i.i_name ||' - '||i.weight||' - '||i.profession||' - '||o.number_of
        from items i inner join items_ownership o 
        on i.i_name = o.item_name
        where o.equipment_id = pid;
    return item_cursor;
end get_equipment;



create or replace function pick_up(pid in number, pname in nvarchar2)
    return nvarchar2 is
	i items_ownership.number_of%type;
    eweight number;
    iweight number;
    fweight number;
begin
    begin
        select nvl(number_of, 0)
        into i
        from items_ownership
        where equipment_id = pid and item_name = pname;
        exception
        when no_data_found then i := null;
    end;
    begin
        select nvl(weight,0)
        into iweight
        from items
        where i_name like pname;
        exception
        when no_data_found then iweight := 0;
    end;    
    begin
        select nvl(capacity_eq,0)
        into eweight
        from equipments
        where owner_id = pid;
        exception
        when no_data_found then eweight := 0;
    end;    
    begin
        select sum(nvl(o.number_of,0) * (select nvl(weight,0)
                                    from items
                                    where o.item_name = i_name))
        into fweight
        from items_ownership o
        where o.equipment_id = pid
        group by o.equipment_id;
         exception
        when no_data_found then fweight := 0;
    end;
    if iweight + fweight > eweight then
        return 'Your equipment will be too heavy, you can not pick that item';
    else
        if i is null then
            insert into items_ownership(number_of, equipment_id, item_name) values
                                        (1, pid, pname);
            return 'Item picked';
        elsif i>=0 then
            update items_ownership
            set number_of = number_of + 1
            where equipment_id = pid and item_name = pname;
            return 'Item picked';
        end if;
    end if;
end pick_up;

select * from items_ownership;

create or replace function drop_item(pid in number, pname in nvarchar2) 
    return nvarchar2 is
	i items_ownership.number_of%type;
begin
    begin
        select nvl(number_of,0)
        into i
        from items_ownership
        where equipment_id = pid and item_name = pname;
         exception
        when no_data_found then i := 0;
    end;	
	if i>0 then
		update items_ownership
		set number_of = number_of - 1
		where equipment_id = pid and item_name = pname;
        return 'Item dropped';
    else
		return 'You cannot drop that item';
	end if;
end drop_item;
-----CLANS, JOIN, LEAVE-------------------------------------------------------------------------------------------------
create or replace function create_clan(pname in nvarchar2, founder_id in number, pheadquater in nvarchar2) 
    return nvarchar2 is
    buf membership.founder%type;
begin
    select m.founder
    into buf
    from membership m
    where m.member_id = founder_id;
    return  'You cannot create clan if you are already in one';
    exception
    when no_data_found then
        insert into clans(clan_id, clan_name, clan_level, headquater) values
            (clan_seq.nextval, pname, 1, pheadquater);
        insert into membership(founder, member_id, clan_id) values
            ('Y', founder_id, clan_seq.currval);
        return 'Clan created'; 
--    when others then
    
end create_clan;

create or replace function join_clan(clan_id in number, player_id in number)
    return nvarchar2 is
    cfounder membership.founder%type;
begin
    select founder
    into cfounder
    from membership
    where member_id = player_id;
    return  'You cannot join clan if you are already in one';
    exception
    when no_data_found then
    insert into membership(founder, member_id, clan_id)values
            ('N', player_id, clan_id);
        return 'You have joined the clan';
end join_clan;

create or replace function leave_clan(player_id in number)
    return nvarchar2 is
    cfounder membership.founder%type;
    cid membership.clan_id%type;
begin
    select founder, clan_id
    into cfounder, cid
    from membership
    where member_id = player_id;
    if cfounder = 'Y' then
        delete from clans
        where clan_id like cid;
        return 'Clan successfully deleted';
    else
        delete from membership
        where member_id = player_id;
        return 'Clan member ejected';
    end if;
    exception
    when no_data_found then
    return 'Player was not a clan member';
    if cfounder = 'Y' then
        delete from clans
        where clan_id = cid;
        return 'Clan successfully deleted';
    else
        delete from membership
        where member_id = player_id;
        return 'Clan member ejected';
    end if;
end leave_clan;

create or replace procedure delete_clan(pid in number) is
begin
    delete from clans
    where clan_id=pid;
end delete_clan;

create or replace function get_clans 
    return sys_refcursor
    is
    clan_cursor sys_refcursor;
begin
    open clan_cursor for
        select clan_id || '. '||clan_name from clans;
    return clan_cursor;
end get_clans;


create or replace function get_clan_members(pid in number)
    return sys_refcursor
    is
    clan_cursor sys_refcursor;
begin
    open clan_cursor for
        select p.player_id || '. '||p.player_name || ' - ' || m.founder || ' - lvl '|| p.player_level
        from players p inner join membership m 
        on m.member_id = p.player_id
        where m.clan_id = pid
        order by m.founder desc;
    return clan_cursor;
end get_clan_members;
select * from membership;
