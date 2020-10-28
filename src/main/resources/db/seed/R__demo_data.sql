-- Demo data
-- TODO(u-ways) #96
--  on conflict this should update the objects to replace them with clean data instead of doing nothing.
--  to do this, you need to ensure demo data is correctly removed/cleaned/dropped before UPSERT.
DO $$
DECLARE
    tribe_bannock_id CONSTANT uuid := '00000000000000000000000000000000';

    squad_metropolitan_id CONSTANT uuid := '0000000000000000000000000000000a';
    squad_circle_id CONSTANT uuid := '0000000000000000000000000000000b';
    squad_district_id CONSTANT uuid := '0000000000000000000000000000000c';

    chapter_lead_id CONSTANT uuid := '00000000000000000000000000000001';
    chapter_ba_id CONSTANT uuid := '00000000000000000000000000000002';
    chapter_tester_id CONSTANT uuid := '00000000000000000000000000000003';
    chapter_float_id CONSTANT uuid := '00000000000000000000000000000004';
    chapter_dev_id CONSTANT uuid := '00000000000000000000000000000005';
    chapter_intern_id CONSTANT uuid := '00000000000000000000000000000006';

    member_arian_id uuid := '00000000000000000000000000000001';
    member_lillie_id uuid := '00000000000000000000000000000002';
    member_tina_id uuid := '00000000000000000000000000000003';
    member_yan_id uuid := '00000000000000000000000000000004';
    member_rose_id uuid := '00000000000000000000000000000005';
    member_jess_id uuid := '00000000000000000000000000000006';
    member_maryam_id uuid := '00000000000000000000000000000007';
    member_monty_id uuid := '00000000000000000000000000000008';
    member_gerald_id uuid := '00000000000000000000000000000009';
    member_rochelle_id uuid := '00000000000000000000000000000010';
    member_lamar_id uuid := '00000000000000000000000000000011';
    member_maxine_id uuid := '00000000000000000000000000000012';
    member_ethel_id uuid := '00000000000000000000000000000013';
    member_hattie_id uuid := '00000000000000000000000000000014';
    member_zahraa_id uuid := '00000000000000000000000000000015';
    member_shantelle_id uuid := '00000000000000000000000000000016';
    member_ezra_id uuid := '00000000000000000000000000000017';
    member_romany_id uuid := '00000000000000000000000000000018';

    iteration_370_id uuid := '00000000000000000000000000000001';
    iteration_371_id uuid := '00000000000000000000000000000002';
    iteration_372_id uuid := '00000000000000000000000000000003';
    iteration_373_id uuid := '00000000000000000000000000000004';
    iteration_374_id uuid := '00000000000000000000000000000005';
    iteration_375_id uuid := '00000000000000000000000000000006';
    iteration_376_id uuid := '00000000000000000000000000000007';
BEGIN
    -- Tribes
    insert into tribe (tribe_id, name)
    values (tribe_bannock_id, 'The Bannock') ON CONFLICT (tribe_id) DO NOTHING;

    -- Squads
    insert into squad (squad_id, name, tribe)
    values (squad_metropolitan_id, 'Metropolitan', tribe_bannock_id)
         , (squad_circle_id, 'Circle', tribe_bannock_id)
         , (squad_district_id, 'District', tribe_bannock_id) ON CONFLICT (squad_id) DO NOTHING;

    -- Chapters
    insert into chapter (chapter_id, name)
    values (chapter_lead_id, 'LEAD')
         , (chapter_ba_id, 'BA')
         , (chapter_tester_id, 'TESTER')
         , (chapter_float_id, 'FLOAT')
         , (chapter_dev_id, 'DEV')
         , (chapter_intern_id, 'INTERN')  ON CONFLICT (chapter_id) DO NOTHING;

    -- Members
    insert into member (member_id, name, chapter)
    values (member_arian_id, 'Arian S.', chapter_lead_id)
         , (member_lillie_id, 'Lillie-Mai K.', chapter_lead_id)
         , (member_tina_id, 'Tina S.', chapter_lead_id)
         , (member_yan_id, 'Yan C.', chapter_ba_id)
         , (member_rose_id, 'Rose B.', chapter_ba_id)
         , (member_jess_id, 'Jess C.', chapter_tester_id)
         , (member_maryam_id, 'Maryam P.', chapter_tester_id)
         , (member_monty_id, 'Monty H.', chapter_tester_id)
         , (member_gerald_id, 'Gerald M.', chapter_float_id)
         , (member_rochelle_id, 'Rochelle R.', chapter_dev_id)
         , (member_lamar_id, 'Lamar C.', chapter_dev_id)
         , (member_maxine_id, 'Maxine C.', chapter_dev_id)
         , (member_ethel_id, 'Ethel A.', chapter_dev_id)
         , (member_hattie_id, 'Hattie P.', chapter_dev_id)
         , (member_zahraa_id, 'Zahraa L.', chapter_dev_id)
         , (member_shantelle_id, 'Shantelle S.', chapter_dev_id)
         , (member_ezra_id, 'Ezra B.', chapter_intern_id)
         , (member_romany_id, 'Romany E.', chapter_intern_id) ON CONFLICT (member_id) DO NOTHING;

    -- Iteration
    insert into iteration (iteration_id, number, start_date, end_date)
    values (iteration_370_id, 370, to_timestamp('01-APR-20', 'DD-MON-RRF'), to_timestamp('15-APR-20', 'DD-MON-RRF'))
         , (iteration_371_id, 371, to_timestamp('15-APR-20', 'DD-MON-RRF'), to_timestamp('29-APR-20', 'DD-MON-RRF'))
         , (iteration_372_id, 372, to_timestamp('29-APR-20', 'DD-MON-RRF'), to_timestamp('13-MAY-20', 'DD-MON-RRF'))
         , (iteration_373_id, 373, to_timestamp('13-MAY-20', 'DD-MON-RRF'), to_timestamp('27-MAY-20', 'DD-MON-RRF'))
         , (iteration_374_id, 374, to_timestamp('27-MAY-20', 'DD-MON-RRF'), to_timestamp('10-JUN-20', 'DD-MON-RRF'))
         , (iteration_375_id, 375, to_timestamp('10-JUN-20', 'DD-MON-RRF'), to_timestamp('24-JUN-20', 'DD-MON-RRF'))
         , (iteration_376_id, 376, to_timestamp('24-JUN-20', 'DD-MON-RRF'), to_timestamp('08-JUL-20', 'DD-MON-RRF')) ON CONFLICT (iteration_id) DO NOTHING;

    -- Squad Setup
    INSERT INTO squad_current_members (squad_id, member_id, rotation_date)
    values (squad_metropolitan_id, member_arian_id, to_timestamp('01-JAN-20', 'DD-MON-RR'))
         , (squad_metropolitan_id, member_rose_id, to_timestamp('01-FEB-20', 'DD-MON-RR'))
         , (squad_metropolitan_id, member_jess_id, to_timestamp('24-JUN-20', 'DD-MON-RR'))
         , (squad_metropolitan_id, member_monty_id, to_timestamp('24-JUN-20', 'DD-MON-RR'))
         , (squad_metropolitan_id, member_ethel_id, to_timestamp('27-MAY-20', 'DD-MON-RR'))
         , (squad_metropolitan_id, member_rochelle_id, to_timestamp('10-JUN-20', 'DD-MON-RR'))
         , (squad_metropolitan_id, member_zahraa_id, to_timestamp('24-JUN-20', 'DD-MON-RR'))
         , (squad_circle_id, member_lillie_id, to_timestamp('01-JAN-20', 'DD-MON-RR'))
         , (squad_circle_id, member_yan_id, to_timestamp('01-FEB-20', 'DD-MON-RR'))
         , (squad_circle_id, member_gerald_id, to_timestamp('24-JUN-20', 'DD-MON-RR'))
         , (squad_circle_id, member_monty_id, to_timestamp('24-JUN-20', 'DD-MON-RR'))
         , (squad_circle_id, member_maxine_id, to_timestamp('10-JUN-20', 'DD-MON-RR'))
         , (squad_circle_id, member_hattie_id, to_timestamp('24-JUN-20', 'DD-MON-RR'))
         , (squad_circle_id, member_romany_id, to_timestamp('23-MAR-20', 'DD-MON-RR'))
         , (squad_district_id, member_tina_id, to_timestamp('01-JAN-20', 'DD-MON-RR'))
         , (squad_district_id, member_yan_id, to_timestamp('01-FEB-20', 'DD-MON-RR'))
         , (squad_district_id, member_maryam_id, to_timestamp('24-JUN-20', 'DD-MON-RR'))
         , (squad_district_id, member_monty_id, to_timestamp('24-JUN-20', 'DD-MON-RR'))
         , (squad_district_id, member_lamar_id, to_timestamp('10-JUN-20', 'DD-MON-RR'))
         , (squad_district_id, member_shantelle_id, to_timestamp('24-JUN-20', 'DD-MON-RR'))
         , (squad_district_id, member_ezra_id, to_timestamp('01-APR-20', 'DD-MON-RR')) ON CONFLICT (squad_id, member_id) DO NOTHING;

    -- Iteration History
    insert into iteration_history (iteration_id, tribe_id, squad_id, member_id)
    values (iteration_370_id, tribe_bannock_id, squad_metropolitan_id, member_arian_id)
         , (iteration_370_id, tribe_bannock_id, squad_metropolitan_id, member_rose_id)
         , (iteration_370_id, tribe_bannock_id, squad_metropolitan_id, member_gerald_id)
         , (iteration_370_id, tribe_bannock_id, squad_metropolitan_id, member_maryam_id)
         , (iteration_370_id, tribe_bannock_id, squad_metropolitan_id, member_zahraa_id)
         , (iteration_370_id, tribe_bannock_id, squad_metropolitan_id, member_maxine_id)
         , (iteration_370_id, tribe_bannock_id, squad_metropolitan_id, member_ezra_id)
         , (iteration_370_id, tribe_bannock_id, squad_circle_id, member_lillie_id)
         , (iteration_370_id, tribe_bannock_id, squad_circle_id, member_yan_id)
         , (iteration_370_id, tribe_bannock_id, squad_circle_id, member_jess_id)
         , (iteration_370_id, tribe_bannock_id, squad_circle_id, member_maryam_id)
         , (iteration_370_id, tribe_bannock_id, squad_circle_id, member_ethel_id)
         , (iteration_370_id, tribe_bannock_id, squad_circle_id, member_hattie_id)
         , (iteration_370_id, tribe_bannock_id, squad_circle_id, member_romany_id)
         , (iteration_370_id, tribe_bannock_id, squad_district_id, member_tina_id)
         , (iteration_370_id, tribe_bannock_id, squad_district_id, member_yan_id)
         , (iteration_370_id, tribe_bannock_id, squad_district_id, member_monty_id)
         , (iteration_370_id, tribe_bannock_id, squad_district_id, member_maryam_id)
         , (iteration_370_id, tribe_bannock_id, squad_district_id, member_shantelle_id)
         , (iteration_370_id, tribe_bannock_id, squad_district_id, member_lamar_id)
         , (iteration_370_id, tribe_bannock_id, squad_district_id, member_rochelle_id)
         , (iteration_371_id, tribe_bannock_id, squad_metropolitan_id, member_arian_id)
         , (iteration_371_id, tribe_bannock_id, squad_metropolitan_id, member_rose_id)
         , (iteration_371_id, tribe_bannock_id, squad_metropolitan_id, member_monty_id)
         , (iteration_371_id, tribe_bannock_id, squad_metropolitan_id, member_gerald_id)
         , (iteration_371_id, tribe_bannock_id, squad_metropolitan_id, member_maxine_id)
         , (iteration_371_id, tribe_bannock_id, squad_metropolitan_id, member_shantelle_id)
         , (iteration_371_id, tribe_bannock_id, squad_metropolitan_id, member_ezra_id)
         , (iteration_371_id, tribe_bannock_id, squad_circle_id, member_lillie_id)
         , (iteration_371_id, tribe_bannock_id, squad_circle_id, member_yan_id)
         , (iteration_371_id, tribe_bannock_id, squad_circle_id, member_maryam_id)
         , (iteration_371_id, tribe_bannock_id, squad_circle_id, member_gerald_id)
         , (iteration_371_id, tribe_bannock_id, squad_circle_id, member_hattie_id)
         , (iteration_371_id, tribe_bannock_id, squad_circle_id, member_zahraa_id)
         , (iteration_371_id, tribe_bannock_id, squad_circle_id, member_romany_id)
         , (iteration_371_id, tribe_bannock_id, squad_district_id, member_tina_id)
         , (iteration_371_id, tribe_bannock_id, squad_district_id, member_yan_id)
         , (iteration_371_id, tribe_bannock_id, squad_district_id, member_jess_id)
         , (iteration_371_id, tribe_bannock_id, squad_district_id, member_gerald_id)
         , (iteration_371_id, tribe_bannock_id, squad_district_id, member_lamar_id)
         , (iteration_371_id, tribe_bannock_id, squad_district_id, member_rochelle_id)
         , (iteration_371_id, tribe_bannock_id, squad_district_id, member_ethel_id)
         , (iteration_372_id, tribe_bannock_id, squad_metropolitan_id, member_arian_id)
         , (iteration_372_id, tribe_bannock_id, squad_metropolitan_id, member_rose_id)
         , (iteration_372_id, tribe_bannock_id, squad_metropolitan_id, member_jess_id)
         , (iteration_372_id, tribe_bannock_id, squad_metropolitan_id, member_monty_id)
         , (iteration_372_id, tribe_bannock_id, squad_metropolitan_id, member_maxine_id)
         , (iteration_372_id, tribe_bannock_id, squad_metropolitan_id, member_shantelle_id)
         , (iteration_372_id, tribe_bannock_id, squad_metropolitan_id, member_lamar_id)
         , (iteration_372_id, tribe_bannock_id, squad_circle_id, member_lillie_id)
         , (iteration_372_id, tribe_bannock_id, squad_circle_id, member_yan_id)
         , (iteration_372_id, tribe_bannock_id, squad_circle_id, member_gerald_id)
         , (iteration_372_id, tribe_bannock_id, squad_circle_id, member_monty_id)
         , (iteration_372_id, tribe_bannock_id, squad_circle_id, member_hattie_id)
         , (iteration_372_id, tribe_bannock_id, squad_circle_id, member_zahraa_id)
         , (iteration_372_id, tribe_bannock_id, squad_circle_id, member_romany_id)
         , (iteration_372_id, tribe_bannock_id, squad_district_id, member_tina_id)
         , (iteration_372_id, tribe_bannock_id, squad_district_id, member_yan_id)
         , (iteration_372_id, tribe_bannock_id, squad_district_id, member_maryam_id)
         , (iteration_372_id, tribe_bannock_id, squad_district_id, member_monty_id)
         , (iteration_372_id, tribe_bannock_id, squad_district_id, member_rochelle_id)
         , (iteration_372_id, tribe_bannock_id, squad_district_id, member_ethel_id)
         , (iteration_372_id, tribe_bannock_id, squad_district_id, member_ezra_id)
         , (iteration_373_id, tribe_bannock_id, squad_metropolitan_id, member_arian_id)
         , (iteration_373_id, tribe_bannock_id, squad_metropolitan_id, member_rose_id)
         , (iteration_373_id, tribe_bannock_id, squad_metropolitan_id, member_maryam_id)
         , (iteration_373_id, tribe_bannock_id, squad_metropolitan_id, member_jess_id)
         , (iteration_373_id, tribe_bannock_id, squad_metropolitan_id, member_shantelle_id)
         , (iteration_373_id, tribe_bannock_id, squad_metropolitan_id, member_lamar_id)
         , (iteration_373_id, tribe_bannock_id, squad_metropolitan_id, member_hattie_id)
         , (iteration_373_id, tribe_bannock_id, squad_circle_id, member_lillie_id)
         , (iteration_373_id, tribe_bannock_id, squad_circle_id, member_yan_id)
         , (iteration_373_id, tribe_bannock_id, squad_circle_id, member_monty_id)
         , (iteration_373_id, tribe_bannock_id, squad_circle_id, member_jess_id)
         , (iteration_373_id, tribe_bannock_id, squad_circle_id, member_zahraa_id)
         , (iteration_373_id, tribe_bannock_id, squad_circle_id, member_rochelle_id)
         , (iteration_373_id, tribe_bannock_id, squad_circle_id, member_romany_id)
         , (iteration_373_id, tribe_bannock_id, squad_district_id, member_tina_id)
         , (iteration_373_id, tribe_bannock_id, squad_district_id, member_yan_id)
         , (iteration_373_id, tribe_bannock_id, squad_district_id, member_gerald_id)
         , (iteration_373_id, tribe_bannock_id, squad_district_id, member_jess_id)
         , (iteration_373_id, tribe_bannock_id, squad_district_id, member_ethel_id)
         , (iteration_373_id, tribe_bannock_id, squad_district_id, member_maxine_id)
         , (iteration_373_id, tribe_bannock_id, squad_district_id, member_ezra_id)
         , (iteration_374_id, tribe_bannock_id, squad_metropolitan_id, member_arian_id)
         , (iteration_374_id, tribe_bannock_id, squad_metropolitan_id, member_rose_id)
         , (iteration_374_id, tribe_bannock_id, squad_metropolitan_id, member_gerald_id)
         , (iteration_374_id, tribe_bannock_id, squad_metropolitan_id, member_maryam_id)
         , (iteration_374_id, tribe_bannock_id, squad_metropolitan_id, member_lamar_id)
         , (iteration_374_id, tribe_bannock_id, squad_metropolitan_id, member_hattie_id)
         , (iteration_374_id, tribe_bannock_id, squad_metropolitan_id, member_ethel_id)
         , (iteration_374_id, tribe_bannock_id, squad_circle_id, member_lillie_id)
         , (iteration_374_id, tribe_bannock_id, squad_circle_id, member_yan_id)
         , (iteration_374_id, tribe_bannock_id, squad_circle_id, member_jess_id)
         , (iteration_374_id, tribe_bannock_id, squad_circle_id, member_maryam_id)
         , (iteration_374_id, tribe_bannock_id, squad_circle_id, member_rochelle_id)
         , (iteration_374_id, tribe_bannock_id, squad_circle_id, member_shantelle_id)
         , (iteration_374_id, tribe_bannock_id, squad_circle_id, member_romany_id)
         , (iteration_374_id, tribe_bannock_id, squad_district_id, member_tina_id)
         , (iteration_374_id, tribe_bannock_id, squad_district_id, member_yan_id)
         , (iteration_374_id, tribe_bannock_id, squad_district_id, member_monty_id)
         , (iteration_374_id, tribe_bannock_id, squad_district_id, member_maryam_id)
         , (iteration_374_id, tribe_bannock_id, squad_district_id, member_maxine_id)
         , (iteration_374_id, tribe_bannock_id, squad_district_id, member_zahraa_id)
         , (iteration_374_id, tribe_bannock_id, squad_district_id, member_ezra_id)
         , (iteration_375_id, tribe_bannock_id, squad_metropolitan_id, member_arian_id)
         , (iteration_375_id, tribe_bannock_id, squad_metropolitan_id, member_rose_id)
         , (iteration_375_id, tribe_bannock_id, squad_metropolitan_id, member_monty_id)
         , (iteration_375_id, tribe_bannock_id, squad_metropolitan_id, member_gerald_id)
         , (iteration_375_id, tribe_bannock_id, squad_metropolitan_id, member_hattie_id)
         , (iteration_375_id, tribe_bannock_id, squad_metropolitan_id, member_ethel_id)
         , (iteration_375_id, tribe_bannock_id, squad_metropolitan_id, member_rochelle_id)
         , (iteration_375_id, tribe_bannock_id, squad_circle_id, member_lillie_id)
         , (iteration_375_id, tribe_bannock_id, squad_circle_id, member_yan_id)
         , (iteration_375_id, tribe_bannock_id, squad_circle_id, member_maryam_id)
         , (iteration_375_id, tribe_bannock_id, squad_circle_id, member_gerald_id)
         , (iteration_375_id, tribe_bannock_id, squad_circle_id, member_shantelle_id)
         , (iteration_375_id, tribe_bannock_id, squad_circle_id, member_maxine_id)
         , (iteration_375_id, tribe_bannock_id, squad_circle_id, member_romany_id)
         , (iteration_375_id, tribe_bannock_id, squad_district_id, member_tina_id)
         , (iteration_375_id, tribe_bannock_id, squad_district_id, member_yan_id)
         , (iteration_375_id, tribe_bannock_id, squad_district_id, member_jess_id)
         , (iteration_375_id, tribe_bannock_id, squad_district_id, member_gerald_id)
         , (iteration_375_id, tribe_bannock_id, squad_district_id, member_zahraa_id)
         , (iteration_375_id, tribe_bannock_id, squad_district_id, member_lamar_id)
         , (iteration_375_id, tribe_bannock_id, squad_district_id, member_ezra_id)
         , (iteration_376_id, tribe_bannock_id, squad_metropolitan_id, member_arian_id)
         , (iteration_376_id, tribe_bannock_id, squad_metropolitan_id, member_rose_id)
         , (iteration_376_id, tribe_bannock_id, squad_metropolitan_id, member_jess_id)
         , (iteration_376_id, tribe_bannock_id, squad_metropolitan_id, member_monty_id)
         , (iteration_376_id, tribe_bannock_id, squad_metropolitan_id, member_ethel_id)
         , (iteration_376_id, tribe_bannock_id, squad_metropolitan_id, member_rochelle_id)
         , (iteration_376_id, tribe_bannock_id, squad_metropolitan_id, member_zahraa_id)
         , (iteration_376_id, tribe_bannock_id, squad_circle_id, member_lillie_id)
         , (iteration_376_id, tribe_bannock_id, squad_circle_id, member_yan_id)
         , (iteration_376_id, tribe_bannock_id, squad_circle_id, member_gerald_id)
         , (iteration_376_id, tribe_bannock_id, squad_circle_id, member_monty_id)
         , (iteration_376_id, tribe_bannock_id, squad_circle_id, member_maxine_id)
         , (iteration_376_id, tribe_bannock_id, squad_circle_id, member_hattie_id)
         , (iteration_376_id, tribe_bannock_id, squad_circle_id, member_romany_id)
         , (iteration_376_id, tribe_bannock_id, squad_district_id, member_tina_id)
         , (iteration_376_id, tribe_bannock_id, squad_district_id, member_yan_id)
         , (iteration_376_id, tribe_bannock_id, squad_district_id, member_maryam_id)
         , (iteration_376_id, tribe_bannock_id, squad_district_id, member_monty_id)
         , (iteration_376_id, tribe_bannock_id, squad_district_id, member_lamar_id)
         , (iteration_376_id, tribe_bannock_id, squad_district_id, member_shantelle_id)
         , (iteration_376_id, tribe_bannock_id, squad_district_id, member_ezra_id) ON CONFLICT (iteration_id, tribe_id, squad_id, member_id) DO NOTHING;
END $$;

