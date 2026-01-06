import { getHighlightColor } from '../../utils/highlightColors';

interface ArrayVisualizerProps {
    name: string;
    data: number[];
    highlightIndex?: number;
    highlightIndices?: number[];
    highlightColor?: string;
    pointers?: { name: string; index: number; color: string }[];
    compact?: boolean;
}

export function ArrayVisualizer({
    name,
    data,
    highlightIndex,
    highlightIndices,
    highlightColor,
    pointers = [],
    compact = false
}: ArrayVisualizerProps) {
    const bgColor = getHighlightColor(highlightColor);
    const cellSize = compact ? 'w-10 h-10 text-sm' : 'w-12 h-12 text-lg';

    return (
        <div className={compact ? 'mb-2' : 'mb-4'}>
            {/* Array name */}
            <div className="text-sm font-mono text-gray-600 mb-2">{name}</div>

            {/* Array visualization */}
            <div className="flex gap-1 flex-wrap">
                {data.map((value, index) => {
                    const isHighlighted =
                        highlightIndex === index ||
                        (highlightIndices && highlightIndices.includes(index));

                    // Find any pointers at this index
                    const pointersHere = pointers.filter(p => p.index === index);

                    return (
                        <div key={index} className="flex flex-col items-center">
                            {/* Pointer labels above */}
                            {pointersHere.length > 0 && (
                                <div className="flex gap-1 mb-1">
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

                            {/* Array cell */}
                            <div
                                className={`
                                    ${cellSize} flex items-center justify-center
                                    border-2 border-black rounded-md font-mono
                                    transition-all duration-200
                                    ${isHighlighted ? 'transform scale-110 shadow-lg' : ''}
                                `}
                                style={{
                                    backgroundColor: isHighlighted ? bgColor : '#FFFEF5',
                                    color: isHighlighted && bgColor !== 'transparent' ? 'white' : 'black',
                                }}
                            >
                                {value}
                            </div>

                            {/* Index below */}
                            <div className="text-xs text-gray-500 mt-1">{index}</div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}
