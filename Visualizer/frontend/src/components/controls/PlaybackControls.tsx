import { PlaybackState } from '../../hooks/useStepPlayer';

interface PlaybackControlsProps {
    state: PlaybackState;
    currentIndex: number;
    totalSteps: number;
    speed: number;
    hasNext: boolean;
    hasPrev: boolean;
    onPlay: () => void;
    onPause: () => void;
    onStop: () => void;
    onStepForward: () => void;
    onStepBack: () => void;
    onSpeedChange: (speed: number) => void;
}

export function PlaybackControls({
    state,
    currentIndex,
    totalSteps,
    speed,
    hasNext,
    hasPrev,
    onPlay,
    onPause,
    onStop,
    onStepForward,
    onStepBack,
    onSpeedChange,
}: PlaybackControlsProps) {
    const isPlaying = state === 'PLAYING';

    return (
        <div className="flex items-center gap-3">
            {/* Step counter */}
            <div className="text-sm font-mono text-gray-600 min-w-[80px]">
                {totalSteps > 0 ? `${currentIndex + 1} / ${totalSteps}` : '- / -'}
            </div>

            {/* Control buttons */}
            <div className="flex gap-1">
                <button
                    onClick={onStop}
                    disabled={totalSteps === 0}
                    className="btn-ghost"
                    title="Reset"
                >
                    ⏹
                </button>
                <button
                    onClick={onStepBack}
                    disabled={!hasPrev}
                    className="btn-ghost"
                    title="Step Back"
                >
                    ⏮
                </button>
                <button
                    onClick={isPlaying ? onPause : onPlay}
                    disabled={totalSteps === 0}
                    className="btn-ghost px-4"
                    title={isPlaying ? 'Pause' : 'Play'}
                >
                    {isPlaying ? '⏸' : '▶'}
                </button>
                <button
                    onClick={onStepForward}
                    disabled={!hasNext}
                    className="btn-ghost"
                    title="Step Forward"
                >
                    ⏭
                </button>
            </div>

            {/* Speed control */}
            <div className="flex items-center gap-2 ml-4">
                <span className="text-xs text-gray-500">Speed:</span>
                <select
                    value={speed}
                    onChange={(e) => onSpeedChange(Number(e.target.value))}
                    className="text-sm border border-border-dark rounded px-2 py-1 bg-white"
                >
                    <option value={0.5}>0.5x</option>
                    <option value={1}>1x</option>
                    <option value={2}>2x</option>
                    <option value={4}>4x</option>
                </select>
            </div>
        </div>
    );
}
