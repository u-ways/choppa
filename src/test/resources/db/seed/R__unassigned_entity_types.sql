-- Unassigned entity types
DO
$$
    DECLARE
        color_grey    CONSTANT INTEGER := '-858993409';
        unassigned_id CONSTANT uuid    := '00000000000000000000000000000000';
    BEGIN
        -- Tribe for unassigned squads
        insert into tribe (tribe_id, name, color, account_id)
        values (unassigned_id, 'Unassigned Squads', color_grey, '00000000000000000000000000000001')
        ON CONFLICT (tribe_id) DO NOTHING;

        -- Squad for unassigned members
        insert into squad (squad_id, name, color, tribe, account_id)
        values (unassigned_id, 'Unassigned Members', color_grey, unassigned_id, '00000000000000000000000000000001')
        ON CONFLICT (squad_id) DO NOTHING;

        -- Unassigned Account
        insert into account (account_id, provider, provider_id, organisation_name)
        values (unassigned_id, 'unassigned-provider-id', 'Unassigned Account', 'Unassigned Org')
        ON CONFLICT (provider, provider_id) DO NOTHING;
    END
$$;
