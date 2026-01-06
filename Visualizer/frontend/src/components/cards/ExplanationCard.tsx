import ReactMarkdown from 'react-markdown';

interface ExplanationCardProps {
    markdown: string;
    title?: string;
}

export function ExplanationCard({ markdown, title = 'Explanation' }: ExplanationCardProps) {
    if (!markdown) {
        return (
            <div className="card">
                <div className="card-header">{title}</div>
                <div className="card-body text-gray-500 text-sm">
                    No explanation available for this question.
                </div>
            </div>
        );
    }

    return (
        <div className="card">
            <div className="card-header">{title}</div>
            <div className="card-body markdown-content max-h-[400px] overflow-y-auto">
                <ReactMarkdown>{markdown}</ReactMarkdown>
            </div>
        </div>
    );
}
