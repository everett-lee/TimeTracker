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

INSERT INTO subtask (owner_id, subtask_name, category, completed, task_fk)
VALUES (1, 'Check the booster', 'Mechanic', false, 1);
INSERT INTO subtask (owner_id, subtask_name, category, completed, task_fk)
VALUES (1, 'Buy new rocket fuel', 'Shopping', false, 1);
INSERT INTO subtask (owner_id, subtask_name, category, completed, task_fk)
VALUES (1, 'Practice kata', 'ninja', false, 3);

INSERT INTO subtask_dependentsubtask (main_subtasktask_fk, dependent_subtask_fk)
VALUES (2, 1);

INSERT INTO time_commit (owner_id, subtask_fk, date, time)
VALUES (1, 1, '2020-01-01', 540000);
INSERT INTO time_commit (owner_id, subtask_fk, date, time)
VALUES (1, 1, '2020-01-02', 22000);
INSERT INTO time_commit (owner_id, subtask_fk, date, time)
VALUES (1, 2, '2020-02-01', 340000);
INSERT INTO time_commit (owner_id, subtask_fk, date, time)
VALUES (1, 2, '2020-02-02', 450000);

