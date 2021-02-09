<template>
  <StandardPageTemplate>
    <template v-slot:whole-page>
      <div class="flex-grow mx-3 grid items-center">
        <div class="w-full max-w-sm -mt-32 mx-auto text-center flex flex-col gap-2">
          <LoginSSOButton name="Github" image-name="github" loginEndpoint="github" />
          <LoginSSOButton name="Google" image-name="google" loginEndpoint="google" />
          <LoginSSOButton name="Microsoft" image-name="microsoft" loginEndpoint="microsoft" />
          <LoginSSOButton name="Okta" image-name="okta" loginEndpoint="okta" />
        </div>
      </div>
    </template>
  </StandardPageTemplate>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import LoginSSOButton from "@/pages/auth/LoginSSOButton";
import { mapGetters } from "vuex";
import router from "@/config/router";

export default {
  name: "LoginPage",
  components: {
    LoginSSOButton,
    StandardPageTemplate,
  },
  computed: {
    ...mapGetters(["isAuthenticated"]),
  },
  mounted() {
    if (this.isAuthenticated) {
      this.redirectToDashboard();
    }
  },
  methods: {
    async redirectToDashboard() {
      await router.push({ name: "dashboard" });
    },
  },
};
</script>
