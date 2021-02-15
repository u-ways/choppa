<template>
  <StandardPageTemplate>
    <template v-slot:whole-page>
      <div class="flex-grow mx-3 grid items-center">
        <div class="w-full max-w-sm -mt-32 mx-auto text-center flex flex-col bg-white dark:bg-gray-700 shadow
        rounded-sm ring-1 ring-black ring-opacity-5 p-5">
          <p class="text-sm font-bold text-gray-700 dark:text-gray-200 mb-5">Login to continue to Choppa</p>
          <div class="grid grid-cols-2 gap-2 mb-4">
            <LoginSSOButton name="Github" image-name="github" loginEndpoint="github" />
            <LoginSSOButton name="Google" image-name="google" loginEndpoint="google" />
            <LoginSSOButton name="Microsoft" image-name="microsoft" loginEndpoint="microsoft" />
            <LoginSSOButton name="Okta" image-name="okta" loginEndpoint="okta" />
          </div>
          <p class="text-xs text-gray-600 dark:text-gray-300 mb-1">OR</p>
          <StyledButton type="button"
                        variant="primary"
                        css="bg-red-500 w-full"
                        @click="continueWithDemo">
            Continue with demo
          </StyledButton>
        </div>
      </div>
    </template>
  </StandardPageTemplate>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import LoginSSOButton from "@/pages/auth/LoginSSOButton";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import { mapActions, mapGetters } from "vuex";
import router from "@/config/router";
import { getDemoAccount } from "@/config/api/account.api";

export default {
  name: "LoginPage",
  components: {
    LoginSSOButton,
    StandardPageTemplate,
    StyledButton,
  },
  computed: {
    ...mapGetters(["isAuthenticated"]),
    demoLink() {
      const authPrefix = process.env.VUE_APP_AUTH_PREFIX || "";
      return `${authPrefix}api/accounts/demo`;
    },
  },
  mounted() {
    if (this.isAuthenticated) {
      this.redirectToDashboard();
    }
  },
  methods: {
    ...mapActions(["updateAuthenticationStatus"]),
    async redirectToDashboard() {
      await router.push({ name: "dashboard" });
    },
    async continueWithDemo() {
      await getDemoAccount();
      await this.updateAuthenticationStatus();
      await router.push({ name: "dashboard" });
    },
  },
};
</script>
