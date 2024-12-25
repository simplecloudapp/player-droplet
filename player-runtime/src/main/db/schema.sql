CREATE TABLE IF NOT EXISTS offline_players
(
    unique_id
    varchar
    PRIMARY
    KEY,
    name
    varchar
    NOT
    NULL,
    display_name
    varchar,
    first_login
    timestamp
    NOT
    NULL,
    last_login
    timestamp
    NOT
    NULL,
    online_time
    bigint
    NOT
    NULL
);

CREATE TABLE IF NOT EXISTS player_connection
(
    unique_id
    varchar
    PRIMARY
    KEY,
    client_language
    varchar
    NOT
    NULL,
    numerical_client_version
    INT
    NOT
    NULL,
    online_mode
    boolean
    NOT
    NULL,
    last_server
    varchar
    NOT
    NULL,
    online
    boolean
    NOT
    NULL,
    CONSTRAINT
    fk_player
    FOREIGN
    KEY
(
    unique_id
)
    REFERENCES offline_players
(
    unique_id
)
    );