SET AUTOCOMMIT = 0;
START TRANSACTION;

CREATE TABLE `battleship_feedback`
(
  `feedback_id`      int(11) NOT NULL,
  `feedback_title`   varchar(30)  DEFAULT NULL,
  `feedback_message` varchar(255) DEFAULT NULL
);

ALTER TABLE `battleship_feedback`
  ADD PRIMARY KEY (`feedback_id`);

ALTER TABLE `battleship_feedback`
  MODIFY `feedback_id` int(11) NOT NULL AUTO_INCREMENT,
  AUTO_INCREMENT = 17;
COMMIT;