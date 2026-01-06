// Placeholder visualizer for Phase 1
// This will be replaced with actual visualizations in Phase 2

interface VisualizerPlaceholderProps {
    questionId: string | null;
    questionName?: string;
}

export function VisualizerPlaceholder({ questionId, questionName }: VisualizerPlaceholderProps) {
    if (!questionId) {
        return (
            <div className="h-full flex items-center justify-center bg-cream">
                <div className="text-center text-gray-500">
                    <div className="text-6xl mb-4">🎯</div>
                    <h2 className="text-xl font-semibold mb-2">DSA Visualizer</h2>
                    <p className="text-sm">Select a question to visualize its algorithm execution</p>
                </div>
            </div>
        );
    }

    return (
        <div className="h-full flex flex-col items-center justify-center bg-cream">
            <div className="card max-w-md text-center">
                <div className="card-header">Visualizer</div>
                <div className="card-body">
                    <div className="text-4xl mb-4">🚧</div>
                    <h3 className="font-semibold mb-2">{questionName || questionId}</h3>
                    <p className="text-sm text-gray-600 mb-4">
                        Visualization engine coming in Phase 2!
                    </p>
                    <div className="text-xs text-gray-400">
                        This area will display:
                        <ul className="mt-2 space-y-1 text-left list-disc list-inside">
                            <li>Arrays with index highlighting</li>
                            <li>Pointer movement animations</li>
                            <li>Hash map state changes</li>
                            <li>Tree/Graph visualizations</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
}
