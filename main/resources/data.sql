INSERT INTO client (owner_id, client_name, business_type, location)
VALUES (1, 'Elon Musk', 'Space stuff', 'Mars');
INSERT INTO client (owner_id, client_name, business_type, location)
VALUES (1, 'Nick Diaz', 'Botany', '209');

INSERT INTO task (owner_id, task_name, client_fk, completed)
VALUES (1, 'Build rocket', 1, false);
INSERT INTO task (owner_id, task_name, client_fk, completed)
VALUES (1, 'Bore some holes', 1, false);
INSERT INTO task (owner_id, task_name, client_fk, completed)
VALUES (1, 'Nunchucks practice', 2, false);

INSERT INTO subtask (owner_id, subtask_name, category, completed)
VALUES (1, 'Check the booster', 'Mechanic', false);
INSERT INTO subtask (owner_id, subtask_name, category, completed)
VALUES (1, 'Buy new rocket fuel', 'Shopping', false);

INSERT INTO task_subtask (task_fk, subtask_fk)
VALUES (1, 1);
INSERT INTO task_subtask (task_fk, subtask_fk)
VALUES (1, 2);

INSERT INTO subtask_dependentsubtask (main_subtasktask_fk, dependent_subtask_fk)
VALUES (2, 1)