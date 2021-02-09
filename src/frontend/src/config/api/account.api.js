import httpClient from "@/config/api/http-client";
import Account from "@/models/domain/account";

async function deserializeAccount(json) {
  return new Account({
    name: json.name,
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
