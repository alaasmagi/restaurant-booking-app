import axios from 'axios'
import type { BookingDto, CreateBookingDto, TableDto } from '@/types'

const BASE_URL = import.meta.env.VITE_API_URL
const api = axios.create({ baseURL: BASE_URL })

export function getApiErrorMessage(error: unknown): string | null {
  if (!axios.isAxiosError(error)) return null
  const data = error.response?.data as { message?: string; details?: string[] } | undefined
  if (!data) return null
  if (Array.isArray(data.details) && data.details.length > 0) {
    return data.details.join('\n')
  }
  if (typeof data.message === 'string' && data.message.trim()) {
    return data.message
  }
  return null
}

export async function fetchTables(startTime?: string, endTime?: string): Promise<TableDto[]> {
  const params: Record<string, string> = {}
  if (startTime) params.startTime = startTime
  if (endTime) params.endTime = endTime
  const { data } = await api.get<TableDto[]>('/tables', { params })
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

export async function verifyAdminPassword(
  adminUsername: string,
  adminPassword: string,
): Promise<boolean> {
  const res = await api.post<boolean>(
    '/auth/verify',
    {
      userName: adminUsername,
      password: adminPassword,
    },
    {
      validateStatus: (status) => status === 200 || status === 401 || status === 403,
    },
  )

  return res.status === 200
}

export async function setTablePosition(
  id: string,
  x: number,
  y: number,
  adminUsername: string,
  adminPassword: string,
): Promise<void> {
  await api.patch(
    `/tables/${id}/position`,
    { x, y },
    {
      auth: {
        username: adminUsername,
        password: adminPassword ?? '',
      },
    },
  )
}
