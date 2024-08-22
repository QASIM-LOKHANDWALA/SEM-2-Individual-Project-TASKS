DELIMITER $$
CREATE TRIGGER `before_insert_user_email_check`
BEFORE INSERT ON `users`
FOR EACH ROW
BEGIN
    IF NEW.email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$' THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Invalid email format. Please enter a valid email address.';
    END IF;
END$$
DELIMITER ;