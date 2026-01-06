import { Step } from '../types/visualization';
import { useState, useCallback, useRef, useEffect } from 'react';

export type PlaybackState = 'STOPPED' | 'PLAYING' | 'PAUSED';

export interface StepPlayerState {
    currentStep: Step | null;
    currentIndex: number;
    state: PlaybackState;
    speed: number;
    totalSteps: number;
    hasNext: boolean;
    hasPrev: boolean;
}

export interface StepPlayerControls {
    play: () => void;
    pause: () => void;
    stop: () => void;
    stepForward: () => void;
    stepBack: () => void;
    goToStep: (index: number) => void;
    setSpeed: (speed: number) => void;
}

export function useStepPlayer(steps: Step[]): StepPlayerState & StepPlayerControls {
    const [currentIndex, setCurrentIndex] = useState(0);
    const [state, setState] = useState<PlaybackState>('STOPPED');
    const [speed, setSpeed] = useState(1);
    const intervalRef = useRef<number | null>(null);

    const currentStep = steps[currentIndex] || null;
    const hasNext = currentIndex < steps.length - 1;
    const hasPrev = currentIndex > 0;

    const play = useCallback(() => {
        if (!hasNext && currentIndex === steps.length - 1) {
            // If at end, restart
            setCurrentIndex(0);
        }
        setState('PLAYING');
    }, [hasNext, currentIndex, steps.length]);

    const pause = useCallback(() => setState('PAUSED'), []);

    const stop = useCallback(() => {
        setState('STOPPED');
        setCurrentIndex(0);
    }, []);

    const stepForward = useCallback(() => {
        if (currentIndex < steps.length - 1) {
            setCurrentIndex(i => i + 1);
        }
    }, [currentIndex, steps.length]);

    const stepBack = useCallback(() => {
        if (currentIndex > 0) {
            setCurrentIndex(i => i - 1);
        }
    }, [currentIndex]);

    const goToStep = useCallback((index: number) => {
        setCurrentIndex(Math.max(0, Math.min(index, steps.length - 1)));
    }, [steps.length]);

    // Auto-play effect
    useEffect(() => {
        if (state === 'PLAYING') {
            intervalRef.current = window.setInterval(() => {
                setCurrentIndex(i => {
                    if (i >= steps.length - 1) {
                        setState('STOPPED');
                        return i;
                    }
                    return i + 1;
                });
            }, 800 / speed);
        }

        return () => {
            if (intervalRef.current) {
                clearInterval(intervalRef.current);
                intervalRef.current = null;
            }
        };
    }, [state, speed, steps.length]);

    // Reset when steps change
    useEffect(() => {
        setCurrentIndex(0);
        setState('STOPPED');
    }, [steps]);

    return {
        currentStep,
        currentIndex,
        state,
        speed,
        totalSteps: steps.length,
        hasNext: currentIndex < steps.length - 1,
        hasPrev: currentIndex > 0,
        play,
        pause,
        stop,
        stepForward,
        stepBack,
        goToStep,
        setSpeed,
    };
}
