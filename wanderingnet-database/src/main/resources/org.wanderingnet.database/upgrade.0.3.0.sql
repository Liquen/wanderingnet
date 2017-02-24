
CREATE TABLE map(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE,
  name_key VARCHAR(255) NOT NULL UNIQUE ,
  human_name VARCHAR(255) NOT NULL,
  map_file_name VARCHAR(2047) NOT NULL,
  size INT NOT NULL DEFAULT 0,
  created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO map(name, name_key, human_name, map_file_name, size) VALUES
  ('EN_CATEGORY_CHAT_WEBSITES','en-category-chat-websites', 'Chat Websites', 'en-category-chat-websites.json',21),
  ('EN_CATEGORY_CULTURE_BOUND_SYNDROMES','en-category-culture-bound-syndromes', 'Culture-bound syndromes', 'en-category-culture-bound-syndromes.json',126),
  ('EN_CATEGORY_FRIENDSHIP','en-category-friendship', 'Friendship', 'en-category-friendship.json',598),
  ('EN_CATEGORY_INTERNET_TROLLING','en-category-internet-trolling', 'Internet trolling', 'en-category-internet-trolling.json',46),
  ('EN_CATEGORY_MEN_AND_FEMINISM','en-category-men-and-feminism', 'Men and Feminism', 'en-category-men-and-feminism.json',260),
  ('EN_CATEGORY_ONLINE_CHAT','en-category-online-chat', 'Online chat', 'en-category-online-chat.json',274),
  ('EN_CATEGORY_SEXUALITY_AND_COMPUTERS','en-category-sexuality-and-computers', 'Sexuality and computers', 'en-category-sexuality-and-computers.json',237);