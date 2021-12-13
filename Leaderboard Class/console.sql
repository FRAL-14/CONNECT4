Create Table INT_Leaderboard(
    player_id BIGSERIAL primary key ,
    player_name varchar(20) UNIQUE NOT NULL,
    last_played_at TIMESTAMP default now(),
    moves INT CONSTRAINT check_move_count check ( moves BETWEEN 0 AND 42),
    game_duration INT not null);


CREATE TABLE INT_save_game(
    player_id BIGINT REFERENCES int_leaderboard(player_id),
    player_name varchar(20) references int_leaderboard(player_name),
    row INT,
    col INT,
    moves INT REFERENCES int_leaderboard(moves),
    game_duration INT REFERENCES int_leaderboard(game_duration),
    coin_owner char(1) CHECK ( coin_owner in ('X','O') )


    --THIS IS NO WHERE NEAR FINAL PLEASE DO NOT USE



)
