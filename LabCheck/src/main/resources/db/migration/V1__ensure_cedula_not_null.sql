-- V1__ensure_cedula_not_null.sql
-- Ensure the `cedula` column exists, fill NULLs with a unique placeholder based on id,
-- then enforce NOT NULL and UNIQUE constraints.

BEGIN;

-- 1) Add cedula column if missing
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS cedula VARCHAR(50);

-- 2) Fill NULL values with a deterministic unique placeholder (left-padded id)
-- NOTE: review before applying in production: this will not validate real cedulas.
UPDATE usuarios SET cedula = LPAD(CAST(id AS text), 10, '0') WHERE cedula IS NULL;

-- 3) Make column NOT NULL
ALTER TABLE usuarios ALTER COLUMN cedula SET NOT NULL;

-- 4) Create unique index if it does not exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes WHERE tablename = 'usuarios' AND indexname = 'ux_usuarios_cedula'
    ) THEN
        CREATE UNIQUE INDEX ux_usuarios_cedula ON usuarios (cedula);
    END IF;
END$$;

COMMIT;
