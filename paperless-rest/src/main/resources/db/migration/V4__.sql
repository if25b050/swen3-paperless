ALTER TABLE documents
    ADD ai_summary VARCHAR(255);

ALTER TABLE documents
    ADD status VARCHAR(30);

UPDATE documents
SET status = 'NEW';

ALTER TABLE documents
    ALTER COLUMN status SET NOT NULL;

ALTER TABLE documents_labels
    ADD CONSTRAINT pk_documents_labels PRIMARY KEY (documents_uuid, labels_uuid);

ALTER TABLE documents
    ALTER COLUMN created_date SET NOT NULL;

ALTER TABLE documents
    ALTER COLUMN modified_date SET NOT NULL;

ALTER TABLE documents
    ALTER COLUMN version SET NOT NULL;