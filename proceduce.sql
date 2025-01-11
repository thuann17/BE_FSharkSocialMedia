
select * from friends

-- th? t?c k?t b?n

CREATE OR ALTER PROCEDURE GetFriendsByUsername
    @username NVARCHAR(50) -- Adjust size if necessary
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Check if @username is provided to avoid unintended results
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
        CONCAT(u.lastname, ' ', u.firstname) AS friend_name
    FROM 
        FRIENDS f
    JOIN 
        users u 
    ON 
        (f.user_target = @username AND u.username = f.user_src)
        OR (f.user_src = @username AND u.username = f.user_target)
    WHERE 
        (f.user_target = @username OR f.user_src = @username)
        AND f.status = 1; -- Assuming 1 is the 'active' status
END


--th?c thi th? t?c k?t b?n 
EXEC GetFriendsByUsername @username = 'nguyenkimlan';

CREATE OR ALTER PROCEDURE GetUsersWithoutFriends
    @username NVARCHAR(50)  -- Parameter to specify the username
AS
BEGIN
    SET NOCOUNT ON;

    SELECT 
        u.*,  -- Select all user fields
        u.USERNAME AS username,  -- Username of the friend
        CONCAT(u.LASTNAME, ' ', u.FIRSTNAME) AS name,  -- Full name (Lastname + Firstname)
        (SELECT TOP 1 AVATARRURL FROM IMAGES i WHERE i.USERNAME = u.USERNAME) AS avatar  -- Single Avatar URL
    FROM USERS u
    WHERE u.USERNAME != @username  -- Exclude the logged-in user
      AND u.USERNAME NOT IN (
          SELECT USER_TARGET FROM FRIENDS WHERE USER_SRC = @username
          UNION
          SELECT USER_SRC FROM FRIENDS WHERE USER_TARGET = @username
      );
END;




CREATE OR ALTER PROCEDURE GetMutualFriends
    @username1 NVARCHAR(200),
    @username2 NVARCHAR(200)
AS
BEGIN
    SET NOCOUNT ON;

    -- Select mutual friends and their count
    SELECT 
        f1.USER_TARGET AS MutualFriend,
        (SELECT COUNT(*) 
         FROM FRIENDS f3 
         WHERE f3.USER_SRC = @username1 
           AND f3.USER_TARGET = f1.USER_TARGET
           AND f3.STATUS = 1) AS MutualFriendCount
    FROM FRIENDS f1
    JOIN FRIENDS f2 ON f1.USER_TARGET = f2.USER_TARGET
    WHERE f1.USER_SRC = @username1
      AND f2.USER_SRC = @username2
      AND f1.STATUS = 1
      AND f2.STATUS = 1; -- Assuming 1 indicates 'active' status
END;


EXEC GetMutualFriends @username1 = 'nguyenkimlan', @username2 = 'thuannminh';


select * from friends

CREATE OR ALTER PROCEDURE GetFollowerByUsername
    @username NVARCHAR(50) -- Adjust size if necessary
AS
BEGIN
    SET NOCOUNT ON;

    -- Kiểm tra username không được NULL hoặc rỗng
    IF @username IS NULL OR LTRIM(RTRIM(@username)) = ''
    BEGIN
        RAISERROR('Username cannot be empty', 16, 1);
        RETURN;
    END

    -- Truy vấn danh sách bạn bè
    SELECT 
        f.*,
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
        ON ((f.user_target = @username AND u.username = f.user_src)
            OR (f.user_src = @username AND u.username = f.user_target))
    LEFT JOIN 
        IMAGES i
        ON i.username = u.username
    WHERE 
        (f.user_target = @username OR f.user_src = @username)
        AND f.status = 0 -- Assuming 0 is the active status
        AND @username <> f.user_src; -- Điều kiện @username khác user_target
END


EXEC GetFollowerByUsername @username = 'user';

CREATE OR ALTER PROCEDURE GetPostsWithUserDetails
    @inputUsername NVARCHAR(255)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT 
        p.id AS PostId,
        u.username AS Username,
        u.email AS Email,
        u.firstname AS Firstname,
        u.lastname AS Lastname,
        p.content AS PostContent,
        p.createdate AS PostCreateDate,
        p.status AS PostStatus,
        (SELECT COUNT(*) FROM Comments c WHERE c.post = p.id) AS CommentCount,
        (SELECT COUNT(*) FROM Likeposts l WHERE l.post = p.id) AS LikeCount,
        pi.image AS PostImage
    FROM 
        Posts p
    INNER JOIN 
        [Users] u ON p.username = u.username
    LEFT JOIN 
        PostImages pi ON pi.postid = p.id
    WHERE 
        u.username = @inputUsername;
END;

EXEC GetPostsWithUserDetails @inputUsername = 'thuyvytran';


select * from users

CREATE or alter PROCEDURE GetPlaceDetailsWithImages
AS
BEGIN
    -- Truy vấn thông tin địa điểm và hình ảnh liên quan
    SELECT 
        p.ID AS PlaceID,
        p.NAMEPLACE AS PlaceName,
        p.URLMAP AS MapURL,
        p.ADDRESS AS Address,
        p.DESCRIPTION AS Description,
        pi.ID AS ImageID,
        pi.IMAGE AS ImageURL
    FROM 
        PLACES p
    LEFT JOIN 
        PLACEIMAGES pi ON p.ID = pi.PLACEID
    ORDER BY 
        p.ID, pi.ID; -- Sắp xếp theo ID địa điểm và hình ảnh
END;
GO

EXEC GetPlaceDetailsWithImages;

CREATE OR ALTER PROCEDURE GetPlacesWithImagesByAddress
    @AddressFilter NVARCHAR(255)
AS
BEGIN
    SET NOCOUNT ON;

    -- Extract the province name (after the last comma) and compare with AddressFilter
    SELECT 
        p.id AS PlaceId,
        p.nameplace AS NamePlace,
        p.address AS Address,
        p.description AS Description,
        pi.id AS ImageId,
        pi.image AS Image
    FROM 
        PLACES p
    LEFT JOIN 
        PLACEIMAGES pi ON p.id = pi.placeid
    WHERE 
        -- Extract province part (after the last comma) and compare with AddressFilter
        LTRIM(RTRIM(
            -- Extract the province after the last comma
            SUBSTRING(p.address, 
                LEN(p.address) - CHARINDEX(',', REVERSE(p.address)) + 2, 
                LEN(p.address)
            )
        )) LIKE '%' + @AddressFilter + '%';
END;
GO

EXEC GetPlacesWithImagesByAddress @AddressFilter = 'Lâm Đồng';

CREATE OR ALTER PROCEDURE GetPlacesByAddress
    @AddressFilter NVARCHAR(255)
AS
BEGIN
    SET NOCOUNT ON;

    -- Select places where the address contains the provided filter string
    SELECT 
        p.id AS PlaceId,
        p.nameplace AS NamePlace,
        p.address AS Address,
        p.description AS Description,
        pi.id AS ImageId,
        pi.image AS Image
    FROM 
        PLACES p
    LEFT JOIN 
        PLACEIMAGES pi ON p.id = pi.placeid
    WHERE 
        -- Use LIKE to check if the address contains the AddressFilter string
        p.address LIKE '%' + @AddressFilter + '%';
END;
GO

EXEC GetPlacesByAddress @AddressFilter = 'Số 2';



SELECT * FROM places WHERE LTRIM(RTRIM(address)) LIKE '%2%';

select * from places

CREATE OR ALTER PROCEDURE GetPlacesByHanoi
AS
BEGIN
    SET NOCOUNT ON;

    -- Select places where the address contains "Hà Nội" in the province part
    SELECT 
        p.id AS PlaceId,
        p.nameplace AS NamePlace,
        p.address AS Address,
        p.description AS Description,
        pi.id AS ImageId,
        pi.image AS Image
    FROM 
        PLACES p
    LEFT JOIN 
        PLACEIMAGES pi ON p.id = pi.placeid
    WHERE 
        -- Extract the province part from the address (second-to-last part) and compare with "Hà Nội"
        LTRIM(RTRIM(SUBSTRING(p.address, 
            LEN(p.address) - CHARINDEX(',', REVERSE(p.address)) + 2, 
            CHARINDEX(' ', REVERSE(p.address)) - 1))) = 'Hà Nội';
END;
GO

EXEC GetPlacesByHanoi;