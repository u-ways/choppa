import { gql } from "../../deps.ts";

export const typeDefinitions = gql`
    ############################################################
    ### GraphQL Types                                       ####
    ############################################################

    # Basic Types
    interface Id {
        id: ID!
    }
    
    # Member Type
    type Member {
        id: ID!
        name: String!
    }
    type CreateMemberType {
        id: ID!
    }
    input UpdateMemberInput {
        id: ID!
        name: String!
    }

    # Squad Type
    type Squad {
        id: ID!
        name: String!
        members: [Member]
    }
    type CreateSquadType {
        id: ID!
    }
    input UpdateSquadInput {
        id: ID!
        name: String!
        members: [UpdateMemberInput]
    }

    ############################################################
    ### GraphQL Queries                                      ###
    ############################################################

    type Query {
        # Member Queries
        FindMemberById(id: ID!): Member

        # Squad Queries
        FindSquadById(id: ID!): Squad
    }

    ############################################################
    ### GraphQL Mutations                                   ####
    ############################################################

    type Mutation {
        # Member Mutations
        CreateMember(name: String!): ID

        # Squad Mutations
        CreateSquad(name: String!): ID
    }
`;
