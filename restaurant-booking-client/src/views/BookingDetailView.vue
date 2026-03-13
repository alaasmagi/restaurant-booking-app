<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchBookingById, cancelBooking } from '@/api'
import type { BookingDto } from '@/types'

const route = useRoute()
const router = useRouter()

const booking = ref<BookingDto | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)

const cancelling = ref(false)
const confirmCancelOpen = ref(false)

onMounted(async () => {
  try {
    booking.value = await fetchBookingById(route.params.id as string)
  } catch {
    error.value = 'Booking not found.'
  } finally {
    loading.value = false
  }
})


async function handleCancel() {
  if (!booking.value) return
  cancelling.value = true
  error.value = null
  try {
    await cancelBooking(booking.value.id)
    booking.value = await fetchBookingById(booking.value.id)
  } catch {
    error.value = 'Failed to cancel booking. Please try again.'
  } finally {
    cancelling.value = false
  }
}

function requestCancel() {
  if (cancelling.value) return
  confirmCancelOpen.value = true
}

function closeConfirm() {
  if (cancelling.value) return
  confirmCancelOpen.value = false
}

async function confirmCancel() {
  if (cancelling.value) return
  confirmCancelOpen.value = false
  await handleCancel()
}

function formatDateTime(iso: string): string {
  return new Date(iso).toLocaleString('en-GB', {
    dateStyle: 'medium',
    timeStyle: 'short',
  })
}
</script>

<template>
  <div class="detail-view">
    <button class="back-btn" @click="router.push('/')">← Back to booking</button>

    <div v-if="loading" class="loading">Loading booking…</div>
    <div v-else-if="error && !booking" class="alert error">{{ error }}</div>

    <div v-else-if="booking" class="card">
      <div class="card-header">
        <h2>Booking Confirmation</h2>
        <span class="status" :class="{ cancelled: booking.status === 'cancelled' }">
          <template v-if="booking.status === 'cancelled'">✗ Cancelled</template>
          <template v-else-if="booking.status === 'active'">✓ Active</template>
          <template v-else>{{ booking.status }}</template>
        </span>
      </div>

      <div class="section">
        <h3>📋 Booking Details</h3>
        <div class="row"><span>Booking ID</span><span>{{ booking.id }}</span></div>
        <div class="row"><span>From</span><span>{{ formatDateTime(booking.startTime) }}</span></div>
        <div class="row"><span>To</span><span>{{ formatDateTime(booking.endTime) }}</span></div>
        <div class="row"><span>Guests</span><span>{{ booking.peopleCount }}</span></div>
      </div>

      <div class="section">
        <h3>👤 Customer Details</h3>
        <div class="row"><span>Name</span><span>{{ booking.customerName }}</span></div>
        <div class="row"><span>Phone</span><span>{{ booking.customerPhone }}</span></div>
        <div class="row"><span>Email</span><span>{{ booking.customerEmail }}</span></div>
      </div>

      <div v-if="error" class="alert error">{{ error }}</div>

      <div v-if="booking.status !== 'cancelled'" class="actions">
        <button class="cancel-btn" :disabled="cancelling" @click="requestCancel">
          {{ cancelling ? 'Cancelling…' : 'Cancel Booking' }}
        </button>
      </div>

      <div v-if="booking.status === 'cancelled'" class="alert success">
        Your booking has been cancelled successfully.
      </div>
    </div>

    <div v-if="confirmCancelOpen" class="modal-backdrop" @click="closeConfirm">
      <div class="modal" role="dialog" aria-modal="true" @click.stop>
        <h4>Cancel this booking?</h4>
        <p>This action cannot be undone.</p>
        <div class="modal-actions">
          <button class="ghost-btn" :disabled="cancelling" @click="closeConfirm">
            Keep Booking
          </button>
          <button class="danger-btn" :disabled="cancelling" @click="confirmCancel">
            {{ cancelling ? 'Cancelling…' : 'Yes, Cancel' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.detail-view {
  max-width: 600px;
  margin: 40px auto;
  padding: 0 16px;
}

.back-btn {
  background: none;
  border: none;
  color: #8b4513;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  padding: 0 0 16px;
  display: block;
}

.back-btn:hover {
  color: #5c2d0a;
}

.loading {
  padding: 60px;
  text-align: center;
  color: #7a4c2e;
}

.card {
  background: white;
  border-radius: 14px;
  padding: 28px 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

h2 {
  margin: 0;
  color: #5c2d0a;
}

.status {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.82rem;
  font-weight: 700;
  background: #e8f5e9;
  color: #2e7d32;
  border: 1px solid #a5d6a7;
}

.status.cancelled {
  background: #ffebee;
  color: #c62828;
  border-color: #ef9a9a;
}

.section {
  margin-bottom: 20px;
}

h3 {
  margin: 0 0 10px;
  font-size: 0.95rem;
  color: #7a4c2e;
  border-bottom: 1px solid #f0ddd0;
  padding-bottom: 6px;
}

.row {
  display: flex;
  justify-content: space-between;
  padding: 7px 0;
  font-size: 0.9rem;
  border-bottom: 1px solid #faf0ea;
  gap: 12px;
}

.row span:first-child {
  color: #9a6a4e;
  font-weight: 600;
  flex-shrink: 0;
}

.actions {
  margin-top: 24px;
}

.cancel-btn {
  width: 100%;
  padding: 12px;
  background: #c62828;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
}

.cancel-btn:hover:not(:disabled) {
  background: #a31515;
}

.cancel-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(26, 12, 6, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  z-index: 20;
}

.modal {
  width: 100%;
  max-width: 420px;
  background: white;
  border-radius: 14px;
  padding: 20px 22px;
  box-shadow: 0 18px 40px rgba(0, 0, 0, 0.22);
}

.modal h4 {
  margin: 0 0 8px;
  color: #5c2d0a;
}

.modal p {
  margin: 0;
  color: #7a4c2e;
  font-size: 0.92rem;
}

.modal-actions {
  display: flex;
  gap: 10px;
  margin-top: 18px;
}

.ghost-btn,
.danger-btn {
  flex: 1;
  padding: 10px 12px;
  border-radius: 8px;
  font-weight: 700;
  border: 1px solid transparent;
  cursor: pointer;
}

.ghost-btn {
  background: #fff7f0;
  color: #7a4c2e;
  border-color: #f0ddd0;
}

.ghost-btn:hover:not(:disabled) {
  background: #f6e8dc;
}

.danger-btn {
  background: #c62828;
  color: white;
}

.danger-btn:hover:not(:disabled) {
  background: #a31515;
}

.ghost-btn:disabled,
.danger-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.alert {
  padding: 12px 16px;
  border-radius: 8px;
  margin-top: 16px;
  font-size: 0.9rem;
}

.error {
  background: #ffebee;
  color: #c62828;
  border: 1px solid #ef9a9a;
}

.success {
  background: #e8f5e9;
  color: #2e7d32;
  border: 1px solid #a5d6a7;
}
</style>
