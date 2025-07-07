-- Inserting values into courses table
INSERT INTO courses (id, name) VALUES (1, 'Python');
INSERT INTO courses (id, name) VALUES (2, 'Java');
INSERT INTO courses (id, name) VALUES (3, 'SQL');
INSERT INTO courses (id, name) VALUES (4, 'Full stack web development');
INSERT INTO courses (id, name) VALUES (5, 'Data science with python');

-- Inserting values into topics table
INSERT INTO topics (id, name, course_id) VALUES (1, 'HTML & CSS Basics', 4);
INSERT INTO topics (id, name, course_id) VALUES (2, 'Pandas and NumPy', 5);
INSERT INTO topics (id, name, course_id) VALUES (3, 'Java basics', 2);
INSERT INTO topics (id, name, course_id) VALUES (4, 'Python basics', 1);

-- Inserting values into trainers table
INSERT INTO trainers (id, name) VALUES (1, 'Alice');
INSERT INTO trainers (id, name) VALUES (2, 'Smith');
INSERT INTO trainers (id, name) VALUES (3, 'Freddy');
INSERT INTO trainers (id, name) VALUES (4, 'Johan');
INSERT INTO trainers (id, name) VALUES (5, 'Michael');

-- Inserting values into candidates table
INSERT INTO candidates (id, name, email, phone_no) VALUES (1, 'John', 'john@example.com', '2222222222');
INSERT INTO candidates (id, name, email, phone_no) VALUES (2, 'Jane', 'jane@example.com', '1111111111');
INSERT INTO candidates (id, name, email, phone_no) VALUES (3, 'Jude', 'jude@example.com', '3333333333');
INSERT INTO candidates (id, name, email, phone_no) VALUES (4, 'Jack', 'jack@example.com', '4444444444');
INSERT INTO candidates (id, name, email, phone_no) VALUES (5, 'Johny','joh@example.com', '5555555555');

-- Inserting values into batches
INSERT INTO batches (batch_code, start_date, end_date, course_id) VALUES (101, '2025-08-01', '2025-10-30', 1);
INSERT INTO batches (batch_code, start_date, end_date, course_id) VALUES (102, '2025-09-15', '2025-12-15', 2);
INSERT INTO batches (batch_code, start_date, end_date, course_id) VALUES (103, '2025-09-17', '2025-11-11', 3);
INSERT INTO batches (batch_code, start_date, end_date, course_id) VALUES (104, '2025-10-01', '2025-12-01', 5);

-- Inserting into joins table
INSERT INTO joins (batch_id, candidate_id, status) VALUES (101, 1, 'In Progress');
INSERT INTO joins (batch_id, candidate_id, status) VALUES (102, 2, 'Completed');
INSERT INTO joins (batch_id, candidate_id, status) VALUES (103, 4, 'Terminated');
INSERT INTO joins (batch_id, candidate_id, status) VALUES (102, 3, 'Completed');

-- Inserting into assignments
INSERT INTO assignments (id, title, description, due_date) VALUES (1, 'HTML Assignment', 'Build a webpage', '2025-08-15');
INSERT INTO assignments (id, title, description, due_date) VALUES (2, 'Pygame', 'Create a pygame', '2025-07-07');
INSERT INTO assignments (id, title, description, due_date) VALUES (3, 'Python Data Task', 'Clean a dataset using Pandas', '2025-09-30');

-- Insert into teaches
INSERT INTO teaches (batch_id, trainer_id) VALUES (101, 1);
INSERT INTO teaches (batch_id, trainer_id) VALUES (102, 2);
INSERT INTO teaches (batch_id, trainer_id) VALUES (103, 3);
INSERT INTO teaches (batch_id, trainer_id) VALUES (104, 4);
INSERT INTO teaches (batch_id, trainer_id) VALUES (102, 4);

-- Insert into submission
INSERT INTO submission (candidate_id, batch_id, assignment_id, submission_date, score) VALUES (1, 101, 1, '2025-08-14', 0);
INSERT INTO submission (candidate_id, batch_id, assignment_id, submission_date, score) VALUES (2, 102, 2, '2025-09-28', 90);
INSERT INTO submission (candidate_id, batch_id, assignment_id, submission_date, score) VALUES (3, 104, 3, '2025-09-28', 100);

