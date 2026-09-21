import { useState } from 'react'
import ChatWindow from './components/ChatWindow'
import EmergencyAnalysis from './components/EmergencyAnalysis'
import QuickEmergencyButtons from './components/QuickEmergencyButtons'
import DemoScenarios from './components/DemoScenarios'

const welcome = { role: 'assistant', text: 'Hello. I am Rescue Me, your AI Emergency Response Assistant.\n\nDescribe what is happening and I will identify the situation and provide immediate safety guidance. If there is immediate danger, contact your local emergency services now.', time: new Date() }
export default function App() {
  const [messages, setMessages] = useState([welcome]); const [analysis, setAnalysis] = useState(null); const [loading, setLoading] = useState(false)
  const send = async (text) => {
    const user = { role: 'user', text, time: new Date() }; setMessages(current => [...current, user]); setLoading(true)
    try { const res = await fetch('http://localhost:8080/api/emergency/analyze', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ message: text }) }); const data = await res.json().catch(() => ({})); if (!res.ok || !data.success) throw new Error(data.error || 'AI service is temporarily unavailable.'); setAnalysis(data.analysis); setMessages(current => [...current, { role: 'assistant', text: `Emergency Type: ${data.analysis.emergencyType}\nSeverity: ${data.analysis.severity}\n\n${data.analysis.summary}\n\nImmediate actions:\n${data.analysis.immediateActions.map((a, i) => `• ${a}`).join('\n')}`, time: new Date() }]) }
    catch (error) { setMessages(current => [...current, { role: 'assistant', text: `${error.message}\n\nPlease contact your local emergency services directly if this is an immediate emergency.`, time: new Date() }]) }
    finally { setLoading(false) }
  }
  const clear = () => { setMessages([{ ...welcome, time: new Date() }]); setAnalysis(null) }
  return <div className="min-h-screen bg-[#071526] text-slate-100"><header className="border-b border-white/10 bg-slate-950/40"><div className="mx-auto flex max-w-7xl items-center justify-between px-5 py-5"><div className="flex items-center gap-3"><div className="grid h-11 w-11 place-items-center rounded-xl bg-gradient-to-br from-red-500 to-orange-400 text-xl shadow-lg shadow-red-500/20">✚</div><div><h1 className="font-bold tracking-tight">Rescue Me</h1><p className="text-xs text-slate-400">AI Emergency Response Assistant</p></div></div><div className="hidden items-center gap-2 rounded-full bg-emerald-400/10 px-3 py-2 text-xs font-medium text-emerald-300 sm:flex"><span className="h-2 w-2 rounded-full bg-emerald-400 shadow-[0_0_10px_#34d399]"/>AI System Online</div></div></header>
    <main className="mx-auto max-w-7xl px-5 py-8"><div className="mb-6 rounded-2xl border border-amber-300/20 bg-amber-300/5 px-4 py-3 text-sm text-amber-100"><span className="mr-2">⚠</span><strong>For immediate danger:</strong> contact local emergency services first. Rescue Me gives informational guidance only.</div><div className="grid gap-6 lg:grid-cols-[minmax(0,1.35fr)_minmax(320px,.8fr)]"><ChatWindow messages={messages} loading={loading} onSend={send} onClear={clear}/><aside className="space-y-5"><EmergencyAnalysis analysis={analysis}/><QuickEmergencyButtons onChoose={send} disabled={loading}/><DemoScenarios onChoose={send} disabled={loading}/></aside></div></main><footer className="border-t border-white/10 px-5 py-6 text-center text-xs text-slate-500">Rescue Me AI provides informational emergency guidance and does not replace professional emergency services.</footer>
  </div>
}
