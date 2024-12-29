USE FShark;
GO
DROP DATABASE FShark_V2;
GO
CREATE DATABASE FShark_V2;
go
USE FShark_V2;
go
CREATE TABLE TYPES (
                       ID INT IDENTITY PRIMARY KEY,
                       TYLE NVARCHAR(100)
);
go

GO
CREATE TABLE USERROLES (
                           ID INT IDENTITY PRIMARY KEY,
                           ROLE NVARCHAR(100)
);
GO
CREATE TABLE USERS (
                       USERNAME VARCHAR(200) PRIMARY KEY,
                       ROLES INT,
                       PASSWORD VARCHAR(255),
                       ACTIVE BIT,
                       EMAIL VARCHAR(200),
                       GENDER BIT,
                       LASTNAME NVARCHAR(100),
                       FIRSTNAME NVARCHAR(100),
                       BIRTHDAY DATE,
                       BIO  NVARCHAR(500),
                       HOMETOWN NVARCHAR(200),
                       CURRENCY NVARCHAR(100),
                       FOREIGN KEY (ROLES) REFERENCES USERROLES(ID),
);
GO
CREATE TABLE POSTS (
                       ID INT IDENTITY PRIMARY KEY,
                       USERNAME VARCHAR(200),
                       CONTENT NVARCHAR(200),
                       CREATEDATE DATETIME,
                       STATUS BIT,
                       FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME)
);
GO
CREATE TABLE COMMENTS (
                          ID INT IDENTITY PRIMARY KEY,
                          CONTENT NVARCHAR(500),
                          USERNAME VARCHAR(200),
                          CREATEDATE DATETIME,
                          POST INT,
                          IMAGE TEXT,
                          FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME),
                          FOREIGN KEY (POST) REFERENCES POSTS(ID)
);
CREATE TABLE LIKECMTS (
                          ID INT IDENTITY PRIMARY KEY,
                          USERNAME VARCHAR(200),
                          COMMENT INT,
                          FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME),
                          FOREIGN KEY (COMMENT) REFERENCES COMMENTS(ID)
);
GO
CREATE TABLE FRIENDS (
                         ID INT IDENTITY PRIMARY KEY,
                         USER_TARGET VARCHAR(200),
                         USER_SRC VARCHAR(200),
                         CREATEDATE DATETIME,
                         STATUS BIT,
                         FOREIGN KEY (USER_TARGET) REFERENCES USERS(USERNAME),
                         FOREIGN KEY (USER_SRC) REFERENCES USERS(USERNAME)
);
GO
CREATE TABLE LIKEPOSTS (
                           ID INT IDENTITY PRIMARY KEY,
                           USERNAME VARCHAR(200),
                           POST INT,
                           FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME),
                           FOREIGN KEY (POST) REFERENCES POSTS(ID)
);
GO
CREATE TABLE TRIPS (
                       ID INT IDENTITY PRIMARY KEY,
                       TRIPNAME NVARCHAR(500),
                       STARTDATE DATE,
                       ENDDATE DATE,
                       CREATEDATE DATETIME,
                       DESCRIPTION TEXT
);
GO
CREATE TABLE TRIPROLES (
                           ID INT IDENTITY PRIMARY KEY,
                           ROLE NVARCHAR(100)
);
GO
CREATE TABLE USERTRIPS (
                           ID INT IDENTITY PRIMARY KEY,
                           TRIPID INT,
                           USERID VARCHAR(200),
                           STATUS VARCHAR(50),
                           ROLE INT,
                           FOREIGN KEY (TRIPID) REFERENCES TRIPS(ID),
                           FOREIGN KEY (USERID) REFERENCES USERS(USERNAME),
                           FOREIGN KEY (ROLE) REFERENCES TRIPROLES(ID)
);
GO
CREATE TABLE NOTIFICATIONS (
                               ID INT IDENTITY PRIMARY KEY,
                               USERNAME VARCHAR(200),
                               CONTENT NVARCHAR(500),
                               TYPE INT,
                               POST INT,
                               STATUS BIT,
                               CREATEDATE DATETIME,
                               FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME),
                               FOREIGN KEY (TYPE) REFERENCES TYPES(ID),
                               FOREIGN KEY (POST) REFERENCES POSTS(ID)
);
GO
CREATE TABLE SHARES (
                        ID INT IDENTITY PRIMARY KEY,
                        USERNAME VARCHAR(200),
                        POST INT,
                        CONTENT NVARCHAR(500),
                        CREATEDATE DATETIME,
                        FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME),
                        FOREIGN KEY (POST) REFERENCES POSTS(ID)
);
GO
CREATE TABLE CONVERSATIONS (
                               ID INT IDENTITY PRIMARY KEY,
                               NAME NVARCHAR(300),
                               CREATEDAT DATETIME,
                               AVATAR TEXT
);
GO
CREATE TABLE GROUPMEMBERS (
                              ID INT IDENTITY PRIMARY KEY,
                              CONVERSATION INT,
                              USERNAME VARCHAR(200),
                              TIMEJOIN DATETIME,
                              FOREIGN KEY (CONVERSATION) REFERENCES CONVERSATIONS(ID),
                              FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME)
);
GO
CREATE TABLE MESSAGES (
                          ID INT IDENTITY PRIMARY KEY,
                          CONVERSATION INT,
                          USERSRC VARCHAR(200),
                          CONTENT NVARCHAR(500),
                          CREATEDATE DATETIME,
                          FOREIGN KEY (CONVERSATION) REFERENCES CONVERSATIONS(ID),
                          FOREIGN KEY (USERSRC) REFERENCES USERS(USERNAME)
);
GO
CREATE TABLE POSTIMAGES (
                            ID INT IDENTITY PRIMARY KEY,
                            POSTID INT,
                            IMAGE TEXT,
                            FOREIGN KEY (POSTID) REFERENCES POSTS(ID)
);
GO
CREATE TABLE PLACES (
                        ID INT IDENTITY PRIMARY KEY,
                        NAMEPLACE NVARCHAR(500),
    --LONGITUDE VARCHAR(100),
    --LATITUDE VARCHAR(100),
                        URLMAP VARCHAR(1000),
                        ADDRESS NVARCHAR(1000),
                        DESCRIPTION NVARCHAR(1000)
);
GO
CREATE TABLE PLACETRIPS (
                            ID INT IDENTITY PRIMARY KEY,
                            PLACEID INT,
                            TRIPID INT,
                            DATETIME DATETIME,
                            NOTE NVARCHAR(500),
                            FOREIGN KEY (PLACEID) REFERENCES PLACES(ID),
                            FOREIGN KEY (TRIPID) REFERENCES TRIPS(ID)
);
GO
CREATE TABLE PLACEIMAGES (
                             ID INT PRIMARY KEY,
                             PLACEID INT,
                             IMAGE TEXT,
                             FOREIGN KEY (PLACEID) REFERENCES PLACES(ID)
);
go
CREATE TABLE IMAGES (
                        ID INT IDENTITY PRIMARY KEY,
                        IMAGE TEXT,
                        CREATEDATE DATETIME,
                        AVATARRURL TEXT,
                        COVERURL TEXT,
                        STATUS BIT,
                        USERNAME VARCHAR(200),
                        FOREIGN KEY (USERNAME) REFERENCES USERS(USERNAME)
);
ALTER TABLE Users
ALTER COLUMN PASSWORD VARCHAR(255) 



CREATE OR ALTER PROCEDURE GetFriendsByUsername
    @username NVARCHAR(50) -- Adjust size if necessary
    AS
BEGIN
    SET NOCOUNT ON;
    IF @username IS NULL OR LTRIM(RTRIM(@username)) = ''
BEGIN
        RAISERROR('Username cannot be empty', 16, 1);
        RETURN;
END

SELECT f.*,
       CASE
           WHEN f.user_target = @username THEN f.user_src
           ELSE f.user_target
           END AS friend_username,
       CONCAT(u.lastname, ' ', u.firstname) AS friend_name,
       i.AVATARRURL AS friend_avatar
FROM
    FRIENDS f
        JOIN
    users u
    ON
        (f.user_target = @username AND u.username = f.user_src)
            OR (f.user_src = @username AND u.username = f.user_target)
        LEFT JOIN
    IMAGES i
    ON
        i.username = u.username
WHERE
    (f.user_target = @username OR f.user_src = @username)
  AND f.status = 1; -- Assuming 1 is the 'active' status
END
--thực thi thủ tục kết bạn 
EXEC GetFriendsByUsername @username = 'thuanreal1';
