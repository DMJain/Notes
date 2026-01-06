// Highlight color palette
export const HIGHLIGHT_COLORS: Record<string, string> = {
    ACTIVE: '#22C55E',
    COMPARING: '#3B82F6',
    SWAPPING: '#F59E0B',
    FOUND: '#10B981',
    NOT_FOUND: '#EF4444',
    POINTER_LEFT: '#F97316',
    POINTER_RIGHT: '#06B6D4',
    POINTER_I: '#8B5CF6',
    POINTER_J: '#EC4899',
    VISITED: '#6B7280',
    IN_QUEUE: '#FBBF24',
    CURRENT: '#EF4444',
    RESULT: '#FACC15',
    BASE_CASE: '#A855F7',
    MEMOIZED: '#14B8A6',
};

export function getHighlightColor(colorName: string | undefined): string {
    if (!colorName) return 'transparent';
    return HIGHLIGHT_COLORS[colorName] || colorName;
}
