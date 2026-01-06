import { useState } from 'react';
import { TopBar } from './components/layout/TopBar';
import { LeftPanel } from './components/layout/LeftPanel';
import { RightPanel } from './components/layout/RightPanel';
import { QuestionCard } from './components/cards/QuestionCard';
import { ExplanationCard } from './components/cards/ExplanationCard';
import { CodePanel } from './components/code/CodePanel';
import { VisualizerPlaceholder } from './components/visualizers/VisualizerPlaceholder';
import { useQuestions, useQuestionDetail } from './hooks/useQuestions';

function App() {
    const [category, setCategory] = useState<string>('LEETCODE');
    const [selectedQuestionId, setSelectedQuestionId] = useState<string | null>(null);

    const { questions, loading: questionsLoading } = useQuestions(category);
    const { detail, loading: detailLoading } = useQuestionDetail(selectedQuestionId, category);

    const handleCategoryChange = (newCategory: string) => {
        setCategory(newCategory);
        setSelectedQuestionId(null);
    };

    const handleQuestionChange = (questionId: string) => {
        setSelectedQuestionId(questionId);
    };

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
                        <VisualizerPlaceholder
                            questionId={selectedQuestionId}
                            questionName={detail?.name}
                        />
                    }
                    code={
                        <CodePanel
                            code={detail?.solutionCode || ''}
                            filename={detail?.solutionFileName}
                            language="java"
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
