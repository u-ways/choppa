/* eslint-disable no-shadow, no-param-reassign */
import { REMOVE_AUTHENTICATED_ACCOUNT, SET_AUTHENTICATED_ACCOUNT } from "@/config/store/mutation-types";
import { getAuthenticatedAccountSafe } from "@/config/api/account.api";

export const authState = {
  authenticatedAccount: undefined,
};

export const authGetters = {
  isAuthenticated: (state) => state.authenticatedAccount !== undefined,
  authenticatedAccount: (state) => state.authenticatedAccount,
};

export const authMutations = {
  [SET_AUTHENTICATED_ACCOUNT](state, account) {
    state.authenticatedAccount = account;
  },
  [REMOVE_AUTHENTICATED_ACCOUNT](state) {
    state.authenticatedAccount = undefined;
    // TODO: should remove the cookie? Although single-sign-off SSO, means we need to actually contact the provider.
  },
};

export const authActions = {
  async updateAuthenticationStatus(context) {
    const response = await getAuthenticatedAccountSafe();
    if (response.isAuthenticated) {
      context.commit(SET_AUTHENTICATED_ACCOUNT, response.authenticatedAccount);
    } else {
      context.commit(REMOVE_AUTHENTICATED_ACCOUNT);
    }
  },
};
