<script setup lang="ts">
import {ProjectInfo} from "../CommonModel.ts";
import {ref, watch} from "vue";
import {newestAPKInfo} from "../api/apk/apk.ts";

const serverURL=import.meta.env.VITE_APP_BASE_API

const dialogShowStatus = ref(false)
const projectInfo = ref<ProjectInfo>(null)
const showDialog = (info: ProjectInfo) => {
  projectInfo.value = info
  dialogShowStatus.value = true
}
const newestAPKInfoRes = ref(null)

function newestAPKInfoRequest() {
  newestAPKInfo(projectInfo.value.projectId).then(res => {
    newestAPKInfoRes.value = res
  })
}


watch(dialogShowStatus, (newVal, _) => {
  if (!newVal) {
    newestAPKInfoRes.value = null
  }
})
defineExpose({
  showDialog
})
</script>

<template>
  <n-modal style="width: 650px" :mask-closable="false" v-model:show="dialogShowStatus" closable preset="dialog">
    <template #header>
      <span>{{ projectInfo != null ? `ProjectID:${projectInfo.projectId}` : '' }}</span>
    </template>
    <div class="dialog-content">
      <div class="item">
        <n-gradient-text :size="18" type="success">
          最新APK信息获取
        </n-gradient-text>
        <n-button @click="newestAPKInfoRequest" type="info">发送请求</n-button>
      </div>
      <div class="item">
        <n-tag type="info">
          GET
        </n-tag>
        <n-tag style="width: 100%;">
          {{ serverURL }}apkManage/newest/{projectId}
        </n-tag>
      </div>
      <n-card>
        <pre v-if="newestAPKInfoRes!=null">{{ JSON.stringify(newestAPKInfoRes, null, 2) }}</pre>
      </n-card>
      <div class="item">
        <n-gradient-text :size="18" type="success">
          APK文件获取
        </n-gradient-text>
      </div>
      <n-tabs type="line" animated>
        <n-tab-pane name="文件下载地址" tab="下载地址">
          <div class="item">
            <n-tag type="info">
              GET
            </n-tag>
            <n-tag style="width: 100%;">
              {{ serverURL }}apkManage/download/{{projectInfo.projectId}}
            </n-tag>
          </div>
        </n-tab-pane>
        <n-tab-pane style="display: flex;gap: 10px;align-items: center;" name="文件下载二维码" tab="二维码">
          <n-qr-code
              size="130"
              :color="'#03602a'"
              icon-src="https://developer.android.google.cn/images/logos/android.svg"
              :value="`${serverURL}apkManage/download/${projectInfo.projectId}`"
          />
        </n-tab-pane>
      </n-tabs>
    </div>
  </n-modal>
</template>

<style scoped lang="scss">
.dialog-content {
  display: flex;
  flex-direction: column;
  gap: 10px;

  .item {
    gap: 5px;
    align-items: center;
    display: flex;
    justify-content: space-between;
  }
}
</style>
