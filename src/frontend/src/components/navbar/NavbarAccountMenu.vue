<template>
  <div class="fixed top-0 left-0 right-0 bottom-0 z-10">
    <div class="left-0 right-0 top-0 bottom-0 absolute bg-white dark:bg-gray-700">
      <IconButton icon="times"
                  class="absolute top-0 right-0 mr-2 mt-1 hover:bg-gray-200 dark:hover:bg-choppa-dark"
                  screen-reader-text="Close account menu"
                  @click="hideAccountMenu"
      />
      <div class="px-5 py-3 flex flex-col items-center gap-2 h-full justify-center -mt-12">
        <Avatar class="w-20 h-20"
                :seed="authenticatedAccount.avatarSeed"
                :imageUrlOverride="authenticatedAccount.profilePicture"
        />
        <div class="text-center text-sm flex flex-col dark:text-gray-200">
          <div class="font-bold">{{authenticatedAccount.name}}</div>
          <div>{{ authenticatedAccount.organisationName }}</div>
        </div>
        <StyledButton type="button" variant="primary" @click="onLogoutClicked">
          Logout
        </StyledButton>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import Avatar from "@/components/member/Avatar";
import IconButton from "@/components/atoms/buttons/IconButton";
import StyledButton from "@/components/atoms/buttons/StyledButton";
import router from "@/config/router";

export default {
  name: "NavbarAccountMenu",
  components: {
    Avatar,
    IconButton,
    StyledButton,
  },
  computed: {
    ...mapGetters(["authenticatedAccount"]),
  },
  methods: {
    ...mapActions(["logOut"]),
    hideAccountMenu() {
      this.$emit("hide");
    },
    async onLogoutClicked() {
      await this.logOut();

      if (this.$route.name !== "home") {
        await router.push({ name: "home" });
      }
    },
  },
};
</script>
