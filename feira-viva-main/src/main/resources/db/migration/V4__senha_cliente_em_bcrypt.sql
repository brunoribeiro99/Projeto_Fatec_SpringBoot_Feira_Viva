-- V4__senha_cliente_em_bcrypt.sql
-- Migra a senha da cliente de teste para BCrypt (Aula 14)
UPDATE clientes
SET senha_hash = '$2a$10$t.CrsV5oyqbqwRDqw1gHF.eYQ0eiHrwrE9Uf.4sy.j4X0zgxfMY82'
WHERE email = 'maria@email.com';