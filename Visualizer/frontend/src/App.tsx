import { useState, useEffect } from 'react';
import { TopBar } from './components/layout/TopBar';
import { LeftPanel } from './components/layout/LeftPanel';
import { RightPanel } from './components/layout/RightPanel';
import { QuestionCard } from './components/cards/QuestionCard';
import { ExplanationCard } from './components/cards/ExplanationCard';
import { CodePanel } from './components/code/CodePanel';
import { StepVisualizer } from './components/visualizers/StepVisualizer';
import { useQuestions, useQuestionDetail } from './hooks/useQuestions';
import { useStepPlayer } from './hooks/useStepPlayer';
import { executeDemo, getMethodSignature, executeWithInput } from './services/execution';
import { Step, MethodSignature } from './types/visualization';
import { InputForm } from './components/controls/InputForm';
import { Modal } from './components/common/Modal';

function App() {
    const [category, setCategory] = useState<string>('LEETCODE');
    const [selectedQuestionId, setSelectedQuestionId] = useState<string | null>(null);
    const [steps, setSteps] = useState<Step[]>([]);
    const [responseInput, setResponseInput] = useState<Record<string, unknown>>({});
    const [responseOutput, setResponseOutput] = useState<unknown>(null);
    const [executionLoading, setExecutionLoading] = useState(false);
    const [executionError, setExecutionError] = useState<string | null>(null);
    const [signature, setSignature] = useState<MethodSignature | null>(null);
    const [isInputModalOpen, setIsInputModalOpen] = useState(false);

    const { questions, loading: questionsLoading } = useQuestions(category);
    const { detail, loading: detailLoading } = useQuestionDetail(selectedQuestionId, category);

    const player = useStepPlayer(steps);

    const handleCategoryChange = (newCategory: string) => {
        setCategory(newCategory);
        setSelectedQuestionId(null);
        setSteps([]);
    };

    const handleQuestionChange = (questionId: string) => {
        setSelectedQuestionId(questionId);
        setSteps([]);
        setExecutionError(null);
        setSignature(null);
    };

    // Load demo visualization and signature when question changes
    useEffect(() => {
        if (!selectedQuestionId) return;

        const loadData = async () => {
            setExecutionLoading(true);
            setExecutionError(null);
            try {
                // Fetch signature first to enable custom input
                try {
                    const sig = await getMethodSignature(selectedQuestionId);
                    setSignature(sig);
                } catch (e) {
                    console.warn('Could not fetch signature', e);
                }

                const response = await executeDemo(selectedQuestionId);
                if (response.success && response.steps) {
                    setSteps(response.steps);
                    setResponseInput(response.input || {});
                    setResponseOutput(response.output || response.result);
                } else if (response.error) {
                    setExecutionError(response.error.message || 'Visualization not available');
                    setSteps([]);
                } else {
                    setExecutionError('Visualization not yet implemented for this question');
                    setSteps([]);
                }
            } catch (err) {
                // Demo not available for this question
                setExecutionError('Visualization not yet implemented for this question');
                setSteps([]);
            } finally {
                setExecutionLoading(false);
            }
        };

        loadData();
    }, [selectedQuestionId]);

    const handleCustomExecution = async (inputs: Record<string, unknown>) => {
        if (!selectedQuestionId || !signature) return;

        setExecutionLoading(true);
        setExecutionError(null);
        // Close modal immediately so user sees loading state
        setIsInputModalOpen(false);

        try {
            const response = await executeWithInput(selectedQuestionId, signature.methodName, inputs);
            if (response.success && response.steps) {
                setSteps(response.steps);
                setResponseInput(response.input || {});
                setResponseOutput(response.output || response.result);
            } else {
                setExecutionError(response.error?.message || 'Execution failed');
                setSteps([]);
            }
        } catch (err) {
            setExecutionError('Execution failed: ' + (err as Error).message);
            setSteps([]);
        } finally {
            setExecutionLoading(false);
        }
    };

    // Get current highlighted line from step
    const currentLineNumber = player.currentStep?.lineNumber;

    return (
        <div className="h-screen w-screen flex flex-col overflow-hidden bg-egg-white">
            {/* Top Bar */}
            <TopBar
                category={category}
                onCategoryChange={handleCategoryChange}
                questions={questions}
                selectedQuestionId={selectedQuestionId}
                onQuestionChange={handleQuestionChange}
                loading={questionsLoading}
                player={player}
            />

            {/* Main Content */}
            <div className="flex-1 flex overflow-hidden relative">
                {/* Left Panel - Question & Explanation */}
                <LeftPanel>
                    <QuestionCard
                        markdown={detail?.questionMarkdown || ''}
                        title="Problem Description"
                    />
                    <ExplanationCard
                        markdown={detail?.explanationMarkdown || ''}
                        title="Approach & Intuition"
                    />
                </LeftPanel>

                {/* Right Panel - Visualizer & Code */}
                <RightPanel
                    topRightAction={
                        signature && (
                            <button
                                onClick={() => setIsInputModalOpen(true)}
                                className="bg-white border border-gray-300 text-gray-700 hover:bg-gray-50 px-3 py-1.5 rounded-md text-sm font-medium shadow-sm transition-colors flex items-center gap-2"
                            >
                                <span>⚙️</span> Custom Input
                            </button>
                        )
                    }
                    visualizer={
                        executionLoading ? (
                            <div className="h-full flex items-center justify-center bg-cream">
                                <div className="text-gray-500">Loading visualization...</div>
                            </div>
                        ) : executionError ? (
                            <div className="h-full flex items-center justify-center bg-cream">
                                <div className="text-center">
                                    <div className="text-4xl mb-4">🔜</div>
                                    <p className="text-gray-500">{executionError}</p>
                                </div>
                            </div>
                        ) : steps.length > 0 && player.currentStep ? (
                            <StepVisualizer
                                step={player.currentStep}
                                allSteps={steps}
                                currentIndex={player.currentIndex}
                                apiInput={responseInput}
                                apiOutput={responseOutput}
                            />
                        ) : (
                            <div className="h-full flex items-center justify-center bg-cream">
                                <div className="text-center text-gray-500">
                                    <div className="text-6xl mb-4">🎯</div>
                                    <h2 className="text-xl font-semibold mb-2">DSA Visualizer</h2>
                                    <p className="text-sm">Select a question to visualize its algorithm</p>
                                </div>
                            </div>
                        )
                    }
                    code={
                        <CodePanel
                            code={detail?.solutionCode || ''}
                            filename={detail?.solutionFileName}
                            language="java"
                            highlightLine={currentLineNumber}
                        />
                    }
                />
            </div>

            {/* Custom Input Modal */}
            <Modal
                isOpen={isInputModalOpen}
                onClose={() => setIsInputModalOpen(false)}
                title="Custom Input"
            >
                {signature && (
                    <InputForm
                        signature={signature}
                        onSubmit={handleCustomExecution}
                        loading={executionLoading}
                    />
                )}
            </Modal>

            {/* Loading overlay */}
            {detailLoading && (
                <div className="fixed inset-0 bg-black/10 flex items-center justify-center z-50">
                    <div className="bg-white px-6 py-4 rounded-lg shadow-lg">
                        Loading question...
                    </div>
                </div>
            )}
        </div>
    );
}

export default App;
