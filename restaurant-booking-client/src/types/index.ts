export const FEATURE_NAME_MAP: Record<string, string> = {
  WINDOW: 'Window',
  PRIVATE: 'Private',
  KIDS_CORNER: 'Kids Corner',
  ACCESSIBLE: 'Accessible',
  OUTDOOR: 'Outdoor',
  BAR_SEATING: 'Bar Seating',
}
export interface TableDto {
  id: string
  seats: number
  zone: string
  features: string[]
  x: number
  y: number
  available: boolean
}

export interface BookingDto {
  id: string
  tableId: string
  status: string
  customerName: string
  customerPhone: string
  customerEmail: string
  peopleCount: number
  startTime: string
  endTime: string
}

export interface CreateBookingDto {
  tableId: string
  customerName: string
  customerPhone: string
  customerEmail: string
  peopleCount: number
  startTime: string
  endTime: string
}
