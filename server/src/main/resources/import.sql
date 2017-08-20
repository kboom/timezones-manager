insert into authorities (id, name) VALUES (1, 'ADMIN');
insert into authorities (id, name) VALUES (2, 'MANAGER');
insert into authorities (id, name) VALUES (3, 'USER');

insert into users (id, username, password, email, enabled, last_pwd_rst_dt) VALUES (100, 'admin', '$2a$10$yaMrsbcEIaXiSCvDEi9tDuzKGpxeilubkJJ2bXa/ZMX6iBDeS6EFq', 'admin@test.com', true, '1970-01-01');
insert into user_authorities (user_id, authority_id) VALUES (100, 1);