-- Unassigned entity types
DO
$$
    DECLARE
        color_grey            CONSTANT INTEGER := '-858993664';
        tribe_unassigned_id   CONSTANT uuid    := '00000000000000000000000000000000';
        squad_unassigned_id   CONSTANT uuid    := '00000000000000000000000000000000';
        chapter_unassigned_id CONSTANT uuid    := '00000000000000000000000000000000';
    BEGIN
        -- Tribe for unassigned squads
        insert into tribe (tribe_id, name, color)
        values (tribe_unassigned_id, 'Unassigned Squads', color_grey)
        ON CONFLICT (tribe_id) DO NOTHING;

        -- Squad for unassigned members
        insert into squad (squad_id, name, color, tribe)
        values (squad_unassigned_id, 'Unassigned Members', color_grey, tribe_unassigned_id)
        ON CONFLICT (squad_id) DO NOTHING;

        -- Chapter for members with unassigned roles
        insert into chapter (chapter_id, name, color)
        values (chapter_unassigned_id, 'Unassigned', color_grey)
        ON CONFLICT (chapter_id) DO NOTHING;
    END
$$;

