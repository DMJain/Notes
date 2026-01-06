import { getHighlightColor } from '../../utils/highlightColors';

interface VariablePanelProps {
    variables: Record<string, { value: unknown; isPointer: boolean; targetArray?: string }>;
    highlightVar?: string;
}

export function VariablePanel({ variables, highlightVar }: VariablePanelProps) {
    const entries = Object.entries(variables);

    if (entries.length === 0) return null;

    // Separate pointers from regular variables
    const pointerVars = entries.filter(([_, v]) => v.isPointer);
    const regularVars = entries.filter(([_, v]) => !v.isPointer);

    return (
        <div className="bg-cream border border-border-dark rounded-lg p-3 mb-4">
            <div className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">
                Variables
            </div>

            <div className="flex flex-wrap gap-3">
                {/* Pointer variables - show with arrow icon */}
                {pointerVars.map(([name, { value, targetArray }]) => {
                    const isHighlighted = name === highlightVar;
                    return (
                        <div
                            key={name}
                            className={`
                flex items-center gap-2 px-3 py-1.5 rounded-md border-2 
                transition-all duration-200
                ${isHighlighted ? 'border-yellow-400 bg-yellow-50 scale-105' : 'border-gray-300 bg-white'}
              `}
                        >
                            <span className="text-xs text-orange-600">➤</span>
                            <span className="font-mono text-sm font-medium text-gray-700">{name}</span>
                            <span className="text-gray-400">=</span>
                            <span
                                className="font-mono text-sm font-bold"
                                style={{ color: getHighlightColor('ACTIVE') }}
                            >
                                {String(value)}
                            </span>
                            {targetArray && (
                                <span className="text-xs text-gray-400">→ {targetArray}</span>
                            )}
                        </div>
                    );
                })}

                {/* Regular variables */}
                {regularVars.map(([name, { value }]) => {
                    const isHighlighted = name === highlightVar;
                    return (
                        <div
                            key={name}
                            className={`
                flex items-center gap-2 px-3 py-1.5 rounded-md border-2
                transition-all duration-200
                ${isHighlighted ? 'border-yellow-400 bg-yellow-50 scale-105' : 'border-gray-300 bg-white'}
              `}
                        >
                            <span className="font-mono text-sm font-medium text-gray-700">{name}</span>
                            <span className="text-gray-400">=</span>
                            <span className="font-mono text-sm font-bold text-gray-900">
                                {String(value)}
                            </span>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}
