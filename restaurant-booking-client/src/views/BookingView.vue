<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import FilterPanel from '@/components/FilterPanel.vue'
import FloorPlan from '@/components/FloorPlan.vue'
import BookingForm from '@/components/BookingForm.vue'
import { fetchTables, createBooking } from '@/api'
import { recommendTables } from '@/utils/recommendation'
import type { TableDto, CreateBookingDto } from '@/types'
import type { Filters } from '@/components/FilterPanel.vue'

const router = useRouter()

const tables = ref<TableDto[]>([])
const loading = ref(false)
const error = ref<string | null>(null)
const successMessage = ref<string | null>(null)

const selectedTableId = ref<string | null>(null)
const showBookingForm = ref(false)

const activeFilters = ref<Filters>({
  date: new Date().toISOString().slice(0, 10),
  startTime: '18:00',
  endTime: '20:00',
  peopleCount: 2,
  zone: '',
  features: [],
})

const isoStart = computed(
  () => `${activeFilters.value.date}T${activeFilters.value.startTime}:00`,
)
const isoEnd = computed(
  () => `${activeFilters.value.date}T${activeFilters.value.endTime}:00`,
)

const zones = computed(() => [...new Set(tables.value.map((t) => t.zone))].sort())

const recommendedIds = computed(() => {
  const recs = recommendTables(
    tables.value,
    activeFilters.value.peopleCount,
    activeFilters.value.features,
  )
  return recs.map((t) => t.id)
})

const selectedTable = computed(
  () => tables.value.find((t) => t.id === selectedTableId.value) ?? null,
)

async function loadTables() {
  loading.value = true
  error.value = null
  try {
    tables.value = await fetchTables(isoStart.value, isoEnd.value)
  } catch {
    error.value = 'Could not load tables. Make sure the backend is running on expected port.'
  } finally {
    loading.value = false
  }
}

watch([isoStart, isoEnd], loadTables, { immediate: true })

function onFiltersUpdate(filters: Filters) {
  activeFilters.value = filters
}

function onTableSelect(id: string) {
  selectedTableId.value = id
  showBookingForm.value = true
}

async function onBookingSubmit(data: CreateBookingDto) {
  error.value = null
  try {
    const booking = await createBooking(data)
    showBookingForm.value = false
    selectedTableId.value = null
    router.push(`/booking/${booking.id}`)
  } catch {
    error.value = 'Failed to create booking. Please try again.'
  }
}
</script>

<template>
  <div class="booking-view">
    <FilterPanel :zones="zones" @update="onFiltersUpdate" />

    <div v-if="error" class="alert error">{{ error }}</div>
    <div v-if="successMessage" class="alert success">{{ successMessage }}</div>

    <div v-if="loading" class="loading">Loading floor plan…</div>

    <template v-if="!loading">
      <div class="plan-header">
        <div class="legend">
          <span class="legend-item available">Available</span>
          <span class="legend-item recommended">★ Recommended</span>
          <span class="legend-item unavailable">Booked</span>
        </div>
        <p class="hint">
          Click an available table to book it. The highlighted table is the best match for your
          preferences.
        </p>
      </div>
      <FloorPlan
        :tables="tables"
        :recommended-ids="recommendedIds"
        :selected-id="selectedTableId"
        :zone="activeFilters.zone"
        @select="onTableSelect"
      />
    </template>

    <BookingForm
      v-if="showBookingForm && selectedTable"
      :table="selectedTable"
      :start-time="isoStart"
      :end-time="isoEnd"
      :people-count="activeFilters.peopleCount"
      @submit="onBookingSubmit"
      @close="showBookingForm = false"
    />
  </div>
</template>

<style scoped>
.booking-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 16px;
}

.loading {
  text-align: center;
  padding: 60px;
  color: #7a4c2e;
  font-size: 1.1rem;
}

.alert {
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 12px;
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

.plan-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 10px;
}

.legend {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.legend-item {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
}

.legend-item.available {
  background: #c7ecd4;
  color: #1a5c25;
  border: 1px solid #4caf50;
}

.legend-item.recommended {
  background: #ffe0a0;
  color: #7a4800;
  border: 1px solid #ff9800;
}

.legend-item.unavailable {
  background: #e0e0e0;
  color: #757575;
  border: 1px solid #9e9e9e;
}

.hint {
  font-size: 0.8rem;
  color: #9a6a4e;
  margin: 0;
}
</style>
