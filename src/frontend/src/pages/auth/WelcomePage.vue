<template>
  <StandardPageTemplate>
    <template v-slot:whole-page>
      <div class="flex-grow mx-3 grid items-center">
        <div class="w-full max-w-sm -mt-32 mx-auto flex flex-col bg-white dark:bg-gray-700 shadow
        rounded-sm ring-1 ring-black ring-opacity-5 p-5 gap-2">
          <p class="text-sm font-bold text-gray-700 dark:text-gray-200 text-center">
            Welcome 👋 to continue to Choppa please enter your organisation name
          </p>
          <StandardInputWithLabel id="organisation-name"
                                  label-text="Organisation Name"
                                  labelTextCSS="hidden"
                                  v-bind:value="organisationName"
                                  v-on:input="organisationName = $event.target.value"
                                  :validator="$v.organisationName"
                                  :custom-show-error="(v) => v.$error"
                                  :max-length="100"
          />
          <StyledButton type="button" variant="primary" css="w-full" @click="submit">
            Continue
          </StyledButton>
        </div>
      </div>
    </template>
  </StandardPageTemplate>
</template>

<script>
import StandardPageTemplate from "@/components/templates/StandardPageTemplate";
import StandardInputWithLabel from "@/components/forms/groups/StandardInputWithLabel";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import { maxLength, minLength, required } from "vuelidate/lib/validators";
import { mapActions, mapGetters } from "vuex";
import { createAccount } from "@/config/api/account.api";
import { toastVariants } from "@/enums/toastVariants";
import ToastData from "@/models/toastData";
import router from "@/config/router";

export default {
  name: "WelcomePage",
  components: {
    StandardPageTemplate,
    StandardInputWithLabel,
    StyledButton,
  },
  data() {
    return {
      organisationName: "",
    };
  },
  validations: {
    organisationName: {
      required,
      minLength: minLength(3),
      maxLength: maxLength(100),
    },
  },
  computed: {
    ...mapGetters(["authenticatedAccount"]),
  },
  methods: {
    ...mapActions(["newToast"]),
    async submit() {
      this.$v.$touch();

      if (!this.$v.$invalid) {
        this.authenticatedAccount.organisationName = this.organisationName;
        try {
          await createAccount(this.authenticatedAccount);
          await this.$store.dispatch("updateAuthenticationStatus");
          await router.push({ name: "dashboard" });
        } catch (error) {
          this.newToast(new ToastData({
            variant: toastVariants.ERROR,
            message: `Something went wrong, please try again later`,
          }));

          throw error;
        }
      }
    },
  },
};
</script>
