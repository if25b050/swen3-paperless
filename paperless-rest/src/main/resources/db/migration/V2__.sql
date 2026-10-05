ALTER TABLE documents
    DROP COLUMN file_url;

ALTER TABLE documents
    ALTER COLUMN name SET NOT NULL;