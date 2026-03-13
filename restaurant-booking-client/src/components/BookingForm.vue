<script setup lang="ts">
import { ref, watch } from 'vue'
import { type SeatFeature, type TableDto, type CreateBookingDto } from '@/types'
import { FEATURE_NAME_MAP } from '@/utils/visual'

const props = defineProps<{
  table: TableDto | null
  startTime: string
  endTime: string
  peopleCount: number
  preferences: SeatFeature[]
}>()

const emit = defineEmits<{
  (e: 'submit', data: CreateBookingDto): void
  (e: 'close'): void
}>()

const customerName = ref('')
const customerPhone = ref('')
const customerEmail = ref('')
const people = ref(props.peopleCount)

watch(
  () => props.peopleCount,
  (v) => {
    people.value = v
  },
)

function handleSubmit() {
  if (!props.table) return
  emit('submit', {
    tableId: props.table.id,
    customerName: customerName.value.trim(),
    customerPhone: customerPhone.value.trim(),
    customerEmail: customerEmail.value.trim(),
    peopleCount: people.value,
    preferences: props.preferences,
    startTime: props.startTime,
    endTime: props.endTime,
  })
}

function formatTime(iso: string): string {
  return iso.replace('T', ' ').slice(0, 16)
}
</script>

<template>
  <div class="modal-backdrop" @click.self="emit('close')">
    <div class="modal">
      <button class="close-btn" @click="emit('close')" aria-label="Close">✕</button>
      <h2>Book a Table</h2>

      <div v-if="table" class="table-info">
        <span>{{ table.seats }} seats · Zone: {{ table.zone }}</span>
        <span v-if="table.features.length">
          Features: {{ table.features.map((f) => FEATURE_NAME_MAP[f]).join(', ') }}
        </span>
        <span>🕐 {{ formatTime(startTime) }} – {{ formatTime(endTime) }}</span>
      </div>

      <form @submit.prevent="handleSubmit" novalidate>
        <label>
          Full Name *
          <input v-model="customerName" required placeholder="Jane Smith" autocomplete="name" />
        </label>
        <label>
          Phone *
          <input
            v-model="customerPhone"
            required
            placeholder="+1234567890"
            type="tel"
            autocomplete="tel"
          />
        </label>
        <label>
          Email *
          <input
            v-model="customerEmail"
            required
            placeholder="jane@example.com"
            type="email"
            autocomplete="email"
          />
        </label>
        <label>
          Guests *
          <input v-model.number="people" required type="number" min="1" :max="table?.seats ?? 20" />
        </label>
        <button type="submit" class="submit-btn">Confirm Booking</button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.modal {
  background: var(--color-surface);
  border-radius: var(--radius-xxl);
  padding: 28px 32px;
  min-width: 340px;
  max-width: 440px;
  width: 100%;
  position: relative;
  box-shadow: var(--shadow-lg);
}

.close-btn {
  position: absolute;
  top: 12px;
  right: 16px;
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #999;
  line-height: 1;
}

.close-btn:hover {
  color: #333;
}

h2 {
  margin: 0 0 12px;
  color: var(--color-primary);
}

.table-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 0.85rem;
  color: var(--color-muted);
  background: var(--color-surface-soft);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  padding: 8px 12px;
  margin-bottom: 16px;
}

form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

label {
  display: flex;
  flex-direction: column;
  font-size: 0.82rem;
  font-weight: 600;
  color: var(--color-muted);
}

input {
  margin-top: 4px;
  padding: 8px 10px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--color-border-strong);
  font-size: 0.9rem;
}

input:focus {
  outline: 2px solid var(--color-primary-muted);
  border-color: var(--color-primary-muted);
}

.submit-btn {
  margin-top: 8px;
  padding: 10px;
  background: var(--color-primary-muted);
  color: var(--color-surface);
  border: none;
  border-radius: var(--radius-md);
  font-size: 1rem;
  cursor: pointer;
  font-weight: 700;
}

.submit-btn:hover {
  background: var(--color-primary-dark);
}
</style>
