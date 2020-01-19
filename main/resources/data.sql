INSERT INTO client (owner_id, client_name, business_type, location)
VALUES (1, 'Elon Musk', 'Space stuff', 'Mars');
INSERT INTO client (owner_id, client_name, business_type, location)
VALUES (1, 'Nick Diaz', 'Botany', '209');

INSERT INTO task (owner_id, task_name, client_fk, completed)
VALUES (1, 'Build rocket', 1, false);
INSERT INTO task (owner_id, task_name, client_fk, completed)
VALUES (1, 'Bore some holes', 1, false);