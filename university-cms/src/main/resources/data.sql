insert into university.users values (1,'$2a$12$gJY5BTg0J8m8xd9Zu1SBE.frrmMCRtt8h7pMV.Iu5PB1yoRkqnZnS', 'Admin');
insert into university.roles values (1,'ROLE_ADMIN');
insert into university.user_role values (1, 1);

insert into university.teachers Values (1, 'James', 'Galakher'), (2, 'Connor', 'Johnson'), (3, 'Callum', 'Williams'), (4, 'Jacob', 'Brown'), (5, 'Kyle', 'Jones');
insert into university.courses Values (1, 'MATH', 1), (2, 'ACCOUNTING', 2), (3, 'BIOLOGY', 3), (4, 'FINANCE', 4), (5, 'SCIENCE', 5);
insert into university.groups Values (1, 'QW-1'), (2, 'QW-2'), (3, 'QW-3'), (4, 'QW-4'), (5, 'QW-5');
insert into university.students Values (1, 'Joe', 'Trebiany', 1), (2, 'Reece', 'Perez', 2), (3, 'Charlie', 'White', 3), (4, 'Ethan', 'Harris', 4), (5, 'William', 'Sanchez', 5);