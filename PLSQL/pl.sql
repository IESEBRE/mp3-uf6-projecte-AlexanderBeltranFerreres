-- Poseu el codi dels procediments/funcions emmagatzemats, triggers, ..., usats al projecte

-- Creem un trigger per a generar automàticament els id dels peixos insertats a la taula.
CREATE OR REPLACE TRIGGER peixos_gen_id
BEFORE INSERT ON peixos
FOR EACH ROW
BEGIN
    IF :new.id IS NULL THEN
        SELECT NVL(MAX(id), 0) + 1 INTO :new.id FROM peixos;
    END IF;
END;
/

commit;

