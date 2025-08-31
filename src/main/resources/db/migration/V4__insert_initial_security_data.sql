INSERT INTO roles (id, name) VALUES (1,  'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2,  'ROLE_USER');

INSERT INTO users (id, login, password) VALUES (1, 'admin', '$2a$12$G9oEh0rRFh.Y.uVFxtUwwe7AXjG2AexCKBaMwCaJl4gXJ5DGLYz7e');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);