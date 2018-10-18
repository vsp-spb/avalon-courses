create table tblterms (
    id INTEGER PRIMARY KEY,
    term VARCHAR (96) NOT NULL UNIQUE
);

create table tbldefinitions(
    id INTEGER PRIMARY KEY,
    definition VARCHAR (255) NOT NULL,
    term_id INTEGER NOT NULL,
    CONSTRAINT term_fk FOREIGN KEY (term_id) REFERENCES tblterms (id) ON DELETE CASCADE
);

insert into tblterms values (1 , 'abstract');
insert into tblterms values (2 , 'bean');
insert into tblterms values (3 , 'AWT');

insert into tbldefinitions values (1, 'A Java keyword used in a class definition to specify that a class is not to be instantiated, but rather inherited by other classes. An abstract class can have abstract methods that are not implemented in the abstract class, but in subclasses.', 1);
insert into tbldefinitions values (2, 'A reusable software component that conforms to certain design and naming conventions.', 2);
insert into tbldefinitions values (3, 'The conventions enable beans to be easily combined to create an application using tools that understand the conventions.', 2);
insert into tbldefinitions values (4, 'A collection of graphical user interface (GUI) components that were implemented using native-platform versions of the components.', 3);
insert into tbldefinitions values (5, 'These components provide that subset of functionality which is common to all native platforms.', 3);
insert into tbldefinitions values (6, 'Largely supplanted by the Project Swing component set.', 3);

CREATE SEQUENCE term_seq as INTEGER start with 4 INCREMENT BY 1 NO CYCLE ;
CREATE SEQUENCE definition_seq as INTEGER start with 7 INCREMENT BY 1 NO CYCLE ;