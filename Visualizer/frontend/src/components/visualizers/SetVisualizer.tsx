import { getHighlightColor } from '../../utils/highlightColors';

interface SetVisualizerProps {
    name: string;
    data: unknown[];
    highlightValue?: unknown;
    highlightColor?: string;
}

export function SetVisualizer({
    name,
    data,
    highlightValue,
    highlightColor
}: SetVisualizerProps) {
    const bgColor = getHighlightColor(highlightColor);

    if (data.length === 0) {
        return (
            <div className="mb-2">
                <div className="text-sm font-mono text-gray-600 mb-1">{name}</div>
                <div className="text-sm text-gray-400 italic">{ } (empty)</div>
            </div>
        );
    }

    return (
        <div className="mb-2">
            <div className="text-sm font-mono text-gray-600 mb-1">{name}</div>
            <div className="flex flex-wrap gap-1">
                <span className="text-gray-400">{'{'}</span>
                {data.map((value, idx) => {
                    const isHighlighted = String(value) === String(highlightValue);
                    return (
                        <span key={idx} className="flex items-center">
                            <span
                                className={`px-2 py-0.5 rounded font-mono text-sm transition-all ${isHighlighted ? 'scale-110' : ''
                                    }`}
                                style={{
                                    backgroundColor: isHighlighted ? bgColor : '#E5E7EB',
                                    color: isHighlighted && bgColor !== 'transparent' ? 'white' : 'black',
                                }}
                            >
                                {String(value)}
                            </span>
                            {idx < data.length - 1 && <span className="text-gray-400 mx-0.5">,</span>}
                        </span>
                    );
                })}
                <span className="text-gray-400">{'}'}</span>
            </div>
        </div>
    );
}
