import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { oneLight } from 'react-syntax-highlighter/dist/esm/styles/prism';

interface CodePanelProps {
    code: string;
    filename?: string;
    highlightLine?: number;
    language?: string;
}

export function CodePanel({
    code,
    filename,
    highlightLine,
    language = 'java'
}: CodePanelProps) {
    if (!code) {
        return (
            <div className="h-full flex items-center justify-center text-gray-500 bg-gray-50">
                <p>Select a question to view its solution code.</p>
            </div>
        );
    }

    return (
        <div className="h-full flex flex-col bg-white">
            {/* Header */}
            {filename && (
                <div className="px-4 py-2 border-b border-border-light bg-gray-50 text-sm font-mono text-gray-600">
                    📄 {filename}
                </div>
            )}

            {/* Code with syntax highlighting */}
            <div className="flex-1 overflow-auto code-panel">
                <SyntaxHighlighter
                    language={language}
                    style={oneLight}
                    showLineNumbers
                    lineNumberStyle={{
                        minWidth: '3em',
                        paddingRight: '1em',
                        color: '#999',
                        userSelect: 'none'
                    }}
                    wrapLines
                    lineProps={(lineNumber) => {
                        const style: React.CSSProperties = { display: 'block' };
                        if (highlightLine && lineNumber === highlightLine) {
                            style.backgroundColor = '#FEF9C3';
                            style.borderLeft = '4px solid #EAB308';
                            style.marginLeft = '-4px';
                        }
                        return { style };
                    }}
                    customStyle={{
                        margin: 0,
                        padding: '1rem',
                        fontSize: '13px',
                        lineHeight: '1.6',
                        background: 'white',
                    }}
                >
                    {code}
                </SyntaxHighlighter>
            </div>
        </div>
    );
}
