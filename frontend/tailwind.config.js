/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      fontFamily: {
        heading: ['Poppins', 'sans-serif'],
        body: ['Open Sans', 'sans-serif'],
      },
      colors: {
        canvas: {
          neutral: '#0A0A0F',
          dark: '#000000',
          light: '#1A1A2E',
        },
        accent: {
          primary: '#8B5CF6',      // 霓虹紫
          secondary: '#A78BFA',      // 浅紫
          cta: '#06B6D4',            // 霓虹青
          vibrant: '#10B981',        // 霓虹绿
          neon: '#EC4899',           // 霓虹粉
        }
      },
      animation: {
        'pattern-move': 'patternMove 20s ease-in-out infinite',
        'particle-float': 'particleFloat 15s infinite',
        'stat-glow': 'statGlow 3s ease-in-out infinite',
      },
      keyframes: {
        patternMove: {
          '0%, 100%': { backgroundPosition: '0 0, 100px 100px, 50px 50px' },
          '50%': { backgroundPosition: '100px 100px, 200px 200px, 150px 150px' },
        },
        particleFloat: {
          '0%': { transform: 'translateY(100vh) translateX(0)', opacity: '0' },
          '10%': { opacity: '1' },
          '90%': { opacity: '1' },
          '100%': { transform: 'translateY(-100vh) translateX(100px)', opacity: '0' },
        },
        statGlow: {
          '0%, 100%': { transform: 'scale(1)', opacity: '0.5' },
          '50%': { transform: 'scale(1.1)', opacity: '0.7' },
        },
      },
    },
  },
  plugins: [],
}
