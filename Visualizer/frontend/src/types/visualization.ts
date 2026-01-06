// Target types for visualizations
export type TargetType =
    | 'ARRAY' | 'LIST' | 'MAP' | 'SET'
    | 'STACK' | 'QUEUE' | 'DEQUE' | 'HEAP'
    | 'LINKED_LIST' | 'DOUBLY_LINKED_LIST'
    | 'TREE' | 'GRAPH' | 'TRIE'
    | 'MATRIX' | 'VARIABLE' | 'RECURSION';

export interface StepTarget {
    type: TargetType;
    name: string;
    state?: unknown;
    index?: number;
    indices?: number[];
    key?: string | number;
    value?: unknown;
    highlight?: string;
    nodeId?: string;
    from?: number | string;
    to?: number | string;
    row?: number;
    col?: number;
    depth?: number;
    callId?: string;
}

export interface Step {
    stepId: number;
    action: string;
    description: string;
    lineNumber: number;
    targets: StepTarget[];
    timestamp?: number;
    data?: Record<string, unknown>;
}

export interface VisualizationResponse {
    questionId: string;
    questionName: string;
    category: 'LEETCODE' | 'LLD' | 'HLD';
    input: UserInput;
    output: unknown;
    steps: Step[];
    codeContent: string;
    totalSteps: number;
    executionTimeMs: number;
    success: boolean;
    error?: {
        type: 'RUNTIME' | 'TIMEOUT' | 'COMPILE' | 'UNKNOWN';
        message: string;
        lineNumber?: number;
    };
}

export interface UserInput {
    raw: string;
    parsed?: {
        arrays?: { name: string; values: unknown[] }[];
        primitives?: { name: string; value: unknown; type: string }[];
    };
}

// Playback state
export type PlaybackState = 'STOPPED' | 'PLAYING' | 'PAUSED';
