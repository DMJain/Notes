import axios from 'axios';
import { QuestionSummary, QuestionDetail } from '../types/question';

const api = axios.create({
    baseURL: '/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

export async function fetchQuestions(category: string = 'LEETCODE'): Promise<QuestionSummary[]> {
    const response = await api.get<QuestionSummary[]>('/questions', {
        params: { category },
    });
    return response.data;
}

export async function fetchQuestionDetail(questionId: string, category: string = 'LEETCODE'): Promise<QuestionDetail> {
    const response = await api.get<QuestionDetail>(`/questions/${questionId}`, {
        params: { category },
    });
    return response.data;
}

export default api;
