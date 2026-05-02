'use client'

import { useEffect, useState } from 'react'

type Kpi = {
  totalCost: number | string
  totalTokens: number
  blockedRequests: number
}

export default function Home() {
  const [kpi, setKpi] = useState<Kpi | null>(null)

  useEffect(() => {
    fetch('/api/kpi')
      .then(res => res.json())
      .then(data => setKpi(data))
  }, [])

  const totalCost = Number(kpi?.totalCost ?? 0)
  const totalTokens = Number(kpi?.totalTokens ?? 0)
  const blockedRequests = Number(kpi?.blockedRequests ?? 0)

  return (
    <main className="min-h-screen bg-slate-900 text-slate-200 p-4 md:p-6 lg:p-8">
      <nav className="mb-8 flex flex-col md:flex-row justify-between items-center bg-slate-800 p-4 rounded-xl border border-slate-700 shadow-lg">
        <div className="flex items-center space-x-3 mb-4 md:mb-0">
          <div className="w-10 h-10 rounded-lg bg-emerald-400 flex items-center justify-center text-2xl">
            🐷
          </div>
          <div>
            <h1 className="text-xl font-bold text-white tracking-wide">
              Spring AI Ledger <span className="text-emerald-400">Cloud</span>
            </h1>
            <p className="text-xs text-slate-400">엔터프라이즈 LLMOps 관측 및 제어 플랫폼</p>
          </div>
        </div>

        <div className="flex items-center space-x-3 w-full md:w-auto">
          <input
            type="text"
            placeholder="테넌트, 모델, 알림 검색..."
            className="w-full md:w-64 bg-slate-900 border border-slate-700 text-sm rounded-lg px-4 py-2 focus:outline-none focus:border-emerald-500"
          />
          <button className="p-2 bg-slate-900 border border-slate-700 rounded-lg hover:bg-slate-700">
            🔔
          </button>
          <button className="p-2 bg-slate-900 border border-slate-700 rounded-lg hover:bg-slate-700">
            ⚙️
          </button>
        </div>
      </nav>

      <section className="flex flex-col sm:flex-row justify-between items-end mb-6 gap-4">
        <div>
          <h2 className="text-2xl font-bold text-white">대시보드 개요</h2>
          <p className="text-sm text-slate-400 mt-1">
            API 호출 비용 및 토큰 사용량 실시간 모니터링
          </p>
        </div>

        <div className="flex bg-slate-800 border border-slate-700 rounded-lg p-1 shadow-sm">
          <button className="px-4 py-1.5 text-xs font-medium rounded-md text-slate-400">
            오늘
          </button>
          <button className="px-4 py-1.5 text-xs font-medium rounded-md bg-emerald-500/20 text-emerald-400">
            이번 주
          </button>
          <button className="px-4 py-1.5 text-xs font-medium rounded-md text-slate-400">
            이번 달
          </button>
        </div>
      </section>

      {!kpi ? (
        <div className="rounded-xl border border-slate-700 bg-slate-800 p-8 text-slate-400">
          로딩중...
        </div>
      ) : (
        <>
          <section className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <KpiCard
              title="총 발생 비용 (USD)"
              value={`$${totalCost.toFixed(8)}`}
              icon="💰"
              accent="emerald"
              sub="RDS usage_logs 기준"
            />
            <KpiCard
              title="총 사용 토큰 (Tokens)"
              value={totalTokens.toLocaleString()}
              icon="🔢"
              accent="blue"
              sub="입력 + 출력 토큰 합산"
            />
            <KpiCard
              title="차단된 요청 (Circuit Broken)"
              value={`${blockedRequests} 건`}
              icon="🛡️"
              accent="rose"
              sub="BLOCKED 상태 요청 수"
            />
          </section>

          <section className="bg-slate-800 rounded-xl border border-slate-700 p-6 shadow-lg mb-8">
            <div className="flex justify-between items-center mb-6">
              <h3 className="text-lg font-bold text-white">AI 모델별 비용 발생 추이</h3>
              <div className="text-sm text-slate-400">projectId=1 / period=week</div>
            </div>

            <div className="h-64 flex items-end gap-4 border-b border-slate-700 pl-2">
              {[
                { label: 'Mon', value: 20 },
                { label: 'Tue', value: 35 },
                { label: 'Wed', value: 45 },
                { label: 'Thu', value: 30 },
                { label: 'Fri', value: 70 },
                { label: 'Sat', value: 50 },
                { label: 'Sun', value: 85 },
              ].map(item => (
                <div key={item.label} className="flex-1 flex flex-col items-center gap-2">
                  <div
                    className="w-full max-w-14 rounded-t-md bg-emerald-500/80 hover:bg-emerald-400 transition"
                    style={{ height: `${item.value * 2}px` }}
                  />
                  <span className="text-xs text-slate-500">{item.label}</span>
                </div>
              ))}
            </div>
          </section>

          <section className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            <div className="bg-slate-800 rounded-xl border border-slate-700 shadow-lg overflow-hidden">
              <div className="p-5 border-b border-slate-700 flex justify-between items-center">
                <h3 className="text-lg font-bold text-white">모델별 비용 랭킹</h3>
                <span className="text-xs text-slate-400 bg-slate-900 px-2 py-1 rounded">
                  최상위 발생 순
                </span>
              </div>

              <table className="w-full text-left text-sm">
                <thead className="bg-slate-900/50 text-slate-400 text-xs">
                  <tr>
                    <th className="px-5 py-3">모델</th>
                    <th className="px-5 py-3 text-right">비용</th>
                    <th className="px-5 py-3 text-right">토큰</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-700/50">
                  <tr>
                    <td className="px-5 py-4 text-slate-200">gpt-4o-mini</td>
                    <td className="px-5 py-4 text-right text-emerald-400">
                      ${totalCost.toFixed(8)}
                    </td>
                    <td className="px-5 py-4 text-right">{totalTokens.toLocaleString()}</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div className="bg-slate-800 rounded-xl border border-slate-700 shadow-lg">
              <div className="p-5 border-b border-slate-700">
                <h3 className="text-lg font-bold text-white">시스템 이벤트 로그</h3>
              </div>

              <div className="p-5 space-y-4">
                <EventCard
                  title="EC2 API 연결 완료"
                  message="Vercel Proxy를 통해 Spring Boot API 응답을 수신했습니다."
                  type="SUCCESS"
                />
                <EventCard
                  title="RDS MySQL 조회 완료"
                  message="usage_logs 테이블 기준 비용과 토큰 집계가 완료되었습니다."
                  type="INFO"
                />
                <EventCard
                  title="예산 차단 요청 없음"
                  message="현재 BLOCKED 상태 요청은 발생하지 않았습니다."
                  type="READY"
                />
              </div>
            </div>
          </section>
        </>
      )}
    </main>
  )
}

function KpiCard({
  title,
  value,
  icon,
  accent,
  sub,
}: {
  title: string
  value: string
  icon: string
  accent: 'emerald' | 'blue' | 'rose'
  sub: string
}) {
  const colorMap = {
    emerald: 'text-emerald-400 bg-emerald-500/10',
    blue: 'text-blue-400 bg-blue-500/10',
    rose: 'text-rose-400 bg-rose-500/10',
  }

  return (
    <div className="bg-slate-800 rounded-xl border border-slate-700 p-6 shadow-lg relative overflow-hidden group">
      <div className={`absolute top-0 right-0 w-24 h-24 ${colorMap[accent]} rounded-bl-full -mr-4 -mt-4`} />
      <div className="flex justify-between items-start relative z-10">
        <div>
          <p className="text-slate-400 text-sm font-medium mb-1">{title}</p>
          <h3 className={`text-3xl font-bold ${accent === 'rose' ? 'text-rose-400' : 'text-white'}`}>
            {value}
          </h3>
        </div>
        <div className="p-3 bg-slate-900 rounded-lg border border-slate-700 text-xl">
          {icon}
        </div>
      </div>
      <div className="mt-4 text-sm text-slate-500">{sub}</div>
    </div>
  )
}

function EventCard({
  title,
  message,
  type,
}: {
  title: string
  message: string
  type: 'SUCCESS' | 'INFO' | 'READY'
}) {
  return (
    <div className="flex items-start bg-slate-900/50 p-3 rounded-lg border border-slate-700/50">
      <div className="mr-3 mt-0.5">
        {type === 'SUCCESS' ? '✅' : type === 'INFO' ? '📡' : '🟢'}
      </div>
      <div>
        <div className="text-sm font-medium text-slate-200">{title}</div>
        <p className="text-sm text-slate-400 mt-1">{message}</p>
      </div>
    </div>
  )
}