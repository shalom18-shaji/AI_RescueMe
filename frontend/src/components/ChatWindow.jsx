import { useEffect, useRef, useState } from 'react'
import ChatMessage from './ChatMessage'

export default function ChatWindow({ messages, loading, onSend, onClear }) {
  const [message, setMessage] = useState(''); const endRef = useRef(null)
  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages, loading])
  const submit = (event) => { event.preventDefault(); if (message.trim() && !loading) { onSend(message); setMessage('') } }
  return <section className="flex min-h-[570px] flex-col rounded-3xl border border-white/10 bg-slate-900/70 shadow-2xl shadow-slate-950/30 backdrop-blur">
    <div className="flex items-center justify-between border-b border-white/10 px-5 py-4"><div><h2 className="font-semibold">Emergency chat</h2><p className="text-xs text-slate-400">Describe what is happening in your own words.</p></div><button onClick={onClear} className="rounded-lg px-3 py-2 text-xs font-medium text-slate-300 hover:bg-white/10 hover:text-white">Clear chat</button></div>
    <div className="flex-1 space-y-5 overflow-y-auto p-5">{messages.map((item, index) => <ChatMessage key={`${item.time}-${index}`} message={item}/>)}
      {loading && <div className="flex items-center gap-3 text-sm text-slate-300"><div className="grid h-9 w-9 place-items-center rounded-xl bg-red-500/20 text-red-300">RM</div><div className="flex gap-1 rounded-2xl rounded-tl-sm bg-slate-800 px-4 py-4"><i/><i/><i/></div><span className="text-xs text-slate-400">Analyzing your report…</span></div>}<div ref={endRef}/>
    </div>
    <form onSubmit={submit} className="border-t border-white/10 p-4"><div className="flex items-end gap-3 rounded-2xl bg-slate-800 p-2 ring-1 ring-white/10 focus-within:ring-cyan-400/60"><textarea value={message} onChange={e => setMessage(e.target.value)} onKeyDown={e => { if (e.key === 'Enter' && !e.shiftKey) submit(e) }} disabled={loading} maxLength="2000" rows="2" placeholder="e.g. There is smoke coming from my apartment…" className="min-h-[44px] flex-1 resize-none bg-transparent px-3 py-2 text-sm outline-none placeholder:text-slate-500 disabled:opacity-50"/><button disabled={loading || !message.trim()} className="rounded-xl bg-cyan-400 px-4 py-3 text-sm font-bold text-slate-950 transition hover:bg-cyan-300 disabled:cursor-not-allowed disabled:opacity-40">Send</button></div><p className="mt-2 px-2 text-[11px] text-slate-500">Press Enter to send · Shift + Enter for a new line</p></form>
  </section>
}
