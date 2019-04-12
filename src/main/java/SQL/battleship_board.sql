SET AUTOCOMMIT = 0;
START TRANSACTION;

CREATE TABLE `battleship_board`
(
  `game_id`     int(11) NOT NULL,
  `user_id`     int(11) NOT NULL,
  `coordinates` varchar(200) DEFAULT NULL
);

ALTER TABLE `battleship_board`
  ADD PRIMARY KEY (`game_id`, `user_id`);
COMMIT;
