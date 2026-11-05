ALTER TABLE documents
    ADD COLUMN created_date TIMESTAMP;
ALTER TABLE documents
    ADD COLUMN modified_date TIMESTAMP;
ALTER TABLE documents
    ADD COLUMN version INTEGER;

ALTER TABLE labels
    ADD COLUMN created_date TIMESTAMP;
ALTER TABLE labels
    ADD COLUMN modified_date TIMESTAMP;
ALTER TABLE labels
    ADD COLUMN version INTEGER;
