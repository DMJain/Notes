import { ReactNode } from 'react';

interface RightPanelProps {
    visualizer: ReactNode;
    code: ReactNode;
}

export function RightPanel({ visualizer, code }: RightPanelProps) {
    return (
        <main className="flex-1 flex flex-col min-w-0 overflow-hidden">
            {/* Visualizer Section */}
            <section className="flex-1 min-h-[200px] border-b border-border-dark overflow-auto p-4">
                {visualizer}
            </section>

            {/* Code Section */}
            <section className="h-[45%] min-h-[200px] overflow-auto">
                {code}
            </section>
        </main>
    );
}
