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
