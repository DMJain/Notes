/** @type {import('tailwindcss').Config} */
export default {
    content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
    theme: {
        extend: {
            colors: {
                'egg-white': '#F5F5DC',
                'cream': '#FFFEF5',
                'panel-bg': '#FAFAF0',
                'border-dark': '#1A1A1A',
                'border-light': '#E5E5E5',
            },
            fontFamily: {
                sans: ['Inter', 'system-ui', 'sans-serif'],
                mono: ['JetBrains Mono', 'Menlo', 'monospace'],
            },
            boxShadow: {
                'panel': '0 1px 3px rgba(0, 0, 0, 0.08)',
            }
        },
    },
    plugins: [],
}
