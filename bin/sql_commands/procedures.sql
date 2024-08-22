-- PROCEDURE TO REMOVE TASK FROM DATABASE
DELIMITER $$
CREATE PROCEDURE removeTask(
    IN taskId INT,
    IN userId INT
)
BEGIN
    DECLARE taskExists INT;
    DECLARE otherUsersAssociated INT;

    SELECT COUNT(*) INTO taskExists
    FROM user_tasks
    WHERE task_id = taskId AND user_id = userId;

    IF taskExists > 0 THEN
        DELETE FROM user_tasks WHERE task_id = taskId AND user_id = userId;

		DELETE FROM tasks WHERE task_id = taskId;
        
        UPDATE users
        SET tasks_completed = tasks_completed + 1
        WHERE id = userId;

    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Task not found or not associated with the user';
    END IF;
END $$
DELIMITER ;

-- GET TOP 10 USERS
DELIMITER $$
CREATE PROCEDURE get_leaderboard(
	out entryCount INT
)
BEGIN
	SELECT count(*) INTO entryCount FROM users;
    SELECT name, tasks_completed
    FROM users
    ORDER BY tasks_completed DESC, reg_date ASC;
END$$
DELIMITER ;

-- ADD TASKS TO TABLES
DELIMITER $$
CREATE PROCEDURE `addTaskForUser`(
    IN p_userId INT,
    IN p_content VARCHAR(255),
    IN p_priorityId INT,
    IN p_dueDate DATE
)
BEGIN
    DECLARE last_task_id INT;
    
    INSERT INTO tasks (content, priority_id, due_date)
    VALUES (p_content, p_priorityId, p_dueDate);

    SET last_task_id = LAST_INSERT_ID();

    INSERT INTO user_tasks (user_id, task_id)
    VALUES (p_userId, last_task_id);
END$$
DELIMITER ;