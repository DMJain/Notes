// Question types
export interface QuestionSummary {
    id: string;
    number: string;
    name: string;
    category: 'LEETCODE' | 'LLD';
}

export interface QuestionDetail {
    id: string;
    number: string;
    name: string;
    category: 'LEETCODE' | 'LLD';
    questionMarkdown: string;
    explanationMarkdown: string;
    solutionCode: string;
    solutionFileName: string;
}
