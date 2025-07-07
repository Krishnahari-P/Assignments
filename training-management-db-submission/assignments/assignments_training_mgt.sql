-- Get all batches a candidate is enrolled in, with their status.

select b.batch_code,b.start_date,b.end_date,b.course_id,j.candidate_id,j.status
from batches b join joins j
on b.batch_code=j.batch_id
where j.candidate_id=1;

-- Get all trainers assigned to a batch. 

select t.id,t.name,teaches.batch_id
from trainers t join teaches
on t.id=teaches.trainer_id
where teaches.batch_id=102;

-- Get all topics under a course.

select c.id as courseID,c.name as courseName,t.id as topicID,t.name as topicName
from courses c join topics t
on t.course_id=c.id
where c.id=1;

-- List assignment scores for a candidate in a batch. 

select c.name,s.score,s.batch_id,a.title,a.description
from submission s join candidates c join batches b join assignments a
on s.candidate_id=c.id and s.batch_id=b.batch_code and s.assignment_id=a.id
where c.id=2;

-- List candidates with status "Completed" in a given batch

select c.name,b.batch_code,j.status
from joins j join candidates c join batches b
on j.candidate_id=c.id and j.batch_id=b.batch_code
where j.status='Completed' and b.batch_code=102;