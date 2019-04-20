SET AUTOCOMMIT = 0;
START TRANSACTION;

CREATE TABLE `BattleshipUser`
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

ALTER TABLE `BattleshipUser`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `user_id` (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

ALTER TABLE `BattleshipUser`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT,
  AUTO_INCREMENT = 1;
COMMIT;
