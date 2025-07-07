CREATE TABLE courses (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));

  CREATE TABLE topics (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  course_id INT,
  PRIMARY KEY (id),
  FOREIGN KEY (course_id)
  REFERENCES courses (id)
  ON DELETE SET NULL
  ON UPDATE NO ACTION);

  CREATE TABLE trainers (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));

  CREATE TABLE batches (
  batch_code INT NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  course_id INT NULL,
  PRIMARY KEY (batch_code),
  FOREIGN KEY (course_id)
  REFERENCES courses (id)
  ON DELETE SET NULL
  ON UPDATE NO ACTION);

  CREATE TABLE candidates (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  email VARCHAR(45) NULL,
  phone_no CHAR(10) NOT NULL,
  PRIMARY KEY (id));

  CREATE TABLE assignments (
  id INT NOT NULL,
  title VARCHAR(45) NOT NULL,
  description VARCHAR(45) NOT NULL,
  due_date DATE,
  PRIMARY KEY (id));

  CREATE TABLE joins (
  batch_id INT NOT NULL,
  candidate_id INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  PRIMARY KEY (batch_id, candidate_id),
  FOREIGN KEY (batch_id)
  REFERENCES batches (batch_code),
  FOREIGN KEY (candidate_id)
  REFERENCES candidates(id));

  CREATE TABLE submission (
  candidate_id INT NOT NULL,
  batch_id INT NOT NULL,
  assignment_id INT NOT NULL,
  submission_date DATE,
  score INT,
  PRIMARY KEY (candidate_id,batch_id,assignment_id),
  FOREIGN KEY (candidate_id)
  REFERENCES candidates (id),
  FOREIGN KEY (batch_id)
  REFERENCES batches (batch_code),
  FOREIGN KEY (assignment_id)
  REFERENCES assignments (id));
