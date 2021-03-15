-- Unassigned entity types (Used for e2e testing)
DO
$$
    DECLARE
        color_grey CONSTANT INTEGER := '-858993409';
        common_id  CONSTANT uuid    := '00000000000000000000000000000000';
    BEGIN
        -- Account
        insert into account (account_id, provider, provider_id, organisation_name, create_date)
        values (common_id, 'account_provider', 'account_provider_id', 'account_organisation_name',
                to_timestamp('2020-01-01', 'YYYY-MM-DD'))
        ON CONFLICT (provider, provider_id) DO NOTHING;

        -- Tribe
        insert into tribe (tribe_id, name, color, account_id)
        values (common_id, 'tribe_name', color_grey, common_id)
        ON CONFLICT (tribe_id) DO NOTHING;

        -- Squad
        insert into squad (squad_id, name, color, tribe, account_id)
        values (common_id, 'squad_name', color_grey, common_id, common_id)
        ON CONFLICT (squad_id) DO NOTHING;

        -- Chapter
        insert into chapter (chapter_id, name, color, account_id)
        values (common_id, 'chapter_name', color_grey, common_id)
        ON CONFLICT (chapter_id) DO NOTHING;

        -- member
        insert into member (member_id, name, chapter, active, account_id)
        values (common_id, 'member_name', common_id, true, common_id)
        ON CONFLICT (member_id) DO NOTHING;
    END
$$;
