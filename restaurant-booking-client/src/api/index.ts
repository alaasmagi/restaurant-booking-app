
import axios from 'axios'
import type { BookingDto, CreateBookingDto, TableDto } from '@/types'

const BASE_URL = import.meta.env.VITE_API_URL
const api = axios.create({ baseURL: BASE_URL })

export async function fetchTables(startTime?: string, endTime?: string): Promise<TableDto[]> {
  const params: Record<string, string> = {}
  if (startTime) params.startTime = startTime
  if (endTime) params.endTime = endTime
  const { data } = await api.get<TableDto[]>('/tables', { params })
  return data
}

export async function fetchBookings(): Promise<BookingDto[]> {
  const { data } = await api.get<BookingDto[]>('/bookings')
  return data
}

export async function createBooking(data: CreateBookingDto): Promise<BookingDto> {
  const res = await api.post<BookingDto>('/bookings', data)
  return res.data
}

export async function fetchBookingById(id: string): Promise<BookingDto> {
  const { data } = await api.get<BookingDto>(`/bookings/${id}`)
  return data
}

export async function cancelBooking(id: string): Promise<void> {
  await api.patch(`/bookings/${id}/cancel`)
}

export async function setTablePosition(id: string, x: number, y: number, adminPassword?: string): Promise<void> {
  await api.patch(
    `/tables/${id}/position`,
    { x, y }, 
    { auth: {
        username: 'user',
        password: adminPassword ?? '',
    }
}
  )
}
