# game_SZBD

## Players
- **PROCEDURE create_player(pname in nvarchar2, pprof in nvarchar2, prace in nvarchar2)**
- **PROCEDURE delete_player(pid in number)**
- **FUNCTION get_players - returns a cursor with data**

## Monsters
- **PROCEDURE create_monster(pname in nvarchar2, ptype in nvarchar2, pitem in nvarchar2, prace in nvarchar2)**
- **PROCEDURE  delete_monster(pid in number)**
- **FUNCTION get_monsters - returns a cursor with data**

## Races and Professions
- **PROCEDURE create_profession(pname in nvarchar2)**
- **PROCEDURE create_race(pname in nvarchar2, ps in number, pa in number, pin in number)**
- **PROCEDURE delete_{race/profession}(pname in nvarchar2)**
- **FUNCTION get_{races/professions} - returns a cursor with data**

## Quests
- **PROCEDURE create_quest(pname in nvarchar2, pexp in number, pid in number)**
- **PROCEDURE delete_quest(pname in nvarchar2)**
- **FUNCTION get_quests - returns a cursor with data**
- **FUNCTION take_the_task(pid in number, pname in nvarchar2) - return string with information e.g. "Quest taken", "Quest is already taken", "Quest is already done"**
- **FUNCTION submit_task(pid in number, pname in nvarchar2) - return string with information e.g. "Quest accomplished", "You cannot submit this quest"**

## Items
- **PROCEDURE create_item(pname in nvarchar2, ps in number, pa in number, pin in number, pweight in number, pprof in nvarchar2)**
- **PROCEDURE delete_item(pname in nvarchar2)**
- **FUNCTION get_items - returns a cursor with data**
- **FUNCTION pick_up(pid in number, pname in nvarchar2) - returns 'Item picked', 'Your equipment will be too heavy, you can not pick that item'**
- **FUNCTION drop_item(pid in number, pname in nvarchar2) - returns 'Item dropped', 'You cannot drop item'**

## Clans
- **FUNCTION create_clan(pname in nvarchar2, founder_id in number, pheadquater in nvarchar2) - returns 'Clan created', 'You cannot create clan if you are already in one'**
- **FUNCTION join_clan(pname in nvarchar2, player_id in number) - returns 'You have joined the clan', You cannot join clan if you are already in one'**
- **FUNCTION leave_clan(player_id in number) - 'Player was not a clan member', 'Player was not a clan member'(if player was a founder), 'Clan member ejected'**
- **PROCEDURE delete_clan(pname in nvarchar2)**
- **FUNCTION get_clan_members(pname in nvarchar2) - returns a cursor with clan members (id + '. ' + name)**
- **FUNCTION get_clans - returns a cursor with data**
