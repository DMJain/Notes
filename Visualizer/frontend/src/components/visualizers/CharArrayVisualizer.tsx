import { getHighlightColor } from '../../utils/highlightColors';

interface CharArrayVisualizerProps {
    name: string;
    data: string[];  // Array of single characters
    highlightIndex?: number;
    highlightIndices?: number[];
    highlightColor?: string;
    pointers?: { name: string; index: number; color: string }[];
    compact?: boolean;
}

export function CharArrayVisualizer({
    name,
    data,
    highlightIndex,
    highlightIndices,
    highlightColor,
    pointers = [],
    compact = false
}: CharArrayVisualizerProps) {
    const bgColor = getHighlightColor(highlightColor);
    const cellSize = compact ? 'w-8 h-8 text-sm' : 'w-10 h-10 text-base';

    return (
        <div className={compact ? 'mb-2' : 'mb-4'}>
            <div className="text-sm font-mono text-gray-600 mb-2">{name}</div>
            <div className="flex gap-0.5 flex-wrap">
                {data.map((char, index) => {
                    const isHighlighted =
                        highlightIndex === index ||
                        (highlightIndices && highlightIndices.includes(index));

                    const pointersHere = pointers.filter(p => p.index === index);

                    return (
                        <div key={index} className="flex flex-col items-center">
                            {pointersHere.length > 0 && (
                                <div className="flex gap-0.5 mb-0.5">
                                    {pointersHere.map(p => (
                                        <span
                                            key={p.name}
                                            className="text-xs font-mono px-1 rounded"
                                            style={{
                                                backgroundColor: getHighlightColor(p.color),
                                                color: 'white'
                                            }}
                                        >
                                            {p.name}
                                        </span>
                                    ))}
                                </div>
                            )}

                            <div
                                className={`
                  ${cellSize} flex items-center justify-center
                  border border-black rounded font-mono font-bold
                  transition-all duration-200
                  ${isHighlighted ? 'transform scale-110 shadow-md border-2' : ''}
                `}
                                style={{
                                    backgroundColor: isHighlighted ? bgColor : '#FFFEF5',
                                    color: isHighlighted && bgColor !== 'transparent' ? 'white' : 'black',
                                }}
                            >
                                {char}
                            </div>

                            <div className="text-xs text-gray-400 mt-0.5">{index}</div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}
