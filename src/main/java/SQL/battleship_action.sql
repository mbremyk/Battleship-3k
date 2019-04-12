SET AUTOCOMMIT = 0;
START TRANSACTION;

CREATE TABLE `battleship_action`
(
  `game_id`     int(11) NOT NULL,
  `move_id`     int(11) NOT NULL DEFAULT '0',
  `user_id`     int(11) NOT NULL,
  `coordinates` varchar(30)      DEFAULT NULL
);

ALTER TABLE `battleship_action`
  ADD PRIMARY KEY (`game_id`, `move_id`);
COMMIT;
