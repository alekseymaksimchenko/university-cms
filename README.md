# university-cms
A content management system that manages users and schedule for university

User administration flow:
Users logged in with an Admin role should be able to
- create/read/update/delete
- assign/reassign
Users, Courses, Groups, Students, Teachers, and Lectures.

Users logged as Teacher role
- create/read/update
- assign/reassign
Courses, Groups, Students, Teachers, and Lectures.

The user logged  logged as Student role
- read
Courses, Groups, Students, Teachers, and Lectures.

Anonymous User (not logged)
- view welcome page
- read Courses

#In case of app start-up issues
1) Db should have university schema
2) As minimum one User with an Admin role should be placed in DB to add other users
3) Sample data can be found in data.sql file.
