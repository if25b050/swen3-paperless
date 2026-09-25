CREATE TABLE documents
(
    uuid     UUID default gen_random_uuid(),
    name     VARCHAR(255),
    file_url VARCHAR(255),
    CONSTRAINT pk_documents PRIMARY KEY (uuid)
);

CREATE TABLE documents_labels
(
    documents_uuid UUID NOT NULL,
    labels_uuid    UUID NOT NULL
);

CREATE TABLE labels
(
    uuid UUID default gen_random_uuid(),
    name VARCHAR(255),
    CONSTRAINT pk_labels PRIMARY KEY (uuid)
);

ALTER TABLE labels
    ADD CONSTRAINT uc_labels_name UNIQUE (name);

ALTER TABLE documents_labels
    ADD CONSTRAINT fk_docentlab_on_documents FOREIGN KEY (documents_uuid) REFERENCES documents (uuid);

ALTER TABLE documents_labels
    ADD CONSTRAINT fk_docentlab_on_labels FOREIGN KEY (labels_uuid) REFERENCES labels (uuid);
