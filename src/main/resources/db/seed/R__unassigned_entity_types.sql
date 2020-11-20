-- Unassigned entity types
DO
$$
    DECLARE
        tribe_unassigned_id   CONSTANT uuid := '00000000000000000000000000000000';
        squad_unassigned_id   CONSTANT uuid := '00000000000000000000000000000000';
        chapter_unassigned_id CONSTANT uuid := '00000000000000000000000000000000';
    BEGIN
        -- Tribe for unassigned squads
        insert into tribe (tribe_id, name)
        values (tribe_unassigned_id, 'Unassigned Squads')
        ON CONFLICT (tribe_id) DO NOTHING;

        -- Squad for unassigned members
        insert into squad (squad_id, name, tribe)
        values (squad_unassigned_id, 'Unassigned Members', tribe_unassigned_id)
        ON CONFLICT (squad_id) DO NOTHING;

        -- Chapter for members with unassigned roles
        insert into chapter (chapter_id, name)
        values (chapter_unassigned_id, 'Unassigned')
        ON CONFLICT (chapter_id) DO NOTHING;
    END
$$;

