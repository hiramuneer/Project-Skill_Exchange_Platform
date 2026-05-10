show databases;
-- create database skill_exchange_platform;

USE skill_exchange_platform;

-- CREATE TABLE users (
--     user_id INT PRIMARY KEY AUTO_INCREMENT,
--     full_name VARCHAR(100) NOT NULL,
--     email VARCHAR(100) NOT NULL UNIQUE,
--     password VARCHAR(100) NOT NULL,
--     department VARCHAR(100),
--     semester INT
-- );

-- CREATE TABLE skills (
--     skill_id INT PRIMARY KEY AUTO_INCREMENT,
--     skill_name VARCHAR(100) NOT NULL UNIQUE
-- );

-- CREATE TABLE teach_skills (
--     teach_id INT PRIMARY KEY AUTO_INCREMENT,
--     user_id INT NOT NULL,
--     skill_id INT NOT NULL,
--     UNIQUE(user_id, skill_id),
--     FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
--     FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE
-- );

-- CREATE TABLE learn_skills (
--     learn_id INT PRIMARY KEY AUTO_INCREMENT,
--     user_id INT NOT NULL,
--     skill_id INT NOT NULL,
--     UNIQUE(user_id, skill_id),
--     FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
--     FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE
-- );

-- INSERT INTO users (user_id, full_name, email, password, department, semester)
-- VALUES
-- (1, 'Hira Ghoto', 'hira@gmail.com', 'hiraghoto', 'CS', 3),

-- (2, 'Sara Ahmed', 'sara@gmail.com', 'saraahmed', 'SE', 4),

-- (3, 'Ahmed Raza', 'ahmed@gmail.com', 'ahmedraza', 'AI', 2),

-- (5, 'Iqra Ghoto', 'iqra@gmail.com', 'iqraghoto', NULL, NULL),

-- (6, 'Sumera Ghoto', 'sumera@gmail.com', 'sumeraghoto', NULL, NULL);



-- INSERT INTO skills (skill_name)
-- VALUES
-- ('Java'),
-- ('Python'),
-- ('SQL'),
-- ('Photoshop'),
-- ('Graphic Design'),
-- ('C++'),
-- ('HTML/CSS'),
-- ('JavaScript');

select * from users;
select * from skills;