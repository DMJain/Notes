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
import { executeDemo } from './services/execution';
import { Step } from './types/visualization';

function App() {
    const [category, setCategory] = useState<string>('LEETCODE');
    const [selectedQuestionId, setSelectedQuestionId] = useState<string | null>(null);
    const [steps, setSteps] = useState<Step[]>([]);
    const [responseInput, setResponseInput] = useState<Record<string, unknown>>({});
    const [responseOutput, setResponseOutput] = useState<unknown>(null);
    const [executionLoading, setExecutionLoading] = useState(false);
    const [executionError, setExecutionError] = useState<string | null>(null);

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
    };

    // Load demo visualization when question changes
    useEffect(() => {
        if (!selectedQuestionId) return;

        const loadDemo = async () => {
            setExecutionLoading(true);
            setExecutionError(null);
            try {
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

        loadDemo();
    }, [selectedQuestionId]);

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
