import type { SeatFeature, TableDto } from '@/types'

// Floor size calculation constants
export const GRID_UNIT = 104
const MIN_WIDTH = 740
const MIN_HEIGHT = 290
const PADDING = 80

// Table size calculation constants
const BASE_SIZE = 108
const SEAT_SIZE = 17

export const FEATURE_NAME_MAP: Record<SeatFeature, string> = {
  WINDOW: 'Window',
  PRIVATE: 'Private',
  KIDS_CORNER: 'Kids Corner',
  ACCESSIBLE: 'Accessible',
  OUTDOOR: 'Outdoor',
  BAR_SEATING: 'Bar Seating',
}

export const FEATURE_ICONS: Record<SeatFeature, string> = {
  WINDOW: '🪟',
  PRIVATE: '🔒',
  KIDS_CORNER: '🧸',
  ACCESSIBLE: '♿',
  OUTDOOR: '🌿',
  BAR_SEATING: '🍸',
}

export function calculateFloorSize(tables: TableDto[]): { width: number; height: number } {
  if (!tables.length) {
    return { width: MIN_WIDTH, height: MIN_HEIGHT }
  }

  const maxRight = Math.max(
    ...tables.map((table) => table.x * GRID_UNIT + calculateTableSize(table).width),
  )

  const maxBottom = Math.max(
    ...tables.map((table) => table.y * GRID_UNIT + calculateTableSize(table).height),
  )

  return {
    width: Math.max(MIN_WIDTH, maxRight + PADDING),
    height: Math.max(MIN_HEIGHT, maxBottom + PADDING),
  }
}

export function calculateTableSize(table: TableDto): { width: number; height: number } {
  const extra = table.seats > 4 ? (table.seats - 4) * SEAT_SIZE : 0

  return {
    width: BASE_SIZE + extra,
    height: BASE_SIZE + extra,
  }
}
