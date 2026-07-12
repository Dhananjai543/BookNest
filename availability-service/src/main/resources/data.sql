INSERT INTO t_availability (isbn, copies_available) VALUES ('9780201616224', 5) ON CONFLICT (isbn) DO NOTHING;
INSERT INTO t_availability (isbn, copies_available) VALUES ('9780132350884', 3) ON CONFLICT (isbn) DO NOTHING;
INSERT INTO t_availability (isbn, copies_available) VALUES ('9780134685991', 0) ON CONFLICT (isbn) DO NOTHING;
