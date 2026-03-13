<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { fetchTables, setTablePosition, verifyAdminPassword, getApiErrorMessage } from '@/api'
import type { TableDto } from '@/types'
import FloorPlan from '@/components/FloorPlan.vue'

const tables = ref<TableDto[]>([])
const loading = ref(false)
const error = ref<string | null>(null)
const savingId = ref<string | null>(null)

const adminUserName = ref<string | null>(sessionStorage.getItem('adminUserName'))
const adminPassword = ref<string | null>(sessionStorage.getItem('adminPassword'))

const usernameInput = ref('')
const passwordInput = ref('')
const loginError = ref(false)

async function submitPassword() {
  if (!usernameInput.value.trim() || !passwordInput.value.trim()) return
  loginError.value = (await verifyAdminPassword(usernameInput.value, passwordInput.value))
    ? false
    : true

  if (loginError.value) return

  sessionStorage.setItem('adminUserName', usernameInput.value)
  sessionStorage.setItem('adminPassword', passwordInput.value)
  adminUserName.value = usernameInput.value
  adminPassword.value = passwordInput.value
  load()
}

async function load() {
  loading.value = true
  error.value = null
  try {
    tables.value = await fetchTables()
  } catch (err) {
    const apiMessage = getApiErrorMessage(err)
    error.value = apiMessage ?? 'Could not load tables. Make sure the backend is running.'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (adminPassword.value !== null) load()
})

async function onTableMove({ id, x, y }: { id: string; x: number; y: number }) {
  const t = tables.value.find((t) => t.id === id)
  if (!t) return
  const prev = { x: t.x, y: t.y }
  t.x = x
  t.y = y
  savingId.value = id
  error.value = null
  try {
    await setTablePosition(id, x, y, adminUserName.value ?? '', adminPassword.value ?? '')
  } catch (err) {
    t.x = prev.x
    t.y = prev.y
    const apiMessage = getApiErrorMessage(err)
    sessionStorage.removeItem('adminUserName')
    sessionStorage.removeItem('adminPassword')
    adminUserName.value = null
    adminPassword.value = null
    usernameInput.value = ''
    passwordInput.value = ''
    loginError.value = true
    error.value = apiMessage ?? 'Failed to save table position.'
  } finally {
    savingId.value = null
  }
}
</script>

<template>
  <div class="admin-view">
    <div v-if="adminPassword === null" class="login-gate">
      <div class="login-card">
        <h2>Admin Access</h2>
        <p>Enter the admin username and password to access the table layout editor.</p>
        <div v-if="loginError" class="alert error">
          Incorrect username or password. Please try again.
        </div>
        <form @submit.prevent="submitPassword" class="login-form">
          <input v-model="usernameInput" type="text" placeholder="Username" class="login-input" />
          <input
            v-model="passwordInput"
            type="password"
            placeholder="Password"
            class="login-input"
            autofocus
          />
          <button type="submit" class="login-btn">Log in</button>
        </form>
      </div>
    </div>

    <template v-else>
      <h1>Admin — Table Layout Editor</h1>
      <p class="hint">
        Drag tables to reposition them. Positions are saved to the backend on release.
      </p>

      <div v-if="error" class="alert error">{{ error }}</div>
      <div v-if="loading" class="loading">Loading tables…</div>

      <FloorPlan
        v-if="!loading"
        :tables="tables"
        :recommended-ids="[]"
        :selected-id="savingId"
        zone=""
        :draggable="true"
        @move="onTableMove"
      />
    </template>
  </div>
</template>

<style scoped>
.admin-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 16px;
}

.login-gate {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

.login-card {
  background: var(--color-surface-soft);
  border: 1px solid var(--color-border);
  border-radius: 16px;
  padding: 40px 48px;
  text-align: center;
  max-width: 360px;
  width: 100%;
  box-shadow: 0 4px 24px rgba(92, 45, 10, 0.08);
}

.login-card h2 {
  color: var(--color-primary);
  margin-bottom: 8px;
}

.login-card p {
  color: var(--color-muted);
  font-size: 0.9rem;
  margin-bottom: 20px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.login-input {
  padding: 10px 14px;
  border: 1px solid #cbb99a;
  border-radius: var(--radius-md);
  font-size: 1rem;
  outline: none;
  transition: border-color 0.2s;
}

.login-input:focus {
  border-color: #a0522d;
}

.login-btn {
  padding: 10px;
  background: #a0522d;
  color: var(--color-surface);
  border: none;
  border-radius: var(--radius-md);
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
}

.login-btn:hover {
  background: #7a3a1a;
}

h1 {
  color: var(--color-primary);
  margin-bottom: 4px;
}

.hint {
  color: var(--color-muted);
  font-size: 0.85rem;
  margin-bottom: 16px;
}

.loading {
  padding: 60px;
  text-align: center;
  color: var(--color-muted);
}

.alert {
  padding: 12px;
  border-radius: var(--radius-md);
  margin-bottom: 12px;
  font-size: 0.9rem;
}

.error {
  background: var(--color-danger-bg);
  color: var(--color-danger);
  border: 1px solid var(--color-danger-border);
}
</style>
