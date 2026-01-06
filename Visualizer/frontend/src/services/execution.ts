import { VisualizationResponse } from '../types/visualization';
import api from './api';

export async function executeDemo(questionId: string): Promise<VisualizationResponse> {
    const response = await api.get<VisualizationResponse>(`/execute/demo/${questionId}`);
    return response.data;
}

export async function executeWithInput(
    questionId: string,
    input: Record<string, unknown>
): Promise<VisualizationResponse> {
    const response = await api.post<VisualizationResponse>('/execute', {
        questionId,
        category: 'LEETCODE',
        input,
    });
    return response.data;
}
