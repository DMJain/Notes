import { ReactNode, useState } from 'react';

interface LeftPanelProps {
    children: ReactNode;
}

export function LeftPanel({ children }: LeftPanelProps) {
    const [collapsed, setCollapsed] = useState(false);

    return (
        <aside
            className={`border-r border-border-dark bg-panel-bg flex flex-col transition-all duration-200 ${collapsed ? 'w-0 min-w-0 overflow-hidden' : 'w-[40%] min-w-[300px]'
                }`}
        >
            {/* Collapse toggle */}
            <button
                onClick={() => setCollapsed(!collapsed)}
                className="absolute left-0 top-1/2 -translate-y-1/2 z-10 w-5 h-12 bg-cream border border-border-dark border-l-0 rounded-r-md flex items-center justify-center hover:bg-gray-100 transition-colors"
                style={{ marginLeft: collapsed ? '0' : 'calc(40% - 1px)' }}
                title={collapsed ? 'Expand panel' : 'Collapse panel'}
            >
                <span className="text-xs">{collapsed ? '›' : '‹'}</span>
            </button>

            {!collapsed && (
                <div className="flex-1 overflow-y-auto p-4 space-y-4">
                    {children}
                </div>
            )}
        </aside>
    );
}
