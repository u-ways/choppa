-- Unassigned chapter type
DO
$$
    DECLARE
        color_grey            CONSTANT INTEGER := '-858993409';
        chapter_unassigned_id CONSTANT uuid    := '00000000000000000000000000000000';
    BEGIN
        -- Chapter for members with unassigned roles
        insert into chapter (chapter_id, name, color)
        values (chapter_unassigned_id, 'Unassigned', color_grey)
        ON CONFLICT (chapter_id) DO NOTHING;
    END
$$;

