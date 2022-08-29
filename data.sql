-- LAVORO SU DATABASE dbTirocini
    CREATE DATABASE dbTirocini;
        USE dbTirocini;


            -- Creazione della tabella Azienda con i relativi campi

            CREATE TABLE company(
                id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                located_at VARCHAR(255) NOT NULL,
                sector VARCHAR(255) NOT NULL,
                type VARCHAR(255) NOT NULL
            )

            -- Creazione della tabella Tutor con i relativi campi

            CREATE TABLE tutor(
              id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
              name VARCHAR(255) NOT NULL,
              surname VARCHAR(255) NOT NULL,
              subject VARCHAR(255) NOT NULL
            )

            -- Creazione della tabella Tirocinanti
            CREATE TABLE trainee(
                id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                surname VARCHAR(255) NOT NULL,
                class VARCHAR(255) NOT NULL,
                company_id INT(10) NULL,
                tutor_id INT(10) NULL
            );

            -- Creazione delle chiavi secondarie che fanno riferimento alle tabelle precedentemente creati, settando i valori
            -- ON DELETE e ON UPDATE su restrict così da non cancellare tutto una volta che si cancella un tutor o un'azienda

            ALTER TABLE trainee
                ADD CONSTRAINT FK_trainee_company_id FOREIGN KEY (company_id)
                    REFERENCES company (id)
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT;

            ALTER TABLE trainee
                ADD CONSTRAINT FK_trainee_tutor_id FOREIGN KEY (tutor_id)
                    REFERENCES tutor(id)
                    ON DELETE RESTRICT
                    ON UPDATE RESTRICT


            -- INSERT INTO `company` (`id`, `name`, `located_at`, `sector`, `type`) VALUES (NULL, 'CGM Consulting', 'Torino', 'Programmazione', 'Consulenza');
            -- Faccio un INSERT dove CGM avra' id 1

            -- 1) Nome e cognome degli studenti che svolgono il tirocinio alla CGM
            -- Questa query mi ritornera' un elenco di tirocinanti (nome e cognome) in base al company id, in questo caso 1 ovvero CGM
                SELECT nome, cognome WHERE company_id = 1

            -- 2) Nome e cognome dei tirocinanti della quarta D
            -- Questa query ritornerà un elenco di tirocinanti dove la classe sara' 4D
                SELECT nome, cognome WHERE class = "4D"

            -- 3) Settore e tipologia delle aziende in cui ha svolto il tirocinio Pippo Boi
            -- Inserisco pippo boy come tirocinante
            -- INSERT INTO `trainee` (`id`, `name`, `surname`, `class`, `company_id`, `tutor_id`) VALUES (1, 'Pippo', 'Boy', '4D', '1', NULL);
                SELECT t1.sector, t1.type FROM company AS t1 INNER JOIN trainee AS t2 ON t2.company_id = t1.id WHERE t2.id = 1;

            -- 4) nome, cognome e materie dei tutor dei tirocinanti del settore informatica

            SELECT      t1.name,
                        t1.surname,
                        t1.subject

            FROM       tutor AS t1
                           INNER JOIN company AS t2 ON t2.sector = "Informatica"
                           INNER JOIN trainee AS t3 ON t3.tutor_id = t1.id
            WHERE t3.company_id = t2.id;

            -- 5) I luoghi dove svolgono i tirocini gli studenti seguiti dal prof. Andrea Vassallo (id 3)

            SELECT      t1.located_at

            FROM       company AS t1
                           INNER JOIN trainee AS t2 ON t2.company_id = t1.id
            WHERE t2.tutor_id = 3;

            -- 6) nome e cognome dei tirocinanti seguiti dal prof. Guido Rossi (che deve essere previsto come tutor) (id 2)
            SELECT name, surname FROM trainee WHERE tutor_id = 2

            -- 7) materie dei tutor dei tirocinanti di Sulcigraf (che ha id 3)
            SELECT      t1.subject

            FROM       	tutor AS t1

                            INNER JOIN  trainee AS t2 ON t2.tutor_id = t1.id
                            INNER JOIN  company AS t3 ON t3.id = t2.company_id
            WHERE 		t2.tutor_id = t1.id AND t3.id = 3;

            -- 8)  classi che fanno il tirocinio (per non ripetere nel risultato una stessa classe, si può
            -- usare la clausola SELECT DISTINCT)
            SELECT DISTINCT class FROM trainee WHERE company_id IS NOT NULL;

-- LAVORO SU DATABASE dbMusica
    -- Mi creo il database dbMusica
    CREATE DATABASE dbMusica;
        USE dbMusica;
            -- Creo la tabella artista
            CREATE TABLE artist (
                id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                genre VARCHAR(255) NOT NULL,
                nationality VARCHAR(255) NOT NULL,
                note VARCHAR(255) NOT NULL
            );

            -- Creo la tabella registrazioni
            CREATE TABLE registration (
                id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                title VARCHAR(255) NOT NULL,
                tag VARCHAR(255) NOT NULL,
                date_of_registration DATETIME NOT NULL,
                number_of_songs INT(10) NOT NULL,
            total_length INT(10) NOT NULL
            );

            -- Creo la tabella songs
            CREATE TABLE songs (
                id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                title VARCHAR(255) NOT NULL,
                duration VARCHAR(255) NOT NULL,
                position VARCHAR(255) NOT NULL,
                artist_id INT(10) NOT NULL,
                registration_id INT(10) NOT NULL
            );
            -- Creo le relazioni tra le tabelle
            ALTER TABLE songs
                ADD CONSTRAINT FK_songs_artist_id FOREIGN KEY (artist_id)
                    REFERENCES artist (id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE;


            ALTER TABLE songs
                ADD CONSTRAINT FK_songs_registration_id FOREIGN KEY (registration_id)
                    REFERENCES registration (id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE;


        -- 1) Nomi degli artisti inglesi (inserire artisti inglesi nel db)
            SELECT name FROM artist WHERE nationality = "British"

        -- 2) Titoli e durata delle canzoni di Battisti (id 1 come artista)
            SELECT 	t1.title,
                    t1.duration

            FROM 	songs AS t1
            WHERE	artist_id = 1;

        -- 3) Titolo, durata e posizione dei brani dell’album ‘Dalla Terra’
        -- (Salvato come registration con id 2)

            SELECT 	t1.title,
                    t1.duration,
                    t1.position

            FROM 	songs AS t1
            WHERE	registration_id = 2;

        -- 4) La durata totale degli album di Eminem (tra quelli inseriti nel database)
            SELECT  t1.duration

            FROM 	songs AS t1
            WHERE	artist_id = 3;

        --5) Titolo del terzo brano dell’album Animals dei Pink Floyd (Registration id 3)

            SELECT 	t1.title

            FROM 	songs AS t1
            WHERE	registration_id = 3
            ORDER BY t1.id
            LIMIT 1 OFFSET 1;

        -- 6) Nome degli artisti che lavorano con l’etichetta discografica della Sony

            SELECT     t1.name

            FROM       artist AS t1
                           INNER JOIN songs AS t2 ON t2.artist_id = t1.id
                           INNER JOIN registration AS t3 ON t3.id = t2.registration_id
            WHERE t3.tag = "Sony";
        -- 7) Numero dei brani di ogni album di David Bowie (id 2)registrati nel database
            SELECT COUNT(id) FROM songs WHERE artist_id = 2;

-- LAVORO SU DATABASE dbVendite
    CREATE DATABASE dbVentide;
        USE DATABASE dbVendite;
            -- Creo la tabella fornitori
            CREATE TABLE supplier(
                id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                address VARCHAR(255) NOT NULL,
                city VARCHAR(255) NOT NULL,
                CAP VARCHAR(255) NOT NULL
            )

            -- Creo la tabella prodotti
            CREATE TABLE products(
                 id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                 quantity INT(255) NOT NULL,
                 price INT(255) NOT NULL,
                 name VARCHAR(255) NOT NULL,
                 brand VARCHAR(255) NOT NULL,
                 supplier_id INT(10) NOT NULL
            );

            -- Creo la relazione tra la tabella prodotti e la tabella fornitori sul campo supplier_id
            ALTER TABLE products
                ADD CONSTRAINT FK_products_supplier_id FOREIGN KEY (supplier_id)
                    REFERENCES supplier (id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE;

            -- Creo la tabella clienti

            CREATE TABLE customers(
                cf INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                surname VARCHAR(255) NOT NULL,
                card_type VARCHAR(255) NOT NULL,
                card_number VARCHAR(255) NOT NULL,
                card_expiration VARCHAR(255) NOT NULL
            )

            -- Creo la tabella ordini
            CREATE TABLE orders (
                id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                product_id INT(10) NOT NULL,
                customer_cf VARCHAR(255) NOT NULL
            );

            -- Creo le relazioni tra le tabelle
            ALTER TABLE orders
                ADD CONSTRAINT FK_orders_product_id FOREIGN KEY (product_id)
                    REFERENCES products (id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE;
            ALTER TABLE orders
                ADD CONSTRAINT FK_orders_customer_ref FOREIGN KEY (customer_cf)
                    REFERENCES customers (cf)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE;

            -- 1) Nome e città dei fornitori della Maionese Kraft
            SELECT 	    t1.name,
                        t1.city

            FROM 		supplier AS t1
            INNER JOIN	products AS t2
            ON 		    t2.supplier_id = t1.id

            WHERE       t2.brand = "KRAFT"

            -- 2) Nome e cognome dei clienti che hanno acquistato prodotti che costano più di 15 Euro
                SELECT 		t1.name,
                            t1.surname

                FROM 		customers AS t1
                INNER JOIN 	products AS t2 ON t2.price > 15
                INNER JOIN 	orders AS t3 ON t3.product_id = t2.id AND t3.customer_cf = t1.cf

            -- 3) Nome dei fornitori dei prodotti acquistati da Ugo Verdi (CF = 1 in customers)

            SELECT 			t3.name

            FROM 			orders AS t1
                                INNER JOIN 		products AS t2 ON t1.product_id = t2.id
                                INNER JOIN 		supplier AS t3 ON t3.id = t2.supplier_id
            WHERE			t1.customer_cf = 1

            -- 4) prezzo dei prodotti forniti da Rossi Vittorio acquistati con carta Visa

            SELECT 			t2.price

            FROM 			supplier AS t1
                                INNER JOIN 		products AS t2 ON t2.supplier_id = t1.id
                                INNER JOIN 		orders AS t3 ON t2.id = t3.product_id
                                INNER JOIN 		customers AS t4 ON t4.cf = t3.customer_cf
            WHERE 			t1.id = 3 AND t4.card_type = "VISA";

            -- 5) Tipo e numero di carta di credito dei clienti che hanno acquistato i Kinder Pinguì

            SELECT 			t1.card_type,
                            t1.card_number
            FROM 			customers AS t1
            INNER JOIN 		orders AS t2 ON t1.cf = t2.customer_cf
            INNER JOIN 		products AS t3 ON t2.product_id = t3.id
            WHERE           t3.name = "KP";

            -- 6) Nome e cognome dei clienti che hanno acquistato prodotti provenienti da Roma
            SELECT 			t1.name,
                            t1.surname
            FROM 			customers AS t1
            INNER JOIN		orders AS t2 ON t2.customer_cf = t1.cf
            INNER JOIN 		products AS t3 ON t3.id = t2.product_id
            INNER JOIN 		supplier AS t4 ON t4.id = t3.supplier_id
            WHERE 			t4.city = "Roma";

            -- 7) Nome, indirizzo e città dei fornitori dei prodotti che costano 1 Euro

            SELECT 			t1.name,
                            t1.address,
                            t1.city

            FROM 			supplier AS t1
            INNER JOIN 		products AS t2 ON t1.id = t2.supplier_id
            INNER JOIN 		orders AS t3 ON t2.id = t3.product_id

            WHERE t2.price = 1
