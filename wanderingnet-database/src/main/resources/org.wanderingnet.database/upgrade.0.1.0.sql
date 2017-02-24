use wanderingnet;

CREATE TABLE database_version
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    release_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version_name VARCHAR(255) NOT NULL,
    script_name VARCHAR(255) NOT NULL,
    user VARCHAR(255),
    dbms_version VARCHAR(255),
    database_name VARCHAR(255),
    connection_ip VARCHAR(62),
    connection_id VARCHAR(62),
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX database_version_indx_script_name ON database_version (script_name);
CREATE UNIQUE INDEX database_version_indx_version_name ON database_version (version_name);

create table user (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password_hash CHAR(128) not null,
    password_salt char(32) not null,
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP
);
alter table user
    add CONSTRAINT user_uk
UNIQUE KEY (email);
