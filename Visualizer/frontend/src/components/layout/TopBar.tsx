import { QuestionSummary } from '../../types/question';

interface TopBarProps {
    category: string;
    onCategoryChange: (category: string) => void;
    questions: QuestionSummary[];
    selectedQuestionId: string | null;
    onQuestionChange: (id: string) => void;
    loading: boolean;
}

export function TopBar({
    category,
    onCategoryChange,
    questions,
    selectedQuestionId,
    onQuestionChange,
    loading,
}: TopBarProps) {
    return (
        <header className="h-14 border-b border-border-dark bg-cream flex items-center px-4 gap-4 shrink-0">
            {/* Logo/Title */}
            <h1 className="font-bold text-lg tracking-tight">
                DSA <span className="text-gray-500 font-normal">Visualizer</span>
            </h1>

            <div className="w-px h-6 bg-border-light mx-2" />

            {/* Category Selector */}
            <div className="flex gap-1">
                {['LEETCODE', 'LLD'].map((cat) => (
                    <button
                        key={cat}
                        onClick={() => onCategoryChange(cat)}
                        className={`px-3 py-1.5 text-sm font-medium rounded-md transition-colors ${category === cat
                                ? 'bg-border-dark text-white'
                                : 'bg-transparent text-border-dark hover:bg-gray-100'
                            }`}
                    >
                        {cat === 'LEETCODE' ? 'LeetCode' : 'LLD'}
                    </button>
                ))}
            </div>

            <div className="w-px h-6 bg-border-light mx-2" />

            {/* Question Selector */}
            <div className="flex-1 max-w-md">
                <select
                    value={selectedQuestionId || ''}
                    onChange={(e) => onQuestionChange(e.target.value)}
                    disabled={loading || questions.length === 0}
                    className="w-full px-3 py-1.5 border border-border-dark rounded-md bg-white text-sm
                     focus:outline-none focus:ring-2 focus:ring-black focus:ring-offset-1
                     disabled:bg-gray-100 disabled:text-gray-400"
                >
                    <option value="" disabled>
                        {loading ? 'Loading...' : 'Select a question'}
                    </option>
                    {questions.map((q) => (
                        <option key={q.id} value={q.id}>
                            {q.number}. {q.name}
                        </option>
                    ))}
                </select>
            </div>

            {/* Spacer */}
            <div className="flex-1" />

            {/* Play Controls (placeholder for Phase 3) */}
            <div className="flex gap-2">
                <button className="btn-ghost" disabled title="Coming in Phase 3">
                    ⏮
                </button>
                <button className="btn-ghost" disabled title="Coming in Phase 3">
                    ▶
                </button>
                <button className="btn-ghost" disabled title="Coming in Phase 3">
                    ⏭
                </button>
            </div>
        </header>
    );
}
