
ALTER table user add column name VARCHAR(255) NOT NULL;

drop table if EXISTS user_document_tag;
drop table if EXISTS tag;
drop table if EXISTS document;

CREATE TABLE tag (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE KEY,
  icon_name VARCHAR(255) NOT NULL,
  created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO tag(name, icon_name) VALUES
  ('Ableism', 'ableism'),
  ('Beliefs', 'beliefs'),
  ('Gender', 'gender'),
  ('Hate', 'hate'),
  ('Islamophobia', 'islamophobia'),
  ('Mansplaining', 'mansplaining'),
  ('Privilege', 'privilege'),
  ('Racism', 'racism'),
  ('Sexisim', 'sexisim'),
  ('Sexuality', 'sexuality');

CREATE TABLE document (
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE KEY,
  url VARCHAR(1024) NOT NULL,
  created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE user_document_tag (
  user_id INT NOT NULL REFERENCES user(id) on DELETE CASCADE ,
  document_id INT NOT NULL REFERENCES document(id) on DELETE  CASCADE ,
  tag_id INT NOT NULL REFERENCES tag(id) on DELETE CASCADE ,
  created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  unique key (user_id, document_id, tag_id)
);