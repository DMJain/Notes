import ReactMarkdown from 'react-markdown';

interface QuestionCardProps {
    markdown: string;
    title?: string;
}

export function QuestionCard({ markdown, title = 'Question' }: QuestionCardProps) {
    if (!markdown) {
        return (
            <div className="card">
                <div className="card-header">{title}</div>
                <div className="card-body text-gray-500 text-sm">
                    Select a question to view its description.
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
