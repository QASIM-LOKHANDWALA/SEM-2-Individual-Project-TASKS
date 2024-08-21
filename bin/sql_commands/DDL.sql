CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `reg_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `tasks_completed` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
);

CREATE TABLE `priority_levels` (
  `priority_id` int NOT NULL AUTO_INCREMENT,
  `priority_name` varchar(50) NOT NULL,
  PRIMARY KEY (`priority_id`),
  UNIQUE KEY `priority_name_UNIQUE` (`priority_name`)
);

CREATE TABLE `tasks` (
  `task_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(10000) NOT NULL,
  `upload_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `priority_id` int DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  CONSTRAINT `fk_tasks_priority` FOREIGN KEY (`priority_id`) REFERENCES `priority_levels` (`priority_id`)
);

CREATE TABLE `user_tasks` (
  `user_id` int NOT NULL,
  `task_id` int NOT NULL,
  PRIMARY KEY (`user_id`, `task_id`),
  CONSTRAINT `fk_user_tasks_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_tasks_tasks` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`) ON DELETE CASCADE
);