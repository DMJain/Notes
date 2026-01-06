import { getHighlightColor } from '../../utils/highlightColors';

interface MapVisualizerProps {
    name: string;
    data: Record<string | number, unknown>;
    highlightKey?: string | number;
    highlightColor?: string;
}

export function MapVisualizer({
    name,
    data,
    highlightKey,
    highlightColor
}: MapVisualizerProps) {
    const entries = Object.entries(data);
    const bgColor = getHighlightColor(highlightColor);

    if (entries.length === 0) {
        return (
            <div className="mb-4">
                <div className="text-sm font-mono text-gray-600 mb-2">{name}</div>
                <div className="text-sm text-gray-400 italic">Empty HashMap</div>
            </div>
        );
    }

    return (
        <div className="mb-4">
            <div className="text-sm font-mono text-gray-600 mb-2">{name}</div>

            <div className="inline-block border-2 border-black rounded-lg overflow-hidden">
                <table className="border-collapse">
                    <thead>
                        <tr className="bg-gray-100">
                            <th className="px-4 py-2 border-b border-r border-black text-sm font-semibold">Key</th>
                            <th className="px-4 py-2 border-b border-black text-sm font-semibold">Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        {entries.map(([key, value]) => {
                            const isHighlighted = String(key) === String(highlightKey);
                            return (
                                <tr
                                    key={key}
                                    className="transition-all duration-200"
                                    style={{
                                        backgroundColor: isHighlighted ? bgColor : '#FFFEF5',
                                        color: isHighlighted && bgColor !== 'transparent' ? 'white' : 'black',
                                    }}
                                >
                                    <td className="px-4 py-2 border-r border-black font-mono text-sm">{key}</td>
                                    <td className="px-4 py-2 font-mono text-sm">{String(value)}</td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
