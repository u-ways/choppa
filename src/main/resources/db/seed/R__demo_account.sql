-- Demo Account
DO
$$
    BEGIN
        insert into accounts (account_id, provider, provider_id, organisation_name)
        values ('00000000000000000000000000000001', 'choppa', 'choppa-demo-account', 'Choppa Demo Org')
        ON CONFLICT (provider, provider_id) DO NOTHING;
    END
$$;

