create table if not exists users(
    id BIGINT AUTO_INCREMENT primary key ,
    nom VARCHAR(100) not null,
    descr VARCHAR(255),
    email VARCHAR(100) not null unique,
    passwd VARCHAR(100) not null,
    image_path VARCHAR(500) NULL,
    ultimAcces TIMESTAMP null,
    dataCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dataUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);