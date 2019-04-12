SET AUTOCOMMIT = 0;
START TRANSACTION;

CREATE TABLE `battleship_game`
(
  `game_id`   int(11) NOT NULL,
  `name`      varchar(30) DEFAULT NULL,
  `host_id`   int(11)     DEFAULT NULL,
  `join_id`   int(11)     DEFAULT NULL,
  `winner_id` int(11)     DEFAULT NULL
);

ALTER TABLE `battleship_game`
  ADD PRIMARY KEY (`game_id`);

ALTER TABLE `battleship_game`
  MODIFY `game_id` int(11) NOT NULL AUTO_INCREMENT,
  AUTO_INCREMENT = 1;
COMMIT;
