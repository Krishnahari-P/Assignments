This is a training management system built using a relational database designed in MySQL.

Assumptions:

Courses & Topics: Each course covers multiple topics.

Batches: Each batch is assigned to a course and has a schedule.

Trainers: Trainers can be assigned to multiple batches (M:N).

Candidates: Candidates can join multiple batches, and their status in each batch can be In Progress, Completed, or Terminated.

Assignments: Assignments are reusable and can be assigned to multiple batches (M:N).

Submissions: Candidates submit assignments for the batches they are enrolled in. Submissions record the score and submission date.