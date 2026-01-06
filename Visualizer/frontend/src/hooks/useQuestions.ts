import { useState, useEffect, useCallback } from 'react';
import { QuestionSummary, QuestionDetail } from '../types/question';
import { fetchQuestions, fetchQuestionDetail } from '../services/api';

export function useQuestions(category: string) {
    const [questions, setQuestions] = useState<QuestionSummary[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        setLoading(true);
        setError(null);
        fetchQuestions(category)
            .then(setQuestions)
            .catch(err => setError(err.message))
            .finally(() => setLoading(false));
    }, [category]);

    return { questions, loading, error };
}

export function useQuestionDetail(questionId: string | null, category: string) {
    const [detail, setDetail] = useState<QuestionDetail | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const loadQuestion = useCallback(() => {
        if (!questionId) {
            setDetail(null);
            return;
        }

        setLoading(true);
        setError(null);
        fetchQuestionDetail(questionId, category)
            .then(setDetail)
            .catch(err => setError(err.message))
            .finally(() => setLoading(false));
    }, [questionId, category]);

    useEffect(() => {
        loadQuestion();
    }, [loadQuestion]);

    return { detail, loading, error, refresh: loadQuestion };
}
