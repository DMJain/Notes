import { VisualizationResponse, MethodSignature } from '../types/visualization';
import api from './api';

export async function executeDemo(questionId: string): Promise<VisualizationResponse> {
    const response = await api.get<VisualizationResponse>(`/execute/auto/demo/${questionId}`);
    return response.data;
}

export async function getMethodSignature(questionId: string): Promise<MethodSignature> {
    const response = await api.get<MethodSignature>(`/execute/auto/signature/${questionId}`);
    return response.data;
}

export async function executeWithInput(
    questionId: string,
    methodName: string,
    inputs: Record<string, unknown>
): Promise<VisualizationResponse> {
    const response = await api.post<VisualizationResponse>('/execute/auto', {
        questionId,
        methodName,
        inputs,
    });
    return response.data;
}
