<template>
  <n-config-provider :style="`background-image: linear-gradient(122deg, ${appBg.first}, ${appBg.end})`" class="provider" :theme="theme" :theme-overrides="appTheme">
    <n-dialog-provider>
      <n-notification-provider>
        <n-message-provider>
          <Home @useDarkTheme="changeTheme"/>
        </n-message-provider>
      </n-notification-provider>
    </n-dialog-provider>
  </n-config-provider>
</template>
<script setup lang="ts">
import {darkTheme, GlobalThemeOverrides} from 'naive-ui'
import type {GlobalTheme} from 'naive-ui'
import Home from "./home/Home.vue";
import {ref} from "vue";

const theme = ref<GlobalTheme | null>(null)
const appBg = ref({
  first:"#42e6d3",
  end:"#21b096",
})
function changeTheme(use: boolean) {
  if (use) {
    appBg.value.first ="#093028"
    appBg.value.end ="#012a1c"
    theme.value = darkTheme
  } else {
    appBg.value.first ="#42e6d3"
    appBg.value.end ="#21b096"
    theme.value = null
  }
}

const appTheme: GlobalThemeOverrides = {
  common: {

  },
  Button: {}
}
</script>
<style lang="scss" scoped>
.provider {
  transition: 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
