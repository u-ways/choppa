<template>
  <div class="inline-block">
    <font-awesome-icon icon="helicopter" :class="[ this.css ]" />
  </div>
</template>

<script>
export default {
  name: "ChoppaLogo",
  props: {
    css: String,
  },
};
</script>
