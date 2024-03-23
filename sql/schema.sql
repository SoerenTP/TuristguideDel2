CREATE DATABASE IF NOT EXISTS tourist_guide;
USE tourist_guide;

CREATE TABLE attraction (
    attraction_id INT AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500) NOT NULL,
    city VARCHAR(500) NOT NULL,
    PRIMARY KEY (attraction_id)
);

CREATE TABLE tag (
    tag_id INT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (tag_id)
);

CREATE TABLE attraction_tags (
    attraction_id INT NOT NULL,
    tag_id INT NOT NULL,
    FOREIGN KEY (attraction_id) REFERENCES attraction(attraction_id),
    FOREIGN KEY (tag_id) REFERENCES tag(tag_id)
);
INSERT INTO attraction (name, description, city) VALUES ('Test Attraction', 'Test Description', 'Test City');

INSERT INTO attraction_tags (attraction_id, tag_id)
SELECT a.attraction_id, t.tag_id
FROM attraction a
         CROSS JOIN tag t
WHERE a.name = 'Test Attraction' AND t.name IN ('PAID', 'FAMILY_FRIENDLY');