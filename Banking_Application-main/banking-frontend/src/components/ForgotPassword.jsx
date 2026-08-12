import React, { useState } from 'react';
import { forgotPassword, verifyOtp, resetPassword } from '../services/api';

export default function ForgotPassword({ onBackToLogin }) {
  const [step, setStep] = useState('email');  // 'email' | 'otp' | 'password'
  const [formData, setFormData] = useState({ email: '', otp: '', password: '', confirmPassword: '' });
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const handleRequestOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setMessage('');
    
    try {
      const response = await forgotPassword(formData.email);
      setMessage('OTP sent to your email. Check your inbox.');
      setStep('otp');
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data?.error || 'Failed to send OTP');
      console.error('Forgot password error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setMessage('');
    
    try {
      const response = await verifyOtp(formData.email, formData.otp);
      setMessage('OTP verified successfully. Please enter your new password.');
      setStep('password');
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data?.error || 'Invalid OTP. Please try again.');
      console.error('OTP verification error:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    
    if (formData.password.length < 6) {
      setError('Password must be at least 6 characters');
      return;
    }
    
    setLoading(true);
    setError('');
    setMessage('');
    
    try {
      const response = await resetPassword(formData.email, formData.password);
      setMessage('Password reset successful! Redirecting to login...');
      setTimeout(() => {
        onBackToLogin();
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data?.error || 'Password reset failed. Please try again.');
      console.error('Reset password error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Reset Password</h2>
        
        {error && <div style={styles.errorBox}>{error}</div>}
        {message && <div style={styles.successBox}>{message}</div>}

        {/* Step 1: Email */}
        {step === 'email' && (
          <form onSubmit={handleRequestOtp} style={styles.form}>
            <div style={styles.formGroup}>
              <label style={styles.label}>Email Address:</label>
              <input
                type="email"
                placeholder="Enter your registered email"
                style={styles.input}
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                required
              />
            </div>
            <button 
              type="submit" 
              style={styles.button}
              disabled={loading}
            >
              {loading ? 'Sending OTP...' : 'Send OTP'}
            </button>
          </form>
        )}

        {/* Step 2: OTP Verification */}
        {step === 'otp' && (
          <form onSubmit={handleVerifyOtp} style={styles.form}>
            <div style={styles.formGroup}>
              <label style={styles.label}>Email:</label>
              <input
                type="email"
                style={{ ...styles.input, backgroundColor: '#f5f5f5' }}
                value={formData.email}
                disabled
              />
            </div>
            <div style={styles.formGroup}>
              <label style={styles.label}>Enter OTP:</label>
              <input
                type="text"
                placeholder="6-digit OTP from email"
                style={styles.input}
                value={formData.otp}
                onChange={(e) => setFormData({ ...formData, otp: e.target.value })}
                required
              />
            </div>
            <button 
              type="submit" 
              style={styles.button}
              disabled={loading}
            >
              {loading ? 'Verifying...' : 'Verify OTP'}
            </button>
          </form>
        )}

        {/* Step 3: Password Reset */}
        {step === 'password' && (
          <form onSubmit={handleResetPassword} style={styles.form}>
            <div style={styles.formGroup}>
              <label style={styles.label}>Email:</label>
              <input
                type="email"
                style={{ ...styles.input, backgroundColor: '#f5f5f5' }}
                value={formData.email}
                disabled
              />
            </div>
            <div style={styles.formGroup}>
              <label style={styles.label}>New Password:</label>
              <input
                type="password"
                placeholder="Enter new password"
                style={styles.input}
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                required
              />
            </div>
            <div style={styles.formGroup}>
              <label style={styles.label}>Confirm Password:</label>
              <input
                type="password"
                placeholder="Confirm new password"
                style={styles.input}
                value={formData.confirmPassword}
                onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
                required
              />
            </div>
            <button 
              type="submit" 
              style={styles.button}
              disabled={loading}
            >
              {loading ? 'Resetting...' : 'Reset Password'}
            </button>
          </form>
        )}

        {/* Back to Login Button */}
        <button 
          onClick={onBackToLogin}
          style={styles.backButton}
        >
          Back to Login
        </button>
      </div>
    </div>
  );
}

const styles = {
  container: {
    padding: '20px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    minHeight: '100vh',
    backgroundColor: '#f9f9f9'
  },
  card: {
    backgroundColor: 'white',
    padding: '40px',
    borderRadius: '8px',
    boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
    maxWidth: '400px',
    width: '100%'
  },
  title: {
    textAlign: 'center',
    color: '#333',
    marginBottom: '30px',
    fontSize: '24px'
  },
  form: {
    marginBottom: '20px'
  },
  formGroup: {
    marginBottom: '15px'
  },
  label: {
    display: 'block',
    marginBottom: '5px',
    color: '#555',
    fontWeight: '500',
    fontSize: '14px'
  },
  input: {
    width: '100%',
    padding: '10px',
    borderRadius: '4px',
    border: '1px solid #ddd',
    fontSize: '14px',
    boxSizing: 'border-box',
    fontFamily: 'inherit'
  },
  button: {
    width: '100%',
    padding: '12px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    fontSize: '14px',
    fontWeight: '600',
    cursor: 'pointer',
    transition: 'background-color 0.3s'
  },
  backButton: {
    width: '100%',
    padding: '10px',
    backgroundColor: '#6c757d',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    fontSize: '14px',
    fontWeight: '500',
    cursor: 'pointer',
    marginTop: '10px'
  },
  errorBox: {
    backgroundColor: '#f8d7da',
    color: '#721c24',
    padding: '12px',
    borderRadius: '4px',
    marginBottom: '15px',
    border: '1px solid #f5c6cb',
    fontSize: '14px'
  },
  successBox: {
    backgroundColor: '#d4edda',
    color: '#155724',
    padding: '12px',
    borderRadius: '4px',
    marginBottom: '15px',
    border: '1px solid #c3e6cb',
    fontSize: '14px'
  }
};
