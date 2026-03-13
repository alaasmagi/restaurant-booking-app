<script setup lang="ts">
import { ref, watch } from 'vue'
import { ALL_FEATURES } from '@/utils/recommendation'
import { FEATURE_NAME_MAP } from '@/utils/visual'
import type { Filters, SeatFeature } from '@/types'

const props = defineProps<{ zones: string[] }>()
const emit = defineEmits<{ (e: 'update', filters: Filters): void }>()

const today = new Date().toISOString().slice(0, 10)

const date = ref(today)
const startTime = ref('18:00')
const endTime = ref('20:00')
const peopleCount = ref(2)
const zone = ref('')
const selectedFeatures = ref<SeatFeature[]>([])

function emitUpdate() {
  emit('update', {
    date: date.value,
    startTime: startTime.value,
    endTime: endTime.value,
    peopleCount: peopleCount.value,
    zone: zone.value,
    features: [...selectedFeatures.value],
  })
}

watch([date, startTime, endTime, peopleCount, zone, selectedFeatures], emitUpdate, {
  immediate: true,
  deep: true,
})
</script>

<template>
  <div class="filter-panel">
    <h2>Find a Table</h2>
    <div class="filters">
      <label>
        Date
        <input type="date" v-model="date" :min="today" />
      </label>
      <label>
        From
        <input type="time" v-model="startTime" />
      </label>
      <label>
        To
        <input type="time" v-model="endTime" />
      </label>
      <label>
        Guests
        <input type="number" v-model.number="peopleCount" min="1" max="20" />
      </label>
      <label>
        Zone
        <select v-model="zone">
          <option value="">Any Zone</option>
          <option v-for="z in zones" :key="z" :value="z">{{ z }}</option>
        </select>
      </label>
    </div>
    <div class="features">
      <span class="features-label">Preferences:</span>
      <label v-for="f in ALL_FEATURES" :key="f" class="feature-toggle">
        <input type="checkbox" :value="f" v-model="selectedFeatures" />
        {{ FEATURE_NAME_MAP[f] }}
      </label>
    </div>
  </div>
</template>

<style scoped>
.filter-panel {
  background: #fff8f2;
  border: 1px solid #e8d5c4;
  border-radius: 10px;
  padding: 16px 24px;
  margin-bottom: 20px;
}

h2 {
  margin: 0 0 12px;
  font-size: 1.2rem;
  color: var(--color-primary);
}

.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: flex-end;
}

label {
  display: flex;
  flex-direction: column;
  font-size: 0.82rem;
  color: var(--color-muted);
  font-weight: 600;
}

input,
select {
  margin-top: 4px;
  padding: 6px 8px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--color-border-strong);
  font-size: 0.9rem;
  background: var(--color-surface);
}

.features {
  margin-top: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.features-label {
  font-weight: 600;
  color: var(--color-muted);
  font-size: 0.82rem;
}

.feature-toggle {
  flex-direction: row;
  align-items: center;
  gap: 4px;
  font-weight: 400;
  font-size: 0.82rem;
  background: #f3e0d2;
  border: 1px solid var(--color-border-strong);
  border-radius: 20px;
  padding: 4px 10px;
  cursor: pointer;
}

.feature-toggle input {
  margin: 0;
  cursor: pointer;
}
</style>
