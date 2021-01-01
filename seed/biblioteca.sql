DROP DATABASE IF EXISTS biblioteca;
CREATE DATABASE biblioteca DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin; 
USE biblioteca;
-- Demo Library Management
-- tables 
CREATE TABLE Users (
	userID SMALLINT UNSIGNED NOT NULL auto_increment,
	userName varchar(255) NOT NULL,
    -- add salt
    userPassword varchar(255) NOT NULL,
    -- roluri: bibliotecar - 0, cititor - 1 
    role SMALLINT UNSIGNED NOT NULL, 
    PRIMARY KEY (userID)
);

CREATE TABLE Books (
	bookID INT UNSIGNED NOT NULL auto_increment,
    title varchar(255) NOT NULL,
    author varchar(255) NOT NULL,
    stock INT UNSIGNED NOT NULL default 0,
	PRIMARY KEY (bookID)
);

CREATE TABLE Records (
	recordID INT UNSIGNED NOT NULL auto_increment,
    -- record by
    bookingRequestBy SMALLINT UNSIGNED NOT NULL,
    bookID INT UNSIGNED NOT NULL,
    -- 0 -returned, 1 -booking request, 2 -borrowed
    state SMALLINT UNSIGNED NOT NULL default 0,
    date DATETIME NOT NULL DEFAULT NOW(),
	PRIMARY KEY (recordID),
    FOREIGN KEY (bookID) REFERENCES Books(bookID),
    FOREIGN KEY (bookingRequestBy) REFERENCES Users(userID)
);

CREATE TABLE Reviews (
	reviewID INT UNSIGNED NOT NULL auto_increment,
    userID SMALLINT UNSIGNED NOT NULL,
    bookID INT UNSIGNED NOT NULL,
    review TEXT NOT NULL,
	PRIMARY KEY (reviewID),
    FOREIGN KEY (userID) REFERENCES Users(userID),
    FOREIGN KEY (bookID) REFERENCES Books(bookID)
);

-- seed data 
INSERT INTO Users VALUES 
(NULL, "bibliotecar", "bibliotecar", 0),  
(NULL, "cititor", "cititor", 1);

INSERT INTO Books VALUES
(NULL, "In search of lost time", "Marcel Proust", 1),
(NULL, "Ulysses", "James Joyce", 1),
(NULL, "Don Quixote", "Miguel de Cervantes", 1),
(NULL, "One hundred years of solitude", "Gabriel Garcia Marquez", 3),
(NULL, "The great Gatsby", "F. Scott Fitzgerald", 1),
(NULL, "Moby Dick", "Herman Melville", 2),
(NULL, "War and peace", "Lev Tolstoy", 1),
(NULL, "Hamlet", "William Shakespeare", 1),
(NULL, "The Odyssey", "Homer", 0);

INSERT INTO Records VALUES
(NULL, 2, 1, 1, NOW());

INSERT INTO Reviews VALUES
(NULL, 2, 2, "A major achievement in 20th century literature.");