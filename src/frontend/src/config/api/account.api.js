import httpClient from "@/config/api/http-client";
import Account from "@/models/domain/account";

function serializeAccount(account) {
  return {
    id: account.id,
    provider: account.provider,
    providerId: account.providerId,
    organisationName: account.organisationName,
  };
}

async function deserializeAccount(json) {
  return new Account({
    id: json.id,
    provider: json.provider,
    providerId: json.providerId,
    name: json.name,
    organisationName: json.organisationName,
    profilePicture: json.profilePicture,
  });
}

export async function getAuthenticatedAccountSafe() {
  let isAuthenticated = false;
  let authenticatedAccount;

  try {
    const response = await httpClient.get("accounts/me");
    authenticatedAccount = await deserializeAccount(response.data);
    isAuthenticated = true;
  } catch (error) {
    if (error.response.status !== 401) {
      throw new Error(error);
    }
  }

  return {
    isAuthenticated,
    authenticatedAccount,
  };
}

export async function invalidateSession() {
  await httpClient.post("logout");
}

export async function getDemoAccount() {
  await httpClient.get("accounts/demo");
}

export async function createAccount(account) {
  await httpClient.post("accounts", serializeAccount(account));
}
