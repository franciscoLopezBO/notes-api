-- ==========================================================
-- Roles
-- ==========================================================
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE -- E.g., ADMIN, USER
);

-- ==========================================================
-- Users
-- ==========================================================
CREATE TABLE user_account (
    id SERIAL PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE INDEX idx_user_email ON user_account(email);

-- ==========================================================
-- Tags
-- ==========================================================
CREATE TABLE tag (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uq_tag_user_name UNIQUE(user_id, name),
    CONSTRAINT fk_tag_user FOREIGN KEY (user_id) REFERENCES user_account(id)
);

CREATE INDEX idx_tag_user ON tag(user_id);

-- ==========================================================
-- Notes
-- ==========================================================
CREATE TABLE note (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    archived BOOLEAN DEFAULT FALSE,
    deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_note_user FOREIGN KEY (user_id) REFERENCES user_account(id)
);

CREATE INDEX idx_note_user ON note(user_id);
CREATE INDEX idx_note_title ON note(title);

-- ==========================================================
-- Note-Tag Relationship (Many to Many)
-- ==========================================================
CREATE TABLE note_tag (
    note_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (note_id, tag_id),
    CONSTRAINT fk_notetag_note FOREIGN KEY (note_id) REFERENCES note(id),
    CONSTRAINT fk_notetag_tag FOREIGN KEY (tag_id) REFERENCES tag(id)
);

-- ==========================================================
-- Note Versions (History)
-- ==========================================================
CREATE TABLE note_version (
    id SERIAL PRIMARY KEY,
    note_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    version_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id INT NOT NULL,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_noteversion_note FOREIGN KEY (note_id) REFERENCES note(id),
    CONSTRAINT fk_noteversion_user FOREIGN KEY (user_id) REFERENCES user_account(id)
);

CREATE INDEX idx_noteversion_note ON note_version(note_id);

-- ==========================================================
-- Saved Filters and Searches
-- ==========================================================
CREATE TABLE saved_filter (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    filter_name VARCHAR(100) NOT NULL,
    parameters JSONB NOT NULL, -- tags, text, etc.
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_filters_user FOREIGN KEY (user_id) REFERENCES user_account(id)
);

CREATE INDEX idx_filters_user ON saved_filter(user_id);

-- ==========================================================
-- Export / Import Logs
-- ==========================================================
CREATE TABLE export_log (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    format VARCHAR(20) NOT NULL, -- 'json', 'markdown'
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    file BYTEA,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_export_user FOREIGN KEY (user_id) REFERENCES user_account(id)
);

CREATE TABLE import_log (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    format VARCHAR(20) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data JSONB,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_import_user FOREIGN KEY (user_id) REFERENCES user_account(id)
);

-- Insert roles
INSERT INTO role (name) VALUES 
('ADMIN'),
('USER');

-- Insert users
INSERT INTO user_account (email, password_hash, role_id, active) VALUES
('francisco@example.com', 'hashed_password_1', 1, TRUE),
('maria@example.com', 'hashed_password_2', 2, TRUE),
('carlos@example.com', 'hashed_password_3', 2, TRUE);

-- Insert tags for users
INSERT INTO tag (user_id, name) VALUES
(1, 'Work'),
(1, 'Personal'),
(2, 'Urgent'),
(3, 'Ideas');

-- Insert notes for users
INSERT INTO note (user_id, title, content, archived, deleted) VALUES
(1, 'Team Meeting', 'Discuss progress on project X.', FALSE, FALSE),
(1, 'Shopping List', 'Milk, Bread, Eggs', FALSE, FALSE),
(2, 'Pending Tasks', 'Send reports, prepare presentation.', FALSE, FALSE),
(3, 'App Ideas', 'Task management application.', FALSE, FALSE);

-- Link notes with tags
INSERT INTO note_tag (note_id, tag_id) VALUES
(1, 1), -- Note 1 with tag Work (user 1)
(2, 2), -- Note 2 with tag Personal (user 1)
(3, 3), -- Note 3 with tag Urgent (user 2)
(4, 4); -- Note 4 with tag Ideas (user 3)

-- Insert note versions (history)
INSERT INTO note_version (note_id, title, content, user_id, deleted) VALUES
(1, 'Team Meeting', 'Discuss progress on project X.', 1, FALSE),
(1, 'Team Meeting (v2)', 'Discuss progress on project X and next steps.', 1, FALSE),
(2, 'Shopping List', 'Milk, Bread, Eggs', 1, FALSE),
(3, 'Pending Tasks', 'Send reports, prepare presentation.', 2, FALSE),
(4, 'App Ideas', 'Task management application.', 3, FALSE);

-- Insert saved filters
INSERT INTO saved_filter (user_id, filter_name, parameters, deleted) VALUES
(1, 'Work Filter', '{"tags":["Work"],"text":""}', FALSE),
(2, 'Urgent Filter', '{"tags":["Urgent"],"text":"reports"}', FALSE);

-- Insert export logs
INSERT INTO export_log (user_id, format, file, deleted) VALUES
(1, 'json', NULL, FALSE),
(2, 'markdown', NULL, FALSE);

-- Insert import logs
INSERT INTO import_log (user_id, format, data, deleted) VALUES
(1, 'json', '{"notes":[{"title":"Imported Note","content":"Imported content"}]}', FALSE),
(3, 'markdown', '{"notes":[{"title":"Another Note","content":"Markdown content"}]}', FALSE);
