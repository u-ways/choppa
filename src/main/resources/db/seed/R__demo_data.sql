-- Demo data
-- TODO(u-ways) #96
--  on conflict this should update the objects to replace them with clean data instead of doing nothing.
--  to do this, you need to ensure demo data is correctly removed/cleaned/dropped before UPSERT.
DO
$$
    DECLARE
        tribe_bannock_id        CONSTANT UUID          := '00000000000000000000000000000001';
        squad_metropolitan_id   CONSTANT UUID          := '0000000000000000000000000000000a';
        squad_circle_id         CONSTANT UUID          := '0000000000000000000000000000000b';
        squad_district_id       CONSTANT UUID          := '0000000000000000000000000000000c';
        chapter_lead_id         CONSTANT UUID          := '00000000000000000000000000000001';
        chapter_ba_id           CONSTANT UUID          := '00000000000000000000000000000002';
        chapter_tester_id       CONSTANT UUID          := '00000000000000000000000000000003';
        chapter_float_id        CONSTANT UUID          := '00000000000000000000000000000004';
        chapter_dev_id          CONSTANT UUID          := '00000000000000000000000000000005';
        chapter_intern_id       CONSTANT UUID          := '00000000000000000000000000000006';
        member_arian_id         CONSTANT UUID          := '00000000000000000000000000000001';
        member_lillie_id        CONSTANT UUID          := '00000000000000000000000000000002';
        member_tina_id          CONSTANT UUID          := '00000000000000000000000000000003';
        member_yan_id           CONSTANT UUID          := '00000000000000000000000000000004';
        member_rose_id          CONSTANT UUID          := '00000000000000000000000000000005';
        member_jess_id          CONSTANT UUID          := '00000000000000000000000000000006';
        member_maryam_id        CONSTANT UUID          := '00000000000000000000000000000007';
        member_monty_id         CONSTANT UUID          := '00000000000000000000000000000008';
        member_gerald_id        CONSTANT UUID          := '00000000000000000000000000000009';
        member_rochelle_id      CONSTANT UUID          := '00000000000000000000000000000010';
        member_lamar_id         CONSTANT UUID          := '00000000000000000000000000000011';
        member_maxine_id        CONSTANT UUID          := '00000000000000000000000000000012';
        member_ethel_id         CONSTANT UUID          := '00000000000000000000000000000013';
        member_hattie_id        CONSTANT UUID          := '00000000000000000000000000000014';
        member_zahraa_id        CONSTANT UUID          := '00000000000000000000000000000015';
        member_shantelle_id     CONSTANT UUID          := '00000000000000000000000000000016';
        member_ezra_id          CONSTANT UUID          := '00000000000000000000000000000017';
        member_romany_id        CONSTANT UUID          := '00000000000000000000000000000018';
        member_jack_id          CONSTANT UUID          := '00000000000000000000000000000019';
        member_ben_id           CONSTANT UUID          := '00000000000000000000000000000020';
        member_sebastian_id     CONSTANT UUID          := '00000000000000000000000000000021';
        member_bob_id           CONSTANT UUID          := '00000000000000000000000000000022';
        iteration_370_id        CONSTANT UUID          := '00000000000000000000000000000001';
        iteration_370_startDate CONSTANT TIMESTAMP     := to_timestamp('2020-04-01', 'YYYY-MM-DD');
        iteration_371_id        CONSTANT UUID          := '00000000000000000000000000000002';
        iteration_371_startDate CONSTANT TIMESTAMP     := to_timestamp('2020-04-15', 'YYYY-MM-DD');
        iteration_372_id        CONSTANT UUID          := '00000000000000000000000000000003';
        iteration_372_startDate CONSTANT TIMESTAMP     := to_timestamp('2020-04-29', 'YYYY-MM-DD');
        iteration_373_id        CONSTANT UUID          := '00000000000000000000000000000004';
        iteration_373_startDate CONSTANT TIMESTAMP     := to_timestamp('2020-05-13', 'YYYY-MM-DD');
        iteration_374_id        CONSTANT UUID          := '00000000000000000000000000000005';
        iteration_374_startDate CONSTANT TIMESTAMP     := to_timestamp('2020-05-27', 'YYYY-MM-DD');
        iteration_375_id        CONSTANT UUID          := '00000000000000000000000000000006';
        iteration_375_startDate CONSTANT TIMESTAMP     := to_timestamp('2020-06-10', 'YYYY-MM-DD');
        iteration_376_id        CONSTANT UUID          := '00000000000000000000000000000007';
        iteration_376_startDate CONSTANT TIMESTAMP     := to_timestamp('2020-06-24', 'YYYY-MM-DD');
        color_red               CONSTANT INTEGER       := '-1407643649';
        color_yellow            CONSTANT INTEGER       := '-171882497';
        color_green             CONSTANT INTEGER       := '1655133951';
        color_grey              CONSTANT INTEGER       := '-858993409';
        color_blue              CONSTANT INTEGER       := '5476863';
        color_purple            CONSTANT INTEGER       := '1733141759';
        revision_add            CONSTANT REVISION_TYPE := 'ADD';
        revision_remove         CONSTANT REVISION_TYPE := 'REMOVE';
        revision_0              CONSTANT INTEGER       := 0;
        revision_1              CONSTANT INTEGER       := 1;
        revision_2              CONSTANT INTEGER       := 2;
        revision_3              CONSTANT INTEGER       := 3;
        revision_4              CONSTANT INTEGER       := 4;
        revision_5              CONSTANT INTEGER       := 5;
        revision_6              CONSTANT INTEGER       := 6;
        revision_7              CONSTANT INTEGER       := 7;
        demo_account_id         CONSTANT UUID          := '00000000000000000000000000000001';
    BEGIN
        -- Demo Account
        insert into accounts (account_id, provider, provider_id, organisation_name)
        values (demo_account_id, 'choppa', 'choppa-demo-account', 'Choppa Demo Org')
        ON CONFLICT (provider, provider_id) DO NOTHING;

        -- Tribes
        insert into tribe (tribe_id, name, color, account_id)
        values (tribe_bannock_id, 'The TFL', color_blue, demo_account_id)
        ON CONFLICT (tribe_id) DO NOTHING;

        -- Squads
        insert into squad (squad_id, name, color, tribe, account_id)
        values (squad_metropolitan_id, 'Metropolitan', color_red, tribe_bannock_id, demo_account_id)
             , (squad_circle_id, 'Circle', color_yellow, tribe_bannock_id, demo_account_id)
             , (squad_district_id, 'District', color_green, tribe_bannock_id, demo_account_id)
        ON CONFLICT (squad_id) DO NOTHING;

        -- Chapters
        insert into chapter (chapter_id, name, color, account_id)
        values (chapter_lead_id, 'LEAD', color_red, demo_account_id)
             , (chapter_ba_id, 'BA', color_yellow, demo_account_id)
             , (chapter_tester_id, 'TESTER', color_green, demo_account_id)
             , (chapter_float_id, 'FLOAT', color_grey, demo_account_id)
             , (chapter_dev_id, 'DEV', color_blue, demo_account_id)
             , (chapter_intern_id, 'INTERN', color_purple, demo_account_id)
        ON CONFLICT (chapter_id) DO NOTHING;

        -- Members
        insert into member (member_id, name, chapter, account_id)
        values (member_arian_id, 'Arian S.', chapter_lead_id, demo_account_id)
             , (member_lillie_id, 'Lillie-Mai K.', chapter_lead_id, demo_account_id)
             , (member_tina_id, 'Tina S.', chapter_lead_id, demo_account_id)
             , (member_yan_id, 'Yan C.', chapter_ba_id, demo_account_id)
             , (member_rose_id, 'Rose B.', chapter_ba_id, demo_account_id)
             , (member_jess_id, 'Jess C.', chapter_tester_id, demo_account_id)
             , (member_maryam_id, 'Maryam P.', chapter_tester_id, demo_account_id)
             , (member_monty_id, 'Monty H.', chapter_tester_id, demo_account_id)
             , (member_jack_id, 'Jack J.', chapter_tester_id, demo_account_id)
             , (member_gerald_id, 'Gerald M.', chapter_float_id, demo_account_id)
             , (member_rochelle_id, 'Rochelle R.', chapter_dev_id, demo_account_id)
             , (member_lamar_id, 'Lamar C.', chapter_dev_id, demo_account_id)
             , (member_maxine_id, 'Maxine C.', chapter_dev_id, demo_account_id)
             , (member_ethel_id, 'Ethel A.', chapter_dev_id, demo_account_id)
             , (member_hattie_id, 'Hattie P.', chapter_dev_id, demo_account_id)
             , (member_zahraa_id, 'Zahraa L.', chapter_dev_id, demo_account_id)
             , (member_shantelle_id, 'Shantelle S.', chapter_dev_id, demo_account_id)
             , (member_ben_id, 'Ben T.', chapter_dev_id, demo_account_id)
             , (member_sebastian_id, 'Sebastian S.', chapter_dev_id, demo_account_id)
             , (member_bob_id, 'Bob N.', chapter_dev_id, demo_account_id)
             , (member_ezra_id, 'Ezra B.', chapter_intern_id, demo_account_id)
             , (member_romany_id, 'Romany E.', chapter_intern_id, demo_account_id)
        ON CONFLICT (member_id) DO NOTHING;

        -- Iteration
        insert into iteration (iteration_id, number, start_date, end_date, account_id)
        values (iteration_370_id, 370, iteration_370_startDate, to_timestamp('2020-04-15', 'YYYY-MM-DD'), demo_account_id)
             , (iteration_371_id, 371, iteration_371_startDate, to_timestamp('2020-04-29', 'YYYY-MM-DD'), demo_account_id)
             , (iteration_372_id, 372, iteration_372_startDate, to_timestamp('2020-05-13', 'YYYY-MM-DD'), demo_account_id)
             , (iteration_373_id, 373, iteration_373_startDate, to_timestamp('2020-05-27', 'YYYY-MM-DD'), demo_account_id)
             , (iteration_374_id, 374, iteration_374_startDate, to_timestamp('2020-06-10', 'YYYY-MM-DD'), demo_account_id)
             , (iteration_375_id, 375, iteration_375_startDate, to_timestamp('2020-06-24', 'YYYY-MM-DD'), demo_account_id)
             , (iteration_376_id, 376, iteration_376_startDate, to_timestamp('2020-07-08', 'YYYY-MM-DD'), demo_account_id)
        ON CONFLICT (iteration_id) DO NOTHING;

        -- Squad Setup (i376 formation)
        INSERT INTO squad_current_members (squad_id, member_id)
        values (squad_circle_id, member_lillie_id)
             , (squad_circle_id, member_yan_id)
             , (squad_circle_id, member_jess_id)
             , (squad_circle_id, member_jack_id)
             , (squad_metropolitan_id, member_zahraa_id)
             , (squad_metropolitan_id, member_ezra_id)
             , (squad_district_id, member_lamar_id)
             , (squad_district_id, member_sebastian_id)
             , (squad_district_id, member_tina_id)
             , (squad_district_id, member_yan_id)
             , (squad_district_id, member_monty_id)
             , (squad_circle_id, member_ethel_id)
             , (squad_circle_id, member_romany_id)
             , (squad_metropolitan_id, member_maxine_id)
             , (squad_metropolitan_id, member_bob_id)
             , (squad_metropolitan_id, member_arian_id)
             , (squad_metropolitan_id, member_rose_id)
             , (squad_metropolitan_id, member_gerald_id)
             , (squad_metropolitan_id, member_maryam_id)
             , (squad_district_id, member_shantelle_id)
             , (squad_district_id, member_sebastian_id)
             , (squad_district_id, member_rochelle_id)
             , (squad_circle_id, member_hattie_id)
             , (squad_circle_id, member_ben_id)
        ON CONFLICT (squad_id, member_id) DO NOTHING;


        -- Squad Setup
        insert into squad_member_history (squad_id, member_id, revision_number, revision_type, create_date)
        values (squad_circle_id, member_lillie_id, revision_0, revision_add, iteration_370_startDate)       -- lead
             , (squad_circle_id, member_yan_id, revision_0, revision_add, iteration_370_startDate)          -- ba
             , (squad_circle_id, member_jess_id, revision_0, revision_add, iteration_370_startDate)         -- tester
             , (squad_circle_id, member_jack_id, revision_0, revision_add, iteration_370_startDate)         -- tester
             , (squad_circle_id, member_ethel_id, revision_0, revision_add, iteration_370_startDate)        -- dev
             , (squad_circle_id, member_hattie_id, revision_0, revision_add, iteration_370_startDate)       -- dev
             , (squad_circle_id, member_ben_id, revision_0, revision_add, iteration_370_startDate)          -- dev
             , (squad_circle_id, member_romany_id, revision_0, revision_add, iteration_370_startDate)       -- intern

             , (squad_district_id, member_tina_id, revision_0, revision_add, iteration_370_startDate)       -- lead
             , (squad_district_id, member_yan_id, revision_0, revision_add, iteration_370_startDate)        -- ba
             , (squad_district_id, member_monty_id, revision_0, revision_add, iteration_370_startDate)      -- tester
             , (squad_district_id, member_shantelle_id, revision_0, revision_add, iteration_370_startDate)  -- dev
             , (squad_district_id, member_lamar_id, revision_0, revision_add, iteration_370_startDate)      -- dev
             , (squad_district_id, member_rochelle_id, revision_0, revision_add, iteration_370_startDate)   -- dev
             , (squad_district_id, member_sebastian_id, revision_0, revision_add, iteration_370_startDate)  -- dev

             , (squad_metropolitan_id, member_arian_id, revision_0, revision_add, iteration_370_startDate)  -- lead
             , (squad_metropolitan_id, member_rose_id, revision_0, revision_add, iteration_370_startDate)   -- ba
             , (squad_metropolitan_id, member_gerald_id, revision_0, revision_add, iteration_370_startDate) -- float
             , (squad_metropolitan_id, member_maryam_id, revision_0, revision_add, iteration_370_startDate) -- tester
             , (squad_metropolitan_id, member_zahraa_id, revision_0, revision_add, iteration_370_startDate) -- dev
             , (squad_metropolitan_id, member_maxine_id, revision_0, revision_add, iteration_370_startDate) -- dev
             , (squad_metropolitan_id, member_bob_id, revision_0, revision_add, iteration_370_startDate)    -- dev
             , (squad_metropolitan_id, member_ezra_id, revision_0, revision_add, iteration_370_startDate)   -- intern

             -- i371 rotation
             --- Testers (Clockwise rotation)
             , (squad_circle_id, member_jess_id, revision_1, revision_remove, iteration_371_startDate)
             , (squad_district_id, member_jess_id, revision_1, revision_add, iteration_371_startDate)
             , (squad_circle_id, member_jack_id, revision_1, revision_remove, iteration_371_startDate)
             , (squad_district_id, member_jack_id, revision_1, revision_add, iteration_371_startDate)
             , (squad_district_id, member_monty_id, revision_1, revision_remove, iteration_371_startDate)
             , (squad_metropolitan_id, member_monty_id, revision_1, revision_add, iteration_371_startDate)
             , (squad_metropolitan_id, member_maryam_id, revision_1, revision_remove, iteration_371_startDate)
             , (squad_circle_id, member_maryam_id, revision_1, revision_add, iteration_371_startDate)
             -- Devs (Random rotation)
             , (squad_circle_id, member_ethel_id, revision_1, revision_remove, iteration_371_startDate)
             , (squad_district_id, member_ethel_id, revision_1, revision_add, iteration_371_startDate)
             , (squad_district_id, member_shantelle_id, revision_1, revision_remove, iteration_371_startDate)
             , (squad_metropolitan_id, member_shantelle_id, revision_1, revision_add, iteration_371_startDate)
             , (squad_metropolitan_id, member_zahraa_id, revision_1, revision_remove, iteration_371_startDate)
             , (squad_circle_id, member_zahraa_id, revision_1, revision_add, iteration_371_startDate)

             -- i372 rotation
             --- Testers (Clockwise rotation)
             , (squad_circle_id, member_maryam_id, revision_2, revision_remove, iteration_372_startDate)
             , (squad_district_id, member_maryam_id, revision_2, revision_add, iteration_372_startDate)
             , (squad_district_id, member_jess_id, revision_2, revision_remove, iteration_372_startDate)
             , (squad_metropolitan_id, member_jess_id, revision_2, revision_add, iteration_372_startDate)
             , (squad_district_id, member_jack_id, revision_2, revision_remove, iteration_372_startDate)
             , (squad_metropolitan_id, member_jack_id, revision_2, revision_add, iteration_372_startDate)
             , (squad_metropolitan_id, member_monty_id, revision_2, revision_remove, iteration_372_startDate)
             , (squad_circle_id, member_monty_id, revision_2, revision_add, iteration_372_startDate)
             -- Devs (Random rotation)
             , (squad_circle_id, member_hattie_id, revision_2, revision_remove, iteration_372_startDate)
             , (squad_district_id, member_hattie_id, revision_2, revision_add, iteration_372_startDate)
             , (squad_district_id, member_lamar_id, revision_2, revision_remove, iteration_372_startDate)
             , (squad_metropolitan_id, member_lamar_id, revision_2, revision_add, iteration_372_startDate)
             , (squad_metropolitan_id, member_maxine_id, revision_2, revision_remove, iteration_372_startDate)
             , (squad_circle_id, member_maxine_id, revision_2, revision_add, iteration_372_startDate)

             -- i373 rotation
             --- Testers (Clockwise rotation)
             , (squad_circle_id, member_monty_id, revision_3, revision_remove, iteration_373_startDate)
             , (squad_district_id, member_monty_id, revision_3, revision_add, iteration_373_startDate)
             , (squad_district_id, member_maryam_id, revision_3, revision_remove, iteration_373_startDate)
             , (squad_metropolitan_id, member_maryam_id, revision_3, revision_add, iteration_373_startDate)
             , (squad_metropolitan_id, member_jess_id, revision_3, revision_remove, iteration_373_startDate)
             , (squad_circle_id, member_jess_id, revision_3, revision_add, iteration_373_startDate)
             , (squad_metropolitan_id, member_jack_id, revision_3, revision_remove, iteration_373_startDate)
             , (squad_circle_id, member_jack_id, revision_3, revision_add, iteration_373_startDate)
             -- Devs (Random rotation)
             , (squad_circle_id, member_ben_id, revision_3, revision_remove, iteration_373_startDate)
             , (squad_district_id, member_ben_id, revision_3, revision_add, iteration_373_startDate)
             , (squad_district_id, member_sebastian_id, revision_3, revision_remove, iteration_373_startDate)
             , (squad_metropolitan_id, member_sebastian_id, revision_3, revision_add, iteration_373_startDate)
             , (squad_metropolitan_id, member_bob_id, revision_3, revision_remove, iteration_373_startDate)
             , (squad_circle_id, member_bob_id, revision_3, revision_add, iteration_373_startDate)

             -- i374 rotation
             --- Testers (Anti-clockwise rotation)
             , (squad_circle_id, member_jess_id, revision_4, revision_remove, iteration_374_startDate)
             , (squad_metropolitan_id, member_jess_id, revision_4, revision_add, iteration_374_startDate)
             , (squad_circle_id, member_jack_id, revision_4, revision_remove, iteration_374_startDate)
             , (squad_metropolitan_id, member_jack_id, revision_4, revision_add, iteration_374_startDate)
             , (squad_district_id, member_monty_id, revision_4, revision_remove, iteration_374_startDate)
             , (squad_circle_id, member_monty_id, revision_4, revision_add, iteration_374_startDate)
             , (squad_metropolitan_id, member_maryam_id, revision_4, revision_remove, iteration_374_startDate)
             , (squad_district_id, member_maryam_id, revision_4, revision_add, iteration_374_startDate)
             -- Devs (Random rotation)
             , (squad_circle_id, member_romany_id, revision_4, revision_remove, iteration_374_startDate)
             , (squad_district_id, member_romany_id, revision_4, revision_add, iteration_374_startDate)
             , (squad_district_id, member_rochelle_id, revision_4, revision_remove, iteration_374_startDate)
             , (squad_metropolitan_id, member_rochelle_id, revision_4, revision_add, iteration_374_startDate)
             , (squad_metropolitan_id, member_ezra_id, revision_4, revision_remove, iteration_374_startDate)
             , (squad_circle_id, member_ezra_id, revision_4, revision_add, iteration_374_startDate)

             -- i375 rotation
             --- Testers (Anti-clockwise rotation)
             , (squad_circle_id, member_monty_id, revision_5, revision_remove, iteration_375_startDate)
             , (squad_metropolitan_id, member_monty_id, revision_5, revision_add, iteration_375_startDate)
             , (squad_metropolitan_id, member_jess_id, revision_5, revision_remove, iteration_375_startDate)
             , (squad_district_id, member_jess_id, revision_5, revision_add, iteration_375_startDate)
             , (squad_metropolitan_id, member_jack_id, revision_5, revision_remove, iteration_375_startDate)
             , (squad_district_id, member_jack_id, revision_5, revision_add, iteration_375_startDate)
             , (squad_district_id, member_maryam_id, revision_5, revision_remove, iteration_375_startDate)
             , (squad_circle_id, member_maryam_id, revision_5, revision_add, iteration_375_startDate)
             -- Devs (Random rotation)
             , (squad_circle_id, member_maxine_id, revision_5, revision_remove, iteration_375_startDate)
             , (squad_district_id, member_maxine_id, revision_5, revision_add, iteration_375_startDate)
             , (squad_district_id, member_hattie_id, revision_5, revision_remove, iteration_375_startDate)
             , (squad_metropolitan_id, member_hattie_id, revision_5, revision_add, iteration_375_startDate)
             , (squad_metropolitan_id, member_lamar_id, revision_5, revision_remove, iteration_375_startDate)
             , (squad_circle_id, member_lamar_id, revision_5, revision_add, iteration_375_startDate)

             -- i376 rotation
             -- Testers (Anti-clockwise rotation)
             , (squad_circle_id, member_maryam_id, revision_6, revision_remove, iteration_376_startDate)
             , (squad_metropolitan_id, member_maryam_id, revision_6, revision_add, iteration_376_startDate)
             , (squad_district_id, member_jess_id, revision_6, revision_remove, iteration_376_startDate)
             , (squad_circle_id, member_jess_id, revision_6, revision_add, iteration_376_startDate)
             , (squad_district_id, member_jack_id, revision_6, revision_remove, iteration_376_startDate)
             , (squad_circle_id, member_jack_id, revision_6, revision_add, iteration_376_startDate)
             , (squad_metropolitan_id, member_monty_id, revision_6, revision_remove, iteration_376_startDate)
             , (squad_district_id, member_monty_id, revision_6, revision_add, iteration_376_startDate)
             -- Devs (Random rotation)
             , (squad_circle_id, member_bob_id, revision_6, revision_remove, iteration_376_startDate)
             , (squad_district_id, member_bob_id, revision_6, revision_add, iteration_376_startDate)
             , (squad_district_id, member_ben_id, revision_6, revision_remove, iteration_376_startDate)
             , (squad_metropolitan_id, member_ben_id, revision_6, revision_add, iteration_376_startDate)
             , (squad_metropolitan_id, member_sebastian_id, revision_6, revision_remove, iteration_376_startDate)
             , (squad_circle_id, member_sebastian_id, revision_6, revision_add, iteration_376_startDate)

             --- Float (moving from own squad and then place them back again on the same squad on the same iteration, i376)
             , (squad_circle_id, member_gerald_id, revision_7, revision_remove, to_timestamp('2020-06-24', 'YYYY-MM-DD'))
             , (squad_metropolitan_id, member_gerald_id, revision_7, revision_add, to_timestamp('2020-06-24', 'YYYY-MM-DD'))
             , (squad_metropolitan_id, member_gerald_id, revision_7, revision_remove, to_timestamp('2020-06-29', 'YYYY-MM-DD'))
             , (squad_circle_id, member_gerald_id, revision_7, revision_add, to_timestamp('2020-06-29', 'YYYY-MM-DD'))
        ON CONFLICT (squad_id, member_id, create_date) DO NOTHING;
    END
$$;

