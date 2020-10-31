<template>
  <nav class="container-fluid navbar">
    <div class="navbar__element ml-1 ml-sm-3 navbar__link">
      <router-link to="/">
        <div class="justify-content-start">
          <div class="d-inline-block pr-1">
            <ChoppaLogoAtom css="navbar__logo"/>
          </div>
          <div class="navbar__name d-inline-block">
            Choppa<span class="navbar__name--light">.app</span>
          </div>
        </div>
      </router-link>
    </div>
  </nav>
</template>

<script>
import ChoppaLogoAtom from "@/components/atoms/ChoppaLogoAtom";

export default {
  components: { ChoppaLogoAtom },
};
</script>

<style lang="scss">
@import "src/assets/scss/colours";
@import "src/assets/scss/typography";

.navbar {
  margin: 0;
  padding: 0;
  background: $white;
  box-shadow: 0 2px 6px 0 hsla(0, 0%, 0%, 0.2);

  &__element {
    padding: 0.8rem 0.5rem 0.2rem;
    border-bottom: 3px solid hsla(0, 0, 0, 0);
  }

  &__link {
    &:hover,
    &:focus {
      border-bottom-color: $accent;
      transition: 0.4s border-bottom-color;
    }
  }

  &__logo {
    font-size: 2rem;
    color: $text-dark;
  }

  &__name {
    font-size: 1.45rem;
    font-weight: $font-weight-body-medium;
    color: $dark-grey;

    &--light {
      font-weight: $font-weight-body-light;
    }
  }
}
</style>
