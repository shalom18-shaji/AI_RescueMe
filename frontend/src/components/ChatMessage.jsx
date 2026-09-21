function timeLabel(date) { return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) }
export default function ChatMessage({ message }) {
  const isUser = message.role === 'user'
  return <div className={`flex gap-3 ${isUser ? 'flex-row-reverse' : ''} animate-rise`}>
    <div className={`grid h-9 w-9 shrink-0 place-items-center rounded-xl ${isUser ? 'bg-cyan-500 text-slate-950' : 'bg-red-500/20 text-red-300 ring-1 ring-red-400/30'}`}>{isUser ? 'You' : 'RM'}</div>
    <div className={`max-w-[82%] rounded-2xl px-4 py-3 ${isUser ? 'rounded-tr-sm bg-cyan-500 text-slate-950' : 'rounded-tl-sm bg-slate-800 text-slate-100 ring-1 ring-white/10'}`}>
      <p className="whitespace-pre-line text-sm leading-6">{message.text}</p><p className={`mt-1 text-[10px] ${isUser ? 'text-cyan-950/70' : 'text-slate-400'}`}>{timeLabel(message.time)}</p>
    </div>
  </div>
}
