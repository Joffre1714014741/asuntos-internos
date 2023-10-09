CREATE DATABASE asuntos_internos;

CREATE TABLE personas (
    id SERIAL PRIMARY KEY,
    nombres VARCHAR (255) NOT NULL,
    apellidos VARCHAR (255) NOT NULL,
    cedula VARCHAR (25) UNIQUE NOT NULL 
);

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombreusuario VARCHAR (255) NOT NULL,
    contrasena TEXT NOT NULL,
    tipo VARCHAR (25),
    fechaultimoingreso TIMESTAMP WITHOUT TIME ZONE,
    persona INT,
    FOREIGN KEY (persona)
    REFERENCES personas (id)
    ON DELETE SET NULL
);

CREATE TABLE causas (
    id SERIAL PRIMARY KEY,
    descripcion VARCHAR (255) NOT NULL
);

CREATE TABLE casos (
    id SERIAL PRIMARY KEY,
    estado VARCHAR (25),
    fechaderealizacion TIMESTAMP WITHOUT TIME ZONE,
    fechadeincidente TIMESTAMP WITHOUT TIME ZONE,
    fechadeasignacion TIMESTAMP WITHOUT TIME ZONE,
    detallecausa TEXT,
    lugardetrabajo VARCHAR (255),
    mediodeingreso VARCHAR (25),
    lugardeincidente VARCHAR (255),
    antecedentes TEXT,
    calificacion TEXT,
    relaciondehechos TEXT,
    conclusiones TEXT,
    recomendaciones TEXT,
    numeroinforme VARCHAR (255),
    involucrado INT,
    responsable INT,
    director INT,
    causa INT,
    FOREIGN KEY (involucrado) 
    REFERENCES usuarios (id)
    ON DELETE SET NULL,
    FOREIGN KEY (responsable)
    REFERENCES usuarios (id)
    ON DELETE SET NULL,
    FOREIGN KEY (director)
    REFERENCES usuarios (id)
    ON DELETE SET NULL,
    FOREIGN KEY (causa)
    REFERENCES causas (id)
    ON DELETE SET NULL
);

CREATE TABLE documentos (
    id SERIAL PRIMARY KEY,
    nombredocumento VARCHAR (255),
    ruta TEXT,
    caso INT,
    FOREIGN KEY (caso)
    REFERENCES casos (id)
    ON DELETE SET NULL
);

CREATE TABLE eventos (
    id SERIAL PRIMARY KEY,
    fechahora TIMESTAMP WITHOUT TIME ZONE,
    estado VARCHAR (25),
    accionrealizada TEXT,
    caso INT,
    FOREIGN KEY (caso)
    REFERENCES casos (id)
    ON DELETE SET NULL 
);