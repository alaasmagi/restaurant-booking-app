import type { SeatFeature, TableDto } from '@/types'

export const ALL_FEATURES: SeatFeature[] = [
  'WINDOW',
  'PRIVATE',
  'KIDS_CORNER',
  'ACCESSIBLE',
  'OUTDOOR',
  'BAR_SEATING',
]

export function scoreTable(
  table: TableDto,
  peopleCount: number,
  desiredFeatures: SeatFeature[],
): number {
  const seatScore = 1 / (1 + (table.seats - peopleCount))
  const matchCount = desiredFeatures.filter((f) => table.features.includes(f)).length
  const featureScore = desiredFeatures.length > 0 ? matchCount / desiredFeatures.length : 1
  return seatScore + featureScore
}

export function recommendTables(
  tables: TableDto[],
  peopleCount: number,
  desiredFeatures: SeatFeature[],
  topN = 1,
): TableDto[] {
  return tables
    .filter((t) => t.available && t.seats >= peopleCount)
    .map((t) => ({ table: t, score: scoreTable(t, peopleCount, desiredFeatures) }))
    .sort((a, b) => b.score - a.score)
    .slice(0, topN)
    .map((s) => s.table)
}
