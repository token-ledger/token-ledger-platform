export async function GET() {
  const res = await fetch(
    'http://52.78.69.13:8080/api/dashboard/model-cost-summary?projectId=1&period=week',
    { cache: 'no-store' }
  )

  const data = await res.json()
  return Response.json(data)
}