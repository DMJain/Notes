import React, { useState, useEffect } from 'react';
import { MethodSignature } from '../../types/visualization';

interface InputFormProps {
    signature: MethodSignature;
    onSubmit: (inputs: Record<string, unknown>) => void;
    loading?: boolean;
}

export const InputForm: React.FC<InputFormProps> = ({ signature, onSubmit, loading }) => {
    const [inputs, setInputs] = useState<Record<string, string>>({});

    // Initialize inputs with empty strings when signature changes
    useEffect(() => {
        const initialInputs: Record<string, string> = {};
        signature.parameters.forEach(param => {
            initialInputs[param.name] = getDefaultValue(param.type);
        });
        setInputs(initialInputs);
    }, [signature]);

    const getDefaultValue = (type: string): string => {
        if (type.includes('[]') || type.startsWith('List')) return '2, 7, 11, 15';
        if (type === 'int' || type === 'long') return '0';
        if (type === 'double' || type === 'float') return '0.0';
        if (type === 'boolean') return 'false';
        if (type === 'String') return 'example';
        return '';
    };

    const handleChange = (name: string, value: string) => {
        setInputs(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const parsedInputs: Record<string, unknown> = {};

        try {
            signature.parameters.forEach(param => {
                const rawValue = inputs[param.name];
                parsedInputs[param.name] = parseValue(rawValue, param.type);
            });
            onSubmit(parsedInputs);
        } catch (err) {
            alert('Error parsing inputs: ' + (err as Error).message);
        }
    };

    const parseValue = (value: string, type: string): unknown => {
        if (type === 'int') return parseInt(value, 10);
        if (type === 'long') return parseInt(value, 10);
        if (type === 'double') return parseFloat(value);
        if (type === 'boolean') return value.toLowerCase() === 'true';
        if (type === 'String') return value;

        if (type === 'int[]') {
            return value.split(',').map(s => parseInt(s.trim(), 10)).filter(n => !isNaN(n));
        }

        if (type === 'String[]' || type.startsWith('List')) {
            return value.split(',').map(s => s.trim());
        }

        return value;
    };

    return (
        <form onSubmit={handleSubmit} className="bg-white p-4 rounded-lg shadow-sm border border-gray-200 mb-4">
            <h3 className="text-sm font-semibold text-gray-700 mb-3">Custom Input</h3>
            <div className="grid grid-cols-1 gap-3">
                {signature.parameters.map(param => (
                    <div key={param.name} className="flex flex-col">
                        <label className="text-xs text-gray-500 mb-1 font-mono">
                            {param.type} {param.name}
                        </label>
                        <input
                            type="text"
                            value={inputs[param.name] || ''}
                            onChange={(e) => handleChange(param.name, e.target.value)}
                            className="border border-gray-300 rounded px-2 py-1 text-sm font-mono focus:outline-none focus:border-blue-500"
                            placeholder={getDefaultValue(param.type)}
                        />
                    </div>
                ))}
            </div>
            <button
                type="submit"
                disabled={loading}
                className={`mt-3 w-full py-1.5 px-4 rounded text-sm font-medium text-white transition-colors ${loading ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-600 hover:bg-blue-700'
                    }`}
            >
                {loading ? 'Running...' : 'Run Custom Input'}
            </button>
        </form>
    );
};
