DROP TABLE IF EXISTS "USERMODEL";
CREATE TABLE "USERMODEL" (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(50),
    pwd VARCHAR(50),
    account FLOAT,
    "lastName" VARCHAR(100),
    "surName" VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE "CARDMODEL" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50),
    description VARCHAR(500),
    family VARCHAR(100),
    affinity VARCHAR(100),
    "imgUrl" VARCHAR(200),
    "smallImgUrl" VARCHAR(200),
    energy FLOAT,
    hp FLOAT,
    defence FLOAT,
    attack FLOAT,
    price FLOAT
);

INSERT INTO "CARDMODEL"(id,name,description,family,affinity,energy,hp,defence,attack,price, "imgUrl") 
VALUES (1,'superman','Super hero','DC','Fire',100,100,50,10,150,'http://fairedesgifs.free.fr/da/sh/superman/superman%20(5).gif');

INSERT INTO "USERMODEL"(id,login,pwd,account,"lastName","surName",email) 
VALUES(1,'jdoe','jdoepwd',500,'Doe','John','jdoe@cmp.com');