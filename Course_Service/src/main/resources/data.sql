-- ==========================
-- INSTRUCTORS
-- ==========================
INSERT INTO instructor (id, name, expertise)
VALUES
    (1, 'Dr. Alice Johnson', 'Data Science'),
    (2, 'Prof. Bob Smith', 'Machine Learning'),
    (3, 'Dr. Carol White', 'Web Development');

-- ==========================
-- COURSES
-- ==========================
INSERT INTO course (id, title, description, rating, instructor_id)
VALUES
    (1, 'Python for Data Science', 'Learn Python and data analysis tools', 4.7, 1),
    (2, 'Machine Learning Basics', 'Introduction to ML algorithms', 4.5, 2),
    (3, 'Advanced React', 'Master React hooks and state management', 4.8, 3),
    (4, 'Deep Learning with TensorFlow', 'Neural networks and CNNs', 4.9, 2),
    (5, 'Full Stack Development', 'Frontend + Backend development concepts', 4.6, 3);

-- ==========================
-- STUDENTS
-- ==========================
INSERT INTO student (id, name, email)
VALUES
    (1, 'John Doe', 'john@example.com'),
    (2, 'Jane Roe', 'jane@example.com'),
    (3, 'Sam Lee', 'sam@example.com'),
    (4, 'Nina Patel', 'nina@example.com');

-- ==========================
-- ENROLLMENTS
-- ==========================
INSERT INTO enrollment (id, student_id, course_id, progress)
VALUES
    (1, 1, 1, 100.0),   -- John completed Python for Data Science
    (2, 1, 2, 60.0),   -- John is halfway through ML Basics
    (3, 2, 1, 100.0),   -- Jane completed Python for Data Science
    (4, 2, 4, 100.0),   -- Jane completed Deep Learning
    (5, 3, 3, 75.0),   -- Sam working on Advanced React
    (6, 4, 5, 50.0);   -- Nina is halfway through Full Stack
