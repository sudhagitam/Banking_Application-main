import React, { useState } from 'react';
import { loginUser } from '../services/api';
import ForgotPassword from './ForgotPassword';

export default function Login({ onLoginSuccess }) {
  const [credentials, setCredentials] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [showForgotPassword, setShowForgotPassword] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await loginUser(credentials);
      const token = response.data.token || response.data.accessToken;
      localStorage.setItem('token', token);
      onLoginSuccess();
    } catch (err) {
      setError('Invalid credentials or server error.');
    }
  };

  if (showForgotPassword) {
    return <ForgotPassword onBackToLogin={() => setShowForgotPassword(false)} />;
  }

  return (
    <div style={{ padding: '20px', maxWidth: '400px', margin: 'auto' }}>
      <h2>Banking Portal Login</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Email:</label>
          <input
            type="email"
            style={{ width: '100%', marginBottom: '10px' }}
            value={credentials.email}
            onChange={(e) => setCredentials({ ...credentials, email: e.target.value })}
            required
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            style={{ width: '100%', marginBottom: '10px' }}
            value={credentials.password}
            onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
            required
          />
        </div>
        <button type="submit" style={{ width: '100%', padding: '10px', marginBottom: '10px' }}>Login</button>
      </form>
      <a 
        href="#" 
        onClick={(e) => { e.preventDefault(); setShowForgotPassword(true); }}
        style={{ color: '#007bff', textDecoration: 'none', fontSize: '14px', display: 'block', textAlign: 'center' }}
      >
        Forgot your password?
      </a>
    </div>
  );
}