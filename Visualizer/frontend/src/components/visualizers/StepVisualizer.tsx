import { Step } from '../../types/visualization';
import { ArrayVisualizer } from './ArrayVisualizer';
import { CharArrayVisualizer } from './CharArrayVisualizer';
import { MapVisualizer } from './MapVisualizer';
import { SetVisualizer } from './SetVisualizer';

interface StepVisualizerProps {
    step: Step;
    allSteps: Step[];
    currentIndex: number;
}

export function StepVisualizer({ step, allSteps, currentIndex }: StepVisualizerProps) {
    const accumulatedState = buildAccumulatedState(allSteps, currentIndex);

    // Categorize data structures
    const inputArrays = ['nums', 's', 'chars', 'matrix', 'grid'];
    const outputArrays = ['ans', 'result', 'output', 'dp'];

    const inputs = Object.entries(accumulatedState.arrays)
        .filter(([name]) => inputArrays.some(inp => name.toLowerCase() === inp || name.toLowerCase().startsWith(inp)));
    const outputs = Object.entries(accumulatedState.arrays)
        .filter(([name]) => outputArrays.some(out => name.toLowerCase() === out || name.toLowerCase().startsWith(out)));
    const workingArrays = Object.entries(accumulatedState.arrays)
        .filter(([name]) => !inputArrays.some(inp => name.toLowerCase() === inp || name.toLowerCase().startsWith(inp)) &&
            !outputArrays.some(out => name.toLowerCase() === out || name.toLowerCase().startsWith(out)));

    // Categorize variables
    const inputVars = Object.entries(accumulatedState.variables)
        .filter(([name]) => ['target'].includes(name.toLowerCase()));
    const pointerVars = Object.entries(accumulatedState.variables)
        .filter(([_, v]) => v.isPointer);
    const resultVars = Object.entries(accumulatedState.variables)
        .filter(([name, v]) => !v.isPointer && !['target'].includes(name.toLowerCase()));

    return (
        <div className="p-4 h-full overflow-auto">
            {/* Step description */}
            <div className="mb-4 p-3 bg-yellow-50 border border-yellow-200 rounded-lg">
                <span className="text-sm font-semibold text-yellow-800">
                    Step {step.stepId}:
                </span>{' '}
                <span className="text-sm text-yellow-700">{step.description}</span>
            </div>

            {/* ===== TOP ROW: Input | Output ===== */}
            <div className="grid grid-cols-2 gap-3 mb-4">
                {/* INPUT Section */}
                <div className="bg-blue-50/50 border border-blue-200 rounded-lg p-3 min-h-[80px]">
                    <div className="text-xs font-semibold text-blue-600 uppercase tracking-wider mb-2">
                        📥 Input
                    </div>
                    <div className="flex flex-wrap gap-3 items-start">
                        {inputs.map(([name, state]) => (
                            <div key={name} className="shrink-0">
                                {state.isCharArray ? (
                                    <CharArrayVisualizer
                                        name={name}
                                        data={state.data as string[]}
                                        highlightIndex={step.data?.target === name ? step.data?.index as number : undefined}
                                        highlightIndices={step.data?.target === name ? step.data?.indices as number[] : undefined}
                                        highlightColor={step.data?.target === name ? step.data?.highlight as string : undefined}
                                        pointers={accumulatedState.pointers
                                            .filter(p => p.targetArray === name)
                                            .map(p => ({ name: p.name, index: p.position, color: p.color }))
                                        }
                                        compact
                                    />
                                ) : (
                                    <ArrayVisualizer
                                        name={name}
                                        data={state.data as number[]}
                                        highlightIndex={step.data?.target === name ? step.data?.index as number : undefined}
                                        highlightIndices={step.data?.target === name ? step.data?.indices as number[] : undefined}
                                        highlightColor={step.data?.target === name ? step.data?.highlight as string : undefined}
                                        pointers={accumulatedState.pointers
                                            .filter(p => p.targetArray === name)
                                            .map(p => ({ name: p.name, index: p.position, color: p.color }))
                                        }
                                        compact
                                    />
                                )}
                            </div>
                        ))}
                        {inputVars.map(([name, { value }]) => (
                            <div key={name} className="px-3 py-2 bg-white border border-blue-300 rounded-md shrink-0">
                                <span className="font-mono text-sm text-gray-600">{name}</span>
                                <span className="mx-1">=</span>
                                <span className="font-mono text-sm font-bold">{String(value)}</span>
                            </div>
                        ))}
                    </div>
                </div>

                {/* OUTPUT Section */}
                <div className="bg-green-50/50 border border-green-200 rounded-lg p-3 min-h-[80px]">
                    <div className="text-xs font-semibold text-green-600 uppercase tracking-wider mb-2">
                        📤 Output
                    </div>
                    <div className="flex flex-wrap gap-3 items-start">
                        {outputs.map(([name, state]) => (
                            <div key={name} className="shrink-0">
                                <ArrayVisualizer
                                    name={name}
                                    data={state.data as number[]}
                                    highlightIndex={step.data?.target === name ? step.data?.index as number : undefined}
                                    highlightColor={step.data?.target === name ? step.data?.highlight as string : undefined}
                                    compact
                                />
                            </div>
                        ))}
                        {resultVars.map(([name, { value }]) => {
                            const isHighlighted = name === step.data?.varName;
                            return (
                                <div
                                    key={name}
                                    className={`px-3 py-2 border rounded-md shrink-0 transition-all ${isHighlighted ? 'bg-yellow-100 border-yellow-400 scale-105' : 'bg-white border-green-300'
                                        }`}
                                >
                                    <span className="font-mono text-sm text-gray-600">{name}</span>
                                    <span className="mx-1">=</span>
                                    <span className="font-mono text-sm font-bold">{String(value)}</span>
                                </div>
                            );
                        })}
                    </div>
                </div>
            </div>

            {/* ===== DYNAMIC FLOW: Pointers + Working Structures ===== */}
            <div className="flex flex-wrap gap-3">
                {/* Pointers */}
                {pointerVars.length > 0 && (
                    <div className="bg-orange-50/50 border border-orange-200 rounded-lg p-3 shrink-0">
                        <div className="text-xs font-semibold text-orange-600 uppercase tracking-wider mb-2">
                            👆 Pointers
                        </div>
                        <div className="flex flex-wrap gap-2">
                            {pointerVars.map(([name, { value, targetArray }]) => {
                                const isHighlighted = name === step.data?.varName;
                                return (
                                    <div
                                        key={name}
                                        className={`flex items-center gap-1.5 px-2 py-1 rounded border transition-all ${isHighlighted ? 'border-yellow-400 bg-yellow-50' : 'border-orange-300 bg-white'
                                            }`}
                                    >
                                        <span className="text-orange-500 text-xs">➤</span>
                                        <span className="font-mono text-sm font-medium">{name}</span>
                                        <span className="text-gray-400">=</span>
                                        <span className="font-mono text-sm font-bold text-orange-600">{String(value)}</span>
                                        {targetArray && (
                                            <span className="text-xs text-gray-400">→{targetArray}</span>
                                        )}
                                    </div>
                                );
                            })}
                        </div>
                    </div>
                )}

                {/* Sets */}
                {Object.entries(accumulatedState.sets).map(([name, data]) => (
                    <div key={name} className="bg-teal-50/50 border border-teal-200 rounded-lg p-3 shrink-0">
                        <SetVisualizer
                            name={name}
                            data={data}
                            highlightValue={step.data?.target === name ? step.data?.value : undefined}
                            highlightColor={step.data?.target === name ? step.data?.highlight as string : undefined}
                        />
                    </div>
                ))}

                {/* Maps */}
                {Object.entries(accumulatedState.maps).map(([name, state]) => (
                    <div key={name} className="bg-purple-50/50 border border-purple-200 rounded-lg p-3 shrink-0 max-w-[350px]">
                        <MapVisualizer
                            name={name}
                            data={state}
                            highlightKey={step.data?.target === name ? step.data?.key as string : undefined}
                            highlightColor={step.data?.target === name ? step.data?.highlight as string : undefined}
                        />
                    </div>
                ))}

                {/* Other working arrays */}
                {workingArrays.map(([name, state]) => (
                    <div key={name} className="bg-gray-50 border border-gray-200 rounded-lg p-3 shrink-0">
                        <ArrayVisualizer
                            name={name}
                            data={state.data as number[]}
                            highlightIndex={step.data?.target === name ? step.data?.index as number : undefined}
                            highlightIndices={step.data?.target === name ? step.data?.indices as number[] : undefined}
                            highlightColor={step.data?.target === name ? step.data?.highlight as string : undefined}
                        />
                    </div>
                ))}
            </div>
        </div>
    );
}

interface AccumulatedState {
    arrays: Record<string, { data: (number | string)[]; isCharArray: boolean }>;
    maps: Record<string, Record<string | number, unknown>>;
    sets: Record<string, unknown[]>;
    pointers: { name: string; targetArray: string; position: number; color: string }[];
    variables: Record<string, { value: unknown; isPointer: boolean; targetArray?: string }>;
}

function buildAccumulatedState(steps: Step[], upToIndex: number): AccumulatedState {
    const state: AccumulatedState = {
        arrays: {},
        maps: {},
        sets: {},
        pointers: [],
        variables: {},
    };

    for (let i = 0; i <= upToIndex && i < steps.length; i++) {
        const step = steps[i];
        const data = step.data || {};
        const target = data.target as string;
        const dataType = data.type as string;

        switch (step.action) {
            case 'ARRAY_INIT':
            case 'ARRAY_GET':
            case 'ARRAY_SET':
            case 'ARRAY_SWAP':
            case 'ARRAY_HIGHLIGHT':
                if (target && data.state) {
                    const isCharArray = dataType === 'CHAR_ARRAY';
                    state.arrays[target] = {
                        data: data.state as (number | string)[],
                        isCharArray
                    };
                }
                break;

            case 'MAP_INIT':
            case 'MAP_PUT':
            case 'MAP_GET':
            case 'MAP_CONTAINS':
            case 'MAP_REMOVE':
                // Check if this is actually a SET
                if (dataType === 'SET') {
                    if (target && data.state) {
                        const setData = data.state;
                        if (setData instanceof Array) {
                            state.sets[target] = setData;
                        } else if (typeof setData === 'object' && setData !== null) {
                            // Convert object keys to array for set-like display
                            state.sets[target] = Object.keys(setData);
                        }
                    }
                } else {
                    if (target && data.state) {
                        state.maps[target] = data.state as Record<string | number, unknown>;
                    }
                }
                break;

            case 'SET_ADD':
            case 'SET_CONTAINS':
            case 'SET_REMOVE':
                if (target && data.state) {
                    const setData = data.state;
                    if (setData instanceof Array) {
                        state.sets[target] = setData;
                    } else if (typeof setData === 'object' && setData !== null) {
                        state.sets[target] = Object.keys(setData);
                    }
                }
                break;

            case 'VAR_INIT':
            case 'VAR_UPDATE': {
                const varName = data.varName as string;
                const value = data.value;
                const isPointer = data.isPointer as boolean || false;

                if (varName) {
                    state.variables[varName] = {
                        value,
                        isPointer,
                        targetArray: isPointer ? target : undefined
                    };

                    if (isPointer && target) {
                        const position = data.to !== undefined ? data.to as number : value as number;
                        const existing = state.pointers.find(p => p.name === varName);
                        if (existing) {
                            existing.position = position;
                        } else {
                            state.pointers.push({
                                name: varName,
                                targetArray: target,
                                position: position,
                                color: getPointerColor(varName),
                            });
                        }
                    }
                }
                break;
            }

            case 'POINTER_MOVE': {
                const pointerName = data.pointerName as string;
                const toPosition = data.to as number;
                if (pointerName !== undefined) {
                    const existing = state.pointers.find(p => p.name === pointerName);
                    if (existing) {
                        existing.position = toPosition;
                    } else {
                        state.pointers.push({
                            name: pointerName,
                            targetArray: target,
                            position: toPosition,
                            color: getPointerColor(pointerName),
                        });
                    }
                }
                break;
            }
        }
    }

    return state;
}

function getPointerColor(name: string): string {
    const lower = name.toLowerCase();
    if (lower === 'left' || lower === 'l' || lower === 'start' || lower === 'p2') return 'POINTER_LEFT';
    if (lower === 'right' || lower === 'r' || lower === 'end') return 'POINTER_RIGHT';
    if (lower === 'i') return 'POINTER_I';
    if (lower === 'j') return 'POINTER_J';
    return 'ACTIVE';
}
