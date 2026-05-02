export async function GET() {
  const res = await fetch(
    'http://52.78.69.13:8080/api/dashboard/kpi?projectId=1&period=week'
  )

  const data = await res.json()

  return Response.json(data)
}