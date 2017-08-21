insert into authorities (id, name) VALUES (1, 'ROLE_ADMIN');
insert into authorities (id, name) VALUES (2, 'ROLE_MANAGER');
insert into authorities (id, name) VALUES (3, 'ROLE_USER');

insert into users (id, username, password, email, enabled, last_pwd_rst_dt) VALUES (100, 'admin', '$2a$10$Q4./1dZmvxV1.NvAhvRBPejp/4ufGOcfF1YhgeCNHleUz7dr.SiTK', 'admin@test.com', true, '2000-01-01');
insert into user_authorities (user_id, authority_id) VALUES (100, 1);

insert into users (id, username, password, email, enabled, last_pwd_rst_dt) VALUES (101, 'manager', '$2a$10$Q4./1dZmvxV1.NvAhvRBPejp/4ufGOcfF1YhgeCNHleUz7dr.SiTK', 'manager@test.com', true, '2000-01-01');
insert into user_authorities (user_id, authority_id) VALUES (101, 2);