<script setup lang="ts">
import {useNotification, NotificationType} from 'naive-ui'
import {reactive, ref, watch, defineEmits} from 'vue'
import {ProjectInfo} from "../CommonModel.ts";
import {updateProject, createProject} from "../api/projects/projects.ts";

const emit = defineEmits(['upsertFinish'])
const dialogShowStatus = ref(false)
const newInfo = reactive<ProjectInfo>({
  projectId: "",
  name: "",
})
const notification = useNotification()
const updateDialogMode = ref(false)
const showDialog = (update: boolean, defaultInfo: ProjectInfo | unknown = null) => {
  dialogShowStatus.value = true
  updateDialogMode.value = update
  if (updateDialogMode.value) {
    Object.assign(newInfo, defaultInfo)
  }
}

async function dialogConfirm() {
  const notificationDuration=2500
  notification.info({
    content: `正在${updateDialogMode.value?'更新':'创建'}`,
    duration: notificationDuration,
    keepAliveOnHover: true
  })
  if (updateDialogMode.value) {
    await updateProject(newInfo.projectId, newInfo.name).then(_ => {
      notification.success({
        content: "更新项目名称成功",
        duration: notificationDuration,
        keepAliveOnHover: true
      })
      dialogShowStatus.value = false
    }).catch(err => {
      notification.error({
        content: err.msg,
        duration: notificationDuration,
        keepAliveOnHover: true
      })
    })
  } else {
    await createProject(newInfo.name).then(_ => {
      notification.success({
        content: "更新项目名称成功",
        duration: notificationDuration,
        keepAliveOnHover: true
      })
    }).catch(err => {
      console.log(err)
      notification.warning({
        content: "项目创建失败，请查看控制台报错",
        duration: notificationDuration,
        keepAliveOnHover: true
      })
    })
  }
  emit("upsertFinish")
}

watch(dialogShowStatus, (newVal, _) => {
  if (!newVal) {
    Object.assign(newInfo, {
      projectId: "",
      name: "",
    })
  }
})
defineExpose({
  showDialog
})
</script>

<template>
  <n-modal :mask-closable="false" v-model:show="dialogShowStatus" closable preset="dialog">
    <template #header>
      <div>{{ updateDialogMode ? `项目更新` : '新增项目' }}</div>
    </template>
    <template #action>
      <n-button @click="dialogConfirm" type="primary">{{ updateDialogMode ? '更新' : '新增' }}</n-button>
    </template>
    <n-form
        size="large"
        ref="formRef"
        :model="newInfo"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging">
      <n-form-item v-if="newInfo.projectId!=''" label="项目ID" path="inputValue">
        <n-input disabled v-model:value="newInfo.projectId" placeholder=""/>
      </n-form-item>
      <n-form-item label="项目名称" path="inputValue">
        <n-input v-model:value="newInfo.name" placeholder="项目名称" clearable maxlength="30" show-count/>
      </n-form-item>
    </n-form>
  </n-modal>
</template>

<style scoped lang="scss">

</style>
