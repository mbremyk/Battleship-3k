SET AUTOCOMMIT = 0;
START TRANSACTION;

CREATE TABLE IF NOT EXISTS `BattleshipUser`
(
  `user_id`     int(11)                                             NOT NULL,
  `username`    varchar(20) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `password`    varbinary(128)                                               DEFAULT NULL,
  `won_games`   int(11)                                             NOT NULL DEFAULT '0',
  `lost_games`  int(11)                                             NOT NULL DEFAULT '0',
  `email`       varchar(50)                                                  DEFAULT NULL,
  `salt`        varbinary(64)                                                DEFAULT NULL,
  `total_games` int(11) GENERATED ALWAYS AS ((`won_games` + `lost_games`)) VIRTUAL,
  `ratio`       double GENERATED ALWAYS AS (if(((`won_games` + `lost_games`) > 0),
                                               (`won_games` / (`won_games` + `lost_games`)), NULL)) VIRTUAL,
  `logged_in`   tinyint(1)                                                   DEFAULT '0'
);

CREATE TABLE IF NOT EXISTS `battleship_game`
(
  `game_id`   int(11) NOT NULL,
  `name`      varchar(30) DEFAULT NULL,
  `host_id`   int(11)     DEFAULT NULL,
  `join_id`   int(11)     DEFAULT NULL,
  `winner_id` int(11)     DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `battleship_board`
(
  `game_id`     int(11) NOT NULL,
  `user_id`     int(11) NOT NULL,
  `coordinates` varchar(200) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `battleship_action`
(
  `game_id`     int(11) NOT NULL,
  `move_id`     int(11) NOT NULL DEFAULT '0',
  `user_id`     int(11) NOT NULL,
  `coordinates` varchar(30)      DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `battleship_feedback`
(
  `feedback_id`      int(11) NOT NULL,
  `feedback_title`   varchar(30)  DEFAULT NULL,
  `feedback_message` varchar(255) DEFAULT NULL
);

ALTER TABLE `BattleshipUser`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `user_id` (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

ALTER TABLE `BattleshipUser`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT,
  AUTO_INCREMENT = 1;

ALTER TABLE `battleship_game`
  ADD PRIMARY KEY (`game_id`);

ALTER TABLE `battleship_game`
  MODIFY `game_id` int(11) NOT NULL AUTO_INCREMENT,
  AUTO_INCREMENT = 1;

ALTER TABLE `battleship_board`
  ADD PRIMARY KEY (`game_id`, `user_id`);

ALTER TABLE `battleship_action`
  ADD PRIMARY KEY (`game_id`, `move_id`);

ALTER TABLE `battleship_feedback`
  ADD PRIMARY KEY (`feedback_id`);

ALTER TABLE `battleship_feedback`
  MODIFY `feedback_id` int(11) NOT NULL AUTO_INCREMENT,
  AUTO_INCREMENT = 1;

COMMIT;
SET AUTOCOMMIT = 1;